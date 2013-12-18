package com.n4systems.model.attachment;

import com.google.common.base.Preconditions;
import com.n4systems.model.orgs.BaseOrg;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue(value= "PLACE")
public class PlaceAttachment extends AbstractS3Attachment {

    private static String BASE_PATH = "/places/%d/attachments/";
    private static String PATH_FORMAT = BASE_PATH+"%s";
    private static String TEMP_PATH_FORMAT = BASE_PATH+"temp/%s";

    protected @Transient BaseOrg org;

    public PlaceAttachment() {
    }

    public PlaceAttachment(BaseOrg org) {
        super(Type.PLACE,org.getTenant());
        this.org = org;
    }

    @Override
    public String getRelativePath() {
        Preconditions.checkState(org!=null,"you must specify org before calling this");
        return String.format(PATH_FORMAT, org.getId(), getSubdiretoryAndFilename(getFileName()));
    }

    @Override
    public String getRelativeTempPath(String fileName) {
        Preconditions.checkState(org!=null,"you must specify org before calling this");
        return String.format(TEMP_PATH_FORMAT,org.getId(),getSubdiretoryAndFilename(fileName));
    }

    @Override
    public Long getTenantId() {
        return getTenant().getId();
    }

}
