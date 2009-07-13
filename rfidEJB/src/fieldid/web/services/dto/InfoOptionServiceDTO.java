package fieldid.web.services.dto;

public class InfoOptionServiceDTO extends LegacyBaseServiceDTO{
	
	
	
	/**
	 * Has not yet been put into a downgrader.. so keep using until we do?!
	 * XXX Fix this up?
	 */
	@Deprecated
	private String uniqueId;
	
	private String name;
	private String r_infoField;
	private boolean staticData;
	private String weight;
	private String valueType;
	
	public String getValueType() {
		return valueType;
	}
	public void setValueType(String valueType) {
		this.valueType = valueType;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setR_infoField(String r_infoOption) {
		this.r_infoField = r_infoOption;
	}
	public String getR_infoField() {
		return r_infoField;
	}
	
	public Long getR_infoFieldLong() {
		return convertToLong( r_infoField );
	}

	/**
	 * Has not yet been put into a downgrader.. so keep using until we do?!
	 */
	@Deprecated
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
	/**
	 * Has not yet been put into a downgrader.. so keep using until we do?!
	 * XXX Fix this up?
	 */
	@Deprecated	
	public String getUniqueId() {
		return uniqueId;
	}
	
	public void setStaticData(boolean staticData) {
		this.staticData = staticData;
	}
	public boolean getStaticData() {
		return staticData;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getWeight() {
		return weight;
	}	
}
