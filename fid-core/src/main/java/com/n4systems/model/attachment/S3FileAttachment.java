package com.n4systems.model.attachment;


import com.n4systems.model.Tenant;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


@Deprecated // DELETE THIS
@Entity
@DiscriminatorValue(value= "S3_FILE")  // Type.toString()
public class S3FileAttachment    {}


//        extends AbstractAttachment {
//
//    public S3FileAttachment(Tenant tenant) {
//        super(Type.S3_FILE,tenant);
//        withSubdirectories("tenants", tenant.getId() + "");
//    }
//
//}