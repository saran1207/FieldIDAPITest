
package com.n4systems.fieldid.actions.notifications;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.utils.StrutsListHelper;
import com.n4systems.model.api.Listable;
import com.n4systems.model.common.RelativeTime;
import com.n4systems.model.common.SimpleFrequency;
import com.n4systems.model.inspectiontype.InspectionTypeListableLoader;
import com.n4systems.model.notificationsettings.NotificationSetting;
import com.n4systems.model.notificationsettings.NotificationSettingByUserListLoader;
import com.n4systems.model.notificationsettings.NotificationSettingSaver;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.producttype.ProductTypeListableLoader;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.util.persistence.SimpleListable;
import com.opensymphony.xwork2.validator.annotations.VisitorFieldValidator;

public class NotificationSettingsCrud extends AbstractCrud {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(NotificationSettingsCrud.class);
	
	// these are the relative times that will be used in each period list (in order)
	private static final RelativeTime[] startTimes = {RelativeTime.TODAY, RelativeTime.TOMORROW, RelativeTime.THIS_WEEK, RelativeTime.NEXT_WEEK, RelativeTime.THIS_MONTH, RelativeTime.NEXT_MONTH};
	private static final RelativeTime[] endTimes = {RelativeTime.DAY_1, RelativeTime.DAY_2, RelativeTime.WEEK_1, RelativeTime.WEEK_2, RelativeTime.MONTH_1, RelativeTime.MONTH_2, RelativeTime.MONTH_3};

	private List<Listable<String>> periodStartList;
	private List<Listable<String>> periodEndList;
	private List<Listable<Long>> productTypeList;
	private List<Listable<Long>> inspectionTypeList;
	private List<FrequencyGroupView> frequencyGroups;
	
	private List<NotificationSetting> settingsList;
	private NotificationSettingViewModelConverter converter;
	private NotificationSettingView view = new NotificationSettingView();
	
	public NotificationSettingsCrud(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}
	
	private void initPeriodList(List<Listable<String>> periodList, RelativeTime[] allowedTimes) {
		// populate the period list with our allowed period times and resolve the label
		for (RelativeTime time: allowedTimes) {
			periodList.add(new SimpleListable<String>(time.getId(), getText(time.getDisplayName())));
		}
	}

	private void initFrequencyGroups() {
		frequencyGroups = new ArrayList<FrequencyGroupView>();
		
	    // build a list of frequency group views, these are used in the frequency select option groups
		// we'll create a temporary map to help with grouping.  Note it's LinkedHashMap so it itterates in order
		Map<String, FrequencyGroupView> freqMap = new LinkedHashMap<String, FrequencyGroupView>();
		for (SimpleFrequency freq: SimpleFrequency.values()) {
			// note that all labels, in the frequency will need to be resolved in to their real text
			if (!freqMap.containsKey(freq.getGroupLabel())) {
				freqMap.put(freq.getGroupLabel(), new FrequencyGroupView(getText(freq.getGroupLabel())));
			}
			
			// in one, step we get the group, create a simple listable from our frequency and resolve the label text
			freqMap.get(freq.getGroupLabel()).getFrequencies().add(new SimpleListable<String>(freq.getId(), getText(freq.getDisplayName())));
		}
		// populate our frequency groups and discard the map
		frequencyGroups.addAll(freqMap.values());
		freqMap = null;
    }

	@Override
	protected void initMemberFields() {
		initConverter();
	}
	
	@Override
	protected void loadMemberFields(Long uniqueId) {
		initConverter();
	}
	
	private void initConverter() {
		converter = new NotificationSettingViewModelConverter(getTenant(), getUser());
	}
	
	@SkipValidation
	public String doList() {
		return SUCCESS;
	}
	
	@SkipValidation
	public String doAdd() {
		converter.populateView(new NotificationSetting(getTenant(), getSessionUserOwner()), view);
		
		// default to the users email address
		view.getAddresses().add(getUser().getEmailAddress());
		
		return SUCCESS;
	}
	
