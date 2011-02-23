package com.n4systems.fieldid.actions.asset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.n4systems.model.AssetTypeSchedule;
import com.n4systems.model.AssociatedEventType;
import com.n4systems.model.EventType;
import com.n4systems.model.eventtype.EventFrequencySaver;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;


import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.LegacyAssetType;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.utils.OwnerPicker;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.validators.HasDuplicateValueValidator;
import com.n4systems.model.AssetType;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.persistence.Transaction;
import com.n4systems.security.Permissions;
import com.n4systems.util.persistence.QueryBuilder;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;

@UserPermissionFilter(userRequiresOneOf={Permissions.ManageSystemConfig})
public class AssetTypeScheduleCrud extends AbstractCrud implements HasDuplicateValueValidator {
	private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(AssetTypeScheduleCrud.class);

	private Long assetTypeId;
	private Long eventTypeId;
	private AssetType assetType;
	private EventType eventType;
	
	private boolean customerForm = false;
	private AssetTypeSchedule schedule;

	private List<EventType> eventTypes;
	private Map<String, AssetTypeSchedule> schedules;
	private Map<String, List<AssetTypeSchedule>> overrideSchedules;

	private LegacyAssetType assetTypeManager;
	
	private OwnerPicker ownerPicker;

	public AssetTypeScheduleCrud(LegacyAssetType assetTypeManager, PersistenceManager persistenceManager) {
		super(persistenceManager);
		this.assetTypeManager = assetTypeManager;
	}

	@Override
	protected void initMemberFields() {
		schedule = new AssetTypeSchedule();
		schedule.setTenant(getTenant());
		
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		getAssetType();
		for (AssetTypeSchedule schedule : assetType.getSchedules()) {
			if (schedule.getId().equals(uniqueId)) {
				this.schedule = schedule;
			}
		}
	}

		
	@Override
	protected void postInit() {
		super.postInit();
		ownerPicker = new OwnerPicker(getLoaderFactory().createFilteredIdLoader(BaseOrg.class), schedule);
	}

	@SkipValidation
	public String doList() {
		return SUCCESS;

	}

	@SkipValidation
	public String doEdit() {
		return INPUT;
	}

	@SkipValidation
	public String doShow() {
		return SUCCESS;
	}

	@SkipValidation
	public String doDelete() {
		Transaction transaction = com.n4systems.persistence.PersistenceManager.startTransaction();
		try {
			List<AssetTypeSchedule> schedulesToRemove = schedulesToRemove(transaction);
			removeSchedules(transaction, schedulesToRemove);
			transaction.commit();

			addFlashMessageText("message.schdeuledeleted");
		} catch (Exception e) {
            logger.error("Error deleting asset type schedule", e);
			addActionErrorText("error.failedtodelete");
			transaction.rollback();
			return ERROR;
		}
		return SUCCESS;
	}

	private void removeSchedules(Transaction transaction, List<AssetTypeSchedule> schedulesToRemove) {
		EventFrequencySaver saver = new EventFrequencySaver();
		for (AssetTypeSchedule scheduleToRemove : schedulesToRemove) {
			saver.remove(transaction, scheduleToRemove);
		}
	}

	private List<AssetTypeSchedule> schedulesToRemove(Transaction transaction) {
		List<AssetTypeSchedule> schedulesToRemove = new ArrayList<AssetTypeSchedule>();

		if (!schedule.getOwner().isExternal()) {
			schedulesToRemove.addAll(getLoaderFactory().createEventFrequenciesListLoader().setEventTypeId(eventTypeId).setAssetTypeId(assetTypeId).load(transaction));
		} else {
			schedulesToRemove.add(new QueryBuilder<AssetTypeSchedule>(AssetTypeSchedule.class, new OpenSecurityFilter()).addSimpleWhere("id", schedule.getId()).getSingleResult(
                    transaction.getEntityManager()));
		}

		return schedulesToRemove;
	}

	public String doSave() {
		getAssetType();
		getEventType();
		try {
			if (schedule.getId() == null) {
				schedule.setAssetType(assetType);
				schedule.setEventType(eventType);
				schedule.setTenant(getTenant());
				schedule.setOwner(getOwner());
				persistenceManager.save(schedule);
				assetType.getSchedules().add(schedule);
			} else {
				schedule = persistenceManager.update(schedule);
			}

			assetType = assetTypeManager.updateAssetType(assetType);
			schedule = assetType.getSchedule(schedule.getEventType(), schedule.getOwner());
		} catch (Exception e) {
			addActionError(getText("error.failedtosave"));
			return ERROR;
		}

		addFlashMessage(getText("message.schedulesaved"));
		return SUCCESS;
	}

