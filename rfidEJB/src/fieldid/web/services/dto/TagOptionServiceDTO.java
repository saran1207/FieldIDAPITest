package fieldid.web.services.dto;

import java.io.Serializable;

public class TagOptionServiceDTO extends LegacyBaseServiceDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String uniqueID;
	private String key;
	private String value;
	private String description;
	private String optionType;
	
	public String getUniqueID() {
		return uniqueID;
	}
	public void setUniqueID(String uniqueID) {
		this.uniqueID = uniqueID;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getOptionType() {
		return optionType;
	}
	public void setOptionType(String optionType) {
		this.optionType = optionType;
	}

}
