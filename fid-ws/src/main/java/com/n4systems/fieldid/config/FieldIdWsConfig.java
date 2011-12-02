package com.n4systems.fieldid.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.n4systems.fieldid.ws.v1.resources.authentication.AuthenticationResource;
import com.n4systems.fieldid.ws.v1.resources.org.ApiOrgResource;
import com.n4systems.fieldid.ws.v1.resources.user.ApiUserResource;

@Configuration
public class FieldIdWsConfig {
    
	@Bean
	@Scope("singleton")
	public CatchAllExceptionMapper catchAllExceptionMapper() {
		return new CatchAllExceptionMapper();
	}
	
    @Bean
    public AuthenticationResource authenticationResource() {
    	return new AuthenticationResource();
    }
    
    @Bean
    public ApiOrgResource apiOrgResource() {
    	return new ApiOrgResource();
    }
    
    @Bean
    public ApiUserResource apiUserResource() {
    	return new ApiUserResource();
    }

}
