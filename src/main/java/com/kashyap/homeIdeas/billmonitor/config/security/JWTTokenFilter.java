package com.kashyap.homeIdeas.billmonitor.config.security;

import com.kashyap.homeIdeas.billmonitor.repostiory.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class JWTTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JWTTokenUtil jwtTokenUtil;

    @Autowired
    private UserRepository userRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        // Get authorization header and validate
        final String header = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isBlank(header) || !header.startsWith("Bearer ")) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        // Get jwt token and validate
        final String token = header.split(" ")[1].trim();
        if (!jwtTokenUtil.validate(token)) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        // Get user identity and set it on the spring security context
        final UserDetails userDetails = userRepo
                .findByUsername(jwtTokenUtil.getUsername(token))
                .orElse(null);


        final UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        Optional.ofNullable(userDetails)
                                .map(UserDetails::getAuthorities)
                                .orElse(List.of())
                );

        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(httpServletRequest)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
