package com.kashyap.homeIdeas.billmonitor.resource.admin;

import com.kashyap.homeIdeas.billmonitor.dto.ApplicationResponse;
import com.kashyap.homeIdeas.billmonitor.dto.UserDto;
import com.kashyap.homeIdeas.billmonitor.exception.BillMonitorValidationException;
import com.kashyap.homeIdeas.billmonitor.exception.NoRecordFoundException;
import com.kashyap.homeIdeas.billmonitor.model.User;
import com.kashyap.homeIdeas.billmonitor.service.UserService;
import com.kashyap.homeIdeas.billmonitor.util.UserUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/admin/user")
public class UserResource {

    private static final Logger log = LoggerFactory.getLogger(UserResource.class);

    @Autowired
    private UserService userService;

    @PreAuthorize("hasAuthority('USER') || hasAuthority('ADMIN')")
    @GetMapping(value = "/welcome")
    @ResponseStatus(HttpStatus.OK)
    public String welcome() {
        return "Welcome to the user rest module.";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = "/create")
    public ApplicationResponse create(@Valid @RequestBody UserDto dto) {
        final ApplicationResponse response = new ApplicationResponse();

        final User user = UserUtil.buildUser(dto);
        userService.save(user);

        response.setSuccess(true);
        response.setMessage("resource saved successfully");
        response.setCode(HttpStatus.CREATED.value());
        return response;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping(value = "/update")
    public ApplicationResponse update(@RequestBody UserDto dto) {
        final ApplicationResponse response = new ApplicationResponse();

        if (StringUtils.isBlank(dto.getEmail())) {
            throw new BillMonitorValidationException("email is empty");
        }

        final User user = UserUtil.buildUser(dto);
        userService.update(user);

        response.setSuccess(true);
        response.setMessage("resource updated successfully");
        response.setCode(HttpStatus.NO_CONTENT.value());
        return response;
    }


    @GetMapping(value = "/id/{id}")
    public ApplicationResponse getById(@PathVariable String id) {
        final ApplicationResponse response = new ApplicationResponse();

        final UserDto dto = UserUtil.buildDto(userService.getById(id));

        response.setSuccess(true);
        response.setMessage("Data Found");
        response.setData(dto);
        response.setCode(HttpStatus.OK.value());
        return response;
    }

    @GetMapping(value = "/email/{email}")
    public ApplicationResponse getByUsername(@PathVariable String email) {
        final ApplicationResponse response = new ApplicationResponse();

        final User user = userService.getNonDeletedUserByEmail(email);

        if (user == null) {
            throw new NoRecordFoundException("User is not found");
        }

        final UserDto dto = UserUtil.buildDto(user);

        response.setSuccess(true);
        response.setMessage("Data Found");
        response.setData(dto);
        response.setCode(HttpStatus.OK.value());
        return response;
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(value = "/delete")
    public ApplicationResponse remove(@RequestParam(value = "email") String email) {
        final ApplicationResponse response = new ApplicationResponse();

        userService.removeByEmail(email);

        response.setSuccess(true);
        response.setMessage("User deleted successfully");
        response.setCode(HttpStatus.OK.value());
        return response;
    }

    @GetMapping(value = "/getDeletedUsers")
    public ApplicationResponse getDeleteUsers() {
        final ApplicationResponse response = new ApplicationResponse();
        final List<UserDto> userList = new ArrayList<>();

        final List<User> deletedUserList = userService.getDeletedUsers();
        if (CollectionUtils.isNotEmpty(deletedUserList)) {
            userList.addAll(deletedUserList.stream().map(UserUtil::buildDto).collect(Collectors.toList()));
        }

        response.setSuccess(true);
        response.setMessage("Data Found");
        response.setData(userList);
        response.setCode(HttpStatus.OK.value());
        return response;
    }

    @GetMapping(value = "/getNonDeletedUsers")
    public ApplicationResponse getNonDeleteUsers() {
        final ApplicationResponse response = new ApplicationResponse();
        final List<UserDto> userList = new ArrayList<>();

        final List<User> nonDeletedUserList = userService.getNonDeletedUsers();
        if (CollectionUtils.isNotEmpty(nonDeletedUserList)) {
            userList.addAll(nonDeletedUserList.stream().map(UserUtil::buildDto).collect(Collectors.toList()));
        }

        response.setSuccess(true);
        response.setMessage("Data Found");
        response.setData(userList);
        response.setCode(HttpStatus.OK.value());
        return response;
    }

    @GetMapping(value = "/search")
    public ApplicationResponse search(@RequestParam(value = "keyword") String keyword) {
        final ApplicationResponse response = new ApplicationResponse();
        final List<UserDto> userList = new ArrayList<>();

        if (StringUtils.isNotBlank(keyword)) {
            final List<User> nonDeletedUserList = userService.search(keyword);
            if (CollectionUtils.isNotEmpty(nonDeletedUserList)) {
                userList.addAll(nonDeletedUserList.stream().map(UserUtil::buildDto).collect(Collectors.toList()));
            }

        }
        response.setSuccess(true);
        response.setMessage("Data Found");
        response.setData(userList);
        response.setCode(HttpStatus.OK.value());
        return response;
    }
}
