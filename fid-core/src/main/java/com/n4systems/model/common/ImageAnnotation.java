package com.n4systems.model.common;

import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.procedure.AnnotationType;

import javax.persistence.*;

@Entity
@Table(name="image_annotation")
public class ImageAnnotation extends EntityWithTenant {

    @Enumerated(EnumType.STRING)
    private ImageAnnotationType type = ImageAnnotationType.W;

    @Column(name="text")
    private String text;

    @ManyToOne(cascade={CascadeType.REFRESH,CascadeType.PERSIST,CascadeType.MERGE}, fetch= FetchType.EAGER, optional=false)
    @JoinColumn(name="image_id")
    private EditableImage image;

    private double x;

    private double y;

    private double x_tail = 0;

    private double y_tail = 0;

    private String fill;

    private String stroke;

    @Column(name="render_annotation")
    private boolean renderAnnotation;

    @Transient
    private Long tempId;

    public ImageAnnotation() {
    }

    public ImageAnnotation(double x, double y, String text, ImageAnnotationType type) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.type = type;
    }

    public ImageAnnotation(double x, double y, String text, ImageAnnotationType type, String fill, String stroke) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.type = type;
        this.fill = fill;
        this.stroke = stroke;
    }

    public ImageAnnotation(double x, double y, double x_tail, double y_tail, String text, ImageAnnotationType type) {
        this.x = x;
        this.y = y;
        this.x_tail = x_tail;
        this.y_tail = y_tail;
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

    public double getX_tail() {
        return x_tail;
    }

    public void setX_tail(double x_tail) {
        this.x_tail = x_tail;
    }

    public double getY_tail() {
        return y_tail;
    }

    public void setY_tail(double y_tail) {
        this.y_tail = y_tail;
    }

    public Long getTempId() {
        return tempId;
    }

    public void setTempId(Long tempId) {
        this.tempId = tempId;
    }

    public String getFill() {
        return fill;
    }

    public void setFill(String fill) {
        this.fill = fill;
    }

    public String getStroke() {
        return stroke;
    }

    public void setStroke(String stroke) {
        this.stroke = stroke;
    }

    public boolean hasCoordinates(AnnotationType type) {
        if (type.equals(AnnotationType.CALL_OUT_STYLE))
            return x != 0 && y != 0;
        else
            return x != 0 && y != 0 && x_tail != 0 && y_tail != 0;
    }

    public boolean isRenderAnnotation() {
        return renderAnnotation;
    }

    public void setRenderAnnotation(boolean renderAnnotation) {
        this.renderAnnotation = renderAnnotation;
    }
}
