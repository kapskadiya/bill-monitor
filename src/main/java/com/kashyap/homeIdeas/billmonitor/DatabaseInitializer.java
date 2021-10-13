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
    private final List<String> emailList = List.of("kapskadiya@gmail.com", "sunilsathwara@gmail.com");
    private final List<String> roleList = List.of(Role.ADMIN.name(), Role.USER.name());

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {

        for (int i = 0; i < emailList.size(); i++) {
            final String password = "abc123_";
            User user = new UserBuilder()
                    .setId(emailList.get(i))
                    .setFirstname(firstnameList.get(i))
                    .setLastname(lastnameList.get(i))
                    .setPassword(encoder.encode(password))
                    .setEmail(emailList.get(i))
                    .setRole(Role.getRole(roleList.get(i)))
                    .build();

            userService.save(user);
        }
    }
}
