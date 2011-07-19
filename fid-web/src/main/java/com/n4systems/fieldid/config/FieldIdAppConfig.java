package com.n4systems.fieldid.config;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.impl.PersistenceManagerImpl;
import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.ejb.legacy.wrapper.UserEJBContainer;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.FieldIdService;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.asset.AssetStatusService;
import com.n4systems.fieldid.service.asset.AssetTypeService;
import com.n4systems.fieldid.service.event.EventFormService;
import com.n4systems.fieldid.service.event.EventTypeService;
import com.n4systems.fieldid.service.job.JobService;
import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.model.security.SecurityFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.instrument.classloading.ReflectiveLoadTimeWeaver;
import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class FieldIdAppConfig {

    @Bean
    public AssetStatusService assetStatusService() {
        return injectPersistentService(new AssetStatusService());
    }

    @Bean
    public AssetTypeService assetTypeService() {
        return injectPersistentService(new AssetTypeService());
    }

    @Bean
    public OrgService orgService() {
        return injectPersistentService(new OrgService());
    }

    @Bean
    public JobService jobService() {
        return injectPersistentService(new JobService());
    }

    @Bean
    public UserService userService() {
        return injectPersistentService(new UserService());
    }

    @Bean
    public EventTypeService eventTypeService() {
        return injectPersistentService(new EventTypeService());
    }

    @Bean
    public EventFormService eventFormService() {
        return injectPersistentService(new EventFormService());
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
    public AbstractEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setPersistenceUnitName("fieldid");
        factoryBean.setLoadTimeWeaver(new ReflectiveLoadTimeWeaver());
        return factoryBean;
    }

    @Bean
    public PlatformTransactionManager txManager() {
        return new JpaTransactionManager(entityManagerFactory().getObject());
    }

    @Bean
    @Scope(value="session", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public SecurityFilter userSecurityFilter() {
        return FieldIDSession.get().getSessionUser().getSecurityFilter();
    }

    @Bean
    public PersistenceService persistenceService() {
        return injectService(new PersistenceService());
    }


    private <T extends FieldIdService> T injectService(T service) {
        service.setUserSecurityFilter(userSecurityFilter());
        return service;
    }

    private <T extends FieldIdPersistenceService> T injectPersistentService(T service) {
        injectService(service);
        service.setPersistenceService(persistenceService());
        return service;
    }

}
