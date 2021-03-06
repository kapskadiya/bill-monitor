package com.kashyap.homeIdeas.billmonitor.builder;

import com.kashyap.homeIdeas.billmonitor.model.Role;
import com.kashyap.homeIdeas.billmonitor.model.User;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

public class UserBuilder {

    private final User user;

    public UserBuilder() {
        this.user = new User();
    }

    public UserBuilder setId(String id) {
        if (StringUtils.isNotBlank(id)) {
            this.user.setId(id);
        }
        return this;
    }

    public UserBuilder setFirstname(String firstname) {
        if (StringUtils.isNotBlank(firstname)) {
            this.user.setFirstname(firstname);
        }
        return this;
    }

    public UserBuilder setLastname(String lastname) {
        if (StringUtils.isNotBlank(lastname)) {
            this.user.setLastname(lastname);
        }
        return this;
    }

    public UserBuilder setUsername(String username) {
        if (StringUtils.isNotBlank(username)) {
            this.user.setUsername(username);
        }
        return this;
    }

    public UserBuilder setPassword(String password) {
        if (StringUtils.isNotBlank(password)) {
            this.user.setPassword(password);
        }
        return this;
    }

    public UserBuilder setEmail(String email) {
        if (StringUtils.isNotBlank(email)) {
            this.user.setEmail(email);
        }
        return this;
    }

    public UserBuilder setMobileNo(Long mobileNo) {
        if (mobileNo != null) {
            this.user.setMobileNo(mobileNo);
        }
        return this;
    }

    public UserBuilder setAuthorities(Set<Role> authorities) {
        if (CollectionUtils.isNotEmpty(authorities)) {
            this.user.setAuthorities(authorities);
        }
        return this;
    }

    public UserBuilder setEnabled(boolean enabled) {
        this.user.setEnabled(enabled);
        return this;
    }

    public User build() {
        return this.user;
    }

}
