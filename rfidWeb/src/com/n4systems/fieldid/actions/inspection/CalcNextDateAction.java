package com.n4systems.fieldid.actions.inspection;

import java.util.Date;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.model.ProductTypeSchedule;
import com.n4systems.model.producttype.ProductTypeScheduleLoader;
import com.n4systems.model.utils.PlainDate;
import com.n4systems.util.DateHelper;

public class CalcNextDateAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	private Long ownerId;
	private Long productTypeId;
	private Long inspectionTypeId;
	private Date startDate;
	private Date nextDate;
	
	public CalcNextDateAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	public String doCalculate() {
		ProductTypeScheduleLoader loader = getLoaderFactory().createProductTypeScheduleLoader();
		loader.setOwner(ownerId);
		loader.setInspectionTypeId(inspectionTypeId);
		loader.setProductTypeId(productTypeId);
		
		ProductTypeSchedule schedule = loader.load();
		
		if (schedule != null) {
			if (startDate == null) {
				startDate = DateHelper.getToday();
			}
			
			nextDate = new PlainDate(schedule.getNextDate(startDate));
		}
		
		return SUCCESS;
	}
	
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	
	public void setProductTypeId(Long productTypeId) {
		this.productTypeId = productTypeId;
	}

	public void setInspectionTypeId(Long inspectionTypeId) {
		this.inspectionTypeId = inspectionTypeId;
	}

	public void setStartDate(String startDate) {
		this.startDate = convertDate(startDate);
	}

	public String getNextDate() {
		return convertDate(nextDate);
	} 
	
}
