package fieldid.web.services.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.n4systems.webservice.dto.ProductTypeScheduleServiceDTO;

public class ProductInfoServiceDTO extends LegacyBaseServiceDTO {

	public static final Long CURRENT_DTO_VERSION = 4L;

	private String cautions;
	private String instructions;
	private String itemNumber;
	private String warnings;
	private Long r_fieldIdScreen;

	private byte[] productImage;

	private Collection<InfoFieldServiceDTO> infoFields;
	private List<ProductTypeScheduleServiceDTO> schedules = new ArrayList<ProductTypeScheduleServiceDTO>();

	@Deprecated
	private Long r_manufacturer;
	@Deprecated
	private ArrayList<EventTypeServiceDTO> eventTypes;

	public ProductInfoServiceDTO() {
		this.eventTypes = new ArrayList<EventTypeServiceDTO>();
		this.infoFields = new ArrayList<InfoFieldServiceDTO>();
	}

	public String getCautions() {
		return cautions;
	}

	public void setCautions(String cautions) {
		this.cautions = cautions;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public String getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}

	public Long getR_fieldIdScreen() {
		return r_fieldIdScreen;
	}

	public void setR_fieldIdScreen(Long idScreen) {
		r_fieldIdScreen = idScreen;
	}

	/**
	 * use tenantId instead Deprecated since version 4
	 * 
	 */
	@Deprecated
	public Long getR_manufacturer() {
		return r_manufacturer;
	}

	/**
	 * use tenantId instead Deprecated since version 4
	 * 
	 */
	@Deprecated
	public void setR_manufacturer(Long r_manufacturer) {
		this.r_manufacturer = r_manufacturer;
	}

	public String getWarnings() {
		return warnings;
	}

	public void setWarnings(String warnings) {
		this.warnings = warnings;
	}

	public void setProductImage(byte[] productImage) {
		this.productImage = productImage;
	}

	public byte[] getProductImage() {
		return productImage;
	}

	/**
	 * Deprecated since version 5
	 * Use tenantId instead 
	 */
	@Deprecated
	public void setEventTypes(ArrayList<EventTypeServiceDTO> eventTypes) {
		this.eventTypes = eventTypes;
	}

	/**
	 * Deprecated since version 5
	 * Use tenantId instead 
	 */
	@Deprecated
	public ArrayList<EventTypeServiceDTO> getEventTypes() {
		return eventTypes;
	}

	public void setInfoFields(Collection<InfoFieldServiceDTO> infoFields) {
		this.infoFields = infoFields;
	}

	public Collection<InfoFieldServiceDTO> getInfoFields() {
		return infoFields;
	}

	public List<ProductTypeScheduleServiceDTO> getSchedules() {
		return schedules;
	}

	public void setSchedules(List<ProductTypeScheduleServiceDTO> schedules) {
		this.schedules = schedules;
	}

}
