package com.kashyap.homeIdeas.billmonitor.builder;

import com.kashyap.homeIdeas.billmonitor.constant.Role;
import com.kashyap.homeIdeas.billmonitor.model.User;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * This is the user builder which can help to build the User object using chain pattern.
 * @author Kashyap Kadiya
 * @since 2021-06
 */
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

    public UserBuilder setRole(Role role) {
        if (role != null) {
            this.user.setRole(role);
        }
        return this;
    }

    public UserBuilder setUserServices(List<User.Service> services) {
        if(CollectionUtils.isNotEmpty(services)) {
            this.user.setServices(services);
        }
        return this;
    }

    public User build() {
        return this.user;
    }

}
