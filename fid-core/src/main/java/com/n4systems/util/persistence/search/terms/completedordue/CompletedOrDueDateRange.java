package com.n4systems.util.persistence.search.terms.completedordue;

import com.n4systems.model.utils.DateRange;
import com.n4systems.util.DateHelper;
import com.n4systems.util.persistence.WhereClause;
import com.n4systems.util.persistence.WhereParameterGroup;
import com.n4systems.util.persistence.search.terms.DateRangeTerm;
import org.joda.time.LocalDate;

import java.util.Date;
import java.util.TimeZone;

// This is a special search term used  for reporting when the user wants to combine both complete and incomplete
// events in a certain time range. basically this results in sql like:
// (<event completed> AND <completed date between x and y>) OR (<event incomplete> and <scheduled date between x and y>)
// which will all be surrounded in parentheses and chained by AND with the rest of the search terms.
public class CompletedOrDueDateRange extends CompleteOrIncompleteTerm {

    private DateRange dateRange;
    private TimeZone timeZone;

    public CompletedOrDueDateRange(TimeZone timeZone, DateRange dateRange) {
        this.timeZone = timeZone;
        this.dateRange = dateRange.withTimeZone(timeZone);
    }

    @Override
    protected void populateIncompleteTerm(WhereParameterGroup completedGroup) {
        DateRangeTerm rangeTerm = new DateRangeTerm("nextDate", dateRange.calculateFromDate(), getToDate());

        for (WhereClause<?> whereClause : rangeTerm.getWhereParameters()) {
            completedGroup.addClause(whereClause);
        }
    }

    @Override
    protected void populateCompletedTerm(WhereParameterGroup incompleteGroup) {
        DateRangeTerm rangeTerm = new DateRangeTerm("completedDate", DateHelper.convertToUTC(dateRange.calculateFromDate(), timeZone), DateHelper.convertToUTC(getToDate(), timeZone));

        for (WhereClause<?> whereClause : rangeTerm.getWhereParameters()) {
            incompleteGroup.addClause(whereClause);
        }
    }


    // TODO : should put the isCustom() logic in the actual dateRange object itself.
    private Date getToDate() {
        LocalDate to = dateRange.getTo();
        if (dateRange.getRangeType().isCustom() && to!=null) {
            to = to.plusDays(1);
        }
        return to.toDate();
    }

}
