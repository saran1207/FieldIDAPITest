package com.n4systems.fieldid.actions.asset;

import java.util.List;

import com.n4systems.model.Asset;
import org.apache.struts2.interceptor.validation.SkipValidation;


import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.ejb.legacy.LegacyProductSerial;
import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.helpers.AllInspectionHelper;
import com.n4systems.fieldid.actions.product.helpers.ProductLinkedHelper;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.Inspection;
import com.n4systems.security.Permissions;
import com.n4systems.taskscheduling.TaskExecutor;
import com.n4systems.taskscheduling.task.AssetMergeTask;

@UserPermissionFilter(userRequiresOneOf={Permissions.Tag})
public class AssetMergeAction extends AbstractCrud {

	private static final long serialVersionUID = 1L;
	
	private final ProductManager productManager;
	private final LegacyProductSerial legacyProductManager;
	
	private AllInspectionHelper allInspectionHelper;
	
	private Asset losingAsset;
	private Asset winningAsset;
	

	public AssetMergeAction(PersistenceManager persistenceManager, ProductManager productManager, LegacyProductSerial legacyProductSerialManager) {
		super(persistenceManager);
		this.productManager = productManager;
		this.legacyProductManager = legacyProductSerialManager;
	}


	@Override
	protected void initMemberFields() {
		losingAsset = new Asset();
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		losingAsset = productManager.findAssetAllFields(uniqueId, getSecurityFilter());
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
			winningAsset = productManager.findAssetAllFields(productId, getSecurityFilter());
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

	public List<Inspection> getInspections() {
		return getAllInspectionHelper().getInspections();
	}

	public Inspection getLastInspection() {
		return getAllInspectionHelper().getLastInspection();
	}
	
	public Long getLocalInspectionCount() {
		return getAllInspectionHelper().getLocalInspectionCount();
	}
	
	public boolean isLinked() {
		return ProductLinkedHelper.isLinked(losingAsset, getLoaderFactory());
	}
}