	@SkipValidation
	public String doEdit() {
		FilteredIdLoader<NotificationSetting> settingLoader = getLoaderFactory().createFilteredIdLoader(NotificationSetting.class);
		settingLoader.setId(getUniqueID());
		
		NotificationSetting setting = settingLoader.load();
		
		if (setting == null) {
			addActionErrorText("error.nonotificationsetting");
			return MISSING;
		}
		
		converter.populateView(setting, view);
		
		return SUCCESS;
	}
	
	public String doSave() {
		try {
			StrutsListHelper.clearEmpties(view.getAddresses());
			
			NotificationSetting setting = new NotificationSetting();
			
			converter.populateModel(setting, view);
			
			NotificationSettingSaver saver = new NotificationSettingSaver();
			
			saver.saveOrUpdate(setting);
			
			uniqueID = setting.getId();
			
			addFlashMessageText("message.notificationsettingsaved");
			
		} catch (MissingEntityException e) {
			return MISSING;
			
		} catch (Exception e) {
			logger.error("Could not save Notification Setting", e);
			addActionErrorText("error.notificationsettingssavefailed");
			
			return ERROR;
		}
		
		// null this out so it reloads
		settingsList = null;
		
		return SUCCESS;
	}
	
	@SkipValidation
	public String doDelete() {
		try {
			NotificationSetting setting = getLoaderFactory().createFilteredIdLoader(NotificationSetting.class).setId(getUniqueID()).load();
			
			NotificationSettingSaver saver = new NotificationSettingSaver();
			
			saver.remove(setting);
			
			addFlashMessageText("message.notificationsettingdeleted");
		} catch (MissingEntityException e) {
			return MISSING;
			
		} catch (Exception e) {
			logger.error("Could not delete Inspection Schedule", e);
			addActionErrorText("error.notificationsettingdeletefailed");
			
			return ERROR;
		}
		
		// null this out so it realoads
		settingsList = null;
		
		return SUCCESS;
	}
	
	public List<NotificationSetting> getSettingsList() {
		if (settingsList == null) {
			NotificationSettingByUserListLoader loader = getLoaderFactory().createNotificationSettingByUserListLoader();
			loader.setUserId(getSessionUserId());
			settingsList = loader.load();
		}
		return settingsList;
	}
	
	public List<FrequencyGroupView> getFrequencyGroups() {
		if (frequencyGroups == null) {
			initFrequencyGroups();
		}
		return frequencyGroups;
	}
	
	public List<Listable<String>> getPeriodStartList() {
		if (periodStartList == null) {
			periodStartList = new ArrayList<Listable<String>>();
			initPeriodList(periodStartList, startTimes);
		}		
    	return periodStartList;
    }

	public List<Listable<String>> getPeriodEndList() {
		if (periodEndList == null) {
			periodEndList = new ArrayList<Listable<String>>();
			initPeriodList(periodEndList, endTimes);
		}		
    	return periodEndList;
    }

	public List<Listable<Long>> getInspectionTypeList() {
		if (inspectionTypeList == null) {
			InspectionTypeListableLoader loader = getLoaderFactory().createInspectionTypeListableLoader();
			inspectionTypeList = loader.load();
		}
    	return inspectionTypeList;
    }

	public List<Listable<Long>> getProductTypeList() {
		if (productTypeList == null) {
			ProductTypeListableLoader loader = getLoaderFactory().createProductTypeListableLoader();
			productTypeList = loader.load();
		}
    	return productTypeList;
    }
	
	
	@VisitorFieldValidator(message = "")
	public NotificationSettingView getView() {
		return view;
	}
	
	public void setView(NotificationSettingView view) {
		this.view = view;
	}

	public BaseOrg getOwner() {
		return view.getOwner();
	}
	
	public Long getOwnerId() {
		return (view.getOwner() != null) ? view.getOwner().getId() : null;
	}
	
	public void setOwnerId(Long id) {
		if (id == null) {
			view.setOwner(null);
		} else if (view.getOwner() == null || !view.getOwner().getId().equals(id)) {
			view.setOwner(getLoaderFactory().createFilteredIdLoader(BaseOrg.class).setId(id).load());
		}
	}
}
