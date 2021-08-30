package com.kashyap.homeIdeas.billmonitor.resource.usermanagement;

import com.kashyap.homeIdeas.billmonitor.dto.ApplicationResponse;
import com.kashyap.homeIdeas.billmonitor.dto.Failure;
import com.kashyap.homeIdeas.billmonitor.dto.UserDto;
import com.kashyap.homeIdeas.billmonitor.model.User;
import com.kashyap.homeIdeas.billmonitor.service.UserService;
import com.kashyap.homeIdeas.billmonitor.util.UserUtil;
import org.apache.commons.collections4.MapUtils;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/rest/usermanagement/user")
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

    @PostMapping(value = "/add")
    public ApplicationResponse save(@RequestBody UserDto dto) {

        final User user = UserUtil.buildUser(dto);
        final ApplicationResponse response = new ApplicationResponse();

        try {
            userService.save(user);
        } catch (IllegalArgumentException iae) {
            final Failure failure = new Failure();
            failure.setReason(iae.getCause().getMessage());
            failure.setException(iae.toString());
            response.setFailure(failure);
            return response;
        }
        response.setSuccess("isSaved", true);
        response.setSuccess("message", "User saved.");
        return response;
    }

    @GetMapping(value = "/id/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable String id) {
        final UserDto dto = UserUtil.buildDto(userService.getById(id));
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping(value = "/username/{username}")
    public ResponseEntity<UserDto> getByUsername(@PathVariable String username) {
        final UserDto dto = UserUtil.buildDto(userService.getByUsername(username));
        return ResponseEntity.ok().body(dto);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/disable")
    public ResponseEntity<String> disableUser(@PathParam(value = "username") String username) throws IOException {
        if (StringUtils.isBlank(username)) {
            return ResponseEntity.badRequest().body("User can't be empty");
        }
        if (!userService.disableUser(username)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user is not disable");
        }
        return ResponseEntity.ok().body("user is disable");
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/enable")
    public ResponseEntity<String> enableUser(@PathParam(value = "username") String username) throws IOException {
        if (StringUtils.isBlank(username)) {
            return ResponseEntity.badRequest().body("User can't be empty");
        }
        if (!userService.enableUser(username)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user is not enable");
        }
        return ResponseEntity.ok().body("user is enable");
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping(value = "/update")
    public ResponseEntity<String> update(@RequestBody UserDto dto) {

        final Map<String, String> invalidFieldsMap = this.mandatoryFieldsCheck(dto);
        if (MapUtils.isEmpty(invalidFieldsMap)) {
            return ResponseEntity.badRequest()
                    .body("there are multiple validation exceptions.");
        }

        final User user = UserUtil.buildUser(dto);
        final String result = userService.update(user) ? "User updated." : "User doesn't updated.";

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(value = "/username/{username}")
    public ResponseEntity<String> remove(@PathVariable String username) {
        String result = StringUtils.EMPTY;
        try {
            result = userService.removeByUsername(username) ? "User is successfully deleted." : "User is not deleted.";
        } catch (IOException e) {
            log.error("Error occur while performing deletion. (0)",e);
        }
        return ResponseEntity.ok().body(result);
    }

    private Map<String, String> mandatoryFieldsCheck(UserDto dto) {
        final Map<String, String> invalidateFieldsMap = new HashMap<>();

        if (StringUtils.isBlank(dto.getUsername())) {
            invalidateFieldsMap.put("Username", "Username is required.");
        }
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
