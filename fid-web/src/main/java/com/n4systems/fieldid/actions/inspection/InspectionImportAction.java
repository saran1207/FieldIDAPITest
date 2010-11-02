package com.n4systems.fieldid.actions.inspection;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.n4systems.model.Event;
import com.n4systems.model.EventBook;
import com.n4systems.model.EventType;
import com.n4systems.model.inspectionschedule.NextEventDateByEventLoader;
import com.n4systems.model.inspectionschedule.NextEventDateByEventPassthruLoader;
import org.apache.log4j.Logger;

import rfid.ejb.entity.AssetStatus;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exporting.Importer;
import com.n4systems.exporting.InspectionExporter;
import com.n4systems.exporting.io.ExcelMapWriter;
import com.n4systems.exporting.io.MapReader;
import com.n4systems.exporting.io.MapWriter;
import com.n4systems.fieldid.actions.importexport.AbstractImportAction;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.Asset;
import com.n4systems.model.Status;
import com.n4systems.model.downloadlink.ContentType;
import com.n4systems.model.location.Location;
import com.n4systems.model.utils.StreamUtils;
import com.n4systems.notifiers.notifications.ImportFailureNotification;
import com.n4systems.notifiers.notifications.ImportSuccessNotification;
import com.n4systems.notifiers.notifications.InspectionImportFailureNotification;
import com.n4systems.notifiers.notifications.InspectionImportSuccessNotification;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.security.Permissions;
import com.n4systems.util.ArrayUtils;
import com.n4systems.util.DateHelper;

@SuppressWarnings("serial")
@UserPermissionFilter(userRequiresOneOf={Permissions.CreateInspection})
public class InspectionImportAction extends AbstractImportAction {
	private Logger logger = Logger.getLogger(InspectionImportAction.class);
	
	private EventType type;
	private InputStream exampleExportFileStream;
	private String exampleExportFileSize;
	
	public InspectionImportAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@Override
	protected Importer createImporter(MapReader reader) {
		return getImporterFactory().createInspectionImporter(reader, getSessionUserId(), type);
	}
	
	@Override
	protected ImportSuccessNotification createSuccessNotification() {
		return new InspectionImportSuccessNotification(getUser());
	}
	
	@Override
	protected ImportFailureNotification createFailureNotification() {
		return new InspectionImportFailureNotification(getUser());
	}
	
	public String doDownloadExample() {
		ListLoader<Event> inspectionLoader = getLoaderFactory().createPassthruListLoader(Arrays.asList(createExampleInspection()));
		NextEventDateByEventLoader nextDateLoader = new NextEventDateByEventPassthruLoader(createExampleNextDate());

		InspectionExporter exporter = new InspectionExporter(inspectionLoader, nextDateLoader);
		
		MapWriter writer = null;
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		try {
			writer = new ExcelMapWriter(byteOut, getPrimaryOrg().getDateFormat());
			exporter.export(writer);
			
		} catch (Exception e) {
			logger.error("Failed generating example inspection export", e);
			return ERROR;
		} finally {
			StreamUtils.close(writer);
		}
		
		byte[] bytes = byteOut.toByteArray();
		exampleExportFileSize = String.valueOf(bytes.length);
		exampleExportFileStream = new ByteArrayInputStream(bytes);
		
		return SUCCESS;
	}
	
	private Event createExampleInspection() {
		Event example = new Event();
		
		example.setDate(new Date());
		example.setOwner(getSessionUserOwner());
		example.setPrintable(true);
		example.setPerformedBy(getUser());
		example.setStatus(Status.PASS);

		example.setComments(getText("example.event.comments"));
		example.setAdvancedLocation(Location.onlyFreeformLocation(getText("example.event.location")));
		
		example.setBook(new EventBook());
		example.getBook().setName(getText("example.event.book"));
		
		example.setAsset(new Asset());
		example.getAsset().setSerialNumber(getText("example.event.serialnumber"));
		
		example.setAssetStatus(getExampleAssetStatus());
		
		return example;
	}
	
	private Date createExampleNextDate() {
		return DateHelper.addDaysToDate(new Date(), 365L);
	}
	
	private AssetStatus getExampleAssetStatus() {
		List<AssetStatus> statuses = getLoaderFactory().createAssetStatusListLoader().load();
		
		return (statuses.isEmpty()) ? null : statuses.get(0);
	}
	
	public EventType getInspectionType() {
		return type;
	}

	public Long getUniqueID() {
		return (type != null) ? type.getId() : null;
	}

	public void setUniqueID(Long id) {
		if (type == null || !type.getId().equals(id)) {
			type = getLoaderFactory().createFilteredIdLoader(EventType.class).setPostFetchFields("sections").setId(id).load();
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
