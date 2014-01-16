package com.n4systems.model.attachment;

import com.n4systems.model.Tenant;

public class AbstractS3Attachment extends AbstractAttachment {

    public AbstractS3Attachment(Type type, Tenant tenant) {
        super(type,tenant);
        withSubdirectories("tenants", tenant.getId() + "");
    }

}
