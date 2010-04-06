package com.n4systems.fieldidadmin.actions;

import java.util.Collection;

import com.n4systems.ejb.legacy.Option;

import rfid.ejb.entity.FindProductOptionManufactureBean;

public class FindProductOptionAction extends AbstractAdminAction {
	private static final long serialVersionUID = 1L;
	
	private Option optionManager;
	
	private FindProductOptionManufactureBean findProductOption;
	private Collection<FindProductOptionManufactureBean> findProductOptions;
	private Long id;
	
	public String doList() {
		findProductOptions = optionManager.getAllFindProductOptionManufacture();
		
		return SUCCESS;
	}
	
	public String doFormInput() {
		if (id != null) {
			findProductOption = optionManager.getFindProductOptionManufacture(id);
		}
		
		return INPUT;
	}
	
	public String doSave() {
		if (id != null) {
			findProductOption.setUniqueID(id);
			optionManager.updateFindProductOptionManufacture(findProductOption);
		} else {
			// not implemented
			return ERROR;
		}
		
		return SUCCESS;
	}
	


	public Option getOptionManager() {
		return optionManager;
	}

	public void setOptionManager(Option optionManager) {
		this.optionManager = optionManager;
	}

	public FindProductOptionManufactureBean getFindProductOption() {
		return findProductOption;
	}

	public void setFindProductOption(
			FindProductOptionManufactureBean findProductOption) {
		this.findProductOption = findProductOption;
	}

	public Collection<FindProductOptionManufactureBean> getFindProductOptions() {
		return findProductOptions;
	}

	public void setFindProductOptions(
			Collection<FindProductOptionManufactureBean> findProductOptions) {
		this.findProductOptions = findProductOptions;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	
}
