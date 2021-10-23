package com.kashyap.homeIdeas.billmonitor.util;

import com.kashyap.homeIdeas.billmonitor.builder.UserBuilder;
import com.kashyap.homeIdeas.billmonitor.dto.BillDto;
import com.kashyap.homeIdeas.billmonitor.dto.UserDto;
import com.kashyap.homeIdeas.billmonitor.exception.BillMonitorValidationException;
import com.kashyap.homeIdeas.billmonitor.exception.NoRecordFoundException;
import com.kashyap.homeIdeas.billmonitor.model.Role;
import com.kashyap.homeIdeas.billmonitor.model.User;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class UserUtil {

    private final static PasswordEncoder encoder = new BCryptPasswordEncoder();

    public static User buildUser(UserDto userDto) {

        final UserDto dto = Optional.ofNullable(userDto)
                .orElseThrow(() -> new BillMonitorValidationException("Given data is empty"));

        final User user = new UserBuilder()
                .setFirstname(dto.getFirstname())
                .setLastname(dto.getLastname())
                .setEmail(dto.getEmail())
                .setPassword( StringUtils.isNotBlank(dto.getPassword()) ? encoder.encode(dto.getPassword()) : null)
                .setRole( StringUtils.isNotBlank(dto.getRole()) ? Role.getRole(dto.getRole()) : null)
                .setUserServices(dto.getServices())
                .build();

        return Optional.ofNullable(user)
                .orElseThrow(() -> new BillMonitorValidationException("Data is not prepared properly"));
    }

    public static UserDto buildDto(final User fetchedUser) {

        final User user = Optional.ofNullable(fetchedUser)
                .orElseThrow(NoRecordFoundException::new);

        final UserDto dto = new UserDto();
        dto.setFirstname(user.getFirstname());
        dto.setLastname(user.getLastname());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().name());
        dto.setCreatedBy(user.getCreatedBy());
        dto.setUpdatedBy(user.getUpdatedBy());
        if (CollectionUtils.isNotEmpty(user.getServices())){
            final Map<String, String> services = user.getServices()
                    .stream()
                    .collect(Collectors.toMap(User.Service::getName, User.Service::getNumber));
            dto.setServices(services);
        }

        return dto;
    }

}
