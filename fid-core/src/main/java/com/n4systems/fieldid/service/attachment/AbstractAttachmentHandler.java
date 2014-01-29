package com.n4systems.fieldid.service.attachment;

import com.google.common.base.Preconditions;
import com.n4systems.model.attachment.Attachment;
import com.n4systems.model.attachment.CacheableAttachment;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

public abstract class AbstractAttachmentHandler<T extends Attachment> implements AttachmentHandler<T> {

    private @Autowired FlavourFactory flavourFactory;


    // URL - strip off attachment & flavour.   tenant/257514893/320x200/adsfj847dkd829d
    protected Flavour getAttachmentFlavour(T attachment, String flavourRequest) {
        Preconditions.checkNotNull(flavourRequest, "must specify a valid non-null flavour class");
        return flavourFactory.createFlavour(attachment, flavourRequest);
    }

    public String getTempPath(T attachment) {
        return "temp/" + attachment.getPath();
    }

    // PULL UP to abstract attachment handler.
    protected void primeCache(T attachment) {
         if(attachment instanceof CacheableAttachment) {
            primeCache(attachment, ((CacheableAttachment) attachment).getFlavoursToInitiallyCache());
            // priming cache means just doing requests for different flavours via webservice??
         }
    }

    protected void primeCache(T attachment, Collection<SupportedFlavour> flavours) {
        for (SupportedFlavour flavour : flavours) {
            flavour.getRequestString();
            // make request....
            // or add request entities to WebServiceCacheQueue??
        }
    }


}
