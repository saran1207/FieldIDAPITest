package com.n4systems.fieldid.actions.notifications;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.n4systems.model.orgs.BaseOrg;
import com.opensymphony.xwork2.validator.annotations.EmailValidator;
import com.opensymphony.xwork2.validator.annotations.FieldExpressionValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validation;

@Validation

public class NotificationSettingView implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private BaseOrg owner;
	private Long createdTimeStamp;
	private String name;
	private String frequency;
	private Boolean includeUpcoming = true;
	private String periodStart;
	private String periodEnd;
	private Long assetTypeGroupId;
	private Long assetTypeId;
	private Long assetStatus;
	private Long eventTypeId;
	private Boolean includeOverdue = false;
	private List<String> addresses = new ArrayList<String>();
	private Boolean includeFailed = false;
	private Boolean sendBlankReport = false;

	
	public NotificationSettingView() {}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public Long getID() {
		return id;
	}
	
	public void setID(Long id) {
		this.id = id;
	}
	
	public BaseOrg getOwner() {
    	return owner;
    }

	public void setOwner(BaseOrg owner) {
    	this.owner = owner;
    }
	
	public Long getCreatedTimeStamp() {
    	return createdTimeStamp;
    }

	public void setCreatedTimeStamp(Long createdTimeStamp) {
    	this.createdTimeStamp = createdTimeStamp;
    }

	public String getName() {
    	return name;
    }

	@RequiredStringValidator( message = "", key = "error.namerequired" )
	public void setName(String name) {
    	this.name = name;
    }

	public String getFrequency() {
    	return frequency;
    }

	public void setFrequency(String frequency) {
    	this.frequency = frequency;
    }

	public String getPeriodStart() {
    	return periodStart;
    }

	public void setPeriodStart(String periodStart) {
    	this.periodStart = periodStart;
    }

	public String getPeriodEnd() {
    	return periodEnd;
    }

	public void setPeriodEnd(String periodEnd) {
    	this.periodEnd = periodEnd;
    }

	public Long getAssetTypeId() {
    	return assetTypeId;
    }

	public void setAssetTypeId(Long assetTypeId) {
    	this.assetTypeId = assetTypeId;
    }

	public Long getEventTypeId() {
    	return eventTypeId;
    }

	public void setEventTypeId(Long eventTypeId) {
    	this.eventTypeId = eventTypeId;
    }

	public List<String> getAddresses() {
    	return addresses;
    }

	@EmailValidator(message="", key = "error.emailformat")
	public void setAddresses(List<String> addresses) {
    	this.addresses = addresses;
    }

	public Boolean getIncludeOverdue() {
		return includeOverdue;
	}

	public void setIncludeOverdue(Boolean includeOverdue) {
		this.includeOverdue = includeOverdue;
	}

	public void setIncludeUpcoming(Boolean includeUpcoming) {
		this.includeUpcoming = includeUpcoming;
	}

	public Boolean getIncludeUpcoming() {
		return includeUpcoming;
	}
	
	@FieldExpressionValidator(message="", key="error.you_must_select_at_least_one_type_of_report", expression="reportSelected == true")
	public boolean isReportSelected() {
		return includeUpcoming || includeOverdue || includeFailed;
	}

	public Boolean getIncludeFailed() {
		return includeFailed;
	}

	public void setIncludeFailed(Boolean includeFailed) {
		this.includeFailed = includeFailed;
	}

	public Boolean getSendBlankReport() {
		return sendBlankReport;
	}

	public void setSendBlankReport(Boolean sendBlankReport) {
		this.sendBlankReport = sendBlankReport;
	}

	public Long getAssetTypeGroupId() {
		return assetTypeGroupId;
	}

	public void setAssetTypeGroupId(Long assetTypeGroupId) {
		this.assetTypeGroupId = assetTypeGroupId;
	}

	public Long getAssetStatus() {
		return assetStatus;
	}

	public void setAssetStatus(Long assetStatus) {
		this.assetStatus = assetStatus;
	}
	
}
