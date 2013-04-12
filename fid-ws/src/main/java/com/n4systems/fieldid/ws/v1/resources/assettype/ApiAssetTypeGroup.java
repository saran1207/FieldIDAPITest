package com.n4systems.fieldid.ws.v1.resources.assettype;

import com.n4systems.fieldid.ws.v1.resources.model.ApiReadonlyModel;

public class ApiAssetTypeGroup extends ApiReadonlyModel {
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
