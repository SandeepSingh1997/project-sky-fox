package com.booking.passwordHistory;

import com.booking.passwordHistory.repository.Constants;
import com.booking.passwordHistory.repository.PasswordHistory;
import com.booking.passwordHistory.repository.PasswordHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PasswordHistoryService {

    private PasswordHistoryRepository passwordHistoryRepository;

    @Autowired
    public PasswordHistoryService(PasswordHistoryRepository passwordHistoryRepository) {
        this.passwordHistoryRepository = passwordHistoryRepository;
    }

    public List<PasswordHistory> findRecentPasswordsByUserId(Long userId, Constants three) {
        return passwordHistoryRepository.findRecentPasswordsByUserIdWithLimit(userId, three.getValue());
    }
}
