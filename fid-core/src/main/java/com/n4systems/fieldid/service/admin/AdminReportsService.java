package com.n4systems.fieldid.service.admin;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.util.views.TableView;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class AdminReportsService extends FieldIdPersistenceService {

    public List<String> getUsageReportColumnHeader() {
        return Arrays.asList("Tenant", "SalesForceId", "Snapshot Date", "*Admin User%", "*Inspection User%",
                "Completed Events", "*Open Events", "*Overdue Events", "*Assets", "Published LOTO", "Draft LOTO",
                "Performed LOTO", "*Last LOTO Revision");
    }

    public TableView getUsageReportData(Date fromDate, Date toDate) {
        /* The data is obtained by running several queries and merging the data into one TableView by tenant name.
           Multiple queries were used since in testing using one large query caused connection timeouts. Some of the
           queries deal with a very large amount of data.
         */

       /* Original QUERY:

       select t.name, t.salesforceId, count(u.id) as total from tenants t, users u
        where t.disabled=0 and u.tenant_id=t.id
        and u.last_login >= '<START DATE>' and u.last_login <= '<END DATE>' group by t.name;*/

     /* queries:
         SELECT t.name, t.salesforceId,
       	 FROM tenants t
		 WHERE t.disabled=0 GROUP BY t.name;

         SELECT GREATEST((select count(*) from users u2 where u2.tenant_id = t.id and u2.userType in ('FULL', 'ADMIN') AND u2.registered = 1 and u2.state = 'ACTIVE') /
            (select ts.maxEmployeeUsers from tenant_settings ts where ts.tenant_id = t.id), -1)
         FROM tenants t WHERE t.disabled = 0 GROUP by t.name;

         SELECT COALESCE(GREATEST((select count(*) from users u2 where u2.tenant_id = t.id and u2.userType in ('LITE') AND u2.registered = 1 and u2.state = 'ACTIVE') /
            (select ts.maxLiteUsers from tenant_settings ts where ts.tenant_id = t.id), -1), 0)
         FROM tenants t WHERE t.disabled=0 GROUP by t.name;

         SELECT (select count(*) from thing_events te
            inner join masterevents me on me.id = te.id
			inner join events e on e.id = me.id
			inner join eventtypes et on et.id = e.type_id
			where e.tenant_id = t.id AND me.state = 'ACTIVE' AND me.workflow_state = 'COMPLETED'
                AND me.completedDate BETWEEN '2010-10-01' AND '2010-10-31' AND et.action_type = 0)
		 FROM tenants t
         GROUP by t.name;

         select (select count(*) from thing_events te
            inner join masterevents me on me.id = te.id
			inner join events e on e.id = me.id
			inner join eventtypes et on et.id = e.type_id
			where e.tenant_id = t.id AND me.state = 'ACTIVE' AND me.workflow_state = 'OPEN' AND et.action_type = 0)
         FROM tenants t where t.disabled = 0
         GROUP by t.name;

         SELECT (select count(*) from thing_events te
            inner join masterevents me on me.id = te.id
		    inner join events e on e.id = me.id
		    inner join eventtypes et on et.id = e.type_id
		    where e.tenant_id = t.id AND me.state = 'ACTIVE' AND me.workflow_state = 'OPEN' AND me.dueDate <= CURDATE() AND et.action_type = 0)
         FROM tenants t where t.disabled = 0
         GROUP by t.name;

         SELECT (select count(*) from assets a WHERE a.tenant_id = t.id AND a.state = 'ACTIVE')
         FROM tenants t where t.disabled = 0
         GROUP by t.name;

	     SELECT (select count(*) from procedure_definitions WHERE state = 'ACtIVE' AND published_state = 'PUBLISHED' and tenant_id = t.id AND origin_date BETWEEN '2017-10-01' AND '2017-10-31')
         FROM tenants t where t.disabled = 0
         GROUP BY t.name;

         SELECT (select count(*) from procedure_definitions WHERE state = 'ACtIVE' AND published_state = 'DRAFT' and tenant_id = t.id
         AND (modified BETWEEN '2018-8-01' AND '2018-6-30' OR created BETWEEN '2018-6-01' AND '2018-6-30'))
         FROM tenants t where t.disabled = 0
         GROUP BY t.name;

         SELECT
         (select count(*) from procedures  p WHERE p.state='ACTIVE' and p.tenant_id = t.id AND p.unlock_date BETWEEN '2017-10-1' AND '2017-10-31') +
         (select count(*) from procedures p WHERE p.state='ACTIVE' and p.tenant_id = t.id AND p.lock_date BETWEEN '2017-10-1' AND '2017-10-31')
         FROM tenants t WHERE t.disabled = 0 GROUP BY t.name;

	     SELECT (select max(p.origin_date) from procedure_definitions p WHERE p.state = 'ACtIVE' AND p.published_state = 'PUBLISHED' and tenant_id = t.id)
         FROM tenants t where t.disabled = 0
         GROUP BY t.name;

        */

        Query q = getEntityManager().createNativeQuery(
                "SELECT t.name, t.salesforceId, " +
                        "DATE_FORMAT(:snapshotDate, '%m/%d/%Y') " +
                        "FROM tenants t " +
                        "WHERE t.disabled = 0 GROUP BY t.name;");
        q.setParameter("snapshotDate", toDate);
        List<Object[]> queryResult = q.getResultList();

        TableView result = new TableView(queryResult.size(), getUsageReportColumnHeader().size());

        int i = 0;
        for (Object[] cols: queryResult) {
            for (int j = 0; j < 3; j++) {
                result.setCell(i, j, cols[j]);
            }
            i++;
        }

        q = getEntityManager().createNativeQuery(
                "SELECT GREATEST(" +
                        "ROUND((SELECT count(*) FROM users u2 " +
                            "WHERE u2.tenant_id = t.id AND u2.userType IN ('FULL', 'ADMIN') " +
                                "AND u2.registered = 1 AND u2.state = 'ACTIVE') / " +
                        "(SELECT ts.maxEmployeeUsers FROM tenant_settings ts WHERE ts.tenant_id = t.id) * 100, 0), -1) " +
                        "FROM tenants t " +
                        "WHERE t.disabled = 0 GROUP BY t.name;"
        );
        queryResult = q.getResultList();
        i = 0;
        for (Object obj: queryResult) {
            result.setCell(i, 3, obj);
            i++;
        }

        q = getEntityManager().createNativeQuery(
                "SELECT COALESCE(GREATEST(" +
                        "ROUND((SELECT count(*) FROM users u2 " +
                        "WHERE u2.tenant_id = t.id AND u2.userType IN ('LITE') " +
                        "AND u2.registered = 1 AND u2.state = 'ACTIVE') / " +
                        "(SELECT ts.maxLiteUsers FROM tenant_settings ts WHERE ts.tenant_id = t.id) * 100, 0), -1), 0) " +
                        "FROM tenants t " +
                        "WHERE t.disabled = 0 GROUP BY t.name;"
        );
        queryResult = q.getResultList();
        i = 0;
        for (Object obj: queryResult) {
            result.setCell(i, 4, obj);
            i++;
        }

        q = getEntityManager().createNativeQuery(
                "SELECT (SELECT count(*) FROM thing_events te " +
                        "INNER JOIN masterevents me ON me.id = te.id " +
                        "INNER JOIN events e ON e.id = me.id " +
                        "INNER JOIN eventtypes et ON et.id = e.type_id " +
                        "WHERE e.tenant_id = t.id AND me.state = 'ACTIVE' AND me.workflow_state = 'COMPLETED' " +
                        "  AND me.completedDate BETWEEN :startDate AND :endDate AND et.action_type = 0) " +
                        "FROM tenants t " +
                        "WHERE t.disabled=0 GROUP BY t.name;"
        );
        q.setParameter("startDate", fromDate);
        q.setParameter("endDate", toDate);
        queryResult = q.getResultList();
        i = 0;
        for (Object obj: queryResult) {
            result.setCell(i, 5, obj);
            i++;
        }

        q = getEntityManager().createNativeQuery(
                "SELECT (SELECT count(*) FROM thing_events te " +
                        "INNER JOIN masterevents me ON me.id = te.id " +
                        "INNER JOIN events e ON e.id = me.id " +
                        "INNER JOIN eventtypes et ON et.id = e.type_id " +
                        "WHERE e.tenant_id = t.id AND me.state = 'ACTIVE' AND me.workflow_state = 'OPEN' AND et.action_type = 0) " +
                        "FROM tenants t " +
                        "WHERE t.disabled=0 GROUP BY t.name;"
        );
        queryResult = q.getResultList();
        i = 0;
        for (Object obj: queryResult) {
            result.setCell(i, 6, obj);
            i++;
        }

        q = getEntityManager().createNativeQuery(
                "SELECT (SELECT count(*) FROM thing_events te " +
                        "INNER JOIN masterevents me ON me.id = te.id " +
                        "INNER JOIN events e ON e.id = me.id " +
                        "INNER JOIN eventtypes et ON et.id = e.type_id " +
                        "WHERE e.tenant_id = t.id AND me.state = 'ACTIVE' AND me.workflow_state = 'OPEN' AND me.dueDate <= CURDATE() " +
                        "     AND et.action_type = 0) " +
                        "FROM tenants t " +
                        "WHERE t.disabled=0 GROUP BY t.name;"
        );
        queryResult = q.getResultList();
        i = 0;
        for (Object obj: queryResult) {
            result.setCell(i, 7, obj);
            i++;
        }

        q = getEntityManager().createNativeQuery(
                "SELECT (select count(*) from assets a WHERE a.tenant_id = t.id AND a.state = 'ACTIVE') " +
                        "FROM tenants t " +
                        "WHERE t.disabled=0 GROUP BY t.name;"
        );
        queryResult = q.getResultList();
        i = 0;
        for (Object obj: queryResult) {
            result.setCell(i, 8, obj);
            i++;
        }

        q = getEntityManager().createNativeQuery(
                "SELECT (select count(*) from procedure_definitions WHERE state = 'ACtIVE' " +
                        "AND published_state = 'PUBLISHED' AND tenant_id = t.id AND " +
                        "origin_date BETWEEN :startDate AND :endDate) " +
                        "FROM tenants t " +
                        "WHERE t.disabled = 0 GROUP BY t.name;"
        );
        q.setParameter("startDate", fromDate);
        q.setParameter("endDate", toDate);
        queryResult = q.getResultList();
        i = 0;
        for (Object obj: queryResult) {
            result.setCell(i, 9, obj);
            i++;
        }

        q = getEntityManager().createNativeQuery(
                "SELECT (SELECT count(*) FROM procedure_definitions WHERE state = 'ACTIVE' " +
                        "AND published_state = 'DRAFT' AND tenant_id = t.id " +
                        "AND (modified BETWEEN :startDate AND :endDate OR created BETWEEN :startDate AND :endDate)) " +
                        "FROM tenants t " +
                        "WHERE t.disabled = 0 GROUP BY t.name;"
        );
        q.setParameter("startDate", fromDate);
        q.setParameter("endDate", toDate);
        queryResult = q.getResultList();
        i = 0;
        for (Object obj: queryResult) {
            result.setCell(i, 10, obj);
            i++;
        }

        q = getEntityManager().createNativeQuery(
                "SELECT  (select count(*) from procedures p WHERE p.state='ACTIVE' and p.tenant_id = t.id AND p.unlock_date BETWEEN :startDate AND :endDate) + " +
                        "(select count(*) from procedures p WHERE p.state='ACTIVE' and p.tenant_id = t.id AND p.lock_date   BETWEEN :startDate AND :endDate) " +
                        "FROM tenants t WHERE t.disabled = 0 GROUP BY t.name;"
        );
        q.setParameter("startDate", fromDate);
        q.setParameter("endDate", toDate);
        queryResult = q.getResultList();
        i = 0;
        for (Object obj: queryResult) {
            result.setCell(i, 11, obj);
            i++;
        }

        q = getEntityManager().createNativeQuery(
                "SELECT (select max(p.origin_date) from procedure_definitions p WHERE p.state = 'ACtIVE' AND p.published_state = 'PUBLISHED' and tenant_id = t.id) " +
                        "FROM tenants t where t.disabled = 0 GROUP BY t.name;"
        );
        queryResult = q.getResultList();
        i = 0;
        for (Object obj: queryResult) {
            result.setCell(i, 12, obj);
            i++;
        }

        return result;
    }
}
