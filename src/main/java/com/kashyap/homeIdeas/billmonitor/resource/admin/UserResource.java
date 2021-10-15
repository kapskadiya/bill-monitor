package com.kashyap.homeIdeas.billmonitor.resource.admin;

import com.kashyap.homeIdeas.billmonitor.dto.ApplicationResponse;
import com.kashyap.homeIdeas.billmonitor.dto.Failure;
import com.kashyap.homeIdeas.billmonitor.dto.UserDto;
import com.kashyap.homeIdeas.billmonitor.model.User;
import com.kashyap.homeIdeas.billmonitor.service.UserService;
import com.kashyap.homeIdeas.billmonitor.util.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import java.util.HashMap;
import java.util.Map;

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
    public ApplicationResponse save(@Valid @RequestBody UserDto dto) {
        final ApplicationResponse response = new ApplicationResponse();

        final User user = UserUtil.buildUser(dto);
        try {
            userService.save(user);
        } catch (IllegalArgumentException iae) {
            final Failure failure = new Failure();
            failure.setReason(iae.getMessage());
            failure.setException(iae.toString());
            response.setFailure(failure);
            response.setHttpCode(HttpStatus.BAD_REQUEST.value());
            return response;
        }
        final Map<String, Object> success = new HashMap<>();
        success.put("isSaved", true);
        success.put("message", "resource saved successfully");
        response.setSuccess(success);

        response.setHttpCode(HttpStatus.CREATED.value());
        return response;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping(value = "/update")
    public ApplicationResponse update(@RequestBody UserDto dto) {
        final ApplicationResponse response = new ApplicationResponse();

        if (StringUtils.isBlank(dto.getEmail())) {
            final Failure failure = new Failure();
            failure.setReason("Email is required.");
            response.setFailure(failure);
            response.setHttpCode(HttpStatus.BAD_REQUEST.value());
            return response;
        }

        final User user = UserUtil.buildUser(dto);
        try {
            userService.update(user);
        } catch (IllegalArgumentException iae) {
            final Failure failure = new Failure();
            failure.setReason(iae.getCause().getMessage());
            failure.setException(iae.toString());
            response.setFailure(failure);
            response.setHttpCode(HttpStatus.BAD_REQUEST.value());
            return response;
        }
        final Map<String, Object> success = new HashMap<>();
        success.put("isUpdated", true);
        success.put("message", "resource updated successfully");
        response.setSuccess(success);

        response.setHttpCode(HttpStatus.NO_CONTENT.value());
        return response;
    }


    @GetMapping(value = "/id/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable String id) {
        final UserDto dto = UserUtil.buildDto(userService.getById(id));
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping(value = "/email/{email}")
    public ResponseEntity<UserDto> getByUsername(@PathVariable String email) {
        final UserDto dto = UserUtil.buildDto(userService.getNonDeletedUserByEmail(email));
        return ResponseEntity.ok().body(dto);
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(value = "")
    public ApplicationResponse remove(@RequestParam(value = "email") String email) {
        final ApplicationResponse response = new ApplicationResponse();
        try {
            userService.removeByEmail(email);
        } catch (IllegalArgumentException iae) {
            final Failure failure = new Failure();
            failure.setReason(iae.getCause().getMessage());
            failure.setException(iae.toString());
            response.setFailure(failure);
            response.setHttpCode(HttpStatus.BAD_REQUEST.value());
            return response;
        }
        final Map<String, Object> success = new HashMap<>();
        success.put("isDeleted", true);
        success.put("message", "resource deleted successfully");
        response.setSuccess(success);

        response.setHttpCode(HttpStatus.NO_CONTENT.value());
        return response;
    }

    private Map<String, String> mandatoryFieldsCheck(UserDto dto) {
        final Map<String, String> invalidateFieldsMap = new HashMap<>();

        if (StringUtils.isBlank(dto.getFirstname())) {
            invalidateFieldsMap.put("Firstname", "Firstname is required.");
        }
        if (StringUtils.isBlank(dto.getLastname())) {
            invalidateFieldsMap.put("Lastname", "Lastname is required.");
        }
        if (StringUtils.isBlank(dto.getEmail())) {
            invalidateFieldsMap.put("Email", "Email is required.");
        }
        return invalidateFieldsMap;
    }

}
