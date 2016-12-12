package com.n4systems.model.utils;

import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeSchedule;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.Tenant;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CleanAssetTypeFactory {

	private AssetType originalType;
	private Tenant targetTenant;

	public CleanAssetTypeFactory(AssetType originalType , Tenant targetTenant) {
		this.originalType = originalType;
		this.targetTenant = targetTenant;
	}
	
	public AssetType clean() {
		cleanAssetType();
		cleanInfoFields();
		setNewTenant();
		return originalType;
	}

	private void setNewTenant() {
		originalType.setTenant(targetTenant);
	}

	private void cleanAssetType() {
		// clear the asset name and all ids
		originalType.setId(null);
		originalType.setCreated(null);
		originalType.setModified(null);
		originalType.setModifiedBy(null);
		originalType.setImageName(null);
		originalType.setGroup(null);
		originalType.setAttachments(new ArrayList<FileAttachment>());
		originalType.setSchedules(new HashSet<AssetTypeSchedule>());
		originalType.setSubTypes(new HashSet<AssetType>());
		originalType.setSchedules(new HashSet<AssetTypeSchedule>());
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
