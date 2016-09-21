package com.fieldid.service.procedure.definition;

import com.fieldid.model.CountByTenant;
import com.fieldid.service.base.AbstractJDBCAnalyzerService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

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

    public List<CountByTenant> getTotalProcedureDefinitionCountByTenant() {
        return template.query(COUNT_BY_TENANT_SQL,
                              ((resultSet, i) -> mapTheRow(resultSet, logger)));
    }

    public List<CountByTenant> getPastMonthProcedureDefinitionCountByTenant() {
        return template.query(PAST_MONTH_BY_TENANT_SQL,
                              Collections.singletonList(LocalDateTime.now().minusMonths(1).format(dateTimeFormat)).toArray(),
                              ((resultSet, i) -> mapTheRow(resultSet, logger)));
    }
}
