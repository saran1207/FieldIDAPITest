package fieldid.web.services.dto;

public class BaseServiceDTO extends AbstractBaseServiceDTO {

	protected String id; // LONG
	protected String modified; // DATE 
	protected String created; // DATE
	protected String modifiedBy; // LONG .. connection to UserBean
	
	public String getId() {
		return id;
	}
	public Long getIdLong() {
		return convertToLong( id );
	}
	public void setId(String id) {
		this.id = id;
	}	
	public void setId(Long id) {
		this.id = id.toString();
	}
	public String getModified() {
		return modified;
	}
	public void setModified(String modified) {
		this.modified = modified;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	
	
	
}
