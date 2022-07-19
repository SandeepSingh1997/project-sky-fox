package com.booking.users;

import com.booking.customer.repository.Customer;
import com.booking.customer.repository.CustomerRepository;
import com.booking.exceptions.CustomerNotFoundException;
import com.booking.exceptions.PasswordMatchesWithLastThreePasswordsException;
import com.booking.exceptions.PasswordMismatchException;
import com.booking.exceptions.UsernameAlreadyExistsException;
import com.booking.passwordHistory.PasswordHistoryService;
import com.booking.users.repository.User;
import com.booking.users.repository.UserRepository;
import com.booking.users.view.ChangePasswordRequest;
import com.booking.users.view.UserDetailsResponse;
import com.booking.users.view.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.booking.passwordHistory.repository.Constants.THREE;

@Service
public class UserPrincipalService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordHistoryService passwordHistoryService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CustomerRepository customerRepository;

    @Autowired
    public UserPrincipalService(UserRepository userRepository, PasswordHistoryService passwordHistoryService, BCryptPasswordEncoder bCryptPasswordEncoder, CustomerRepository customerRepository) {
        this.userRepository = userRepository;
        this.passwordHistoryService = passwordHistoryService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User savedUser = findUserByUsername(username);

        return new UserPrincipal(savedUser);
    }

    public User findUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public User add(User user) throws UsernameAlreadyExistsException {
        if (userAlreadyExists(user)) throw new UsernameAlreadyExistsException();
        String hashedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        User savedUser = userRepository.save(user);
        passwordHistoryService.add(user.getId(), hashedPassword);
        return savedUser;
    }

    private boolean userAlreadyExists(User user) {
        return userRepository.findByUsername(user.getUsername()).isPresent();
    }

    public UserDetailsResponse getUserDetailsById(Long id) throws CustomerNotFoundException {
        User user = getUserById(id);
        Customer customer = getCustomerByUserId(id);
        return new UserDetailsResponse(customer.getName(), user.getUsername(), customer.getEmail(), customer.getPhoneNumber());
    }

    public void changePassword(String username, ChangePasswordRequest changePasswordRequest) throws Exception {
        User user = findUserByUsername(username);
        if (!isMatches(changePasswordRequest.getCurrentPassword(), user.getPassword()))
            throw new PasswordMismatchException("Entered current password is not matching with existing password");

        List<String> lastThreePasswords = passwordHistoryService.findRecentPasswordsByUserId(user.getId(), THREE);
        for (String password : lastThreePasswords) {
            if (isMatches(changePasswordRequest.getNewPassword(), password))
                throw new PasswordMatchesWithLastThreePasswordsException("Entered new password matches with recent three passwords");
        }

        String hashedNewPassword = bCryptPasswordEncoder.encode(changePasswordRequest.getNewPassword());
        passwordHistoryService.add(user.getId(), hashedNewPassword);

        user.setPassword(hashedNewPassword);
        userRepository.save(user);
    }

    private boolean isMatches(String changePasswordRequest, String user) {
        return bCryptPasswordEncoder.matches(changePasswordRequest, user);
    }

    private User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private Customer getCustomerByUserId(Long id) throws CustomerNotFoundException {
        return customerRepository.findByUserId(id).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
    }
}
