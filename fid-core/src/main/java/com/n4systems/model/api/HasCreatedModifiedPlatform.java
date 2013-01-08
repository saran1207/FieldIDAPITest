package com.n4systems.model.api;

import com.n4systems.model.PlatformType;

public interface HasCreatedModifiedPlatform {

    public String getModifiedPlatform();
    public void setModifiedPlatform(String modifiedPlatform);

    public String getCreatedPlatform();
    public void setCreatedPlatform(String createdPlatform);

    public PlatformType getModifiedPlatformType();
    public void setModifiedPlatformType(PlatformType platformType);

    public PlatformType getCreatedPlatformType();
    public void setCreatedPlatformType(PlatformType platformType);

}
