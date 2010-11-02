package com.n4systems.fieldid.actions.asset;

import java.util.List;

import com.n4systems.ejb.AssetManager;
import com.n4systems.ejb.legacy.LegacyAsset;
import com.n4systems.fieldid.actions.asset.helpers.AssetLinkedHelper;
import com.n4systems.model.Asset;
import org.apache.struts2.interceptor.validation.SkipValidation;


import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.helpers.AllInspectionHelper;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.Event;
import com.n4systems.security.Permissions;
import com.n4systems.taskscheduling.TaskExecutor;
import com.n4systems.taskscheduling.task.AssetMergeTask;

@UserPermissionFilter(userRequiresOneOf={Permissions.Tag})
public class AssetMergeAction extends AbstractCrud {

	private static final long serialVersionUID = 1L;
	
	private final AssetManager assetManager;
	private final LegacyAsset legacyProductManager;
	
	private AllInspectionHelper allInspectionHelper;
	
	private Asset losingAsset;
	private Asset winningAsset;
	

	public AssetMergeAction(PersistenceManager persistenceManager, AssetManager assetManager, LegacyAsset legacyAssetManager) {
		super(persistenceManager);
		this.assetManager = assetManager;
		this.legacyProductManager = legacyAssetManager;
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
		
	public void setWinningAssetId(Long productId) {
		if (productId == null) {
			winningAsset = null;
		} else if (winningAsset == null || !productId.equals(winningAsset.getId())){
			winningAsset = assetManager.findAssetAllFields(productId, getSecurityFilter());
		}
	}

	public AllInspectionHelper getAllInspectionHelper() {
		if (allInspectionHelper == null)
			allInspectionHelper = new AllInspectionHelper(legacyProductManager, losingAsset, getSecurityFilter());
		return allInspectionHelper;
	}
	
	public Long getExcludeId() {
		return getUniqueID();
	}

	
	public Long getInspectionCount() {
		return getAllInspectionHelper().getInspectionCount();
	}

	public List<Event> getInspections() {
		return getAllInspectionHelper().getInspections();
	}

	public Event getLastInspection() {
		return getAllInspectionHelper().getLastInspection();
	}
	
	public Long getLocalInspectionCount() {
		return getAllInspectionHelper().getLocalInspectionCount();
	}
	
	public boolean isLinked() {
		return AssetLinkedHelper.isLinked(losingAsset, getLoaderFactory());
	}
}
