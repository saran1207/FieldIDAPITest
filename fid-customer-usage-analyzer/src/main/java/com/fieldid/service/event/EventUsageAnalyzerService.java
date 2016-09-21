package com.fieldid.service.event;

import com.fieldid.model.CountByTenant;
import com.fieldid.service.base.AbstractJDBCAnalyzerService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

/**
 *
 *
 * Created by Jordan Heath on 2016-09-20.
 */
@Service
public class EventUsageAnalyzerService extends AbstractJDBCAnalyzerService {
    private static final Logger logger = Logger.getLogger(EventUsageAnalyzerService.class);

    private static final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

    private static final String EVENT_COUNT_BY_TENANT_SQL =
            "SELECT count(e.id) as the_count, t.name as tenant_name " +
            "FROM events e " +
            "INNER JOIN masterevents me " +
            "ON e.id = me.id " +
            "INNER JOIN tenants t " +
            "ON e.tenant_id = t.id " +
            "WHERE t.disabled = 0 " +
                "AND me.workflow_state <> 'OPEN' " +
            "GROUP BY t.name;";

    private static final String EVENT_COUNT_BY_TENANT_LAST_MONTH_SQL =
            "SELECT count(e.id) as the_count, t.name as tenant_name " +
            "FROM events e " +
            "INNER JOIN masterevents me " +
            "ON e.id = me.id " +
            "INNER JOIN tenants t " +
            "ON e.tenant_id = t.id " +
            "WHERE t.disabled = 0 " +
                "AND me.workflow_state <> 'OPEN' " +
                "AND me.completedDate >= ? " +
            "GROUP BY t.name;";

    private static final String EVENT_COUNT_BY_TENANT_BY_MONTH_SQL =
            "SELECT count(e.id) as the_count, t.name as tenant_name " +
            "FROM events e " +
            "INNER JOIN masterevents me " +
            "ON e.id = me.id " +
            "INNER JOIN tenants t " +
            "ON e.tenant_id = t.id " +
            "WHERE t.disabled = 0 " +
                "AND me.workflow_state <> 'OPEN' " +
                "AND me.completedDate >= ? " +
                "AND me.completedDate < ? " +
            "GROUP BY t.name;";

    public List<CountByTenant> getTotalEventCountByTenant() {
        return template.query(EVENT_COUNT_BY_TENANT_SQL, ((resultSet, i) -> mapTheRow(resultSet, logger)));
    }

    public List<CountByTenant> getPastMonthEventCountByTenant() {
        return template.query(EVENT_COUNT_BY_TENANT_LAST_MONTH_SQL,
                              Collections.singletonList(LocalDateTime.now().minusMonths(1).format(dateTimeFormat)).toArray(),
                              ((resultSet, i) -> mapTheRow(resultSet, logger)));
    }

    public List<CountByTenant> getTotalEventCountByTenantByMonth(int monthsBack) {
        LocalDate todaysDate = LocalDate.now();

        LocalDateTime[] range = new LocalDateTime[2];

        range[0] = LocalDateTime.of(todaysDate.getYear(), todaysDate.getMonthValue(), 1, 0, 0).minusMonths(monthsBack);
        range[1] = range[0].plusMonths(1);

        logger.info("For the month of " + range[0].getMonth().toString());

        return template.query(EVENT_COUNT_BY_TENANT_BY_MONTH_SQL,
                              range,
                              ((resultSet, i) -> mapTheRow(resultSet, logger)));
    }
}
