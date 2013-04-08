package com.n4systems.model.common;

import com.google.common.collect.Lists;
import com.n4systems.model.parents.EntityWithTenant;

import javax.persistence.*;
import java.util.List;

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
}
