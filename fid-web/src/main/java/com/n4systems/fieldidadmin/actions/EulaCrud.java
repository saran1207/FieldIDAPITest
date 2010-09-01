package com.n4systems.fieldidadmin.actions;

import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.model.eula.EULA;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.tools.Pager;
import com.n4systems.util.persistence.QueryBuilder;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;

public class EulaCrud extends AbstractAdminAction implements Preparable {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(EulaCrud.class);

	private EULA eula;
	private Long id;

	private Pager<EULA> page;
	private Integer pageNumber;

	public EulaCrud() {
		super();
	}

	public void prepare() throws Exception {
		if (id != null) {
			eula = persistenceManager.find(EULA.class, id);
		} else {
			eula = new EULA();
		}
	}

	private void testRequiredEntities(boolean existing) {
	}

	@SkipValidation
	public String doShow() {
		testRequiredEntities(true);
		return SUCCESS;
	}

	@SkipValidation
	public String doList() {
		try {
			QueryBuilder<EULA> query = new QueryBuilder<EULA>(EULA.class, new OpenSecurityFilter()).addOrder("effectiveDate", false);
			page = persistenceManager.findAllPaged(query, getPageNumber(), 10);
			return SUCCESS;
		} catch (InvalidQueryException iqe) {
			logger.error("couldn't load the list of eulas ", iqe);
		} catch (Exception e) {
			logger.error("couldn't load the list of eulas ", e);
		}
		addActionError(getText("error.failedtoload"));
		return ERROR;
	}

	@SkipValidation
	public String doAdd() {
		testRequiredEntities(false);
		return SUCCESS;
	}

	public String doCreate() {
		testRequiredEntities(false);
		try {
			persistenceManager.save(eula);
			addActionMessage("Saved EULA");
			return SUCCESS;
		} catch (Exception e) {
			logger.error("could not save eula", e);
			addActionError("Could not save EULA");
			return ERROR;
		}
		
	}

	@SkipValidation
	public String doEdit() {
		testRequiredEntities(true);
		return SUCCESS;
	}

	public String doUpdate() {
		testRequiredEntities(true);
		try {
			persistenceManager.update(eula);
			addActionMessage("Saved EULA");
			return SUCCESS;
		} catch (Exception e) {
			logger.error("could not save eula", e);
			addActionError("Could not save EULA");
			return ERROR;
		}
	}

	@SkipValidation
	public String doDelete() {
		testRequiredEntities(true);
		return SUCCESS;
	}

	public EULA getEula() {
		return eula;
	}

	public Date getEffectiveDate() {
		return eula.getEffectiveDate();
	}

	public String getLegalText() {
		return eula.getLegalText();
	}

	public String getVersion() {
		return eula.getVersion();
	}

	@RequiredFieldValidator(message="the effective date is required")
	public void setEffectiveDate(Date effectiveDate) {
		eula.setEffectiveDate(effectiveDate);
	}

	@RequiredStringValidator(message="legal text is required")
	public void setLegalText(String legalText) {
		eula.setLegalText(legalText);
	}

	@RequiredStringValidator(message="version number is required")
	public void setVersion(String version) {
		eula.setVersion(version);
	}

	public Integer getPageNumber() {
		if (pageNumber == null) {
			pageNumber = 1;
		}
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Pager<EULA> getPage() {
		return page;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
