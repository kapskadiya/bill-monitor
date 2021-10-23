package com.kashyap.homeIdeas.billmonitor.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        final AntPathMatcher antPathMatcher = new AntPathMatcher();
        antPathMatcher.setCaseSensitive(false);
        configurer.setPathMatcher(antPathMatcher);
    }
}
