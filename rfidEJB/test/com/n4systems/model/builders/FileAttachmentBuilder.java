package com.n4systems.model.builders;

import com.n4systems.model.FileAttachment;
import com.n4systems.model.Tenant;
import com.n4systems.testutils.TestHelper;

public class FileAttachmentBuilder extends BaseBuilder<FileAttachment> {

	private Tenant tenant;
	private String fileName;
	private String comments;
	
	public FileAttachmentBuilder(Tenant tenant, String fileName, String comments) {
		super();
		this.tenant = tenant;
		this.fileName = fileName;
		this.comments = comments;
	}
	
	public static FileAttachmentBuilder aFileAttachment() {
		return new FileAttachmentBuilder(TenantBuilder.aTenant().build(), TestHelper.randomString(), TestHelper.randomString());
	}
	
	@Override
	public FileAttachment build() {
		FileAttachment attachment = new FileAttachment();
		
		attachment.setId(id);
		attachment.setTenant(tenant);
		attachment.setFileName(fileName);
		attachment.setComments(comments);
		
		return attachment;
	}

}
