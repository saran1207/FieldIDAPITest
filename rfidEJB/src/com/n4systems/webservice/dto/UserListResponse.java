package com.n4systems.webservice.dto;

import java.util.ArrayList;
import java.util.List;

public class UserListResponse extends AbstractListResponse {
	
	private List<UserServiceDTO> users = new ArrayList<UserServiceDTO>();

	public List<UserServiceDTO> getUsers() {
		return users;
	}

	public void setUsers(List<UserServiceDTO> users) {
		this.users = users;
	}

}
