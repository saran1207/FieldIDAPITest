package fieldid.web.services.dto;

public class CommentTemplateServiceDTO extends LegacyBaseServiceDTO {

	private String templateID;
	private String contents;
	
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
	/**
	 * use tenantId instead
	 * 
	 */
	@Deprecated
	public Long getR_manufacture() {
		return getTenantIdLong();
	}
	

	
	
}
