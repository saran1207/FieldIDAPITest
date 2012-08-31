package com.n4systems.util.math;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MathUtilTest {

    @Test
    public void test_nullSafeMin() {
        Long x = 123L;
        Long y = 999L;
        Long Null = null;
        Long anotherNull = null;

        assertEquals(x, MathUtil.nullSafeMin(Null, x));
        assertEquals(x, MathUtil.nullSafeMin(x, Null));
        assertEquals(null, MathUtil.nullSafeMin(anotherNull, Null));
        assertEquals(x, MathUtil.nullSafeMin(x, x));
        assertEquals(y, MathUtil.nullSafeMin(y, y));
        assertEquals(x, MathUtil.nullSafeMin(y, x));
        assertEquals(x, MathUtil.nullSafeMin(x, y));
    }

    @Test
    public void test_nullSafeMax() {
        Long x = 123L;
        Long y = 999L;
        Long Null = null;
        Long anotherNull = null;

        assertEquals(x, MathUtil.nullSafeMax(Null, x));
        assertEquals(x, MathUtil.nullSafeMax(x, Null));
        assertEquals(null, MathUtil.nullSafeMax(anotherNull, Null));
        assertEquals(x, MathUtil.nullSafeMax(x, x));
        assertEquals(y, MathUtil.nullSafeMax(y, x));
        assertEquals(y, MathUtil.nullSafeMax(x, y));

    }

}
