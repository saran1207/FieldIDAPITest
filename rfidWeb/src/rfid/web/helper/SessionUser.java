package rfid.web.helper;

import java.util.Date;
import java.util.TimeZone;

import rfid.ejb.entity.UserBean;

import com.n4systems.model.Organization;
import com.n4systems.model.TenantOrganization;
import com.n4systems.security.Permissions;
import com.n4systems.util.BitField;
import com.n4systems.util.DateHelper;
import com.n4systems.util.DateTimeDefinition;
import com.n4systems.util.SecurityFilter;

public class SessionUser implements DateTimeDefinition {
	private TenantOrganization tenant;
	private Organization organization;
	private Long r_EndUser;
	private Long r_Division;
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
	
	public SessionUser( UserBean user ) {
		this.tenant = user.getTenant();
		this.organization = user.getOrganization();
		this.r_EndUser = ( user.getR_EndUser() == null ) ? -1L : user.getR_EndUser(); 
		this.r_Division = user.getR_Division();
		this.userID = user.getUserID();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.id = user.getId();
		this.fromQuickLogin = true;
		this.permissions = user.getPermissions();
		this.timeZone = user.getTimeZone();
		this.dateFormat = user.getTenant().getDateFormat();
		this.otherDateFormat = DateHelper.java2Unix(dateFormat);
	}

	public TenantOrganization getTenant() {
		tenant.getName();
		return tenant;
	}

	public Long getR_EndUser() {
		return r_EndUser;
	}
	
	public Long getR_Division() {
		return r_Division;
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
		return (getR_EndUser() != null && getR_EndUser() > 0 ) ? true : false;
	}
	
	public boolean isAnEndUser() {
		return isCustomerUser();
	}
	
	public boolean isInDivision() {
		return (getR_Division() != null && getR_Division() > 0 ) ? true : false;
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

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}
	
	@Deprecated
	public String getManID() {
		return getTenant().getName();
	}
	
	public SecurityFilter getSecurityFilter() {
		return new SecurityFilter(getTenant().getId(), getR_EndUser(), getR_Division(), getId());
	}
	
	public boolean hasAdministrationAccess() {
		if( ( r_EndUser == null || r_EndUser == -1L ) && ( hasAccess("manageendusers") || hasAccess("managesystemusers") || hasAccess("managesystemconfig") ) ) {
			return true;
		}
		
		return false;
	}
	
	public String getSerialNumberLabel(){
		if( tenant.isUsingSerialNumber() ) {
			return "label.serialnumber";
		}
		return "label.reelid";
	}
	
	public String getUserName() {
		return userID;
	}
}
