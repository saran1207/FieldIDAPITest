package com.n4systems.fieldid.actions.inspection.viewmodel;

import java.util.Date;

import com.n4systems.fieldid.actions.api.UserDateFormatValidator;
import com.n4systems.fieldid.actions.helpers.UserDateConverter;
import com.n4systems.fieldid.actions.utils.OwnerPicker;
import com.n4systems.model.Inspection;
import com.n4systems.model.orgs.BaseOrg;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.ValidationParameter;

public class WebModifiedableInspection implements UserDateFormatValidator {

	private final OwnerPicker ownerPicker;
	private final UserDateConverter dateConverter;
	
	
	private String location;
	
	private Date utcInspectionDate;
	private String inspectionDate;
	
	

	public WebModifiedableInspection(OwnerPicker ownerPicker, UserDateConverter dateConverter) {
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
		utcInspectionDate = inspection.getDate();
	}
	
	
	public void pushValuesTo(Inspection inspection) {
		inspection.setLocation(location);
		inspection.setOwner(ownerPicker.getOwner());
		inspection.setDate(inspectionDate != null ? dateConverter.convertDateTime(inspectionDate) : null);
	}
	
	public String getInspectionDate() {
		if (inspectionDate == null) {
			inspectionDate = dateConverter.convertDateTime(utcInspectionDate);
		}
		return inspectionDate;
	}

	@RequiredStringValidator(message = "", key = "error.mustbeadatetime")
	@CustomValidator(type = "n4systemsDateValidator", message = "", key = "error.mustbeadatetime", parameters = { @ValidationParameter(name = "usingTime", value = "true") })
	public void setInspectionDate(String inspectionDate) {
		this.inspectionDate = inspectionDate;
	}


	public boolean isValidDate(String date, boolean usingTime) {
		return dateConverter.isValidDate(date, usingTime);
	}
}
