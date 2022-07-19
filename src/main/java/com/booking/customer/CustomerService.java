package com.booking.customer;

import com.booking.exceptions.CustomerNotFoundException;
import com.booking.exceptions.UsernameAlreadyExistsException;
import com.booking.users.UserPrincipalService;
import org.springframework.stereotype.Service;

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

    public Customer getCustomerByUserId(Long userId) throws CustomerNotFoundException {
        return customerRepository.findByUserId(userId).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
    }
}