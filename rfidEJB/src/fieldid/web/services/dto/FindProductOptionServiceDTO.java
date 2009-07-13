package fieldid.web.services.dto;

public class FindProductOptionServiceDTO extends LegacyBaseServiceDTO {

	private String key;
	private String value;
	private int mobileWeight;
	
	/**
	 * use tenantId instead
	 * 
	 */
	@Deprecated
	public String getR_manufacturer() {
		return getTenantId();
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int getMobileWeight() {
		return mobileWeight;
	}
	public void setMobileWeight(int mobileWeight) {
		this.mobileWeight = mobileWeight;
	}
}
