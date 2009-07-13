package fieldid.web.services.dto;


public class EventInfoOptionServiceDTO extends LegacyBaseServiceDTO{

	
	
	private String name;
	private String r_eventInfoField;
	private boolean staticData;
	private String weight;
	
	public EventInfoOptionServiceDTO(){
		
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getR_eventInfoField() {
		return r_eventInfoField;
	}
	public void setR_eventInfoField(String infoField) {
		r_eventInfoField = infoField;
	}
	public boolean isStaticData() {
		return staticData;
	}
	public void setStaticData(boolean staticData) {
		this.staticData = staticData;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	
}
