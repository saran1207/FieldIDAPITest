package com.n4systems.util.persistence.search.terms;

import com.n4systems.model.EventSchedule;
import com.n4systems.model.utils.DateRange;
import com.n4systems.util.DateHelper;
import com.n4systems.util.persistence.WhereClause;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameterGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

// This is a special search term used  for reporting when the user wants to combine both complete and incomplete
// events in a certain time range. basically this results in sql like:
// (<event completed> AND <completed date between x and y>) OR (<event incomplete> and <scheduled date between x and y>)
// which will all be surrounded in parentheses and chained by AND with the rest of the search terms.
public class CompletedOrDueDateRange implements SearchTermDefiner {

    private DateRange dateRange;
    private TimeZone timeZone;

    public CompletedOrDueDateRange(TimeZone timeZone, DateRange dateRange) {
        this.timeZone = timeZone;
        this.dateRange = dateRange;
    }

    @Override
    public List<WhereClause<?>> getWhereParameters() {
        List<WhereClause<?>> params = new ArrayList<WhereClause<?>>();

        WhereParameterGroup outerGroup = new WhereParameterGroup();

        outerGroup.addClause(new CompleteDateRangeTerm(timeZone, dateRange));
        outerGroup.addClause(new DueDateRangeTerm(timeZone, dateRange));

        params.add(outerGroup);

        return params;
    }

    static class CompleteDateRangeTerm extends WhereParameterGroup {
        CompleteDateRangeTerm(TimeZone timeZone, DateRange dateRange) {
            setChainOperator(ChainOp.OR);
            addClause(WhereClauseFactory.create("status", EventSchedule.ScheduleStatus.COMPLETED, ChainOp.AND));

            DateRangeTerm rangeTerm = new DateRangeTerm("completedDate", DateHelper.convertToUTC(dateRange.calculateFromDate(), timeZone), DateHelper.convertToUTC(dateRange.calculateToDate(), timeZone));

            for (WhereClause<?> whereClause : rangeTerm.getWhereParameters()) {
                addClause(whereClause);
            }
        }
    }

    static class DueDateRangeTerm extends WhereParameterGroup {
        DueDateRangeTerm(TimeZone timeZone, DateRange dateRange) {
            setChainOperator(ChainOp.OR);
            addClause(WhereClauseFactory.create(WhereParameter.Comparator.NE, "status", EventSchedule.ScheduleStatus.COMPLETED, ChainOp.AND));

            DateRangeTerm rangeTerm = new DateRangeTerm("nextDate", DateHelper.convertToUTC(dateRange.calculateFromDate(), timeZone), DateHelper.convertToUTC(dateRange.calculateToDate(), timeZone));

            for (WhereClause<?> whereClause : rangeTerm.getWhereParameters()) {
                addClause(whereClause);
            }
        }
    }

}
