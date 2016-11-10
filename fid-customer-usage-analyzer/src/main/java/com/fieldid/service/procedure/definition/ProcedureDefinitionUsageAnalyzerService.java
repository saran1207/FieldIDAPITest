package com.fieldid.service.procedure.definition;

import com.fieldid.model.CountByUnit;
import com.fieldid.service.base.AbstractJDBCAnalyzerService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 *
 *
 * Created by Jordan Heath on 2016-09-20.
 */
@Service
public class ProcedureDefinitionUsageAnalyzerService extends AbstractJDBCAnalyzerService {
    private static final Logger logger = Logger.getLogger(ProcedureDefinitionUsageAnalyzerService.class);

    private static final String PROCEDURE_DEFINITION_COUNT =
            "SELECT COUNT(p.id) as the_count " +
                    "FROM procedure_definitions p " +
                    "INNER JOIN tenants t " +
                    "ON p.tenant_id = t.id " +
                    "WHERE t.disabled = 0 " +
                    "AND p.state = 'ACTIVE';";

    private static final String COUNT_BY_TENANT_SQL =
            "SELECT COUNT(p.id) as the_count, t.name as tenant_name " +
                    "FROM procedure_definitions p " +
                    "INNER JOIN tenants t " +
                    "ON p.tenant_id = t.id " +
                    "WHERE t.disabled = 0 " +
                    "AND p.state = 'ACTIVE' " +
                    "GROUP BY t.name;";

    private static final String PAST_MONTH_BY_TENANT_SQL =
            "SELECT COUNT(p.id) as the_count, t.name as tenant_name " +
                    "FROM procedure_definitions p " +
                    "INNER JOIN tenants t " +
                    "ON p.tenant_id = t.id " +
                    "WHERE t.disabled = 0 " +
                    "AND p.state = 'ACTIVE' " +
                    "AND p.created >= ? " +
                    "GROUP BY t.name;";

    private static final String PROCEDURE_COUNT_BY_TIME_PERIOD =
            "SELECT COUNT(p.id) as the_count " +
                    "FROM procedure_definitions p " +
                    "INNER JOIN tenants t " +
                    "ON p.tenant_id = t.id " +
                    "WHERE t.disabled = 0 " +
                    "AND p.state = 'ACTIVE' " +
                    "AND p.created >= ? " +
                    "AND p.created < ? ;";

    public List<CountByUnit> getTotalProcedureDefinitionCount() {
        return template.query(PROCEDURE_DEFINITION_COUNT,
                ((resultSet, i) -> mapNonTenantRow(resultSet, "INFINITE", logger)));
    }

    public List<CountByUnit> getTotalProcedureDefinitionCountByTenant() {
        return template.query(COUNT_BY_TENANT_SQL,
                              ((resultSet, i) -> mapTheRow(resultSet, logger)));
    }

    public List<CountByUnit> getPastMonthProcedureDefinitionCountByTenant() {
        return template.query(PAST_MONTH_BY_TENANT_SQL,
                              Collections.singletonList(LocalDateTime.now().minusMonths(1).format(dateTimeFormat)).toArray(),
                              ((resultSet, i) -> mapTheRow(resultSet, logger)));
    }

    public List<CountByUnit> getTotalProcedureDefinitionCountByMonth(int monthsBack) {
        LocalDate todaysDate = LocalDate.now();

        LocalDateTime[] range = new LocalDateTime[2];

        range[0] = LocalDateTime.of(todaysDate.getYear(), todaysDate.getMonthValue(), 1, 0, 0).minusMonths(monthsBack);
        range[1] = range[0].plusMonths(1);

        logger.info("For the month of " + range[0].getMonth().toString());

        return template.query(PROCEDURE_COUNT_BY_TIME_PERIOD,
                range,
                ((resultSet, i) -> mapNonTenantRow(resultSet, range[0].getMonth().toString(), logger)));
    }

    public List<CountByUnit> getTotalProcedureDefinitionCountByYear(int yearsBack) {
        LocalDate todaysDate = LocalDate.now();

        LocalDateTime[] range = new LocalDateTime[2];

        range[0] = LocalDateTime.of(todaysDate.getYear(), 1, 1, 0, 0).minusYears(yearsBack);
        range[1] = range[0].plusYears(1);

        logger.info("For the year of " + range[0].getYear());

        return template.query(PROCEDURE_COUNT_BY_TIME_PERIOD,
                range,
                ((resultSet, i) -> mapNonTenantRow(resultSet, Integer.toString(range[0].getYear()), logger)));
    }
}
