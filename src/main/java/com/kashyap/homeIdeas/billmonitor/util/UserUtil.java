package com.kashyap.homeIdeas.billmonitor.util;

import com.kashyap.homeIdeas.billmonitor.builder.UserBuilder;
import com.kashyap.homeIdeas.billmonitor.dto.UserDto;
import com.kashyap.homeIdeas.billmonitor.exception.BillMonitorValidationException;
import com.kashyap.homeIdeas.billmonitor.exception.NoRecordFoundException;
import com.kashyap.homeIdeas.billmonitor.model.Role;
import com.kashyap.homeIdeas.billmonitor.model.User;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

import static com.kashyap.homeIdeas.billmonitor.constant.ApplicationConstant.DATA_INVALID;
import static com.kashyap.homeIdeas.billmonitor.constant.ApplicationConstant.DATA_NOT_PREPARED;

/**
 * @author Kashyap Kadiya
 * @since 2021-06
 */
public class UserUtil {

    public static User buildUser(UserDto userDto) {

        final UserDto dto = Optional.ofNullable(userDto)
                .orElseThrow(() -> new BillMonitorValidationException(DATA_INVALID));

        final User user = new UserBuilder()
                .setFirstname(dto.getFirstname())
                .setLastname(dto.getLastname())
                .setEmail(dto.getEmail())
                .setRole( StringUtils.isNotBlank(dto.getRole()) ? Role.getRole(dto.getRole()) : null)
                .setUserServices(dto.getServices())
                .build();

        return Optional.ofNullable(user)
                .orElseThrow(() -> new BillMonitorValidationException(DATA_NOT_PREPARED));
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
        dto.setCreatedDate(user.getCreatedDate());
        dto.setUpdatedDate(user.getUpdatedDate());
        dto.setServices(user.getServices());

        return dto;
    }

}
