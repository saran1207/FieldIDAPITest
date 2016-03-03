package com.n4systems.fieldid.actions.event;

import com.n4systems.api.model.EventView;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exporting.EventExporter;
import com.n4systems.exporting.Importer;
import com.n4systems.exporting.beanutils.*;
import com.n4systems.exporting.io.ExcelXSSFMapWriter;
import com.n4systems.exporting.io.MapReader;
import com.n4systems.exporting.io.MapWriter;
import com.n4systems.fieldid.actions.importexport.AbstractImportAction;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.*;
import com.n4systems.model.downloadlink.ContentType;
import com.n4systems.model.eventschedule.NextEventDateByEventLoader;
import com.n4systems.model.eventschedule.NextEventDateByEventPassthruLoader;
import com.n4systems.model.location.Location;
import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.model.utils.StreamUtils;
import com.n4systems.notifiers.notifications.EventImportFailureNotification;
import com.n4systems.notifiers.notifications.EventImportSuccessNotification;
import com.n4systems.notifiers.notifications.ImportFailureNotification;
import com.n4systems.notifiers.notifications.ImportSuccessNotification;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.security.Permissions;
import com.n4systems.util.ArrayUtils;
import com.n4systems.util.DateHelper;
import com.n4systems.util.persistence.QueryBuilder;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;

@SuppressWarnings("serial")
@UserPermissionFilter(userRequiresOneOf={Permissions.CREATE_EVENT})
public class EventImportAction extends AbstractImportAction {
	private Logger logger = Logger.getLogger(EventImportAction.class);
	
	private ThingEventType type;
	private List<ThingEventType> eventTypes;
	private InputStream exampleExportFileStream;
	private String exampleExportFileSize;
	private boolean includeRecommendationsAndDeficiencies;
	
	public EventImportAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@Override
	protected Importer createImporter(MapReader reader) {
		return getImporterFactory().createEventImporter(reader, getCurrentUser(), type);
	}
	
	@Override
	protected ImportSuccessNotification createSuccessNotification() {
		return new EventImportSuccessNotification(getCurrentUser(), type);
	}
	
	@Override
	protected ImportFailureNotification createFailureNotification() {
		return new EventImportFailureNotification(getCurrentUser(), type);
	}
	
	public String doDownloadExample() {		
		MapWriter writer = null;
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		try {
			ListLoader<ThingEvent> eventLoader = getLoaderFactory().createPassthruListLoader(Collections.singletonList(createExampleEvent()));
			NextEventDateByEventLoader nextDateLoader = new NextEventDateByEventPassthruLoader(createExampleNextDate());
			
			// override the serialization handler factory so we can control which fields are output.  (i.e. use different handlers
			//  depending on the "isInclude..." state of the action.
			SerializationHandlerFactory handlerFactory = new SerializationHandlerFactory() {
				@Override
				protected Class<? extends SerializationHandler> getSerializationHandlerForField(Field field, SerializableField annotation) {
					if (annotation.handler().equals(CriteriaResultSerializationHandler.class) && !isIncludeRecommendationsAndDeficiencies() ) {
						return FilteredCriteriaResultSerializationHandler.class;
					}
					return super.getSerializationHandlerForField(field, annotation);
				}
			};
			EventExporter exporter = new EventExporter(eventLoader, nextDateLoader, new ExportMapMarshaller<>(EventView.class, handlerFactory));
			
			writer = new ExcelXSSFMapWriter(new DateTimeDefiner(getCurrentUser()));
			exporter.export(writer);
			((ExcelXSSFMapWriter)writer).writeToStream(byteOut);
		} catch (Exception e) {
			logger.error("Failed generating example event export", e);
			return ERROR;
		} finally {
			StreamUtils.close(writer);
		}
		
		byte[] bytes = byteOut.toByteArray();
		exampleExportFileSize = String.valueOf(bytes.length);
		exampleExportFileStream = new ByteArrayInputStream(bytes);
		
		return SUCCESS;
	}
	
