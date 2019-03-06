package com.n4systems.util.math;

public class MathUtil {

    public static <T extends Comparable> T nullSafeMax(T a, T b) {
        return a==null ? b :
                b==null ? a :
                        a.compareTo(b) < 0 ? b : a;
    }

    public static <T extends Comparable> T nullSafeMin(T a, T b) {
        return a==null ? b :
                b==null ? a :
                        a.compareTo(b) < 0 ? a : b;
    }

    /**
     * The method performs regex check: myString.matches("[A-Za-z0-9]+")
     * https://stackoverflow.com/questions/12831719/fastest-way-to-check-a-string-is-alphanumeric-in-java
     * @param str
     * @return
     */
    public static boolean isAlphanumeric(String str) {
        for (int i=0; i<str.length(); i++) {
            char c = str.charAt(i);
            if (c < 0x30 || (c >= 0x3a && c <= 0x40) || (c > 0x5a && c <= 0x60) || c > 0x7a)
                return false;
        }
        return true;
    }
}
