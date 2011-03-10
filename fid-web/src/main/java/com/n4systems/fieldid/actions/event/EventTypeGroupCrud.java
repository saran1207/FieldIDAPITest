package com.n4systems.fieldid.actions.event;

import java.util.List;

import com.n4systems.model.EventType;
import com.n4systems.model.EventTypeGroup;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.web.helper.Constants;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.fieldid.actions.api.AbstractPaginatedCrud;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.validators.HasDuplicateValueValidator;
import com.n4systems.model.PrintOut;
import com.n4systems.model.PrintOut.PrintOutType;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.security.Permissions;
import com.n4systems.util.persistence.QueryBuilder;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;

@UserPermissionFilter(userRequiresOneOf={Permissions.ManageSystemConfig})
public class EventTypeGroupCrud extends AbstractPaginatedCrud<EventTypeGroup> implements HasDuplicateValueValidator{
	
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(EventTypeGroupCrud.class);
	
	private EventTypeGroup eventTypeGroup;
	private List<EventType> eventTypes;
	private List<PrintOut> certPrintOuts;
	private List<PrintOut> observationPrintOuts;
	
	
	public EventTypeGroupCrud(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@Override
	protected void initMemberFields() {
		eventTypeGroup = new EventTypeGroup();
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		eventTypeGroup = persistenceManager.find(EventTypeGroup.class, uniqueId, getTenantId());
	}
		
	private void testRequiredEntities(boolean existing) {
		if (eventTypeGroup == null) {
			logger.info(getLogLinePrefix() + " could not load the requested event type :" + ((uniqueID != null) ? uniqueID.toString() : "null"));
			addActionErrorText("error.noeventtypegroup");
			throw new MissingEntityException();
		} else if (existing && eventTypeGroup.isNew()) {
			logger.info(getLogLinePrefix() + " event type must be loaded but no id was provided");
			addActionErrorText("error.noeventtypegroup");
			throw new MissingEntityException();
		}
	}
	
	@SkipValidation
	public String doShow() {
		testRequiredEntities(true);
		return SUCCESS;
	}

	@SkipValidation
	public String doList() {
		try {
			QueryBuilder<EventTypeGroup> query = new QueryBuilder<EventTypeGroup>(EventTypeGroup.class, getSecurityFilter());
			query.addOrder("name");
			query.addPostFetchPaths("modifiedBy", "createdBy");
			page = persistenceManager.findAllPaged(query, getCurrentPage(), Constants.PAGE_SIZE);
		} catch (Exception e) {
			logger.error("Could not load the list of event types", e);
			addActionErrorText("error.noeventtypegrouplist");
			return ERROR;
		}
		return SUCCESS;
	}

	@SkipValidation
	public String doAdd() {
		return SUCCESS;
	}

	public String doCreate() {
		testRequiredEntities(false);
		eventTypeGroup.setTenant(getTenant());
		try {
			uniqueID = persistenceManager.save(eventTypeGroup, getSessionUserId());
			addFlashMessageText("message.eventtypegroupsaved");
			return SUCCESS;
		} catch (Exception e) {
			addActionErrorText("error.savingeventtypegroup");
			logger.error("failed to save the new event type group", e);
		}
		return ERROR;
	}

	@SkipValidation
	public String doEdit() {
		testRequiredEntities(true);
		return SUCCESS;
	}

	public String doUpdate() {
		testRequiredEntities(true);
		try {
			eventTypeGroup = persistenceManager.update(eventTypeGroup, getSessionUser().getId());
			addFlashMessageText("message.eventtypegroupsaved");
			return SUCCESS;
		} catch (Exception e) {
			addActionErrorText("error.savingeventtypegroup");
			logger.error("failed to updating event type group " + eventTypeGroup.getName(), e);
		}
		return ERROR;
	}

	@SkipValidation
	public String doDelete() {
		testRequiredEntities(true);
		if (canBeDeleted(eventTypeGroup)) {
			try {
				persistenceManager.delete(eventTypeGroup);
				addFlashMessageText("message.eventtypegroupdeleted");
				return SUCCESS;
			} catch (Exception e) {
				addActionErrorText("error.deletingeventtypegroup");
				return ERROR;
			}
		} else {
			addActionErrorText("error.cantdeleteeventtypegroup");
			return INPUT;
		}
		
	}
	
	public boolean canBeDeleted(EventTypeGroup group) {
		QueryBuilder<Long> eventTypeCountQuery = new QueryBuilder<Long>(EventType.class, new TenantOnlySecurityFilter(getSecurityFilter()).setShowArchived(true));
		eventTypeCountQuery.setCountSelect().addSimpleWhere("group", group);
		
		if (group.equals(eventTypeGroup)) {
			return getEventTypes().isEmpty() && persistenceManager.findCount(eventTypeCountQuery) == 0;
		} else {
			return (  persistenceManager.findCount(eventTypeCountQuery) == 0);
		}
	}
	
	public boolean duplicateValueExists(String formValue) {
		return !persistenceManager.uniqueNameAvailable(EventTypeGroup.class, formValue.trim(), uniqueID, getTenantId());
	}

	public String getName() {
		return eventTypeGroup.getName();
	}

	public String getReportTitle() {
		return eventTypeGroup.getReportTitle();
	}
	
	@RequiredStringValidator(message="", key="error.namerequired")
	@CustomValidator(type="uniqueValue", message = "", key="error.eventtypegroupnameduplicated")
	public void setName(String name) {
		eventTypeGroup.setName(name);
	}
	
	@RequiredStringValidator(message="", key="error.reporttitlerequired")
	public void setReportTitle(String reportTitle) {
		eventTypeGroup.setReportTitle(reportTitle);
	}

	public EventTypeGroup getGroup() {
		return eventTypeGroup;
	}

	
	public List<EventType> getEventTypes() {
		if (eventTypes == null) {
			QueryBuilder<EventType> query = new QueryBuilder<EventType>(EventType.class, getSecurityFilter());
			query.addSimpleWhere("group", eventTypeGroup).addOrder("name");
			eventTypes = persistenceManager.findAll(query);
		}
		return eventTypes;
	}

	public Long getCertPrintOutId() {
		return (eventTypeGroup.getPrintOut() != null) ? eventTypeGroup.getPrintOut().getId() : 0L;
	}

	public void setCertPrintOutId(Long printOut) {
		if (printOut == null || printOut.equals(0L)) {
			eventTypeGroup.setPrintOut(null);
		} else if (eventTypeGroup.getPrintOut() == null || !printOut.equals(eventTypeGroup.getPrintOut().getId())) {
			eventTypeGroup.setPrintOut(persistenceManager.find(PrintOut.class, printOut));
		}
	}
	
	public Long getObservationPrintOutId() {
		return (eventTypeGroup.getObservationPrintOut() != null) ? eventTypeGroup.getObservationPrintOut().getId() : 0L;
	}

	public void setObservationPrintOutId(Long printOut) {
		if (printOut == null || printOut.equals(0L)) {
			eventTypeGroup.setObservationPrintOut(null);
		} else if (eventTypeGroup.getObservationPrintOut() == null || !printOut.equals(eventTypeGroup.getObservationPrintOut().getId())) {
			eventTypeGroup.setObservationPrintOut(persistenceManager.find(PrintOut.class, printOut));
		}
	}


	public List<PrintOut> getCertPrintOuts() {
		if (certPrintOuts == null) {
			certPrintOuts = findPrintOutsOfType(PrintOutType.CERT);
		}
		return certPrintOuts;
	}
	
	public List<PrintOut> getObservationPrintOuts() {
		if (observationPrintOuts == null) {
			observationPrintOuts = findPrintOutsOfType(PrintOutType.OBSERVATION);
		}
		return observationPrintOuts;
	}

	private List<PrintOut> findPrintOutsOfType(PrintOutType type) {
		List<PrintOut> printOuts = null;
		
		QueryBuilder<PrintOut> queryBuilder = new QueryBuilder<PrintOut>(PrintOut.class, new OpenSecurityFilter());
		queryBuilder.addSimpleWhere("type", type);
		queryBuilder.addSimpleWhere("custom", false);
		queryBuilder.addOrder("name");
		printOuts = persistenceManager.findAll(queryBuilder);
		
		QueryBuilder<PrintOut>queryBuilderCustomPrintOuts = new QueryBuilder<PrintOut>(PrintOut.class, new OpenSecurityFilter());
		queryBuilderCustomPrintOuts.addSimpleWhere("type", type);
		queryBuilderCustomPrintOuts.addSimpleWhere("custom", true);
		queryBuilderCustomPrintOuts.addSimpleWhere("tenant", getTenant());
		queryBuilder.addOrder("name");
		printOuts.addAll(persistenceManager.findAll(queryBuilderCustomPrintOuts));
		
		return printOuts;
	}

}