	private ThingEvent createExampleEvent() throws InstantiationException, IllegalAccessException {
		ThingEvent example = new ThingEvent();
		
		example.setEventForm(type.getEventForm());
		
		example.setDate(new Date());
		example.setOwner(getSessionUserOwner());
		example.setPrintable(true);
		example.setPerformedBy(getCurrentUser());
		example.setEventResult(EventResult.PASS);

		example.setComments(getText("example.event.comments"));
		example.setAdvancedLocation(Location.onlyFreeformLocation(getText("example.event.location")));
		
		example.setBook(new EventBook());
		example.getBook().setName(getText("example.event.book"));
		
		example.setAsset(new Asset());
		example.getAsset().setIdentifier(getText("example.event.identifier"));
		
		example.setAssetStatus(getExampleAssetStatus());
		
		example.setCriteriaResults(createExampleResults());		
		
		return example;
	}
	
	private Set<CriteriaResult> createExampleResults() throws InstantiationException, IllegalAccessException {
		Set<CriteriaResult> results = new HashSet<CriteriaResult>();
		
		if(type.getEventForm() != null) {
			List<CriteriaSection> availableSections = type.getEventForm().getAvailableSections();
			for (CriteriaSection section:availableSections) { 
				for (Criteria criterion:section.getAvailableCriteria()) {
					if (criterion.getCriteriaType() != CriteriaType.SIGNATURE) {		// skip signature because we don't support import/export of those fields.
						Class<? extends CriteriaResult> resultClass = criterion.getCriteriaType().getResultClass();
						CriteriaResult result = resultClass.newInstance();
						result.setCriteria(criterion);				
						result.setDeficiencies(new ArrayList<>());
						result.setRecommendations(new ArrayList<>());
						result.setTenant(criterion.getTenant());
						results.add(result);
					}
				}
			}		
		}
		return results;
	}

	private Date createExampleNextDate() {
		return DateHelper.addDaysToDate(new Date(), 365L);
	}
	
	private AssetStatus getExampleAssetStatus() {
		List<AssetStatus> statuses = getLoaderFactory().createAssetStatusListLoader().load();
		
		return (statuses.isEmpty()) ? null : statuses.get(0);
	}
	
	public EventType getEventType() {
		return type;
	}
	
	public Long getUniqueID() {
		return (type != null) ? type.getId() : null;
	}

	public void setUniqueID(Long id) {
		if (type == null || !type.getId().equals(id)) {
			type = getLoaderFactory().createFilteredIdLoader(ThingEventType.class).setPostFetchFields("eventForm.sections").setId(id).load();
		}
	}
	
	/*
	 * Example export file download params
	 */
	private String getExportFileName() {
		String exportName = type.getName();
		return getText("label.export_file.event", ArrayUtils.newArray(exportName));
	}
	
	public String getFileName() {
		return ContentType.EXCEL.prepareFileName(getExportFileName());
	}
	
	public String getFileSize() {
		return exampleExportFileSize;
	}

	public String getContentType() {
		return ContentType.EXCEL.getMimeType();
	}
	
	public InputStream getFileStream() {
		return exampleExportFileStream;
	}

	public void setIncludeRecommendationsAndDeficiencies(boolean include) {
		this.includeRecommendationsAndDeficiencies = include;
	}

	public boolean isIncludeRecommendationsAndDeficiencies() {
		return includeRecommendationsAndDeficiencies;
	}
	
	public List<ThingEventType> getEventTypes() {
		if (eventTypes == null) {
            QueryBuilder<ThingEventType> thingEventTypeQueryBuilder = new QueryBuilder<ThingEventType>(ThingEventType.class, getSecurityFilter());
            thingEventTypeQueryBuilder.addOrder("name");
            eventTypes = persistenceManager.findAll(thingEventTypeQueryBuilder);
		}
		return eventTypes;
	}
}
