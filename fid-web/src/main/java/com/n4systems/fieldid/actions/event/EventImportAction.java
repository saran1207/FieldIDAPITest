package com.n4systems.fieldid.actions.event;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exporting.EventExporter;
import com.n4systems.exporting.Importer;
import com.n4systems.exporting.io.ExcelMapWriter;
import com.n4systems.exporting.io.MapReader;
import com.n4systems.exporting.io.MapWriter;
import com.n4systems.fieldid.actions.importexport.AbstractImportAction;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetStatus;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.Deficiency;
import com.n4systems.model.Event;
import com.n4systems.model.EventBook;
import com.n4systems.model.EventType;
import com.n4systems.model.Recommendation;
import com.n4systems.model.Status;
import com.n4systems.model.downloadlink.ContentType;
import com.n4systems.model.eventschedule.NextEventDateByEventLoader;
import com.n4systems.model.eventschedule.NextEventDateByEventPassthruLoader;
import com.n4systems.model.location.Location;
import com.n4systems.model.utils.StreamUtils;
import com.n4systems.notifiers.notifications.EventImportFailureNotification;
import com.n4systems.notifiers.notifications.EventImportSuccessNotification;
import com.n4systems.notifiers.notifications.ImportFailureNotification;
import com.n4systems.notifiers.notifications.ImportSuccessNotification;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.security.Permissions;
import com.n4systems.util.ArrayUtils;
import com.n4systems.util.DateHelper;

@SuppressWarnings("serial")
@UserPermissionFilter(userRequiresOneOf={Permissions.CreateEvent})
public class EventImportAction extends AbstractImportAction {
	private Logger logger = Logger.getLogger(EventImportAction.class);
	
	private EventType type;
	private InputStream exampleExportFileStream;
	private String exampleExportFileSize;
	
	public EventImportAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@Override
	protected Importer createImporter(MapReader reader) {
		return getImporterFactory().createEventImporter(reader, getSessionUserId(), type);
	}
	
	@Override
	protected ImportSuccessNotification createSuccessNotification() {
		return new EventImportSuccessNotification(getUser());
	}
	
	@Override
	protected ImportFailureNotification createFailureNotification() {
		return new EventImportFailureNotification(getCurrentUser());
	}
	
	public String doDownloadExample() {
		MapWriter writer = null;
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		try {
			ListLoader<Event> eventLoader = getLoaderFactory().createPassthruListLoader(Arrays.asList(createExampleEvent()));
			NextEventDateByEventLoader nextDateLoader = new NextEventDateByEventPassthruLoader(createExampleNextDate());
			
			EventExporter exporter = new EventExporter(eventLoader, nextDateLoader);
			
			writer = new ExcelMapWriter(byteOut, getPrimaryOrg().getDateFormat());
			exporter.export(writer);
			
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
	
	private Event createExampleEvent() throws InstantiationException, IllegalAccessException {
		Event example = new Event();
		
		example.setEventForm(type.getEventForm());
		
		example.setDate(new Date());
		example.setOwner(getSessionUserOwner());
		example.setPrintable(true);
		example.setPerformedBy(getCurrentUser());
		example.setStatus(Status.PASS);

		example.setComments(getText("example.event.comments"));
		example.setAdvancedLocation(Location.onlyFreeformLocation(getText("example.event.location")));
		
		example.setBook(new EventBook());
		example.getBook().setName(getText("example.event.book"));
		
		example.setAsset(new Asset());
		example.getAsset().setSerialNumber(getText("example.event.serialnumber"));
		
		example.setAssetStatus(getExampleAssetStatus());
		
		example.setResults(createExampleResults());
		
		
		return example;
	}
	
	private Set<CriteriaResult> createExampleResults() throws InstantiationException, IllegalAccessException {
		Set<CriteriaResult> results = new HashSet<CriteriaResult>();
		
		List<CriteriaSection> availableSections = type.getEventForm().getAvailableSections();
		for (CriteriaSection section:availableSections) { 
			for (Criteria criterion:section.getAvailableCriteria()) {				
				Class<? extends CriteriaResult> resultClass = criterion.getCriteriaType().getResultClass();
				CriteriaResult result = resultClass.newInstance();
				result.setCriteria(criterion);
				result.setDeficiencies(new ArrayList<Deficiency>());   // FIXME DD : how to convert these types to and from Strings
				result.setRecommendations(new ArrayList<Recommendation>());	// TODO : populate these with defaults.  need someway to generically get default value...doesn't currently allow that easily. hmmmmm...
				result.setTenant(criterion.getTenant());
				results.add(result);
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
			type = getLoaderFactory().createFilteredIdLoader(EventType.class).setPostFetchFields("eventForm.sections").setId(id).load();
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
}
