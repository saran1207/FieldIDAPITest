package com.n4systems.fieldid.actions.users;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;


import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.user.UserWelcomeNotificationProducer;
import com.n4systems.fieldid.actions.utils.OwnerPicker;
import com.n4systems.fieldid.permissions.ExtendedFeatureFilter;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.UserRequest;
import com.n4systems.model.api.Listable;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.security.Permissions;
import com.n4systems.tools.Pager;
import com.n4systems.util.ListHelper;
import com.n4systems.util.ListingPair;
import com.opensymphony.xwork2.validator.annotations.FieldExpressionValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;

@ExtendedFeatureFilter(requiredFeature=ExtendedFeature.PartnerCenter)
@UserPermissionFilter(userRequiresOneOf={Permissions.ManageSystemUsers})
public class UserRequestCrud extends AbstractCrud {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(UserRequestCrud.class);

	private UserRequest userRequest;
	private List<UserRequest> userRequests;

	private UserManager userManager;
	private Collection<ListingPair> organizationalUnits;
	
	private OwnerPicker ownerPicker;
	

	public UserRequestCrud(UserManager userManager, PersistenceManager persistenceManager) {
		super(persistenceManager);
		this.userManager = userManager;
	}

	@Override
	protected void initMemberFields() {}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		userRequest = persistenceManager.find(UserRequest.class, uniqueId, getTenantId());
		ownerPicker = new OwnerPicker(getLoaderFactory().createFilteredIdLoader(BaseOrg.class), userRequest.getUserAccount());
	}
	
	

	@SkipValidation
	public String doShow() {
		if (userRequest == null) {
			addFlashErrorText("error.unknownuserrequest");
			return ERROR;
		}
		return SUCCESS;
	}

	public String doAcceptRequest() {
		if (userRequest == null) {
			addFlashErrorText("error.unknownuserrequest");
			return ERROR;
		}
		// customer users have no permissions
		userRequest.getUserAccount().setPermissions(0);
		
		try {
			userManager.acceptRequest(userRequest);
		} catch (Exception e) {
			logger.error(getLogLinePrefix() + "could not accept the user request ", e);
			addActionErrorText("error.failedtosave");
			return INPUT;
		}
		createNewAccountEmail();

		addFlashMessage(getText("message.accountaccepted"));
		return SUCCESS;
	}

	private void createNewAccountEmail() {
		
		try {
			new UserWelcomeNotificationProducer(getDefaultNotifier(), createActionUrlBuilder()).sendWelcomeNotificationTo(userRequest.getUserAccount());
			logger.info(getLogLinePrefix() + " user request email sent for to user " + userRequest.getUserAccount().getUserID() + " for tenant " + userRequest.getTenant().getName());
		} catch (Exception e) {
			logger.warn(getLogLinePrefix() + "acceptance message failed to send. ", e);
		}
	}

	@SkipValidation
	public String doDeny() {
		if (userRequest == null) {
			addFlashErrorText("error.unknownuserrequest");
			return ERROR;
		}
		try {
			userManager.denyRequest(userRequest);

		} catch (Exception e) {
			logger.error("error rejecting user request.", e);
			addActionError(getText("error.failedtosave"));
			return ERROR;
		}
		addFlashMessage(getText("message.accountdenied"));
		return SUCCESS;
	}

	@SkipValidation
	public String doList() {
		return SUCCESS;
	}

	public List<UserRequest> getUserRequests() {
		if (userRequests == null) {
			Map<String, Object> where = new HashMap<String, Object>();
			where.put("tenant", getTenant());
			Map<String, Boolean> orderBy = new HashMap<String, Boolean>();
			orderBy.put("created", true);

			userRequests = persistenceManager.findAll(UserRequest.class, where, orderBy);
		}
		return userRequests;
	}

	public UserRequest getUserRequest() {
		return userRequest;
	}


	public Collection<ListingPair> getOrganizationalUnits() {
		if (organizationalUnits == null) {
			List<Listable<Long>> orgList = getLoaderFactory().createSecondaryOrgListableLoader().load();
			organizationalUnits = ListHelper.longListableToListingPair(orgList);
		}
		return organizationalUnits;
	}
	
	public boolean customersExist() {
		Pager<CustomerOrg> page = getLoaderFactory().createCustomerOrgPaginatedLoader().setPageSize(1).setFirstPage().load();
		return page.hasResults();
	}

	@RequiredFieldValidator(message="", key="error.owner_required")
	@FieldExpressionValidator(message="", key="error.owner_be_a_customer_or_division", expression="owner.external == true")
	public BaseOrg getOwner() {
		return ownerPicker.getOwner();
	}

	public Long getOwnerId() {
		return ownerPicker.getOwnerId();
	}

	public void setOwnerId(Long id) {
		ownerPicker.setOwnerId(id);
	}
}
