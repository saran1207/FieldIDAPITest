package com.n4systems.model.common;

import com.n4systems.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="image_annotation_type")
public class ImageAnnotationType extends BaseEntity {

    public static final ImageAnnotationType DEFAULT = new ImageAnnotationType();

// TODO : turn this into an ENUMERATION = ELECTRICAL, WATER, ETC.... field id wide definitions.  can restrict with join table if needed.
    String url;//s3 url key.
    String color;
    String borderColor = "black";
    String borderRadius;
    String background = "white";


    public ImageAnnotationType() {
    }

    public String getCss() {
        // TODO : implement this...join all parameters into a single style string.
        return "padding:4px; background:"+background+";border:4px dotted " + borderColor+ ";color:black";
    }

    public ImageAnnotationType withBorderColor(String color) {
        this.borderColor = normalizedString(color);
        return this;
    }

    public ImageAnnotationType withBackground(String color) {
        this.background = normalizedString(color);
        return this;
    }

    private String normalizedString(String s) {
        // TODO strip off any trailing semi-colons.
        return s.trim();
    }
}
