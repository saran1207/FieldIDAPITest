package com.n4systems.model.attachment;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.attachment.SupportedFlavour;
import com.n4systems.model.Tenant;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.Collection;
import java.util.List;

@Entity
@DiscriminatorValue(value= "S3_FILE") // === Type.toString()
public class S3Attachment extends AbstractAttachment implements CacheableAttachment {

    private @Transient List<SupportedFlavour> flavourRequests = Lists.newArrayList();

    public S3Attachment(Tenant tenant) {
        super(Type.S3_FILE,tenant);
        withSubdirectories("tenants", tenant.getId() + "");
    }

    public S3Attachment withFlavoursToInitiallyCache(List<SupportedFlavour> flavourRequests) {
        this.flavourRequests = flavourRequests;
        return this;
    }

    @Override
    public Collection<SupportedFlavour> getFlavoursToInitiallyCache() {
        return flavourRequests;
    }
}
