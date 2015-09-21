package com.n4systems.fieldid.ws.v2.resources.setupdata.assettype;

import com.n4systems.fieldid.ws.v2.resources.model.ApiReadOnlyModel;

public class ApiAssetTypeGroup extends ApiReadOnlyModel {
	private String name;
	private Long weight;
    private boolean lotoLock;
    private boolean lotoDevice;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getWeight() {
		return weight;
	}

	public void setWeight(Long weight) {
		this.weight = weight;
	}

    public boolean isLotoLock() {
        return lotoLock;
    }

    public void setLotoLock(boolean lotoLock) {
        this.lotoLock = lotoLock;
    }

    public boolean isLotoDevice() {
        return lotoDevice;
    }

    public void setLotoDevice(boolean lotoDevice) {
        this.lotoDevice = lotoDevice;
    }
}
