package com.n4systems.fieldid.service.amazon;

import com.n4systems.model.attachment.S3Attachment;

import java.util.List;

public interface S3AttachmentHandler {

    List<S3Attachment> createFlavours(S3Attachment attachment);

    List<S3Attachment> getFlavours(S3Attachment attachment);

//    void uploadAttachment();
//
//    AmazonS3Client getS3Client();
//    // TODO DD : add/refactor create,delete,finalize methods into handler.
//
//    void uploadTempAttachment(S3Attachment attachment);
//
//    void uploadAttachment(S3Attachment attachment);
//
//    void finalizeTempAttachment(S3Attachment attachment);
//
//    URL getAttachmentUrl(S3Attachment attachment, String suffix);

//    public void uploadTempAttachment(S3Attachment attachment) {
//        for (S3Attachment attachmentFlavour:getS3AttachmentHandler(attachment).createFlavours(attachment)) {
//            putObject(attachmentFlavour.getTempPath(), attachmentFlavour.getBytes(), attachmentFlavour.getContentType());
//        }
//    }
//
//    public void uploadAttachment(S3Attachment attachment) {
//        for (S3Attachment attachmentFlavour:getS3AttachmentHandler(attachment).createFlavours(attachment)) {
//            putObject(attachmentFlavour.getPath(), attachmentFlavour.getBytes(), attachmentFlavour.getContentType());
//        }
//    }
//
//    private S3AttachmentHandler getS3AttachmentHandler(S3Attachment attachment) {
//        // TODO : check meta-data for content-type and return appropriate handler.
//        // for now we only have one type of attachments running through this code.
//        return s3ImageAttachmentHandler;
//    }
//
//    public void finalize(S3Attachment attachment) {
//        List<S3Attachment> flavours = getS3AttachmentHandler(attachment).createFlavours(attachment);
//        for (S3Attachment flavour:flavours) {
//            finalizeImpl(flavour);
//        }
//    }
//
//    private void finalizeImpl(S3Attachment attachment) {
//        CopyObjectRequest copyObjectRequest = new CopyObjectRequest(
//                getBucket(),
//                attachment.getTempPath(),
//                getBucket(),
//                attachment.getPath());
//
//        getClient().copyObject(copyObjectRequest);
//        // what about removing from temp? when should this be done.
//    }
//
//    public URL getAttachmentUrl(S3Attachment attachment, String suffix) {
//        if (attachment==null || StringUtils.isBlank(attachment.getPath())) {
//            return null;
//        }
//        Date expires = new DateTime().plusDays(getExpiryInDays()).toDate();
//
//        String path = attachment.getPath();
//        if (StringUtils.isNotBlank(suffix)) {
//            path = suffix.startsWith(".") ? path+suffix : path + "." + suffix;
//        }
//        URL url = generatePresignedUrl(path, expires, HttpMethod.GET);
//        return url;
//    }
//
//    public URL getAttachmentUrl(S3Attachment attachment) {
//        return getAttachmentUrl(attachment,null);
//    }
//
//    public void removeAttachment(S3Attachment attachment) {
//        List<S3Attachment> flavours = getS3AttachmentHandler(attachment).getFlavours(attachment);
//        for (S3Attachment flavour:flavours) {
//            deleteObject(flavour.getPath());
//        }
//    }


}
