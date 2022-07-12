package com.booking.users;

import com.booking.exceptions.PasswordMatchesWithLastThreePasswordsException;
import com.booking.exceptions.PasswordMismatchException;
import com.booking.passwordHistory.PasswordHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.booking.passwordHistory.repository.Constants.THREE;

@Service
public class UserPrincipalService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordHistoryService passwordHistoryService;

    @Autowired
    public UserPrincipalService(UserRepository userRepository, PasswordHistoryService passwordHistoryService) {
        this.userRepository = userRepository;
        this.passwordHistoryService = passwordHistoryService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User savedUser = findUserByUsername(username);

        return new UserPrincipal(savedUser);
    }

    public User findUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public void changePassword(String username, ChangePasswordRequest changePasswordRequest) throws Exception {
        User user = findUserByUsername(username);
        if (!user.getPassword().equals(changePasswordRequest.getCurrentPassword()))
            throw new PasswordMismatchException("Entered current password is not matching with existing password");

        List<String> lastThreePasswords = passwordHistoryService.findRecentPasswordsByUserId(user.getId(), THREE);
        for (String password : lastThreePasswords) {
            if (password.equals(changePasswordRequest.getNewPassword()))
                throw new PasswordMatchesWithLastThreePasswordsException("Entered new password matches with recent three passwords");
        }

        passwordHistoryService.add(user.getId(), changePasswordRequest.getNewPassword());

        user.setPassword(changePasswordRequest.getNewPassword());
        userRepository.save(user);
    }
}
