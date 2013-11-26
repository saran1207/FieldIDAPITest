package com.n4systems.model.attachment;

import com.n4systems.model.builders.TenantBuilder;
import com.n4systems.model.orgs.BaseOrg;

import javax.persistence.Transient;

public class PlaceAttachment extends AbstractS3Attachment {

    private static String PATH = "/places/%d/attachments/%s";
    private static String TEMP_PATH = "/places/%d/attachments/temp/%s";

    protected @Transient BaseOrg org;

    @Deprecated // for debugging only..
    public PlaceAttachment(double bogus) {
        super();
        setFileName("/jenn.jpg");
        setTenant(TenantBuilder.aTenant().named("bogus").withId(123L).build());
    }


    public PlaceAttachment(BaseOrg org, String clientFileName, String contentType, byte[] bytes) {
        super(clientFileName, contentType, bytes);
        this.org = org;
        setTenant(org.getTenant());
    }

    @Override
    public String getRelativePath() {
        return String.format(PATH, org.getId(), getFileName());
    }

    @Override
    public String getRelativeTempPath() {
        return String.format(TEMP_PATH,org.getId(),getTempFileName());
    }

    @Override
    public Long getTenantId() {
        return org.getTenant().getId();
    }

}
