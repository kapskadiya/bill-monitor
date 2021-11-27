package com.kashyap.homeIdeas.billmonitor.constant;

/**
 * @author Kashyap Kadiya
 * @since 2021-06
 */
public enum Role {
    ADMIN, USER;

    public static Role getRole(String role) {
        if(role.equalsIgnoreCase(ADMIN.name())) {
            return ADMIN;
        }
        return USER;
    }
}
