package rfid.dto;

import java.util.Date;

public class CommentTempDTO extends BaseDTO {
	private static final long serialVersionUID = 1L;
	
	private String templateID;
	private String contents;
	private Long tenantId;
	
	public CommentTempDTO() {}

	public CommentTempDTO(Long uniqueID, Date dateCreated, Date dateModified, 
			String modifiedBy, String templateID, String contents, Long tenantId) {
	
		super(uniqueID, dateCreated, dateModified, modifiedBy);
	
		this.templateID = templateID;
		this.contents = contents;
		this.tenantId = tenantId;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getTemplateID() {
		return templateID;
	}

	public void setTemplateID(String templateID) {
		this.templateID = templateID;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

}
