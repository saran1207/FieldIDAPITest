package com.n4systems.model;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.jscience.economics.money.Currency;

import javax.measure.unit.*;
import java.util.ArrayList;
import java.util.Locale;

public enum UnitType {

    FEET(NonSI.FOOT, new String[]{"feet", "foot", "ft","'"}),    // recall : single quote for feet...double for inches.
    INCH(NonSI.INCH, new String[]{"inches", "inch", "in","\""}),
    FEET_INCHES(NonSI.FOOT.compound(NonSI.INCH)),
    METRE_CM(SI.METRE.compound(SI.CENTIMETRE)),
    METRE(SI.METRE, new String[]{"meter","metre","metres","meters","m"}),
    KILOMETRE(SI.KILOMETRE,new String[]{"km","kilometer","kilomtetre"}),
    MILLIMETRE(SI.MILLIMETER,new String[]{"mm","millimeter","millimetre"}),
    CENTIMETRE(SI.CENTIMETER,new String[]{"cm","centimeter","centimetre"}),
    MILE(NonSI.MILE,new String[]{"mile","miles"}),
    HOUR(NonSI.HOUR, new String[]{"hour","hr"}),
    SECOND(SI.SECOND, new String[]{"sec","second","s"}),
    DAY(NonSI.DAY, new String[]{"day","days"}),
    HOURS_MINUTES(NonSI.HOUR.compound(NonSI.MINUTE)),
    USD(Currency.USD, new String[]{"$","USD"},Locale.US),
    CAD(Currency.CAD, new String[]{"$","CAD"},Locale.CANADA),
    EURO(Currency.EUR, new String[]{"EUR"}),
    HOURS_MINUTES_SECONDS(NonSI.HOUR.compound(NonSI.MINUTE).compound(SI.SECOND)),
    METRIC_LENGTH(SI.KILOMETRE.compound(SI.METER).compound(SI.CENTIMETRE).compound(SI.MILLIMETRE)),
    IMPERIAL_LENGTH(NonSI.MILE.compound(NonSI.YARD).compound(NonSI.FOOT).compound(NonSI.INCH));
    // add more as needed...

    private final Unit<?> unit;
    private final ArrayList<String> aliases;
    private final Locale locale;


    UnitType(Unit<?> unit, String[] aliases) {
        this(unit,aliases,null);
        Preconditions.checkArgument(!(unit instanceof CompoundUnit),"use other constructor for compound units; aliases not allowed for compound units.");
    }

    UnitType(CompoundUnit<?> unit) {
        this(unit,new String[]{},null);
    }

    UnitType(Unit unit, String[] aliases, Locale locale) {
        this.unit = unit;
        this.aliases = Lists.newArrayList(aliases);
        this.locale = locale;
    }

    public Unit<?> getUnit() {
        return unit;
    }

    public static Unit getUnitFor(String s, Dimension dimension, Locale locale) {
        // TODO : handle compound units
        for (UnitType unitType:values()) {
            if (unitType.getAliases().contains(s)) { // TODO : check for locale equality.
                Unit unit = unitType.getUnit();
                if (unit.getDimension().equals(dimension) && unitType.requiresLocale(locale)) {
                    return unit;
                }
            }
        }
        return null;
    }

    public boolean requiresLocale(Locale locale) {
        // note : i only do matching if unit type actually cares about locale.
        return this.locale==null || this.locale.equals(locale);
    }

    public ArrayList<String> getAliases() {
        return aliases;
    }

    public Locale getLocale() {
        return locale;
    }

    // workaround. this exists because we want a different standard unit.
    // i.e. i want to use MM when storing distances, not meters.   overriding the getStandardUnit method would have been preferred way.
    // in the future it might be desired to have this customizable on a tenant by tenant basis.
    public Unit<?> getFieldIdStandardUnit() {
        if (unit.getStandardUnit().equals(SI.METER)) {
            return NonSI.INCH.divide(64);
        }
        return unit.getStandardUnit();
    }
}
