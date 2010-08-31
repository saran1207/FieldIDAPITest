package com.n4systems.model.messages;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "CREATE_SAFETY_NETWORK_CONNECTION")
public class CreateSafetyNetworkConnectionMessageCommand extends MessageCommand {

	
	public Long getVendorOrgId() {
		return new Long(getParamaters().get("vendorOrgId"));
	}
	
	public void setVendorOrgId(Long vendorOrgId) {
		getParamaters().put("vendorOrgId", vendorOrgId.toString());
	}
	
	public Long getCustomerOrgId() {
		return new Long(getParamaters().get("customerOrgId"));
	}
	
	public void setCustomerOrgId(Long customerOrgId) {
		getParamaters().put("customerOrgId", customerOrgId.toString());
	}

	
	
	
}
