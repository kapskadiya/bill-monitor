package com.kashyap.homeIdeas.billmonitor;

import com.kashyap.homeIdeas.billmonitor.builder.UserBuilder;
import com.kashyap.homeIdeas.billmonitor.model.Role;
import com.kashyap.homeIdeas.billmonitor.model.User;
import com.kashyap.homeIdeas.billmonitor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class DatabaseInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final List<String> firstnameList = List.of("Kashyap", "Sunil");
    private final List<String> lastnameList = List.of("Kadiya", "Sathwara");
    private final List<String> usernameList = List.of("kashyapkadiya", "sunilsathwara");
    private final List<String> emailList = List.of("kapskadiya@gmail.com", "sunilsathwara@gmail.com");
    private final List<String> roleList = List.of(Role.ADMIN, Role.USER);
    private final List<Long> mobileNoList = List.of(9925035402L, 9925035401L);
    private final String password = "abc123_";

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {

        for (int i = 0; i < usernameList.size(); i++) {
            User user = new UserBuilder()
                    .setId(usernameList.get(i))
                    .setFirstname(firstnameList.get(i))
                    .setLastname(lastnameList.get(i))
                    .setUsername(usernameList.get(i))
                    .setPassword(encoder.encode(password))
                    .setEmail(emailList.get(i))
                    .setMobileNo(mobileNoList.get(i))
                    .setAuthorities(Set.of(new Role(roleList.get(i))))
                    .setEnabled(true)
                    .build();

            userService.save(user);
        }
    }
}
