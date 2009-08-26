package com.n4systems.handlers.creator.signup;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.InspectionTypeGroup;
import com.n4systems.model.ProductType;
import com.n4systems.model.State;
import com.n4systems.model.StateSet;
import com.n4systems.model.Status;
import com.n4systems.model.TagOption;
import com.n4systems.model.Tenant;
import com.n4systems.model.inspectiontypegroup.InspectionTypeGroupSaver;
import com.n4systems.model.producttype.ProductTypeSaver;
import com.n4systems.model.stateset.StateSetSaver;
import com.n4systems.model.tagoption.TagOptionSaver;
import com.n4systems.persistence.Transaction;

public class BaseSystemSetupDataCreateHandlerImpl implements BaseSystemSetupDataCreateHandler {


	private final TagOptionSaver tagSaver;
	private final ProductTypeSaver productTypeSaver;
	private final InspectionTypeGroupSaver inspectionTypeGroupSaver;
	private final StateSetSaver stateSetSaver;
	
	private Tenant tenant;

	
	public BaseSystemSetupDataCreateHandlerImpl(TagOptionSaver tagSaver, ProductTypeSaver productTypeSaver, InspectionTypeGroupSaver inspectionTypeGroupSaver, StateSetSaver stateSetSaver) {
		super();
		this.tagSaver = tagSaver;
		this.productTypeSaver = productTypeSaver;
		this.inspectionTypeGroupSaver = inspectionTypeGroupSaver;
		this.stateSetSaver = stateSetSaver;
	}
	
	
	public void create(Transaction transaction) {
		if (invalidTenant()) {
			throw new InvalidArgumentException("you must set a saved tenant.");
		}

		createDefaultTagOptions(transaction);
		createDefaultProductType(transaction);
		createDefaultInspectionTypeGroups(transaction);
		createDefaultStateSets(transaction);
	}

	private boolean invalidTenant() {
		return tenant == null || tenant.isNew();
	}

	private void createDefaultTagOptions(Transaction transaction) {
		TagOption tagOption = new TagOption();
		tagOption.setTenant(tenant);
		tagOption.setKey(TagOption.OptionKey.SHOPORDER);

		tagSaver.save(transaction, tagOption);
	}

	private void createDefaultProductType(Transaction transaction) {
		ProductType productType = new ProductType();
		productType.setTenant(tenant);
		productType.setName("*");
		productTypeSaver.save(transaction, productType);
	}

	private void createDefaultInspectionTypeGroups(Transaction transaction) {
		inspectionTypeGroupSaver.save(transaction, createVisualInspection());
		inspectionTypeGroupSaver.save(transaction, createProofTest());
		inspectionTypeGroupSaver.save(transaction, createRepair());
	}

	private InspectionTypeGroup createRepair() {
		InspectionTypeGroup repair = new InspectionTypeGroup();
		repair.setName("Repair");
		repair.setReportTitle("Repair");
		repair.setTenant(tenant);
		return repair;
	}

	private InspectionTypeGroup createProofTest() {
		InspectionTypeGroup prooftest = new InspectionTypeGroup();
		prooftest.setName("Proof Test");
		prooftest.setReportTitle("Proof Test");
		prooftest.setTenant(tenant);
		return prooftest;
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
