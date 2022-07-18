package com.booking.customer;

import com.booking.exceptions.UsernameAlreadyExistsException;
import com.booking.users.UserPrincipalService;
import com.booking.users.repository.User;
import com.booking.users.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {
    private CustomerRepository customerRepository;
    private UserPrincipalService userPrincipalService;


    public CustomerService(CustomerRepository customerRepository, UserPrincipalService userPrincipalService) {
        this.customerRepository = customerRepository;
        this.userPrincipalService = userPrincipalService;
    }

    public Customer signup(Customer customer) throws UsernameAlreadyExistsException {
        userPrincipalService.add(customer.getUser());
        return customerRepository.save(customer);
    }
}
