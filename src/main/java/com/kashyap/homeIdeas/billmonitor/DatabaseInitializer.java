package com.kashyap.homeIdeas.billmonitor;

import com.kashyap.homeIdeas.billmonitor.builder.UserBuilder;
import com.kashyap.homeIdeas.billmonitor.model.BillType;
import com.kashyap.homeIdeas.billmonitor.constant.Role;
import com.kashyap.homeIdeas.billmonitor.model.User;
import com.kashyap.homeIdeas.billmonitor.repostiory.BillTypeRepository;
import com.kashyap.homeIdeas.billmonitor.repostiory.UserRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kashyap Kadiya
 * @since 2021-06
 */
@Component
public class DatabaseInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final List<String> firstnameList = List.of("Kashyap");
    private final List<String> lastnameList = List.of("Kadiya");
    private final List<String> emailList = List.of("kapskadiya@gmail.com");
    private final List<String> roleList = List.of(Role.ADMIN.name());
    private final List<String> billTypeList = List.of("ELECTRICITY","DTH","GAS","MOBILE_RECHARGE","BROADBAND");

    private static final Logger log = LoggerFactory.getLogger(DatabaseInitializer.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private BillTypeRepository billTypeRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        final String password = "admin123";

        final List<User.Service> serviceList = new ArrayList<>();
        serviceList.add(new User.Service("DTH", "101010101010111"));

        for (int i = 0; i < emailList.size(); i++) {
            if (userRepository.existsById(emailList.get(i)+"__ADMIN_BY_SYSTEM")) {
                continue;
            }

            final User user = new UserBuilder()
                    .setId(emailList.get(i)+"__ADMIN_BY_SYSTEM")
                    .setFirstname(firstnameList.get(i))
                    .setLastname(lastnameList.get(i))
                    .setPassword(encoder.encode(password))
                    .setEmail(emailList.get(i))
                    .setRole(Role.getRole(roleList.get(i)))
                    .setUserServices(serviceList)
                    .build();

            try {
                userRepository.save(user);
            } catch (Exception e) {
                log.error("***Database Initializer*** ERROR: ",e);
            }

        }

        final List<BillType> types = IterableUtils.toList(billTypeRepository.findAll());

        if (CollectionUtils.isEmpty(types)) {
            for (String s : billTypeList) {
                billTypeRepository.save(new BillType(s, s));
            }

        }
    }
}
