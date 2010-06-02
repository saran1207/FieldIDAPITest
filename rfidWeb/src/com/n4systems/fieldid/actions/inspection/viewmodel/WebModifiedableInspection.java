package com.n4systems.fieldid.actions.inspection.viewmodel;

import java.util.Date;

import com.n4systems.fieldid.actions.api.UserDateFormatValidator;
import com.n4systems.fieldid.actions.helpers.SessionUserDateConverter;
import com.n4systems.fieldid.actions.utils.OwnerPicker;
import com.n4systems.model.Inspection;
import com.n4systems.model.orgs.BaseOrg;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.ValidationParameter;

public class WebModifiedableInspection implements UserDateFormatValidator {

	private final OwnerPicker ownerPicker;
	private final SessionUserDateConverter dateConverter;
	
	
	private String location;
	
	private Date utcDatePerformed;
	private String datePerformed;
	
	

	public WebModifiedableInspection(OwnerPicker ownerPicker, SessionUserDateConverter dateConverter) {
		super();
		this.ownerPicker = ownerPicker;
		this.dateConverter = dateConverter;
	}


	public Long getOwnerId() {
		return ownerPicker.getOwnerId();
	}

	
	public void setOwnerId(Long id) {
		ownerPicker.setOwnerId(id);
	}

	@RequiredFieldValidator(message="", key="error.owner_required")
	public BaseOrg getOwner() {
		return ownerPicker.getOwner();
	}
	
	
	public void setLocation(String location) {
		this.location = location;
	}

	public String getLocation() {
		return location;
	}
	
	
	public void updateValuesToMatch(Inspection inspection) {
		location = inspection.getLocation();
		ownerPicker.updateOwner(inspection.getOwner());
		utcDatePerformed = inspection.getDate();
	}
	
	
	public void pushValuesTo(Inspection inspection) {
		inspection.setLocation(location);
		inspection.setOwner(ownerPicker.getOwner());
		inspection.setDate(datePerformed != null ? dateConverter.convertDateTime(datePerformed) : null);
	}
	
	public String getDatePerformed() {
		if (datePerformed == null) {
			datePerformed = dateConverter.convertDateTime(utcDatePerformed);
		}
		return datePerformed;
	}

	@RequiredStringValidator(message = "", key = "error.mustbeadatetime")
	@CustomValidator(type = "n4systemsDateValidator", message = "", key = "error.mustbeadatetime", parameters = { @ValidationParameter(name = "usingTime", value = "true") })
	public void setDatePerformed(String datePerformed) {
		this.datePerformed = datePerformed;
	}


	public boolean isValidDate(String date, boolean usingTime) {
		return dateConverter.isValidDate(date, usingTime);
	}
}
