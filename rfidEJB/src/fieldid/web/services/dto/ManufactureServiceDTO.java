package fieldid.web.services.dto;


/**
 * 
 * @author aaitken
 * deprectated since verision 4 of the service.
 */
@Deprecated
public class ManufactureServiceDTO extends LegacyBaseServiceDTO {
	
	private String manufactureID;
	private String manufactureName;
	private String address;	
	private String serialNumberFormat;
	
	/**
	 * @author Shaun
	 * Default empty constructor
	 */
	public ManufactureServiceDTO() {
		
	}
	
	public void setManufactureID(String manufactureID) {
		this.manufactureID = manufactureID;
	}
	public String getManufactureID() {
		return manufactureID;
	}
	public void setManufactureName(String manufactureName) {
		this.manufactureName = manufactureName;
	}
	public String getManufactureName() {
		return manufactureName;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAddress() {
		return address;
	}

	public void setSerialNumberFormat(String serialNumberFormat) {
		this.serialNumberFormat = serialNumberFormat;
	}

	public String getSerialNumberFormat() {
		return serialNumberFormat;
	}

}
