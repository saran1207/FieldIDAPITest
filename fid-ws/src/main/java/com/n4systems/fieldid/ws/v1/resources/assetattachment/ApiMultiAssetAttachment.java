package com.n4systems.fieldid.ws.v1.resources.assetattachment;

import com.n4systems.fieldid.ws.v1.resources.asset.ApiAssetLink;

import java.util.List;

public class ApiMultiAssetAttachment {
	private ApiAssetAttachment assetAttachmentTemplate;
	private List<ApiAssetLink> attachments;

	public ApiAssetAttachment getAssetAttachmentTemplate() {
		return assetAttachmentTemplate;
	}

	public void setAssetAttachmentTemplate(ApiAssetAttachment assetAttachmentTemplate) {
		this.assetAttachmentTemplate = assetAttachmentTemplate;
	}

	public List<ApiAssetLink> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<ApiAssetLink> attachments) {
		this.attachments = attachments;
	}
}
