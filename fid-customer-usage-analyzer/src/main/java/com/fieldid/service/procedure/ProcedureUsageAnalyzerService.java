package com.fieldid.service.procedure;

import com.fieldid.model.CountByTenant;
import com.fieldid.service.base.AbstractJDBCAnalyzerService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 *
 * Created by Jordan Heath on 2016-09-20.
 */
@Service
public class ProcedureUsageAnalyzerService extends AbstractJDBCAnalyzerService {
    private static final Logger logger = Logger.getLogger(ProcedureUsageAnalyzerService.class);

    private static final String COUNT_BY_TENANT_SQL =
            "SELECT COUNT(p.id) as the_count, t.name as tenant_name " +
            "FROM procedures p " +
            "INNER JOIN tenants t " +
                "ON p.tenant_id = t.id " +
            "WHERE t.disabled = 0 " +
                "AND p.state = 'ACTIVE' " +
                "AND p.workflow_state <> 'OPEN' " +
            "GROUP BY t.name;";

    private static final String PAST_MONTH_COUNT_BY_TENANT_SQL =
            "SELECT COUNT(p.id) as the_count, t.name as tenant_name " +
            "FROM procedures p " +
            "INNER JOIN tenants t " +
                "ON p.tenant_id = t.id " +
            "WHERE t.disabled = 0 " +
                "AND p.state = 'ACTIVE' " +
                "AND p.workflow_state <> 'OPEN' " +
                "AND (p.unlock_date >= ? OR p.lock_date >= ?) " +
            "GROUP BY t.name;";

    private static final String MONTHLY_COUNT_BY_TENANT_SQL =
            "SELECT COUNT(p.id) as the_count, t.name as tenant_name " +
            "FROM procedures p " +
            "INNER JOIN tenants t " +
                "ON p.tenant_id = t.id " +
            "WHERE t.disabled = 0 " +
                "AND p.state = 'ACTIVE' " +
                "AND p.workflow_state <> 'OPEN' " +
                "AND (p.unlock_date >= ? OR p.lock_date >= ?) " +
                "AND (p.unlock_date < ? OR p.lock_date < ?) " +
            "GROUP BY t.name;";

    public List<CountByTenant> getTotalProcedureCountByTenant() {
        return template.query(COUNT_BY_TENANT_SQL, ((resultSet, i) -> mapTheRow(resultSet, logger)));
    }

    public List<CountByTenant> getPastMonthProcedureCountByTenant() {
        String[] duplicate = new String[2];
        duplicate[0] = LocalDateTime.now().minusMonths(1).format(dateTimeFormat);
        duplicate[1] = LocalDateTime.now().minusMonths(1).format(dateTimeFormat);
        return template.query(PAST_MONTH_COUNT_BY_TENANT_SQL,
                              duplicate,
                              ((resultSet, i) -> mapTheRow(resultSet, logger)));
    }

    public List<CountByTenant> getMonthlyProcedureCountByTenant(int monthsBack) {
        LocalDate todaysDate = LocalDate.now();

        LocalDateTime[] range = new LocalDateTime[4];

        range[0] = LocalDateTime.of(todaysDate.getYear(), todaysDate.getMonthValue(), 1, 0, 0).minusMonths(monthsBack);
        range[1] = range[0];
        range[2] = range[0].plusMonths(1);
        range[3] = range[2];

        return template.query(MONTHLY_COUNT_BY_TENANT_SQL,
                range,
                ((resultSet, i) -> mapTheRow(resultSet, logger)));
    }
}
