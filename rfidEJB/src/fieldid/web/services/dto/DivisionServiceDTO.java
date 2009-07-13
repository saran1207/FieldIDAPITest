package fieldid.web.services.dto;

public class DivisionServiceDTO extends LegacyBaseServiceDTO {

	private String name;
	private String r_EndUser;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getR_EndUser() {
		return r_EndUser;
	}

	public void setR_EndUser(String endUser) {
		r_EndUser = endUser;
	}

	/** @deprecated Not used anymore.  Returns String of length 0. */
	public String getLocation() { return new String(); }

	/** @deprecated Not used anymore.  Does nothing. */
	public void setLocation(String location) {}
}
