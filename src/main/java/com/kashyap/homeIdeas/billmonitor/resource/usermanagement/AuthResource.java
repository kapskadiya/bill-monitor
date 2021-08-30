package com.kashyap.homeIdeas.billmonitor.resource.usermanagement;

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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/usermanagement/auth")
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

        final User user = userService.getByUsername(authRequest.getUsername());

        if (user == null) {
            final Failure failure = new Failure();
            failure.setReason("User not exist in the system");
            response.setFailure(failure);

            return response;
        }

        try {
            final UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword());
            final Authentication authentication = authenticationManager.authenticate(token);

            final User loggedInUser = (User) authentication.getPrincipal();
            response.setSuccess("token", jwtTokenUtil.generateAccessToken(loggedInUser));
            response.setSuccess("user", UserUtil.buildDto(loggedInUser));

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
