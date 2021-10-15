package com.kashyap.homeIdeas.billmonitor.resource.admin;

import com.kashyap.homeIdeas.billmonitor.config.security.JWTTokenUtil;
import com.kashyap.homeIdeas.billmonitor.dto.ApplicationResponse;
import com.kashyap.homeIdeas.billmonitor.dto.AuthRequest;
import com.kashyap.homeIdeas.billmonitor.dto.Failure;
import com.kashyap.homeIdeas.billmonitor.model.User;
import com.kashyap.homeIdeas.billmonitor.service.UserService;
import com.kashyap.homeIdeas.billmonitor.util.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/rest/admin/auth")
public class AuthResource {

    private static final Logger log = LoggerFactory.getLogger(AuthResource.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ApplicationResponse login(@RequestBody AuthRequest authRequest) {
        final ApplicationResponse response = new ApplicationResponse();

        final User user = userService.getNonDeletedUserByEmail(authRequest.getEmail());

        if (user == null) {
            final Failure failure = new Failure();
            failure.setReason("User not exist in the system");
            response.setFailure(failure);

            return response;
        }

        try {
            final UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword());
            final Authentication authentication = authenticationManager.authenticate(token);

            final User loggedInUser = (User) authentication.getPrincipal();

            final Map<String, Object> success = new HashMap<>();
            success.put("token", jwtTokenUtil.generateAccessToken(loggedInUser));
            success.put("user", UserUtil.buildDto(loggedInUser));
            response.setSuccess(success);

            return response;
        } catch (BadCredentialsException bce) {
            log.error("Error occur while log-in. {}", bce.getMessage());
            final Failure failure = new Failure();
            failure.setException(bce.toString());
            failure.setReason(bce.getMessage());
            response.setFailure(failure);

            return response;
        }
    }
}
