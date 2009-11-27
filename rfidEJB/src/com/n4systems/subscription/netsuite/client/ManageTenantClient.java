package com.n4systems.subscription.netsuite.client;

import com.n4systems.subscription.netsuite.model.ManageTenantResponse;


public class ManageTenantClient extends AbstractNetsuiteClient<ManageTenantResponse> {

	private Long netsuiteRecordId;
	private String poNumber;
	
	public ManageTenantClient() {
		super(ManageTenantResponse.class, "managetenant");
	}
	
	@Override
	protected void addRequestParameters() {
		applyOptionalParameter("nsrecordid", netsuiteRecordId);
		applyOptionalParameter("ponumber", poNumber);
		
	}

	private void applyOptionalParameter(String paramaterName, Object parameterValue) {
		if (parameterValue != null) {
			addRequestParameter(paramaterName, parameterValue.toString());			
		}
	}
	
	
	public ManageTenantClient setNetsuiteRecordId(Long netsuiteRecordId) {
		this.netsuiteRecordId = netsuiteRecordId;
		return this;
	}
	
	public ManageTenantClient setPoNumber(String poNumber) {
		this.poNumber = poNumber;
		return this;
	}
}
