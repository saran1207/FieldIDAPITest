package com.n4systems.fieldid.ws.v1.resources.assetcount;

public class ApiAssetCount {
	private Long orgId;
	private Long count;

	public ApiAssetCount() {
		this(null, null);
	}
	
	public ApiAssetCount(Long orgId, Long count) {
		this.orgId = orgId;
		this.count = count;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

}
