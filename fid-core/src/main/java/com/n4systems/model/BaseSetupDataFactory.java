package com.n4systems.model;

import java.util.ArrayList;
import java.util.List;

import rfid.ejb.entity.SerialNumberCounterBean;

import com.n4systems.model.tenant.SetupDataLastModDates;
import com.n4systems.util.DateHelper;

public class BaseSetupDataFactory {
	private final Tenant tenant;
	
	public BaseSetupDataFactory(Tenant tenant) {
		this.tenant = tenant;
	}
	
	public SetupDataLastModDates createSetupDataLastModDates() {
		return new SetupDataLastModDates(tenant);
	}
	
	public SerialNumberCounterBean createSerialNumberCounterBean() {
		SerialNumberCounterBean serialNumberCounter = new SerialNumberCounterBean();
		serialNumberCounter.setTenant(tenant);
		serialNumberCounter.setCounter(1L);
		serialNumberCounter.setDecimalFormat("000000");
		serialNumberCounter.setDaysToReset(365L);
		serialNumberCounter.setLastReset(DateHelper.getFirstDayOfThisYear());
		return serialNumberCounter;
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
	
	private State createState(String displayText, String buttonName, Status status) {
		State state = new State();
		state.setTenant(tenant);
		state.setButtonName(buttonName);
		state.setDisplayText(displayText);
		state.setStatus(status);
		return state;
	}
	
	public StateSet createPassFailStateSet() {
		StateSet stateSet = new StateSet();
		stateSet.setTenant(tenant);
		stateSet.setName("Pass, Fail");
		stateSet.getStates().add(createState("Pass", "btn0", Status.PASS));
		stateSet.getStates().add(createState("Fail", "btn1", Status.FAIL));
		return stateSet;
	}
	
	public StateSet createNAPassFailStateSet() {
		StateSet stateSet = new StateSet();
		stateSet.setTenant(tenant);
		stateSet.setName("NA, Pass, Fail");
		stateSet.getStates().add(createState("NA", "btn2", Status.NA));
		stateSet.getStates().add(createState("Pass", "btn0", Status.PASS));
		stateSet.getStates().add(createState("Fail", "btn1", Status.FAIL));
		return stateSet;
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
}
