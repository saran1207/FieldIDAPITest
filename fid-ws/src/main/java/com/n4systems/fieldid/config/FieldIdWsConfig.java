package com.n4systems.fieldid.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.n4systems.fieldid.ws.v1.resources.AuthenticationResource;

@Configuration
public class FieldIdWsConfig {
    
    @Bean
    public AuthenticationResource authenticationResource() {
    	return new AuthenticationResource();
    }
    
         
}
