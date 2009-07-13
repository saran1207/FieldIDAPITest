package fieldid.web.services.dto;

import java.util.ArrayList;
import java.util.Date;

public class InspectionDocServiceDTO extends LegacyBaseServiceDTO {
	public static final Long CURRENT_DTO_VERSION = 4L;
	
    private String r_Inspection;
    private String r_FieldIDUser;
	private String seqNumber;
	private String locationPath;
	private String inspectorName;
	private String docType;
	private String comments;
	private String xmlValues;
	
    private String r_EventType;
    private String eventStatus;
    private String discriminator;
    private byte[] image;
    private Boolean deleted;
    private String inspectionResult;
    private String r_inspectionStatus;
    private String inspectionStatus;
    private ArrayList<EventInfoOptionServiceDTO> eventInfoOptions;
    private boolean printable;
    private String r_manufacturer;
    
    // these are never populated on the way down to the mobile but used on the way up 
    private String productStatusId;
    private String nextInspectionDate;


	public String getR_FieldIDUser() {
		return r_FieldIDUser;
	}
	
	public Long retrieveFieldIdUserId() {
		return convertToLong(r_FieldIDUser);
	}

	public void setR_FieldIDUser(String fieldIDUser) {
		r_FieldIDUser = fieldIDUser;
	}

	public String getInspectorName() {
		return inspectorName;
	}

	public void setInspectorName(String inspectorName) {
		this.inspectorName = inspectorName;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getLocationPath() {
		return locationPath;
	}

	public void setLocationPath(String locationPath) {
		this.locationPath = locationPath;
	}

	public String getR_Inspection() {
		return r_Inspection;
	}

	public void setR_Inspection(String inspection) {
		r_Inspection = inspection;
	}

	public String getSeqNumber() {
		return seqNumber;
	}

	public void setSeqNumber(String seqNumber) {
		this.seqNumber = seqNumber;
	}

	public String getEventStatus() {
		return eventStatus;
	}

	public void setEventStatus(String eventStatus) {
		this.eventStatus = eventStatus;
	}

	public String getR_EventType() {
		return r_EventType;
	}

	public void setR_EventType(String eventType) {
		r_EventType = eventType;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public void setXmlValues(String xmlValues) {
		this.xmlValues = xmlValues;
	}

	public String getXmlValues() {
		return xmlValues;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public byte[] getImage() {
		return image;
	}

	public void setDiscriminator(String discriminator) {
		this.discriminator = discriminator;
	}

	public String getDiscriminator() {
		return discriminator;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public String getInspectionResult() {
		return inspectionResult;
	}
	
	public Long retrieveProductStatusId() {
		return convertToLong(productStatusId);
	}

	public String getProductStatusId() {
		return productStatusId;
	}

	public void setProductStatusId(String productStatusId) {
		this.productStatusId = productStatusId;
	}
	
	public void setInspectionResult(String inspectionResult) {
		this.inspectionResult = inspectionResult;
	}

	/**
	 * use inspectionStatus instead
	 * Deprecated since version 6
	 * 
	 */
	@Deprecated
	public String getR_inspectionStatus() {
		return r_inspectionStatus;
	}

	/**
	 * use inspectionStatus instead
	 * Deprecated since version 6
	 * 
	 */
	@Deprecated
	public void setR_inspectionStatus(String r_inspectionStatus) {
		this.r_inspectionStatus = r_inspectionStatus;
	}

	public void setEventInfoOptions(ArrayList<EventInfoOptionServiceDTO> eventInfoOptions) {
		this.eventInfoOptions = eventInfoOptions;
	}

	public ArrayList<EventInfoOptionServiceDTO> getEventInfoOptions() {
		return eventInfoOptions;
	}

	public boolean isPrintable() {
		return printable;
	}

	public void setPrintable(boolean printable) {
		this.printable = printable;
	}
	
	public Long getR_EventTypeLong(){
		return convertToLong( r_EventType );
	}
	
	/**
	 * use tenantId instead
	 * Deprecated since version 4
	 * 
	 */
	@Deprecated
	public String getR_manufacturer() {
		return r_manufacturer;
	}

	/**
	 * use tenantId instead
	 * Deprecated since version 4
	 * 
	 */
	@Deprecated
	public void setR_manufacturer(String r_manufacturer) {
		this.r_manufacturer = r_manufacturer;
	}

	public String getInspectionStatus() {
		return inspectionStatus;
	}

	public void setInspectionStatus(String inspectionStatus) {
		this.inspectionStatus = inspectionStatus;
	}

	public String getNextInspectionDate() {
		return nextInspectionDate;
	}
	
	public Date retrieveNextInspectionDate() {
		return stringToDate(nextInspectionDate);
	}

	public void setNextInspectionDate(String nextInspectionDate) {
		this.nextInspectionDate = nextInspectionDate;
	}
}
	
