package com.booking.users;

import com.booking.exceptions.PasswordMatchesWithLastThreePasswordsException;
import com.booking.exceptions.PasswordMismatchException;
import com.booking.passwordHistory.repository.PasswordHistory;
import com.booking.passwordHistory.repository.PasswordHistoryRepository;
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
    private final PasswordHistoryRepository passwordHistoryRepository;

    @Autowired
    public UserPrincipalService(UserRepository userRepository, PasswordHistoryRepository passwordHistoryRepository) {
        this.userRepository = userRepository;
        this.passwordHistoryRepository = passwordHistoryRepository;
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

        List<PasswordHistory> lastThreePasswords = passwordHistoryRepository.findRecentPasswordsByUserIdWithLimit(user.getId(), THREE.getValue());
        for (PasswordHistory password : lastThreePasswords) {
            if (password.getPasswordHistoryPK().getPassword().equals(changePasswordRequest.getNewPassword()))
                throw new PasswordMatchesWithLastThreePasswordsException("New password matches with recent three passwords");
        }

        user.setPassword(changePasswordRequest.getNewPassword());
        userRepository.save(user);
    }
}
