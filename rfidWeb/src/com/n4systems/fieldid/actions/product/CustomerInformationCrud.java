package com.n4systems.fieldid.actions.product;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.session.LegacyProductSerial;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.helpers.MissingEntityException;
import com.n4systems.fieldid.permissions.ExtendedFeatureFilter;
import com.n4systems.model.Division;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.JobSite;
import com.n4systems.model.Product;
import com.n4systems.model.api.Listable;

@ExtendedFeatureFilter(requiredFeature=ExtendedFeature.PartnerCenter)
public class CustomerInformationCrud extends AbstractCrud {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(CustomerInformationCrud.class);
	
	private Product product;
	private ProductManager productManager;
	private LegacyProductSerial legacyProductManager;
	
	private List<Listable<Long>> divisions;
	private List<Listable<Long>> jobSites;
	
	
	
	public CustomerInformationCrud(PersistenceManager persistenceManager, ProductManager productManager, LegacyProductSerial legacyProductSerial) {
		super(persistenceManager);
		this.productManager = productManager;
		this.legacyProductManager = legacyProductSerial;
	}

	@Override
	protected void initMemberFields() {
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		product = productManager.findProductAllFields(uniqueId, getSecurityFilter());
		
	}
	
	private void testRequiredEntities() {
		if (product == null) {
			addActionErrorText("error.noproduct");
			throw new MissingEntityException("you must have a product.");
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
			legacyProductManager.update(product);
			addFlashMessageText("message.productupdated");
			
		} catch (Exception e) {
			addActionErrorText("error.productsave");
			logger.error("failed to save Product", e);
			return INPUT;
		}
		return SUCCESS;
	}

	public boolean isSubProduct() {
		return (productManager.parentProduct(product) != null);
	}
	
	public String getCustomerRefNumber() {
		return product.getCustomerRefNumber();
	}

	public String getLocation() {
		return product.getLocation();
	}

	public String getPurchaseOrder() {
		return product.getPurchaseOrder();
	}

	public void setCustomerRefNumber(String customerRefNumber) {
		product.setCustomerRefNumber(customerRefNumber);
	}

	public void setLocation(String location) {
		product.setLocation(location);
	}

	public void setPurchaseOrder(String purchaseOrder) {
		product.setPurchaseOrder(purchaseOrder);
	}

	public Long getDivision() {
		return (product.getDivision() != null) ? product.getDivision().getId() : null;
	}

	public Long getJobSite() {
		return (product.getJobSite() != null) ? product.getJobSite().getId() : null;
	}
	
	public void setDivision(Long division) {
		if (division == null) {
			product.setDivision(null);
		} else if (product.getDivision() == null || !product.getDivision().getId().equals(division)) {
			product.setDivision(persistenceManager.find(Division.class, division, getTenantId()));
		}
	}

	public void setJobSite(Long jobSite) {
		if (jobSite == null) {
			product.setDivision(null);
		} else if (product.getJobSite() == null || !product.getJobSite().getId().equals(jobSite)) {
			product.setJobSite(persistenceManager.find(JobSite.class, jobSite, getTenantId()));
		}
	}

	
	public Product getProduct() {
		return product;
	}

	public List<Listable<Long>> getDivisions() {
		if (divisions == null) {
			
			divisions = getLoaderFactory().createDivisionListableLoader().load();
		}
		return divisions;
	}

	public List<Listable<Long>> getJobSites() {
		if (jobSites == null) {
			jobSites = getLoaderFactory().createJobSiteListableLoader().load();
		}
		return jobSites;
	}
	
}
