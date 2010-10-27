package com.n4systems.fieldid.actions.asset;

import java.util.List;

import com.n4systems.ejb.AssetManager;
import com.n4systems.ejb.legacy.LegacyAsset;
import com.n4systems.model.Asset;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;


import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.product.helpers.ProductLinkedHelper;
import com.n4systems.fieldid.actions.utils.OwnerPicker;
import com.n4systems.fieldid.permissions.ExtendedFeatureFilter;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.api.Listable;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;

@ExtendedFeatureFilter(requiredFeature=ExtendedFeature.PartnerCenter)
public class CustomerInformationCrud extends AbstractCrud {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(CustomerInformationCrud.class);
	
	private Asset asset;
	private AssetManager assetManager;
	private LegacyAsset legacyProductManager;
	
	private OwnerPicker ownerPicker;
	
	private List<Listable<Long>> divisions;
	
	private AssetWebModel assetWebModel = new AssetWebModel(this);

	public CustomerInformationCrud(PersistenceManager persistenceManager, AssetManager assetManager, LegacyAsset legacyAsset) {
		super(persistenceManager);
		this.assetManager = assetManager;
		this.legacyProductManager = legacyAsset;
	}

	@Override
	protected void initMemberFields() {}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		asset = assetManager.findAssetAllFields(uniqueId, getSecurityFilter());
		assetWebModel.match(asset);
	}
	
	@Override
	protected void postInit() {
		super.postInit();
		ownerPicker = new OwnerPicker(getLoaderFactory().createFilteredIdLoader(BaseOrg.class), asset);
		overrideHelper(new CustomerInformationCrudHelper(getLoaderFactory()));
	}

	private void testRequiredEntities() {
		if (asset == null) {
			addActionErrorText("error.noasset");
			throw new MissingEntityException("you must have an asset.");
		}
	}

	@SkipValidation
	public String doEdit() {
		testRequiredEntities();
		return SUCCESS;
	}

	public String doUpdate() {
		testRequiredEntities();
		
		try {	
			assetWebModel.fillInAsset(asset);
			
			legacyProductManager.update(asset, getUser());
			addFlashMessageText("message.assetupdated");
			
		} catch (Exception e) {
			addActionErrorText("error.assetsave");
			logger.error("failed to save Asset", e);
			return INPUT;
		}
		return SUCCESS;
	}

	public boolean isSubAsset() {
		return (assetManager.parentAsset(asset) != null);
	}
	
	public String getCustomerRefNumber() {
		return asset.getCustomerRefNumber();
	}


	public String getPurchaseOrder() {
		return asset.getPurchaseOrder();
	}

	public void setCustomerRefNumber(String customerRefNumber) {
		asset.setCustomerRefNumber(customerRefNumber);
	}

	
	public void setPurchaseOrder(String purchaseOrder) {
		asset.setPurchaseOrder(purchaseOrder);
	}

		
	public Asset getAsset() {
		return asset;
	}

	public List<Listable<Long>> getDivisions() {
		if (divisions == null) {
			divisions = getLoaderFactory().createFilteredListableLoader(DivisionOrg.class).load();
		}
		return divisions;
	}

	public Long getOwnerId() {
		return ownerPicker.getOwnerId();
	}

	public void setOwnerId(Long id) {
		ownerPicker.setOwnerId(id);
	}
	
	public AssetWebModel getAssetWebModel() {
		return assetWebModel;
	}
	
	@RequiredFieldValidator(message="", key="error.owner_required")
	public BaseOrg getOwner() {
		return ownerPicker.getOwner();
	}

	
	public boolean isLinked() {
		return ProductLinkedHelper.isLinked(asset, getLoaderFactory());
	}
	
	
}
