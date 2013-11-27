package com.n4systems.model.attachment;

import com.n4systems.model.builders.TenantBuilder;
import com.n4systems.model.orgs.BaseOrg;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
public class PlaceAttachment extends AbstractS3Attachment {

    private static String BASE_PATH = "/places/%d/attachments/";
    private static String PATH_FORMAT = BASE_PATH+"%s";
    private static String TEMP_PATH_FORMAT = BASE_PATH+"temp/%s";

    protected @Transient BaseOrg org;

    @Deprecated // for debugging only..remove this later.
    public PlaceAttachment(double bogus) {
        super();
        setFileName("/jenn.jpg");
        setTenant(TenantBuilder.aTenant().named("bogus").withId(123L).build());
    }

    public PlaceAttachment(BaseOrg org) {
        super(Type.PLACE, org.getTenant());
        this.org = org;
    }

    @Override
    public String getRelativePath() {
        return String.format(PATH_FORMAT, org.getId(), getFileName());
    }

    @Override
    public String getRelativeTempPath(String fileName) {
        return String.format(TEMP_PATH_FORMAT,org.getId(),fileName);
    }

    @Override
    public Long getTenantId() {
        return getTenant().getId();
    }

}
