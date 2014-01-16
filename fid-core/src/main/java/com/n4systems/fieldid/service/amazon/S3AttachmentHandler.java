package com.n4systems.fieldid.service.amazon;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.attachment.AttachmentHandler;
import com.n4systems.fieldid.service.attachment.Flavour;
import com.n4systems.model.attachment.Attachment;
import com.n4systems.services.ConfigService;
import com.n4systems.util.ConfigEntry;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.List;

public class S3AttachmentHandler implements AttachmentHandler<Attachment>, InitializingBean {

    private @Autowired AmazonS3Client s3Client;
    private @Autowired ConfigService configService;

    private String bucket;


    protected S3AttachmentHandler() {
    }

    private List<Attachment> createFlavours(Attachment attachment) {
        List<Attachment> result = Lists.newArrayList(attachment);
        for (Class<? extends Flavour> flavour: getFlavourTypes()) {
            Flavour attachmentFlavour = getAttachmentFlavour(attachment, flavour);
            attachmentFlavour.generateBytes();
            result.add(attachmentFlavour);
        }
        return result;
    }

    private List<Attachment> getFlavours(Attachment attachment) {
        List<Attachment> result = Lists.newArrayList(attachment);
        for (Class<? extends Flavour> flavour: getFlavourTypes()) {
            Flavour attachmentFlavour = getAttachmentFlavour(attachment, flavour);
            result.add(attachmentFlavour);
        }
        return result;
    }

    protected Flavour getAttachmentFlavour(Attachment attachment, Class<? extends Flavour> flavour) {
        throw new UnsupportedOperationException("if you have flavours in your handler, you must override this method to create them.");
    }

    protected List<Class<? extends Flavour>> getFlavourTypes() {
        return Lists.newArrayList();
    }

    @Override
    public void uploadTemp(Attachment attachment) {
        for (Attachment attachmentFlavour:getFlavours(attachment)) {
            putObject(attachmentFlavour.getTempPath(), attachmentFlavour.getBytes(), attachmentFlavour.getContentType());
            attachmentFlavour.setState(Attachment.State.LIMBO);
        }
    }

    @Override
    public void upload(Attachment attachment) {
        for (Attachment attachmentFlavour:createFlavours(attachment)) {
            putObject(attachmentFlavour.getPath(), attachmentFlavour.getBytes(), attachmentFlavour.getContentType());
            attachmentFlavour.setState(Attachment.State.UPLOADED);
        }
    }

    @Override
    public void finalize(Attachment attachment) {
        List<Attachment> flavours = createFlavours(attachment);
        for (Attachment flavour:flavours) {
            finalizeImpl(flavour);
        }
    }

    private void finalizeImpl(Attachment attachment) {
        CopyObjectRequest copyObjectRequest = new CopyObjectRequest(
                getBucket(),
                attachment.getTempPath(),
                getBucket(),
                attachment.getPath());

        s3Client.copyObject(copyObjectRequest);
        attachment.setState(Attachment.State.UPLOADED);
        // what about removing from temp? when should this be done.
    }

    protected int getExpiryInDays() {
        return 1;
    }

    @Override
    public URL getUrl(Attachment attachment, Class<? extends Flavour> flavour) {
        return getUrl(getAttachmentFlavour(attachment,flavour));
    }

    @Override
    public URL getUrl(Attachment attachment) {
        Preconditions.checkArgument(attachment!=null && StringUtils.isNotBlank(attachment.getPath()));
        Date expires = new DateTime().plusDays(getExpiryInDays()).toDate();
        URL url = s3Client.generatePresignedUrl(getBucket(), attachment.getPath(), expires, HttpMethod.GET);
        return url;
    }

    @Override
    public List<URL> getUrls(Attachment attachment) {
        List<URL> result = Lists.newArrayList(getUrl(attachment));
        for (Attachment flavour:getFlavours(attachment)) {
            result.add(getUrl(flavour));
        }
        return result;
    }

    @Override
    public int remove(Attachment attachment) {
        List<Attachment> flavours = getFlavours(attachment);
        for (Attachment flavour:flavours) {
            deleteObject(flavour.getPath());
        }
        return flavours.size();
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



