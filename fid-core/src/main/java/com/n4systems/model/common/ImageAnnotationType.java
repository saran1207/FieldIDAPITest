package com.n4systems.model.common;

import com.google.common.base.Joiner;

public enum ImageAnnotationType {

    // currently, the annotation types match up with IsolationPointSourceTypes.
    //  they don't have to, but it's just the only need now.

    // TODO DD : need to get proper images from matt here. these are placeholders.
    CP("control-pane","#D68741","#FFFFFF",	"#D68741","images/proof_test.jpg"),
    E("electrical", "#D52029","#FFFFFF","#D52029","images/setup-wizard-gear.png"),
    G("gas", "#7C4075","#FFFFFF","#7C4075","images/loto/gas.jpg"),
    P("pneumatic","#FFFFFF","#144B8F","#FFFFFF","images/single-event.png"),
    S("steam","#FFFFFF","#D83E37","#FFFFFF","images/loto/steam.jpg"),
    V("valve","#000000","#FFFFFF","#000000","images/loto/valve.jpg"),
    W("water", "#FFFFFF","#099C4F","#FFFFFF","images/loto/water.jpg");

    private String cssClass;
    private String fontColor;
    private String backgroundColor;
    private String borderColor;
    private String icon;

    private ImageAnnotationType(String cssClass,String borderColor, String backgroundColor, String fontColor, String icon) {
        this.cssClass = cssClass;
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

    public String getCssClass() {
        return cssClass;
    }

    public String getCss() {
        return Joiner.on(",").join("font-color:",fontColor,
                                    "background-color:",backgroundColor,
                                    "border-color:",borderColor,
                                    "background-url:",icon);
    }
}
