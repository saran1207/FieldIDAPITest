package com.n4systems.fieldid.actions.customers;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exporting.Importer;
import com.n4systems.exporting.io.MapReader;
import com.n4systems.fieldid.actions.importexport.AbstractImportAction;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.notifiers.notifications.CustomerImportFailureNotification;
import com.n4systems.notifiers.notifications.CustomerImportSuccessNotification;
import com.n4systems.notifiers.notifications.ImportFailureNotification;
import com.n4systems.notifiers.notifications.ImportSuccessNotification;
import com.n4systems.security.Permissions;

@UserPermissionFilter(userRequiresOneOf={Permissions.ManageEndUsers})
@SuppressWarnings("serial")
public class CustomerImportAction extends AbstractImportAction {
	
	public CustomerImportAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}
	
	@Override
	protected Importer createImporter(MapReader reader) {
		return getImporterFactory().createCustomerImporter(reader);
	}
	
	@Override
	protected ImportSuccessNotification createSuccessNotification() {
		return new CustomerImportSuccessNotification(getCurrentUser(), getText("label.customer"));
	}
	
	@Override
	protected ImportFailureNotification createFailureNotification() {
		return new CustomerImportFailureNotification(getCurrentUser(), getText("label.customer"));
	}
	
}
