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
        return Arrays.asList("Tenant", "SalesForceId", "Users");
    }

    public TableView getUageReportData(Date fromDate, Date toDate) {
       /* select t.name, t.salesforceId, count(u.id) as total from tenants t, users u
        where t.disabled=0 and u.tenant_id=t.id
        and u.last_login >= '<START DATE>' and u.last_login <= '<END DATE>' group by t.name;*/

        Query q = getEntityManager().createNativeQuery(
                "SELECT t.name, t.salesforceId, count(u.id) AS total " +
                "FROM tenants t, users u " +
                "WHERE t.disabled=0 AND u.tenant_id=t.id AND u.last_login >= :startDate AND u.last_login <= :endDate GROUP BY t.name");
        q.setParameter("startDate", fromDate);
        q.setParameter("endDate", toDate);
        List<Object[]> queryResult = q.getResultList();
        TableView result = new TableView(queryResult.size(), 3);
        System.out.println("Results size " + result.size());
        int i = 0;
        for (Object[] cols: queryResult) {
            for (int j = 0; j < cols.length; j++) {
                System.out.println("row[" + i + "] col[" + j + "] val " + cols[j]);
                result.setCell(i, j, cols[j]);
            }
            i++;
        }
        return result;
    }
}
