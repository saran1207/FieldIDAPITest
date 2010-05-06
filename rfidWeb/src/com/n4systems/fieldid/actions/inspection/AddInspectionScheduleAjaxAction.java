package com.n4systems.fieldid.actions.inspection;

import java.util.Date;

import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Product;
import com.n4systems.model.ProductTypeSchedule;
import com.n4systems.model.Project;
import com.n4systems.util.DateHelper;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;

public class AddInspectionScheduleAjaxAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private InspectionType inspectionType;
	private Product product;
	private Project job;
	private String date;
	private Long index;
	private String inspectionDate;
	
	public AddInspectionScheduleAjaxAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	public String doAdd() {
		return SUCCESS;
	}
	
	@SkipValidation
	public String doAutoSuggest() {
		Date startDate = convertDate(inspectionDate);
		
		if (startDate == null) {
			startDate = DateHelper.getToday();
		}
		
		ProductTypeSchedule schedule = product.getType().getSchedule(inspectionType, product.getOwner());
		if (schedule != null) {
			Date nextDate = schedule.getNextDate(startDate);
			date = convertDate(nextDate);
		}
		return SUCCESS;
	}

	public InspectionType getType() {
		return inspectionType;
	}
	
	public Long getInspectionType() {
		return inspectionType.getId();
	}

	public void setInspectionType(Long id) {
		inspectionType = persistenceManager.find(InspectionType.class, id, getTenantId());
	}

	public Long getProduct() {
		return (product != null) ? product.getId(): null;
	}

	public void setProduct(Long id) {
		product = getLoaderFactory().createFilteredIdLoader(Product.class).setId(id).setPostFetchFields("type.inspectionTypes").load();
	}

	public Project getJob() {
		return job;
	}
	
	public Long getJobId() {
		return (product != null) ? product.getId(): null;
	}

	public void setJobId(Long id) {
		job = getLoaderFactory().createFilteredIdLoader(Project.class).setId(id).load();
	}
	
	public String getDate() {
		return date;
	}

	@RequiredStringValidator(message="", key="error.mustbeadate")
	@CustomValidator(type = "n4systemsDateValidator", message = "", key = "error.mustbeadate")
	public void setDate(String date) {
		this.date = date;
	}

	public String getInspectionDate() {
		return inspectionDate;
	}

	public void setInspectionDate(String inspectionDate) {
		this.inspectionDate = inspectionDate;
	}

	public Long getIndex() {
		return index;
	}

	public void setIndex(Long index) {
		this.index = index;
	}
	
}
