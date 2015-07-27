package com.n4systems.fieldid.ws.v2.resources.customerdata.procedure;

import java.util.Date;

public class ApiIsolationPointResult implements Comparable<ApiIsolationPointResult> {
    private Long isolationPointId;
    private String lockAssetId;
    private String deviceAssetId;
    private Date locationCheckTime;
    private Date methodCheckTime;
    private Date lockScanOrCheckTime;
    private Date deviceScanOrCheckTime;
    private Date checkCheckTime;

    public String getLockAssetId() {
        return lockAssetId;
    }

    public void setLockAssetId(String lockAssetId) {
        this.lockAssetId = lockAssetId;
    }

    public String getDeviceAssetId() {
        return deviceAssetId;
    }

    public void setDeviceAssetId(String deviceAssetId) {
        this.deviceAssetId = deviceAssetId;
    }

    public Date getLocationCheckTime() {
        return locationCheckTime;
    }

    public void setLocationCheckTime(Date locationCheckTime) {
        this.locationCheckTime = locationCheckTime;
    }

    public Date getMethodCheckTime() {
        return methodCheckTime;
    }

    public void setMethodCheckTime(Date methodCheckTime) {
        this.methodCheckTime = methodCheckTime;
    }

    public Date getLockScanOrCheckTime() {
        return lockScanOrCheckTime;
    }

    public void setLockScanOrCheckTime(Date lockScanOrCheckTime) {
        this.lockScanOrCheckTime = lockScanOrCheckTime;
    }

    public Date getDeviceScanOrCheckTime() {
        return deviceScanOrCheckTime;
    }

    public void setDeviceScanOrCheckTime(Date deviceScanOrCheckTime) {
        this.deviceScanOrCheckTime = deviceScanOrCheckTime;
    }

    public Date getCheckCheckTime() {
        return checkCheckTime;
    }

    public void setCheckCheckTime(Date checkCheckTime) {
        this.checkCheckTime = checkCheckTime;
    }

    public Long getIsolationPointId() {
        return isolationPointId;
    }

    public void setIsolationPointId(Long isolationPointId) {
        this.isolationPointId = isolationPointId;
    }

    @Override
    public int compareTo(ApiIsolationPointResult other) {
        return checkCheckTime.compareTo(other.checkCheckTime);
    }
}
