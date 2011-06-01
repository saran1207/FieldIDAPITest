package com.n4systems.fieldid.wicket.data;

import com.n4systems.ejb.legacy.PopulatorLog;
import com.n4systems.tools.Pager;
import org.apache.wicket.spring.injection.annot.SpringBean;
import rfid.ejb.PopulatorCriteria;
import rfid.ejb.entity.PopulatorLogBean;

import java.util.Date;

public class PopulatorLogBeanDataProvider extends PagerAdapterDataProvider<PopulatorLogBean> {

    @SpringBean
    private PopulatorLog populatorLog;

    private Long tenantId;
    private PopulatorCriteria criteria;

    public void setCriteria(Long tenantId, Date fromDate, Date toDate, PopulatorLog.logStatus logStatus, PopulatorLog.logType logType) {
        this.tenantId = tenantId;
        criteria = new PopulatorCriteria();
        criteria.setFromDate(fromDate);
        criteria.setToDate(toDate);
        criteria.setLogStatus(logStatus);
        criteria.setLogType(logType);
    }

    @Override
    protected Pager<PopulatorLogBean> getPager(int pageNumber, int pageSize) {
        if (criteria == null) {
            throw new IllegalStateException("criteria not initialized");
        }
        return populatorLog.findPopulatorLogBySearch(tenantId, criteria, pageNumber, pageSize);
    }

}
