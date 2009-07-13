package rfid.dto;

import java.io.Serializable;
import java.util.Date;

public class BaseDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long uniqueID;
	private Date dateModified;
	private Date dateCreated;
	private String modifiedBy;

	public BaseDTO() {
	}

	public BaseDTO(Long uniqueID, Date dateCreated, Date dateModified, String modifiedBy) {
		this.uniqueID = uniqueID;
		this.dateModified = dateModified;
		this.dateCreated = dateCreated;
		this.modifiedBy = modifiedBy;
	}

	public void setUniqueID(Long uniqueID) {
		this.uniqueID = uniqueID;
	}

	public Long getUniqueID() {
		return this.uniqueID;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateCreated() {
		return this.dateCreated;
	}

	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}

	public Date getDateModified() {
		return this.dateModified;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}
}
