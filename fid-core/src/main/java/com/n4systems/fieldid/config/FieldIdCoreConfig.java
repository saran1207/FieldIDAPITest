package com.n4systems.fieldid.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
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
import com.n4systems.fieldid.service.ReportServiceHelper;
import com.n4systems.fieldid.service.SecurityContextInitializer;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.asset.*;
import com.n4systems.fieldid.service.certificate.CertificateService;
import com.n4systems.fieldid.service.certificate.PrintAllCertificateService;
import com.n4systems.fieldid.service.event.*;
import com.n4systems.fieldid.service.export.EventTypeExportService;
import com.n4systems.fieldid.service.images.ImageService;
import com.n4systems.fieldid.service.job.JobService;
import com.n4systems.fieldid.service.mail.MailService;
import com.n4systems.fieldid.service.massupdate.MassUpdateService;
import com.n4systems.fieldid.service.offlineprofile.OfflineProfileService;
import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.fieldid.service.procedure.NotifyProcedureAuthorizersService;
import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.service.procedure.ProcedureService;
import com.n4systems.fieldid.service.schedule.AssetTypeScheduleService;
import com.n4systems.fieldid.service.schedule.MassScheduleService;
import com.n4systems.fieldid.service.schedule.RecurringScheduleService;
import com.n4systems.fieldid.service.schedule.ScheduleService;
import com.n4systems.fieldid.service.search.*;
import com.n4systems.fieldid.service.search.columns.DynamicColumnsService;
import com.n4systems.fieldid.service.sendsearch.SendSearchService;
import com.n4systems.fieldid.service.task.AsyncService;
import com.n4systems.fieldid.service.task.DownloadLinkService;
import com.n4systems.fieldid.service.tenant.ExtendedFeatureService;
import com.n4systems.fieldid.service.tenant.SystemSettingsService;
import com.n4systems.fieldid.service.tenant.TenantSettingsService;
import com.n4systems.fieldid.service.transaction.TransactionService;
import com.n4systems.fieldid.service.user.SendWelcomeEmailService;
import com.n4systems.fieldid.service.user.UserGroupService;
import com.n4systems.fieldid.service.user.UserLimitService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.service.uuid.AtomicLongService;
import com.n4systems.fieldid.service.uuid.UUIDService;
import com.n4systems.persistence.listeners.LocalizationListener;
import com.n4systems.persistence.listeners.SetupDataUpdateEventListener;
import com.n4systems.persistence.localization.HibernatePersistenceProvider;
import com.n4systems.services.ConfigService;
import com.n4systems.services.SecurityContext;
import com.n4systems.services.asset.AssetSaveServiceSpring;
import com.n4systems.services.dashboard.DashboardService;
import com.n4systems.services.date.DateService;
import com.n4systems.services.localization.LocalizationService;
import com.n4systems.services.reporting.DashboardReportingService;
import com.n4systems.services.search.*;
import com.n4systems.services.search.parser.DateParser;
import com.n4systems.services.search.parser.SearchParserService;
import com.n4systems.services.search.parser.SimpleParser;
import com.n4systems.services.search.parser.ValueFactory;
import com.n4systems.services.search.writer.AssetIndexWriter;
import com.n4systems.services.search.writer.CriteriaTrendsIndexWriter;
import com.n4systems.services.search.writer.EventIndexWriter;
import com.n4systems.services.tenant.TenantCreationService;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.json.JsonRenderer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.StringReader;

@Configuration
@EnableCaching
public class FieldIdCoreConfig {

    @Bean
    public AmazonS3Client amazonS3Client() {
        String accessKeyId = configService().getString(ConfigEntry.AMAZON_ACCESS_KEY_ID);
        String secretAccessKey = configService().getString(ConfigEntry.AMAZON_SECRET_ACCESS_KEY);
        AWSCredentials credentials = new BasicAWSCredentials(accessKeyId, secretAccessKey);
        ClientConfiguration config = new ClientConfiguration();
        if ("https".equals(configService().getString(ConfigEntry.SYSTEM_PROTOCOL))) {
            config.setProtocol(Protocol.HTTPS);
        }
        return new AmazonS3Client(credentials, config);
    }

    @Bean
    public EventTypeGroupService eventTypeGroupService() {
        return new EventTypeGroupService();
    }

    @Bean
    public ActionService actionService() {
        return new ActionService();
    }

    @Bean
    public EventCriteriaEditService eventCriteriaEditService() {
        return new EventCriteriaEditService();
    }

    @Bean
    public NotifyEventAssigneeService notifyEventAssigneeService() {
        return new NotifyEventAssigneeService();
    }

