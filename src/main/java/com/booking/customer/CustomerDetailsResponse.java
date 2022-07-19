package com.booking.customer;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;
@ApiModel("Customer Details Response")
public class CustomerDetailsResponse {
    @JsonProperty
    @ApiModelProperty(name = "name", value = "customer name", required = true, position = 1)
    private String name;
    @JsonProperty
    @ApiModelProperty(name = "username", value = "username", required = true, position = 2)
    private String username;
    @JsonProperty
    @ApiModelProperty(name = "email", value = "customer email", required = true, position = 3)
    private String email;
    @JsonProperty
    @ApiModelProperty(name = "mobile", value = "customer phone number", required = true, position = 4)
    private String mobile;

    public CustomerDetailsResponse(String name, String username, String email, String mobile) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.mobile = mobile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerDetailsResponse that = (CustomerDetailsResponse) o;
        return Objects.equals(name, that.name) && Objects.equals(username, that.username) && Objects.equals(email, that.email) && Objects.equals(mobile, that.mobile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, username, email, mobile);
    }

    @Override
    public String toString() {
        return "CustomerDetailsResponse{" +
                "name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }
}
