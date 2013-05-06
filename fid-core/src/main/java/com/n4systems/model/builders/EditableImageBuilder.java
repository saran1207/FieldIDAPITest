package com.n4systems.model.builders;

import com.google.common.collect.Lists;
import com.n4systems.model.Tenant;
import com.n4systems.model.common.EditableImage;
import com.n4systems.model.common.ImageAnnotation;

import java.util.List;

public class EditableImageBuilder<T extends EditableImage> extends EntityWithTenantBuilder<T> {

    private String fileName = "imageFile";
    private List<ImageAnnotation> annotations = Lists.newArrayList();

    public static EditableImageBuilder anEditableImage() {
        return new EditableImageBuilder(TenantBuilder.n4());
    }

    public EditableImageBuilder(Tenant tenant) {
        super(tenant);
    }

    public EditableImageBuilder() {
        this(TenantBuilder.n4());
    }

    public EditableImageBuilder withFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public EditableImageBuilder withAnnotations(List<ImageAnnotation> annotations) {
        this.annotations = annotations;
        return this;
    }

    @Override
    public T createObject() {
        T image = createObjectImpl();
        image.setFileName(fileName);
        image.setAnnotations(annotations);
        return image;
    }

    protected T createObjectImpl() {
        return null;
    }


}
