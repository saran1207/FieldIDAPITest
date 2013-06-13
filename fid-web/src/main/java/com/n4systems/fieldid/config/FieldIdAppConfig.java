package com.n4systems.fieldid.config;

import com.n4systems.fieldid.service.StoreWsClientInformationAspect;
import com.n4systems.fieldid.service.comment.CommentService;
import com.n4systems.fieldid.service.search.columns.AssetColumnsService;
import com.n4systems.fieldid.service.search.columns.DynamicColumnsService;
import com.n4systems.fieldid.service.search.columns.EventColumnsService;
import com.n4systems.fieldid.service.search.columns.ProcedureColumnsService;
import com.n4systems.fieldid.servlets.ImageDownloadHandler;
import com.n4systems.fieldid.servlets.ImageUploadHandler;
import com.n4systems.fieldid.wicket.pages.widgets.OrgDateRangeSubtitleHelper;
import com.n4systems.fieldid.wicket.pages.widgets.OrgPeriodSubtitleHelper;
import com.n4systems.fieldid.wicket.pages.widgets.OrgSubtitleHelper;
import com.n4systems.fieldid.wicket.pages.widgets.WidgetFactory;
import com.n4systems.services.brainforest.SearchParserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
@Import({FieldIdWsConfig.class, FieldIdCoreConfig.class, FieldIdDownloadConfig.class, FieldIdEntityRemovalConfig.class})
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
    public ProcedureColumnsService procedureColumnsService() {
        return new ProcedureColumnsService();
    }
    
    @Bean
    public DynamicColumnsService dynamicColumnsService() { 
    	return new DynamicColumnsService();
    }
    
    @Bean
    public CommentService commentService() {
    	return new CommentService();
    }

    @Bean
    public SearchParserService globalSearchService() {
        return new SearchParserService();
    }

    @Bean
    // CAVEAT : make sure the servlet name is exactly the same as this bean name...that's how spring stitches them together.
    public ImageUploadHandler imageUploadHandler() {
        return new ImageUploadHandler();
    }

    @Bean
    // as above, make sure bean & servlet names match.
    public ImageDownloadHandler imageDownloadHandler() {
        return new ImageDownloadHandler();
    }

    @Bean
    public StoreWsClientInformationAspect testAspect() {
        return new StoreWsClientInformationAspect();
    }


}
