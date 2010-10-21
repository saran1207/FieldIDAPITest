package com.n4systems.ejb.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.n4systems.ejb.NoteManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProjectManager;
import com.n4systems.exceptions.AssetAlreadyAttachedException;
import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.model.Asset;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.Project;
import com.n4systems.model.security.ManualSecurityFilter;
import com.n4systems.model.security.SecurityDefiner;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.utils.CompressedScheduleStatus;
import com.n4systems.tools.Page;
import com.n4systems.tools.Pager;


public class ProjectManagerImpl implements ProjectManager {

	
	private EntityManager em;

	
	private PersistenceManager persistenceManager;

	
	private NoteManager noteManager;

	public ProjectManagerImpl(EntityManager em) {
		this.em = em;
		this.persistenceManager = new PersistenceManagerImpl(em);
		this.noteManager = new NoteManagerImpl(em);
	}

	public int attachAsset(Asset asset, Project project, Long modifiedBy) throws AssetAlreadyAttachedException {
		persistenceManager.reattach(project);
		if (project.getAssets().contains(asset)) {
			throw new AssetAlreadyAttachedException();
		}
		project.getAssets().add(0, asset);
		project = persistenceManager.update(project, modifiedBy);
		return project.getAssets().size();
	}

	public int detachAsset(Asset asset, Project project, Long modifiedBy) {
		persistenceManager.reattach(project);
		project.getAssets().remove(asset);
		project = persistenceManager.update(project, modifiedBy);
		return project.getAssets().size();
	}

	public FileAttachment attachNote(FileAttachment note, Project project, Long modifiedBy) throws FileAttachmentException {
		return noteManager.attachNote(note, project, modifiedBy);
	}

	public int detachNote(FileAttachment note, Project project, Long modifiedBy) throws FileAttachmentException {
		return noteManager.detachNote(note, project, modifiedBy);
	}

	public Pager<Asset> getAssetsPaged(Project project, SecurityFilter userFilter, int page, int pageSize) {
		ManualSecurityFilter filter = new ManualSecurityFilter(userFilter);
		filter.setTargets("p.tenant.id", "asset.owner", null, null);
		String queryStr = "SELECT asset FROM " + Project.class.getName() + " p , IN( p.assets ) asset where p = :project AND " + filter.produceWhereClause();
		String countQueryStr = "SELECT count( asset ) FROM " + Project.class.getName() + " p , IN( p.assets ) asset where p = :project and " + filter.produceWhereClause();

		Query selectQuery = em.createQuery(queryStr).setParameter("project", project);
		filter.applyParameters(selectQuery);
		
		Query countQuery = em.createQuery(countQueryStr).setParameter("project", project);
		filter.applyParameters(countQuery);

		return new Page<Asset>(selectQuery, countQuery, page, pageSize);
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
	public List<Project> getProjectsForAsset(Asset asset, SecurityFilter filter) {
		String queryStr = "SELECT p FROM " + Project.class.getName() + " p , IN( p.assets ) asset where asset = :asset AND p.retired = false AND " + filter.produceWhereClause(Project.class, "p") ;
		Query selectQuery = em.createQuery(queryStr);
		selectQuery.setParameter("asset", asset);
		filter.applyParameters(selectQuery, Project.class);
	
		return (List<Project>)selectQuery.getResultList();
	}

	
	

	
}
