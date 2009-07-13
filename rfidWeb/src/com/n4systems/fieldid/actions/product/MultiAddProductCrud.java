package com.n4systems.fieldid.actions.product;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import rfid.ejb.entity.ProductSerialExtensionBean;
import rfid.ejb.entity.ProductStatusBean;
import rfid.ejb.session.LegacyProductSerial;

import com.n4systems.ejb.OrderManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.SubProductUniquenessException;
import com.n4systems.fieldid.actions.helpers.InfoOptionInput;
import com.n4systems.fieldid.actions.helpers.ProductExtensionValueInput;
import com.n4systems.fieldid.actions.helpers.ProductTypeLister;
import com.n4systems.fieldid.actions.helpers.UploadAttachmentSupport;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.Product;
import com.n4systems.model.api.Note;
import com.n4systems.model.commenttemplate.CommentTemplateListableLoader;
import com.n4systems.model.customer.CustomerListableLoader;
import com.n4systems.model.jobsites.JobSiteListableLoader;
import com.n4systems.model.product.ProductAttachment;
import com.n4systems.model.product.ProductCleaner;
import com.n4systems.model.producttype.AutoAttributeCriteriaByProductTypeIdLoader;
import com.n4systems.model.user.UserListableLoader;
import com.n4systems.services.product.ProductSaveService;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.ListHelper;
import com.n4systems.util.ListingPair;

public class MultiAddProductCrud extends UploadAttachmentSupport {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(MultiAddProductCrud.class);
	
	private final LegacyProductSerial legacyProductManager;
	private final OrderManager orderManager;
	
	// drop down lists
	private List<ListingPair> jobSites;
	private List<ListingPair> employees;
	private List<ProductStatusBean> productStatuses;
	private List<ListingPair> commentTemplates;
	private List<ProductSerialExtensionBean> extentions;
	private ProductTypeLister productTypeLister;
	private List<ListingPair> customers;
	private AutoAttributeCriteria autoAttributeCriteria;
	
	// form inputs
	private List<ProductIdentifierView> identifiers = new ArrayList<ProductIdentifierView>();
	private ProductView productView = new ProductView();
	
	
	public MultiAddProductCrud(PersistenceManager persistenceManager, OrderManager orderManager, LegacyProductSerial legacyProductManager) {
		super(persistenceManager);
		this.orderManager = orderManager;
		this.legacyProductManager = legacyProductManager;
	}
	
	@Override
	protected void initMemberFields() {
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
	}

	public String doForm() {
		return SUCCESS;
	}
	
	public String doCreate() {
		ProductCleaner cleaner = new ProductCleaner();
		
		logger.info("Product Multi-Add saving " + identifiers.size() + " products");
		
		logger.info("Resolving fields on base product");
		ProductViewModeConverter converter = new ProductViewModeConverter(getLoaderFactory(), orderManager, getUser());
		Product product = converter.viewToModel(productView);
		
		try {
			ProductSaveService saver = new ProductSaveService(legacyProductManager, persistenceManager, fetchCurrentUser());
			int i = 1;
			for (ProductIdentifierView productIdent: identifiers) {
				logger.info("Saving product " + i + " of " + identifiers.size());
				
				product.setSerialNumber(productIdent.getSerialNumber());
				product.setCustomerRefNumber(productIdent.getReferenceNumber());
				product.setRfidNumber(productIdent.getRfidNumber());
				
				saver.setProduct(product);
				saver.setUploadedAttachments(copyUploadedFiles());
				saver.create();
				saver.clear();
				
				// make sure all persistence fields have been wiped
				cleaner.clean(product);
				
				i++;
			}
			
			addFlashMessage(getText("message.productscreated", new String[] {String.valueOf(identifiers.size())}));
			
		} catch (Exception e) {
			logger.error("Failed to create product.", e);
			addActionErrorText("error.productsave");
			return ERROR;
		}
		
		logger.info("Product Multi-Add Complete");
		return SUCCESS;
	}
	
	private List<ProductAttachment> copyUploadedFiles() {
		List<ProductAttachment> copiedUploadedFiles = new ArrayList<ProductAttachment>();
		for (ProductAttachment uploadedFile : getUploadedFiles()) {
			ProductAttachment copiedUploadedFile = new ProductAttachment(new Note(uploadedFile.getNote()));
			copiedUploadedFiles.add(copiedUploadedFile);
		}
		
		return copiedUploadedFiles;
	}

