package com.n4systems.util.persistence.search.terms;

import com.n4systems.model.EventSchedule;
import com.n4systems.model.utils.DateRange;
import com.n4systems.util.persistence.WhereClause;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameterGroup;

import java.util.ArrayList;
import java.util.List;

public class CompletedOrDueDateRange implements SearchTermDefiner {

    private DateRange dateRange;

    public CompletedOrDueDateRange(DateRange dateRange) {
        this.dateRange = dateRange;
    }

    @Override
    public List<WhereClause<?>> getWhereParameters() {
        List<WhereClause<?>> params = new ArrayList<WhereClause<?>>();

        WhereParameterGroup outerGroup = new WhereParameterGroup();

        outerGroup.addClause(new CompleteDateRangeTerm(dateRange));
        outerGroup.addClause(new DueDateRangeTerm(dateRange));

        params.add(outerGroup);

        return params;
    }

    static class CompleteDateRangeTerm extends WhereParameterGroup {
        CompleteDateRangeTerm(DateRange dateRange) {
            setChainOperator(ChainOp.OR);
            addClause(WhereClauseFactory.create("status", EventSchedule.ScheduleStatus.COMPLETED, ChainOp.AND));

            DateRangeTerm rangeTerm = new DateRangeTerm("completedDate", dateRange.calculateFromDate(), dateRange.calculateToDate());

            for (WhereClause<?> whereClause : rangeTerm.getWhereParameters()) {
                addClause(whereClause);
            }
        }
    }

    static class DueDateRangeTerm extends WhereParameterGroup {
        DueDateRangeTerm(DateRange dateRange) {
            setChainOperator(ChainOp.OR);
            addClause(WhereClauseFactory.create(WhereParameter.Comparator.NE, "status", EventSchedule.ScheduleStatus.COMPLETED, ChainOp.AND));

            DateRangeTerm rangeTerm = new DateRangeTerm("nextDate", dateRange.calculateFromDate(), dateRange.calculateToDate());

            for (WhereClause<?> whereClause : rangeTerm.getWhereParameters()) {
                addClause(whereClause);
            }
        }
    }

}
