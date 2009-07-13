package fieldid.web.services.dto;

public class UnitOfMeasureServiceDTO extends LegacyBaseServiceDTO {
	
	private String unitName;
	private String unitType;
	private String unitShortName;
	private Boolean selectable;
	private Long r_unitofmeasure;
	
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getUnitType() {
		return unitType;
	}
	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}
	public String getUnitShortName() {
		return unitShortName;
	}
	public void setUnitShortName(String unitShortName) {
		this.unitShortName = unitShortName;
	}
	public Boolean getSelectable() {
		return selectable;
	}
	public void setSelectable(Boolean selectable) {
		this.selectable = selectable;
	}
	public Long getR_unitofmeasure() {
		return r_unitofmeasure;
	}
	public void setR_unitofmeasure(Long r_unitofmeasure) {
		this.r_unitofmeasure = r_unitofmeasure;
	}

}
