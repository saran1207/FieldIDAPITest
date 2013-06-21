package com.n4systems.services.brainforest;

import com.n4systems.model.utils.DateRange;
import com.n4systems.services.date.DateService;
import com.n4systems.util.chart.RangeType;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.TimeZone;

public class DateParser {

    private static final Logger logger=Logger.getLogger(DateParser.class);

    public enum FloatingDate {
        TODAY( "today"),
        TOMORROW( "tomorrow"),
        YESTERDAY("yesterday"),

//        MONDAY(DateTimeConstants.MONDAY,"monday","mon"),
//        TUESDAY(DateTimeConstants.TUESDAY,"tuesday","tue"),
//        WEDNESDAY(DateTimeConstants.WEDNESDAY,"wednesday","wed"),
//        THURSDAY(DateTimeConstants.THURSDAY,"thursday","thu"),
//        FRIDAY(DateTimeConstants.FRIDAY,"friday","fri"),
//        SATURDAY(DateTimeConstants.SATURDAY,"saturday","sat"),
//        SUNDAY(DateTimeConstants.SUNDAY,"sunday","sun"),
//
//        NEXT_MONDAY(DateTimeConstants.MONDAY,"monday","mon"),
//        NEXT_TUESDAY(DateTimeConstants.TUESDAY,"tuesday","tue"),
//        NEXT_WEDNESDAY(DateTimeConstants.WEDNESDAY,"wednesday","wed"),
//        NEXT_THURSDAY(DateTimeConstants.THURSDAY,"thursday","thu"),
//        NEXT_FRIDAY(DateTimeConstants.FRIDAY,"friday","fri"),
//        NEXT_SATURDAY(DateTimeConstants.SATURDAY,"saturday","sat"),
//        NEXT_SUNDAY(DateTimeConstants.SUNDAY,"sunday","sun"),
//
//        LAST_MONDAY(DateTimeConstants.MONDAY,"monday","mon"),
//        LAST_TUESDAY(DateTimeConstants.TUESDAY,"tuesday","tue"),
//        LAST_WEDNESDAY(DateTimeConstants.WEDNESDAY,"wednesday","wed"),
//        LAST_THURSDAY(DateTimeConstants.THURSDAY,"thursday","thu"),
//        LAST_FRIDAY(DateTimeConstants.FRIDAY,"friday","fri"),
//        LAST_SATURDAY(DateTimeConstants.SATURDAY,"saturday","sat"),
//        LAST_SUNDAY(DateTimeConstants.SUNDAY,"sunday","sun"),

        JANUARY(DateTimeConstants.JANUARY,"january","jan"),
        FEBRUARY(DateTimeConstants.FEBRUARY,"february","feb"),
        MARCH(DateTimeConstants.MARCH,"march","mar"),
        APRIL(DateTimeConstants.APRIL,"april","apr"),
        MAY(DateTimeConstants.MAY,"may","may"),
        JUNE(DateTimeConstants.JUNE,"june","jun"),
        JULY(DateTimeConstants.JULY,"july","jul"),
        AUGUST(DateTimeConstants.AUGUST,"august","aug"),
        SEPTEMBER(DateTimeConstants.SEPTEMBER,"september","sept"),
        OCTOBER(DateTimeConstants.OCTOBER,"october","oct"),
        NOVEMBER(DateTimeConstants.NOVEMBER,"november","nov"),
        DECEMBER(DateTimeConstants.DECEMBER,"december","dec"),

        THIS_WEEK("this week"),
        THIS_MONTH("this month"),
        THIS_QUARTER("this quarter"),
        THIS_YEAR("this year"),

        LAST_WEEK("last week"),
        LAST_MONTH("last month"),
        LAST_QUARTER("last quarter"),
        LAST_YEAR("last year"),

        NEXT_WEEK("next week"),
        NEXT_MONTH("next month"),
        NEXT_QUARTER("next quarter"),
        NEXT_YEAR("next year");

        private String[] alias;
        private Integer constant;

        FloatingDate(String... alias) {
            this.alias = alias;
            constant = null;
        }

        FloatingDate(int constant, String...alias) {
            this.constant = constant;
            this.alias = alias;
        }

        public String[] getAliases() {
            return alias;
        }
    }



    // TODO : make sure all dates have timeZone applied!!!


    private @Autowired DateService dateService = new DateService();

    public DateRange parseRange(String stringValue, SimpleValue.DateFormatType type) throws java.text.ParseException {
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
        stringValue = stringValue.toLowerCase();
        for (FloatingDate floatingDate: FloatingDate.values()) {
            for (String alias:floatingDate.getAliases()) {
                if (alias.equals(stringValue)) {
                    return parseFloatingDate(floatingDate);
                }
            }
        }
        throw new IllegalStateException("can't parse floating date [" + stringValue + "]");
    }

    private DateRange parseFloatingDate(FloatingDate floatingDate) {
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
//            case MONDAY:
//            case TUESDAY:
//            case WEDNESDAY:
//            case THURSDAY:
//            case FRIDAY:
//            case SATURDAY:
//            case SUNDAY:
//                LocalDate day = now().withDayOfWeek(floatingDate.constant);
//                return new DateRange(day, day.plusDays(7));
//            case LAST_MONDAY:
//            case LAST_TUESDAY:
//            case LAST_WEDNESDAY:
//            case LAST_THURSDAY:
//            case LAST_FRIDAY:
//            case LAST_SATURDAY:
//            case LAST_SUNDAY:
//                day = now().withDayOfWeek(floatingDate.constant).minusWeeks(1);
//                return new DateRange(day, day.plusDays(7));
//            case NEXT_MONDAY:
//            case NEXT_TUESDAY:
//            case NEXT_WEDNESDAY:
//            case NEXT_THURSDAY:
//            case NEXT_FRIDAY:
//            case NEXT_SATURDAY:
//            case NEXT_SUNDAY:
//                day = now().withDayOfWeek(floatingDate.constant).plusWeeks(1);
//                return new DateRange(day, day.plusDays(7));
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
            case THIS_WEEK:
                return new DateRange(monday, monday.plusWeeks(1));
            case NEXT_WEEK:
                return new DateRange(monday.plusWeeks(1), monday.plusWeeks(2));
            case LAST_WEEK:
                return new DateRange(monday.minusWeeks(1), monday);

            case THIS_MONTH:
                return new DateRange(firstOfMonth, firstOfMonth.plusMonths(1));
            case LAST_MONTH:
                return new DateRange(firstOfMonth.minusMonths(1), firstOfMonth);
            case NEXT_MONTH:
                return new DateRange(firstOfMonth.plusMonths(1), firstOfMonth.plusMonths(2));

            case THIS_QUARTER:
                return new DateRange(firstOfQuarter, firstOfQuarter.plusMonths(3));
            case LAST_QUARTER:
                return new DateRange(firstOfQuarter.minusMonths(3), firstOfQuarter);
            case NEXT_QUARTER:
                return new DateRange(firstOfQuarter.plusMonths(3), firstOfQuarter.plusMonths(6));

            case THIS_YEAR:
                return new DateRange(jan1,jan1.plusYears(1));
            case LAST_YEAR:
                return new DateRange(jan1.minusYears(1), jan1);
            case NEXT_YEAR:
                return new DateRange(jan1.plusYears(1), jan1.plusYears(2));
            default:
                throw new IllegalStateException("can't parse floating date type " + floatingDate);
        }
    }

    private LocalDate now() {
        return LocalDate.now();
    }
}
