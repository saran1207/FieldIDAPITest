package com.n4systems.handlers.remover;

import java.util.List;

import javax.persistence.Query;

import com.n4systems.handlers.remover.summary.NotificationSettingDeleteSummary;
import com.n4systems.model.InspectionType;
import com.n4systems.model.notificationsettings.NotificationSetting;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.utils.LargeUpdateQueryRunner;

public class NotificationSettingDeleteHandlerImpl implements NotificationSettingDeleteHandler {

	private InspectionType inspectionType;
	
	public void remove(Transaction transaction) {
		List<Long> notificationsToDelete = getNotificationIdsWithInspectionType(transaction);
		
		String deleteQuery = "DELETE FROM " + NotificationSetting.class.getName() + " WHERE id IN (:ids)";
		new LargeUpdateQueryRunner(transaction.getEntityManager().createQuery(deleteQuery), notificationsToDelete).executeUpdate();
	}

	public NotificationSettingDeleteSummary summary(Transaction transaction) {
		NotificationSettingDeleteSummary summary = new NotificationSettingDeleteSummary();
		
		summary.setNotificationsToDelete(getNotificationIdsWithInspectionType(transaction).size());
		
		return summary;
	}

	@SuppressWarnings("unchecked")
	private List<Long> getNotificationIdsWithInspectionType(Transaction transaction) {
		Query query = transaction.getEntityManager().createQuery("SELECT ns.id FROM " + NotificationSetting.class.getName() + " ns, IN (ns.inspectionTypes) inspectionTypeId WHERE inspectionTypeId = :inspectionTypeId" );
		query.setParameter("inspectionTypeId", inspectionType.getId());
		
		 
		return (List<Long>)query.getResultList();
	}


	public NotificationSettingDeleteHandlerImpl forInspectionType(InspectionType inspectionType) {
		this.inspectionType = inspectionType;
		return this;
	}

}
