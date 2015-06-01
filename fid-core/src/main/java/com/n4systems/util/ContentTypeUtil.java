package com.n4systems.util;

import javax.activation.FileTypeMap;

public class ContentTypeUtil {

    public static String getContentType(String fileName) {
        if (fileName != null && fileName.toLowerCase().endsWith(".png")) {
            return "image/png";
        } else if (fileName != null && fileName.toLowerCase().endsWith(".ics")) {
            return "text/calendar";
        } else {
            return FileTypeMap.getDefaultFileTypeMap().getContentType(fileName);
        }
    }

}
