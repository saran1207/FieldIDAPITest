package com.fieldid.service.asset;

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
public class AssetUsageAnalyzerService extends AbstractJDBCAnalyzerService {
    private static final Logger logger = Logger.getLogger(AssetUsageAnalyzerService.class);

    private static final String ASSET_COUNT_SQL =
            "SELECT count(a.id) as the_count  " +
                    "FROM assets a " +
                    "INNER JOIN tenants t " +
                    "ON a.tenant_id = t.id " +
                    "WHERE t.disabled = 0 ";

    private static final String ASSET_COUNT_BY_TENANT_SQL =
            "SELECT count(a.id) as the_count, t.name as tenant_name " +
            "FROM assets a " +
            "INNER JOIN tenants t " +
            "ON a.tenant_id = t.id " +
            "WHERE t.disabled = 0 " +
            "GROUP BY t.name;";

    private static final String ASSET_COUNT_BY_TENANT_PAST_MONTH_SQL =
            "SELECT count(a.id) as the_count, t.name as tenant_name " +
            "FROM assets a " +
            "INNER JOIN tenants t " +
            "ON a.tenant_id = t.id " +
            "WHERE t.disabled = 0 " +
                "AND a.created >= ? " +
                "AND a.state = 'ACTIVE' " +
            "GROUP BY t.name;";

    private static final String ASSET_COUNT_BY_TIME_PERIOD_SQL =
            "SELECT count(a.id) as the_count " +
                    "FROM assets a " +
                    "INNER JOIN tenants t " +
                    "ON a.tenant_id = t.id " +
                    "WHERE t.disabled = 0 " +
                    "AND a.created >= ? " +
                    "AND a.created < ? " +
                    "AND a.state = 'ACTIVE';";

    public List<CountByUnit> getTotalAssetCount() {
        return template.query(ASSET_COUNT_SQL,
                (resultSet, i) -> mapNonTenantRow(resultSet, "INFINITE", logger));
    }

    public List<CountByUnit> getTotalAssetCountByTenant() {
        return template.query(ASSET_COUNT_BY_TENANT_SQL,
                              (resultSet, rowNumber) -> mapTheRow(resultSet, logger));
    }

    public List<CountByUnit> getPastMonthAssetCountByTenant() {
        return template.query(ASSET_COUNT_BY_TENANT_PAST_MONTH_SQL,
                              Collections.singletonList(LocalDateTime.now().minusMonths(1).format(dateTimeFormat)).toArray(),
                              (resultSet, rowNumber) -> mapTheRow(resultSet, logger));
    }

    public List<CountByUnit> getAssetCountByMonth(int monthsBack) {
        LocalDate todaysDate = LocalDate.now();

        LocalDateTime[] range = new LocalDateTime[2];

        range[0] = LocalDateTime.of(todaysDate.getYear(), todaysDate.getMonthValue(), 1, 0, 0).minusMonths(monthsBack);
        range[1] = range[0].plusMonths(1);

        logger.info("For the month of " + range[0].getMonth().toString());

        return template.query(ASSET_COUNT_BY_TIME_PERIOD_SQL,
                range,
                ((resultSet, i) -> mapNonTenantRow(resultSet, range[0].getMonth().toString(), logger)));
    }

    public List<CountByUnit> getAssetCountByYear(int yearsBack) {

        LocalDate todaysDate = LocalDate.now();

        LocalDateTime[] range = new LocalDateTime[2];

        range[0] = LocalDateTime.of(todaysDate.getYear(), 1, 1, 0, 0).minusYears(yearsBack);
        range[1] = range[0].plusYears(1);

        logger.info("For the year of " + range[0].getYear());

        return template.query(ASSET_COUNT_BY_TIME_PERIOD_SQL,
                range,
                ((resultSet, i) -> mapNonTenantRow(resultSet, Integer.toString(range[0].getYear()), logger)));
    }
}
