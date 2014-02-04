package com.n4systems.fieldid.service.attachment;

import com.amazonaws.services.s3.AmazonS3Client;
import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.amazon.S3AttachmentHandler;
import com.n4systems.fieldid.service.uuid.UUIDService;
import com.n4systems.model.Tenant;
import com.n4systems.model.attachment.*;
import com.n4systems.model.orgs.BaseOrg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

@Transactional
public class AttachmentService extends FieldIdPersistenceService {

    private @Autowired AmazonS3Client s3Client;
    private @Autowired S3AttachmentHandler s3AttachmentHandler;
    private @Autowired FileAttachmentHandler fileAttachmentHandler;
    private @Autowired UUIDService uuidService;

    private static List<SupportedFlavour> imageFlavoursToCache = Lists.newArrayList(SupportedFlavour.imageFlavours);


    public void get(Attachment attachment) {
        URL url = getAttachmentUrl(attachment);
        try {
            URLConnection connection = url.openConnection();
            BufferedImage image = ImageIO.read(url);

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public void upload(Attachment attachment) {
        getAttachmentHandler(attachment).upload(attachment);
    }

    public void uploadTemp(Attachment attachment) {
        getAttachmentHandler(attachment).uploadTemp(attachment);
    }

    public void finalize(Attachment attachment) {
        getAttachmentHandler(attachment).finalize(attachment);
    }

    public URL getAttachmentUrl(Attachment attachment, String flavourRequest) {
        return getAttachmentHandler(attachment).getUrl(attachment,flavourRequest);
    }

    public URL getAttachmentUrl(Attachment attachment) {
        return getAttachmentHandler(attachment).getUrl(attachment);
    }

    public int remove(Attachment attachment) {
        return getAttachmentHandler(attachment).remove(attachment);
    }

    private AttachmentHandler getAttachmentHandler(Attachment attachment) {
        if (attachment instanceof S3Attachment) {
            return s3AttachmentHandler;
        } else if (attachment instanceof LocalFileAttachment) {
            return fileAttachmentHandler;
        }
        throw new IllegalArgumentException("attachment type " + attachment.getClass().getSimpleName() + " does not have a handler to support it");
    }

    public AbstractAttachment save(AbstractAttachment attachment) {
        if (Attachment.State.VOID.equals(attachment.getState())) {
            upload(attachment);
        } else if (Attachment.State.LIMBO.equals(attachment.getState())) {
            finalize(attachment);
        }
        persistenceService.save(attachment);
        return attachment;
    }

    public S3Attachment createPlaceImageAttachment(BaseOrg org, String fileName, String contentType, byte[] bytes) {
        return createImageAttachment(org.getTenant(), fileName, contentType, bytes)
                .addMetaInf("org",org.getId());
    }

    public S3Attachment createImageAttachment(Tenant tenant, String fileName, String contentType, byte[] bytes) {
        return new S3Attachment(tenant)
                .withFlavoursToInitiallyCache(imageFlavoursToCache)
                .withContent(fileName, contentType, bytes)
                .addMetaInf("type", "image")
                .named(uuidService.createUuid());
    }

    public S3Attachment createBogusImageAttachment(Tenant tenant, String fileName, String contentType, byte[] bytes) {
        return createImageAttachment(tenant, fileName, contentType, bytes)
                .withMetaInf("foo", "bar");
    }

    public LocalFileAttachment createBogusFileAttachment(Tenant tenant, String fileName, String contentType, byte[] bytes) {
        return new LocalFileAttachment(tenant)
                .withContent(fileName, contentType, bytes)
                .named(uuidService.createUuid())
                .withMetaInf("foo1", "bar1");
    }


    public <T extends AbstractAttachment> T find(Long id) {
        return (T) persistenceService.find(AbstractAttachment.class, id);
    }
}
