package com.booking.customer;

import com.booking.users.User;
import com.booking.utilities.serializers.date.DateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Objects;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    @ApiModelProperty(name = "id", value = "The customer id", example = "0", position = 1)
    private Long id;

    @Column(nullable = false)
    @JsonProperty
    @NotBlank(message = "Customer name must be provided")
    @ApiModelProperty(name = "customer name", value = "Name of customer", required = true, example = "Customer name", position = 2)
    private String name;

    @Column(nullable = false)
    @JsonProperty
    @NotBlank(message = "Customer email must be provided")
    @Pattern(regexp = "(a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$)", message = "Enter valid email")
    @ApiModelProperty(name = "customer email", value = "Email of customer", required = true, example = "xyz@abc.com", position = 3)
    private String email;

    @Column(name = "phone_number", nullable = false)
    @JsonProperty
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Phone number must have exactly 10 digits")
    @NotBlank(message = "Phone number must be provided")
    @ApiModelProperty(name = "phone number", value = "Phone number of the customer", required = true, example = "9933221100", position = 3)
    private String phoneNumber;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    @NotNull(message = "User must be provided")
    private User user;

    public Customer(String name, String email, String phoneNumber, User user) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.user = user;
    }

    public Customer() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public User getUser() {
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return id.equals(customer.id) && name.equals(customer.name) && email.equals(customer.email) && phoneNumber.equals(customer.phoneNumber) && user.equals(customer.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, phoneNumber, user);
    }
}
