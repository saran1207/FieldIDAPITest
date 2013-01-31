package com.n4systems.model;

import org.jscience.physics.amount.Amount;
import org.junit.Test;

import javax.measure.Measure;
import javax.measure.converter.UnitConverter;
import javax.measure.quantity.Quantity;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import javax.measure.unit.UnitFormat;
import java.text.ParseException;

public class UnitTest {

    @Test
    public void test_it() throws ParseException {
        UnitConverter converter = UnitType.FEET.getUnit().getConverterTo(SI.METRE);
        double convert = converter.convert(1);
        System.out.println(convert);

        converter = UnitType.INCH.getUnit().getConverterTo(NonSI.FOOT);
        convert = converter.convert(4);
        System.out.println(convert);

        converter = UnitType.FEET.getUnit().getConverterTo(NonSI.INCH);
        convert = converter.convert(1);
        System.out.println(convert);

        converter = UnitType.FEET_INCHES.getUnit().getConverterTo(SI.METRE);
        convert = converter.convert(1.5);
        System.out.println(convert);

        converter = UnitType.FEET_INCHES.getUnit().getConverterTo(SI.METRE);
        convert = converter.convert(1);
        System.out.println(convert);

        Measure<Integer, ? extends Quantity> measure = Measure.valueOf(100, UnitType.FEET_INCHES.getUnit());
        measure.toString();
        System.out.println(measure.toString());

        Amount<?> amount = Amount.valueOf("1 ft");
        converter = amount.getUnit().getConverterTo(NonSI.INCH);
        convert = converter.convert(amount.getExactValue());
        System.out.println(convert);

        UnitFormat.getInstance().alias(UnitType.FEET.getUnit(),"feets");

        amount = Amount.valueOf("4.777777777777777777 feets");
        converter = amount.getUnit().getConverterTo(SI.METRE);
        convert = converter.convert(amount.getEstimatedValue());
        System.out.println(convert);
        converter = SI.METRE.getConverterTo(NonSI.FOOT);
        convert = converter.convert(convert);
        System.out.println(convert);

        amount = Amount.valueOf("11.25 m");
        converter = amount.getUnit().getConverterTo(SI.METRE);
        convert = converter.convert(amount.getEstimatedValue());
        System.out.println(convert);


        Double smallestIncrement = Double.longBitsToDouble(0x1L);
        int count=0;
//        for (double i=1115.0;i<1115.0+2.0;i+=smallestIncrement) {
//            double metres = Amount.valueOf(i, SI.METER).getEstimatedValue();
//            String metreString = metres+"";
//            double inches = SI.METER.getConverterTo(NonSI.INCH).convert(metres);
//            String inchesString = inches+"";
//            double translatedMetres = NonSI.INCH.getConverterTo(SI.METER).convert(inches);
//            String tranlsatedMetreString = translatedMetres+"";
//            assertEquals(metreString, tranlsatedMetreString);
//            count++;
//            if (count%10000==0) System.out.print(".");
//        }
    }

}
