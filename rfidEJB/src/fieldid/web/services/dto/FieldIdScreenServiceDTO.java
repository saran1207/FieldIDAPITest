package fieldid.web.services.dto;

public class FieldIdScreenServiceDTO extends LegacyBaseServiceDTO {

	private String screentype;
	private Long r_eventstatus;
	
	private Long r_manufacturer;
	
	private String description;
	private int determinesResultButtonId;

	public Long getR_eventstatus() {
		return r_eventstatus;
	}
	public void setR_eventstatus(Long r_eventstatus) {
		this.r_eventstatus = r_eventstatus;
	}
	public String getScreentype() {
		return screentype;
	}
	public void setScreentype(String screentype) {
		this.screentype = screentype;
	}
	/**
	 * use tenantId instead
	 * 
	 */
	@Deprecated
	public void setR_manufacturer(Long r_manufacturer) {
		this.r_manufacturer = r_manufacturer;
	}
	/**
	 * use tenantId instead
	 * 
	 */
	@Deprecated
	public Long getR_manufacturer() {
		return r_manufacturer;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescription() {
		return description;
	}
	public void setDeterminesResultButtonId(int determinesResultButtonId) {
		this.determinesResultButtonId = determinesResultButtonId;
	}
	public int getDeterminesResultButtonId() {
		return determinesResultButtonId;
	}
	
}
