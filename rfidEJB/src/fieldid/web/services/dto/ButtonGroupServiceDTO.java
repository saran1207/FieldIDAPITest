package fieldid.web.services.dto;

public class ButtonGroupServiceDTO extends LegacyBaseServiceDTO {
	
	private String name;
	private Long r_fieldIdScreen;
	
	private Long r_manufacturer;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getR_fieldIdScreen() {
		return r_fieldIdScreen;
	}
	public void setR_fieldIdScreen(Long idScreen) {
		r_fieldIdScreen = idScreen;
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

}
