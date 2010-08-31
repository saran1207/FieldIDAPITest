package com.n4systems.fieldid.actions;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;


import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.UnitOfMeasureManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.model.UnitOfMeasure;
import com.opensymphony.xwork2.Preparable;

public class UnitOfMeasureAction extends AbstractAction implements  Preparable {

	private static final long serialVersionUID = 1L;

	private final UnitOfMeasureManager unitOfMeasureManager;

	private List<UnitOfMeasure> unitOfMeasures;
	
	private Long unitOfMeasureId;
	
	private UnitOfMeasure selectedUnitOfMeasure;
	private Long infoFieldId;

	private List<String> unitInputs;
	private String unitString = "";

	public UnitOfMeasureAction(PersistenceManager persistenceManager, UnitOfMeasureManager unitOfMeasureManager) {
		super(persistenceManager);
		this.unitOfMeasureManager = unitOfMeasureManager;
	}
	
	
	public void prepare() throws Exception {
		if(getUnitOfMeasureId() != null) {
			loadMemberFields(getUnitOfMeasureId());
		} else {
			initMemberFields();
		}		
	}

	protected void initMemberFields() {
		if (infoFieldId != null) {
			selectedUnitOfMeasure = unitOfMeasureManager.getUnitOfMeasureForInfoField(infoFieldId);
		}

		if (selectedUnitOfMeasure == null) {
			selectedUnitOfMeasure = getUnitOfMeasures().get(0);
		}

	}

	protected void loadMemberFields(Long uniqueId) {
		selectedUnitOfMeasure = persistenceManager.find(UnitOfMeasure.class, uniqueId);

		if (selectedUnitOfMeasure == null) {
			initMemberFields();
		}

	}

	@SkipValidation
	public String doShow() {
		unitOfMeasureId = selectedUnitOfMeasure.getId();
		return SUCCESS;
	}

	@SkipValidation
	public String doSelect() {

		unitString = "";
		for (int i = 0; i < getInputOrder().size(); i++) {
			UnitOfMeasure unit = getInputOrder().get(i);

			String unitAmount = (unitInputs.size() >= i) ? unitInputs.get(i) : null;

			if (unitAmount != null && unitAmount.trim().length() > 0) {
				unitString += unitAmount.trim() + " " + unit.getShortName() + " ";
			}

		}

		return SUCCESS;
	}

	public List<UnitOfMeasure> getUnitOfMeasures() {
		if (unitOfMeasures == null) {
			unitOfMeasures = persistenceManager.findAll(UnitOfMeasure.class, "name");
		}
		return unitOfMeasures;
	}

	public UnitOfMeasure getSelectedUnitOfMeasure() {
		return selectedUnitOfMeasure;
	}

	public Long getInfoFieldId() {
		return infoFieldId;
	}

	public void setInfoFieldId(Long infoFieldId) {
		this.infoFieldId = infoFieldId;
	}

	public List<UnitOfMeasure> getInputOrder() {
		List<UnitOfMeasure> inputOrder = new ArrayList<UnitOfMeasure>();
		UnitOfMeasure currentUnitOfMeasure = selectedUnitOfMeasure;

		while (currentUnitOfMeasure != null) {
			inputOrder.add(currentUnitOfMeasure);
			currentUnitOfMeasure = currentUnitOfMeasure.getChild();
		}
		return inputOrder;
	}

	public List<String> getUnitInputs() {
		return unitInputs;
	}

	public void setUnitInputs(List<String> unitInputs) {
		this.unitInputs = unitInputs;
	}

	public String getUnitString() {
		return unitString;
	}

	public Long getUnitOfMeasureId() {
		return unitOfMeasureId;
	}

	public void setUnitOfMeasureId(Long unitOfMeasureId) {
		this.unitOfMeasureId = unitOfMeasureId;
	}

}
