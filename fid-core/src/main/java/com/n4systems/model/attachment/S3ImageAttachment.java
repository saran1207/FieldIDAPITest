package com.n4systems.model.attachment;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.attachment.Flavour;
import com.n4systems.fieldid.service.attachment.ImageFlavour;
import com.n4systems.model.Tenant;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Collection;
import java.util.List;

@Deprecated // DELETE THIS
@Entity
@DiscriminatorValue(value= "S3_IMAGE") // === Type.toString()
public class S3ImageAttachment
{}
//
//        extends S3Attachment {
//
//
//    public S3ImageAttachment(Tenant tenant) {
//        super(Type.S3_IMAGE,tenant);
//    }
//
//    @Override
//    public Collection<String> getFlavoursToInitiallyCache() {
//        return preferredFlavours;
//    }
//
//}
