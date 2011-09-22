package com.n4systems.util;

public class DoubleFormatter {

    public static String simplifyDouble(Double d) {
        if (d - d.intValue() != 0) {
            return d.toString();
        } else {
            return d.intValue()+"";
        }
    }

}
