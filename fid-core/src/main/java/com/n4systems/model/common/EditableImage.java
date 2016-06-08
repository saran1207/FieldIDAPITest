package com.n4systems.model.common;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.n4systems.model.PlatformType;
import com.n4systems.model.api.HasCreatedModifiedPlatform;
import com.n4systems.model.parents.EntityWithTenant;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "editable_images")
@Inheritance(strategy= InheritanceType.JOINED)
@Cacheable
@org.hibernate.annotations.Cache(region = "ProcedureCache", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EditableImage extends EntityWithTenant implements S3Image, HasCreatedModifiedPlatform {

    @OneToMany(mappedBy = "image", fetch = FetchType.EAGER)
    @org.hibernate.annotations.Cache(region = "ProcedureCache-Collections", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<ImageAnnotation> annotations = Lists.newArrayList();
    
    @Column(name = "filename", nullable = false)
    private String fileName;

    @Column(name = "mobileguid")
    private String mobileGUID;

    @Transient
    private String tempFileName;

    @Transient
    private String contentType;

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

    public EditableImage() {
    }

    public EditableImage(String fileName) {
        this.fileName = fileName;
    }

    public List<ImageAnnotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<ImageAnnotation> annotations) {
        this.annotations.clear();
        this.annotations.addAll(annotations);
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

    public boolean hasAnnotation(ImageAnnotation annotation) {
        return getAnnotations().contains(annotation);
    }

    public void removeAnnotation(ImageAnnotation annotation) {
        Preconditions.checkArgument(getAnnotations().contains(annotation),"trying to remove annotation " + annotation.getId() + " that isn't currently contained in " + getId());
        getAnnotations().remove(annotation);
        annotation.setImage(null);
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

    private void ensureMobileGuidIsSet() {
        if (getMobileGUID() == null || getMobileGUID().length() == 0) {
            setMobileGUID(UUID.randomUUID().toString());
        }
    }

    public void addAnnotation(ImageAnnotation annotation) {
        if (!getAnnotations().contains(annotation)) {
            getAnnotations().add(annotation);
        }
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
