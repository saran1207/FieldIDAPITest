package com.n4systems.fieldid.actions.inspection;

import java.util.Date;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.model.AssetTypeSchedule;
import com.n4systems.model.InspectionType;
import com.n4systems.model.AssetType;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.utils.PlainDate;
import com.n4systems.util.DateHelper;

public class CalcNextDateAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	private Long ownerId;
	private Long assetTypeId;
	private Long inspectionTypeId;
	private Date startDate;
	private Date nextDate;
	
	public CalcNextDateAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	public String doCalculate() {
		AssetType assetType = getLoaderFactory().createAssetTypeLoader().setId(assetTypeId).load();
		
		InspectionType inspectionType = getLoaderFactory().createFilteredIdLoader(InspectionType.class).setId(inspectionTypeId).load();
		
		BaseOrg owner = getLoaderFactory().createFilteredIdLoader(BaseOrg.class).setId(ownerId).load();
		
		AssetTypeSchedule schedule = assetType.getSchedule(inspectionType, owner);
		
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
	
	public void setAssetTypeId(Long assetTypeId) {
		this.assetTypeId = assetTypeId;
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
