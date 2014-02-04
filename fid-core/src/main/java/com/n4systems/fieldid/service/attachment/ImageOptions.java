package com.n4systems.fieldid.service.attachment;

public class ImageOptions implements FlavourOptions {

    private int width;
    private int height;
    // other stuff to add here...constraining, bg color, transparency, max size, min size etc.....


    public ImageOptions(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public ImageOptions(String flavourRequest) {

    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String toFlavourRequestString() {
        return width + "x" + height;
    }
}
