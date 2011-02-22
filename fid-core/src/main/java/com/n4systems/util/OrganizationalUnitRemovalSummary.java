package com.n4systems.util;

import com.n4systems.model.orgs.InternalOrg;

public class OrganizationalUnitRemovalSummary {
	
	private InternalOrg organization;
	private Integer customersToArchive = 0;
	private Integer divisionsToArchive = 0;
	private Integer usersToArchive = 0;
	
	public OrganizationalUnitRemovalSummary(InternalOrg organization) {
		this.organization = organization; 
	}
	
	public InternalOrg getOrganization() {
		return organization;
	}
	
	public Integer getCustomersToArchive() {
		return customersToArchive;
	}
	
	public void setCustomersToArchive(Integer customersToArchive) {
		this.customersToArchive = customersToArchive;
	}
	
	public Integer getDivisionsToArchive() {
		return divisionsToArchive;
	}
	
	public void setDivisionsToArchive(Integer divisionsToArchive) {
		this.divisionsToArchive = divisionsToArchive;
	}
	
	public void addDivisions(Integer divisions) {
		divisionsToArchive += divisions;
	}
	
	public void addDivision() {
		addDivisions(1);
	}
	
	public Integer getUsersToArchive() {
		return usersToArchive;
	}
	
	public void setUsersToArchive(Integer usersToArchive) {
		this.usersToArchive = usersToArchive;
	}
	
	public void addUsers(Integer users) {
		usersToArchive += users;
	}

}
