package com.n4systems.model.attachment;

import com.n4systems.model.Tenant;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value= "FILE") // === Type.FILE.toString()
public class LocalFileAttachment extends AbstractAttachment {

    public LocalFileAttachment(Tenant tenant) {
        super(Type.LOCAL_FILE, tenant);
        withSubdirectories("var","fieldid","private","tenant", getTenantId()+"");
        withPrefix("/");
    }

}




