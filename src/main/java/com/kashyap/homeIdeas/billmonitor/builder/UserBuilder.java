package com.kashyap.homeIdeas.billmonitor.builder;

import com.kashyap.homeIdeas.billmonitor.model.User;
import com.kashyap.homeIdeas.billmonitor.model.UserRole;

public class UserBuilder {

    private final User user;

    public UserBuilder() {
        this.user = new User();
    }

    public UserBuilder setFirstname(String firstname) {
        this.user.setFirstname(firstname);
        return this;
    }

    public UserBuilder setLastname(String lastname) {
        this.user.setLastname(lastname);
        return this;
    }

    public UserBuilder setUsername(String username) {
        this.user.setUsername(username);
        return this;
    }

    public UserBuilder setPassword(String password) {
        this.user.setPassword(password);
        return this;
    }

    public UserBuilder setEmail(String email) {
        this.user.setEmail(email);
        return this;
    }

    public UserBuilder setMobileNo(long mobileNo) {
        this.user.setMobileNo(mobileNo);
        return this;
    }

    public UserBuilder setRole(UserRole role) {
        this.user.setRole(role);
        return this;
    }

    public User build() {
        return this.user;
    }

}
