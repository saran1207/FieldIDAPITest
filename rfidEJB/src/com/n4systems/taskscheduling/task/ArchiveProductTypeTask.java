package com.n4systems.taskscheduling.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;

import org.apache.log4j.Logger;

import rfid.ejb.entity.UserBean;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.model.Product;
import com.n4systems.model.ProductType;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.tools.Pager;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.mail.MailMessage;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class ArchiveProductTypeTask implements Runnable {
	private static final Logger logger = Logger.getLogger(ArchiveProductTypeTask.class);
	private static final int PAGE_SIZE = 100;
	
	private Long productTypeId;
	private String productTypeArchivedName;
	private Long archivedById;
	
	private boolean productCodeMappingFailed = false;
	private boolean subProductTypeDetachFailed = false;
	private boolean autoAttributeDeleteFailed = false;
	private Map<Product, Exception> failedProducts = new HashMap<Product, Exception>();
	private UserBean archivedBy;
	private ProductType type;
	private ProductManager productManager;
	private PersistenceManager persistenceManager;

	public ArchiveProductTypeTask() {
		persistenceManager = ServiceLocator.getPersistenceManager();
		productManager = ServiceLocator.getProductManager();
	}

	public void run() {
		archivedBy = persistenceManager.findLegacy(UserBean.class, archivedById);
		type = persistenceManager.find(ProductType.class, productTypeId);
		if (type == null) {
			logger.error("product type was not correctly given. It can not be deleted.");
			return;
		}

		logger.info("beginning deletion of product type " + getProductTypeName());

		deleteAutoAttributes();
		logger.debug("completed delete auto attribute " + (autoAttributeDeleteFailed ? " failed" : "passed"));
		detachFromMasterProductTypes();
		logger.debug("completed detach " + (subProductTypeDetachFailed ? " failed" : "passed"));
		deleteProducts();
		logger.debug("completed product deletion # of errors " + failedProducts.size());
		deleteRelatedProductCodeMappings();
		logger.debug("completed product code mappings " + (subProductTypeDetachFailed ? " failed" : "passed"));

		logger.info("packing up results for deletion of product type " + getProductTypeName());
		try {
			sendResultNotifications();
		} catch (Exception e) {
			logger.error("Unable to send notification for Archive Product Type Task", e);
		}

		logger.info("completed deletion of product type " + getProductTypeName());
	}

	private void sendResultNotifications() throws NoSuchProviderException, MessagingException {
		// TODO this should use a resource file to put out the correct language and move html to the template system for emails.
		String subject = "Product Type Deleted [" + getProductTypeName() + "]";
		String body;
		if (deleteFailed()) {
			subject += " with errors";
			body = "<p>All related parts of the product type  "
					+ getProductTypeName()
					+ " could not be removed.  "
					+ "Please contact FieldId Support by sending an email to <a href=\"mailto:support@n4systems.com\">support@n4systems.com</a> or calling (416)-599-6466.</p>"
					+ "<p>Summary of failures.</p>" + "<ul>";
			if (autoAttributeDeleteFailed) {
				body += "<li>Auto Attributes could not be correctly removed.</li>";
			}
			if (productCodeMappingFailed) {
				body += "<li>Product Code Mappings could not be correctly removed.</li>";
			}
			if (subProductTypeDetachFailed) {
				body += "<li>Product Type could not be detached from Master Product Types.</li>";
			}

			for (Entry<Product, Exception> failure : failedProducts.entrySet()) {
				body += "<li>Product " + failure.getKey().getArchivedSerialNumber() + " failed to delete</li>";
			}
			body += "</ul>";
		} else {
			body = "<p>All related parts of the product type " + getProductTypeName() + " have been removed.</p>";
		}

		logger.info("Sending product type deletion notification email [" + archivedBy.getEmailAddress() + "]");
		MailMessage message = new MailMessage(subject, body, archivedBy.getEmailAddress());

		if (deleteFailed()) {
			message.getBccAddresses().add(ConfigContext.getCurrentContext().getString(ConfigEntry.FIELDID_ADMINISTRATOR_EMAIL));
		}

		ServiceLocator.getMailManager().sendMessage(message);
	}

	private boolean deleteFailed() {
		return productCodeMappingFailed || subProductTypeDetachFailed || !failedProducts.isEmpty() || autoAttributeDeleteFailed;
	}

	private void deleteAutoAttributes() {
		if (type.getAutoAttributeCriteria() != null) {
			try {
				ServiceLocator.getAutoAttributeManager().delete(type.getAutoAttributeCriteria());
			} catch (Exception e) {
				autoAttributeDeleteFailed = true;
				logger.error("failed to delete auto attributes for " + getProductTypeName(), e);
			}
		}
	}

	private void detachFromMasterProductTypes() {
		try {
			productManager.removeAsASubProductType(type, archivedById);
		} catch (Exception e) {
			subProductTypeDetachFailed = true;
			logger.error("failed to detach sub product type " + getProductTypeName() + " from masters", e);
		}
	}

	private void deleteRelatedProductCodeMappings() {
		try {
			productManager.removeProductCodeMappingsThatUse(type);
		} catch (Exception e) {
			productCodeMappingFailed = true;
			logger.error("failed to remove product code mappings using product type " + getProductTypeName() + " from masters", e);
		}
	}

	private void deleteProducts() throws InvalidQueryException {
		List<Long> failedIds = new ArrayList<Long>();
		Map<Product, Exception> failedProducts = new HashMap<Product, Exception>();
		Pager<Product> products;

		QueryBuilder<Product> queryBuilder = new QueryBuilder<Product>(Product.class);
		queryBuilder.setSimpleSelect().addSimpleWhere("type", type).addSimpleWhere("state", EntityState.ACTIVE);

		while ((products = persistenceManager.findAllPaged(queryBuilder, 1, PAGE_SIZE)).getTotalResults() > 0L) {
			for (Product product : products.getList()) {
				try {
					productManager.archive(product, archivedBy);
				} catch (Exception e) {
					logger.error("could not delete the product " + product.getId() + " for product type " + getProductTypeName(), e);
					failedIds.add(product.getId());
					failedProducts.put(product, e);
					queryBuilder.addWhere(Comparator.NOTIN, "id", "id", failedIds);
				}
			}
		}
	}

	public Long getProductTypeId() {
		return productTypeId;
	}

	public void setProductTypeId(Long productTypeId) {
		this.productTypeId = productTypeId;
	}

	public String getProductTypeName() {
		return productTypeArchivedName;
	}

	public void setProductTypeName(String productTypeName) {
		this.productTypeArchivedName = productTypeName;
	}

	public Long getArchivedById() {
		return archivedById;
	}

	public void setArchivedById(Long archivedById) {
		this.archivedById = archivedById;
	}

}
