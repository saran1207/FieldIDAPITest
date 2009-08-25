package com.n4systems.subscription.netsuite.client;

import com.n4systems.subscription.netsuite.model.NetsuiteTenant;

public class ManageTenantClient extends AbstractNetsuiteClient {

	private NetsuiteTenant tenant;
	private Long netsuiteRecordId;
	
	public ManageTenantClient() {
		super(Object.class, "managetenant");
	}
	
	@Override
	protected void addRequestParameters() {
		if (netsuiteRecordId != null) {
			addRequestParameter("nsrecordid", netsuiteRecordId.toString());			
		}
		
		// TODO Fill in the rest of the parameters
	}
	
	public ManageTenantClient setTenant(NetsuiteTenant tenant) {
		this.tenant = tenant;
		return this;
	}
	
	public ManageTenantClient setNetsuiteRecordId(Long netsuiteRecordId) {
		this.netsuiteRecordId = netsuiteRecordId;
		return this;
	}
}
