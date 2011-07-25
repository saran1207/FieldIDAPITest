package com.n4systems.fieldid.config;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.protocol.http.WebRequestCycle;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import rfid.web.helper.SessionUser;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.impl.PersistenceManagerImpl;
import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.ejb.legacy.wrapper.UserEJBContainer;
import com.n4systems.fieldid.actions.utils.WebSession;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.asset.AssetStatusService;
import com.n4systems.fieldid.service.asset.AssetTypeService;
import com.n4systems.fieldid.service.event.EventFormService;
import com.n4systems.fieldid.service.event.EventTypeService;
import com.n4systems.fieldid.service.job.JobService;
import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.fieldid.service.search.ReportService;
import com.n4systems.fieldid.service.search.SearchService;
import com.n4systems.fieldid.service.tenant.TenantSettingsService;
import com.n4systems.fieldid.service.user.LoginService;
import com.n4systems.fieldid.service.user.UserLimitService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.services.TenantSecurityFilterDelegate;
import com.n4systems.util.HostNameParser;

@Configuration
public class FieldIdAppConfig {
    
    @Bean
    public SearchService searchService() {
        return new SearchService();
    }

    @Bean
    public ReportService reportService() {
        return new ReportService(searchService());
    }

    @Bean
    public AssetStatusService assetStatusService() {
        return new AssetStatusService();
    }

    @Bean
    public AssetTypeService assetTypeService() {
        return new AssetTypeService();
    }

    @Bean
    public OrgService orgService() {
        return new OrgService();
    }

    @Bean
    public JobService jobService() {
        return new JobService();
    }

    @Bean
    public UserService userService() {
        return new UserService();
    }

    @Bean
    public EventTypeService eventTypeService() {
        return new EventTypeService();
    }

    @Bean
    public EventFormService eventFormService() {
        return new EventFormService();
    }

    @Bean
    public PersistenceManager persistenceEJBContainer() {
        return new PersistenceManagerImpl();
    }
    
    @Bean
    public UserManager userEJBContainer() {
        return new UserEJBContainer(loginService());		// return new service
    }
    
    @Bean 
    public LoginService loginService() { 
    	return new LoginService();
    }
        
    @Bean
    @Scope(value="request", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public UserLimitService userLimitService() {
    	UserLimitService userLimitService = new UserLimitService();
    	userLimitService.setTenantSettingsService(tenantSettingsService());   // use autowiring instead.
    	return userLimitService;
    }

    @Bean 
    public TenantSettingsService tenantSettingsService() {
		return new TenantSettingsService();
	}

	@Bean
    public AbstractEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setPersistenceUnitName("fieldid");
        return factoryBean;
    }

    @Bean
    public PlatformTransactionManager txManager() {
        return new JpaTransactionManager(entityManagerFactory().getObject());
    }

    @Bean
    @Scope(value="session", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public SecurityFilter userSecurityFilter() {
    	SessionUser user = (RequestCycle.get() != null) ? FieldIDSession.get().getSessionUser() : new WebSession().getSessionUser();
        return user.getSecurityFilter();
    }
    
    @Bean
    @Scope(value="request", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public SecurityFilter tenantSecurityFilter() {
    	HttpServletRequest request = (WebRequestCycle.get() != null) ? ((ServletWebRequest) WebRequestCycle.get().getRequest()).getHttpServletRequest() : ServletActionContext.getRequest();
    	String tenantName = HostNameParser.create(request.getRequestURL().toString()).getFirstSubDomain();
    	
    	return new TenantSecurityFilterDelegate(tenantName);
    }

    @Bean
    public PersistenceService persistenceService() {
        return new PersistenceService();
    }
}
