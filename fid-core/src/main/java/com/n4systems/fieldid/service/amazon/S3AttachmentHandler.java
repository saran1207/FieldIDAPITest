package com.n4systems.fieldid.service.amazon;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.google.common.base.Preconditions;
import com.n4systems.fieldid.service.attachment.*;
import com.n4systems.fieldid.service.images.ImageService;
import com.n4systems.model.attachment.S3Attachment;
import com.n4systems.model.attachment.Attachment;
import com.n4systems.services.config.ConfigService;
import com.n4systems.util.ConfigEntry;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;
import java.util.Date;

public class S3AttachmentHandler extends AbstractAttachmentHandler<S3Attachment> implements InitializingBean {

    private @Autowired AmazonS3Client s3Client;
    private @Autowired ConfigService configService;
    private @Autowired ImageService imageService;


    private String bucket;


    public S3AttachmentHandler() {
    }

    @Override
    public void uploadTemp(S3Attachment attachment) {
        putObject(getTempPath(attachment), attachment.getBytes(), attachment.getContentType());
    }

    @Override
    public void upload(S3Attachment attachment) {
        putObject(attachment.getPath(), attachment.getBytes(), attachment.getContentType());
        attachment.setState(Attachment.State.UPLOADED);
        primeCache(attachment);
    }

    @Override
    public void finalize(S3Attachment attachment) {
        finalizeImpl(attachment);
        primeCache(attachment);
    }

    private void finalizeImpl(S3Attachment attachment) {
        CopyObjectRequest copyObjectRequest = new CopyObjectRequest(
                getBucket(),
                getTempPath(attachment),
                getBucket(),
                attachment.getPath());

        s3Client.copyObject(copyObjectRequest);
        attachment.setState(Attachment.State.UPLOADED);
    }

    @Deprecated  // do we need this anymore??
    protected int getExpiryInDays() {
        return 10000;
    }

    @Override
    public URL getUrl(S3Attachment attachment, String flavourRequest) {
        Flavour x = getAttachmentFlavour(attachment, flavourRequest);
        x.getBytes();
        x.getContentType();
        x.getComments();
        return null;
    }

    // either front-end hits this service to get bytes OR it asks for flavour.
    //   at the end of the day it needs bytes, contentType written to response.

    @Override
    public URL getUrl(S3Attachment attachment) {
        Preconditions.checkArgument(attachment!=null && StringUtils.isNotBlank(attachment.getPath()));
        Date expires = new DateTime().plusDays(getExpiryInDays()).toDate();
        URL url = s3Client.generatePresignedUrl(getBucket(), attachment.getPath(), expires, HttpMethod.GET);
        return url;
    }

    @Override
    public int remove(S3Attachment attachment) {
        deleteObject(attachment.getPath());
        // need to purge cache here!!???
        return 1;
    }

    private PutObjectResult putObject(String path, File file) {
        PutObjectResult result = s3Client.putObject(getBucket(), path, file);
        return result;
    }

    private PutObjectResult putObject(String path, byte[] data, String contentType) {
        ObjectMetadata objectMeta = new ObjectMetadata();
        objectMeta.setContentLength(data.length);
        objectMeta.setContentType(contentType);

        PutObjectResult result = s3Client.putObject(new PutObjectRequest(getBucket(), path, new ByteArrayInputStream(data), objectMeta));
        return result;
    }

    private void deleteObject(String path) {
        s3Client.deleteObject(getBucket(), path);
    }

    private S3Object getObject(String path) {
        return s3Client.getObject(getBucket(), path);
    }

    private ObjectMetadata getObjectMetadata(String path) {
        return s3Client.getObjectMetadata(getBucket(), path);
    }

    private String getBucket() {
        return bucket;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // CAVEAT : this is only set once when bean is created. assumes you don't change bucket mid-stream.
        bucket = configService.getString(ConfigEntry.AMAZON_S3_BUCKET);
    }

    public AmazonS3Client getS3Client() {
        return s3Client;
    }
}



