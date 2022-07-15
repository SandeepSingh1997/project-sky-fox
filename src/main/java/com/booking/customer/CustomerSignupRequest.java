package com.booking.customer;

import com.booking.roles.repository.Role;
import com.booking.users.repository.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

public class CustomerSignupRequest {
    @JsonProperty
    @ApiModelProperty(name = "name", value = "customer  name", required = true, position = 1)
    private String name;

    @JsonProperty
    @ApiModelProperty(name = "email", value = "customer  email", required = true, position = 2)
    private String email;

    @JsonProperty
    @ApiModelProperty(name = "phoneNumber", value = "customer  phone number", required = true, position = 3)
    private String phoneNumber;

    @JsonProperty
    @ApiModelProperty(name = "username", value = "customer  username", required = true, position = 4)
    private String username;

    @JsonProperty
    @ApiModelProperty(name = "password", value = "customer  password", required = true, position = 5)
    private String password;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public CustomerSignupRequest() {
    }

    public CustomerSignupRequest(String name, String email, String phoneNumber, String username, String password) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.password = password;
    }

    public Customer getCustomer() {
        return new Customer(this.name, this.email, this.phoneNumber, new User(this.username, this.password, new Role(2L,"Customer")));
    }
}
