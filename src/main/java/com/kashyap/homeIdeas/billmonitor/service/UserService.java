package com.kashyap.homeIdeas.billmonitor.service;

import com.kashyap.homeIdeas.billmonitor.model.User;

import java.util.List;

public interface UserService {

    void save(User user);

    void update(User user);

    User getById(String id);

    List<User> getByFirstname(String firstname);

    List<User> getByLastname(String lastname);

    List<User> search(String keyword);

    void removeById(String id);

    void removeByEmail(String email);

    User getNonDeletedUserByEmail(String email);
}
