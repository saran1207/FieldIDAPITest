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
import com.n4systems.api.validation.validators.FieldValidator;
import com.n4systems.api.validation.validators.PasswordValidator;
import com.n4systems.api.validation.validators.YNValidator.YNField;
import com.n4systems.exporting.io.MapReader;
import com.n4systems.model.security.PasswordPolicy;
import com.n4systems.model.tenant.UserLimits;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserSaver;
import com.n4systems.persistence.Transaction;
import com.n4systems.security.UserType;
import com.n4systems.utils.email.WelcomeNotifier;

public class UserImporter extends AbstractImporter<UserView> {
	private final UserLimits userLimits;
	private final UserSaver saver;
	private final UserToModelConverter converter;
	private WelcomeNotifier emailNotifier;

	private String timeZoneId;

	public UserImporter(MapReader mapReader, Validator<ExternalModelView> validator, UserLimits userLimits, UserSaver saver, UserToModelConverter converter, WelcomeNotifier emailNotifier, String timeZoneId, PasswordPolicy passwordPolicy) {
		super(UserView.class, mapReader, validator);
		this.userLimits = userLimits;
		this.saver = saver;
		this.converter = converter;
		this.emailNotifier = emailNotifier;
		this.timeZoneId = timeZoneId;
		validator.getValidationContext().put(PasswordValidator.PASSWORD_POLICY_KEY, passwordPolicy);		
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
		// negative limits are considered "unlimited"
		if (userLimits.getMaxLiteUsers() >= 0 && userLimits.getMaxLiteUsers() < liteUsers.size()) {
			results.add(ValidationResult.fail(FieldValidator.MaxLiteUsersFail, userLimits.getMaxLiteUsers(), liteUsers.size()).setRow(-1));
		}
		if (userLimits.getMaxReadOnlyUsers() >= 0 && userLimits.getMaxReadOnlyUsers() < readOnlyUsers.size()) {
			results.add(ValidationResult.fail(FieldValidator.MaxReadOnlyUsersFail,userLimits.getMaxReadOnlyUsers(),readOnlyUsers.size()).setRow(-1));
		}
		if (userLimits.getMaxEmployeeUsers() >= 0 && userLimits.getMaxEmployeeUsers() < fullUsers.size()) {
			results.add(ValidationResult.fail(FieldValidator.MaxEmployeeUersFail, userLimits.getMaxEmployeeUsers(),fullUsers.size()).setRow(-1));
		}
		return results;
	}

	@Override
	protected void importView(Transaction transaction, UserView view) throws ConversionException {
		User user = converter.toModel(view, transaction);
		if (user.getTimeZoneID() == null) { 
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
