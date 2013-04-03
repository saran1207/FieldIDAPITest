package com.n4systems.model.common;

import com.n4systems.model.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name="image_annotation")

public class ImageAnnotation extends BaseEntity /* TODO : Change to EntityWithTenant */ {

    // TODO : make this handled by javascript automatically.  shouldn't need this column.
    // i.e. if X>.50 direction = W etc...
    public enum Direction {
        N("north"), S("south"), W("west"), E("east");
        String css;
        Direction(String css) { this.css = css; }
        public String getCss() { return css; }
    };

    @Enumerated(EnumType.STRING)
    private ImageAnnotationType type = ImageAnnotationType.W;

    @Column(name="text")
    private String text;

    @Enumerated(EnumType.STRING)
    private Direction direction = Direction.W;

    @ManyToOne(cascade= CascadeType.REFRESH, fetch= FetchType.EAGER, optional=false)
    @JoinColumn(name="image_id")
    private EditableImage image;

    private double x;

    private double y;

    public ImageAnnotation() {
    }

    public ImageAnnotation withDirection(String direction) {
        setDirection(Direction.valueOf(direction.toUpperCase().substring(0,1)));
        return this;
    }

    public ImageAnnotation(double x, double y, String text, ImageAnnotationType type) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.type = type;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public EditableImage getImage() {
        return image;
    }

    public void setImage(EditableImage image) {
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

    public ImageAnnotation north() {
        this.direction = Direction.N;
        return this;
    }
    public ImageAnnotation south() {
        this.direction = Direction.S;
        return this;
    }
    public ImageAnnotation west() {
        this.direction = Direction.W;
        return this;
    }
    public ImageAnnotation east() {
        this.direction = Direction.E;
        return this;
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
