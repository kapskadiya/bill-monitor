package com.kashyap.homeIdeas.billmonitor.model;

import org.springframework.security.core.GrantedAuthority;

public class Role implements GrantedAuthority {

    public static final String ADMIN = "ADMIN";
    public static final String USER = "USER";

    private String authority;

    public Role() {
        //Default arg consturctor
    }

    public Role(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
