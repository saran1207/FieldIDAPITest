package com.n4systems.ejb;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.n4systems.compliance.ComplianceCheck;
import com.n4systems.ejb.interceptor.TimingInterceptor;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.Tenant;
import com.n4systems.model.InspectionSchedule.ScheduleStatus;
import com.n4systems.util.DateHelper;

@Interceptors( { TimingInterceptor.class })
@Stateless
public class ComplianceManagerImpl implements ComplianceManager {
	private static final String unitName = "rfidEM";

	@PersistenceContext(unitName = unitName)
	private EntityManager em;

	public ComplianceCheck multiAssetComplianceCheck(Tenant tenant) {
		Long assetCount = (Long) em.createQuery(
				"SELECT DISTINCT count( schedule.product.id ) FROM " + InspectionSchedule.class.getName()
						+ " schedule left join schedule.product where schedule.tenant = :tenant AND schedule.status != :completedStatus").setParameter("tenant", tenant).setParameter(
				"completedStatus", ScheduleStatus.COMPLETED).getSingleResult();
		Long nonComplianceCount = (Long) em.createQuery(
				"SELECT DISTINCT count( schedule.product.id ) FROM " + InspectionSchedule.class.getName()
						+ " schedule left join schedule.product WHERE schedule.tenant = :tenant AND nextDate < :today AND schedule.status != :completedStatus").setParameter("today",
				DateHelper.getToday()).setParameter("tenant", tenant).setParameter("completedStatus", ScheduleStatus.COMPLETED).getSingleResult();

		return new ComplianceCheck(nonComplianceCount, assetCount);
	}

}
