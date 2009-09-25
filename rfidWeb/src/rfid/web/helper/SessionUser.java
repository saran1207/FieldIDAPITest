package rfid.web.helper;

import java.util.Date;
import java.util.TimeZone;

import rfid.ejb.entity.UserBean;

import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.UserSecurityFilter;
import com.n4systems.security.Permissions;
import com.n4systems.util.BitField;
import com.n4systems.util.DateHelper;
import com.n4systems.util.DateTimeDefinition;

public class SessionUser implements DateTimeDefinition {
	private Tenant tenant;
	private BaseOrg owner;
	private String userID;
	private String firstName;
	private String lastName;
	private Byte langID;
	private Long id;
	private int permissions;
	private String prevAction;
	private String dateFormat;
	private String otherDateFormat;
	private String orderNumber;
	private String searchType;
	private TimeZone timeZone;
	private boolean fromQuickLogin;
	private SecurityFilter securityFilter;
	
	public SessionUser( UserBean user ) {
		this.tenant = user.getTenant();
		this.owner = user.getOwner();
		this.userID = user.getUserID();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.id = user.getId();
		this.fromQuickLogin = true;
		this.permissions = user.getPermissions();
		this.timeZone = user.getTimeZone();
		this.dateFormat = owner.getPrimaryOrg().getDateFormat();
		this.otherDateFormat = DateHelper.java2Unix(dateFormat);
		this.securityFilter = new UserSecurityFilter(user);
	}

	public Tenant getTenant() {
		tenant.getName();
		return tenant;
	}

	public String getUserID() {
		return this.userID;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public Byte getLangID() {
		return this.langID;
	}

	public String getPrevAction() {
		return prevAction;
	}

	public void setPrevAction(String prevAction) {
		prevAction = prevAction.replaceAll(Constants.SLASH, Constants.BLANK);
		this.prevAction = prevAction;
	}

	/* (non-Javadoc)
	 * @see rfid.web.helper.DateTimeDefinition#getDateFormat()
	 */
	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	/* (non-Javadoc)
	 * @see rfid.web.helper.DateTimeDefinition#getDisplayDateFormat()
	 */
	public String getDisplayDateFormat() {
		return convertJavaDateFormatToHumanDisplay(getDateFormat());
	}
	
	/* (non-Javadoc)
	 * @see rfid.web.helper.DateTimeDefinition#getDateTimeFormat()
	 */
	public String getDateTimeFormat() {
		return dateFormat +  " h:mm a";
	}
	
	/* (non-Javadoc)
	 * @see rfid.web.helper.DateTimeDefinition#getDisplayDateTimeFormat()
	 */
	public String getDisplayDateTimeFormat() {
		return convertJavaDateFormatToHumanDisplay(getDateTimeFormat());
	}
	
	private String convertJavaDateFormatToHumanDisplay(String format) {
		format = format.replace("a", "AM/PM");
		format = format.replace("h", "HH");
		
		return format.toUpperCase();
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Long getUniqueID() {
		return id;
	}

	public void setUniqueID(Long uniqueID) {
		this.id = uniqueID;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public boolean hasAccess(String permissionName) {
		int perm = Permissions.getPermissionForLegacyName(permissionName);
		return BitField.create(permissions).isSet(perm);
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getOtherDateTimeFormat() {
		return otherDateFormat + " %l:%M %P";
	}
	
	public String getOtherDateFormat() {
		return otherDateFormat;
	}

	public void setOtherDateFormat(String otherDateFormat) {
		this.otherDateFormat = otherDateFormat;
	}

	public boolean isFromQuickLogin() {
		return fromQuickLogin;
	}

	public void setFromQuickLogin(boolean fromQuickLogin) {
		this.fromQuickLogin = fromQuickLogin;
	}

	public boolean isEmployeeUser() {
		return !isCustomerUser();
	}
	
	public boolean isCustomerUser() {
		return owner.isExternal();
	}
	
	public boolean isAnEndUser() {
		return isCustomerUser();
	}
	
	public boolean isInDivision() {
		return owner.isDivision();
	}

	/* (non-Javadoc)
	 * @see rfid.web.helper.DateTimeDefinition#getTimeZone()
	 */
	public TimeZone getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}
	
	public String getTimeZoneName() {
		return DateHelper.getTimeZoneShortName(new Date(), this.timeZone);
	}
	
	public String getName() {
		return firstName + " " + lastName;
	}

	public BaseOrg getOrganization() {
		return getOwner();
	}

	public void setOrganization(BaseOrg organization) {
		setOwner(organization);
	}
	
	public BaseOrg getOwner() {
		return owner;
	}
	
	public void setOwner(BaseOrg owner) {
		this.owner = owner;
	}
	
	@Deprecated
	public String getManID() {
		return getTenant().getName();
	}
	
	public SecurityFilter getSecurityFilter() {
		return securityFilter;
	}
	
	public boolean hasAdministrationAccess() {
		return (!isCustomerUser() && (hasAccess("manageendusers") || hasAccess("managesystemusers") || hasAccess("managesystemconfig")));
	}
	
	public String getSerialNumberLabel(){
		if(owner.getPrimaryOrg().isUsingSerialNumber()) {
			return "label.serialnumber";
		}
		return "label.reelid";
	}
	
	public String getUserName() {
		return userID;
	}

	public int getPermissions() {
		return permissions;
	}
}
