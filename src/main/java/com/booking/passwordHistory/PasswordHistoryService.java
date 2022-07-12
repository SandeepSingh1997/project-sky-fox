package com.booking.passwordHistory;

import com.booking.passwordHistory.repository.Constants;
import com.booking.passwordHistory.repository.PasswordHistory;
import com.booking.passwordHistory.repository.PasswordHistoryPK;
import com.booking.passwordHistory.repository.PasswordHistoryRepository;
import com.booking.users.User;
import com.booking.users.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
public class PasswordHistoryService {

    private PasswordHistoryRepository passwordHistoryRepository;

    private UserRepository userRepository;

    public PasswordHistoryService(PasswordHistoryRepository passwordHistoryRepository, UserRepository userRepository) {
        this.passwordHistoryRepository = passwordHistoryRepository;
        this.userRepository = userRepository;
    }

    public List<String> findRecentPasswordsByUserId(Long userId, Constants three) {
        return passwordHistoryRepository.findRecentPasswordsByUserIdWithLimit(userId, three.getValue());
    }

    public PasswordHistory add(Long userId, String password) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with this user id"));

        PasswordHistoryPK passwordHistoryPK = new PasswordHistoryPK(user, password);
        Timestamp createdAt = Timestamp.from(Instant.now());
        PasswordHistory passwordHistory = new PasswordHistory(passwordHistoryPK, createdAt);
        return passwordHistoryRepository.save(passwordHistory);
    }
}
