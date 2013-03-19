package com.n4systems.model.common;

import com.google.common.collect.Lists;
import com.n4systems.model.parents.EntityWithTenant;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "editable_images")

public class EditableImage extends EntityWithTenant implements S3Image {

    @OneToMany(mappedBy = "image", cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ImageAnnotation> annotations = Lists.newArrayList();

    @Column(name = "filename", nullable = false)
    private String fileName;

    @Column(name = "thumbnail")
    private String thumbnail;

    //    TODO : this might not be needed
    @Column(name="original_filename")
    private String originalFile;

    @Transient
    private byte[] imageData;

    @Transient
    private String contentType;


    public EditableImage() {

    }


    public EditableImage(String originalFile) {
        this.originalFile = originalFile;
        this.fileName = originalFile;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
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

    public String getOriginalFile() {
        return originalFile;
    }

    public void setOriginalFile(String originalFile) {
        this.originalFile = originalFile;
    }

    @Override
    public String getS3Path() {
        return fileName;
    }
}
