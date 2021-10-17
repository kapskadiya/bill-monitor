package com.kashyap.homeIdeas.billmonitor.service;

import com.kashyap.homeIdeas.billmonitor.model.User;

public interface AuthenticationService {
    User getLoggedInUser();
}
