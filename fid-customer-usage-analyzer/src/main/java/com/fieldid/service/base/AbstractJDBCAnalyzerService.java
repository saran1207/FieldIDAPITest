package com.fieldid.service.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 *
 * Created by Jordan Heath on 2016-09-20.
 */
public abstract class AbstractJDBCAnalyzerService {
    @SuppressWarnings("SpringJavaAutowiredMembersInspection") //Suppressing this warning, because we use the @Service annotation further along the inheritance chain.
    @Autowired
    protected JdbcTemplate template;
}
