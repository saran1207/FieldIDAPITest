package com.n4systems.services.date;

import com.n4systems.fieldid.service.FieldIdService;
import com.n4systems.model.utils.DateRange;
import com.n4systems.util.chart.RangeType;
import org.apache.commons.lang.time.DateUtils;
import org.joda.time.LocalDate;

import java.util.Date;
import java.util.TimeZone;

// NOTE : this is a timezone sensitive service.
// all dates with be calculated with the users timezone in mind.
public class DateService extends FieldIdService {


    public Date calculateFromDate(DateRange dateRange) {
        return calculateFromDateWithTimeZone(dateRange, getUserTimeZone());
    }

    public Date calculateToDate(DateRange dateRange) {
        return calculateToDateWithTimeZone(dateRange, getUserTimeZone());
    }

    public Date calculateAfterToDate(DateRange dateRange) {
        Date date = calculateToDate(dateRange);
        return DateUtils.addDays(date,1);
    }

    public Date calculateFromDateWithTimeZone(DateRange dateRange, TimeZone timeZone) {
        LocalDate from = dateRange.withTimeZone(timeZone).getFrom();
        return from==null ? null : from.toDate();
    }

    public Date calculateToDateWithTimeZone(DateRange dateRange, TimeZone timeZone) {
        LocalDate to = dateRange.withTimeZone(timeZone).getTo();
        return to==null ? null : to.toDate();
    }

    // for example, in reporting you might specify a custome range of jan4-jan12.   what this really means is
    //   where date4=jan1 and date<jan13  (include jan 12).   .: we adjust the "to" date ahead by 1.
    //  for other range types like TODAY, LAST_WEEK etc... this is already handled.
    public Date calculateInclusiveToDateWithTimeZone(DateRange dateRange, TimeZone timeZone) {
        LocalDate to = dateRange.withTimeZone(timeZone).getTo();
        if (dateRange.getRangeType().isCustom() && to!=null) {
            to = to.plusDays(1);
        }
        return to==null ? null : to.toDate();
    }

    public DateRange getDateRange(RangeType rangeType) {
        return new DateRange(rangeType, getUserTimeZone());
    }
}
