package com.kashyap.homeIdeas.billmonitor.dto;

/**
 * @author Kashyap Kadiya
 * @since 2021-06
 */
public class AuthRequest {

    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
