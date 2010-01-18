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
import com.n4systems.model.security.ManualSecurityFilter;
import com.n4systems.model.security.SecurityDefiner;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.utils.CompressedScheduleStatus;
import com.n4systems.tools.Page;
import com.n4systems.tools.Pager;

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

	public Pager<Product> getAssetsPaged(Project project, SecurityFilter userFilter, int page, int pageSize) {
		ManualSecurityFilter filter = new ManualSecurityFilter(userFilter);
		filter.setTargets("p.tenant.id", "asset.owner", null, null);
		String queryStr = "SELECT asset FROM " + Project.class.getName() + " p , IN( p.products ) asset where p = :project AND " + filter.produceWhereClause();
		String countQueryStr = "SELECT count( asset ) FROM " + Project.class.getName() + " p , IN( p.products ) asset where p = :project and " + filter.produceWhereClause();

		Query selectQuery = em.createQuery(queryStr).setParameter("project", project);
		filter.applyParameters(selectQuery);
		
		Query countQuery = em.createQuery(countQueryStr).setParameter("project", project);
		filter.applyParameters(countQuery);

		return new Page<Product>(selectQuery, countQuery, page, pageSize);
	}
	
	public Pager<InspectionSchedule> getSchedulesPaged(Project project, SecurityFilter filter, int page, int pageSize, List<InspectionSchedule.ScheduleStatus> statuses ) {
		Query countQuery = scheduleCountQuery(project, filter, statuses);
		Query selectQuery = scheduleSelectQuery(project, filter, statuses);
		return new Page<InspectionSchedule>(selectQuery, countQuery, page, pageSize);
	}
	
	private Query scheduleCountQuery(Project project, SecurityFilter userFilter, List<InspectionSchedule.ScheduleStatus> statuses) {
		ManualSecurityFilter filter = createManualSecurityFilter(userFilter, "schedule");
		String countQueryStr = "SELECT count( schedule ) FROM " + Project.class.getName() + " p , IN( p.schedules ) schedule where p = :project and " + filter.produceWhereClause();
		
		if (statuses != null && !statuses.isEmpty()) {
			countQueryStr += " AND schedule.status IN (:statuses) ";
		}
		
		Query countQuery = em.createQuery(countQueryStr).setParameter("project", project);
		filter.applyParameters(countQuery);
		if (statuses != null && !statuses.isEmpty()) {
			countQuery.setParameter("statuses", statuses);
		}
		return countQuery;
		
	}

	private ManualSecurityFilter createManualSecurityFilter(SecurityFilter userFilter, String scheduleTablePrefix) {
		SecurityDefiner securityDefiner = InspectionSchedule.createSecurityDefiner();
		ManualSecurityFilter filter = new ManualSecurityFilter(userFilter);
		filter.setTargets(scheduleTablePrefix + "." + securityDefiner.getTenantPath(), scheduleTablePrefix + "." + securityDefiner.getOwnerPath(), null, scheduleTablePrefix + "." + securityDefiner.getStatePath());
		return filter;
	}
	
	private Query scheduleSelectQuery(Project project, SecurityFilter userFilter, List<InspectionSchedule.ScheduleStatus> statuses) {
		ManualSecurityFilter filter = createManualSecurityFilter(userFilter, "schedule");
		String queryStr = "SELECT schedule FROM " + Project.class.getName() + " p , IN( p.schedules ) schedule where p = :project AND " + filter.produceWhereClause();
		if (statuses != null && !statuses.isEmpty()) {
			queryStr += " AND schedule.status IN (:statuses) ";
		}
		
		queryStr += " ORDER BY schedule.nextDate";
		
		Query selectQuery = em.createQuery(queryStr).setParameter("project", project);
		filter.applyParameters(selectQuery);
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
		String queryStr = "SELECT p FROM " + Project.class.getName() + " p , IN( p.products ) asset where asset = :asset AND p.retired = false AND " + filter.produceWhereClause(Project.class, "p") ;
		Query selectQuery = em.createQuery(queryStr);
		selectQuery.setParameter("asset", asset);
		filter.applyParameters(selectQuery, Project.class);
	
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
		filter.applyParameters(query, Project.class);
		return query;
	}
	
	private Query resourceSelectQuery(Project project, SecurityFilter filter) {
		String selectQueryStr = "SELECT resource " + baseResourceQuery(filter) + " ORDER BY resource.firstName, resource.lastName"; 
		return createResourceQuery(project, filter, selectQueryStr);
	}

	private String baseResourceQuery(SecurityFilter filter) {
		String queryStr = "FROM " + Project.class.getName() + " p, IN( p.resources ) resource where p = :project AND " + filter.produceWhereClause(Project.class, "p");
		return queryStr;
	}
	
}
