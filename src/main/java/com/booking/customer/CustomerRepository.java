package com.booking.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query(value = "SELECT * FROM CUSTOMER WHERE USER_ID =:userId", nativeQuery = true)
    Optional<Customer> findByUserId(Long userId);
}
