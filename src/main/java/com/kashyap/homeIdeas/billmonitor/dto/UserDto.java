package com.kashyap.homeIdeas.billmonitor.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kashyap.homeIdeas.billmonitor.model.User;

public class UserDto {

    private String firstname;
    private String lastname;
    private String username;
    private String password;
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

    public UserDto buildDto(final User user) {
        this.setFirstname(user.getFirstname());
        this.setLastname(user.getLastname());
        this.setUsername(user.getUsername());
        this.setEmail(user.getEmail());
        this.setMobileNo(user.getMobileNo());
        this.setRole(user.getAuthorities().stream().findFirst().get().getAuthority());
        this.setEnabled(user.isEnabled());

        return this;
    }
}
