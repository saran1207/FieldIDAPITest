package com.n4systems.fieldid.service.amazon;

import com.n4systems.model.attachment.S3Attachment;

import java.util.List;

public interface S3AttachmentHandler {

    List<S3Attachment> createFlavours(S3Attachment attachment);

    List<S3Attachment> getFlavours(S3Attachment attachment);
}
