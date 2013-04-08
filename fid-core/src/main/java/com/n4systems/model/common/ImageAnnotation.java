package com.n4systems.model.common;

import com.n4systems.model.parents.EntityWithTenant;

import javax.persistence.*;

@Entity
@Table(name="image_annotation")
public class ImageAnnotation extends EntityWithTenant {

    @Enumerated(EnumType.STRING)
    private ImageAnnotationType type = ImageAnnotationType.W;

    @Column(name="text")
    private String text;

    @ManyToOne(cascade= CascadeType.REFRESH, fetch= FetchType.EAGER, optional=false)
    @JoinColumn(name="image_id")
    private EditableImage image;

    private double x;

    private double y;

    public ImageAnnotation() {
    }

    public ImageAnnotation(double x, double y, String text, ImageAnnotationType type) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.type = type;
    }

    public EditableImage getImage() {
        return image;
    }

    public void setImage(EditableImage  image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ImageAnnotationType getType() {
        return type;
    }

    public void setType(ImageAnnotationType type) {
        this.type = type;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
