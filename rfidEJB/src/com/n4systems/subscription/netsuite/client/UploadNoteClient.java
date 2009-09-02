package com.n4systems.subscription.netsuite.client;

import com.n4systems.subscription.netsuite.model.BaseResponse;

public class UploadNoteClient extends AbstractNetsuiteClient<BaseResponse> {

	private Long tenantId;
	private String title;
	private String note;
	
	public UploadNoteClient() {
		super(BaseResponse.class, "uploadnote");
	}
	
	@Override
	protected void addRequestParameters() {
		addRequestParameter("tenantid", tenantId.toString());
		addRequestParameter("title", title);
		addRequestParameter("note", note);
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setNote(String note) {
		this.note = note;
	}
}