	public Long getAssetTypeId() {
		return assetTypeId;
	}

	public void setAssetTypeId(Long assetTypeId) {
		this.assetTypeId = assetTypeId;
	}

	public AssetType getAssetType() {
		if (assetType == null) {
			assetType = getLoaderFactory().createAssetTypeLoader().setId(assetTypeId).setStandardPostFetches().load();
		}
		return assetType;
	}

	public EventType getEventType() {
		if (eventType == null) {
			eventType = persistenceManager.find(EventType.class, eventTypeId, getTenant());
		}
		return eventType;
	}

	public List<EventType> getEventTypes() {
		if (eventTypes == null) {
			eventTypes = new ArrayList<EventType>();
			List<AssociatedEventType> associatedEventTypes = getLoaderFactory().createAssociatedEventTypesLoader().setAssetType(getAssetType()).load();
			for (AssociatedEventType associatedEventType : associatedEventTypes) {
				eventTypes.add(associatedEventType.getEventType());
			}
		}
		return eventTypes;
	}

	public Map<String, AssetTypeSchedule> getSchedules() {
		if (schedules == null) {
			getAssetType();
			schedules = new HashMap<String, AssetTypeSchedule>();
			for (AssetTypeSchedule schedule : assetType.getSchedules()) {
				if (schedule.getOwner().isPrimary()) {
					schedules.put(schedule.getEventType().getName(), schedule);
				}
			}
		}
		return schedules;
	}

	public Map<String, List<AssetTypeSchedule>> getOverrideSchedules() {
		if (overrideSchedules == null) {
			getAssetType();
			overrideSchedules = new HashMap<String, List<AssetTypeSchedule>>();
			for (AssetTypeSchedule schedule : assetType.getSchedules()) {
				if (schedule.isOverride()) {
					if (overrideSchedules.get(schedule.getEventType().getName()) == null) {
						overrideSchedules.put(schedule.getEventType().getName(), new ArrayList<AssetTypeSchedule>());
					}
					overrideSchedules.get(schedule.getEventType().getName()).add(schedule);
				}
			}
		}
		return overrideSchedules;
	}

	public Long getEventTypeId() {
		return eventTypeId;
	}

	public void setEventTypeId(Long eventTypeId) {
		this.eventTypeId = eventTypeId;
	}

	public boolean isAutoSchedule() {
		return schedule.isAutoSchedule();
	}

	public void setAutoSchedule(boolean autoSchedule) {
		schedule.setAutoSchedule(autoSchedule);

	}

	public Long getFrequency() {
		return schedule.getFrequency();
	}

	@RequiredFieldValidator(message = "", key = "error.frequencyisrequired")
	public void setFrequency(Long frequency) {
		schedule.setFrequency(frequency);
	}

	public AssetTypeSchedule getSchedule() {
		return schedule;
	}

	public boolean isCustomerForm() {
		return customerForm;
	}

	public void setCustomerForm(boolean customerForm) {
		this.customerForm = customerForm;
	}

	public AssetTypeSchedule newSchedule() {
		AssetTypeSchedule newSchedule = new AssetTypeSchedule();
		newSchedule.setOwner(getPrimaryOrg());
		return newSchedule;

	}

	public boolean duplicateValueExists(String formValue) {
		if (getOwner() == null) {
			return false;
		}

		// check if there is already an override for this customer.
		getAssetType();
		getEventType();

		Map<String, List<AssetTypeSchedule>> overrideSchedules = getOverrideSchedules();
		List<AssetTypeSchedule> existingSchedules = overrideSchedules.get(eventType.getName());
		
		if (existingSchedules == null) {
			return false;
		}
		
		for (AssetTypeSchedule existingSchedule : existingSchedules) {
			if (existingSchedule.getOwner().equals(getOwner()) && !existingSchedule.equals(schedule)) {
				return true;
			}
		}

		return false;
	}

	@RequiredFieldValidator(message="", key="error.owner_required")
	@CustomValidator(type="uniqueValue", message = "", key="errors.overrideexists")
	public BaseOrg getOwner() {
		return ownerPicker.getOwner();
	}

	
	public Long getOwnerId() {
		return ownerPicker.getOwnerId();
	}

	public void setOwnerId(Long id) {
		ownerPicker.setOwnerId(id);
	}
	
	
}
