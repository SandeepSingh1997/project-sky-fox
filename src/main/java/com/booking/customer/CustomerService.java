package com.booking.customer;

public class CustomerService {
    private CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer signup(Customer customer) {
        return customerRepository.save(customer);
    }
}
