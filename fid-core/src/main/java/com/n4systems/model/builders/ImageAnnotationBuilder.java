package com.n4systems.model.builders;

import com.n4systems.model.Tenant;
import com.n4systems.model.common.ImageAnnotation;
import com.n4systems.model.common.ImageAnnotationType;
import com.n4systems.model.procedure.ProcedureDefinitionImage;

public class ImageAnnotationBuilder extends EntityWithTenantBuilder<ImageAnnotation> {

    private ImageAnnotationType type = ImageAnnotationType.W;
    private String text = "hello";
    private ProcedureDefinitionImage image = ProcedureDefinitionImageBuilder.aProcedureDefinitionImage().build();
    private double x = .444;
    private double y =.666;
    private Long tempId = -111L;

    public static ImageAnnotationBuilder anImageAnnotation() {
        return new ImageAnnotationBuilder();
    }

    public ImageAnnotationBuilder(Tenant tenant) {
        super(tenant);
    }

    public ImageAnnotationBuilder() {
        this(TenantBuilder.n4());
    }

    public ImageAnnotationBuilder withX(double x) {
        this.x = x;
        return this;
    }
    public ImageAnnotationBuilder withText(String text) {
        this.text = text;
        return this;
    }

    public ImageAnnotationBuilder withY(double y) {
        this.y = y;
        return this;
    }

    public ImageAnnotationBuilder withImage(ProcedureDefinitionImage image) {
        this.image = image;
        return this;
    }

    @Override
    public ImageAnnotation createObject() {
        ImageAnnotation annotation = assignAbstractFields(new ImageAnnotation());
        annotation.setX(x);
        annotation.setY(y);
        annotation.setImage(image);
        image.addImageAnnotation(annotation);
        annotation.setText(text);
        annotation.setType(type);
        return annotation;
    }

}

