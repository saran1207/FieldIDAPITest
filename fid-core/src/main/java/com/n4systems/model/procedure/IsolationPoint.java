package com.n4systems.model.procedure;

import com.n4systems.model.IsolationPointSourceType;
import com.n4systems.model.PlatformType;
import com.n4systems.model.api.HasCreatedModifiedPlatform;
import com.n4systems.model.common.ImageAnnotation;
import com.n4systems.model.parents.EntityWithTenant;

import javax.persistence.*;
import java.util.Comparator;

@Entity
@Table(name = "isolation_points")
public class IsolationPoint extends EntityWithTenant implements HasCreatedModifiedPlatform {

    public static Comparator<IsolationPoint> LOCK_ORDER = new Comparator<IsolationPoint>() {
        @Override
        public int compare(IsolationPoint o1, IsolationPoint o2) {
            return o1.getFwdIdx().compareTo(o2.getFwdIdx());
        }
    };

    public static Comparator<IsolationPoint> UNLOCK_ORDER = new Comparator<IsolationPoint>() {
        @Override
        public int compare(IsolationPoint o1, IsolationPoint o2) {
            return o1.getRevIdx().compareTo(o2.getRevIdx());
        }
    };

    @Column(name="electronic_identifier")
    private String electronicIdentifier;

    @Column(name="identifier")
    private String identifier;

    @Column(name="source_text")
    private String sourceText;

    @Enumerated(EnumType.STRING)
    @Column(name="source")
    private IsolationPointSourceType sourceType = IsolationPointSourceType.getDefault();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="device_definition_id")
    private IsolationDeviceDescription deviceDefinition = new IsolationDeviceDescription();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="lock_definition_id")
    private IsolationDeviceDescription lockDefinition = new IsolationDeviceDescription();

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "image_annotation_id")
    private ImageAnnotation annotation;

    @Column(name="location")
    private String location;

    @Column(name="method")
    private String method;

    @Column(name="check_str")
    private String check;

    @Column(name="fwd_idx")
    private Integer fwdIdx;

    @Column(name="rev_idx")
    private Integer revIdx;

    @Column(name="modified_platform", length = 200)
    private String modifiedPlatform;

    @Column(name="created_platform", length = 200)
    private String createdPlatform;

    @Enumerated(EnumType.STRING)
    @Column(name="modified_platform_type")
    private PlatformType modifiedPlatformType;

    @Enumerated(EnumType.STRING)
    @Column(name="created_platform_type")
    private PlatformType createdPlatformType;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public IsolationPointSourceType getSourceType() {
        return sourceType;
    }

    public void setSourceType(IsolationPointSourceType source) {
        this.sourceType = source;
    }

    public IsolationDeviceDescription getDeviceDefinition() {
        return deviceDefinition;
    }

    public void setDeviceDefinition(IsolationDeviceDescription deviceDefinition) {
        this.deviceDefinition = deviceDefinition;
    }

    public IsolationDeviceDescription getLockDefinition() {
        return lockDefinition;
    }

    public void setLockDefinition(IsolationDeviceDescription lockDefinition) {
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

    public ImageAnnotation getAnnotation() {
        return annotation;
    }

    public void setAnnotation(ImageAnnotation annotation) {
        this.annotation = annotation;
    }

    public String getSourceText() {
        return sourceText;
    }

    public void setSourceText(String sourceText) {
        this.sourceText = sourceText;
    }

    public String getElectronicIdentifier() {
        return electronicIdentifier;
    }

    public void setElectronicIdentifier(String electronicIdentifier) {
        this.electronicIdentifier = electronicIdentifier;
    }

    public Integer getFwdIdx() {
        return fwdIdx;
    }

    public void setFwdIdx(Integer fwdIdx) {
        this.fwdIdx = fwdIdx;
    }

    public Integer getRevIdx() {
        return revIdx;
    }

    public void setRevIdx(Integer revIdx) {
        this.revIdx = revIdx;
    }

    @Override
    public String getModifiedPlatform() {
        return modifiedPlatform;
    }

    @Override
    public void setModifiedPlatform(String modifiedPlatform) {
        this.modifiedPlatform = modifiedPlatform;
    }

    @Override
    public String getCreatedPlatform() {
        return createdPlatform;
    }

    @Override
    public void setCreatedPlatform(String createdPlatform) {
        this.createdPlatform = createdPlatform;
    }

    @Override
    public PlatformType getModifiedPlatformType() {
        return modifiedPlatformType;
    }

    @Override
    public void setModifiedPlatformType(PlatformType modifiedPlatformType) {
        this.modifiedPlatformType = modifiedPlatformType;
    }

    @Override
    public PlatformType getCreatedPlatformType() {
        return createdPlatformType;
    }

    @Override
    public void setCreatedPlatformType(PlatformType createdPlatformType) {
        this.createdPlatformType = createdPlatformType;
    }
}
