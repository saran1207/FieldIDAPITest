package com.n4systems.services.brainforest;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.n4systems.model.utils.DateRange;
import com.n4systems.services.date.DateService;
import com.n4systems.util.StringUtils;
import com.n4systems.util.chart.RangeType;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.StringTokenizer;
import java.util.TimeZone;

public class DateParser {

    private static final Logger logger=Logger.getLogger(DateParser.class);

    static Period day = Period.days(1);
    static Period week = Period.weeks(1);
    static Period month = Period.months(1);
    static Period year = Period.years(1);
    static Period quarter = Period.months(3);

    enum FloatingDateModifier {
        THIS(0),NEXT(1),LAST(-1);

        private final int offset;

        FloatingDateModifier(int offset) {
            this.offset = offset;
        }

        public int getOffset() {
            return offset;
        }

        public static FloatingDateModifier fromString(String text) {
            String s = text.trim();
            for (FloatingDateModifier modifier:values()) {
                if (modifier.name().equalsIgnoreCase(s)) {
                    return modifier;
                }
            }
            return null;
        }
    }

    enum FloatingDateSpec {
        TODAY(day, "today"),
        TOMORROW(day, "tomorrow"),
        YESTERDAY(day, "yesterday"),

        WEEK(week,"week"),
        MONTH(month,"month"),
        QUARTER(quarter,"quarter"),
        YEAR(year, "year"),

        MONDAY(week, DateTimeConstants.MONDAY,"monday","mon"),
        TUESDAY(week, DateTimeConstants.TUESDAY,"tuesday","tue"),
        WEDNESDAY(week, DateTimeConstants.WEDNESDAY,"wednesday","wed"),
        THURSDAY(week, DateTimeConstants.THURSDAY,"thursday","thu"),
        FRIDAY(week, DateTimeConstants.FRIDAY,"friday","fri"),
        SATURDAY(week, DateTimeConstants.SATURDAY,"saturday","sat"),
        SUNDAY(week, DateTimeConstants.SUNDAY,"sunday","sun"),

        JANUARY(year, DateTimeConstants.JANUARY,"january","jan"),
        FEBRUARY(year, DateTimeConstants.FEBRUARY,"february","feb"),
        MARCH(year, DateTimeConstants.MARCH,"march","mar"),
        APRIL(year, DateTimeConstants.APRIL,"april","apr"),
        MAY(year, DateTimeConstants.MAY,"may","may"),
        JUNE(year, DateTimeConstants.JUNE,"june","jun"),
        JULY(year, DateTimeConstants.JULY,"july","jul"),
        AUGUST(year, DateTimeConstants.AUGUST,"august","aug"),
        SEPTEMBER(year, DateTimeConstants.SEPTEMBER,"september","sept"),
        OCTOBER(year, DateTimeConstants.OCTOBER,"october","oct"),
        NOVEMBER(year, DateTimeConstants.NOVEMBER,"november","nov"),
        DECEMBER(year, DateTimeConstants.DECEMBER,"december","dec");

        private List<String> alias;
        private int constant;
        private Period period;

        FloatingDateSpec(Period period, int constant,String... alias) {
            this.alias = Lists.newArrayList(alias);
            this.constant  = constant;
            this.period = period;
        }

        FloatingDateSpec(Period period, String... alias) {
            this.alias = Lists.newArrayList(alias);
            this.period = period;
        }

        public List<String> getAlias() {
            return alias;
        }

        public int getConstant() {
            return constant;
        }

        public Period getPeriod() {
            return period;
        }

        public static FloatingDateSpec fromString(String text) {
            String s = text.toLowerCase().trim();
            for (FloatingDateSpec spec:values()) {
                if (spec.getAlias().contains(s)) {
                    return spec;
                }
            }
            return null;
        }
    }


    // TODO : make sure all dates have timeZone applied!!!


    private @Autowired DateService dateService = new DateService();

