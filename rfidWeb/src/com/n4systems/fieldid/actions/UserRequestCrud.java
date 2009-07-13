package com.n4systems.fieldid.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.session.User;

import com.n4systems.ejb.CustomerManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.permissions.ExtendedFeatureFilter;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.Organization;
import com.n4systems.model.UserRequest;
import com.n4systems.security.Permissions;
import com.n4systems.util.BitField;
import com.n4systems.util.ListHelper;
import com.n4systems.util.ListingPair;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.mail.MailMessage;
import com.n4systems.util.persistence.QueryBuilder;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@ExtendedFeatureFilter(requiredFeature=ExtendedFeature.PartnerCenter)
public class UserRequestCrud extends AbstractCrud {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(UserRequestCrud.class);

	private UserRequest userRequest;

	private List<UserRequest> userRequests;

	private Map<String, Boolean> userPermissions = new HashMap<String, Boolean>();

	private User userManager;
	private CustomerManager customerManager;

	private Collection<ListingPair> customers;
	private Collection<ListingPair> divisions;
	private Collection<ListingPair> organizationalUnits;
	private List<ListingPair> permissions;

	public UserRequestCrud(User userManager, PersistenceManager persistenceManager, CustomerManager customerManager) {
		super(persistenceManager);
		this.userManager = userManager;
		this.customerManager = customerManager;
	}

	@Override
	protected void initMemberFields() {

	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		userRequest = persistenceManager.find(UserRequest.class, uniqueId, getTenantId());
	}

	@SkipValidation
	public String doShow() {
		if (userRequest == null) {
			addFlashError(getText("error.unknownuserrequest"));
			return ERROR;
		}

		setupPermissions();
		return SUCCESS;
	}

	private void setupPermissions() {
		userPermissions = new HashMap<String, Boolean>();
		for (ListingPair permission : getPermissions()) {
			userPermissions.put(permission.getId().toString(), false);
		}
	}

	public String doAcceptRequest() {
		if (userRequest == null) {
			addFlashError(getText("error.unknownuserrequest"));
			return ERROR;
		}

		
		if( userPermissions != null ) {  // needed to handle when there is an empty list submitted.
			BitField perms = new BitField();
			/*
			 * we could do this by simply converting the id's back to strings and setting them on the field,
			 * however that would unsafe, since we can't trust what's coming back.  Instead, get the list of 
			 * permissions they were allowed to see in the first place, and ensure our key is from that set
			 */
			int permValue;
			String permStr;
			for (ListingPair allowedPerm: getPermissions()) {
				permValue = allowedPerm.getId().intValue();
				permStr = allowedPerm.getId().toString();
				
				// if our permission map contains the permission id, and it's value is true, add the permission
				if (userPermissions.containsKey(permStr) && userPermissions.get(permStr)) {
					perms.set(permValue);
				}
			}
			
			userRequest.getUserAccount().setPermissions(perms.getMask());
		}
		
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

	@RequiredFieldValidator(type = ValidatorType.FIELD, message = "", key = "error.customerrequired")
	public Long getCustomer() {
		return userRequest.getUserAccount().getR_EndUser();
	}

	public void setCustomer(Long customer) {
		userRequest.getUserAccount().setR_EndUser(customer);
	}

	public Long getDivision() {
		return userRequest.getUserAccount().getR_Division();
	}

	public void setDivision(Long division) {
		userRequest.getUserAccount().setR_Division(division);
	}

	@RequiredFieldValidator(type = ValidatorType.FIELD, message = "", key = "error.organizationrequired")
	public Long getOrganizationalUnit() {
		return userRequest.getUserAccount().getOrganization().getId();
	}

	public void setOrganizationalUnit(Long orgId) {
		try {
			QueryBuilder<Organization> builder = new QueryBuilder<Organization>(Organization.class, getSecurityFilter().setDefaultTargets());
			Organization org = persistenceManager.find(builder.addSimpleWhere("id", orgId));
			userRequest.getUserAccount().setOrganization(org);
		} catch (InvalidQueryException e) {
			logger.error("Unable to load Organization", e);
		}
	}

	public Collection<ListingPair> getCustomers() {
		if (customers == null) {
			customers = customerManager.findCustomersLP(getTenantId(), getSecurityFilter());
		}
		return customers;
	}

	public Collection<ListingPair> getDivisions() {
		if (divisions == null) {
			divisions = new ArrayList<ListingPair>();
			if (getCustomer() != null) {
				divisions = customerManager.findDivisionsLP(getCustomer(), getSecurityFilter());
			}
		}
		return divisions;
	}

	public Collection<ListingPair> getOrganizationalUnits() {
		if (organizationalUnits == null) {
			try {
				QueryBuilder<Organization> builder = new QueryBuilder<Organization>(Organization.class, getSecurityFilter().setDefaultTargets());
				organizationalUnits = ListHelper.longListableToListingPair(persistenceManager.findAll(builder));
			} catch (InvalidQueryException e) {
				logger.error("Unable to get list of Organizations", e);
			}
		}
		return organizationalUnits;
	}

	public List<ListingPair> getPermissions() {
		if (permissions == null) {
			permissions = ListHelper.intListableToListingPair(Permissions.getCustomerUserPermissions());
		}
		return permissions;
	}

	@SuppressWarnings("unchecked")
	public Map getUserPermissions() {
		return userPermissions;
	}

	@SuppressWarnings("unchecked")
	public void setUserPermissions(Map userPermissions) {
		this.userPermissions = userPermissions;
	}
}
