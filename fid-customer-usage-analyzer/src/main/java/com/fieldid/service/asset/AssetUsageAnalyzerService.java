package com.fieldid.service.asset;

import com.fieldid.model.CountByTenant;
import com.fieldid.service.base.AbstractJDBCAnalyzerService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
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
public class AssetUsageAnalyzerService extends AbstractJDBCAnalyzerService {
    private static final Logger logger = Logger.getLogger(AssetUsageAnalyzerService.class);

    private static final String ASSET_COUNT_BY_TENANT_SQL =
            "SELECT count(a.id) as asset_count, t.name as tenant_name " +
            "FROM assets a " +
            "INNER JOIN tenants t " +
            "ON a.tenant_id = t.id " +
            "WHERE t.disabled = 0 " +
            "GROUP BY t.name;";

    private static final String ASSET_COUNT_BY_TENANT_BY_MONTH_SQL =
            "SELECT count(a.id) as asset_count, t.name as tenant_name " +
            "FROM assets a " +
            "INNER JOIN tenants t " +
            "ON a.tenant_id = t.id " +
            "WHERE t.disabled = 0 " +
            "AND created >= ? " +
            "GROUP BY t.name;";


    public List<CountByTenant> getTotalAssetCountByTenant() {
        return template.query(ASSET_COUNT_BY_TENANT_SQL, (resultSet, rowNumber) -> mapTheRow(resultSet));
    }

    public List<CountByTenant> getPastMonthAssetCountByTenant() {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);

        String dateString = oneMonthAgo.format(dateFormat);

        return template.query(ASSET_COUNT_BY_TENANT_BY_MONTH_SQL, Collections.singletonList(dateString).toArray(), (resultSet, rowNumber) -> mapTheRow(resultSet));
    }

    public CountByTenant mapTheRow(ResultSet resultSet) {
        try {
            Long count = resultSet.getLong("asset_count");
            String name = resultSet.getString("tenant_name");

            return new CountByTenant(name, count);
        } catch (SQLException e) {
            logger.error("There was an exception while processing an Asset Count row...", e);
        }

        return null;
    }
}
