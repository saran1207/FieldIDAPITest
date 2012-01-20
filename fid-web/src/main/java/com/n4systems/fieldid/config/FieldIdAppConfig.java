package com.n4systems.fieldid.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.n4systems.fieldid.service.search.columns.AssetColumnsService;
import com.n4systems.fieldid.service.search.columns.EventColumnsService;
import com.n4systems.fieldid.service.search.columns.ScheduleColumnsService;
import com.n4systems.fieldid.wicket.pages.widgets.OrgDateRangeSubtitleHelper;
import com.n4systems.fieldid.wicket.pages.widgets.OrgPeriodSubtitleHelper;
import com.n4systems.fieldid.wicket.pages.widgets.OrgSubtitleHelper;
import com.n4systems.fieldid.wicket.pages.widgets.WidgetFactory;

@Configuration
@Import({FieldIdWsConfig.class, FieldIdCoreConfig.class})
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

    @Bean 
    public AssetColumnsService assetColumnsService() { 
    	return new AssetColumnsService(); 
    }
    
    @Bean 
    public EventColumnsService eventColumnsService() { 
    	return new EventColumnsService();
    }
    
    @Bean 
    public ScheduleColumnsService scheduleColumnsService() { 
    	return new ScheduleColumnsService();
    }
    
}
