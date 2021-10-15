package com.kashyap.homeIdeas.billmonitor.util;

import com.kashyap.homeIdeas.billmonitor.builder.UserBuilder;
import com.kashyap.homeIdeas.billmonitor.dto.UserDto;
import com.kashyap.homeIdeas.billmonitor.model.Role;
import com.kashyap.homeIdeas.billmonitor.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

public class UserUtil {

    private final static PasswordEncoder encoder = new BCryptPasswordEncoder();

    public static User buildUser(UserDto dto) {
        return new UserBuilder()
                .setFirstname(dto.getFirstname())
                .setLastname(dto.getLastname())
                .setEmail(dto.getEmail())
                .setPassword( StringUtils.isNotBlank(dto.getPassword()) ? encoder.encode(dto.getPassword()) : null)
                .setRole( StringUtils.isNotBlank(dto.getRole()) ? Role.getRole(dto.getRole()) : null)
                .build();
    }

    public static UserDto buildDto(final User user) {
        final UserDto dto = new UserDto();
        dto.setFirstname(user.getFirstname());
        dto.setLastname(user.getLastname());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().name());
        dto.setCreatedBy(user.getCreatedBy());
        dto.setUpdatedBy(user.getUpdatedBy());

        return dto;
    }

}
