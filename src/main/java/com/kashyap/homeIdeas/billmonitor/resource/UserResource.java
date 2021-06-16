package com.kashyap.homeIdeas.billmonitor.resource;

import com.kashyap.homeIdeas.billmonitor.builder.UserBuilder;
import com.kashyap.homeIdeas.billmonitor.dto.UserDto;
import com.kashyap.homeIdeas.billmonitor.model.User;
import com.kashyap.homeIdeas.billmonitor.model.UserRole;
import com.kashyap.homeIdeas.billmonitor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest/user")
public class UserResource {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserService userService;

    @GetMapping(value = "/welcome")
    public String welcome() {
        return "Welcome to the user rest module.";
    }

    @PostMapping(value = "/save")
    public String save(@RequestBody final UserDto dto) {
        // TODO: validate dto.

        final UserRole role = "admin".equals(dto.getRole().toLowerCase()) ? UserRole.ADMIN : UserRole.CUSTOMER;

        final User user = new UserBuilder()
                .setFirstname(dto.getFirstname())
                .setLastname(dto.getLastname())
                .setEmail(dto.getEmail())
                .setUsername(dto.getUsername())
                .setPassword(encoder.encode(dto.getPassword()))
                .setMobileNo(dto.getMobileNo())
                .setRole(role)
                .build();

//        final User user = convertToUser(dto);

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
        user.setPassword(encoder.encode(dto.getPassword()));
        user.setMobileNo(dto.getMobileNo());
        final UserRole role = "admin".equals(dto.getRole().toLowerCase()) ? UserRole.ADMIN : UserRole.CUSTOMER;
        user.setRole(role);

        return user;
    }

}
