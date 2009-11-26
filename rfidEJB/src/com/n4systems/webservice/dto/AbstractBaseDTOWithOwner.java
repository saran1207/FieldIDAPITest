package com.n4systems.webservice.dto;

import static com.n4systems.webservice.dto.MobileDTOHelper.*;

public abstract class AbstractBaseDTOWithOwner extends AbstractBaseServiceDTO implements DTOHasOwners {

	private long ownerId;
	private long customerId;
	private long divisionId;
	private long orgId;

	public long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(long ownerId) {
		this.ownerId = ownerId;
	}
	public boolean ownerIdExists() {
		return isValidServerId(ownerId);
	}
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	public boolean customerExists() {
		return isValidServerId( customerId );
	}
	public long getDivisionId() {
		return divisionId;
	}
	public void setDivisionId(long divisionId) {
		this.divisionId = divisionId;
	}
	public boolean divisionExists() {
		return isValidServerId( divisionId );
	}
	public long getOrgId() {
		return orgId;
	}
	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}	
}
