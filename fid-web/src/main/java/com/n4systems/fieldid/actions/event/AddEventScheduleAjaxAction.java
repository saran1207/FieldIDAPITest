package com.n4systems.fieldid.actions.event;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetTypeSchedule;
import com.n4systems.model.EventType;
import com.n4systems.model.Project;
import com.n4systems.model.user.Assignable;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserGroup;
import com.n4systems.util.DateHelper;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import org.apache.struts2.interceptor.validation.SkipValidation;

import java.util.Date;

public class AddEventScheduleAjaxAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private EventType eventType;
	private Asset asset;
	private Project job;
	private String date;
	private Long index;
	private String datePerformed;
    private Assignable assignee;
	
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

    public Assignable getAssignee() {
        return assignee;
    }

    public void setAssigneeId(String assigneeId) {
        if (assigneeId==null) {
            return;
        }
        assignee = findAssignee(assigneeId);
        if (assignee != null) {
            nextSchedule.setAssignee(assigneeId);
            nextSchedule.setAssigneeName(assignee.getDisplayName());
        }
    }

    private Assignable findAssignee(String assigneeId) {
        if (assigneeId.startsWith("U")) {
            return getLoaderFactory().createFilteredIdLoader(User.class).setId(Long.valueOf(assigneeId.substring(1))).load();
        } else if (assigneeId.startsWith("G")) {
            return getLoaderFactory().createFilteredIdLoader(UserGroup.class).setId(Long.valueOf(assigneeId.substring(1))).load();
        }
        return null;
    }
}
