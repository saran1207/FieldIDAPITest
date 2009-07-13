package fieldid.web.services.dto;

import java.util.ArrayList;

// TODO: Event Info field needs to extend base service dto.
public class EventInfoFieldServiceDTO{
	
	public static final Long CURRENT_DTO_VERSION = 4L;
	
	private long uniqueID;
	private String name;
	private long r_eventType;
	private long weight;
	private String tenantId;
	private ArrayList<EventInfoOptionServiceDTO> infoOptions;
	private String dtoVersion;
	
	public EventInfoFieldServiceDTO(){
		
	}
	public long getUniqueID() {
		return uniqueID;
	}
	public void setUniqueID(long uniqueID) {
		this.uniqueID = uniqueID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getR_eventType() {
		return r_eventType;
	}
	public void setR_eventType(long type) {
		r_eventType = type;
	}
	public long getWeight() {
		return weight;
	}
	public void setWeight(long weight) {
		this.weight = weight;
	}
	public void setInfoOptions(ArrayList<EventInfoOptionServiceDTO> infoOptions) {
		this.infoOptions = infoOptions;
	}
	public ArrayList<EventInfoOptionServiceDTO> getInfoOptions() {
		if(this.infoOptions == null)
			this.infoOptions = new ArrayList<EventInfoOptionServiceDTO>();
		return infoOptions;
	}
	public String getTenantId() {
		return tenantId;
	}
	
	
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	
	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId.toString();
	}
	public String getDtoVersion() {
		return dtoVersion;
	}
	public void setDtoVersion(String dtoVersion) {
		this.dtoVersion = dtoVersion;
	}
	

}
