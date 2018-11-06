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

         SELECT t.name, count(*)  from  tenants t
			inner join events e on e.tenant_id = t.id
            inner join eventtypes et on et.id = e.type_id AND et.action_type = 0
		     inner join masterevents me on me.id = e.id AND me.state = 'ACTIVE' AND me.workflow_state = 'COMPLETED' AND me.completedDate BETWEEN '2010-10-01' AND '2010-10-31'
             inner join thing_events te on te.id = me.id
             WHERE t.disabled = 0
         GROUP by t.name;

        SELECT t.name, count(*) FROM tenants t
		    INNER JOIN events e ON e.tenant_id = t.id
            INNER JOIN eventtypes et ON et.id = e.type_id AND et.action_type = 0
            INNER JOIN masterevents me ON me.id = e.id AND me.state = 'ACTIVE' AND me.workflow_state = 'OPEN'
            INNER JOIN thing_events te ON te.id = me.id
        WHERE t.disabled = 0 GROUP by t.name;

        SELECT t.name, count(*) FROM tenants t
		    INNER JOIN events e ON e.tenant_id = t.id
            INNER JOIN eventtypes et ON et.id = e.type_id AND et.action_type = 0
            INNER JOIN masterevents me ON me.id = e.id AND me.state = 'ACTIVE' AND me.workflow_state = 'OPEN' AND me.dueDate <= CURDATE()
            INNER JOIN thing_events te ON te.id = me.id
         WHERE t.disabled = 0 GROUP by t.name;

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
        int rowCount = i;

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
               "SELECT t.name, count(*) from tenants t " +
                    "inner join events e on e.tenant_id = t.id " +
                    "inner join eventtypes et on et.id = e.type_id AND et.action_type = 0 " +
                    "inner join masterevents me on me.id = e.id AND me.state = 'ACTIVE' AND me.workflow_state = 'COMPLETED' AND me.completedDate BETWEEN :startDate AND :endDate " +
                    "inner join thing_events te on te.id = me.id " +
                    "WHERE t.disabled=0 GROUP BY t.name;"
        );
        q.setParameter("startDate", fromDate);
        q.setParameter("endDate", toDate);
        placeResultInTable(q.getResultList(), result, 5, rowCount);

        q = getEntityManager().createNativeQuery(
                "SELECT t.name, count(*) FROM tenants t " +
                    "INNER JOIN events e ON e.tenant_id = t.id " +
                    "INNER JOIN eventtypes et ON et.id = e.type_id AND et.action_type = 0 " +
                    "INNER JOIN masterevents me ON me.id = e.id AND me.state = 'ACTIVE' AND me.workflow_state = 'OPEN' " +
                    "INNER JOIN thing_events te ON te.id = me.id " +
                    "WHERE t.disabled=0 GROUP BY t.name;"
        );
        placeResultInTable(q.getResultList(), result, 6, rowCount);

        q = getEntityManager().createNativeQuery(
                "SELECT t.name, count(*) FROM tenants t " +
                        "INNER JOIN events e ON e.tenant_id = t.id " +
                        "INNER JOIN eventtypes et ON et.id = e.type_id AND et.action_type = 0 " +
                        "INNER JOIN masterevents me ON me.id = e.id AND me.state = 'ACTIVE' AND me.workflow_state = 'OPEN' AND me.dueDate <= CURDATE() " +
                        "INNER JOIN thing_events te ON te.id = me.id " +
                        "WHERE t.disabled=0 GROUP BY t.name;"
        );
        placeResultInTable(q.getResultList(), result, 7, rowCount);

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

    /**
     * The query doesn't return a row when none of the conditions apply but we need to put '0' into the
     * tableview for this situation. Merge the query result into the tableview matching by tenant name. For
     * those tableview rows with a tenant name not in the query result insert a value of '0'.
     *
     * @param queryResult List<Object[]> where Object[0] is the tenant name and Object[1] is the query's result value.
     * @param tableView The tableview to insert the results. The cell at column 0 in each row is the tenant name.
     * @param cellColIndex
     * @param rowCount The number of rows in the tableview (which is the number of active tenants)
     */
    private void placeResultInTable(List<Object[]> queryResult, TableView tableView, int cellColIndex, int rowCount) {

        int cellRowIndex = 0;
        for (Object[] obj: queryResult) {
            while (!(tableView.getCell(cellRowIndex, 0)).equals(obj[0]) && cellRowIndex < rowCount) {
                tableView.setCell(cellRowIndex, cellColIndex, new Integer(0));
                cellRowIndex++;
            }
            if (cellRowIndex < rowCount) {
                tableView.setCell(cellRowIndex, cellColIndex, obj[1]);
                cellRowIndex++;
            }
        }
        while(cellRowIndex < rowCount) {
            tableView.setCell(cellRowIndex, cellColIndex, 0);
            cellRowIndex++;
        }
    }
}
