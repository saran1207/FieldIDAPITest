package com.n4systems.fieldid.ws.v1.resources.procedure;

import com.n4systems.fieldid.ws.v1.resources.model.ApiReadonlyModel;

public class ApiImageAnnotation extends ApiReadonlyModel {

    private String imageId;
    private String text;
    private String annotationType;
    private double x;
    private double y;
    private double x_tail;
    private double y_tail;

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAnnotationType() {
        return annotationType;
    }

    public void setAnnotationType(String annotationType) {
        this.annotationType = annotationType;
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
}
