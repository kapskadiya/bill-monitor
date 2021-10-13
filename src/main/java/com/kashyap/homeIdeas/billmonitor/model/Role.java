package com.kashyap.homeIdeas.billmonitor.model;

public enum Role {
    ADMIN, USER;

    public static Role getRole(String role) {
        if(role.equals(ADMIN.name())) {
            return ADMIN;
        }
        return USER;
    }
}
