package com.n4systems.services.date;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.utils.DateRange;
import com.n4systems.util.DateHelper;
import com.n4systems.util.DateTimeDefinition;
import com.n4systems.util.chart.RangeType;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

import java.util.Date;
import java.util.TimeZone;

// NOTE : this is a timezone sensitive service.
// all dates with be calculated with the users timezone in mind.
public class DateService extends FieldIdPersistenceService {

    public Date calculateFromDate(DateRange dateRange) {
        return calculateFromDateWithTimeZone(dateRange, getUserTimeZone());
    }

    public Date calculateToDate(DateRange dateRange) {
        return calculateToDateWithTimeZone(dateRange, getUserTimeZone());
    }

    public Date calculateFromDateWithTimeZone(DateRange dateRange, TimeZone timeZone) {
        LocalDate from = dateRange.withTimeZone(timeZone).getFrom();
        return from==null ? null : from.toDate();
    }

    public Date calculateToDateWithTimeZone(DateRange dateRange, TimeZone timeZone) {
        LocalDate to = dateRange.withTimeZone(timeZone).getTo();
        return to==null ? null : to.toDate();
    }

    // for example, in reporting you might specify a custom range of jan4-jan12.   what this really means is
    //   where date4=jan1 and date<jan13  (INCLUDE all jan 12).
    // .: we adjust the "to" date ahead by 1.
    //  for other range types like TODAY, LAST_WEEK etc... this is already handled.
    public Date calculateInclusiveToDateWithTimeZone(DateRange dateRange, TimeZone timeZone) {
        return calculateInclusiveToDate(dateRange.withTimeZone(timeZone));
    }

    public Date calculateInclusiveToDate(DateRange dateRange) {
        LocalDate to = dateRange.getTo();
        if (dateRange.getRangeType().isCustom() && to!=null) {
            to = to.plusDays(1);
        }
        return to==null ? null : to.toDate();
    }

    public DateRange getDateRange(RangeType rangeType) {
        return new DateRange(rangeType, getUserTimeZone());
    }

    public LocalDate today() {
        return new LocalDate(new DateMidnight(DateTimeZone.forTimeZone(getUserTimeZone())));
    }

    public Date todayAsDate() {
        return new LocalDate(new DateMidnight(DateTimeZone.forTimeZone(getUserTimeZone()))).toDate();
    }

    public LocalDate todayUTC() {
        return new LocalDate(new DateMidnight(DateTimeZone.UTC));
    }

    public TimeZone getUsersTimeZone() {
        return super.getUserTimeZone();
    }

    public DateTimeDefinition getDateTimeDefinition() {
        return getCurrentUser();
    }

    public DateTime nowUTC() {
        return new DateTime(DateTimeZone.UTC);
    }

    public DateTime now() {
        return new DateTime(DateTimeZone.forTimeZone(getUserTimeZone()));
    }

    public Long getDaysFromToday(Date date) {
        return DateHelper.millisToDays(date.getTime() - todayAsDate().getTime());
    }

    public boolean isPastDue(Date dueDate) {
        return todayAsDate().after(dueDate);
    }
}
