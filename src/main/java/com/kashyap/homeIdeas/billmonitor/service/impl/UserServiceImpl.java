package com.kashyap.homeIdeas.billmonitor.service.impl;

import com.kashyap.homeIdeas.billmonitor.model.User;
import com.kashyap.homeIdeas.billmonitor.repostiory.UserRepository;
import com.kashyap.homeIdeas.billmonitor.service.UserService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepo;

    @Override
    public void save(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User object is null.");
        }

        final User existingUser = getNonDeletedUserByEmail(user.getEmail());
        if (existingUser != null) {
            throw new IllegalArgumentException("User is already existed.");
        }

        final User loggedInUser = getLoggedInUser();

        if (loggedInUser != null) {
            user.setCreatedBy(loggedInUser.getEmail());
        } else {
            user.setCreatedBy("admin@admin.com");
        }
        user.setDeleted(false);
        userRepo.save(user);
    }

    @Override
    public void update(final User user) {
        if (user != null) {
            final User existingUser = this.getNonDeletedUserByEmail(user.getEmail());
            if (existingUser != null) {
                this.fillUser(existingUser, user);
                userRepo.save(existingUser);
            }
        }
    }

    @Override
    public User getById(String id) {
        validateString(id);

        final Optional<User> opUser = userRepo.findById(id);
        return opUser.orElse(null);
    }

    @Override
    public List<User> getByFirstname(final String firstname) {
        validateString(firstname);

        final List<User> userList = userRepo.findByFirstname(firstname);
        return userList == null ? new ArrayList<>() : userList ;
    }

    @Override
    public List<User> getByLastname(final String lastname) {
        validateString(lastname);

        final List<User> userList = userRepo.findByLastname(lastname);
        return userList == null ? new ArrayList<>() : userList;
    }

    @Override
    public List<User> search(final String keyword) {
        validateString(keyword);

        final List<User> userList = userRepo.findByFirstnameContainingOrLastnameContainingOrEmailContaining(keyword, keyword, keyword);
        return userList == null ? new ArrayList<>() : userList;
    }

    @Override
    public void removeById(String id) {
        validateString(id);
        userRepo.deleteById(id);
    }

    @Override
    public void removeByEmail(final String email) {
        validateString(email);
        final User existingUser = this.getNonDeletedUserByEmail(email);
        if (existingUser != null) {
            final User loggedInUser = getLoggedInUser();
            if (loggedInUser != null) {
                existingUser.setUpdatedBy(loggedInUser.getEmail());
            } else {
                existingUser.setUpdatedBy("admin@admin.com");
            }
            existingUser.setDeleted(true);
            userRepo.save(existingUser);
        }
    }

    @Override
    public User getNonDeletedUserByEmail(String email) {
        validateString(email);
        final List<User> userList = userRepo.findByEmailAndIsDeleted(email, false);
        if (CollectionUtils.isNotEmpty(userList)) {
            return userList.get(0);
        }
        return null;
    }

    @Override
    public List<User> getDeletedUsers() {
        return userRepo.findByIsDeleted(true);
    }

    @Override
    public List<User> getNonDeletedUsers() {
        return userRepo.findByIsDeleted(false);
    }

    private void validateString(final String str) {
        if (StringUtils.isBlank(str)) {
            log.error("Please add a valid ID. {}",str);
            throw new NullPointerException("Please add a valid ID.");
        }
    }

    private void fillUser(final User existingUser, final User newUser) {
        final User loggedInUser = getLoggedInUser();
        if (StringUtils.isNotBlank(newUser.getFirstname())) {
            existingUser.setFirstname(newUser.getFirstname());
        }
        if (StringUtils.isNotBlank(newUser.getLastname())) {
            existingUser.setLastname(newUser.getLastname());
        }
        if (StringUtils.isNotBlank(newUser.getEmail())) {
            existingUser.setEmail(newUser.getEmail());
        }
        if (loggedInUser != null) {
            existingUser.setUpdatedBy(loggedInUser.getEmail());
        }

    }

    private User getLoggedInUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return  (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
       return null;
    }


}
