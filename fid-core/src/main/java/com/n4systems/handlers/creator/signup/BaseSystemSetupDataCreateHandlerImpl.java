package com.n4systems.handlers.creator.signup;

import com.n4systems.model.EventTypeGroup;
import com.n4systems.model.assetstatus.AssetStatusSaver;
import com.n4systems.model.assettype.AssetTypeSaver;
import com.n4systems.model.eventtypegroup.EventTypeGroupSaver;
import rfid.ejb.entity.AssetStatus;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.AssetType;
import com.n4systems.model.State;
import com.n4systems.model.StateSet;
import com.n4systems.model.Status;
import com.n4systems.model.TagOption;
import com.n4systems.model.Tenant;
import com.n4systems.model.stateset.StateSetSaver;
import com.n4systems.model.tagoption.TagOptionSaver;
import com.n4systems.persistence.Transaction;

public class BaseSystemSetupDataCreateHandlerImpl implements BaseSystemSetupDataCreateHandler {
	private static final String[] DEFAULT_ASSET_STATUS_NAMES = {"In Service", "Out of Service", "In for Repair", "In need of Repair", "Destroyed"};
	
	private final TagOptionSaver tagSaver;
	private final AssetTypeSaver assetTypeSaver;
	private final EventTypeGroupSaver eventTypeGroupSaver;
	private final StateSetSaver stateSetSaver;
	private final AssetStatusSaver assetStatusSaver;
	
	private Tenant tenant;

	public BaseSystemSetupDataCreateHandlerImpl(TagOptionSaver tagSaver, AssetTypeSaver assetTypeSaver, EventTypeGroupSaver eventTypeGroupSaver, StateSetSaver stateSetSaver, AssetStatusSaver assetStatusSaver) {
		this.tagSaver = tagSaver;
		this.assetTypeSaver = assetTypeSaver;
		this.eventTypeGroupSaver = eventTypeGroupSaver;
		this.stateSetSaver = stateSetSaver;
		this.assetStatusSaver = assetStatusSaver;
	}
	
	public void create(Transaction transaction) {
		if (invalidTenant()) {
			throw new InvalidArgumentException("you must set a saved tenant.");
		}

		createDefaultTagOptions(transaction);
		createDefaultAssetType(transaction);
		createDefaultEventTypeGroups(transaction);
		createDefaultStateSets(transaction);
		createDefaultAssetStatuses(transaction);
	}

	private boolean invalidTenant() {
		return tenant == null || tenant.isNew();
	}
	
	private void createDefaultAssetStatuses(Transaction transaction) {
		for (String psName: DEFAULT_ASSET_STATUS_NAMES) {
			createAssetStatus(transaction, psName);
		}
	}
	
	public void createAssetStatus(Transaction transaction, String name) {
		AssetStatus pStatus = new AssetStatus();
		pStatus.setTenant(tenant);
		pStatus.setName(name);
		
		assetStatusSaver.save(transaction, pStatus);
	}
	
	private void createDefaultTagOptions(Transaction transaction) {
		TagOption tagOption = new TagOption();
		tagOption.setTenant(tenant);
		tagOption.setOptionKey(TagOption.OptionKey.SHOPORDER);

		tagSaver.save(transaction, tagOption);
	}

	private void createDefaultAssetType(Transaction transaction) {
		AssetType assetType = new AssetType();
		assetType.setTenant(tenant);
		assetType.setName("*");
		assetTypeSaver.save(transaction, assetType);
	}

	private void createDefaultEventTypeGroups(Transaction transaction) {
		eventTypeGroupSaver.save(transaction, createVisualInspectionTypeGroup());
	}

	private EventTypeGroup createVisualInspectionTypeGroup() {
		EventTypeGroup visualEvent = new EventTypeGroup();
		visualEvent.setName("Visual Inspection");
		visualEvent.setReportTitle("Visual Inspection");
		visualEvent.setTenant(tenant);
		return visualEvent;
	}

	private void createDefaultStateSets(Transaction transaction) {
		stateSetSaver.save(transaction, createPassFailSet());
		stateSetSaver.save(transaction, createNAPassFailSet());
	}

	private StateSet createPassFailSet() {
		StateSet passFail = new StateSet();
		passFail.setName("Pass, Fail");
		passFail.setTenant(tenant);

		passFail.getStates().add(createPassState());
		passFail.getStates().add(createFailState());
		return passFail;
	}

	private StateSet createNAPassFailSet() {
		StateSet naPassFail = new StateSet();
		naPassFail.setName("NA, Pass, Fail");
		naPassFail.setTenant(tenant);

		naPassFail.getStates().add(createNAState());
		naPassFail.getStates().add(createPassState());
		naPassFail.getStates().add(createFailState());
		return naPassFail;
	}

	private State createNAState() {
		State na = new State();
		na.setTenant(tenant);
		na.setButtonName("btn2");
		na.setDisplayText("NA");
		na.setStatus(Status.NA);
		return na;
	}

	private State createFailState() {
		State fail = new State();
		fail.setTenant(tenant);
		fail.setButtonName("btn1");
		fail.setDisplayText("Fail");
		fail.setStatus(Status.FAIL);
		return fail;
	}

	private State createPassState() {
		State pass = new State();
		pass.setTenant(tenant);
		pass.setButtonName("btn0");
		pass.setDisplayText("Pass");
		pass.setStatus(Status.PASS);
		return pass;
	}

	public BaseSystemSetupDataCreateHandler forTenant(Tenant tenant) {
		this.tenant = tenant;
		return this;
	}

}
