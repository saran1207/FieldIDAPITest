package com.n4systems.fieldid.actions.inspection;

import java.util.Date;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Product;
import com.n4systems.model.ProductTypeSchedule;
import com.n4systems.util.DateHelper;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;

public class AddInspectionScheduleAjaxAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private InspectionType inspectionType;
	private Product product;
	private String date;
	private Long index;
	
	public AddInspectionScheduleAjaxAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	public String doAdd() {
		return SUCCESS;
	}
	
	public String doAutoSuggest() {
		ProductTypeSchedule schedule = product.getType().getSchedule(inspectionType, product.getOwner());
		if (schedule != null) {
			Date nextDate = schedule.getNextDate(DateHelper.getToday());
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

	public String getDate() {
		return date;
	}

	@CustomValidator(type = "n4systemsDateValidator", message = "", key = "error.mustbeadate")
	public void setDate(String date) {
		this.date = date;
	}

	public Long getIndex() {
		return index;
	}

	public void setIndex(Long index) {
		this.index = index;
	}
	
}
