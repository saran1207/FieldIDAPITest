package com.n4systems.fieldid.api.mobile.resources.assetattachment;

import com.n4systems.fieldid.api.mobile.resources.asset.ApiAssetLink;

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
