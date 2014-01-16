package com.n4systems.model.attachment;

import com.n4systems.model.Tenant;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value= "S3_IMAGE") // === Type.toString()
public class S3ImageAttachment extends AbstractS3Attachment {

    public S3ImageAttachment(Tenant tenant) {
        super(Type.S3_IMAGE,tenant);
    }

}
