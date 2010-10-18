package com.n4systems.model.builders;

import com.n4systems.model.FileAttachment;
import com.n4systems.model.Tenant;
import com.n4systems.testutils.TestHelper;

public class FileAttachmentBuilder extends BaseBuilder<FileAttachment> {

	private static final String IMAGE_EXTENTION = "jpg";
	private static final String NON_IMAGE_EXTENTION = "pdf";
	private Tenant tenant;
	private String fileName;
	private String comments;
	
	public FileAttachmentBuilder(Tenant tenant, String fileName, String comments) {
		this.tenant = tenant;
		this.fileName = fileName;
		this.comments = comments;
	}
	
	private static FileAttachmentBuilder aFileAttachmentBuilder(String fileName) {
		return new FileAttachmentBuilder(TenantBuilder.aTenant().build(), fileName, TestHelper.randomString());
	}
	
	public static FileAttachmentBuilder anImageFile() {
		return aFileAttachmentBuilder(TestHelper.randomString() + "." + IMAGE_EXTENTION);
	}
	
	public static FileAttachmentBuilder aNonImageFile() {
		return aFileAttachmentBuilder(TestHelper.randomString() + "." + NON_IMAGE_EXTENTION);
	}
	
	public static FileAttachmentBuilder aFileAttachmentWithNoFile() {
		return aFileAttachmentBuilder(null);
	}
	
	public static FileAttachmentBuilder aFileAttachment() {
		return aNonImageFile();
	}
	
	public FileAttachmentBuilder withFileName(String fileName) {
		return new FileAttachmentBuilder(tenant, fileName, comments);
	}
	
	@Override
	public FileAttachment createObject() {
		FileAttachment attachment = new FileAttachment();
		
		attachment.setId(id);
		attachment.setTenant(tenant);
		attachment.setFileName(fileName);
		attachment.setComments(comments);
		
		return attachment;
	}

}
