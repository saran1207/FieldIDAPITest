package com.n4systems.fieldid.wicket.util;

import com.google.common.collect.Sets;
import com.n4systems.model.UnitType;
import com.n4systems.model.common.AmountWithString;
import com.n4systems.util.StringUtils;
import org.jscience.mathematics.number.Rational;
import org.jscience.physics.amount.Amount;

import javax.measure.converter.ConversionException;
import javax.measure.converter.UnitConverter;
import javax.measure.quantity.Quantity;
import javax.measure.unit.CompoundUnit;
import javax.measure.unit.Dimension;
import javax.measure.unit.Unit;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AmountParser<Q extends Quantity, T extends Unit<Q>> {

    // examples.   3, 3 1/4, 3-4/5, 5.6754,
    private static final String AMOUNT_REGEX =
            "(\\d+)" +
                    "(" +
                        "(\\s*/\\s*(\\d+))" +
                        "|(\\.\\d+)" +
                        "|(\\s*(-\\s*)?(\\d+)\\s*/\\s*(\\d+))" +
                    ")?" +
             "\\s*([a-z]+|'|\")?";

    private static final Pattern AMOUNT_PATTERN = Pattern.compile(AMOUNT_REGEX, Pattern.CASE_INSENSITIVE);

    private Dimension dimension;
    private UnitType unitType;
    private Unit unit;

    private Unit lastUnit;
    private Set<Unit<?>> units = Sets.newTreeSet(new UnitComparator());
    private Iterator<Unit<?>> defaultUnitIterator;
    private Locale locale;

    public AmountParser(UnitType unitType) {
        this.unitType = unitType;
        this.unit = unitType.getUnit();
        this.dimension = unitType.getUnit().getDimension();
        addDefaultUnit(unitType.getUnit());
        defaultUnitIterator = units.iterator();
    }

    private <U extends Unit<?>> void addDefaultUnit(U unit) {
        // e.g. for compound unit like yards/feet/inches, will have map in that sorted order.
        if (unit instanceof CompoundUnit) {
            addDefaultUnit(((CompoundUnit) unit).getHigher());
            addDefaultUnit(((CompoundUnit) unit).getLower());
        } else {
            units.add(unit);
        }
    }

    public AmountWithString<Q> parse(String text) {
        double total = 0.0;
        AmountTokenizer amountTokenizer = new AmountTokenizer(text);
        while (amountTokenizer.hasNext()) {
            // maybe i should just add doubles my self?  keep total with ints and doubles?
            Double amount = amountTokenizer.next();
            total = total + amount;
        }

        // i want to preserve as much accuracy as possible. don't run thru converters and switch to decimal if not needed because that's when problems start.
        Amount<Q> amount;
        if (total!=Math.rint(total)) {
            amount = (Amount<Q>) Amount.valueOf(total, getStandardUnit());
        } else {
            amount = (Amount<Q>) Amount.valueOf((long)total, getStandardUnit());
        }
        return new AmountWithString<Q>(text, amount);
    }

    protected Unit<?> getStandardUnit() {
        return unitType.getFieldIdStandardUnit();
    }

    private Unit getDefaultUnit() {
        if (lastUnit==null) {
            if (!defaultUnitIterator.hasNext()) {
                return null;
            }
            return defaultUnitIterator.next();
        } else {
            boolean found = false;
            for (Unit unit:units) {
                if (found) {
                    return unit;
                }
                if (unit.equals(lastUnit)) {
                    found = true;//set flag, return the one after this one.
                }
            }
            return null;
        }
    }

    public AmountParser withLocale(Locale locale) {
        // TODO : this might affect things like meaning of $ or time or punctuaction (semi-colons, commas, slashes, etc...)
        this.locale = locale;
        return this;
    }

    class AmountTokenizer implements Iterator<Double> {
        private static final int INTEGRAL_GROUP = 1;
        private static final int DENOMINATOR_GROUP = 4;
        private static final int FRACTIONAL_GROUP = 5;
        private static final int NUMERATOR_GROUP = 8;
        private static final int DENOMINATOR1_GROUP = 9;
        private static final int UNIT_GROUP = 10;

        private Matcher matcher;
        private final String text;

        public AmountTokenizer(String text) {
            this.text = text;
            this.matcher = AMOUNT_PATTERN.matcher(text);
        }

        private double getAmount(Matcher matcher) throws ConversionException {
            String integral = matcher.group(INTEGRAL_GROUP);
            String denominator = matcher.group(DENOMINATOR_GROUP);
            String fractional = matcher.group(FRACTIONAL_GROUP);
            String numerator = matcher.group(NUMERATOR_GROUP);
            String denominator1 = matcher.group(DENOMINATOR1_GROUP);
            Unit unit = getUnit(matcher.group(UNIT_GROUP),locale);

            UnitConverter converterTo = unit.getConverterTo(getStandardUnit());  // NOTE : if units mismatched this will throw exception   e.g. 4 Watts 3 Meters.

            if (denominator!=null)  { // e.g. 3/4
                return converterTo.convert(Rational.valueOf(Long.parseLong(integral), Long.parseLong(denominator)).doubleValue());
            } else if (fractional!=null) {  // e.g.  3.14
                return converterTo.convert(new BigDecimal(integral + fractional).doubleValue());
            } else if (numerator!=null&&denominator1!=null) {   // e.g.   4 1/2
                System.out.println(converterTo.convert(new BigDecimal(integral).add(new BigDecimal(numerator).divide(new BigDecimal(denominator1),20,BigDecimal.ROUND_DOWN)).doubleValue()));
                return converterTo.convert(new BigDecimal(integral).add(new BigDecimal(numerator).divide(new BigDecimal(denominator1), 20, BigDecimal.ROUND_DOWN)).doubleValue());
            } else if (integral!=null) {   // eg. 786
                return converterTo.convert(Long.parseLong(integral));
            }
            throw new IllegalArgumentException("can't calculate numeric value from " + text);
        }

        private Unit getUnit(String unitString, Locale locale) {
            if (StringUtils.isEmpty(unitString)) {
                lastUnit = getDefaultUnit();
            } else {
                lastUnit = UnitType.getUnitFor(unitString,dimension,locale);
            }
            if (lastUnit==null) {
                throw new ConversionException("can't find unit " + unitString + ". Either it doesn't match expected dimension '"+dimension.getClass().getSimpleName()+"' (e.g. using Celcius for length) or can't find alias");
            }
            return lastUnit;
        }

        @Override
        public boolean hasNext() {
            return (matcher.find());
        }

        @Override
        public Double next() {
            return getAmount(matcher);
        }

        @Override
        public void remove() {
            ;
        }
    }

    class UnitComparator implements Comparator<Unit<?>> {
        @Override public int compare(Unit<?> a, Unit<?> b) {
            double a1 = a.getConverterTo(a.getStandardUnit()).convert(1.0);
            double b1 = b.getConverterTo(a.getStandardUnit()).convert(1.0);
            // note : i negate because i want biggest first.
            return -(a1<b1 ? -1 :
                    a1==b1 ? 0 : +1);
        }
    }

}
