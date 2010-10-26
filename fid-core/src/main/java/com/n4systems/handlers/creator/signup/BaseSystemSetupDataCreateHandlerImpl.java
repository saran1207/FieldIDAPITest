package com.n4systems.handlers.creator.signup;

import com.n4systems.model.producttype.AssetTypeSaver;
import rfid.ejb.entity.AssetStatus;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.InspectionTypeGroup;
import com.n4systems.model.AssetType;
import com.n4systems.model.State;
import com.n4systems.model.StateSet;
import com.n4systems.model.Status;
import com.n4systems.model.TagOption;
import com.n4systems.model.Tenant;
import com.n4systems.model.inspectiontypegroup.InspectionTypeGroupSaver;
import com.n4systems.model.productstatus.ProductStatusSaver;
import com.n4systems.model.stateset.StateSetSaver;
import com.n4systems.model.tagoption.TagOptionSaver;
import com.n4systems.persistence.Transaction;

public class BaseSystemSetupDataCreateHandlerImpl implements BaseSystemSetupDataCreateHandler {
	private static final String[] DEFAULT_ASSET_STATUS_NAMES = {"In Service", "Out of Service", "In for Repair", "In need of Repair", "Destroyed"};
	
	private final TagOptionSaver tagSaver;
	private final AssetTypeSaver assetTypeSaver;
	private final InspectionTypeGroupSaver inspectionTypeGroupSaver;
	private final StateSetSaver stateSetSaver;
	private final ProductStatusSaver productStatusSaver;
	
	private Tenant tenant;

	public BaseSystemSetupDataCreateHandlerImpl(TagOptionSaver tagSaver, AssetTypeSaver assetTypeSaver, InspectionTypeGroupSaver inspectionTypeGroupSaver, StateSetSaver stateSetSaver, ProductStatusSaver productStatusSaver) {
		this.tagSaver = tagSaver;
		this.assetTypeSaver = assetTypeSaver;
		this.inspectionTypeGroupSaver = inspectionTypeGroupSaver;
		this.stateSetSaver = stateSetSaver;
		this.productStatusSaver = productStatusSaver;
	}
	
	public void create(Transaction transaction) {
		if (invalidTenant()) {
			throw new InvalidArgumentException("you must set a saved tenant.");
		}

		createDefaultTagOptions(transaction);
		createDefaultProductType(transaction);
		createDefaultInspectionTypeGroups(transaction);
		createDefaultStateSets(transaction);
		createDefaultProductStatuses(transaction);
	}

	private boolean invalidTenant() {
		return tenant == null || tenant.isNew();
	}
	
	private void createDefaultProductStatuses(Transaction transaction) {
		for (String psName: DEFAULT_ASSET_STATUS_NAMES) {
			createProductStatus(transaction, psName);
		}
	}
	
	public void createProductStatus(Transaction transaction, String name) {
		AssetStatus pStatus = new AssetStatus();
		pStatus.setTenant(tenant);
		pStatus.setName(name);
		
		productStatusSaver.save(transaction, pStatus);
	}
	
	private void createDefaultTagOptions(Transaction transaction) {
		TagOption tagOption = new TagOption();
		tagOption.setTenant(tenant);
		tagOption.setOptionKey(TagOption.OptionKey.SHOPORDER);

		tagSaver.save(transaction, tagOption);
	}

	private void createDefaultProductType(Transaction transaction) {
		AssetType assetType = new AssetType();
		assetType.setTenant(tenant);
		assetType.setName("*");
		assetTypeSaver.save(transaction, assetType);
	}

	private void createDefaultInspectionTypeGroups(Transaction transaction) {
		inspectionTypeGroupSaver.save(transaction, createVisualInspection());
	}

	private InspectionTypeGroup createVisualInspection() {
		InspectionTypeGroup visualInspection = new InspectionTypeGroup();
		visualInspection.setName("Visual Inspection");
		visualInspection.setReportTitle("Visual Inspection");
		visualInspection.setTenant(tenant);
		return visualInspection;
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
