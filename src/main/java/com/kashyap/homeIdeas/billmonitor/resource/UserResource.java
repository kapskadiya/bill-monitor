package com.kashyap.homeIdeas.billmonitor.resource;

import com.kashyap.homeIdeas.billmonitor.dto.UserDto;
import com.kashyap.homeIdeas.billmonitor.model.User;
import com.kashyap.homeIdeas.billmonitor.model.UserRole;
import com.kashyap.homeIdeas.billmonitor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest/user")
public class UserResource {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/welcome")
    public String welcome() {
        return "Welcome to the user rest module.";
    }

    @PostMapping(value = "/save")
    public String save(@RequestBody final UserDto dto) {
        // TODO: validate dto.

        final User user = convertToUser(dto);

        return userService.save(user) ? "User saved." : "User doesn't saved.";

    }

    @GetMapping(value = "/getUser/{id}")
    public User getById(@PathVariable final String id) {
        return userService.getById(id);
    }

    private User convertToUser(final UserDto dto) {
        final User user = new User();

        user.setId(dto.getUsername());
        user.setFirstname(dto.getFirstname());
        user.setLastname(dto.getLastname());
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setMobileNo(dto.getMobileNo());
        final UserRole role = "admin".equals(dto.getRole().toLowerCase()) ? UserRole.ADMIN : UserRole.CUSTOMER;
        user.setRole(role);

        return user;
    }

}
