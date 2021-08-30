package com.kashyap.homeIdeas.billmonitor.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kashyap.homeIdeas.billmonitor.model.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserDto {

    private String firstname;
    private String lastname;

    @NotEmpty
    @Size(min = 2, message = "user name should have at least 2 characters")
    private String username;

    @NotEmpty
    @Size(min = 8, message = "password should have at least 8 characters")
    private String password;

    @Email
    private String email;

    private Long mobileNo;
    private String role;
    private boolean enabled;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname.trim();
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username.trim();
    }

    @JsonIgnore
    @JsonProperty
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email.trim();
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(Long mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role.trim().toLowerCase();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
