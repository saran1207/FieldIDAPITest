package com.n4systems.model.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import com.n4systems.model.FileAttachment;
import com.n4systems.model.ProductType;
import com.n4systems.model.ProductTypeSchedule;
import com.n4systems.model.Tenant;

public class CleanProductTypeFactory {

	private ProductType originalType;
	private Tenant targetTenant;

	public CleanProductTypeFactory(ProductType originalType , Tenant targetTenant) {
		super();
		this.originalType = originalType;
		this.targetTenant = targetTenant;
	}
	
	public ProductType clean() {
		cleanProductType();
		cleanInfoFields();
		setNewTenant();
		return originalType;
	}

	private void setNewTenant() {
		originalType.setTenant(targetTenant);
	}

	private void cleanProductType() {
		// clear the product name and all ids
		originalType.setId(null);
		originalType.setCreated(null);
		originalType.setModified(null);
		originalType.setModifiedBy(null);
		originalType.setImageName(null);
		originalType.setGroup(null);
		originalType.setAttachments(new HashSet<FileAttachment>());
		originalType.setSchedules(new HashSet<ProductTypeSchedule>());
		originalType.setSubTypes(new HashSet<ProductType>());
		originalType.setSchedules(new HashSet<ProductTypeSchedule>());
		originalType.setAutoAttributeCriteria(null);
	}


	protected void cleanInfoFields() {
		removedRetiredInfoFields();
		
		for (InfoFieldBean infoField : originalType.getInfoFields()) {
			infoField.setUniqueID(null);
			cleanInfoOptions(infoField);
		}
	}

	private void removedRetiredInfoFields() {
		List<InfoFieldBean> retiredInfoField = new ArrayList<InfoFieldBean>();
		for (InfoFieldBean infoField : originalType.getInfoFields()) {
			if (infoField.isRetired()) {
				retiredInfoField.add(infoField);
			}
		}
		originalType.getInfoFields().removeAll(retiredInfoField);
	}

	
	protected void cleanInfoOptions(InfoFieldBean infoField) {
		for (InfoOptionBean infoOption : infoField.getInfoOptions()) {
			infoOption.setUniqueID(null);
		}
	}
	
	
	
}
