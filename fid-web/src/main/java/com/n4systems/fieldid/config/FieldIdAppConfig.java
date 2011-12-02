package com.n4systems.fieldid.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.n4systems.fieldid.wicket.pages.widgets.OrgDateRangeSubtitleHelper;
import com.n4systems.fieldid.wicket.pages.widgets.OrgPeriodSubtitleHelper;
import com.n4systems.fieldid.wicket.pages.widgets.OrgSubtitleHelper;
import com.n4systems.fieldid.wicket.pages.widgets.WidgetFactory;

@Configuration
public class FieldIdAppConfig {

	
    @Bean
    public OrgSubtitleHelper orgDateRangeSubtitleHelper() {
    	return new OrgDateRangeSubtitleHelper();
    }
    
    @Bean
    public OrgPeriodSubtitleHelper orgPeriodSubtitleHelper() {
    	return new OrgPeriodSubtitleHelper();
    }
    
    @Bean 
    public SchedulerFactoryBean schedulerFactoryBean() { 
    	return new SchedulerFactoryBean();
    }
    
    @Bean
    public WidgetFactory dashboardWidgetFactory() { 
    	return new WidgetFactory();
    }    
    
    
}
