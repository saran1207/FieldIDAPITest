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
import com.n4systems.exporting.io.MapReader;
import com.n4systems.model.orgs.OrgByNameLoader;
import com.n4systems.model.user.UserSaver;
import com.n4systems.persistence.Transaction;
import com.n4systems.security.UserType;
import com.n4systems.services.limiters.ResourceLimit;
import com.n4systems.services.limiters.TenantLimitService;

public class UserImporter extends AbstractImporter<UserView> {
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(UserImporter.class);
	private final UserSaver saver;
	private final UserToModelConverter converter;
	private final OrgByNameLoader orgByNameLoader;	
	
	public UserImporter(MapReader mapReader, Validator<ExternalModelView> validator, UserSaver saver, UserToModelConverter converter, OrgByNameLoader orgByNameLoader) {
		super(UserView.class, mapReader, validator);
		this.saver = saver;
		this.converter = converter;
		this.orgByNameLoader = orgByNameLoader;
	}

	@Override
	protected List<ValidationResult> validateAllViews(List<UserView> views) {
		List<ValidationResult> results = super.validateAllViews(views);
		results.addAll(checkForDuplicateUsers(views));
		results.addAll(checkForUserLimits(views));
		return results;
	}

	private Collection<? extends ValidationResult> checkForUserLimits(List<UserView> views) {
		ArrayList<ValidationResult> results = new ArrayList<ValidationResult>();
		
		Set<UserView> liteUsers = new HashSet<UserView>();
		Set<UserView> fullUsers = new HashSet<UserView>();
		Set<UserView> readOnlyUsers = new HashSet<UserView>();		
		
		for (UserView userView:views) {
			switch (UserType.valueFromLabel(userView.getAccountType())) {
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
				throw new IllegalStateException("unsupported user type being added");
			}
		}
		Long tenantId = orgByNameLoader.getTenantId();
		ResourceLimit liteUsersLimit = TenantLimitService.getInstance().getLiteUsers(tenantId);		
		if ( liteUsersLimit.getAvailable() < liteUsers.size() && liteUsers.size() > 0 ) {
			results.add(ValidationResult.fail("You can not import more than " + liteUsersLimit.getAvailable() + " lite users. (Attempted to import " + liteUsers.size()+ ")").setRow(-1));
		}
		ResourceLimit readOnlyUsersLimit = TenantLimitService.getInstance().getReadonlyUsers(tenantId);
		if ( readOnlyUsersLimit.getAvailable() < readOnlyUsers.size() && readOnlyUsers.size() > 0 ) { 
			results.add(ValidationResult.fail("you can not import more than " + readOnlyUsersLimit.getAvailable() + " read only users. (Attempted to import " + readOnlyUsers.size()+ ")").setRow(-1));
		}
		ResourceLimit fullUsersLimit = TenantLimitService.getInstance().getEmployeeUsers(tenantId);
		if ( fullUsersLimit.getAvailable() < fullUsers.size() && fullUsers.size() > 0) { 
			results.add(ValidationResult.fail("you can not import more than " + fullUsersLimit.getAvailable() + " employee (full) users. (Attempted to import " + fullUsers.size()+ ")").setRow(-1));
		}
		return results;
	}

	private ArrayList<ValidationResult> checkForDuplicateUsers(List<UserView> views) {
		ArrayList<ValidationResult> results = new ArrayList<ValidationResult>();
		Map<String, UserView> map = new HashMap<String, UserView>();
		int row = FIRST_DATA_ROW;
		for (UserView userView:views) { 
			String userName = userView.getUserID();
			if (map.containsKey(userName)) { 
				results.add(ValidationResult.fail("Username %s is duplicated" , userName).setRow(row));
			}
			map.put(userName, userView);
			row++;
		}
		return results;
	}

	@Override
	protected void importView(Transaction transaction, UserView view) throws ConversionException {
		saver.saveOrUpdate(converter.toModel(view, transaction));
	}

}
