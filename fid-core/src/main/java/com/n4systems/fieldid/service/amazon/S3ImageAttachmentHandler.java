package com.n4systems.fieldid.service.amazon;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.images.ImageService;
import com.n4systems.model.attachment.S3Attachment;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class S3ImageAttachmentHandler implements S3AttachmentHandler {

    @Autowired private ImageService imageService;

    @Override
    public List<S3Attachment> getFlavours(S3Attachment attachment) {
        List<S3Attachment> result = Lists.newArrayList(attachment);
        S3Attachment medium = new Flavour(attachment, ".medium", imageService.generateMedium(attachment.getBytes()));
        result.add(medium);
        S3Attachment thumbnail = new Flavour(attachment, ".thumbnail", imageService.generateThumbnail(attachment.getBytes()));
        result.add(thumbnail);
        return result;
    }


    class Flavour implements S3Attachment {
        S3Attachment delegate;
        String suffix;
        byte[] bytes;

        Flavour(S3Attachment delegate, String suffix, byte[] bytes) {
            this.delegate = delegate;
            this.suffix = suffix;
            this.bytes = bytes;
        }

        @Override
        public byte[] getBytes() {
            return bytes;
        }

        @Override
        public String getContentType() {
            return delegate.getContentType();
        }

        @Override
        public String getComments() {
            return delegate.getComments();
        }

        @Override
        public String getPath() {
            return delegate.getPath()+suffix;
        }

        @Override
        public String getTempPath() {
            return delegate.getTempPath()+suffix;
        }

    }
}
