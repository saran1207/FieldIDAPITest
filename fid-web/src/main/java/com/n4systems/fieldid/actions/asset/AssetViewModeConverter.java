package com.n4systems.fieldid.actions.asset;

import com.n4systems.ejb.OrderManager;
import com.n4systems.fieldid.actions.helpers.InfoOptionInput;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetStatus;
import com.n4systems.model.AssetType;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.assetstatus.AssetStatusFilteredLoader;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserFilteredLoader;
import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.services.TenantFinder;
import rfid.ejb.entity.InfoOptionBean;
import rfid.web.helper.SessionUser;

import java.util.List;
import java.util.TreeSet;

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

		// TODO DD WEB-2157 : cache this result for reuse within same multi-add transaction.
		PrimaryOrg primaryOrg = TenantFinder.getInstance().findPrimaryOrg(identifier.getTenant().getId());

		// TODO DD WEB-2157 : having multiple transactions for multi-add seems slow and dangerous.
		transaction = PersistenceManager.startTransaction();
		
		try {
			model.setTenant(identifier.getTenant());
			model.setIdentifiedBy(identifier);	
			model.setOwner(view.getOwner());
			model.setType(resolveAssetType(view.getAssetTypeId()));
			model.setAssignedUser(resolveUser(view.getAssignedUser()));
			model.setAssetStatus(resolveAssetStatus(view.getAssetStatus()));
			model.setIdentified(view.getIdentified());
			model.setPurchaseOrder(view.getPurchaseOrder());
			model.setComments(view.getComments());
			
			model.setPublished(primaryOrg.isAutoPublish());
			
			List<InfoOptionBean> infoOptions = InfoOptionInput.convertInputInfoOptionsToInfoOptions(view.getAssetInfoOptions(), model.getType().getInfoFields(), new SessionUser(identifier));
			model.setInfoOptions(new TreeSet<InfoOptionBean>(infoOptions));
			
			if (view.getLineItemId() != null && primaryOrg.hasExtendedFeature(ExtendedFeature.Integration)) {
				model.setShopOrder(orderManager.findLineItemById(view.getLineItemId()));
			}
			
			if (view.getNonIntegrationOrderNumber() != null && !primaryOrg.hasExtendedFeature(ExtendedFeature.Integration)) {
				model.setNonIntergrationOrderNumber(view.getNonIntegrationOrderNumber());
			}
									
		} finally {
			transaction.commit();
		}
		
		return model;
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
}
