package fieldid.web.services.dto;

import java.util.ArrayList;
import java.util.List;

public class EndUserServiceDTO extends LegacyBaseServiceDTO {

	private String endUserID;
	private String endUserName;
	private List<DivisionServiceDTO> divisions = new ArrayList<DivisionServiceDTO>();
	
	public EndUserServiceDTO() {}

	public String getEndUserID() {
		return endUserID;
	}

	public void setEndUserID(String endUserID) {
		this.endUserID = endUserID;
	}

	public String getEndUserName() {
		return endUserName;
	}

	public void setEndUserName(String endUserName) {
		this.endUserName = endUserName;
	}

	/** @deprecated Use {@link #getTenantIdLong()} */
	public Long getR_Manufacture() {
		return getTenantIdLong();
	}

	

	public List<DivisionServiceDTO> getDivisions() {
		return divisions;
	}

	public void setDivisions(List<DivisionServiceDTO> divisions) {
		this.divisions = divisions;
	}
	
	/** @deprecated Not used anymore.  Returns String of length 0. */
	public String getLocation() { return new String(); }

	/** @deprecated Not used anymore.  Does nothing. */
	public void setLocation(String location) {}
}
