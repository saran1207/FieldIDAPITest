package com.n4systems.fieldid.actions.inspection;

import java.util.Date;

import com.n4systems.model.AssetTypeSchedule;
import com.n4systems.model.EventType;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.model.Asset;
import com.n4systems.model.Project;
import com.n4systems.util.DateHelper;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;

public class AddEventScheduleAjaxAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private EventType eventType;
	private Asset asset;
	private Project job;
	private String date;
	private Long index;
	private String datePerformed;
	
	private WebEventSchedule nextSchedule = new WebEventSchedule();
	
	public AddEventScheduleAjaxAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	public String doAdd() {
		return SUCCESS;
	}
	
	@SkipValidation
	public String doAutoSuggest() {
		Date startDate = convertDate(datePerformed);
		
		if (startDate == null) {
			startDate = DateHelper.getToday();
		}
		
		AssetTypeSchedule schedule = asset.getType().getSchedule(eventType, asset.getOwner());
		if (schedule != null) {
			Date nextDate = schedule.getNextDate(startDate);
			setDate(convertDate(nextDate));
			nextSchedule.setAutoScheduled(true);
		}
		
		return SUCCESS;
	}
	
	public EventType getEventType() {
		return eventType;
	}
	
	public Long getEventTypeId() {
		return eventType.getId();
	}

	public void setEventTypeId(Long id) {
		eventType = persistenceManager.find(EventType.class, id, getTenantId());
		nextSchedule.setType(id);
		nextSchedule.setTypeName(eventType.getName());
	}

	public Long getAsset() {
		return (asset != null) ? asset.getId(): null;
	}

	public void setAsset(Long id) {
		asset = getLoaderFactory().createFilteredIdLoader(Asset.class).setId(id).setPostFetchFields("type.eventTypes").load();
	}

	public Project getJob() {
		return job;
	}
	
	public Long getJobId() {
		return (job != null) ? job.getId(): null;
	}

	public void setJobId(Long id) {
		job = getLoaderFactory().createFilteredIdLoader(Project.class).setId(id).load();
		if (job != null) {
			nextSchedule.setJob(job.getId());
			nextSchedule.setJobName(job.getName());
		}
	}
	
	public String getDate() {
		return date;
	}

	@RequiredStringValidator(message="", key="error.mustbeadate")
	@CustomValidator(type = "n4systemsDateValidator", message = "", key = "error.mustbeadate")
	public void setDate(String date) {
		this.date = date;
		nextSchedule.setDate(date);
	}

	public Long getIndex() {
		return index;
	}

	public void setIndex(Long index) {
		this.index = index;
	}

	public WebEventSchedule getNextSchedule() {
		return nextSchedule;
	}

	public String getDatePerformed() {
		return datePerformed;
	}

	public void setDatePerformed(String datePerformed) {
		this.datePerformed = datePerformed;
	}
	
}
