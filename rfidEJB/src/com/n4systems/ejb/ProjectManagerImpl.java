package com.n4systems.ejb;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import rfid.ejb.entity.UserBean;

import com.n4systems.exceptions.AssetAlreadyAttachedException;
import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.Product;
import com.n4systems.model.Project;
import com.n4systems.model.utils.CompressedScheduleStatus;
import com.n4systems.tools.Page;
import com.n4systems.tools.Pager;
import com.n4systems.util.SecurityFilter;

@Stateless
public class ProjectManagerImpl implements ProjectManager {
	private static final String unitName = "rfidEM";

	@PersistenceContext(unitName = unitName)
	private EntityManager em;

	@EJB
	private PersistenceManager persistenceManager;

	@EJB
	private NoteManager noteManager;

	public int attachAsset(Product asset, Project project, Long modifiedBy) throws AssetAlreadyAttachedException {
		persistenceManager.reattach(project);
		if (project.getProducts().contains(asset)) {
			throw new AssetAlreadyAttachedException();
		}
		project.getProducts().add(0, asset);
		project = persistenceManager.update(project, modifiedBy);
		return project.getProducts().size();
	}

	public int detachAsset(Product asset, Project project, Long modifiedBy) {
		persistenceManager.reattach(project);
		project.getProducts().remove(asset);
		project = persistenceManager.update(project, modifiedBy);
		return project.getProducts().size();
	}

	public FileAttachment attachNote(FileAttachment note, Project project, Long modifiedBy) throws FileAttachmentException {
		return noteManager.attachNote(note, project, modifiedBy);
	}

	public int detachNote(FileAttachment note, Project project, Long modifiedBy) throws FileAttachmentException {
		return noteManager.detachNote(note, project, modifiedBy);
	}

	public Pager<Product> getAssetsPaged(Project project, SecurityFilter filter, int page, int pageSize) {
		filter.setTargets("p.tenant.id", "asset.owner.id", "asset.division.id");
		String queryStr = "SELECT asset FROM " + Project.class.getName() + " p , IN( p.products ) asset where p = :project AND " + filter.produceWhereClause();
		String countQueryStr = "SELECT count( asset ) FROM " + Project.class.getName() + " p , IN( p.products ) asset where p = :project and " + filter.produceWhereClause();

		Query selectQuery = em.createQuery(queryStr).setParameter("project", project);
		filter.applyParamers(selectQuery);
		
		Query countQuery = em.createQuery(countQueryStr).setParameter("project", project);
		filter.applyParamers(countQuery);

		return new Page<Product>(selectQuery, countQuery, page, pageSize);
	}
	
	public Pager<InspectionSchedule> getSchedulesPaged(Project project, SecurityFilter filter, int page, int pageSize, List<InspectionSchedule.ScheduleStatus> statuses ) {
		Query countQuery = scheduleCountQuery(project, filter, statuses);
		Query selectQuery = scheduleSelectQuery(project, filter, statuses);
		return new Page<InspectionSchedule>(selectQuery, countQuery, page, pageSize);
	}
	
	private Query scheduleCountQuery(Project project, SecurityFilter filter, List<InspectionSchedule.ScheduleStatus> statuses) {
		filter.setTargets("p.tenant.id", "schedule.customer.id", "schedule.division.id");
		String countQueryStr = "SELECT count( schedule ) FROM " + Project.class.getName() + " p , IN( p.schedules ) schedule where p = :project and " + filter.produceWhereClause();
		
		if (statuses != null && !statuses.isEmpty()) {
			countQueryStr += " AND schedule.status IN (:statuses) ";
		}
		
		Query countQuery = em.createQuery(countQueryStr).setParameter("project", project);
		filter.applyParamers(countQuery);
		if (statuses != null && !statuses.isEmpty()) {
			countQuery.setParameter("statuses", statuses);
		}
		return countQuery;
		
	}
	
