package com.n4systems.fieldid.actions.event;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeSchedule;
import com.n4systems.model.EventType;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.utils.PlainDate;
import com.n4systems.util.DateHelper;

import java.util.Date;

public class CalcNextDateAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	private Long ownerId;
	private Long assetTypeId;
	private Long eventTypeId;
	private Date startDate;
	private Date nextDate;
	
	public CalcNextDateAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	public String doCalculate() {
		AssetType assetType = getLoaderFactory().createAssetTypeLoader().setId(assetTypeId).load();
		
		EventType eventType = getLoaderFactory().createFilteredIdLoader(EventType.class).setId(eventTypeId).load();
		
		BaseOrg owner = getLoaderFactory().createFilteredIdLoader(BaseOrg.class).setId(ownerId).load();
		
		AssetTypeSchedule schedule = assetType.getSchedule(eventType, owner);
		
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

	public void setEventTypeId(Long eventTypeId) {
		this.eventTypeId = eventTypeId;
	}

	public void setStartDate(String startDate) {
		this.startDate = convertDate(startDate);
	}

	public String getNextDate() {
		return convertDate(nextDate);
	} 
	
}
