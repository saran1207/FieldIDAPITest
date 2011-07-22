package com.n4systems.fieldid.actions.users;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exporting.Importer;
import com.n4systems.exporting.io.MapReader;
import com.n4systems.fieldid.actions.importexport.AbstractImportAction;
import com.n4systems.fieldid.actions.user.UserWelcomeNotificationProducer;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.notifiers.notifications.ImportFailureNotification;
import com.n4systems.notifiers.notifications.ImportSuccessNotification;
import com.n4systems.notifiers.notifications.UserImportFailureNotification;
import com.n4systems.notifiers.notifications.UserImportSuccessNotification;
import com.n4systems.security.Permissions;
import com.n4systems.utils.email.WelcomeNotifier;

@UserPermissionFilter(userRequiresOneOf={Permissions.ManageEndUsers})
@SuppressWarnings("serial")
public class UserImportAction extends AbstractImportAction {

	public UserImportAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}
	
	@Override
	protected Importer createImporter(MapReader reader) {
		return getImporterFactory().createUserImporter(reader, createWelcomeNotifier(), getTenant().getSettings(), getCurrentUser().getTimeZoneID());
	}
	
	@Override
	protected ImportSuccessNotification createSuccessNotification() {
		return new UserImportSuccessNotification(getCurrentUser());
	}
	
	@Override
	protected ImportFailureNotification createFailureNotification() {
		return new UserImportFailureNotification(getCurrentUser());
	}	

	private WelcomeNotifier createWelcomeNotifier() {
		UserWelcomeNotificationProducer userWelcomeNotificationProducer = new UserWelcomeNotificationProducer(getDefaultNotifier(), createActionUrlBuilder());
		return userWelcomeNotificationProducer;
	}
		
}
