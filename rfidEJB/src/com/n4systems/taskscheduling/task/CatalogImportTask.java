package com.n4systems.taskscheduling.task;

import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;

import org.apache.log4j.Logger;

import rfid.ejb.entity.UserBean;
import rfid.ejb.session.LegacyProductType;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.NoAccessToTenantException;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.security.UserSecurityFilter;
import com.n4systems.services.safetyNetwork.CatalogService;
import com.n4systems.services.safetyNetwork.ImportCatalogService;
import com.n4systems.services.safetyNetwork.SafetyNetworkAccessService;
import com.n4systems.services.safetyNetwork.catalog.summary.CatalogImportSummary;
import com.n4systems.services.safetyNetwork.catalog.summary.BaseImportSummary.FailureType;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.mail.MailMessage;

public class CatalogImportTask implements Runnable {
	private static final Logger logger = Logger.getLogger(CatalogImportTask.class);

	private UserBean user;
	private PrimaryOrg primaryOrg;
	private Tenant linkedTenant;
	private Set<Long> importProductTypeIds;
	private Set<Long> importInspectionTypeIds;
	private boolean usingPackages;
	private boolean failed;
	
	private PersistenceManager persistenceManager;
	private LegacyProductType productTypeManager;
	private CatalogService linkedCatalogAccess;
	private ImportCatalogService importCatalogService;

	public void run() {
		init();
		doImport();
		try {
			sendEmail();
		} catch (Exception e) {
			logger.error("Failed to send notification email for CatalogImportTask", e);
		}
	}

	private void doImport() {
		failed = false;
		try {
			importCatalogService = new ImportCatalogService(persistenceManager, primaryOrg, linkedCatalogAccess, productTypeManager);
			importCatalogService.setImportAllRelations(true);
			importCatalogService.setImportProductTypeIds(importProductTypeIds);
			importCatalogService.setImportInspectionTypeIds(importInspectionTypeIds);
			importCatalogService.setImportAllRelations(usingPackages);
			failed = !importCatalogService.importSelection();
		} catch (Exception e) {
			failed = true;
			logger.error("exception thrown from importing a catalog.", e);
		}
	}

	private void sendEmail() throws NoSuchProviderException, MessagingException {
		String body;
		if (failed) {
			body = failureBody();
		} else {
			body = successBody();
		}
		
		logger.info("Sending catalog import notification email [" + user.getEmailAddress() + "] " + ((failed) ? "Failure" : "Successful"));
		MailMessage message = new MailMessage("Catalog Import", body, user.getEmailAddress());
		ServiceLocator.getMailManager().sendMessage(message);
	}

	private String failureBody() {
		String body = "<h4>Your Catalog Import could not be completed sucessfully. sorry</h4>";
		body += "<p>";
		CatalogImportSummary summary = importCatalogService.getSummary();
		if (summary.getProductTypeGroupImportSummary().failed()) {
			body += "Product Type Group " + summary.getProductTypeGroupImportSummary().getFailedImporting() + " could not be correctly imported.";
		} else if (summary.getProductTypeImportSummary().failed()) {
			if (summary.getProductTypeImportSummary().getFailureType() == FailureType.COULD_NOT_CREATE) {
				body += "Product Type  " + summary.getProductTypeImportSummary().getFailedImporting() + " could not be correctly imported.";
			} else {
				body += "Auto attributes for product type  " + summary.getProductTypeImportSummary().getFailedImporting() + " could not be correctly imported.";
			}
		} else if (summary.getInspectionTypeImportSummary().failed()) {
			body += "Inspection Type  " + summary.getInspectionTypeImportSummary().getFailedImporting() + " could not be correctly imported.";
		} else if (summary.getInspectionTypeGroupImportSummary().failed()) {
			body += "Event Type Group " + summary.getInspectionTypeGroupImportSummary().getFailedImporting() + " could not be correctly imported.";
		} else if (summary.getStateSetImportSummary().failed()) {
			body += "Button Group " + summary.getStateSetImportSummary().getFailedImporting() + " could not be correctly imported.";
		} else if (summary.getProductTypeRelationshipsImportSummary().failed()) {
			body += "Could not set up the relationships to inspection types for product type " + summary.getProductTypeRelationshipsImportSummary().getFailedImporting();
		}
		 
		return body + "</p><p>The rest of import has been undone.  You can attempt the import again, if the problem persists contact FieldID support by sending an email to support@n4systems.com</p>";
	}

	private String successBody() {
		String body = "<h4>Your Catalog Import has completed.</h4>";
		body += "<table>" +
					"<tr><td>" + importCatalogService.getSummary().getProductTypeImportSummary().getImportMapping().size() + "</td>" +
					"<td>Product Type(s) have been imported.</td></tr>" +
					"<tr><td>" + importCatalogService.getSummary().getInspectionTypeImportSummary().getImportMapping().size() + "</td>" +
					"<td>Inspection Type(s) have been imported.</td></tr>" +
				"</table>";
		return body;
	}

	private void init() {
		persistenceManager = ServiceLocator.getPersistenceManager();
		productTypeManager = ServiceLocator.getProductType();
		
		linkedCatalogAccess = null;
		
		SafetyNetworkAccessService safetyNetwork = new SafetyNetworkAccessService(persistenceManager, new UserSecurityFilter(user));
		try { 
			linkedCatalogAccess = safetyNetwork.getCatalogAccess(linkedTenant);
		} catch (NoAccessToTenantException e) {
			logger.warn("attempt to access non linked tenant catalog for " + linkedTenant.getName() + " by " + primaryOrg.getTenant().getName());
		}
	}

	public UserBean getUser() {
		return user;
	}

	public void setUser(UserBean user) {
		this.user = user;
	}


	public Tenant getLinkedTenant() {
		return linkedTenant;
	}

	public void setLinkedTenant(Tenant linkedTenant) {
		this.linkedTenant = linkedTenant;
	}

	public Set<Long> getImportProductTypeIds() {
		return importProductTypeIds;
	}

	public void setImportProductTypeIds(Set<Long> importProductTypeIds) {
		this.importProductTypeIds = importProductTypeIds;
	}

	public Set<Long> getImportInspectionTypeIds() {
		return importInspectionTypeIds;
	}

	public void setImportInspectionTypeIds(Set<Long> importInspectionTypeIds) {
		this.importInspectionTypeIds = importInspectionTypeIds;
	}

	public boolean isUsingPackages() {
		return usingPackages;
	}

	public void setUsingPackages(boolean usingPackages) {
		this.usingPackages = usingPackages;
	}

	public PrimaryOrg getPrimaryOrg() {
		return primaryOrg;
	}

	public void setPrimaryOrg(PrimaryOrg primaryOrg) {
		this.primaryOrg = primaryOrg;
	}
	
}
