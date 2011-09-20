package com.n4systems.fieldid.actions.export;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.service.export.ExportService;
import com.n4systems.model.EventType;
import com.n4systems.model.downloadlink.ContentType;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.downloadlink.DownloadState;
import com.n4systems.model.user.User;
import com.n4systems.security.Permissions;

@SuppressWarnings("serial")
@UserPermissionFilter(userRequiresOneOf={Permissions.EditEvent})
public class ExportAction extends AbstractAction {

	@Autowired ExportService exportService;
	
	private Long eventTypeId;
	private Long from;
	private Long to;
	private DownloadLink downloadLink;
	private transient EventType eventType;

	private String reportName;
	
	public ExportAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}
	
	public String doExport() {
		DownloadLink link = getDownloadLink();
		exportService.exportEventTypeToExcel(getSessionUserId(), eventTypeId, getFromDate(), getToDate(), link.getId() );
		return SUCCESS;
	}
	
	private Date getToDate() {
		return to==null ? new Date(Long.MAX_VALUE) : new Date(to);
	}

	private Date getFromDate() {
		return from==null ? new Date(0) : new Date(from);
	}

	private String generateFileName() {
		return String.format("%s_Events.xls", getEventType().getName());		
	}

	private EventType getEventType() {
		if (eventType==null) { 
			eventType = persistenceManager.find(EventType.class, eventTypeId);
		}
		return eventType;
	}

	public void setEventType(Long eventType) {
		this.eventTypeId = eventType;
	}

	public Long getEventTypeId() {
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

	public void setFrom(Long from) {
		this.from = from;
	}

	public Long getFrom() {
		return from;
	}

	public void setTo(Long to) {
		this.to = to;
	}

	public Long getTo() {
		return to;
	}
	
}




