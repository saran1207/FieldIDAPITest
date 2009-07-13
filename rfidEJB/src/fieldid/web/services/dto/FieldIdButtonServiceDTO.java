package fieldid.web.services.dto;

public class FieldIdButtonServiceDTO extends LegacyBaseServiceDTO {
	
	private String name;
	private Long r_buttonGroup;
	private String value;
	private Integer buttonid;
	private Integer buttonOrder;
	private String imagePath;
	private String groupName;
	private String r_inspectionStatus;
	private String inspectionStatus;
	
	public Integer getButtonid() {
		return buttonid;
	}

	public void setButtonid(Integer buttonid) {
		this.buttonid = buttonid;
	}

	public Integer getButtonOrder() {
		return buttonOrder;
	}

	public void setButtonOrder(Integer buttonOrder) {
		this.buttonOrder = buttonOrder;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Long getR_buttonGroup() {
		return r_buttonGroup;
	}

	public void setR_buttonGroup(Long group) {
		r_buttonGroup = group;
	}

	/**
	 * use inspectionStatus instead
	 * Deprecated since version 6
	 * 
	 */
	public void setR_inspectionStatus(String r_inspectionStatus) {
		this.r_inspectionStatus = r_inspectionStatus;
	}

	/**
	 * use inspectionStatus instead
	 * Deprecated since version 6
	 * 
	 */
	public String getR_inspectionStatus() {
		return r_inspectionStatus;
	}

	public void setinspectionStatus(String inspectionStatus) {
		this.inspectionStatus = r_inspectionStatus;
	}

	public String getinspectionStatus() {
		return inspectionStatus;
	}
}
