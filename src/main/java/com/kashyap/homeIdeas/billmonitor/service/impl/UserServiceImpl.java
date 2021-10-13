package com.kashyap.homeIdeas.billmonitor.service.impl;

import com.kashyap.homeIdeas.billmonitor.model.User;
import com.kashyap.homeIdeas.billmonitor.repostiory.UserRepository;
import com.kashyap.homeIdeas.billmonitor.service.UserService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
            throw new IllegalArgumentException("user object is null.");
        }
        userRepo.save(user);
    }

    @Override
    public boolean update(final User user) {
        if (user == null) {
            return false;
        }

        final User existingUser = this.getById(user.getEmail());
        if (existingUser == null) {
            return false;
        }

        this.fillUser(existingUser, user);
        return (userRepo.save(existingUser) != null);
    }

    @Override
    public User getById(String id) {
        validateString(id);

        final Optional<User> opUser = userRepo.findById(id);
        return opUser.orElse(new User());
    }

    @Override
    public User getByUsername(String username) {
        validateString(username);

        final Optional<User> opUser = userRepo.findByUsername(username);
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
        return null;
    }

    @Override
    public void removeById(String id) throws IOException {
        validateString(id);
        userRepo.deleteById(id);
    }

    @Override
    public boolean removeByUsername(final String username) throws IOException {
        validateString(username);
//        return userRepo.removeByUsername(username);
        return true;
    }

    @Override
    public boolean disableUser(final String username) throws IOException {
        validateString(username);
        return userRepo.disableUser(username);
    }

    @Override
    public boolean enableUser(final String username) throws IOException {
        validateString(username);
        return userRepo.enableUser(username);
    }

    private void validateString(final String str) {
        if (StringUtils.isBlank(str)) {
            log.error("Please add a valid ID. {}",str);
            throw new NullPointerException("Please add a valid ID.");
        }
    }

    @Override
    public void upsert(final User user) throws IOException {
        if (user == null) {
            return;
        }
 //       userRepo.upsert(user);
    }

    private void fillUser(final User existingUser, final User newUser) {
        if (StringUtils.isNotBlank(newUser.getFirstname())) {
            existingUser.setFirstname(newUser.getFirstname());
        }
        if (StringUtils.isNotBlank(newUser.getLastname())) {
            existingUser.setLastname(newUser.getLastname());
        }
        if (StringUtils.isNotBlank(newUser.getEmail())) {
            existingUser.setEmail(newUser.getEmail());
        }

    }


}
