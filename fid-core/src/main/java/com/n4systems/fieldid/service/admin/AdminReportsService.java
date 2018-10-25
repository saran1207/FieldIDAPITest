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
        return Arrays.asList("Tenant", "SalesForceId", "Snapshot Date", "*Admin User%", "*Inspection User%", "Completed Events", "*Open Events", "*Overdue Events", "*Assets");
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

         SELECT (select count(*) from masterevents me
			inner join events e on e.id = me.id where e.tenant_id = t.id AND me.workflow_state = 'COMPLETED'
                AND me.completedDate BETWEEN '2010-10-01' AND '2010-10-31')
		 FROM tenants t
         GROUP by t.name;

         select (select count(*) from masterevents me
			inner join events e on e.id = me.id where e.tenant_id = t.id AND me.workflow_state = 'OPEN' )
         FROM tenants t where t.disabled = 0
         GROUP by t.name;

         SELECT (select count(*) from masterevents me
		    inner join events e on e.id = me.id where e.tenant_id = t.id AND me.workflow_state = 'OPEN' AND me.dueDate <= CURDATE())
         FROM tenants t where t.disabled = 0
         GROUP by t.name;

         SELECT (select count(*) from assets a WHERE a.tenant_id = t.id AND a.state = 'ACTIVE')
         FROM tenants t where t.disabled = 0
         GROUP by t.name;

        */

        System.out.println("Start: getUsageReportData");
        Query q1 = getEntityManager().createNativeQuery(
                "SELECT t.name, t.salesforceId, " +
                        "date(:snapshotDate) " +
                        "FROM tenants t " +
                        "WHERE t.disabled = 0 GROUP BY t.name;");
        q1.setParameter("snapshotDate", toDate);
        List<Object[]> queryResult = q1.getResultList();
        System.out.println("Query 1 Results size " + queryResult.size());

        TableView result = new TableView(queryResult.size(), getUsageReportColumnHeader().size());

        int i = 0;
        for (Object[] cols: queryResult) {
            for (int j = 0; j < 3; j++) {
                System.out.println("query 1 row[" + i + "] col[" + j + "] val " + cols[j]);
                result.setCell(i, j, cols[j]);
            }
            i++;
        }

        Query q2 = getEntityManager().createNativeQuery(
                "SELECT GREATEST(" +
                        "ROUND((SELECT count(*) FROM users u2 " +
                            "WHERE u2.tenant_id = t.id AND u2.userType IN ('FULL', 'ADMIN') " +
                                "AND u2.registered = 1 AND u2.state = 'ACTIVE') / " +
                        "(SELECT ts.maxEmployeeUsers FROM tenant_settings ts WHERE ts.tenant_id = t.id) * 100, 0), -1) " +
                        "FROM tenants t " +
                        "WHERE t.disabled = 0 GROUP BY t.name;"
        );
        queryResult = q2.getResultList();
        System.out.println("Query 2 Results size " + queryResult.size());
        i = 0;
        for (Object obj: queryResult) {
            System.out.println("query 2 row[" + i + "] col[" + 4 + "] val " + obj);
            result.setCell(i, 3, obj);
            i++;
        }

        Query q3 = getEntityManager().createNativeQuery(
                "SELECT COALESCE(GREATEST(" +
                        "ROUND((SELECT count(*) FROM users u2 " +
                        "WHERE u2.tenant_id = t.id AND u2.userType IN ('LITE') " +
                        "AND u2.registered = 1 AND u2.state = 'ACTIVE') / " +
                        "(SELECT ts.maxLiteUsers FROM tenant_settings ts WHERE ts.tenant_id = t.id) * 100, 0), -1), 0) " +
                        "FROM tenants t " +
                        "WHERE t.disabled = 0 GROUP BY t.name;"
        );
        queryResult = q3.getResultList();
        System.out.println("Query 3 Results size " + queryResult.size());
        i = 0;
        for (Object obj: queryResult) {
            System.out.println("query 3 row[" + i + "] col[" + 4 + "] val " + obj);
            result.setCell(i, 4, obj);
            i++;
        }

        Query q4 = getEntityManager().createNativeQuery(
                "SELECT (SELECT count(*) FROM masterevents me " +
                        "INNER JOIN events e ON e.id = me.id WHERE e.tenant_id = t.id AND me.workflow_state = 'COMPLETED' " +
                        "                AND me.completedDate BETWEEN :startDate AND :endDate) " +
                        "FROM tenants t " +
                        "WHERE t.disabled=0 GROUP BY t.name;"
        );
        q4.setParameter("startDate", fromDate);
        q4.setParameter("endDate", toDate);
        queryResult = q4.getResultList();
        System.out.println("Query 4 Results size " + queryResult.size());
        i = 0;
        for (Object obj: queryResult) {
            System.out.println("query 4 row[" + i + "] col[" + 5 + "] val " + obj);
            result.setCell(i, 5, obj);
            i++;
        }

        Query q5 = getEntityManager().createNativeQuery(
                "SELECT (SELECT count(*) FROM masterevents me " +
                        "INNER JOIN events e ON e.id = me.id WHERE e.tenant_id = t.id AND me.workflow_state = 'OPEN') " +
                        "FROM tenants t " +
                        "WHERE t.disabled=0 GROUP BY t.name;"
        );
        queryResult = q5.getResultList();
        System.out.println("Query 5 Results size " + queryResult.size());
        i = 0;
        for (Object obj: queryResult) {
            System.out.println("query 5 row[" + i + "] col[" + 6 + "] val " + obj);
            result.setCell(i, 6, obj);
            i++;
        }

        Query q6 = getEntityManager().createNativeQuery(
                "SELECT (SELECT count(*) FROM masterevents me " +
                        "INNER JOIN events e ON e.id = me.id WHERE e.tenant_id = t.id AND me.workflow_state = 'OPEN' AND me.dueDate <= CURDATE()) " +
                        "FROM tenants t " +
                        "WHERE t.disabled=0 GROUP BY t.name;"
        );
        queryResult = q6.getResultList();
        System.out.println("Query 6 Results size " + queryResult.size());
        i = 0;
        for (Object obj: queryResult) {
            System.out.println("query 6 row[" + i + "] col[" + 7 + "] val " + obj);
            result.setCell(i, 7, obj);
            i++;
        }

        Query q7 = getEntityManager().createNativeQuery(
                "SELECT (select count(*) from assets a WHERE a.tenant_id = t.id AND a.state = 'ACTIVE') " +
                        "FROM tenants t " +
                        "WHERE t.disabled=0 GROUP BY t.name;"
        );
        queryResult = q7.getResultList();
        System.out.println("Query 7 Results size " + queryResult.size());
        i = 0;
        for (Object obj: queryResult) {
            System.out.println("query 7 row[" + i + "] col[" + 8 + "] val " + obj);
            result.setCell(i, 8, obj);
            i++;
        }

        return result;
    }
}
