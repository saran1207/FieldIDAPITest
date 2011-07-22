package com.n4systems.exporting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.conversion.users.UserToModelConverter;
import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.model.UserView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.api.validation.Validator;
import com.n4systems.api.validation.validators.YNValidator.YNField;
import com.n4systems.exporting.io.MapReader;
import com.n4systems.model.tenant.TenantSettings;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserSaver;
import com.n4systems.persistence.Transaction;
import com.n4systems.security.UserType;
import com.n4systems.utils.email.WelcomeNotifier;

public class UserImporter extends AbstractImporter<UserView> {
	private final TenantSettings tenantSettings;
	private final UserSaver saver;
	private final UserToModelConverter converter;
	private WelcomeNotifier emailNotifier;

	private String timeZoneId;

	public UserImporter(MapReader mapReader, Validator<ExternalModelView> validator, TenantSettings tenantSettings, UserSaver saver, UserToModelConverter converter, WelcomeNotifier emailNotifier, String timeZoneId) {
		super(UserView.class, mapReader, validator);
		this.tenantSettings = tenantSettings;
		this.saver = saver;
		this.converter = converter;
		this.emailNotifier = emailNotifier;
		this.timeZoneId = timeZoneId;
	}

	@Override
	protected List<ValidationResult> validateAllViews(List<UserView> views) {
		List<ValidationResult> results = super.validateAllViews(views);
		results.addAll(checkForDuplicateUsers(views));
		results.addAll(checkForUserLimits(views));
		return results;
	}

	private ArrayList<ValidationResult> checkForDuplicateUsers(List<UserView> views) {
		ArrayList<ValidationResult> results = new ArrayList<ValidationResult>();
		Map<String, UserView> map = new HashMap<String, UserView>();
		int row = FIRST_DATA_ROW;
		for (UserView userView : views) {
			String userName = userView.getUserID();
			// if (userName!=null) {
			if (map.containsKey(userName)) {
				results.add(ValidationResult.fail("Username %s is duplicated", userName).setRow(row));
			}
			map.put(userName, userView);
			// }
			row++;
		}
		return results;
	}

	private Collection<? extends ValidationResult> checkForUserLimits(List<UserView> views) {
		ArrayList<ValidationResult> results = new ArrayList<ValidationResult>();

		Set<UserView> liteUsers = new HashSet<UserView>();
		Set<UserView> fullUsers = new HashSet<UserView>();
		Set<UserView> readOnlyUsers = new HashSet<UserView>();

		for (UserView userView : views) {
			UserType userType = UserType.valueFromLabel(userView.getAccountType());
			if (userType != null) {
				switch (userType) {
				case FULL:
				case SYSTEM:
				case ADMIN:
					fullUsers.add(userView);
					break;
				case LITE:
					liteUsers.add(userView);
					break;
				case READONLY:
					readOnlyUsers.add(userView);
					break;
				default:
					throw new IllegalStateException("illegal user account type '" + userView.getAccountType() + "' ");
				}
			}
		}
		if (tenantSettings.getMaxLiteUsers() != -1 && tenantSettings.getMaxLiteUsers() < liteUsers.size()) {
			results.add(ValidationResult.fail("You can not import more than " + tenantSettings.getMaxLiteUsers() + " lite users. (Attempted to import " + liteUsers.size() + ")").setRow(-1));
		}
		if (tenantSettings.getMaxReadOnlyUsers() != -1 && tenantSettings.getMaxReadOnlyUsers() < readOnlyUsers.size()) {
			results.add(ValidationResult.fail("You can not import more than " + tenantSettings.getMaxReadOnlyUsers() + " read only users. (Attempted to import " + readOnlyUsers.size() + ")").setRow(-1));
		}
		if (tenantSettings.getMaxEmployeeUsers() != -1 && tenantSettings.getMaxEmployeeUsers() < fullUsers.size()) {
			results.add(ValidationResult.fail("You can not import more than " + tenantSettings.getMaxEmployeeUsers() + " employee (full) users. (Attempted to import " + fullUsers.size() + ")").setRow(-1));
		}
		return results;
	}

	@Override
	protected void importView(Transaction transaction, UserView view) throws ConversionException {
		User user = converter.toModel(view, transaction);
		if (user.getTimeZoneID() == null) { // TODO DD: this responsibility
											// should be in converter?
			user.setTimeZoneID(timeZoneId);
		}
		saver.saveOrUpdate(user);
		maybeSendWelcomeEmail(user, view);
	}

	private void maybeSendWelcomeEmail(User user, UserView view) {
		if (YNField.isYes(view.getSendWelcomeEmail())) {
			emailNotifier.sendWelcomeNotificationTo(user);
		}
	}

}
