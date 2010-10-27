package com.n4systems.fieldid.actions.asset;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.n4systems.fieldid.actions.helpers.AssetExtensionValueInput;
import com.n4systems.model.AssetType;
import rfid.ejb.entity.AssetSerialExtension;
import rfid.ejb.entity.AssetStatus;
import rfid.ejb.entity.InfoOptionBean;
import rfid.ejb.entity.AssetSerialExtensionValue;

import com.n4systems.ejb.OrderManager;
import com.n4systems.fieldid.actions.helpers.InfoOptionInput;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.LineItem;
import com.n4systems.model.Asset;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.assetstatus.AssetStatusFilteredLoader;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserFilteredLoader;
import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.services.TenantCache;

public class AssetViewModeConverter {
	private final LoaderFactory loaderFactory;
	private final OrderManager orderManager;
	private final User identifier;
	private Transaction transaction;
	
	public AssetViewModeConverter(LoaderFactory loaderFactory, OrderManager orderManager, User identifier) {
		this.loaderFactory = loaderFactory;
		this.orderManager = orderManager;
		this.identifier = identifier;
	}

	public Asset viewToModel(AssetView view) {
		Asset model = new Asset();

		PrimaryOrg primaryOrg = TenantCache.getInstance().findPrimaryOrg(identifier.getTenant().getId());
		
		transaction = PersistenceManager.startTransaction();
		
		try {
			model.setTenant(identifier.getTenant());
			model.setIdentifiedBy(identifier);	
			model.setOwner(view.getOwner());
			model.setType(resolveAssetType(view.getAssetTypeId()));
			model.setAssignedUser(resolveUser(view.getAssignedUser()));
			model.setAssetStatus(resolveAssetStatus(view.getAssetStatus()));
			model.setShopOrder(createNonIntegrationOrder(view.getNonIntegrationOrderNumber(), primaryOrg));
			model.setIdentified(view.getIdentified());
			model.setPurchaseOrder(view.getPurchaseOrder());
			model.setComments(view.getComments());
			
			model.setPublished(primaryOrg.isAutoPublish());
			
			List<InfoOptionBean> infoOptions = InfoOptionInput.convertInputInfoOptionsToInfoOptions(view.getAssetInfoOptions(), model.getType().getInfoFields());
			model.setInfoOptions(new TreeSet<InfoOptionBean>(infoOptions));
			
			resolveExtensionValues(view.getAssetExtentionValues(), model);
		} finally {
			transaction.commit();
		}
		
		return model;
	}
	
	private LineItem createNonIntegrationOrder(String orderNumber, PrimaryOrg primaryOrg) {
		LineItem line = null;
		// only do this if the order nuberm is not null and the tenant does not have Integration
		if (orderNumber != null && !primaryOrg.hasExtendedFeature(ExtendedFeature.Integration)) {
			line = orderManager.createNonIntegrationShopOrder(orderNumber, primaryOrg.getTenant().getId());
		}
		return line;
	}
	
		
	private AssetType resolveAssetType(Long assetTypeId) {
		AssetType assetType = null;
		if (assetTypeId != null) {
			FilteredIdLoader<AssetType> loader = loaderFactory.createFilteredIdLoader(AssetType.class).setId(assetTypeId);
			assetType = loader.load(transaction);
		}
		return assetType;
	}
	
	private User resolveUser(Long userId) {
		User user = null;
		if (userId != null) {
			UserFilteredLoader loader = loaderFactory.createUserFilteredLoader().setId(userId);
			user = loader.load(transaction);
		}
		return user;
	}
	
	private AssetStatus resolveAssetStatus(Long statusId) {
		AssetStatus status = null;
		if (statusId != null) {
			AssetStatusFilteredLoader loader = loaderFactory.createAssetStatusFilteredLoader().setId(statusId);
			status = loader.load(transaction);
		}
		return status;
	}
	
	private void resolveExtensionValues(List<AssetExtensionValueInput> assetExtentionValues, Asset asset) {
		if (assetExtentionValues != null) {
			List<AssetSerialExtension> extensions = loaderFactory.createAssetSerialExtensionListLoader().load(transaction);
			
			Set<AssetSerialExtensionValue> newExtensionValues = new TreeSet<AssetSerialExtensionValue>();
			for (AssetExtensionValueInput input : assetExtentionValues) {
				if (input != null) { // some of the inputs can be null due to the retired info fields.
					for (AssetSerialExtension extension : extensions) {
						if (extension.getUniqueID().equals(input.getExtensionId())) {
							AssetSerialExtensionValue extensionValue = input.convertToExtensionValueBean(extension, asset);
							if (extensionValue != null) {
								newExtensionValues.add(extensionValue);
							}
						}
					}
				}
			}
			
			asset.setAssetSerialExtensionValues(newExtensionValues);
		}
	}
}
