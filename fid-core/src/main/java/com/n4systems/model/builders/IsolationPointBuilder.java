package com.n4systems.model.builders;

import com.n4systems.model.IsolationPointSourceType;
import com.n4systems.model.Tenant;
import com.n4systems.model.common.ImageAnnotation;
import com.n4systems.model.procedure.IsolationDeviceDescription;
import com.n4systems.model.procedure.IsolationPoint;


public class IsolationPointBuilder extends EntityWithTenantBuilder<IsolationPoint> {

    private String electronicIdentifier;
    private String identifier;
    private String sourceText;
    private IsolationPointSourceType sourceType = IsolationPointSourceType.getDefault();
    private IsolationDeviceDescription deviceDefinition = new IsolationDeviceDescription();
    private IsolationDeviceDescription lockDefinition = new IsolationDeviceDescription();
    private ImageAnnotation annotation;
    private String location;
    private String method;
    private String check;

    public static IsolationPointBuilder anIsolationPoint() {
        return new IsolationPointBuilder(TenantBuilder.n4(),
                "new Isolation Point",
                IsolationPointSourceType.getDefault(),
                "ISO-source",
                new IsolationDeviceDescription(),
                new IsolationDeviceDescription(),
                "ISO-Location",
                "ISO-method",
                "ISO-check",
                "ISO-elecIdentifier"
        );
    }

    public IsolationPointBuilder() {
        this(TenantBuilder.n4(),"new Isolation Point", IsolationPointSourceType.getDefault());
    }

    private IsolationPointBuilder(Tenant tenant,
                                  String identifier,
                                  IsolationPointSourceType sourceType,
                                  String sourceText,
                                  IsolationDeviceDescription deviceDescription,
                                  IsolationDeviceDescription lockDescription,
                                  String location,
                                  String method,
                                  String check,
                                  String electronicIdentifier) {
        this(tenant, identifier, sourceType);
        this.method = method;
        this.check = check;
        this.location = location;
        this.lockDefinition = lockDescription;
        this.deviceDefinition = deviceDescription;
        this.sourceText = sourceText;
        this.electronicIdentifier = electronicIdentifier;
    }

    private IsolationPointBuilder(Tenant tenant,String identifier, IsolationPointSourceType sourceType ) {
        this.tenant = tenant;
        this.identifier = identifier;
        this.sourceType = sourceType;
    }

    public IsolationPointBuilder withSourceText(String text) {
        this.sourceText = text;
        return this;
    }
    public IsolationPointBuilder withDeviceDefinition(IsolationDeviceDescription deviceDefinition) {
        this.deviceDefinition = deviceDefinition;
        return this;
    }

    public IsolationPointBuilder withLockDefinition(IsolationDeviceDescription lockDefinition) {
        this.lockDefinition = lockDefinition;
        return this;
    }

    public IsolationPointBuilder withAnnotation(ImageAnnotation annotation) {
        this.annotation = annotation;
        return this;
    }

    public IsolationPointBuilder withLocation(String location) {
        this.location = location;
        return this;
    }

    public IsolationPointBuilder withMethod(String method) {
        this.method = method;
        return this;
    }

    public IsolationPointBuilder withCheck(String check) {
        this.check = check;
        return this;
    }

    @Override
    public IsolationPoint createObject() {
        IsolationPoint isolationPoint = assignAbstractFields(new IsolationPoint());
        isolationPoint.setIdentifier(identifier);
        isolationPoint.setSourceType(sourceType);
        isolationPoint.setSourceText(sourceText);
        isolationPoint.setAnnotation(annotation);
        return isolationPoint;
    }


    public IsolationPointBuilder withIdentifer(String identifier) {
        this.identifier = identifier;
        return this;
    }
}
