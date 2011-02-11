package com.n4systems.fieldid.config;

import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.ejb.legacy.wrapper.UserEJBContainer;
import com.n4systems.persistence.loaders.LoaderFactory;
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
    @Scope(value="request", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public LoaderFactory loaderFactory() {
        return new LoaderFactory();
    }

}
