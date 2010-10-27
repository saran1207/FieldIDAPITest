package com.n4systems.ejb.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeSchedule;
import org.apache.log4j.Logger;

import com.n4systems.ejb.InspectionScheduleManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.InspectionType;
import com.n4systems.model.InspectionSchedule.ScheduleStatus;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.services.InspectionScheduleService;
import com.n4systems.services.InspectionScheduleServiceImpl;
import com.n4systems.util.DateHelper;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter.Comparator;

 
public class InspectionScheduleManagerImpl implements InspectionScheduleManager {
	

	private static Logger logger = Logger.getLogger( InspectionScheduleManagerImpl.class );
	
	private PersistenceManager persistenceManager;
	
	
	protected EntityManager em;

	public InspectionScheduleManagerImpl(EntityManager em) {
		this.em = em;
		persistenceManager = new PersistenceManagerImpl(em);
	}

	@SuppressWarnings("deprecation")
	public List<InspectionSchedule> autoSchedule(Asset asset) {
		List<InspectionSchedule> schedules = new ArrayList<InspectionSchedule>();  
		
		AssetType assetType = persistenceManager.find(AssetType.class, asset.getType().getId());
		if (assetType != null) {
			for (InspectionType type : assetType.getInspectionTypes()) {
				AssetTypeSchedule schedule = assetType.getSchedule(type, asset.getOwner());
				if (schedule != null && schedule.isAutoSchedule()) {
					InspectionSchedule inspectionSchedule = new InspectionSchedule(asset, type);
					inspectionSchedule.setNextDate(assetType.getSuggestedNextInspectionDate(new Date(), type, asset.getOwner()));
					schedules.add(inspectionSchedule);
					new InspectionScheduleServiceImpl(persistenceManager).updateSchedule(inspectionSchedule);
				}
			}
		}
		logger.info("auto scheduled for asset " + asset);
		return schedules;
	}
	
	public InspectionSchedule update(InspectionSchedule schedule) {
		InspectionScheduleService service = new InspectionScheduleServiceImpl(persistenceManager);
		return service.updateSchedule(schedule);
	}
	
	public void restoreScheduleForInspection(Inspection inspection) {
		InspectionSchedule schedule = inspection.getSchedule();
		if (schedule != null) {
			schedule.removeInspection();
			new InspectionScheduleServiceImpl(persistenceManager).updateSchedule(schedule);
		}
		
	}
	
	public void removeAllSchedulesFor(Asset asset) {
		for (InspectionSchedule schedule : getAvailableSchedulesFor(asset)) {
			persistenceManager.delete(schedule);
		}
	}
	
	
	
	
	
	public void create(AssetTypeSchedule schedule) {
		persistenceManager.save(schedule);
	}
	
	
	
	
	


	
	public List<InspectionSchedule> getAvailableSchedulesFor(Asset asset) {
		QueryBuilder<InspectionSchedule> query = new QueryBuilder<InspectionSchedule>(InspectionSchedule.class, new OpenSecurityFilter());
		query.addSimpleWhere("asset", asset).addWhere(Comparator.NE, "status", "status", ScheduleStatus.COMPLETED);
		query.addOrder("nextDate");
		
		return persistenceManager.findAll(query);
	}
	
	public boolean schedulePastDue(Long scheduleId) {
		// here we'll select the next date off the schedule and see if it's after today
		QueryBuilder<Date> builder = new QueryBuilder<Date>(InspectionSchedule.class, new OpenSecurityFilter());
		builder.setSimpleSelect("nextDate");
		builder.addSimpleWhere("id", scheduleId);
		builder.addWhere(Comparator.NE, "status", "status", ScheduleStatus.COMPLETED);
		
		Date nextDate = persistenceManager.find(builder);
		
		
		return (nextDate != null) && DateHelper.getToday().after(nextDate);
	}
	
	public Long getAssetIdForSchedule(Long scheduleId) {
		QueryBuilder<Long> builder = new QueryBuilder<Long>(InspectionSchedule.class, new OpenSecurityFilter());
		builder.setSimpleSelect("asset.id");
		builder.addSimpleWhere("id", scheduleId);
		
		return persistenceManager.find(builder);
	}
	
	public Long getInspectionTypeIdForSchedule(Long scheduleId) {
		QueryBuilder<Long> builder = new QueryBuilder<Long>(InspectionSchedule.class, new OpenSecurityFilter());
		builder.setSimpleSelect("inspectionType.id");
		builder.addSimpleWhere("id", scheduleId);
		
		return persistenceManager.find(builder);
	}
	
	
	
	public Long getInspectionIdForSchedule(Long scheduleId) {
		QueryBuilder<Long> builder = new QueryBuilder<Long>(InspectionSchedule.class, new OpenSecurityFilter());
		builder.setSimpleSelect("inspection.id");
		builder.addSimpleWhere("id", scheduleId);
		
		return persistenceManager.find(builder);
	}
	
	
}
