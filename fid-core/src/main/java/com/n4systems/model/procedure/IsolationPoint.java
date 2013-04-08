package com.n4systems.model.procedure;

import com.n4systems.model.IsolationPointSourceType;
import com.n4systems.model.common.ImageAnnotation;
import com.n4systems.model.parents.EntityWithTenant;

import javax.persistence.*;

@Entity
@Table(name = "isolation_points")
public class IsolationPoint extends EntityWithTenant {

    @Column(name="identifier")
    private String identifier;


    @Column(name="source_text")
    private String sourceText;

    @Enumerated(EnumType.STRING)
    @Column(name="source")
    private IsolationPointSourceType source = IsolationPointSourceType.getDefault();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="device_definition_id")
    private IsolationDeviceDescription deviceDefinition = new IsolationDeviceDescription();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="lock_definition_id")
    private IsolationDeviceDescription lockDefinition = new IsolationDeviceDescription();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_annotation_id")
    private ImageAnnotation annotation;

    @Column(name="location")
    private String location;

    @Column(name="method")
    private String method;

    @Column(name="check_str")
    private String check;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public IsolationPointSourceType getSource() {
        return source;
    }

    public void setSource(IsolationPointSourceType source) {
        this.source = source;
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
}
