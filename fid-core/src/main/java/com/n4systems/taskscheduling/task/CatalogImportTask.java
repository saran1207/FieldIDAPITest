package com.n4systems.taskscheduling.task;

import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;

import com.n4systems.ejb.legacy.LegacyAssetType;
import org.apache.log4j.Logger;


import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.NoAccessToTenantException;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.security.UserSecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.services.safetyNetwork.CatalogService;
import com.n4systems.services.safetyNetwork.ImportCatalogService;
import com.n4systems.services.safetyNetwork.SafetyNetworkAccessService;
import com.n4systems.services.safetyNetwork.catalog.summary.CatalogImportSummary;
import com.n4systems.services.safetyNetwork.catalog.summary.BaseImportSummary.FailureType;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.mail.MailMessage;

public class CatalogImportTask implements Runnable {
	private static final Logger logger = Logger.getLogger(CatalogImportTask.class);

	private User user;
	private PrimaryOrg primaryOrg;
	private Tenant linkedTenant;
	private Set<Long> importProductTypeIds;
	private Set<Long> importEventTypeIds;
	private boolean usingPackages;
	private boolean failed;
	
	private PersistenceManager persistenceManager;
	private LegacyAssetType assetTypeManager;
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
			importCatalogService = new ImportCatalogService(persistenceManager, primaryOrg, linkedCatalogAccess, assetTypeManager);
			importCatalogService.setImportAllRelations(true);
			importCatalogService.setImportAssetTypeIds(importProductTypeIds);
			importCatalogService.setImportEventTypeIds(importEventTypeIds);
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
		if (summary.getAssetTypeGroupImportSummary().failed()) {
			body += "Asset Type Group " + summary.getAssetTypeGroupImportSummary().getFailedImporting() + " could not be correctly imported.";
		} else if (summary.getAssetTypeImportSummary().failed()) {
			if (summary.getAssetTypeImportSummary().getFailureType() == FailureType.COULD_NOT_CREATE) {
				body += "Asset Type  " + summary.getAssetTypeImportSummary().getFailedImporting() + " could not be correctly imported.";
			} else {
				body += "Auto attributes for asset type  " + summary.getAssetTypeImportSummary().getFailedImporting() + " could not be correctly imported.";
			}
		} else if (summary.getEventTypeImportSummary().failed()) {
			body += "Event Type  " + summary.getEventTypeImportSummary().getFailedImporting() + " could not be correctly imported.";
		} else if (summary.getEventTypeGroupImportSummary().failed()) {
			body += "Event Type Group " + summary.getEventTypeGroupImportSummary().getFailedImporting() + " could not be correctly imported.";
		} else if (summary.getStateSetImportSummary().failed()) {
			body += "Button Group " + summary.getStateSetImportSummary().getFailedImporting() + " could not be correctly imported.";
		} else if (summary.getAssetTypeRelationshipsImportSummary().failed()) {
			body += "Could not set up the relationships to event types for asset type " + summary.getAssetTypeRelationshipsImportSummary().getFailedImporting();
		}
		 
		return body + "</p><p>The rest of import has been undone.  You can attempt the import again, if the problem persists contact FieldID support by sending an email to support@fieldid.com</p>";
	}

	private String successBody() {
		String body = "<h4>Your Catalog Import has completed.</h4>";
		body += "<table>" +
					"<tr><td>" + importCatalogService.getSummary().getAssetTypeImportSummary().getImportMapping().size() + "</td>" +
					"<td>Asset Type(s) have been imported.</td></tr>" +
					"<tr><td>" + importCatalogService.getSummary().getEventTypeImportSummary().numberImported() + "</td>" +
					"<td>Event Type(s) have been imported.</td></tr>" +
				"</table>";
		return body;
	}

	private void init() {
		persistenceManager = ServiceLocator.getPersistenceManager();
		assetTypeManager = ServiceLocator.getProductType();
		
		linkedCatalogAccess = null;
		
		SafetyNetworkAccessService safetyNetwork = new SafetyNetworkAccessService(persistenceManager, new UserSecurityFilter(user));
		try { 
			linkedCatalogAccess = safetyNetwork.getCatalogAccess(linkedTenant);
		} catch (NoAccessToTenantException e) {
			logger.warn("attempt to access non linked tenant catalog for " + linkedTenant.getName() + " by " + primaryOrg.getTenant().getName());
		}
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
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

	public Set<Long> getImportEventTypeIds() {
		return importEventTypeIds;
	}

	public void setImportEventTypeIds(Set<Long> importEventTypeIds) {
		this.importEventTypeIds = importEventTypeIds;
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