    public DateRange parseRange(String stringValue, SimpleValue.DateFormatType type) throws java.text.ParseException {
        Preconditions.checkArgument(!StringUtils.isEmpty(stringValue), "can't parse date from empty string");

        TimeZone timeZone = getDateService().getUserTimeZone();
        switch (type) {
            case VOID:
            case TODAY:
                return new DateRange(RangeType.TODAY, timeZone);
            case YESTERDAY:
                return new DateRange(RangeType.YESTERDAY, timeZone);
            case TOMORROW:
                return new DateRange(RangeType.TOMORROW, timeZone);
            case DASH_DATE:
            case SLASH_DATE:
                LocalDate from = new LocalDate(DateUtils.parseDate(stringValue, type.getPatterns()));
                return new DateRange(from, from.plusDays(1));
            case VERBOSE_DATE:
                LocalDate to;
                from = to = new LocalDate(DateUtils.parseDate(stringValue, type.getPatterns()));

                if (from.getYear()<2005) {
                    from = from.withYear(DateTime.now().getYear());
                }
                if (from.getDayOfMonth()==1 && stringValue.indexOf("1")==-1) {
                    to = from.plusMonths(1);
                }
                return new DateRange(from,to.plusDays(1));
            case FLOATING_DATE:
                return parseFloatingDate(stringValue);
            default:
                throw new IllegalStateException("can't parse date with invalid type " + type);
        }

    }

    protected DateService getDateService() {
        return dateService;
    }

    private DateRange parseFloatingDate(String stringValue) {
        StringTokenizer tokenizer = new StringTokenizer(stringValue);
        String token = tokenizer.nextToken();
        FloatingDateModifier modifier = FloatingDateModifier.fromString(token);
        if (modifier==null) {
            modifier=FloatingDateModifier.THIS;
        } else {
            token = tokenizer.nextToken();
        }
        FloatingDateSpec spec = FloatingDateSpec.fromString(token);

        DateRange dateRange = parseFloatingDate(spec);
        return modifyRange(dateRange, spec, modifier);
    }

    private DateRange modifyRange(DateRange dateRange, FloatingDateSpec spec, FloatingDateModifier modifier) {
        LocalDate from = dateRange.getFrom();
        LocalDate to = dateRange.getTo();

        switch (modifier) {
            case THIS:
                break;
            case NEXT:
                from = from.plus(spec.getPeriod());
                to = to.plus(spec.getPeriod());
                break;
            case LAST:
                from = from.minus(spec.getPeriod());
                to = to.minus(spec.getPeriod());
                break;
            default:
                throw new IllegalArgumentException("illegal modifier for date " + modifier);
        }
        return new DateRange(from,to);
    }


    private DateRange parseFloatingDate(FloatingDateSpec floatingDate) {
        LocalDate monday = now().withDayOfWeek(DateTimeConstants.MONDAY);
        LocalDate firstOfMonth = now().withDayOfMonth(1);
        LocalDate firstOfQuarter = now().minusMonths((now().getMonthOfYear()-1)%3).withDayOfMonth(1);
        LocalDate jan1 = now().withMonthOfYear(DateTimeConstants.JANUARY).withDayOfMonth(1);

        switch (floatingDate) {
            case TODAY:
                return new DateRange(now(),now().plusDays(1));
            case TOMORROW:
                return new DateRange(now().plusDays(1),now().plusDays(2));
            case YESTERDAY:
                return new DateRange(now().minusDays(1),now());

            case JANUARY:
            case FEBRUARY:
            case MARCH:
            case APRIL:
            case MAY:
            case JUNE:
            case JULY:
            case AUGUST:
            case SEPTEMBER:
            case OCTOBER:
            case NOVEMBER:
            case DECEMBER:
                LocalDate month = now().withDayOfMonth(1).withMonthOfYear(floatingDate.constant);
                return new DateRange(month, month.plusMonths(1));

            case MONDAY:
            case TUESDAY:
            case WEDNESDAY:
            case THURSDAY:
            case FRIDAY:
            case SATURDAY:
            case SUNDAY:
                LocalDate day = LocalDate.now().withDayOfWeek(floatingDate.constant);
                return new DateRange(day, day.plusDays(1));

            case WEEK:
                return new DateRange(monday, monday.plusWeeks(1));

            case MONTH:
                return new DateRange(firstOfMonth, firstOfMonth.plusMonths(1));

            case QUARTER:
                return new DateRange(firstOfQuarter, firstOfQuarter.plusMonths(3));

            case YEAR:
                return new DateRange(jan1,jan1.plusYears(1));
            default:
                throw new IllegalStateException("can't parse floating date type " + floatingDate);
        }
    }

    private LocalDate now() {
        return LocalDate.now();
    }
}
