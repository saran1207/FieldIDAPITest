package com.n4systems.fieldid.actions.helpers;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.model.Asset;
import com.n4systems.model.SubAsset;
import com.n4systems.model.api.NamedEntity;

public class SubAssetHelper implements NamedEntity {

	private String label;
	private Asset asset;

	public SubAssetHelper() {
	}

	public SubAssetHelper(String label, Asset asset) {
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

	public static List<SubAssetHelper> convert(List<SubAsset> subAssets) {
		List<SubAssetHelper> subComponents = new ArrayList<SubAssetHelper>();
		for (SubAsset subAsset : subAssets) {
			if (subAsset != null) {
				subComponents.add(new SubAssetHelper(subAsset.getLabel(), subAsset.getAsset()));
			}
		}
		return subComponents;
	}

}
