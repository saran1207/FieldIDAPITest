package com.n4systems.services.reporting;

import java.util.Date;
import org.joda.time.LocalDate;
import com.n4systems.util.chart.DateChartable;

@SuppressWarnings("serial")
public class UpcomingScheduledProcedureAuditsRecord extends DateChartable {

    public UpcomingScheduledProcedureAuditsRecord(Date date, Long value) {
        super(date, value);
    }

    public UpcomingScheduledProcedureAuditsRecord(LocalDate date, Long value) {
        super(date, value);
    }

}
