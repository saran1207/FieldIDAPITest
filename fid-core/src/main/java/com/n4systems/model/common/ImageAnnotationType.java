package com.n4systems.model.common;

import com.google.common.base.Joiner;
import com.n4systems.model.IsolationPointSourceType;

public enum ImageAnnotationType {

    //This enum is similar to IsolationPointSourceTypes we should probably merge them

    /*** Colors are also defined in annotated-image.css please update when changing colors!!! ***/

    C("chemical",       "#FFFFFF",  "#660066",  "#FFFFFF",  "none",    "images/loto/chemical-full.png"),
    CP("control-panel", "#000000",  "#FF8000",  "#000000",  "none",    "images/loto/control-panel-full.png"),
    E("electrical",     "#FFFFFF",  "#FF0000",  "#FFFFFF",  "none",    "images/loto/electrical-full.png"),
    G("gas",            "#FFFFFF",  "#009900",  "#FFFFFF",  "none",    "images/loto/gas-full.png"),
    H("hydraulic",      "#FFFFFF",  "#994C00",  "#FFFFFF",  "none",    "images/loto/hydraulic-full.png"),
    M("mechanical",     "#FFFFFF",  "#666666",  "#FFFFFF",  "none",    "images/loto/mechanical-full.png"),
    N("notes",          "#000000",  "#FFFFFF",  "#000000",  "none",    "images/loto/note-full.png"),
    P("pneumatic",      "#000000",  "#99CCFF",  "#000000",  "none",    "images/loto/pneumatic-full.png"),
    S("steam",          "#000000",  "#FFFF00",  "#000000",  "none",    "images/loto/steam-full.png"),
    SP("stored-pressure","#FFFFFF", "#000000",  "#FFFFFF",  "none",    "images/loto/stored-pressure-full.png"),
    V("valve",          "#000000",  "#FFFFFF",  "#000000",  "none",    "images/loto/valve-full.png"),
    W("water",          "#FFFFFF",  "#0000FF",  "#FFFFFF",  "none",    "images/loto/water-full.png");

    // in the world of the browser, this is how the annotations are differentiated.
    // i.e. a P type will be rendered as <span class="pneumatic ..."/>
    private String cssClass;

    private String fontColor;
    private String backgroundColor;
    private String borderColor;

    private String icon;
    private final String fullIcon;

    private ImageAnnotationType(String cssClass,String borderColor, String backgroundColor, String fontColor, String icon, String fullIcon) {
        this.cssClass = cssClass;
        this.fullIcon = fullIcon;
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

    public String getBorderColor() {
        return borderColor;
    }

    public String getFontColor() {
        return fontColor;
    }

    public String getIcon() {
        return icon;
    }

    public String getCssClass() {
        return cssClass;
    }

    public String getFullIcon() {
        return fullIcon;
    }

    public String getCss() {
        return Joiner.on(",").join("font-color:",fontColor,
                                    "background-color:",backgroundColor,
                                    "border-color:",borderColor,
                                    "background-url:",icon);
    }

    public static ImageAnnotationType fromIsolationPointSourceType(IsolationPointSourceType source) {
        try {
            return valueOf(source.name());
        } catch (IllegalArgumentException iae) {
            return null;
        } catch (NullPointerException npe) {
            return null;
        }
    }

    public ImageAnnotationType toIsolationPointSourceType() {
        try {
            return valueOf(name());
        } catch (IllegalArgumentException iae) {
            return null;
        } catch (NullPointerException npe) {
            return null;
        }
    }

}