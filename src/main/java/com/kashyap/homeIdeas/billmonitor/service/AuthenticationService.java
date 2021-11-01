package com.kashyap.homeIdeas.billmonitor.service;

import com.kashyap.homeIdeas.billmonitor.model.User;

/**
 * @author Kashyap Kadiya
 * @since 2021-06
 */
public interface AuthenticationService {
    User getLoggedInUser();
}
