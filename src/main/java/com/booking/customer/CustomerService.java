package com.booking.customer;

import com.booking.exceptions.UsernameAlreadyExistsException;
import com.booking.users.repository.User;
import com.booking.users.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {
    private CustomerRepository customerRepository;
    private UserRepository userRepository;

    public CustomerService(CustomerRepository customerRepository, UserRepository userRepository) {
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
    }

    public Customer signup(Customer customer) throws UsernameAlreadyExistsException {
        if(userAlreadyExists(customer.getUser())) throw new UsernameAlreadyExistsException();
        userRepository.save(customer.getUser());
        return customerRepository.save(customer);
    }

    private boolean userAlreadyExists(User user){
        return userRepository.findByUsername(user.getUsername()).isEmpty() ? false : true;
    }
}
