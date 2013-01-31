package com.n4systems.fieldid.wicket.util;

import com.n4systems.fieldid.wicket.pages.AmountWithString;
import com.n4systems.model.UnitType;
import junit.framework.Assert;
import org.junit.Test;

import javax.measure.converter.ConversionException;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class AmountParserTest {
    @Test
    public void testParse_default_standard_unit() throws Exception {
        String input = "100 m 5";
        AmountWithString result = createParser(UnitType.METRIC_LENGTH).parse(input);
        assertFalse(result.getAmount().isExact());
        Assert.assertEquals(252094.48818897636, result.getAmount().getEstimatedValue(), result.getAmount().getAbsoluteError());

        input = "1 1 1 1";
        result = createParser(UnitType.IMPERIAL_LENGTH).parse(input);
        assertEquals(input, result.getText());
        assertTrue(result.getAmount().isExact());
        assertEquals(4058176, result.getAmount().getExactValue());

        input = "2' 11\"";
        result = createParser(UnitType.IMPERIAL_LENGTH).parse(input);
        assertTrue(result.getAmount().isExact());
        assertEquals(2240.0, result.getAmount().getExactValue(), result.getAmount().getAbsoluteError());

        result = createParser(UnitType.METRE).parse(input="4 m 3cm");
        assertEquals(input,result.getText());
        assertFalse(result.getAmount().isExact());
        assertEquals(10154.330708661415,result.getAmount().getEstimatedValue(), result.getAmount().getAbsoluteError());

        result = createParser(UnitType.IMPERIAL_LENGTH).parse("4 feet 3");
        assertTrue(result.getAmount().isExact());
        assertEquals(3264, result.getAmount().getExactValue());
        assertEquals(3264.0, result.getAmount().getEstimatedValue(), result.getAmount().getAbsoluteError());

        result = createParser(UnitType.FEET).parse("4.12312 feet");
        Assert.assertEquals(3166.55616,result.getAmount().getEstimatedValue(),result.getAmount().getAbsoluteError());
        System.out.println(result.getAmount().getEstimatedValue());

        result = createParser(UnitType.IMPERIAL_LENGTH).parse("4 1 / 2 mile");
        assertTrue(result.getAmount().isExact());
        Assert.assertEquals(1.824768E7,result.getAmount().getEstimatedValue(),result.getAmount().getAbsoluteError());
        System.out.println(result.getAmount().getEstimatedValue());

        result = createParser(UnitType.IMPERIAL_LENGTH).parse("4-3/7 mile");
        Assert.assertEquals(1.7958034285714284E7,result.getAmount().getEstimatedValue(),result.getAmount().getAbsoluteError());
        System.out.println(result.getAmount().getEstimatedValue());

        result = createParser(UnitType.IMPERIAL_LENGTH).parse("4/6 mile");
        Assert.assertEquals(2703360.0,result.getAmount().getEstimatedValue(),result.getAmount().getAbsoluteError());
        System.out.println(result.getAmount().getEstimatedValue());

        result = createParser(UnitType.IMPERIAL_LENGTH).parse("4mile");
        Assert.assertEquals(1.622016E7,result.getAmount().getEstimatedValue(),result.getAmount().getAbsoluteError());
        System.out.println(result.getAmount().getEstimatedValue());

        result = createParser(UnitType.IMPERIAL_LENGTH).parse("4 ft");
        assertTrue(result.getAmount().isExact());
        Assert.assertEquals(3072.0,result.getAmount().getEstimatedValue(),result.getAmount().getAbsoluteError());
        Assert.assertEquals(3072,result.getAmount().getExactValue(),result.getAmount().getAbsoluteError());
        System.out.println(result.getAmount().getEstimatedValue());

        result =createParser(UnitType.METRIC_LENGTH).parse("4 m");
        Assert.assertEquals(10078.740157480315,result.getAmount().getEstimatedValue(),result.getAmount().getAbsoluteError());
        System.out.println(result.getAmount().getEstimatedValue());

        result = createParser(UnitType.IMPERIAL_LENGTH).parse("4 m");
        Assert.assertEquals(10078.740157480315,result.getAmount().getEstimatedValue(),result.getAmount().getAbsoluteError());
        System.out.println(result.getAmount().getEstimatedValue());

        result = createParser(UnitType.IMPERIAL_LENGTH).parse("4 mm");
        Assert.assertEquals(10.078740157480315,result.getAmount().getEstimatedValue(),result.getAmount().getAbsoluteError());
        System.out.println(result.getAmount().getEstimatedValue());

        result = createParser(UnitType.IMPERIAL_LENGTH).parse("4 miles");
        assertTrue(result.getAmount().isExact());
        Assert.assertEquals(1.622016E7,result.getAmount().getEstimatedValue(),result.getAmount().getAbsoluteError());
        System.out.println(result.getAmount().getEstimatedValue());

        result = createParser(UnitType.IMPERIAL_LENGTH).parse("4 mile");
        assertTrue(result.getAmount().isExact());
        assertEquals(1.622016E7, result.getAmount().getEstimatedValue(), result.getAmount().getAbsoluteError());
        System.out.println(result.getAmount().getEstimatedValue());

    }

    @Test
    public void test_different_standard_units() {
        String input = "100m 5cm";
        AmountWithString result = createParser(SI.MILLIMETER,UnitType.METRIC_LENGTH).parse(input);
        assertTrue(result.getAmount().isExact());
        Assert.assertEquals(100050.0, result.getAmount().getEstimatedValue(), result.getAmount().getAbsoluteError());

        result = createParser(SI.MILLIMETER,UnitType.IMPERIAL_LENGTH).parse(input);
        assertTrue(result.getAmount().isExact());
        Assert.assertEquals(100050.0, result.getAmount().getEstimatedValue(), result.getAmount().getAbsoluteError());

        result = createParser(SI.CENTIMETRE,UnitType.METRIC_LENGTH).parse(input);
        assertTrue(result.getAmount().isExact());
        Assert.assertEquals(10005.0, result.getAmount().getEstimatedValue(), result.getAmount().getAbsoluteError());

        result = createParser(NonSI.INCH,UnitType.METRIC_LENGTH).parse(input);
        assertFalse(result.getAmount().isExact());
        System.out.println(result.getAmount().getEstimatedValue());
        Assert.assertEquals(3938.9763779527557, result.getAmount().getEstimatedValue(), result.getAmount().getAbsoluteError());

    }

    @Test
    public void  test_different_default_units() {
        // giving different unit types will affect how parser assigns units to numbers.
        // i.e. if you enter "5" does it mean 5 ft or 5 inches.

        String input = "100 5";
        AmountWithString result = createParser(SI.MILLIMETER,UnitType.METRE_CM).parse(input);
        assertTrue(result.getAmount().isExact());
        Assert.assertEquals(100*1000+5*10, result.getAmount().getEstimatedValue(), result.getAmount().getAbsoluteError());

        // interprets as 100 km, 5 meters.
        result = createParser(SI.MILLIMETER,UnitType.METRIC_LENGTH).parse(input);
        assertTrue(result.getAmount().isExact());
        Assert.assertEquals(100*(1000*1000)+5*1000, result.getAmount().getEstimatedValue(), result.getAmount().getAbsoluteError());

        result = createParser(NonSI.INCH,UnitType.FEET_INCHES).parse(input);
        assertTrue(result.getAmount().isExact());
        Assert.assertEquals(12*100+5, result.getAmount().getEstimatedValue(), result.getAmount().getAbsoluteError());

        // interprets as miles, yards, feet,inches.
        result = createParser(NonSI.INCH,UnitType.IMPERIAL_LENGTH).parse(input);
        assertTrue(result.getAmount().isExact());
        Assert.assertEquals(63360*100+5*(12*3), result.getAmount().getEstimatedValue(), result.getAmount().getAbsoluteError());
    }

    @Test(expected = ConversionException.class)
    public void test_not_enough_units() {
        String input = "100 5 5";
        AmountWithString result = createParser(NonSI.INCH,UnitType.FEET_INCHES).parse(input);
    }

    @Test(expected = ConversionException.class)
    public void test_mismatched_units() {
        String input = "100ft 5hr";
        AmountWithString result = createParser(NonSI.INCH,UnitType.FEET_INCHES).parse(input);
    }


    private AmountParser createParser(UnitType unitType) {
        return new AmountParser(unitType) {
            @Override protected Unit<?> getStandardUnit() {
                return NonSI.INCH.divide(64);
            }
        };
    }

    private AmountParser createParser(final Unit<?> standardUnit, UnitType unitType ) {
        return new AmountParser(unitType) {
            @Override protected Unit<?> getStandardUnit() {
                return standardUnit;
            }
        };
    }

}
