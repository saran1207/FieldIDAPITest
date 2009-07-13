package fieldid.web.services.dto;

import java.util.ArrayList;
import java.util.Date;

public class InspectionServiceDTO extends LegacyBaseServiceDTO {

	public static final Long CURRENT_DTO_VERSION = 3L;
	
    private String r_ProductSerial;
	private Byte rating;
	private Long seqNumber;
	private String location;

	private String mobileGuid;
	private String nextDate;
	private String inspectionDate;
    private Long r_InspectionType;
    private String inspectionType;
    private String r_EndUser;
    private String r_Division;
    private String slingCapacity;
    private String proofLoad;
    private boolean deleted;
    private ArrayList<InspectionDocServiceDTO> inspectionDocServiceDTOs;
    //the two fields below are mean to support an inspection created from a product serial that
    //was created offline
    private String productSerialSerialNumber;
    private String r_Manufacturer;
    private ProductSerialServiceDTO offlineProductSerialDTO;
    private String r_InspectionBook;
    private String inspectionBookString;
    private String r_user;
    
    // Unique id of user who created this inspection
    private String r_User;
    
    
    /**
     * This is used to reference the product serial coming across.  
     * This means if the serial number gets autoreplaced we can still find the right product serial
     */
    private String productSerialMobileGUID;

    public InspectionServiceDTO() {}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Deprecated
	public String getNextDate() {
		return nextDate;
	}

	@Deprecated
	public void setNextDate(String nextDate) {
		this.nextDate = nextDate;
	}
	
	@Deprecated
	public Date retrieveNextDate() {
		return stringToDate(nextDate);
	}

	public String getR_ProductSerial() {
		return r_ProductSerial;
	}

	public void setR_ProductSerial(String productSerial) {
		r_ProductSerial = productSerial;
	}

	public Byte getRating() {
		return rating;
	}

	public void setRating(Byte rating) {
		this.rating = rating;
	}

	public Long getSeqNumber() {
		return seqNumber;
	}

	public void setSeqNumber(Long seqNumber) {
		this.seqNumber = seqNumber;
	}

	public String getInspectionDate() {
		return inspectionDate;
	}

	public void setInspectionDate(String inspectionDate) {
		this.inspectionDate = inspectionDate;
	}
	
	public Date retrieveInspectionDate() {
		return stringToDate(inspectionDate);
	}

	public String getInspectionType() {
		return inspectionType;
	}

	public void setInspectionType(String inspectionType) {
		this.inspectionType = inspectionType;
	}

	public Long getR_InspectionType() {
		return r_InspectionType;
	}

	public void setR_InspectionType(Long inspectionType) {
		r_InspectionType = inspectionType;
	}

	public void setSlingCapacity(String slingCapacity) {
		this.slingCapacity = slingCapacity;
	}

	public String getSlingCapacity() {
		return slingCapacity;
	}

	public void setProofLoad(String proofLoad) {
		this.proofLoad = proofLoad;
	}

	public String getProofLoad() {
		return proofLoad;
	}

	public void setR_EndUser(String r_EndUser) {
		this.r_EndUser = r_EndUser;
	}

	public String getR_EndUser() {
		return r_EndUser;
	}
	
	public Long getR_EndUserLong() {
		return convertToLong(r_EndUser);
	}

	public void setInspectionDocServiceDTOs(ArrayList<InspectionDocServiceDTO> inspectionDocServiceDTOs) {
		this.inspectionDocServiceDTOs = inspectionDocServiceDTOs;
	}

	public ArrayList<InspectionDocServiceDTO> getInspectionDocServiceDTOs() {
		return inspectionDocServiceDTOs;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setProductSerialSerialNumber(String productSerialSerialNumber) {
		this.productSerialSerialNumber = productSerialSerialNumber;
	}

	public String getProductSerialSerialNumber() {
		return productSerialSerialNumber;
	}

	/**
	 * use tenantId instead
	 * Deprecated since version 4
	 * 
	 */
	@Deprecated
	public void setR_Manufacturer(String r_Manufacturer) {
		this.r_Manufacturer = r_Manufacturer;
	}

	/**
	 * use tenantId instead
	 * Deprecated since version 4
	 * 
	 */
	@Deprecated
	public String getR_Manufacturer() {
		return r_Manufacturer;
	}
	
	public Long getR_ManufacturerLong() {
		return convertToLong(getR_Manufacturer());
	}

	public void setOfflineProductSerialDTO(ProductSerialServiceDTO offlineProductSerialDTO) {
		this.offlineProductSerialDTO = offlineProductSerialDTO;
	}

	public ProductSerialServiceDTO getOfflineProductSerialDTO() {
		return offlineProductSerialDTO;
	}

	public void setR_InspectionBook(String r_InspectionBook) {
		this.r_InspectionBook = r_InspectionBook;
	}

	public String getR_InspectionBook() {
		return r_InspectionBook;
	}
	
	public Long getR_InspectionBookLong() {
		return convertToLong(r_InspectionBook);
	}

	public void setInspectionBookString(String inspectionBookString) {
		this.inspectionBookString = inspectionBookString;
	}

	public String getInspectionBookString() {
		return inspectionBookString;
	}

	public void setR_Division(String r_Division) {
		this.r_Division = r_Division;
	}

	public String getR_Division() {
		return r_Division;
	}
	
	public Long getR_DivisionLong() {
		return convertToLong(r_Division);
	}

	public String getProductSerialMobileGUID() {
		return productSerialMobileGUID;
	}

	public void setProductSerialMobileGUID(String productSerialMobileGUID) {
		this.productSerialMobileGUID = productSerialMobileGUID;
	}

	public String getR_User() {
		return r_User;
	}

	public void setR_User(String user) {
		r_User = user;
	}

	public void setR_user(String r_user) {
		this.r_user = r_user;
	}

	public String getR_user() {
		return r_user;
	}

	public String getMobileGuid() {
		return mobileGuid;
	}

	public void setMobileGuid(String mobileGuid) {
		this.mobileGuid = mobileGuid;
	}

	
}
