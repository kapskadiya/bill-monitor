package com.kashyap.homeIdeas.billmonitor.resource.admin;

import com.kashyap.homeIdeas.billmonitor.config.security.JWTTokenUtil;
import com.kashyap.homeIdeas.billmonitor.dto.ApplicationResponse;
import com.kashyap.homeIdeas.billmonitor.dto.AuthRequest;
import com.kashyap.homeIdeas.billmonitor.exception.NoRecordFoundException;
import com.kashyap.homeIdeas.billmonitor.model.User;
import com.kashyap.homeIdeas.billmonitor.service.UserService;
import com.kashyap.homeIdeas.billmonitor.util.UserUtil;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import static com.kashyap.homeIdeas.billmonitor.constant.ApplicationConstant.TOKEN;
import static com.kashyap.homeIdeas.billmonitor.constant.ApplicationConstant.USER;

/**
 * This is the Authentication resource which can help to login into the system
 * @author Kashyap Kadiya
 * @since 2021-06
 */
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

    @Operation(summary = "Login into the system")
    @PostMapping("/login")
    public ApplicationResponse login(@RequestBody AuthRequest authRequest) {
        final ApplicationResponse response = new ApplicationResponse();
        final Map<String, Object> success = new HashMap<>();

        if (!userService.isExistByEmail(authRequest.getEmail())) {
            throw new NoRecordFoundException("User is not found.");
        }

        try {
            final UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword());
            final Authentication authentication = authenticationManager.authenticate(token);

            final User loggedInUser = (User) authentication.getPrincipal();

            response.setSuccess(true);
            response.setMessage("User is successfully logged in");

            success.put(TOKEN, jwtTokenUtil.generateAccessToken(loggedInUser));
            success.put(USER, UserUtil.buildDto(loggedInUser));

            response.setData(success);

            return response;
        } catch (BadCredentialsException bce) {
            log.error("Error occur while log-in. {}", bce.getMessage());
            response.setSuccess(false);
            response.setMessage(bce.getMessage());
            response.setCode(HttpStatus.BAD_REQUEST.value());
            return response;
        }
    }
}
