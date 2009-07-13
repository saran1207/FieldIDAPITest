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
import com.n4systems.fieldid.viewhelpers.ProductSearchContainer;
import com.n4systems.model.Customer;
import com.n4systems.model.Division;
import com.n4systems.model.JobSite;
import com.n4systems.model.Product;
import com.n4systems.util.ListingPair;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;

public class ProductMassUpdate extends MassUpdate {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(ProductMassUpdate.class);
	
	private User userManager;
	private LegacyProductSerial productSerialManager;
	private ProductSearchContainer criteria;

	private Product product = new Product();

	private List<ListingPair> jobSites;
	private List<ListingPair> employees;
	
	private String identified;
	
	public ProductMassUpdate(CustomerManager customerManager, MassUpdateManager massUpdateManager, LegacyProductSerial productSerialManager, PersistenceManager persistenceManager, User userManager) {
		super(customerManager, massUpdateManager, persistenceManager);
		this.productSerialManager = productSerialManager;
		this.userManager = userManager;
	}

	private void applyCriteriaDefaults() {
		setCustomer(criteria.getCustomer());
		setDivision(criteria.getDivision());
		setProductStatus(criteria.getProductStatus());
		setPurchaseOrder(criteria.getPurchaseOrder());
		setJobSite(criteria.getJobSite());
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
			
			List<Long> ids = persistenceManager.idSearch(criteria);
			
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

	private boolean findCriteria() {
		if (getSession().containsKey(ProductSearchAction.SEARCH_CRITERIA) && getSession().get(ProductSearchAction.SEARCH_CRITERIA) != null) {
			criteria = (ProductSearchContainer)getSession().get(ProductSearchAction.SEARCH_CRITERIA);
		}

		if (criteria == null || searchId == null || !searchId.equals(criteria.getSearchId())) {
			return false;
		}
		return true;
	}

	public Long getCustomer() {
		return (product.getOwner() == null) ? null : product.getOwner().getId();
	}

	public void setCustomer(Long customer) {
		if (customer == null) {
			product.setOwner(null);
		} else if (product.getOwner() == null || !customer.equals(product.getOwner().getId())) {
			Customer customerBean = customerManager.findCustomer(customer, getSecurityFilter());
			product.setOwner(customerBean);
		}
	}

	public Long getDivision() {
		return (product.getDivision() == null) ? null : product.getDivision().getId();
	}

	public void setDivision(Long division) {
		if (division == null) {
			product.setDivision(null);
		} else if (product.getDivision() == null || !division.equals(product.getDivision().getId())) {
			Division divisionBean = customerManager.findDivision(division, getSecurityFilter());
			product.setDivision(divisionBean);
		}
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
	
	public Collection<ListingPair> getCustomers() {
		return customerManager.findCustomersLP(getTenantId(), getSecurityFilter());
	}

	public Collection<ListingPair> getDivisions() {
		Collection<ListingPair> divisions = new ArrayList<ListingPair>();
		if (getCustomer() != null) {
			divisions = customerManager.findDivisionsLP(getCustomer(), getSecurityFilter());
		}

		return divisions;
	}

	public Collection<ProductStatusBean> getProductStatuses() {
		return productSerialManager.getAllProductStatus(getTenantId());
	}

	public Long getJobSite() {
		return ( product.getJobSite() != null ) ? product.getJobSite().getId() : null;
	}

	
	public void setJobSite( Long jobSite ) {
		if (jobSite == null) {
			product.setJobSite( null );
		} else if (product.getJobSite() == null || !jobSite.equals(product.getJobSite().getId())) {
			product.setJobSite( persistenceManager.find( JobSite.class, jobSite, getSecurityFilter().setDefaultTargets() ) );
		}
	}

	public List<ListingPair> getJobSites() {
		if( jobSites == null ) {
			jobSites = persistenceManager.findAllLP( JobSite.class, getSecurityFilter().setDefaultTargets(), "name" );
		}
		return jobSites;
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
}
