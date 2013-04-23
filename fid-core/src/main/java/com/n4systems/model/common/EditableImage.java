package com.n4systems.model.common;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.n4systems.model.parents.EntityWithTenant;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "editable_images")
@Inheritance(strategy= InheritanceType.JOINED)
public class EditableImage extends EntityWithTenant implements S3Image {

    @OneToMany(mappedBy = "image", cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ImageAnnotation> annotations = Lists.newArrayList();

    @Column(name = "filename", nullable = false)
    private String fileName;

    // TODO DD : remove this.
    @Column(name = "thumbnail")
    private String thumbnail;

    @Column(name = "mobileguid")
    private String mobileGUID;

    @Transient
    private String tempFileName;

    @Transient
    private String contentType;

    public EditableImage() {

    }

    public EditableImage(String fileName) {
        this.fileName = fileName;
    }

    public List<ImageAnnotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<ImageAnnotation> annotations) {
        this.annotations = annotations;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String getS3Path() {
        return fileName;
    }

    public EditableImage addImageAnnotation(ImageAnnotation annotation) {
        if (!getAnnotations().contains(annotation)) {
            getAnnotations().add(annotation);
        }
        if(annotation.getImage()!=null && annotation.getImage().getId()!=this.getId()) {  // remove it from other image, add it to this one.
            annotation.getImage().removeAnnotation(annotation);
        }
        annotation.setImage(this);
        annotation.setTenant(this.getTenant());
        return this;
    }

    public void removeAnnotation(ImageAnnotation annotation) {
        Preconditions.checkArgument(getAnnotations().contains(annotation),"trying to remove annotation " + annotation.getId() + " that isn't currently contained in " + getId());
        getAnnotations().remove(annotation);
    }

    public String getMobileGUID() {
        return mobileGUID;
    }
    public void setMobileGUID(String mobileGUID) {
        this.mobileGUID = mobileGUID;
    }

    @Override
    protected void onUpdate() {
        super.onUpdate();
        ensureMobileGuidIsSet();
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        ensureMobileGuidIsSet();
    }

    private void ensureMobileGuidIsSet() {
        if (mobileGUID == null) {
            mobileGUID = UUID.randomUUID().toString();
        }
    }

    public String getTempFileName() {
        return tempFileName;
    }

    public void setTempFileName(String tempFileName) {
        this.tempFileName = tempFileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

}
