package com.booking.users.repository;

import com.booking.roles.repository.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "usertable")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty
    @NotBlank(message = "User name must be provided")
    @Column(nullable = false, unique = true)
    @ApiModelProperty(name = "username", value = "Name of user (must be unique)", required = true, example = "user_name", position = 1)
    private String username;

    @JsonProperty
    @NotBlank(message = "Password name must be provided")
    @Column(nullable = false)
    @ApiModelProperty(name = "password", value = "Password of the user", required = true, example = "password", position = 2)
    private String password;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "role_id")
    private Role role;

    public User() {
    }

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
