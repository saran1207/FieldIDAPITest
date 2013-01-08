package com.n4systems.fieldid.ws.v1.resources.model;

import com.n4systems.model.PlatformType;
import com.n4systems.model.api.PlatformContext;

import java.util.Date;

public class ApiReadWriteModel implements PlatformContext {
	private String sid;
	private Date modified;
	private boolean active;

    private String appDevice = "SGH-17";
    private String appType = "Phone";
    private String appPlatform = "Android";
    private String osVersion = "2.3.5";
    private String appVersion = "2012.8.1";
    private String appNotes = "API Level 10";

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

    public String getAppDevice() {
        return appDevice;
    }

    public void setAppDevice(String appDevice) {
        this.appDevice = appDevice;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getAppPlatform() {
        return appPlatform;
    }

    public void setAppPlatform(String appPlatform) {
        this.appPlatform = appPlatform;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getAppNotes() {
        return appNotes;
    }

    public void setAppNotes(String appNotes) {
        this.appNotes = appNotes;
    }

    @Override
    public String getPlatform() {
        StringBuffer sb = new StringBuffer();
        if (getAppPlatform() != null) {
            sb.append(getAppPlatform()).append(", ");
        }
        if (getAppVersion() != null) {
            sb.append(getAppVersion()).append(" ");
        }
        if (getAppDevice() != null) {
            sb.append(getAppDevice()).append(" ");
        }
        if (getAppType() != null) {
            sb.append(getAppType()).append(" ");
        }
        if (getOsVersion() != null) {
            sb.append(getOsVersion()).append(" ");
        }
        if (getAppNotes() != null) {
            sb.append(getAppNotes()).append(" ");
        }
        return sb.toString().trim();
    }

    @Override
    public PlatformType getPlatformType() {
        return PlatformType.MOBILE;
    }
}
