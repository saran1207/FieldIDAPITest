package com.n4systems.fieldid.config;

import com.n4systems.fieldid.service.tenant.ExtendedFeatureService;
import com.n4systems.fieldid.service.tenant.SystemSettingsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.impl.PersistenceManagerImpl;
import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.ejb.legacy.wrapper.LegacyAssetEJBContainer;
import com.n4systems.ejb.legacy.wrapper.LegacyAssetTypeEJBContainer;
import com.n4systems.ejb.legacy.wrapper.ServiceDTOBeanConverterEJBContainer;
import com.n4systems.ejb.legacy.wrapper.UserEJBContainer;
import com.n4systems.ejb.wrapper.AssetManagerEJBContainer;
import com.n4systems.ejb.wrapper.EventScheduleManagerEJBContainer;
import com.n4systems.ejb.wrapper.OrderManagerEJBContainer;
import com.n4systems.ejb.wrapper.ProofTestHandlerEJBContainer;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.asset.AssetStatusService;
import com.n4systems.fieldid.service.asset.AssetTypeService;
import com.n4systems.fieldid.service.event.EventFormService;
import com.n4systems.fieldid.service.event.EventTypeService;
import com.n4systems.fieldid.service.job.JobService;
import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.fieldid.service.search.ReportService;
import com.n4systems.fieldid.service.search.SavedReportService;
import com.n4systems.fieldid.service.search.SearchService;
import com.n4systems.fieldid.service.search.columns.DynamicColumnsService;
import com.n4systems.fieldid.service.tenant.TenantSettingsService;
import com.n4systems.fieldid.service.user.LoginService;
import com.n4systems.fieldid.service.user.UserLimitService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.services.SecurityContext;
import com.n4systems.util.ServiceLocator;

@Configuration
public class FieldIdAppConfig {

	@Bean
	public ServiceLocator serviceLocator() { 
		return new ServiceLocator();
	}

    @Bean
    public SystemSettingsService systemSettingsService() {
        return new SystemSettingsService();
    }

    @Bean
    public ExtendedFeatureService extendedFeatureService() {
        return new ExtendedFeatureService();
    }

	@Bean 
	public ServiceDTOBeanConverterEJBContainer serviceDTOBeanConverter() { 
		return new ServiceDTOBeanConverterEJBContainer();
	}
	
	@Bean 
	public EventScheduleManagerEJBContainer scheduleEJBContainer() { 
		return new EventScheduleManagerEJBContainer(); 
	}
	
    @Bean 
    public LegacyAssetEJBContainer productSerialEJBContainer() { 
    	return new LegacyAssetEJBContainer();
    }
    
    @Bean
    public OrderManagerEJBContainer orderEJBContainer() { 
    	return new OrderManagerEJBContainer();
    }

    @Bean 
    public LegacyAssetTypeEJBContainer productTypeEJBContainer() {   
    	return new LegacyAssetTypeEJBContainer();
    }
	
	@Bean
	public AssetManagerEJBContainer productEJBContainer() {
		return new AssetManagerEJBContainer();
	}
	
    @Bean 
    public ProofTestHandlerEJBContainer proofTestHandler() {
    	return new ProofTestHandlerEJBContainer(); 
    }
	
    @Bean
    public SavedReportService savedReportService() {
        return new SavedReportService();
    }

    @Bean
    public DynamicColumnsService dynamicColumnsService() {
        return new DynamicColumnsService();
    }
    
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
        return new UserEJBContainer();
    }
    
    @Bean 
    public LoginService loginService() { 
    	return new LoginService();
    }
        
    @Bean
    @Scope(value="request", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public UserLimitService userLimitService() {
    	return new UserLimitService();
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
    @Scope(value="request", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public SecurityContext securityContext() {
    	return new SecurityContext();
    }

    @Bean
    public PersistenceService persistenceService() {
        return new PersistenceService();
    }
}
