package com.n4systems.fieldid.actions.export;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.service.export.EventTypeExportService;
import com.n4systems.model.EventType;
import com.n4systems.model.downloadlink.ContentType;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.downloadlink.DownloadState;
import com.n4systems.model.user.User;
import com.n4systems.security.Permissions;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@SuppressWarnings("serial")
@UserPermissionFilter(userRequiresOneOf={Permissions.EDIT_EVENT})
public class ExportAction extends AbstractAction {

	@Autowired
    EventTypeExportService eventTypeEventTypeExportService;
	@Autowired EventService eventService;
	
	private Long eventTypeId;
	private String from;
	private String to;
	private Long linkId;
	private DownloadLink downloadLink;
	private transient EventType eventType;

	private String reportName;
	
	public ExportAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	public List<EventType> getEventTypes() {
		return eventService.getEventTypes();
	}

	public String doShow() { 
		return SUCCESS;
	}
	
	public String doExport() {
		if (eventService.countThingEventsByType(eventTypeId, getFromDate(), getToDate()) > 0) {
			DownloadLink link = getDownloadLink();
			eventTypeEventTypeExportService.exportEventTypeToExcel(getSessionUserId(), eventTypeId, getFromDate(), getToDate(), link.getId() );
			return SUCCESS;
		} else {
			addActionError(getText("error.no_events_to_export"));
			return ERROR;
		}
	}
		
	private EventType getEventType() {
		if (eventType==null) { 
			eventType = persistenceManager.find(EventType.class, getEventTypeId());
		}
		return eventType;
	}

	public String doConfirmDownloadName() {
		DownloadLink link = persistenceManager.find(DownloadLink.class, getLinkId());		
		if (link==null) { 
			return ERROR;
		}
		if(!reportName.equals(link.getName()) && !reportName.isEmpty()) {
			link.setName(reportName);
			persistenceManager.update(link);
		}
		return SUCCESS;
	}

	public void setEventTypeId(Long eventTypeId) {
		this.eventTypeId = eventTypeId;
	}

	public Long getEventTypeId() {
		checkNotNull(eventTypeId, "event type id must be supplied");
		return eventTypeId;
	}

	public void setDownloadLink(DownloadLink downloadLink) {
		this.downloadLink = downloadLink;
	}

	public DownloadLink getDownloadLink() {
		if (downloadLink==null) { 
			User user = getCurrentUser();
			DownloadLink link = new DownloadLink();
			link.setState(DownloadState.REQUESTED);
			link.setContentType(ContentType.EXCEL);
			link.setTenant(user.getTenant());
			link.setUser(user);
			link.setName(generateFileName());
			persistenceManager.save(link);
			downloadLink = link;
		}
		return downloadLink;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getReportName() {
		reportName = generateFileName();
		return reportName;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getFrom() {
		return from;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getTo() {
		return to;
	}
	
	private Date getToDate() {
		try {
			return StringUtils.isBlank(to) ? new Date(Long.MAX_VALUE) : new SimpleDateFormat(getSessionUser().getDateFormat()).parse(to);
		} catch (ParseException e) {
			e.printStackTrace();
			return new Date(Long.MAX_VALUE);		
		}
	}
	
	private Date getFromDate() {
		try {
			return StringUtils.isBlank(from) ? new Date(0) : new SimpleDateFormat(getSessionUser().getDateFormat()).parse(from);
		} catch (ParseException e) {
			e.printStackTrace();
			return new Date(0);		
		}
	}

	private String generateFileName() {
		return reportName = String.format("%s_Events.xlsx", getEventType().getName());
	}

	public void setLinkId(Long linkId) {
		this.linkId = linkId;
	}

	public Long getLinkId() {
		return linkId;
	}
}




