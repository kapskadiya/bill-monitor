package com.kashyap.homeIdeas.billmonitor.service.impl;

import com.kashyap.homeIdeas.billmonitor.exception.NoRecordFoundException;
import com.kashyap.homeIdeas.billmonitor.model.User;
import com.kashyap.homeIdeas.billmonitor.service.AuthenticationService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.kashyap.homeIdeas.billmonitor.constant.ApplicationConstant.USER_NOT_FOUND;

/**
 * @author Kashyap Kadiya
 * @since 2021-06
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Override
    public User getLoggedInUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = null;
        if (authentication != null) {
            final Object auth = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (auth instanceof User) {
                loggedInUser = (User) auth;
            } else {
                final User dummyUser = new User();
                dummyUser.setEmail("application@billmonitor.com");
                loggedInUser = dummyUser;
            }
        }

        return Optional.ofNullable(loggedInUser)
                .orElseThrow(() -> new NoRecordFoundException(USER_NOT_FOUND));
    }
}
