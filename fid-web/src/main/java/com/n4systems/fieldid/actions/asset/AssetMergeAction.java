package com.n4systems.fieldid.actions.asset;

import java.util.Date;
import java.util.List;

import com.n4systems.ejb.AssetManager;
import com.n4systems.ejb.legacy.LegacyAsset;
import com.n4systems.fieldid.actions.asset.helpers.AssetLinkedHelper;
import com.n4systems.fieldid.actions.helpers.AllEventHelper;
import com.n4systems.model.Asset;
import com.n4systems.model.ThingEvent;
import com.n4systems.model.eventschedule.NextEventScheduleLoader;
import org.apache.struts2.interceptor.validation.SkipValidation;


import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.Event;
import com.n4systems.security.Permissions;
import com.n4systems.taskscheduling.TaskExecutor;
import com.n4systems.taskscheduling.task.AssetMergeTask;

@UserPermissionFilter(userRequiresOneOf={Permissions.Tag})
public class AssetMergeAction extends AbstractCrud {

	private static final long serialVersionUID = 1L;
	
	private final AssetManager assetManager;
	private final LegacyAsset legacyAssetManager;
	
	private AllEventHelper allEventHelper;
	
	private Asset losingAsset;
	private Asset winningAsset;
	

	public AssetMergeAction(PersistenceManager persistenceManager, AssetManager assetManager, LegacyAsset legacyAssetManager) {
		super(persistenceManager);
		this.assetManager = assetManager;
		this.legacyAssetManager = legacyAssetManager;
	}


	@Override
	protected void initMemberFields() {
		losingAsset = new Asset();
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		losingAsset = assetManager.findAssetAllFields(uniqueId, getSecurityFilter());
	}
	
	private void testRequiredEntities() {
		if (losingAsset == null || losingAsset.isNew()) {
			addActionErrorText("error.noasset");
			throw new MissingEntityException();
		}
	}

	@SkipValidation
	public String doShow() {
		testRequiredEntities();
		return SUCCESS;
	}

	
	@SkipValidation
	public String doAdd() {
		testRequiredEntities();
		return SUCCESS;
	}

	
	public String doCreate() {
		testRequiredEntities();
		if (winningAsset == null) {
			addActionErrorText("error.you_must_choose_a_valid_asset_to_merge_into");
			return INPUT;
		}
		
		AssetMergeTask task = new AssetMergeTask(winningAsset, losingAsset, fetchCurrentUser());
		TaskExecutor.getInstance().execute(task);
		
		return SUCCESS;
	}


	public Asset getLosingAsset() {
		return losingAsset;
	}
	
	public Asset getAsset() {
		return losingAsset;
	}


	public Asset getWinningAsset() {
		return winningAsset;
	}
	
	public Long getWinngingAssetId() {
		return (winningAsset != null) ? winningAsset.getId() : null;
	}
		
	public void setWinningAssetId(Long assetId) {
		if (assetId == null) {
			winningAsset = null;
		} else if (winningAsset == null || !assetId.equals(winningAsset.getId())){
			winningAsset = assetManager.findAssetAllFields(assetId, getSecurityFilter());
		}
	}

	public AllEventHelper getAllEventHelper() {
		if (allEventHelper == null)
			allEventHelper = new AllEventHelper(legacyAssetManager, losingAsset, getSecurityFilter());
		return allEventHelper;
	}
	
	public Long getExcludeId() {
		return getUniqueID();
	}

	
	public Long getEventCount() {
		return getAllEventHelper().getEventCount();
	}

	public List<ThingEvent> getEvents() {
		return getAllEventHelper().getEvents();
	}

	public Event getLastEvent() {
		return getAllEventHelper().getLastEvent();
	}
	
	public Long getLocalEventCount() {
		return getAllEventHelper().getLocalEventCount();
	}
	
	public boolean isLinked() {
		return AssetLinkedHelper.isLinked(losingAsset, getLoaderFactory());
	}

    @Override
    public String getIEHeader() {
        return "EmulateIE8";
    }

    public Date getNextScheduledEventDate(Long id) {
        Event schedule = new NextEventScheduleLoader().setAssetId(id).load();
        return schedule==null ? null : schedule.getDueDate();
    }
}
