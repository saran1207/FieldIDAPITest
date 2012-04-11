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

}
