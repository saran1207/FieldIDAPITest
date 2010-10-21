package com.n4systems.taskscheduling.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;

import com.n4systems.model.Asset;
import org.apache.log4j.Logger;


import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.model.AssetType;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.user.User;
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
	private Map<Asset, Exception> failedProducts = new HashMap<Asset, Exception>();
	private User archivedBy;
	private AssetType type;
	private ProductManager productManager;
	private PersistenceManager persistenceManager;

	public ArchiveProductTypeTask() {
		persistenceManager = ServiceLocator.getPersistenceManager();
		productManager = ServiceLocator.getProductManager();
	}

	public void run() {
		archivedBy = persistenceManager.find(User.class, archivedById);
		type = persistenceManager.find(AssetType.class, productTypeId);
		if (type == null) {
			logger.error("asset type was not correctly given. It can not be deleted.");
			return;
		}

		logger.info("beginning deletion of asset type " + getProductTypeName());

		deleteAutoAttributes();
		logger.debug("completed delete auto attribute " + (autoAttributeDeleteFailed ? " failed" : "passed"));
		detachFromMasterProductTypes();
		logger.debug("completed detach " + (subProductTypeDetachFailed ? " failed" : "passed"));
		deleteProducts();
		logger.debug("completed asset deletion # of errors " + failedProducts.size());
		deleteRelatedProductCodeMappings();
		logger.debug("completed asset code mappings " + (subProductTypeDetachFailed ? " failed" : "passed"));

		logger.info("packing up results for deletion of asset type " + getProductTypeName());
		try {
			sendResultNotifications();
		} catch (Exception e) {
			logger.error("Unable to send notification for Archive Asset Type Task", e);
		}

		logger.info("completed deletion of asset type " + getProductTypeName());
	}

	private void sendResultNotifications() throws NoSuchProviderException, MessagingException {
		String subject = "Asset Type Deleted [" + getProductTypeName() + "]";
		String body;
		if (deleteFailed()) {
			subject += " with errors";
			body = "<p>All related parts of the asset type  "
					+ getProductTypeName()
					+ " could not be removed.  "
					+ "Please contact FieldId Support by sending an email to <a href=\"mailto:support@fieldid.com\">support@fieldid.com</a> or calling (416)-599-6466.</p>"
					+ "<p>Summary of failures.</p>" + "<ul>";
			if (autoAttributeDeleteFailed) {
				body += "<li>Auto Attributes could not be correctly removed.</li>";
			}
			if (productCodeMappingFailed) {
				body += "<li>Asset Code Mappings could not be correctly removed.</li>";
			}
			if (subProductTypeDetachFailed) {
				body += "<li>Asset Type could not be detached from Master Asset Types.</li>";
			}

			for (Entry<Asset, Exception> failure : failedProducts.entrySet()) {
				body += "<li>Asset " + failure.getKey().getArchivedSerialNumber() + " failed to delete</li>";
			}
			body += "</ul>";
		} else {
			body = "<p>All related parts of the asset type " + getProductTypeName() + " have been removed.</p>";
		}

		logger.info("Sending asset type deletion notification email [" + archivedBy.getEmailAddress() + "]");
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
			logger.error("failed to detach sub asset type " + getProductTypeName() + " from masters", e);
		}
	}

	private void deleteRelatedProductCodeMappings() {
		try {
			productManager.removeProductCodeMappingsThatUse(type);
		} catch (Exception e) {
			productCodeMappingFailed = true;
			logger.error("failed to remove asset code mappings using asset type " + getProductTypeName() + " from masters", e);
		}
	}

	private void deleteProducts() throws InvalidQueryException {
		List<Long> failedIds = new ArrayList<Long>();
		Map<Asset, Exception> failedProducts = new HashMap<Asset, Exception>();
		Pager<Asset> products;

		QueryBuilder<Asset> queryBuilder = new QueryBuilder<Asset>(Asset.class, new OpenSecurityFilter());
		queryBuilder.setSimpleSelect().addSimpleWhere("type", type).addSimpleWhere("state", EntityState.ACTIVE);

		while ((products = persistenceManager.findAllPaged(queryBuilder, 1, PAGE_SIZE)).getTotalResults() > 0L) {
			for (Asset product : products.getList()) {
				try {
					productManager.archive(product, archivedBy);
				} catch (Exception e) {
					logger.error("could not delete the asset " + product.getId() + " for asset type " + getProductTypeName(), e);
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
