package com.n4systems.fieldid.actions.users;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.session.User;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.utils.DummyOwnerHolder;
import com.n4systems.fieldid.actions.utils.OwnerPicker;
import com.n4systems.fieldid.permissions.ExtendedFeatureFilter;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.UserRequest;
import com.n4systems.model.api.Listable;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.tools.Pager;
import com.n4systems.util.ListHelper;
import com.n4systems.util.ListingPair;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.mail.MailMessage;

@ExtendedFeatureFilter(requiredFeature=ExtendedFeature.PartnerCenter)
public class UserRequestCrud extends AbstractCrud {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(UserRequestCrud.class);

	private UserRequest userRequest;
	private List<UserRequest> userRequests;

	private User userManager;
	private Collection<ListingPair> organizationalUnits;
	
	private OwnerPicker ownerPicker;
	

	public UserRequestCrud(User userManager, PersistenceManager persistenceManager) {
		super(persistenceManager);
		this.userManager = userManager;
	}

	@Override
	protected void initMemberFields() {}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		userRequest = persistenceManager.find(UserRequest.class, uniqueId, getTenantId());
		ownerPicker = new OwnerPicker(getLoaderFactory().createFilteredIdLoader(BaseOrg.class), new DummyOwnerHolder());
	}
	
	

	@SkipValidation
	public String doShow() {
		if (userRequest == null) {
			addFlashError(getText("error.unknownuserrequest"));
			return ERROR;
		}
		return SUCCESS;
	}

	public String doAcceptRequest() {
		if (userRequest == null) {
			addFlashError(getText("error.unknownuserrequest"));
			return ERROR;
		}
		// customer users have no permissions
		userRequest.getUserAccount().setPermissions(0);
		
		try {
			userManager.acceptRequest(userRequest);
		} catch (Exception e) {
			logger.error(getLogLinePrefix() + "could not accept the user request ", e);
			addActionError(getText("error.failedtosave"));
			return INPUT;
		}
		createNewAccountEmail();

		addFlashMessage(getText("message.accountaccepted"));
		return SUCCESS;
	}

	private void createNewAccountEmail() {
		MailMessage message = new MailMessage();
		message.setSubject("Your Field ID Account Information");
		String loginUrl =  getBaseURI().resolve("").toString() + "login/"+  userRequest.getUserAccount().getTenant().getName();
		
		String body = "<h2>Thank you for requesting a Field ID Account.</h2>" +
					"To access your account, go the following company login page:<br/><br/>" +
					"<a href=\"" + loginUrl +"\">" + loginUrl + "</a><br/><br/>" +
					"Login securely using the following information:<br/><br/>" +
					"User name: " + userRequest.getUserAccount().getUserID() + "<br/><br/>" +
					"If you forget your password you can reset it here from your company login page.<br/><br/>" + 
					"<h2>Resources to help you with Field ID</h2>" + 
					"The Field ID Help System:<br/>" +
					"<a href=\"https://www.fieldid.com/fieldid_help/index.html\">https://www.fieldid.com/fieldid_help/index.html</a><br/><br/>" +
					"For more information about Field ID:<br/>" +
					"<a href=\"http://www.n4systems.com\">http://www.n4systems.com</a><br/><br/>" +
					"Follow us on Twitter:<br/>" +
					"<a href=\"http://www.twitter.com/fieldid\">http://www.twitter.com/fieldid</a><br/>" +
					"<a href=\"http://www.twitter.com/n4systems\">http://www.twitter.com/n4systems</a><br/>";

		message.setBody(body);
		message.getToAddresses().add( userRequest.getUserAccount().getEmailAddress() );
		try {
			ServiceLocator.getMailManager().sendMessage(message);
			
			logger.info(getLogLinePrefix() + " user request email sent for to user " + userRequest.getUserAccount().getUserID() + " for tenant " + userRequest.getTenant().getName());
		} catch (Exception e) {
			logger.warn(getLogLinePrefix() + "acceptance message failed to send. ", e);
		}
	}

	@SkipValidation
	public String doDeny() {
		if (userRequest == null) {
			addFlashError(getText("error.unknownuserrequest"));
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
}
