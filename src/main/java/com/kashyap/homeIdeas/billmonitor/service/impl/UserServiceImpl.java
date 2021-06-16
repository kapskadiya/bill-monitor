package com.kashyap.homeIdeas.billmonitor.service.impl;

import com.kashyap.homeIdeas.billmonitor.model.User;
import com.kashyap.homeIdeas.billmonitor.repostiory.UserRepository;
import com.kashyap.homeIdeas.billmonitor.service.UserService;
import org.apache.commons.lang3.StringUtils;
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

    @Autowired
    private UserRepository userRepo;

    @Override
    public boolean save(User user) {

        if (user == null) {
            return false;
        }

        return (userRepo.save(user) != null);
    }

    @Override
    public boolean update(User user) {

        if (user == null) {
            return false;
        }

        // TODO: add logic to fill up the empty values.

        return (userRepo.save(user) != null);
    }

    @Override
    public boolean partialUpdate(User user) {
        return false;
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
        return opUser.orElse(new User());
    }

    @Override
    public List<User> getByFirstname(String firstname) {
        validateString(firstname);

        final List<User> userList = userRepo.findByFirstname(firstname);

        return userList == null ? new ArrayList<>() : userList ;
    }

    @Override
    public List<User> getByLastname(String lastname) {
        validateString(lastname);

        final List<User> userList = userRepo.findByLastname(lastname);

        return userList == null ? new ArrayList<>() : userList;
    }

    @Override
    public List<User> search(String keyword) {
        return null;
    }

    @Override
    public boolean removeById(String id) throws IOException {
        validateString(id);
        return userRepo.removeById(id);
    }

    @Override
    public boolean removeByUsername(String username) throws IOException {
        validateString(username);
        return userRepo.removeByUsername(username);
    }

    private void validateString(final String str) {
        if (StringUtils.isBlank(str)) {
            throw new NullPointerException("Please add a valid ID.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException(String.format("User with username - %s, not found", username)));
    }
}
