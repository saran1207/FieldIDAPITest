package com.n4systems.exporting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.conversion.users.UserToModelConverter;
import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.model.UserView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.api.validation.Validator;
import com.n4systems.api.validation.validators.YNValidator.YNField;
import com.n4systems.exporting.io.MapReader;
import com.n4systems.model.orgs.OrgByNameLoader;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserSaver;
import com.n4systems.persistence.Transaction;
import com.n4systems.security.UserType;
import com.n4systems.services.limiters.ResourceLimit;
import com.n4systems.services.limiters.TenantLimitService;
import com.n4systems.utils.email.WelcomeNotifier;

public class UserImporter extends AbstractImporter<UserView> {
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(UserImporter.class);
	private final UserSaver saver;
	private final UserToModelConverter converter;
	private final OrgByNameLoader orgByNameLoader;
	private WelcomeNotifier emailNotifier;	
	
	private String timeZoneId;
	
	public UserImporter(MapReader mapReader, Validator<ExternalModelView> validator, UserSaver saver, UserToModelConverter converter, OrgByNameLoader orgByNameLoader, WelcomeNotifier emailNotifier, String timeZoneId) {
		super(UserView.class, mapReader, validator);
		this.saver = saver;
		this.converter = converter;
		this.orgByNameLoader = orgByNameLoader;
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
		for (UserView userView:views) { 
			String userName = userView.getUserID();
//			if (userName!=null) { 
				if (map.containsKey(userName)) { 
					results.add(ValidationResult.fail("Username %s is duplicated" , userName).setRow(row));
				}
				map.put(userName, userView);
	//		}
			row++;
		}
		return results;
	}
	
	private Collection<? extends ValidationResult> checkForUserLimits(List<UserView> views) {
		ArrayList<ValidationResult> results = new ArrayList<ValidationResult>();
		
		Set<UserView> liteUsers = new HashSet<UserView>();
		Set<UserView> fullUsers = new HashSet<UserView>();
		Set<UserView> readOnlyUsers = new HashSet<UserView>();		
		
		for (UserView userView:views) {
			UserType userType = UserType.valueFromLabel(userView.getAccountType());
			if (userType!=null) { 
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
		Long tenantId = orgByNameLoader.getTenantId();
		ResourceLimit liteUsersLimit = getLiteUsersLimit(tenantId);		
		if ( liteUsersLimit.getAvailable()!=-1 && liteUsersLimit.getAvailable() < liteUsers.size() ) {
			results.add(ValidationResult.fail("You can not import more than " + liteUsersLimit.getAvailable() + " lite users. (Attempted to import " + liteUsers.size()+ ")").setRow(-1));
		}
		ResourceLimit readOnlyUsersLimit = getReadOnlyUsersLimit(tenantId);
		if ( readOnlyUsersLimit.getAvailable()!=-1 && readOnlyUsersLimit.getAvailable() < readOnlyUsers.size() ) { 
			results.add(ValidationResult.fail("You can not import more than " + readOnlyUsersLimit.getAvailable() + " read only users. (Attempted to import " + readOnlyUsers.size()+ ")").setRow(-1));
		}
		ResourceLimit fullUsersLimit = getEmployeeUsersLimit(tenantId);
		if ( fullUsersLimit.getAvailable()!=-1 && fullUsersLimit.getAvailable() < fullUsers.size() ) { 
			results.add(ValidationResult.fail("You can not import more than " + fullUsersLimit.getAvailable() + " employee (full) users. (Attempted to import " + fullUsers.size()+ ")").setRow(-1));
		}
		return results;
	}

	ResourceLimit getEmployeeUsersLimit(Long tenantId) {
		return TenantLimitService.getInstance().getEmployeeUsers(tenantId);
	}

	ResourceLimit getReadOnlyUsersLimit(Long tenantId) {
		return TenantLimitService.getInstance().getReadonlyUsers(tenantId);
	}

	ResourceLimit getLiteUsersLimit(Long tenantId) {
		return TenantLimitService.getInstance().getLiteUsers(tenantId);
	}

	@Override
	protected void importView(Transaction transaction, UserView view) throws ConversionException {
		User user = converter.toModel(view, transaction);
		if (user.getTimeZoneID()==null) {  // TODO DD: this responsibility should be in converter? 
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
