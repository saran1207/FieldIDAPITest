package com.n4systems.model.setupdata;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.n4systems.model.tenant.SetupDataLastModDates;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.UnitOfWork;
import com.n4systems.services.SetupDataGroup;

public class SetupDataLastModDatesUpdater implements UnitOfWork<Void> {
	private final SetupDataGroup group;
	private final Long tenantId;

	public SetupDataLastModDatesUpdater(SetupDataGroup group, Long tenantId) {
		this.group = group;
		this.tenantId = tenantId;
	}
	
	@Override
	public Void run(Transaction transaction) {
		EntityManager em = transaction.getEntityManager();
		
		String stmt = String.format("UPDATE %s m SET m.%s = :modDate%s", SetupDataLastModDates.class.getName(), group.getFieldName(),(tenantId == null) ? "" : " WHERE m.tenantId = :tenantId");
		
		Query query = em.createQuery(stmt);
		query.setParameter("modDate", new Date());
		
		if (tenantId != null) {
			query.setParameter("tenantId", tenantId);
		}
		
		query.executeUpdate();
		
		return null;
	}

}
