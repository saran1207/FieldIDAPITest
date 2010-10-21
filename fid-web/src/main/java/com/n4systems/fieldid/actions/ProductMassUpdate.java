package com.n4systems.fieldid.actions;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.model.Asset;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.entity.AssetStatus;

import com.n4systems.ejb.MassUpdateManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.LegacyProductSerial;
import com.n4systems.exceptions.UpdateConatraintViolationException;
import com.n4systems.exceptions.UpdateFailureException;
import com.n4systems.fieldid.actions.helpers.MassUpdateProductHelper;
import com.n4systems.fieldid.actions.product.AssetWebModel;
import com.n4systems.fieldid.actions.product.PublishedState;
import com.n4systems.fieldid.actions.search.ProductSearchAction;
import com.n4systems.fieldid.actions.utils.OwnerPicker;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.viewhelpers.ProductSearchContainer;
import com.n4systems.model.api.Listable;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.UserListableLoader;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.security.Permissions;
import com.n4systems.util.StringListingPair;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;

@UserPermissionFilter(userRequiresOneOf={Permissions.Tag})
public class ProductMassUpdate extends MassUpdate implements Preparable {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(ProductMassUpdate.class);
	
	private LegacyProductSerial productSerialManager;
	private ProductSearchContainer criteria;

	private Asset asset = new Asset();
	private List<Listable<Long>> employees;
	
	private String identified;
	private OwnerPicker ownerPicker;
	
	private AssetWebModel assetWebModel = new AssetWebModel(this);
	
	public ProductMassUpdate(MassUpdateManager massUpdateManager, LegacyProductSerial productSerialManager, PersistenceManager persistenceManager) {
		super(massUpdateManager, persistenceManager);
		this.productSerialManager = productSerialManager;
	}

	public void prepare() throws Exception {
		ownerPicker = new OwnerPicker(getLoaderFactory().createFilteredIdLoader(BaseOrg.class), asset);
		overrideHelper(new MassUpdateProductHelper(getLoaderFactory()));
	}
	
	private void applyCriteriaDefaults() {
	
		setOwnerId(criteria.getOwnerId());
		
		setAssetStatus(criteria.getAssetStatus());
		setPurchaseOrder(criteria.getPurchaseOrder());
		setAssignedUser(criteria.getAssignedUser());
	}

	@SkipValidation
	public String doEdit() {
		if (!findCriteria()) {
			addFlashErrorText("error.searchexpired");
			return ERROR;
		}
		
		identified = convertDate(asset.getIdentified());

		applyCriteriaDefaults();
		
		assetWebModel.match(asset);
		return SUCCESS;
	}

	public String doSave() {
		if (!findCriteria()) {
			addFlashErrorText("error.searchexpired");
			return ERROR;
		}
		
		if (select.get("identified") != null && select.get("identified")) {
			if (identified == null || identified.length() == 0) {
				addFlashErrorText("error.identifiedrequired");
				return INPUT;
			}
		}

		try {
			
			asset.setIdentified(convertDate(identified));
			assetWebModel.fillInAsset(asset);
			List<Long> ids = getSearchIds(criteria, criteria.getSecurityFilter());
			
			Long results = massUpdateManager.updateProducts(ids, asset, select, fetchCurrentUser());
			List<String> messageArgs = new ArrayList<String>();
			messageArgs.add(results.toString());
			addFlashMessage(getText("message.productmassupdatesuccessful", messageArgs));

			return SUCCESS;
		} catch (UpdateFailureException ufe) {
			logger.error("failed to run a mass update on products", ufe);
		} catch (UpdateConatraintViolationException ucve) {
			addActionError(getText("error.massupdateproductconstriantviolation"));
			return INPUT;
		} catch (Exception e) {
			logger.error("failed to run a mass update on products", e);
		}

		addActionError(getText("error.failedtomassupdate"));
		return INPUT;
	}

	public FilteredIdLoader<BaseOrg> getOrgLoader() {
		return getLoaderFactory().createFilteredIdLoader(BaseOrg.class);
	}
	
	private boolean findCriteria() {
		if (getSession().containsKey(ProductSearchAction.SEARCH_CRITERIA) && getSession().get(ProductSearchAction.SEARCH_CRITERIA) != null) {
			criteria = (ProductSearchContainer)getSession().get(ProductSearchAction.SEARCH_CRITERIA);
		}

		if (criteria == null || searchId == null || !searchId.equals(criteria.getSearchId())) {
			return false;
		}
		return true;
	}

	public Long getAssetStatus() {
		return (asset.getAssetStatus() == null) ? null : asset.getAssetStatus().getUniqueID();
	}

	public void setAssetStatus(Long statusId) {
		if (statusId == null) {
			asset.setAssetStatus(null);
		} else if (asset.getAssetStatus() == null || !statusId.equals(asset.getAssetStatus().getUniqueID())) {
			AssetStatus assetStatus = productSerialManager.findProductStatus(statusId, getTenantId());
			asset.setAssetStatus(assetStatus);
		}
	}

	public String getPurchaseOrder() {
		return asset.getPurchaseOrder();
	}

	public void setPurchaseOrder(String purcahseOrder) {
		asset.setPurchaseOrder(purcahseOrder);
	}

	public List<AssetStatus> getAssetStatuses() {
		return getLoaderFactory().createProductStatusListLoader().load();
	}
	
	public Long getAssignedUser() {
		return ( asset.getAssignedUser() != null ) ? asset.getAssignedUser().getId() : null;
	}
	
	public void setAssignedUser(Long user) {
		if(user == null) {
			asset.setAssignedUser(null);
		} else if (asset.getAssignedUser() == null || !user.equals(asset.getAssignedUser().getId())) {
			asset.setAssignedUser(getLoaderFactory().createUserFilteredLoader().setId(user).load());
		}
	}
	
	public List<Listable<Long>> getEmployees() {
		if( employees == null ) {
			UserListableLoader loader = getLoaderFactory().createHistoricalEmployeesListableLoader();
			employees = loader.load();
		}
		return employees;
	}

	public String getIdentified() {
		return identified;
	}

	@CustomValidator(type = "n4systemsDateValidator",  message = "", key = "error.mustbeadate" )	
	public void setIdentified(String identified) {
		this.identified = identified;
	}

	public BaseOrg getOwner() {
		return ownerPicker.getOwner();
	}

	public Long getOwnerId() {
		return ownerPicker.getOwnerId();
	}

	public void setOwnerId(Long id) {
		ownerPicker.setOwnerId(id);
	}

	public void setPublished(String stateName) {
		asset.setPublished(PublishedState.valueOf(stateName).isPublished());
	}
	
	public String getPublished() {
		return PublishedState.resolvePublishedState(asset.isPublished()).name();
	}
	
	public List<StringListingPair> getPublishedStates() {
		return PublishedState.getPublishedStates(this);
	}

	public AssetWebModel getAssetWebModel() {
		return assetWebModel;
	}
}
