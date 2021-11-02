package com.kashyap.homeIdeas.billmonitor.resource.admin;

import com.kashyap.homeIdeas.billmonitor.dto.ApplicationResponse;
import com.kashyap.homeIdeas.billmonitor.dto.UserDto;
import com.kashyap.homeIdeas.billmonitor.exception.BillMonitorValidationException;
import com.kashyap.homeIdeas.billmonitor.exception.NoRecordFoundException;
import com.kashyap.homeIdeas.billmonitor.model.User;
import com.kashyap.homeIdeas.billmonitor.service.UserService;
import com.kashyap.homeIdeas.billmonitor.util.UserUtil;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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

import static com.kashyap.homeIdeas.billmonitor.constant.ApplicationConstant.DATA_FOUND;
import static com.kashyap.homeIdeas.billmonitor.constant.ApplicationConstant.DATA_INVALID;
import static com.kashyap.homeIdeas.billmonitor.constant.ApplicationConstant.KEYWORD;
import static com.kashyap.homeIdeas.billmonitor.constant.ApplicationConstant.RESOURCE_SAVED_SUCCESSFULLY;
import static com.kashyap.homeIdeas.billmonitor.constant.ApplicationConstant.RESOURCE_UPDATED_SUCCESSFULLY;
import static com.kashyap.homeIdeas.billmonitor.constant.ApplicationConstant.USER_DELETED_SUCCESSFULLY;
import static com.kashyap.homeIdeas.billmonitor.constant.ApplicationConstant.USER_NOT_FOUND;

/**
 * This is the User resource which can help to manage user related operation. like Create, Update, View, and Delete
 * @author Kashyap Kadiya
 * @since 2021-06
 */
@RestController
@RequestMapping("/rest/admin/user")
public class UserResource {

    @Autowired
    private UserService userService;

    @PreAuthorize("hasAuthority('USER') || hasAuthority('ADMIN')")
    @GetMapping(value = "/welcome")
    @ResponseStatus(HttpStatus.OK)
    public String welcome() {
        return "Welcome to the user rest module.";
    }

    @Operation(summary = "Create new user")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = "/create")
    public ApplicationResponse create(@Valid @RequestBody UserDto dto) {
        final ApplicationResponse response = new ApplicationResponse();

        final User user = UserUtil.buildUser(dto);
        userService.save(user);

        response.setSuccess(true);
        response.setMessage(RESOURCE_SAVED_SUCCESSFULLY);
        response.setCode(HttpStatus.CREATED.value());
        return response;
    }

    @Operation(summary = "Update existing user")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping(value = "/update")
    public ApplicationResponse update(@RequestBody UserDto dto) {
        final ApplicationResponse response = new ApplicationResponse();

        if (StringUtils.isBlank(dto.getEmail())) {
            throw new BillMonitorValidationException(DATA_INVALID+" Data:"+dto.getEmail());
        }

        final User user = UserUtil.buildUser(dto);
        userService.update(user);

        response.setSuccess(true);
        response.setMessage(RESOURCE_UPDATED_SUCCESSFULLY);
        response.setCode(HttpStatus.NO_CONTENT.value());
        return response;
    }

    @Operation(summary = "Get user by unique id")
    @GetMapping(value = "/id/{id}")
    public ApplicationResponse getById(@PathVariable String id) {
        final ApplicationResponse response = new ApplicationResponse();

        final UserDto dto = UserUtil.buildDto(userService.getById(id));

        response.setSuccess(true);
        response.setMessage(DATA_FOUND);
        response.setData(dto);
        response.setCode(HttpStatus.OK.value());
        return response;
    }

    @Operation(summary = "Get user by email id")
    @GetMapping(value = "/email/{email}")
    public ApplicationResponse getByUsername(@PathVariable String email) {
        final ApplicationResponse response = new ApplicationResponse();

        final User user = userService.getNonDeletedUserByEmail(email);

        if (user == null) {
            throw new NoRecordFoundException(USER_NOT_FOUND+" Email: "+email);
        }

        final UserDto dto = UserUtil.buildDto(user);

        response.setSuccess(true);
        response.setMessage(DATA_FOUND);
        response.setData(dto);
        response.setCode(HttpStatus.OK.value());
        return response;
    }

    @Operation(summary = "Delete user from the system")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(value = "/delete")
    public ApplicationResponse remove(@RequestParam(value = "email") String email) {
        final ApplicationResponse response = new ApplicationResponse();

        userService.removeByEmail(email);

        response.setSuccess(true);
        response.setMessage(USER_DELETED_SUCCESSFULLY);
        response.setCode(HttpStatus.OK.value());
        return response;
    }

    @Operation(summary = "Get all deleted users", hidden = true)
    @GetMapping(value = "/getDeletedUsers")
    public ApplicationResponse getDeleteUsers() {
        final ApplicationResponse response = new ApplicationResponse();
        final List<UserDto> userList = new ArrayList<>();

        final List<User> deletedUserList = userService.getDeletedUsers();
        if (CollectionUtils.isNotEmpty(deletedUserList)) {
            userList.addAll(deletedUserList.stream().map(UserUtil::buildDto).collect(Collectors.toList()));
        }

        response.setSuccess(true);
        response.setMessage(DATA_FOUND);
        response.setData(userList);
        response.setCode(HttpStatus.OK.value());
        return response;
    }

    @Operation(summary = "Get all non deleted users", hidden = true)
    @GetMapping(value = "/getNonDeletedUsers")
    public ApplicationResponse getNonDeleteUsers() {
        final ApplicationResponse response = new ApplicationResponse();
        final List<UserDto> userList = new ArrayList<>();

        final List<User> nonDeletedUserList = userService.getNonDeletedUsers();
        if (CollectionUtils.isNotEmpty(nonDeletedUserList)) {
            userList.addAll(nonDeletedUserList.stream().map(UserUtil::buildDto).collect(Collectors.toList()));
        }

        response.setSuccess(true);
        response.setMessage(DATA_FOUND);
        response.setData(userList);
        response.setCode(HttpStatus.OK.value());
        return response;
    }

    @Operation(summary = "Search user by keyword")
    @GetMapping(value = "/search")
    public ApplicationResponse search(@RequestParam(value = KEYWORD) String keyword) {
        final ApplicationResponse response = new ApplicationResponse();
        final List<UserDto> userList = new ArrayList<>();

        if (StringUtils.isNotBlank(keyword)) {
            final List<User> nonDeletedUserList = userService.search(keyword);
            if (CollectionUtils.isNotEmpty(nonDeletedUserList)) {
                userList.addAll(nonDeletedUserList.stream().map(UserUtil::buildDto).collect(Collectors.toList()));
            }

        }
        response.setSuccess(true);
        response.setMessage(DATA_FOUND);
        response.setData(userList);
        response.setCode(HttpStatus.OK.value());
        return response;
    }
}