    @Bean
    public NotifyProcedureAuthorizersService notifyProcedureAuthorizersService() {
        return new NotifyProcedureAuthorizersService();
    }

    @Bean
    public SendSearchService sendSearchService() {
        return new SendSearchService();
    }
	
	@Bean
	public AssetSaveServiceSpring assetSaveService() {
		return new AssetSaveServiceSpring();
	}

    @Bean
    public NextEventScheduleService nextEventScheduleService() {
        return new NextEventScheduleService();
    }

    @Bean
    public TransactionService transactionService() {
        return new TransactionService();
    }

    @Bean
    public LastEventDateService lastEventDateService() {
        return new LastEventDateService();
    }

    @Bean
    public EventCreationService eventCreationService() {
        return new EventCreationService();
    }

    @Bean
    public EventResolutionService eventResolutionService() {
        return new EventResolutionService();
    }

    @Bean
    public AssociatedEventTypesService associatedEventTypesService() {
        return new AssociatedEventTypesService();
    }

    @Bean
    public AssetTypeScheduleService assetTypeScheduleService() {
        return new AssetTypeScheduleService();
    }
	
    @Bean
    public DashboardService dashboardService() {
        return new DashboardService();
    }

    @Bean
    public ScoreService scoreService() {
        return new ScoreService();
    }

    @Bean
    public AssetTypeGroupService assetTypeGroupService() {
        return new AssetTypeGroupService();
    }
	
	@Bean
	public TenantCreationService tenantCreationService() {
		return new TenantCreationService();
	}
	
