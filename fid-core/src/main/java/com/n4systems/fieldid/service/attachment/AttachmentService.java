package com.n4systems.fieldid.service.attachment;

import com.amazonaws.services.s3.AmazonS3Client;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.amazon.S3AttachmentHandler;
import com.n4systems.fieldid.service.amazon.S3ImageAttachmentHandler;
import com.n4systems.model.Tenant;
import com.n4systems.model.attachment.*;
import com.n4systems.model.orgs.BaseOrg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.Collection;

@Transactional
public class AttachmentService extends FieldIdPersistenceService {

    private @Autowired AmazonS3Client s3Client;
    private @Autowired S3ImageAttachmentHandler s3ImageAttachmentHandler;
    private @Autowired S3AttachmentHandler s3AttachmentHandler;
    private @Autowired FileAttachmentHandler fileAttachmentHandler;


    public void upload(Attachment attachment) {
        getAttachmentHandler(attachment).upload(attachment);
    }

    public void uploadTemp(Attachment attachment) {
        getAttachmentHandler(attachment).uploadTemp(attachment);
    }

    public void finalize(Attachment attachment) {
        getAttachmentHandler(attachment).finalize(attachment);
    }

    public URL getAttachmentUrl(Attachment attachment, Class<? extends Flavour> flavour) {
        return getAttachmentHandler(attachment).getUrl(attachment,flavour);
    }

    public URL getAttachmentUrl(Attachment attachment) {
        return getAttachmentHandler(attachment).getUrl(attachment);
    }

    public Collection<URL> getUrls(Attachment attachment) {
        return getAttachmentHandler(attachment).getUrls(attachment);
    }

    public int remove(Attachment attachment) {
        return getAttachmentHandler(attachment).remove(attachment);
    }

    private AttachmentHandler getAttachmentHandler(Attachment attachment) {
        if (attachment instanceof S3ImageAttachment) {
            return s3ImageAttachmentHandler;
        } else if (attachment instanceof S3FileAttachment) {
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

    public S3ImageAttachment createPlaceImageAttachment(BaseOrg org, String fileName, String contentType, byte[] bytes) {
        return new S3ImageAttachment(org.getTenant())
                .withContent(fileName, contentType, bytes)
                .withSubdirectories("places", org.getId()+"", "images");
    }

    public S3ImageAttachment createBogusImageAttachment(Tenant tenant, String fileName, String contentType, byte[] bytes) {
        return new S3ImageAttachment(tenant)
                .withContent(fileName, contentType, bytes)
                .withSubdirectories("foo", "bar");
    }

    public LocalFileAttachment createBogusFileAttachment(Tenant tenant, String fileName, String contentType, byte[] bytes) {
        return new LocalFileAttachment(tenant)
                .withContent(fileName, contentType, bytes)
                .withSubdirectories("foo1", "bar1");
    }


    public <T extends AbstractAttachment> T find(Long id) {
        return (T) persistenceService.find(AbstractAttachment.class, id);
    }
}
