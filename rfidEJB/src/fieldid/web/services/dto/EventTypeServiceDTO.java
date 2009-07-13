package fieldid.web.services.dto;

import java.util.ArrayList;

public class EventTypeServiceDTO extends LegacyBaseServiceDTO {
	
	
    private Long r_Manufacture;
	private String statusID;
	private String description;
	private String discriminator;
	private boolean required;
	private String inspectorName;
	private String eventTypeName;
	private Long r_fieldIdScreen;
	private ArrayList<EventInfoFieldServiceDTO> eventInfoFields;
	private boolean printable;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Deprecated
	public Long getR_Manufacture() {
		return r_Manufacture;
	}
	@Deprecated
	public void setR_Manufacture(Long manufacture) {
		r_Manufacture = manufacture;
	}
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	public String getStatusID() {
		return statusID;
	}
	public void setStatusID(String statusID) {
		this.statusID = statusID;
	}
	public void setDiscriminator(String discriminator) {
		this.discriminator = discriminator;
	}
	public String getDiscriminator() {
		return discriminator;
	}
	public void setInspectorName(String inspectorName) {
		this.inspectorName = inspectorName;
	}
	public String getInspectorName() {
		return inspectorName;
	}
	public void setEventTypeName(String eventTypeName) {
		this.eventTypeName = eventTypeName;
	}
	public String getEventTypeName() {
		return eventTypeName;
	}
	public void setR_fieldIdScreen(Long r_fieldIdScreen) {
		this.r_fieldIdScreen = r_fieldIdScreen;
	}
	public Long getR_fieldIdScreen() {
		return r_fieldIdScreen;
	}	
	public boolean isPrintable() {
		return printable;
	}
	public void setPrintable(boolean printable) {
		this.printable = printable;
	}
	public void setEventInfoFieldBean(ArrayList<EventInfoFieldServiceDTO> eventInfoFieldBean) {
		this.eventInfoFields = eventInfoFieldBean;
	}
	public ArrayList<EventInfoFieldServiceDTO> getEventInfoFieldBean() {
		if(this.eventInfoFields ==null)
			this.eventInfoFields = new ArrayList<EventInfoFieldServiceDTO>();
		return eventInfoFields;
	}
}
