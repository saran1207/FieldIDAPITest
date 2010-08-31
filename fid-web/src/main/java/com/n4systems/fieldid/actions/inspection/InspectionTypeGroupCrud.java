package com.n4systems.fieldid.actions.inspection;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.web.helper.Constants;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.fieldid.actions.api.AbstractPaginatedCrud;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.validators.HasDuplicateValueValidator;
import com.n4systems.model.InspectionType;
import com.n4systems.model.InspectionTypeGroup;
import com.n4systems.model.PrintOut;
import com.n4systems.model.PrintOut.PrintOutType;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.security.Permissions;
import com.n4systems.util.persistence.QueryBuilder;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;

@UserPermissionFilter(userRequiresOneOf={Permissions.ManageSystemConfig})
public class InspectionTypeGroupCrud extends AbstractPaginatedCrud<InspectionTypeGroup> implements HasDuplicateValueValidator{
	
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(InspectionTypeGroupCrud.class);
	
	private InspectionTypeGroup inspectionTypeGroup;
	private List<InspectionType> inspectionTypes;
	private List<PrintOut> certPrintOuts;
	private List<PrintOut> observationPrintOuts;
	
	
	public InspectionTypeGroupCrud(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@Override
	protected void initMemberFields() {
		inspectionTypeGroup = new InspectionTypeGroup();
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		inspectionTypeGroup = persistenceManager.find(InspectionTypeGroup.class, uniqueId, getTenantId());
	}
		
	private void testRequiredEntities(boolean existing) {
		if (inspectionTypeGroup == null) {
			logger.info(getLogLinePrefix() + " could not load the requested event type :" + ((uniqueID != null) ? uniqueID.toString() : "null"));
			addActionErrorText("error.noeventtypegroup");
			throw new MissingEntityException();
		} else if (existing && inspectionTypeGroup.isNew()) {
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
			QueryBuilder<InspectionTypeGroup> query = new QueryBuilder<InspectionTypeGroup>(InspectionTypeGroup.class, getSecurityFilter());
			query.addOrder("name");
			page = persistenceManager.findAllPaged(query, getCurrentPage(), Constants.PAGE_SIZE);
		} catch (Exception e) {
			logger.error("Could not load the list of inspection types", e);
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
		inspectionTypeGroup.setTenant(getTenant());
		try {
			uniqueID = persistenceManager.save(inspectionTypeGroup);
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
			inspectionTypeGroup = persistenceManager.update(inspectionTypeGroup, getSessionUser().getId());
			addFlashMessageText("message.eventtypegroupsaved");
			return SUCCESS;
		} catch (Exception e) {
			addActionErrorText("error.savingeventtypegroup");
			logger.error("failed to updating event type group " + inspectionTypeGroup.getName(), e);
		}
		return ERROR;
	}

	@SkipValidation
	public String doDelete() {
		testRequiredEntities(true);
		if (canBeDeleted(inspectionTypeGroup)) {
			try {
				persistenceManager.delete(inspectionTypeGroup);
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
	
	public boolean canBeDeleted(InspectionTypeGroup group) {
		if (group.equals(inspectionTypeGroup)) {
			return getInspectionTypes().isEmpty();
		} else {
			QueryBuilder<Long> inspectionTypeCountQuery = new QueryBuilder<Long>(InspectionType.class, getSecurityFilter());
			inspectionTypeCountQuery.setCountSelect().addSimpleWhere("group", group);
			return (persistenceManager.findCount(inspectionTypeCountQuery) == 0);
		}
	}
	
	public boolean duplicateValueExists(String formValue) {
		return !persistenceManager.uniqueNameAvailable(InspectionTypeGroup.class, formValue.trim(), uniqueID, getTenantId());
	}

	public String getName() {
		return inspectionTypeGroup.getName();
	}

	public String getReportTitle() {
		return inspectionTypeGroup.getReportTitle();
	}
	
	@RequiredStringValidator(message="", key="error.namerequired")
	@CustomValidator(type="uniqueValue", message = "", key="error.eventtypegroupnameduplicated")
	public void setName(String name) {
		inspectionTypeGroup.setName(name);
	}
	
	@RequiredStringValidator(message="", key="error.reporttitlerequired")
	public void setReportTitle(String reportTitle) {
		inspectionTypeGroup.setReportTitle(reportTitle);
	}

	public InspectionTypeGroup getGroup() {
		return inspectionTypeGroup;
	}

	
	public List<InspectionType> getInspectionTypes() {
		if (inspectionTypes == null) {
			QueryBuilder<InspectionType> query = new QueryBuilder<InspectionType>(InspectionType.class, getSecurityFilter());
			query.addSimpleWhere("group", inspectionTypeGroup).addOrder("name");
			inspectionTypes = persistenceManager.findAll(query);
		}
		return inspectionTypes;
	}

	public Long getCertPrintOutId() {
		return (inspectionTypeGroup.getPrintOut() != null) ? inspectionTypeGroup.getPrintOut().getId() : 0L;
	}

	public void setCertPrintOutId(Long printOut) {
		if (printOut == null || printOut.equals(0L)) {
			inspectionTypeGroup.setPrintOut(null);
		} else if (inspectionTypeGroup.getPrintOut() == null || !printOut.equals(inspectionTypeGroup.getPrintOut().getId())) {
			inspectionTypeGroup.setPrintOut(persistenceManager.find(PrintOut.class, printOut));
		}
	}
	
	public Long getObservationPrintOutId() {
		return (inspectionTypeGroup.getObservationPrintOut() != null) ? inspectionTypeGroup.getObservationPrintOut().getId() : 0L;
	}

	public void setObservationPrintOutId(Long printOut) {
		if (printOut == null || printOut.equals(0L)) {
			inspectionTypeGroup.setObservationPrintOut(null);
		} else if (inspectionTypeGroup.getObservationPrintOut() == null || !printOut.equals(inspectionTypeGroup.getObservationPrintOut().getId())) {
			inspectionTypeGroup.setObservationPrintOut(persistenceManager.find(PrintOut.class, printOut));
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
