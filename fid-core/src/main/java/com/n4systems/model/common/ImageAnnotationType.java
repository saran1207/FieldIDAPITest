package com.n4systems.model.common;

public enum ImageAnnotationType {

    // currently, the annotation types match up with IsolationPointSourceTypes.
    //  they don't have to, it's just the only need now.

    // TODO : need to get proper images from matt here. these are placeholders.
    CP("#D68741","#FFFFFF",	"#D68741","images/proof_test.jpg"),
    E("#D52029","#FFFFFF","#D52029","images/setup-wizard-gear.png"),
    G("#7C4075","#FFFFFF","#7C4075","images/small-help-logo.png"),
    P("#FFFFFF","#144B8F","#FFFFFF","images/single-event.png"),
    S("#FFFFFF","#D83E37","#FFFFFF","images/tip-icon.png"),
    V("#000000","#FFFFFF","#000000","images/user-icon.png"),
    W("#FFFFFF","#099C4F","#FFFFFF","images/www.png");
    
    private String fontColor;
    private String backgroundColor;
    private String borderColor;
    private String icon;

    private ImageAnnotationType(String backgroundColor, String borderColor, String fontColor, String icon) {
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
        this.fontColor = fontColor;
        this.icon = icon;
    }

    public static ImageAnnotationType getDefault() {
        return ImageAnnotationType.W;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCss() {
        return "";
    }
}
