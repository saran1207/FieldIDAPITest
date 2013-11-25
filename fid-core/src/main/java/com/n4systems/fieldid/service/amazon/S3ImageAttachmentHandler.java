package com.n4systems.fieldid.service.amazon;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.images.ImageService;
import com.n4systems.model.attachment.S3Attachment;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class S3ImageAttachmentHandler implements S3AttachmentHandler {

    @Autowired private ImageService imageService;

    @Override
    public List<S3Attachment> getFlavours(S3Attachment attachment) {
        if (attachment.getTempFileName()!=null) {
            return getFlavours(attachment, attachment.getTempFileName());
        } else {
            return getFlavours(attachment, attachment.getFileName());
        }
    }

    private List<S3Attachment> getFlavours(S3Attachment attachment, String fileName) {
        List<S3Attachment> result = Lists.newArrayList(attachment);
        S3Attachment medium = new Flavour(attachment, fileName, ".medium", imageService.generateMedium(attachment.getBytes()));
        result.add(medium);
        S3Attachment thumbnail = new Flavour(attachment, fileName, ".thumbnail", imageService.generateThumbnail(attachment.getBytes()));
        result.add(thumbnail);
        return result;
    }


    class Flavour implements S3Attachment {
        S3Attachment delegate;
        String suffix;
        byte[] bytes;
        String md5sum;
        String fileName;

        Flavour(S3Attachment delegate, String fileName, String suffix, byte[] bytes) {
            this.delegate = delegate;
            this.suffix = suffix;
            this.bytes = bytes;
            this.md5sum = DigestUtils.md5Hex(bytes);
            this.fileName = fileName;
        }

        public byte[] getBytes() {
            return bytes;
        }

        public String getContentType() {
            return delegate.getContentType();
        }

        public String getFileName() {
            return fileName+suffix;
        }

        public String getTempFileName() {
            return delegate.getTempFileName();
        }

        public String getComments() {
            return delegate.getComments();
        }

        public String getTempPath() {
            return delegate.getTempPath()+suffix;
        }

        @Override
        public void setTempFileName(String uuid) {
            ;//
        }

        public String getMd5sum() {
            return md5sum;
        }
    }
}
