package fieldid.web.services.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.n4systems.webservice.dto.InspectionScheduleServiceDTO;

public class ProductSerialServiceDTO extends LegacyBaseServiceDTO {

	

	private String rfidNumber;
	private String r_ordermaster;
	private String r_endUser;
	private String r_Division;
	private String serialNumber;
	private String r_productInfo;
	private Collection<InfoOptionServiceDTO> infoOptions;
	private String reelId;
	private String comment;
	private String mobileGUID;
	private String r_User; // user who created this product
	private String endUserReferenceNumber;		// XXX - this should get renamed to customerRefNumber
	private String r_productStatus;
	private String location;
	private long jobSiteId;

	public void setRfidNumber(String rfidNumber) {
		this.rfidNumber = rfidNumber;
	}

	public String getRfidNumber() {
		return rfidNumber;
	}

	/**
	 * Deprecated since version 2
	 */
	@Deprecated
	public void setR_manufacturer(String r_manufacturer) {
		setTenantId( r_manufacturer );
	}

	/**
	 * Deprecated since version 2
	 */
	@Deprecated
	public String getR_manufacturer() {
		return getTenantId();
	}

	public void setR_ordermaster(String r_ordermaster) {
		this.r_ordermaster = r_ordermaster;
	}

	public String getR_ordermaster() {
		return r_ordermaster;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setR_endUser(String r_endUser) {
		this.r_endUser = r_endUser;
	}

	public String getR_endUser() {
		return r_endUser;
	}

	public void setR_productInfo(String r_productInfo) {
		this.r_productInfo = r_productInfo;
	}

	public String getR_productInfo() {
		return r_productInfo;
	}

	public void setInfoOptions(Collection<InfoOptionServiceDTO> infoOptions) {
		this.infoOptions = infoOptions;
	}

	public Collection<InfoOptionServiceDTO> getInfoOptions() {
		if (this.infoOptions == null)
			infoOptions = new ArrayList<InfoOptionServiceDTO>();
		return infoOptions;
	}

	
	public void setReelId(String reelId) {
		this.reelId = reelId;
	}

	public String getReelId() {
		return reelId;
	}

	public void setR_Division(String r_Division) {
		this.r_Division = r_Division;
	}

	public String getR_Division() {
		return r_Division;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getComment() {
		return comment;
	}

	public String getMobileGUID() {
		return mobileGUID;
	}

	public void setMobileGUID(String mobileGUID) {
		this.mobileGUID = mobileGUID;
	}

	public String getR_User() {
		return r_User;
	}

	public void setR_User(String user) {
		r_User = user;
	}

	public void setEndUserReferenceNumber(String endUserReferenceNumber) {
		this.endUserReferenceNumber = endUserReferenceNumber;
	}

	public String getEndUserReferenceNumber() {
		return endUserReferenceNumber;
	}

	public String getR_productStatus() {
		return r_productStatus;
	}

	public void setR_productStatus(String status) {
		r_productStatus = status;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public List<InspectionScheduleServiceDTO> getInspectionSchedules() {
		return null;
	}

	public void setInspectionSchedules(
			List<InspectionScheduleServiceDTO> inspectionSchedules) {
		return;
	}

	public long getJobSiteId() {
		return jobSiteId;
	}

	public void setJobSiteId(long jobSiteId) {
		this.jobSiteId = jobSiteId;
	}
	
}