	public List<ListingPair> getEmployees() {
		if (employees == null) {
			UserListableLoader loader = getLoaderFactory().createUserListableLoader();
			employees = ListHelper.longListableToListingPair(loader.load());
		}
		return employees;
	}

	public List<ListingPair> getCustomers() {
		if (customers == null) {
			CustomerListableLoader loader = getLoaderFactory().createCustomerListableLoader();
			customers = ListHelper.longListableToListingPair(loader.load());
		}
		return customers;
	}

	public List<ListingPair> getDivisions() {
		// since multi add has no edit, the division list will always be loaded by ajax
		// and is initially empty
		return new ArrayList<ListingPair>();
	}
	
	public List<ListingPair> getJobSites() {
		if (jobSites == null) {
			JobSiteListableLoader loader = getLoaderFactory().createJobSiteListableLoader();
			jobSites = ListHelper.longListableToListingPair(loader.load());
		}
		return jobSites;
	}

	public List<ProductStatusBean> getProductStatuses() {
		if (productStatuses == null) {
			productStatuses = getLoaderFactory().createProductStatusListLoader().load();
		}
		return productStatuses;
	}

	public List<ListingPair> getCommentTemplates() {
		if (commentTemplates == null) {
			CommentTemplateListableLoader loader = getLoaderFactory().createCommentTemplateListableLoader();
			commentTemplates = ListHelper.longListableToListingPair(loader.load());
		}
		return commentTemplates;
	}

	public List<ProductSerialExtensionBean> getExtentions() {
		if (extentions == null) {
			extentions = getLoaderFactory().createProductSerialExtensionListLoader().load();
		}
		return extentions;
	}
	
	public ProductTypeLister getProductTypes() {
		if (productTypeLister == null) {
			productTypeLister = new ProductTypeLister(persistenceManager, getSecurityFilter());
		}

		return productTypeLister;
	}
	
	public AutoAttributeCriteria getAutoAttributeCriteria() {
		if (autoAttributeCriteria == null && productView.getProductTypeId() != null) {
			AutoAttributeCriteriaByProductTypeIdLoader loader = getLoaderFactory().createAutoAttributeCriteriaByProductTypeIdLoader();
			
			loader.setProductTypeId(productView.getProductTypeId());
			
			autoAttributeCriteria = loader.load();
		}
		return autoAttributeCriteria;
	}
	
	public String getIdentified() {
		return convertDate(new Date());
	}
	
	public Integer getMaxProducts() {
		return ConfigContext.getCurrentContext().getInteger(ConfigEntry.MAX_MULTI_ADD_SIZE, getTenantId());  
	}
	
	/*************** Form input get/set's go below here **********************/
	
	public void setOwner(Long ownerId) {
		productView.setOwner(ownerId);
	}
	
	public void setDivision(Long divisionId) {
		productView.setDivision(divisionId);
	}
	
	public void setJobSite(Long jobSiteId) {
		productView.setJobSite(jobSiteId);
	}
	
	public void setAssignedUser(Long userId) {
		productView.setAssignedUser(userId);
	}
	
	public void setProductStatus(Long statusId) {
		productView.setProductStatus(statusId);
	}
	
	public void setProductTypeId(Long typeId) {
		productView.setProductTypeId(typeId);
	}
	
	public void setLocation(String location) {
		productView.setLocation(location);
	}
	
	public void setPurchaseOrder(String purchaseOrder) {
		productView.setPurchaseOrder(purchaseOrder);
	}
	
	public void setIdentified(String identified) {
		productView.setIdentified(convertDate(identified));
	}
	
	public void setNonIntegrationOrderNumber(String orderNumber) {
		productView.setNonIntegrationOrderNumber(orderNumber);
	}
	
	public void setComments(String comments) {
		productView.setComments(comments);
	}
	
	public List<ProductExtensionValueInput> getProductExtentionValues() {
		return productView.getProductExtentionValues();
	}
	
	public List<InfoOptionInput> getProductInfoOptions() {
		return productView.getProductInfoOptions();
	}
	
	public void setProductInfoOptions(List<InfoOptionInput> infoOptions) {
		productView.setProductInfoOptions(infoOptions);
	}
	
	public List<ProductIdentifierView> getIdentifiers() {
		return identifiers;
	}

	
}
