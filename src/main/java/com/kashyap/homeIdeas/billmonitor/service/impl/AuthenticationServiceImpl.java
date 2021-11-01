package com.kashyap.homeIdeas.billmonitor.service.impl;

import com.kashyap.homeIdeas.billmonitor.exception.NoRecordFoundException;
import com.kashyap.homeIdeas.billmonitor.model.User;
import com.kashyap.homeIdeas.billmonitor.service.AuthenticationService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Kashyap Kadiya
 * @since 2021-06
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Override
    public User getLoggedInUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final User loggedInUser = (authentication != null)
                ? (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()
                : null;

        return Optional.ofNullable(loggedInUser)
                .orElseThrow(() -> new NoRecordFoundException("Logged-in user is not found"));
    }
}
