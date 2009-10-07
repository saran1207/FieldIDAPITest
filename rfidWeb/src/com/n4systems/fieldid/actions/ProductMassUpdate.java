package com.n4systems.fieldid.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.entity.ProductStatusBean;
import rfid.ejb.session.LegacyProductSerial;
import rfid.ejb.session.User;

import com.n4systems.ejb.CustomerManager;
import com.n4systems.ejb.MassUpdateManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.UpdateConatraintViolationException;
import com.n4systems.exceptions.UpdateFailureException;
import com.n4systems.fieldid.actions.search.ProductSearchAction;
import com.n4systems.fieldid.actions.utils.OwnerPicker;
import com.n4systems.fieldid.viewhelpers.ProductSearchContainer;
import com.n4systems.model.Product;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.util.ListingPair;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;

public class ProductMassUpdate extends MassUpdate implements Preparable {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(ProductMassUpdate.class);
	
	private User userManager;
	private LegacyProductSerial productSerialManager;
	private ProductSearchContainer criteria;

	private Product product = new Product();
	private List<ListingPair> employees;
	
	private String identified;
	
	private OwnerPicker ownerPicker;
	
	public ProductMassUpdate(CustomerManager customerManager, MassUpdateManager massUpdateManager, LegacyProductSerial productSerialManager, PersistenceManager persistenceManager, User userManager) {
		super(customerManager, massUpdateManager, persistenceManager);
		this.productSerialManager = productSerialManager;
		this.userManager = userManager;
	}

	public void prepare() throws Exception {
		ownerPicker = new OwnerPicker(getLoaderFactory().createFilteredIdLoader(BaseOrg.class), product);
	}
	
	
	private void applyCriteriaDefaults() {
	
		setOwnerId(criteria.getOwnerId());
		
		setProductStatus(criteria.getProductStatus());
		setPurchaseOrder(criteria.getPurchaseOrder());
		setAssignedUser(criteria.getAssingedUser());
	}

	@SkipValidation
	public String doEdit() {
		if (!findCriteria()) {
			addFlashError(getText("error.searchexpired"));
			return ERROR;
		}
		
		identified = convertDate(product.getIdentified());

		applyCriteriaDefaults();
		return SUCCESS;
	}

	public String doSave() {
		if (!findCriteria()) {
			addFlashError(getText("error.searchexpired"));
			return ERROR;
		}
		
		if (select.get("identified") != null && select.get("identified")) {
			if (identified == null || identified.length() == 0) {
				addFlashError(getText("error.identifiedrequired"));
				return INPUT;
			}
		}

		try {
			
			product.setIdentified(convertDate(identified));
			
			List<Long> ids = persistenceManager.idSearch(criteria, criteria.getSecurityFilter());
			
			Long results = massUpdateManager.updateProducts(ids, product, select, getSessionUser().getUniqueID() );
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

	public String getLocation() {
		return product.getLocation();
	}

	public void setLocation(String location) {
		product.setLocation(location);
	}

	public String getPurchaseOrder() {
		return product.getPurchaseOrder();
	}

	public void setPurchaseOrder(String purcahseOrder) {
		product.setPurchaseOrder(purcahseOrder);
	}

	public Collection<ProductStatusBean> getProductStatuses() {
		return productSerialManager.getAllProductStatus(getTenantId());
	}
	
	public Long getAssignedUser() {
		return ( product.getAssignedUser() != null ) ? product.getAssignedUser().getUniqueID() : null;
	}
	
	public void setAssignedUser(Long user) {
		if(user == null) {
			product.setAssignedUser(null);
		} else if (product.getAssignedUser() == null || !user.equals(product.getAssignedUser().getUniqueID())) {
			product.setAssignedUser(userManager.findUser(user, getTenantId()));
		}
	}
	
	public List<ListingPair> getEmployees() {
		if( employees == null ) {
			employees = userManager.getEmployeeList(getSecurityFilter());
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
	
}
