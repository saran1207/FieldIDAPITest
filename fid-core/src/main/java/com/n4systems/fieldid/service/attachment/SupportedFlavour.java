package com.n4systems.fieldid.service.attachment;

import java.util.EnumSet;

public enum SupportedFlavour {

    IMG_64x64("96x96"),
    IMG_320x200("320x200"),
    IMG_640x480("640x480");

    public static EnumSet<SupportedFlavour> imageFlavours = EnumSet.of(IMG_320x200,IMG_640x480,IMG_64x64);

    private String requestString;

    private SupportedFlavour(String requestString) {
        this.requestString = requestString;
    }

    public String getRequestString() {
        return requestString;
    }
}
