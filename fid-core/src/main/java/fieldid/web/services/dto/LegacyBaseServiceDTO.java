package fieldid.web.services.dto;

public class LegacyBaseServiceDTO extends AbstractBaseServiceDTO {

	protected String uniqueID;
	protected String dateModified;
	protected String dateCreated;
	protected String modifiedBy;
	protected Boolean moreData;
	protected String lastItemDate;

	public LegacyBaseServiceDTO() {
	}

	public LegacyBaseServiceDTO(String uniqueID, String dateCreated, 
			String dateModified, String modifiedBy, String tenantId) {
		this.uniqueID = uniqueID;
		this.dateModified = dateModified;
		this.dateCreated = dateCreated;
		this.modifiedBy = modifiedBy;
		this.moreData = false;
		this.tenantId = tenantId;
	}

	public void setUniqueID(String uniqueID) {
		this.uniqueID = uniqueID;
	}

	public String getUniqueID() {
		return this.uniqueID;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getDateCreated() {
		return this.dateCreated;
	}

	public void setDateModified(String dateModified) {
		this.dateModified = dateModified;
	}

	public String getDateModified() {
		return this.dateModified;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setMoreData(Boolean moreData) {
		this.moreData = moreData;
	}

	public Boolean getMoreData() {
		return moreData;
	}

	public void setLastItemDate(String lastItemDate) {
		this.lastItemDate = lastItemDate;
	}

	public String getLastItemDate() {
		return lastItemDate;
	}
	
}
