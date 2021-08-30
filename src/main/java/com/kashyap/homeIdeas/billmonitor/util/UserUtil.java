package com.kashyap.homeIdeas.billmonitor.util;

import com.kashyap.homeIdeas.billmonitor.builder.UserBuilder;
import com.kashyap.homeIdeas.billmonitor.dto.UserDto;
import com.kashyap.homeIdeas.billmonitor.model.Role;
import com.kashyap.homeIdeas.billmonitor.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

public class UserUtil {

    private final static PasswordEncoder encoder = new BCryptPasswordEncoder();

    public static User buildUser(UserDto dto) {
        final String role = "admin".equals(dto.getRole())
                ? Role.ADMIN : Role.USER;

        return new UserBuilder()
                .setId(dto.getUsername())
                .setFirstname(dto.getFirstname())
                .setLastname(dto.getLastname())
                .setEmail(dto.getEmail())
                .setUsername(dto.getUsername())
                .setPassword(encoder.encode(dto.getPassword()))
                .setMobileNo(dto.getMobileNo())
                .setAuthorities(Set.of(new Role(role)))
                .build();

    }

    public static UserDto buildDto(final User user) {
        final UserDto dto = new UserDto();
        dto.setFirstname(user.getFirstname());
        dto.setLastname(user.getLastname());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setMobileNo(user.getMobileNo());
        dto.setRole(user.getAuthorities().stream().findFirst().get().getAuthority());
        dto.setEnabled(user.isEnabled());

        return dto;
    }

}
