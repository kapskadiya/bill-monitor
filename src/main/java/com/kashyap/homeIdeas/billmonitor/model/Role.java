package com.kashyap.homeIdeas.billmonitor.model;

/**
 * @author Kashyap Kadiya
 * @since 2021-06
 */
public enum Role {
    ADMIN, USER;

    public static Role getRole(String role) {
        if(role.equals(ADMIN.name())) {
            return ADMIN;
        }
        return USER;
    }
}
