package com.n4systems.ejb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.n4systems.ejb.interceptor.TimingInterceptor;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Product;
import com.n4systems.model.ProductType;
import com.n4systems.model.ProductTypeSchedule;
import com.n4systems.model.InspectionSchedule.ScheduleStatus;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.inspectionschedulecount.InspectionScheduleCount;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.services.InspectionScheduleService;
import com.n4systems.services.InspectionScheduleServiceImpl;
import com.n4systems.util.DateHelper;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter.Comparator;

@Interceptors({TimingInterceptor.class})
@Stateless 
public class InspectionScheduleManagerImpl implements InspectionScheduleManager {
	private static final long INSPECTION_SCHEDULE_DATE_RANGE = 30L;

	private static Logger logger = Logger.getLogger( InspectionScheduleManagerImpl.class );
	
	@EJB private PersistenceManager persistenceManager;
	
	@PersistenceContext (unitName="rfidEM")
	protected EntityManager em;
	
	
	public List<InspectionSchedule> autoSchedule(Product product) {
		List<InspectionSchedule> schedules = new ArrayList<InspectionSchedule>();  
		
		ProductType productType = persistenceManager.find(ProductType.class, product.getType().getId());
		if (productType != null) {
			for (InspectionType type : productType.getInspectionTypes()) {
				ProductTypeSchedule schedule = productType.getSchedule(type, product.getOwner());
				if (schedule != null && schedule.isAutoSchedule()) {
					InspectionSchedule inspectionSchedule = new InspectionSchedule(product, type);
					inspectionSchedule.setNextDate(productType.getSuggestedNextInspectionDate(new Date(), type, product.getOwner()));
					schedules.add(inspectionSchedule);
					new InspectionScheduleServiceImpl(persistenceManager).updateSchedule(inspectionSchedule);
				}
			}
		}
		logger.info("auto scheduled for product " + product);
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
	
	public void removeAllSchedulesFor(Product product) {
		for (InspectionSchedule schedule : getAvailableSchedulesFor(product)) {
			persistenceManager.delete(schedule);
		}
	}
	
	public void removeAllSchedulesFor(ProductType productType, InspectionType inspectionType) {
		List<Long> inspectionIds = getAvailableScheduleIdsFor(productType, inspectionType);
		if (!inspectionIds.isEmpty()) {
			String jpql = "DELETE " + InspectionSchedule.class.getName() + " WHERE id IN (:ids)";
			Query query = em.createQuery(jpql);
			query.setParameter("ids", inspectionIds);
			query.executeUpdate();
		}
	}
	
	private List<Long> getAvailableScheduleIdsFor(ProductType productType, InspectionType inspectionType) {
		QueryBuilder<Long> query = new QueryBuilder<Long>(InspectionSchedule.class, new OpenSecurityFilter());
		query.setSimpleSelect("id");
		query.addSimpleWhere("product.type", productType).addSimpleWhere("inspectionType", inspectionType);
		query.addWhere(Comparator.NE, "status", "status", ScheduleStatus.COMPLETED);
		return persistenceManager.findAll(query);
	}
	
	
	public void create(ProductTypeSchedule schedule) {
		persistenceManager.save(schedule);
	}
	
	public ProductTypeSchedule update(ProductTypeSchedule schedule) {
		return persistenceManager.update(schedule);
	}
	
	public void remove(ProductTypeSchedule schedule) {
		persistenceManager.delete(schedule);
	}
	
	public List<InspectionScheduleCount> getInspectionScheduleCount(Date fromDate, Date toDate, Long tenantId) {
		return getInspectionScheduleCount(fromDate, toDate, new TenantOnlySecurityFilter(tenantId));
	}
	
	@SuppressWarnings("unchecked")
	public List<InspectionScheduleCount> getInspectionScheduleCount(Date fromDate, Date toDate, SecurityFilter secFilter) {
		String jpql = "select new " + InspectionScheduleCount.class.getName() + "(i.nextDate, i.product.owner.name, i.product.type.name, count(*)) " +
				"from " + InspectionSchedule.class.getName() + " i " +
				"where " + secFilter.produceWhereClause(InspectionSchedule.class, "i") + " and i.nextDate >= :fromDate and i.nextDate < :toDate AND state = :activeState AND status != :completedStatus " + 
				"group by i.nextDate, i.product.owner.name, i.product.type.name " + 
				"order by i.nextDate asc";
				
		Query query = em.createQuery(jpql);
		
		secFilter.applyParameters(query, InspectionSchedule.class);
		query.setParameter("fromDate", fromDate);
		query.setParameter("toDate", toDate);
		query.setParameter("activeState", EntityState.ACTIVE);
		query.setParameter("completedStatus", ScheduleStatus.COMPLETED);
		
		return query.getResultList();
	}

	public InspectionSchedule getNextScheduleFor(Long productId, Long typeId) {
		InspectionSchedule schedule = null;
		
		QueryBuilder<InspectionSchedule> query = new QueryBuilder<InspectionSchedule>(InspectionSchedule.class, new OpenSecurityFilter());
		query.addSimpleWhere("product.id", productId).addWhere(Comparator.NE, "status", "status", ScheduleStatus.COMPLETED).addSimpleWhere("inspectionType.id", typeId);
		query.addOrder("nextDate");
		
		List<InspectionSchedule> schedules = query.getResultList(em, 1, 1);
			
		if (!schedules.isEmpty()) {
			schedule = schedules.get(0);
		}

		return schedule;
	}
	
	public List<InspectionSchedule> getAvailableSchedulesFor(Product product) {
		QueryBuilder<InspectionSchedule> query = new QueryBuilder<InspectionSchedule>(InspectionSchedule.class, new OpenSecurityFilter());
		query.addSimpleWhere("product", product).addWhere(Comparator.NE, "status", "status", ScheduleStatus.COMPLETED);
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
	
	public Long getProductIdForSchedule(Long scheduleId) {
		QueryBuilder<Long> builder = new QueryBuilder<Long>(InspectionSchedule.class, new OpenSecurityFilter());
		builder.setSimpleSelect("product.id");
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
	
	/**
	 * gets the list of schedules from the past that are not closed and into the future only INPSECTION_SCHEDULE_DATE_RANGE days
	 *  
	 */
	public List<InspectionSchedule> getSchedulesInTimeFrame(Product product, InspectionType inspectionType, Date inspectionDate) {
		
		Date to = DateHelper.addDaysToDate(inspectionDate, INSPECTION_SCHEDULE_DATE_RANGE);
		
		QueryBuilder<InspectionSchedule> query = new QueryBuilder<InspectionSchedule>(InspectionSchedule.class, new OpenSecurityFilter());
		query.addSimpleWhere("product", product).addSimpleWhere("inspectionType", inspectionType);
		query.addWhere(Comparator.NE, "status", "status", ScheduleStatus.COMPLETED);
		query.addWhere(Comparator.LE, "to", "nextDate", to);
		query.addOrder("nextDate");
		
		return persistenceManager.findAll(query);
	}
}
