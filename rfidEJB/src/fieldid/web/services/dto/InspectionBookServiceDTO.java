package fieldid.web.services.dto;

public class InspectionBookServiceDTO extends LegacyBaseServiceDTO {

	private String r_manufacturer;
	private String title;
	
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
