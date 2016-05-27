package com.n4systems.model.procedure;

import com.n4systems.model.Asset;
import com.n4systems.model.BaseEntity;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "isolation_point_results")
@Cacheable
@org.hibernate.annotations.Cache(region = "ProcedureCache", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class IsolationPointResult extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "isolation_point_id")
    private IsolationPoint isolationPoint;

    @ManyToOne
    @JoinColumn(name = "lock_asset_id")
    private Asset lock;

    @ManyToOne
    @JoinColumn(name = "device_asset_id")
    private Asset device;

    @Column(name="location_check_time")
    private Date locationCheckTime;

    @Column(name="method_check_time")
    private Date methodCheckTime;

    @Column(name="lock_check_time")
    private Date lockScanOrCheckTime;

    @Column(name="device_check_time")
    private Date deviceScanOrCheckTime;

    @Column(name="check_check_time")
    private Date checkCheckTime;

    public Asset getLock() {
        return lock;
    }

    public void setLock(Asset lock) {
        this.lock = lock;
    }

    public Asset getDevice() {
        return device;
    }

    public void setDevice(Asset device) {
        this.device = device;
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

    public IsolationPoint getIsolationPoint() {
        return isolationPoint;
    }

    public void setIsolationPoint(IsolationPoint isolationPoint) {
        this.isolationPoint = isolationPoint;
    }
}
