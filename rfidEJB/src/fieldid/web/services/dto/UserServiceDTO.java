package fieldid.web.services.dto;


public class UserServiceDTO extends LegacyBaseServiceDTO {
	private String userID;
	private String firstName;
	private String lastName;
	private String hashPassword;
	private String emailAddress;
	private String initials;
	private boolean endUser;
	private boolean printReport;
	
	private Long r_Manufacture;
    private Long r_EndUser;
    
	private String manName;
	private String userLabel;
	private String endUserName;
	private String hashSecurityCardNumber;
	private String serialNumberFormat;
	
	private boolean usingJobSites;

	@Deprecated private boolean tag = true;
	@Deprecated private boolean inspect = true;
	@Deprecated private boolean user = true;
	
	public UserServiceDTO() {}

	public String getUserLabel() {
		return userLabel;
	}

	public void setUserLabel(String userLabel) {
		this.userLabel = userLabel;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Deprecated 
	public boolean getInspect() {
		return inspect;
	}

	@Deprecated 
	public void setInspect(boolean inspect) {
		this.inspect = inspect;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getHashPassword() {
		return hashPassword;
	}

	public void setHashPassword(String hashPassword) {
		this.hashPassword = hashPassword;
	}

	@Deprecated 
	public boolean getTag() {
		return tag;
	}

	@Deprecated 
	public void setTag(boolean tag) {
		this.tag = tag;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	/**
	 * use tenantId instead
	 * Deprecated since version 4
	 * 
	 */
	@Deprecated
	public Long getR_Manufacture() {
		return r_Manufacture;
	}

	/**
	 * use tenantId instead
	 * Deprecated since version 4
	 * 
	 */
	@Deprecated
	public void setR_Manufacture(Long manufacture) {
		r_Manufacture = manufacture;
	}
	
	public boolean getEndUser() {
		return endUser;
	}

	public void setEndUser(boolean endUser) {
		this.endUser = endUser;
	}

	@Deprecated 
	public boolean getUser() {
		return user;
	}

	@Deprecated 
	public void setUser(boolean user) {
		this.user = user;
	}

	public Long getR_EndUser() {
		return r_EndUser;
	}

	public void setR_EndUser(Long endUser) {
		r_EndUser = endUser;
	}

	public String getManName() {
		return manName;
	}

	public void setManName(String manName) {
		this.manName = manName;
	}

	public String getEndUserName() {
		return endUserName;
	}

	public void setEndUserName(String endUserName) {
		this.endUserName = endUserName;
	}

	/**
	 * @param printReport the printReport to set
	 */
	public void setPrintReport(boolean printReport) {
		this.printReport = printReport;
	}

	/**
	 * @return the printReport
	 */
	public boolean getPrintReport() {
		return printReport;
	}


	public void setSerialNumberFormat(String serialNumberFormat) {
		this.serialNumberFormat = serialNumberFormat;
	}

	public String getSerialNumberFormat() {
		return serialNumberFormat;
	}

	public String getInitials() {
		return initials;
	}

	public void setInitials(String initials) {
		this.initials = initials;
	}

	public boolean isUsingJobSites() {
		return usingJobSites;
	}

	public void setUsingJobSites(boolean usingJobSites) {
		this.usingJobSites = usingJobSites;
	}

	public String getHashSecurityCardNumber() {
		return hashSecurityCardNumber;
	}

	public void setHashSecurityCardNumber(String hashSecurityCardNumber) {
		this.hashSecurityCardNumber = hashSecurityCardNumber;
	}
}
