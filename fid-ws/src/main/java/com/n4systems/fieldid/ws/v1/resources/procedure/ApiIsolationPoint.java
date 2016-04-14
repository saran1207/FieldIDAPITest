package com.n4systems.fieldid.ws.v1.resources.procedure;

import com.n4systems.fieldid.ws.v1.resources.model.ApiReadonlyModel;

public class ApiIsolationPoint extends ApiReadonlyModel {

    private String electronicIdentifier;
    private String identifier;
    private String source;
    private String sourceText;
    private String deviceDefinition;
    private String lockDefinition;
    private String location;
    private String method;
    private String check;
    private ApiImageAnnotation annotation;
	private int fwdIdx;
	private int revIdx;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDeviceDefinition() {
        return deviceDefinition;
    }

    public void setDeviceDefinition(String deviceDefinition) {
        this.deviceDefinition = deviceDefinition;
    }

    public String getLockDefinition() {
        return lockDefinition;
    }

    public void setLockDefinition(String lockDefinition) {
        this.lockDefinition = lockDefinition;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public String getElectronicIdentifier() {
        return electronicIdentifier;
    }

    public void setElectronicIdentifier(String electronicIdentifier) {
        this.electronicIdentifier = electronicIdentifier;
    }

    public String getSourceText() {
        return sourceText;
    }

    public void setSourceText(String sourceText) {
        this.sourceText = sourceText;
    }

    public ApiImageAnnotation getAnnotation() {
        return annotation;
    }

    public void setAnnotation(ApiImageAnnotation annotation) {
        this.annotation = annotation;
    }

	public int getFwdIdx() {
		return fwdIdx;
	}

	public void setFwdIdx(int fwdIdx) {
		this.fwdIdx = fwdIdx;
	}

	public int getRevIdx() {
		return revIdx;
	}

	public void setRevIdx(int revIdx) {
		this.revIdx = revIdx;
	}
}
