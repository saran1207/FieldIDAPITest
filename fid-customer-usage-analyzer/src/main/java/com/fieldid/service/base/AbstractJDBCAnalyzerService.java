package com.fieldid.service.base;

import com.fieldid.model.CountByUnit;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

/**
 *
 *
 * Created by Jordan Heath on 2016-09-20.
 */
public abstract class AbstractJDBCAnalyzerService {
    @SuppressWarnings("SpringJavaAutowiredMembersInspection") //Suppressing this warning, because we use the @Service annotation further along the inheritance chain.
    @Autowired
    protected JdbcTemplate template;
    protected static final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

    protected CountByUnit mapTheRow(ResultSet resultSet, Logger logger) {
        try {
            Long count = resultSet.getLong("the_count");
            String name = resultSet.getString("tenant_name");

            return new CountByUnit(name, count);
        } catch (SQLException e) {
            logger.error("There was an exception while processing a Count row...", e);
        }

        return null;
    }

    protected CountByUnit mapNonTenantRow(ResultSet resultSet, String unit, Logger logger) {
        try {
            Long count = resultSet.getLong("the_count");

            return new CountByUnit(unit, count);
        } catch (SQLException e) {
            logger.error("There was an exception while processing a Non-Tenant Count Row...", e);
        }

        return null;
    }
}
