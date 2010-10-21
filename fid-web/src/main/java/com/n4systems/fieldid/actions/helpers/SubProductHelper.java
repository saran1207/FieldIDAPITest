package com.n4systems.fieldid.actions.helpers;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.model.Asset;
import com.n4systems.model.SubProduct;
import com.n4systems.model.api.NamedEntity;

public class SubProductHelper implements NamedEntity {

	private String label;
	private Asset asset;

	public SubProductHelper() {
	}

	public SubProductHelper(String label, Asset asset) {
		setLabel(label);
		this.asset = asset;
	}

	@Deprecated
	public String getName() {
		return getLabel();
	}

	@Deprecated
	public void setName(String label) {
		setLabel(label);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		if (label != null) {
			label = label.trim();
			if (label.length() == 0) {
				label = null;
			}
		}
		this.label = label;
	}

	public Asset getAsset() {
		return asset;
	}

	public void setAsset(Asset asset) {
		this.asset = asset;
	}

	public static List<SubProductHelper> convert(List<SubProduct> subProducts) {
		List<SubProductHelper> subComponents = new ArrayList<SubProductHelper>();
		for (SubProduct subProduct : subProducts) {
			if (subProduct != null) {
				subComponents.add(new SubProductHelper(subProduct.getLabel(), subProduct.getAsset()));
			}
		}
		return subComponents;
	}

}
