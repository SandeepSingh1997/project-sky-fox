package com.booking.passwordHistory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PasswordHistoryRepository extends JpaRepository<PasswordHistory, PasswordHistoryPK> {

    @Query(value = "SELECT * FROM PASSWORD_HISTORY WHERE USER_ID =:userId ORDER BY CREATED_AT DESC LIMIT :limit", nativeQuery = true)
    List<PasswordHistory> findRecentPasswordsByUserIdWithLimit(Long userId, int limit);
}
