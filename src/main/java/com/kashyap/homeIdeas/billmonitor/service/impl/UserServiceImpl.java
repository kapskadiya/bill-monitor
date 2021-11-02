package com.kashyap.homeIdeas.billmonitor.service.impl;

import com.kashyap.homeIdeas.billmonitor.exception.BillMonitorValidationException;
import com.kashyap.homeIdeas.billmonitor.exception.NoRecordFoundException;
import com.kashyap.homeIdeas.billmonitor.model.User;
import com.kashyap.homeIdeas.billmonitor.repostiory.UserRepository;
import com.kashyap.homeIdeas.billmonitor.service.AuthenticationService;
import com.kashyap.homeIdeas.billmonitor.service.UserService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.kashyap.homeIdeas.billmonitor.constant.ApplicationConstant.DATA_INVALID;
import static com.kashyap.homeIdeas.billmonitor.constant.ApplicationConstant.USER_EXIST;
import static com.kashyap.homeIdeas.billmonitor.constant.ApplicationConstant.USER_NOT_FOUND;

/**
 * @author Kashyap Kadiya
 * @since 2021-06
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private AuthenticationService authService;

    @Override
    public void save(User user) {

        if (isExistByEmail(user.getEmail())) {
            throw new BillMonitorValidationException(USER_EXIST);
        }

        final User loggedInUser = authService.getLoggedInUser();
        user.setCreatedBy(loggedInUser.getEmail());
        user.setCreatedDate(new Date());
        user.setDeleted(false);

        userRepo.save(user);
    }

    @Override
    public void update(final User user) {
        final User existingUser = this.getNonDeletedUserByEmail(user.getEmail());
        if (existingUser == null) {
            throw new NoRecordFoundException(USER_NOT_FOUND);
        }

        this.fillUser(existingUser, user);
        userRepo.save(existingUser);
    }

    @Override
    public User getById(String id) {
        validateString(id);

        final Optional<User> opUser = userRepo.findById(id);
        return opUser.orElseThrow(NoRecordFoundException::new);
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
        if (existingUser == null) {
            throw new NoRecordFoundException(USER_NOT_FOUND);
        }

        final User loggedInUser = authService.getLoggedInUser();

        existingUser.setUpdatedBy(loggedInUser.getEmail());
        existingUser.setUpdatedDate(new Date());
        existingUser.setDeleted(true);

        userRepo.save(existingUser);
    }

    @Override
    public User getNonDeletedUserByEmail(String email) {
        validateString(email);
        final List<User> userList = userRepo.findByEmailAndIsDeleted(email, false);
        if (CollectionUtils.isEmpty(userList)) {
            return null;
        }
        return userList.get(0);
    }

    @Override
    public boolean isExistByEmail(String email) {
        validateString(email);
        final User user = getNonDeletedUserByEmail(email);
        return Optional.ofNullable(user).isPresent();
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
            throw new BillMonitorValidationException(DATA_INVALID+ " Data:"+str);
        }
    }

    private void fillUser(final User existingUser, final User newUser) {
        final User loggedInUser = authService.getLoggedInUser();
        if (StringUtils.isNotBlank(newUser.getFirstname())) {
            existingUser.setFirstname(newUser.getFirstname());
        }
        if (StringUtils.isNotBlank(newUser.getLastname())) {
            existingUser.setLastname(newUser.getLastname());
        }
        if (StringUtils.isNotBlank(newUser.getEmail())) {
            existingUser.setEmail(newUser.getEmail());
        }
        if (CollectionUtils.isNotEmpty(newUser.getServices())) {
            existingUser.setServices(newUser.getServices());
        }
        existingUser.setUpdatedBy(loggedInUser.getEmail());
        existingUser.setUpdatedDate(new Date());
    }

}
