package com.n4systems.fieldidadmin.actions;

import java.util.Collection;

import com.n4systems.ejb.legacy.Option;

import rfid.ejb.entity.FindAssetOptionManufacture;

public class FindAssetOptionAction extends AbstractAdminAction {
	private static final long serialVersionUID = 1L;
	
	private Option optionEJBContainer;
	
	private FindAssetOptionManufacture findAssetOption;
	private Collection<FindAssetOptionManufacture> findAssetOptions;
	private Long id;
	
	public String doList() {
		findAssetOptions = optionEJBContainer.getAllFindAssetOptionManufacture();
		
		return SUCCESS;
	}
	
	public String doFormInput() {
		if (id != null) {
			findAssetOption = optionEJBContainer.getFindAssetOptionManufacture(id);
		}
		
		return INPUT;
	}
	
	public String doSave() {
		if (id != null) {
			findAssetOption.setUniqueID(id);
			optionEJBContainer.updateFindAssetOptionManufacture(findAssetOption);
		} else {
			// not implemented
			return ERROR;
		}
		
		return SUCCESS;
	}
	


	public Option getOptionEJBContainer() {
		return optionEJBContainer;
	}

	public void setOptionEJBContainer(Option optionManager) {
		this.optionEJBContainer = optionManager;
	}

	public FindAssetOptionManufacture getFindAssetOption() {
		return findAssetOption;
	}

	public void setFindAssetOption(
			FindAssetOptionManufacture findAssetOption) {
		this.findAssetOption = findAssetOption;
	}

	public Collection<FindAssetOptionManufacture> getFindAssetOptions() {
		return findAssetOptions;
	}

	public void setFindAssetOptions(
			Collection<FindAssetOptionManufacture> findAssetOptions) {
		this.findAssetOptions = findAssetOptions;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	
}