	@Bean
	@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
	public ConfigService configService() {
		return new ConfigService();
	}

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
    public SavedAssetSearchService savedAssetSearchService() {
        return new SavedAssetSearchService();
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
    public ReportService reportService() {
        return new ReportService();
    }

    @Bean
    public ProcedureSearchService procedureSearchService() {
        return new ProcedureSearchService();
    }

    @Bean
    public ProcedureService procedureService() {
        return new ProcedureService();
    }

    @Bean
    public ProcedureDefinitionService procedureDefinitionService() {
        return new ProcedureDefinitionService();
    }

    @Bean
    public AssetSearchService assetSearchService() {
        return new AssetSearchService();
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
    @Scope(value="request", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public UserLimitService userLimitService() {
    	return new UserLimitService();
    }

    @Bean 
    public TenantSettingsService tenantSettingsService() {
		return new TenantSettingsService();
	}

    @Bean
    public LocalizationListener localizationListener() {
        return new LocalizationListener();
    }

    @Bean
    public SetupDataUpdateEventListener setupDataUpdateEventListener() {
        return new SetupDataUpdateEventListener();
    }

	@Bean
    public AbstractEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setPersistenceUnitName("fieldid");
        factoryBean.setPersistenceProvider(hibernatePersistenceProvider());
        return factoryBean;
    }

    @Bean HibernatePersistenceProvider hibernatePersistenceProvider() {
        return new HibernatePersistenceProvider();
    }

    @Bean
    public PlatformTransactionManager txManager() {
        return new JpaTransactionManager(entityManagerFactory().getObject());
    }

    @Bean
    @Scope(value="thread", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public SecurityContext securityContext() {
    	return new SecurityContext();
    }

    @Bean
    public PersistenceService persistenceService() {
        return new PersistenceService();
    }
    
    @Bean
    public EventTypeExportService exportService() {
        return new EventTypeExportService();
    }

    @Bean
    public EventService eventService() { 
    	return new EventService();
    }
    

    @Bean 
    public AsyncService asyncService() { 
    	return new AsyncService();
    }
    
    @Bean 
    public DashboardReportingService reportingService() { 
    	return new DashboardReportingService();
    }
        
    @Bean 
    public JsonRenderer jsonRenderer() { 
    	return new JsonRenderer();
    }
    
    @Bean
    public EventScheduleService eventScheduleService() {
    	return new EventScheduleService();
    }
    
    @Bean
    public CertificateService certificateService() {
    	return new CertificateService();
    }
    
    @Bean
    public PrintAllCertificateService printAllCertificateService() {
    	return new PrintAllCertificateService();
    }
    
    @Bean
    public MailService mailService() {
    	return new MailService();
    }
    
    @Bean
    public DownloadLinkService downloadLinkService() {
    	return new DownloadLinkService();
    }
    
    @Bean 
    public AssetService assetService() { 
    	return new AssetService();
    }
    
    @Bean 
    public ReportServiceHelper reportServiceHelper() {    
    	return new ReportServiceHelper();
    }
        
    @Bean
    public ScheduleService scheduleService() {
        return new ScheduleService();
    }

    @Bean
    public MassScheduleService massScheduleService() {
        return new MassScheduleService();
    }
    
    @Bean
    @Scope("singleton")
    public SecurityContextInitializer securityContextInitializer() {
    	return new SecurityContextInitializer();
    }

    @Bean
    public OfflineProfileService offlineProfileService() {
    	return new OfflineProfileService();
    }

    @Bean
    public MassUpdateService massUpdateService() {
    	return new MassUpdateService();
    }

    @Bean
    public EventStatusService eventStatusService() {
        return new EventStatusService();
    }

    @Bean
    public S3Service s3Service() {
        return new S3Service();
    }

	@Bean
	public ImageService imageService() {
		return new ImageService();
	}

    @Bean
    public DateService dateService() {
        return new DateService();
    }

    @Bean
    public PriorityCodeService priorityCodeService() {
        return new PriorityCodeService();
    }

    @Bean
    public RecurringScheduleService recurringScheduleService() {
        return new RecurringScheduleService();
    }

    @Bean
    public UserGroupService userGroupService() {
        return new UserGroupService();
    }

    @Bean
    public UUIDService uuidService() {
        return new UUIDService();
    }

    @Bean
    public AtomicLongService atomicLongService() {
        return new AtomicLongService();
    }

    @Bean
    public AssetIdentifierService assetIdentifierService() {
        return new AssetIdentifierService();
    }

    @Bean
    public SendWelcomeEmailService sendWelcomeEmailService() {
        return new SendWelcomeEmailService();
    }

    @Bean
    public UsageBasedEventThresholdAlert usageBasedEventThresholdAlert() {
        return new UsageBasedEventThresholdAlert();
    }

	@Bean
	public AssetFullTextSearchService fullTextSearchService() {
		return new AssetFullTextSearchService();
	}

    @Bean
    public EventFullTextSearchService eventFullTextSearchService() {
        return new EventFullTextSearchService();
    }

    @Bean
    public CriteriaTrendsIndexerServiceForOther criteriaTrendsIndexerService() {
        return new CriteriaTrendsIndexerServiceForOther();
    }

    @Bean
    public CriteriaTrendsIndexerServiceForTenants criteriaTrendsIndexerServiceTenants() {
        return new CriteriaTrendsIndexerServiceForTenants();
    }

    @Bean
    public EventIndexerService eventIndexerService() {
        return new EventIndexerService();
    }

    @Bean
    public AssetIndexerService assetIndexerService() {
        return new AssetIndexerService();
    }

	@Bean
	public SearchParserService globalSearchService() {
		return new SearchParserService();
	}

	@Bean
	public DateParser dateParser() {
		return new DateParser();
	}

	@Bean
	public SimpleParser simpleParser() {
		// NOTE : because this bean is generated via javaCC we have no control over the constructors.
		// .: we have to use this single arg one....users are required to call SearchParser.REINIT(myReader) before using.
		return new SimpleParser(new StringReader(""));
	}

	@Bean
	public ValueFactory valueFactory() {
		return new ValueFactory();
	}

    @Bean
    public MassUpdateEventService massUpdateEventService() {
        return new MassUpdateEventService();
    }

    @Bean
    public AssetCodeMappingService assetCodeMappingService() {
        return new AssetCodeMappingService();
    }

    @Bean
    public AutoAttributeService autoAttributeService() {
        return new AutoAttributeService();
    }

    @Bean
    public CharArraySet stopWordSet() {
        return new StopWordSet();
    }

    @Bean
    public AnalyzerFactory analyzerFactory() {
        return new AnalyzerFactory();
    }

    @Bean
    public CacheManager cacheManager() {
        EhCacheCacheManager ehCacheCacheManager = new EhCacheCacheManager();
        try {
            ehCacheCacheManager.setCacheManager(ehCache().getObject());
        } catch (Exception e) {
            throw new IllegalStateException("failed to create ehCache");
        }
        return ehCacheCacheManager;
    }

    @Bean
    public EhCacheManagerFactoryBean ehCache() {
        return new EhCacheManagerFactoryBean();
    }

    @Bean
    public AssetIndexWriter assetIndexWriter() {
        return new AssetIndexWriter();
    }

    @Bean
    public EventIndexWriter eventIndexWriter() {
        return new EventIndexWriter();
    }

    @Bean
    public CriteriaTrendsIndexWriter trendsIndexWriter() {
        return new CriteriaTrendsIndexWriter();
    }

    @Bean
    public CriteriaTrendsService criteriaTrendsService() {
        return new CriteriaTrendsService();
    }

    @Bean
    public LocalizationService localizationService() {
        return new LocalizationService();
    }

    @Bean
    public EventBookService eventBookService() {
        return new EventBookService();
    }


}
