package fieldid.web.services;

import fieldid.web.services.dto.UserServiceDTO;

public class BaseService {
	
	private UserServiceDTO user;

	public BaseService(UserServiceDTO user) {
		this.user = user;
	}
	
	public void setUser(UserServiceDTO user) {
		this.user = user;
	}

	public UserServiceDTO getUser() {
		return user;
	}
	
	

}
