package com.fieldid.service.asset;

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
public class AssetUsageAnalyzerService extends AbstractJDBCAnalyzerService {
    private static final Logger logger = Logger.getLogger(AssetUsageAnalyzerService.class);

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


    public List<CountByTenant> getTotalAssetCountByTenant() {
        return template.query(ASSET_COUNT_BY_TENANT_SQL,
                              (resultSet, rowNumber) -> mapTheRow(resultSet, logger));
    }

    public List<CountByTenant> getPastMonthAssetCountByTenant() {
        return template.query(ASSET_COUNT_BY_TENANT_PAST_MONTH_SQL,
                              Collections.singletonList(LocalDateTime.now().minusMonths(1).format(dateTimeFormat)).toArray(),
                              (resultSet, rowNumber) -> mapTheRow(resultSet, logger));
    }
}
