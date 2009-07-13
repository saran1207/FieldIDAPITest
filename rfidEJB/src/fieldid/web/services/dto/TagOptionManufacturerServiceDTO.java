package fieldid.web.services.dto;

import java.io.Serializable;

public class TagOptionManufacturerServiceDTO  extends LegacyBaseServiceDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String uniqueID;
	private String r_manufacture;
	private TagOptionServiceDTO tagOption;
	private String weight;
	
	public String getUniqueID() {
		return uniqueID;
	}
	
	public void setUniqueID(String uniqueID) {
		this.uniqueID = uniqueID;
	}
	
	/**
	 * use tenantId instead
	 * Deprecated since version 4
	 * 
	 */
	@Deprecated
	public String getR_manufacture() {
		return r_manufacture;
	}
	
	/**
	 * use tenantId instead
	 * Deprecated since version 4
	 * 
	 */
	@Deprecated
	public void setR_manufacture(String r_manufacture) {
		this.r_manufacture = r_manufacture;
	}
	
	public TagOptionServiceDTO getTagOption() {
		return tagOption;
	}
	
	public void setTagOption(TagOptionServiceDTO tagOption) {
		this.tagOption = tagOption;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}	

}
