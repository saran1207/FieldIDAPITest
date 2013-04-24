package com.n4systems.model.common;

import com.google.common.base.Joiner;

//There is a corresponding table 'image_annotation_type' with this information in that database
//Should we be using the db or is the enum enough?

public enum ImageAnnotationType {

    // currently, the annotation types match up with IsolationPointSourceTypes.
    //  they don't have to, but it's just the only need now.

    CP("control-panel","#D68741","#FFFFFF",	"#D68741","images/loto/proof_test.jpg","images/loto/control-panel-full.png"),
    E("electrical", "#D52029","#FFFFFF","#D52029","images/setup-wizard-gear.png","images/loto/electrical-full.png"),
    G("gas", "#7C4075","#FFFFFF","#7C4075","images/loto/gas.jpg","images/loto/gas-full.png"),
    P("pneumatic","#FFFFFF","#144B8F","#FFFFFF","images/single-event.png","images/loto/pneumatic-full.png"),
    S("steam","#FFFFFF","#D83E37","#FFFFFF","images/loto/steam.jpg","images/loto/steam-full.png"),
    V("valve","#000000","#FFFFFF","#000000","images/loto/valve.jpg","images/loto/valve-full.png"),
    W("water", "#FFFFFF","#099C4F","#FFFFFF","images/loto/water.jpg","images/loto/water-full.png");

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
}
