package com.kashyap.homeIdeas.billmonitor.service;

import com.kashyap.homeIdeas.billmonitor.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.util.List;

public interface UserService extends UserDetailsService {

    boolean save(final User user);

    boolean update(final User user);

    boolean partialUpdate(final User user);

    User getById(final String id);

    User getByUsername(final String username);

    List<User> getByFirstname(final String firstname);

    List<User> getByLastname(final String lastname);

    List<User> search(final String keyword);

    boolean removeById(final String id) throws IOException;

    boolean removeByUsername(final String username) throws IOException;

}
