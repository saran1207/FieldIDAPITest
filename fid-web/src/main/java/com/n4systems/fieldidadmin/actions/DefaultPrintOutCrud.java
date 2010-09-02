package com.n4systems.fieldidadmin.actions;

import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.model.InspectionTypeGroup;
import com.n4systems.model.PrintOut;
import com.n4systems.model.PrintOut.PrintOutType;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.tools.Pager;
import com.n4systems.util.persistence.QueryBuilder;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;

public class DefaultPrintOutCrud extends AbstractAdminAction implements Preparable {

	private static final long serialVersionUID = 1L;

	protected Long uniqueID;
	
	protected PrintOut printOut;
	
	protected Pager<PrintOut> page;
	protected Integer pageNumber;

	
	public void prepare() throws Exception {
		if (uniqueID == null) {
			printOut = new PrintOut();
			printOut.setType(PrintOut.PrintOutType.CERT);
		} else {
			printOut = persistenceEJBContainer.find(PrintOut.class, uniqueID);
		}
	}
		
	protected void testRequiredEntities(boolean existing) {
		if (printOut == null) {
			throw new RuntimeException();
		} else if (existing && printOut.isNew()) {
			throw new RuntimeException();
		}
	}

	@SkipValidation
	public String doList() {
		QueryBuilder<PrintOut> queryBuilder = new QueryBuilder<PrintOut>(PrintOut.class, new OpenSecurityFilter());
		queryBuilder.addSimpleWhere("custom", false);
		queryBuilder.addOrder("type");
		queryBuilder.addOrder("name");
		page = persistenceEJBContainer.findAllPaged(queryBuilder, getPageNumber(), 20);
		return SUCCESS;
	}

	@SkipValidation
	public String doAdd() {
		testRequiredEntities(false);
		return SUCCESS;
	}

	public String doCreate() {
		testRequiredEntities(false);
		printOut.setTenant(null);
		printOut.setCustom(false);
		try {
			persistenceEJBContainer.save(printOut);
		} catch (Exception e) {
			addActionError("could not save print out.");
			return ERROR;
		}
		return SUCCESS;
	}

	@SkipValidation
	public String doEdit() {
		testRequiredEntities(true);
		return SUCCESS;
	}

	public String doUpdate() {
		testRequiredEntities(true);
		try {
			persistenceEJBContainer.update(printOut);
		} catch (Exception e) {
			addActionError("could not save print out.");
			return ERROR;
		}
		return SUCCESS;
	}

	@SkipValidation
	public String doDelete() {
		testRequiredEntities(true);
		QueryBuilder<InspectionTypeGroup> printOutUsedQuery = new QueryBuilder<InspectionTypeGroup>(InspectionTypeGroup.class, new OpenSecurityFilter());
		printOutUsedQuery.addSimpleWhere("printOut", printOut);
		Long numberOfInspectionGroupsUsingPrintOut = persistenceEJBContainer.findCount(printOutUsedQuery);
		if (numberOfInspectionGroupsUsingPrintOut != 0 ) {
			addActionError("this can not be deleted, it is being used by " + numberOfInspectionGroupsUsingPrintOut);
			return INPUT;
		}
		try {
			persistenceEJBContainer.delete(printOut);
		} catch (Exception e) {
			addActionError("could not remove the print out");
			return ERROR;
		}
		return SUCCESS;
	}

	public Long getUniqueID() {
		return uniqueID;
	}

	public void setUniqueID(Long uniqueID) {
		this.uniqueID = uniqueID;
	}

	
	public Integer getPageNumber() {
		if( pageNumber == null ) {
			pageNumber = 1;
		}
		return pageNumber;
	}
	
	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber; 
	}

	public PrintOut getPrintOut() {
		return printOut;
	}

	public Pager<PrintOut> getPage() {
		return page;
	}

	public PrintOutType[] getPrintOutTypes() {
		return PrintOutType.values();
	}
	
	public String getPrintOutType() {
		return printOut.getType().name();
	}
	
	@RequiredStringValidator(message="you must choose a print out type.")
	public void setPrintOutType(String printOutName) {
		try {
			PrintOutType type = PrintOutType.valueOf(printOutName);
			printOut.setType(type);
		} catch (IllegalArgumentException e) {
			printOut.setType(null);
		}
	}
	
}