	private Query scheduleSelectQuery(Project project, SecurityFilter filter, List<InspectionSchedule.ScheduleStatus> statuses) {
		filter.setTargets("p.tenant.id", "schedule.customer.id", "schedule.division.id");
		String queryStr = "SELECT schedule FROM " + Project.class.getName() + " p , IN( p.schedules ) schedule where p = :project AND " + filter.produceWhereClause();
		if (statuses != null && !statuses.isEmpty()) {
			queryStr += " AND schedule.status IN (:statuses) ";
		}
		
		queryStr += " ORDER BY schedule.nextDate";
		
		Query selectQuery = em.createQuery(queryStr).setParameter("project", project);
		filter.applyParamers(selectQuery);
		if (statuses != null && !statuses.isEmpty()) {
			selectQuery.setParameter("statuses", statuses);
		}
		return selectQuery;
	}
	
	public Long getIncompleteSchedules(Project project, SecurityFilter filter) {
		Query countQuery = scheduleCountQuery(project, filter, CompressedScheduleStatus.INCOMPLETE.getScheduleStatuses());
		return (Long)(countQuery.getSingleResult());
	}
	
	public Long getCompleteSchedules(Project project, SecurityFilter filter) {
		Query countQuery = scheduleCountQuery(project, filter, CompressedScheduleStatus.COMPLETE.getScheduleStatuses());
		return (Long)(countQuery.getSingleResult());
		
	}

	public Pager<FileAttachment> getNotesPaged(Project project, int page, int pageSize) {
		String query = "SELECT note FROM " + Project.class.getName() + " p , IN( p.notes ) note where p = :project " + " ORDER BY note.created DESC ";
		String countQuery = "SELECT count( note ) FROM " + Project.class.getName() + " p , IN( p.notes ) note where p = :project ";

		Pager<FileAttachment> results = new Page<FileAttachment>(em.createQuery(query).setParameter("project", project), em.createQuery(countQuery).setParameter("project", project), page, pageSize);
		persistenceManager.postFetchFields(results.getList(), "modifiedBy.firstName");
		return results;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Project> getProjectsForAsset(Product asset, SecurityFilter filter) {
		filter.setTargets("p.tenant.id", "p.customer.id", "p.division.id");
		String queryStr = "SELECT p FROM " + Project.class.getName() + " p , IN( p.products ) asset where asset = :asset AND p.retired = false AND " + filter.produceWhereClause() ;
		Query selectQuery = em.createQuery(queryStr);
		selectQuery.setParameter("asset", asset);
		filter.applyParamers(selectQuery);
	
		return (List<Project>)selectQuery.getResultList();
	}

	
	public Pager<UserBean> getResourcesPaged(Project project, SecurityFilter filter, int page, int pageSize) {
		Query countQuery = resourceCountQuery(project, filter);
		Query selectQuery = resourceSelectQuery(project, filter);
		return new Page<UserBean>(selectQuery, countQuery, page, pageSize);
	}
	
	private Query resourceCountQuery(Project project, SecurityFilter filter) {
		String countQueryStr = "SELECT count(resource) " + baseResourceQuery(filter);
		return createResourceQuery(project, filter, countQueryStr);
	}

	private Query createResourceQuery(Project project, SecurityFilter filter, String queryStr) {
		Query query = em.createQuery(queryStr).setParameter("project", project);
		filter.applyParamers(query);
		return query;
	}
	
	private Query resourceSelectQuery(Project project, SecurityFilter filter) {
		String selectQueryStr = "SELECT resource " + baseResourceQuery(filter) + " ORDER BY resource.firstName, resource.lastName"; 
		return createResourceQuery(project, filter, selectQueryStr);
	}

	private String baseResourceQuery(SecurityFilter filter) {
		filter.setTargets("p.tenant.id");
		String queryStr = "FROM " + Project.class.getName() + " p, IN( p.resources ) resource where p = :project AND " + filter.produceWhereClause();
		return queryStr;
	}
	
}
