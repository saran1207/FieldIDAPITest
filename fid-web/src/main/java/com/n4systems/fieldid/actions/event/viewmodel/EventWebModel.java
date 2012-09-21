package com.n4systems.fieldid.actions.event.viewmodel;

import com.n4systems.fieldid.actions.api.LoaderFactoryProvider;
import com.n4systems.fieldid.actions.api.UserDateFormatValidator;
import com.n4systems.fieldid.actions.asset.LocationWebModel;
import com.n4systems.fieldid.actions.helpers.SessionUserDateConverter;
import com.n4systems.fieldid.actions.utils.OwnerPicker;
import com.n4systems.model.Event;
import com.n4systems.model.orgs.BaseOrg;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.ValidationParameter;

import java.util.Date;

public class EventWebModel implements UserDateFormatValidator {

	private final OwnerPicker ownerPicker;
	private final SessionUserDateConverter dateConverter;
	
	private LocationWebModel location;

	private Date utcDatePerformed;
	private String datePerformed;

    private Date utcNextDate;
    private String nextDate;
	
	private String overrideResult;
	
	private boolean locationSetFromAsset = false;
	private boolean ownerSetFromAsset = false;
	private boolean assetStatusSetFromAsset = false;

    private boolean dontModifyNextDate = false;
	
	public EventWebModel(OwnerPicker ownerPicker, SessionUserDateConverter dateConverter, LoaderFactoryProvider loaderFactoryProvider) {
		this.ownerPicker = ownerPicker;
		this.dateConverter = dateConverter;
		this.location = new LocationWebModel(loaderFactoryProvider);
	}

	public Long getOwnerId() {		
		return ownerPicker.getOwnerId();
	}

	public void setOwnerId(Long id) {
		ownerPicker.setOwnerId(id);
	}
	
	public BaseOrg getOwner(){
		return ownerPicker.getOwner();
	}
	 
	public LocationWebModel getLocation() {
		return location;
	}
	
	public void updateValuesToMatch(Event event) {
		location = location.matchLocation(event.getAdvancedLocation());
		ownerPicker.updateOwner(event.getOwner());
		utcDatePerformed = event.getDate();
        utcNextDate = event.getNextDate();
	}
	
	public void pushValuesTo(Event event) {
		if (locationSetFromAsset){
			event.setAdvancedLocation(event.getAsset().getAdvancedLocation());
		}else{
			event.setAdvancedLocation(location.createLocation());
		}
	
		if (ownerSetFromAsset){
			event.setOwner(event.getAsset().getOwner());
		}else{
			event.setOwner(ownerPicker.getOwner());
		}
		
		if(assetStatusSetFromAsset){
			event.setAssetStatus(event.getAsset().getAssetStatus());
		}
		
		event.setDate(datePerformed != null ? dateConverter.convertDateTime(datePerformed) : null);
        if (!dontModifyNextDate) {
            event.setNextDate(nextDate != null ? dateConverter.convertDateTimeWithNoTimeZone(nextDate) : null);
        }
	}
	
	public String getDatePerformed() {
		if (datePerformed == null) {
			datePerformed = dateConverter.convertDateTime(utcDatePerformed);
		}
		return datePerformed;
	}

    public String getNextDate() {
        if (nextDate == null) {
            nextDate = dateConverter.convertDateTimeWithNoTimeZone(utcNextDate);
        }
        return nextDate;
    }

	@RequiredStringValidator(message = "", key = "error.mustbeadatetime")
	@CustomValidator(type = "n4systemsDateValidator", message = "", key = "error.mustbeadatetime", parameters = { @ValidationParameter(name = "usingTime", value = "true") })
	public void setDatePerformed(String datePerformed) {
		this.datePerformed = datePerformed;
	}

    @CustomValidator(type = "n4systemsDateValidator", message = "", key = "error.mustbeadatetime", parameters = { @ValidationParameter(name = "usingTime", value = "true") })
    public void setNextDate(String nextDate) {
        this.nextDate = nextDate;
    }


	@Override
	public boolean isValidDate(String date, boolean usingTime) {
		return dateConverter.isValidDate(date, usingTime);
	}

	public String getOverrideResult() {
		return overrideResult;
	}

	public void setOverrideResult(String overrideResult) {
		this.overrideResult = overrideResult;
	}

	public void setLocationSetFromAsset(boolean isLocationSetFromAsset) {
		this.locationSetFromAsset = isLocationSetFromAsset;
	}

	public void setOwnerSetFromAsset(boolean ownerSetFromAsset) {
		this.ownerSetFromAsset = ownerSetFromAsset;
	}

    public boolean isOwnerSetFromAsset() {
        return ownerSetFromAsset;
    }

	public void setStatusSetFromAsset(boolean isStatusSetFromAsset) {
		this.assetStatusSetFromAsset  = isStatusSetFromAsset;
	}

    public boolean isDontModifyNextDate() {
        return dontModifyNextDate;
    }

    public void setDontModifyNextDate(boolean dontModifyNextDate) {
        this.dontModifyNextDate = dontModifyNextDate;
    }
}
