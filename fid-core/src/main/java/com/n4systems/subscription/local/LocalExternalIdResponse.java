package com.n4systems.subscription.local;

import com.n4systems.subscription.ExternalIdResponse;

public class LocalExternalIdResponse implements ExternalIdResponse {
	private final Long externalId;
	
	public LocalExternalIdResponse(Long externalId) {
		super();
		this.externalId = externalId;
	}


	public Long getExternalId() {
		return externalId;
	}

}
