package com.n4systems.model.common;

import com.google.common.base.Joiner;
import com.n4systems.model.IsolationPointSourceType;

public enum ImageAnnotationType {

    // currently, the annotation types match up with IsolationPointSourceTypes.
    //  they don't have to, but it's just the only need now.

    /*** Colors are also defined in annotated-image.css please update when changing colors!!! ***/

    W("water", "#FFFFFF","#00A650","#FFFFFF","images/loto/water.jpg","images/loto/water-full.png"),
    V("valve","#231F20","#FFFFFF","#231F20","images/loto/valve.jpg","images/loto/valve-full.png"),
    S("steam","#FFFFFF","#EF3E36","#FFFFFF","images/loto/steam.jpg","images/loto/steam-full.png"),
    P("pneumatic","#FFFFFF","#0055A5","#FFFFFF","images/single-event.png","images/loto/pneumatic-full.png"),
    G("gas", "#A5439A","#FFFFFF","#A5439A","images/loto/gas.jpg","images/loto/gas-full.png"),
    E("electrical", "#EE1C25","#FFFFFF","#EE1C25","images/setup-wizard-gear.png","images/loto/electrical-full.png"),
    CP("control-panel","#F78F1E","#FFFFFF",	"#F78F1E","none","images/loto/control-panel-full.png"),
    M("mechanical", "#808080","#FFFFFF","#808080","none","images/loto/mechanical-full.png"),
    H("hydraulic", "#000000","#FFFFFF","#000000","none","images/loto/hydraulic-full.png"),
    C("chemical", "#808080","#808080","#FCF903","none","images/loto/chemical-full.png"),

    //TODO This has to be unique for this "Isolation Point" type...
    N("notes", "#FFFFFF","#099C4F","#FFFFFF","images/loto/note.jpg","images/loto/note-full.png");

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