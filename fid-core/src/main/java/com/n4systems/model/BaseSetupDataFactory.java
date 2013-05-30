package com.n4systems.model;

import com.n4systems.model.tenant.SetupDataLastModDates;
import com.n4systems.util.DateHelper;
import rfid.ejb.entity.IdentifierCounter;

import java.util.ArrayList;
import java.util.List;

public class BaseSetupDataFactory {
	private final Tenant tenant;
	
	public BaseSetupDataFactory(Tenant tenant) {
		this.tenant = tenant;
	}
	
	public SetupDataLastModDates createSetupDataLastModDates() {
		return new SetupDataLastModDates(tenant);
	}
	
	public IdentifierCounter createSerialNumberCounterBean() {
		IdentifierCounter identifierCounter = new IdentifierCounter();
		identifierCounter.setTenant(tenant);
		identifierCounter.setCounter(1L);
		identifierCounter.setDecimalFormat("000000");
		identifierCounter.setDaysToReset(365L);
		identifierCounter.setLastReset(DateHelper.getFirstDayOfThisYear());
		return identifierCounter;
	}
	
	public TagOption createTagOption() {
		TagOption tagOption = new TagOption();
		tagOption.setTenant(tenant);
		tagOption.setOptionKey(TagOption.OptionKey.SHOPORDER);
		return tagOption;
	}
	
	public AssetType createAssetType() {
		AssetType assetType = new AssetType();
		assetType.setTenant(tenant);
		assetType.setName("*");
		return assetType;
	}
	
	public EventTypeGroup createEventTypeGroup() {
		EventTypeGroup eventTypeGroup = new EventTypeGroup();
		eventTypeGroup.setTenant(tenant);
		eventTypeGroup.setName("Visual Inspection");
		eventTypeGroup.setReportTitle("Visual Inspection");
		return eventTypeGroup;
	}
	
	private Button createState(String displayText, String buttonName, EventResult eventResult) {
		Button button = new Button();
		button.setTenant(tenant);
		button.setButtonName(buttonName);
		button.setDisplayText(displayText);
		button.setEventResult(eventResult);
		return button;
	}
	
	public ButtonGroup createPassFailStateSet() {
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.setTenant(tenant);
		buttonGroup.setName("Pass, Fail");
		buttonGroup.getButtons().add(createState("Pass", "btn0", EventResult.PASS));
		buttonGroup.getButtons().add(createState("Fail", "btn1", EventResult.FAIL));
		return buttonGroup;
	}
	
	public ButtonGroup createNAPassFailStateSet() {
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.setTenant(tenant);
		buttonGroup.setName("NA, Pass, Fail");
		buttonGroup.getButtons().add(createState("NA", "btn2", EventResult.NA));
		buttonGroup.getButtons().add(createState("Pass", "btn0", EventResult.PASS));
		buttonGroup.getButtons().add(createState("Fail", "btn1", EventResult.FAIL));
		return buttonGroup;
	}
	
	public List<AssetStatus> createAssetStatuses() {
		List<AssetStatus> statuses = new ArrayList<AssetStatus>();
		for (String statusName: new String[] {"In Service", "Out of Service", "In for Repair", "In need of Repair", "Destroyed"}) {
			AssetStatus status = new AssetStatus();
			status.setTenant(tenant);
			status.setName(statusName);
			statuses.add(status);
		}
		return statuses;
	}
    
    public List<EventStatus> createEventStatuses() {
        List<EventStatus> statuses = new ArrayList<EventStatus>();
        for (String statusName: new String [] {"Could Not Inspect", "Not In Use", "Destroyed"}) {
            EventStatus status = new EventStatus();
            status.setTenant(tenant);
            status.setName(statusName);
            statuses.add(status);
        }
        return statuses;
    }
}
