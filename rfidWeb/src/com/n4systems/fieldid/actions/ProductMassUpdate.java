package com.n4systems.fieldid.actions;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.entity.ProductStatusBean;

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
import com.n4systems.model.Product;
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

	private Product product = new Product();
	private List<Listable<Long>> employees;
	
	private String identified;
	private OwnerPicker ownerPicker;
	
	private AssetWebModel asset = new AssetWebModel(this);
	
	public ProductMassUpdate(MassUpdateManager massUpdateManager, LegacyProductSerial productSerialManager, PersistenceManager persistenceManager) {
		super(massUpdateManager, persistenceManager);
		this.productSerialManager = productSerialManager;
	}

	public void prepare() throws Exception {
		ownerPicker = new OwnerPicker(getLoaderFactory().createFilteredIdLoader(BaseOrg.class), product);
		overrideHelper(new MassUpdateProductHelper(getLoaderFactory()));
	}
	
	
	
	private void applyCriteriaDefaults() {
	
		setOwnerId(criteria.getOwnerId());
		
		setProductStatus(criteria.getProductStatus());
		setPurchaseOrder(criteria.getPurchaseOrder());
		setAssignedUser(criteria.getAssignedUser());
	}

	@SkipValidation
	public String doEdit() {
		if (!findCriteria()) {
			addFlashErrorText("error.searchexpired");
			return ERROR;
		}
		
		identified = convertDate(product.getIdentified());

		applyCriteriaDefaults();
		
		asset.match(product);
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
			
			product.setIdentified(convertDate(identified));
			asset.fillInAsset(product);
			List<Long> ids = getSearchIds(criteria, criteria.getSecurityFilter());
			
			Long results = massUpdateManager.updateProducts(ids, product, select, fetchCurrentUser());
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

	
	

	public Long getProductStatus() {
		return (product.getProductStatus() == null) ? null : product.getProductStatus().getUniqueID();
	}

	public void setProductStatus(Long productStatus) {
		if (productStatus == null) {
			product.setProductStatus(null);
		} else if (product.getProductStatus() == null || !productStatus.equals(product.getProductStatus().getUniqueID())) {
			ProductStatusBean productStatusBean = productSerialManager.findProductStatus(productStatus, getTenantId());
			product.setProductStatus(productStatusBean);
		}
	}


	public String getPurchaseOrder() {
		return product.getPurchaseOrder();
	}

	public void setPurchaseOrder(String purcahseOrder) {
		product.setPurchaseOrder(purcahseOrder);
	}

	public List<ProductStatusBean> getProductStatuses() {
		return getLoaderFactory().createProductStatusListLoader().load();
	}
	
	
	public Long getAssignedUser() {
		return ( product.getAssignedUser() != null ) ? product.getAssignedUser().getId() : null;
	}
	
	public void setAssignedUser(Long user) {
		if(user == null) {
			product.setAssignedUser(null);
		} else if (product.getAssignedUser() == null || !user.equals(product.getAssignedUser().getId())) {
			product.setAssignedUser(getLoaderFactory().createUserFilteredLoader().setId(user).load());
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
		product.setPublished(PublishedState.valueOf(stateName).isPublished());
	}
	
	public String getPublished() {
		return PublishedState.resolvePublishedState(product.isPublished()).name();
	}
	
	public List<StringListingPair> getPublishedStates() {
		return PublishedState.getPublishedStates(this);
	}

	public AssetWebModel getAsset() {
		return asset;
	}
}
