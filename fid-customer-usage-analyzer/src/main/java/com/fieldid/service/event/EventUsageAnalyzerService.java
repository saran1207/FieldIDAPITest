package com.fieldid.service.event;

import com.fieldid.model.CountByUnit;
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

    private static final String EVENT_COUNT_SQL =
            "SELECT count(e.id) as the_count " +
                    "FROM events e " +
                    "INNER JOIN masterevents me " +
                    "ON e.id = me.id " +
                    "INNER JOIN tenants t " +
                    "ON e.tenant_id = t.id " +
                    "WHERE t.disabled = 0 " +
                    "AND me.workflow_state = 'COMPLETED' ";

    private static final String EVENT_COUNT_BY_TENANT_SQL =
            "SELECT count(e.id) as the_count, t.name as tenant_name " +
            "FROM events e " +
            "INNER JOIN masterevents me " +
            "ON e.id = me.id " +
            "INNER JOIN tenants t " +
            "ON e.tenant_id = t.id " +
            "WHERE t.disabled = 0 " +
                "AND me.workflow_state = 'COMPLETED' " +
            "GROUP BY t.name;";

    private static final String EVENT_COUNT_BY_TENANT_LAST_MONTH_SQL =
            "SELECT count(e.id) as the_count, t.name as tenant_name " +
            "FROM events e " +
            "INNER JOIN masterevents me " +
            "ON e.id = me.id " +
            "INNER JOIN tenants t " +
            "ON e.tenant_id = t.id " +
            "WHERE t.disabled = 0 " +
                "AND me.workflow_state = 'COMPLETED' " +
                "AND me.completedDate >= ? " +
            "GROUP BY t.name;";

    private static final String EVENT_COUNT_BY_TIME_PERIOD =
            "SELECT count(e.id) as the_count, t.name as tenant_name " +
            "FROM events e " +
            "INNER JOIN masterevents me " +
            "ON e.id = me.id " +
            "INNER JOIN tenants t " +
            "ON e.tenant_id = t.id " +
            "WHERE t.disabled = 0 " +
                "AND me.workflow_state = 'COMPLETED' " +
                "AND me.completedDate >= ? " +
                "AND me.completedDate < ? " +
            "GROUP BY t.name;";

    private static final String EVENT_COUNT_BY_TIME_PERIOD_NO_TENANT =
            "SELECT count(e.id) as the_count " +
                    "FROM events e " +
                    "INNER JOIN masterevents me " +
                    "ON e.id = me.id " +
                    "INNER JOIN tenants t " +
                    "ON e.tenant_id = t.id " +
                    "WHERE t.disabled = 0 " +
                    "AND me.workflow_state = 'COMPLETED' " +
                    "AND me.completedDate >= ? " +
                    "AND me.completedDate < ? ;";

    public List<CountByUnit> getTotalEventCount() {
        return template.query(EVENT_COUNT_SQL, (resultSet, i) -> mapNonTenantRow(resultSet, "INFINITE", logger));
    }

    public List<CountByUnit> getTotalEventCountByTenant() {
        return template.query(EVENT_COUNT_BY_TENANT_SQL, ((resultSet, i) -> mapTheRow(resultSet, logger)));
    }

    public List<CountByUnit> getPastMonthEventCountByTenant() {
        return template.query(EVENT_COUNT_BY_TENANT_LAST_MONTH_SQL,
                              Collections.singletonList(LocalDateTime.now().minusMonths(1).format(dateTimeFormat)).toArray(),
                              ((resultSet, i) -> mapTheRow(resultSet, logger)));
    }

    public List<CountByUnit> getTotalEventCountByMonth(int monthsBack) {
        LocalDate todaysDate = LocalDate.now();

        LocalDateTime[] range = new LocalDateTime[2];

        range[0] = LocalDateTime.of(todaysDate.getYear(), todaysDate.getMonthValue(), 1, 0, 0).minusMonths(monthsBack);
        range[1] = range[0].plusMonths(1);

        logger.info("For the month of " + range[0].getMonth().toString());

        return template.query(EVENT_COUNT_BY_TIME_PERIOD_NO_TENANT,
                range,
                ((resultSet, i) -> mapNonTenantRow(resultSet, range[0].getMonth().toString(), logger)));
    }

    public List<CountByUnit> getTotalEventCountByYear(int yearsBack) {
        LocalDate todaysDate = LocalDate.now();

        LocalDateTime[] range = new LocalDateTime[2];

        range[0] = LocalDateTime.of(todaysDate.getYear(), 1, 1, 0, 0).minusYears(yearsBack);
        range[1] = range[0].plusYears(1);

        logger.info("For the year of " + range[0].getYear());

        return template.query(EVENT_COUNT_BY_TIME_PERIOD_NO_TENANT,
                range,
                ((resultSet, i) -> mapNonTenantRow(resultSet, Integer.toString(range[0].getYear()), logger)));
    }

    public List<CountByUnit> getTotalEventCountByTenantByMonth(int monthsBack) {
        LocalDate todaysDate = LocalDate.now();

        LocalDateTime[] range = new LocalDateTime[2];

        range[0] = LocalDateTime.of(todaysDate.getYear(), todaysDate.getMonthValue(), 1, 0, 0).minusMonths(monthsBack);
        range[1] = range[0].plusMonths(1);

        logger.info("For the month of " + range[0].getMonth().toString());

        return template.query(EVENT_COUNT_BY_TIME_PERIOD,
                              range,
                              ((resultSet, i) -> mapTheRow(resultSet, logger)));
    }

    public List<CountByUnit> getTotalEventCountByTenantByYear(int yearsBack) {
        LocalDate todaysDate = LocalDate.now();

        LocalDateTime[] range = new LocalDateTime[2];

        range[0] = LocalDateTime.of(todaysDate.getYear(), 1, 1, 0, 0).minusYears(yearsBack);
        range[1] = range[0].plusYears(1);

        logger.info("For the year of " + range[0].getYear());

        return template.query(EVENT_COUNT_BY_TIME_PERIOD,
                              range,
                              ((resultSet, i) -> mapTheRow(resultSet, logger)));
    }
}
