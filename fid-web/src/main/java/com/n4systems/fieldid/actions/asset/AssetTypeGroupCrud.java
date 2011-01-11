package com.n4systems.fieldid.actions.asset;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.ejb.AssetManager;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;
import com.n4systems.util.AssetTypeGroupRemovalSummary;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.validators.HasDuplicateValueValidator;
import com.n4systems.security.Permissions;
import com.n4systems.util.persistence.QueryBuilder;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;

@UserPermissionFilter(userRequiresOneOf={Permissions.ManageSystemConfig})
public class AssetTypeGroupCrud extends AbstractCrud implements HasDuplicateValueValidator {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(AssetTypeGroupCrud.class);

	private AssetManager assetManager;
	
	private List<AssetTypeGroup> groups;
	private AssetTypeGroup group;
	private AssetTypeGroupRemovalSummary removalSummary;
	private List<AssetType> assetTypes;
	
	private List<Long> indexes = new ArrayList<Long>();
	

	public AssetTypeGroupCrud(PersistenceManager persistenceManager, AssetManager assetManager) {
		super(persistenceManager);
		this.assetManager = assetManager;
	}
	
	@Override
	protected void initMemberFields() {
		group = new AssetTypeGroup();
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		group = persistenceManager.find(AssetTypeGroup.class, uniqueId, getTenantId());
	}
	
	private void testRequiredEntities(boolean existing) {
		if (group == null || (existing && group.isNew())) {
			addActionErrorText("error.noassettypegroup");
			throw new MissingEntityException();
		} 
	}

	@SkipValidation
	public String doShow() {
		testRequiredEntities(true);
		return SUCCESS;
	}

	@SkipValidation
	public String doList() {
		return SUCCESS;
	}

	@SkipValidation
	public String doAdd() {
		testRequiredEntities(false);
		return SUCCESS;
	}

	public String doCreate() {
		testRequiredEntities(false);
		group.setTenant(getTenant());
		group.setOrderIdx(getNextAvailableIndex());
		
		try {
			uniqueID = persistenceManager.save(group, getSessionUser().getId());
			addFlashMessageText("message.assettypegroupsaved");
			logger.info(getLogLinePrefix() + "saved asset type group " + group.getName());
		} catch (Exception e) {
			addActionErrorText("error.savingassettypegroup");
			logger.error(getLogLinePrefix() + "could not save asset type group", e);
			return ERROR;
		}
		
		return SUCCESS;
	}
	
	private Long getNextAvailableIndex() {
		QueryBuilder<AssetTypeGroup> query = new QueryBuilder<AssetTypeGroup>(AssetTypeGroup.class, getSecurityFilter());
		return persistenceManager.findCount(query);
	}

	@SkipValidation
	public String doEdit() {
		testRequiredEntities(true);
		return SUCCESS;
	}

	public String doUpdate() {
		testRequiredEntities(true);
		
		try {
			group = persistenceManager.update(group, getSessionUser().getId());
			addFlashMessageText("message.assettypegroupsaved");
			logger.info(getLogLinePrefix() + "updated asset type group " + group.getName());
		} catch (Exception e) {
			addActionErrorText("error.savingassettypegroup");
			logger.error(getLogLinePrefix() + "could not update asset type group", e);
			return ERROR;
		}
		return SUCCESS;
	}
	
	
	@SkipValidation
	public String doUpdateOrder() {
		List<AssetTypeGroup> reorderedList = new ArrayList<AssetTypeGroup>();
		for (int i = 0; i < indexes.size(); i++) {
			Long id = indexes.get(i);
			for (AssetTypeGroup group : getGroups()) {
				if (group.getId().equals(id)) {
					reorderedList.add(group);
					getGroups().remove(group);
					break;
				}
			}
		}
		
		reorderedList.addAll(getGroups());
		
		for (int i = 0; i < reorderedList.size(); i++) {
			AssetTypeGroup group = reorderedList.get(i);
			group.setOrderIdx(new Long(i));
		}
		
		persistenceManager.updateAll(reorderedList, getSessionUser().getId());
		return null;
	}
	
	@SkipValidation
	public String doDeleteConfirm() {
		testRequiredEntities(true);
		
		removalSummary = assetManager.testDelete(group);
		logger.info(getLogLinePrefix() + " confirming delete asset type group " + group.getName());
		return SUCCESS;
	}
	
	@SkipValidation
	public String doDelete() {
		testRequiredEntities(true);
		
		try {
			assetManager.deleteAssetTypeGroup(group);
			addFlashMessageText("message.assettypegroupdeleted");
		} catch (Exception e) {
			logger.error(getLogLinePrefix() + " could not delete asset type group",e);
			addActionErrorText("error.deletingassettypegroup");
			return ERROR;
		}
		
		return SUCCESS;
	}

	public List<AssetTypeGroup> getGroups() {
		if (groups == null) {
			groups = getLoaderFactory().createAssetTypeGroupsLoader().setPostFetchFields("modifiedBy").load();
		}
		return groups;
	}

	public AssetTypeGroup getGroup() {
		return group;
	}

	public String getName() {
		return group.getName();
	}

	@RequiredStringValidator(message="", key="error.namerequired")
	@StringLengthFieldValidator(message="", key="error.assettypegroup.name.length", trim=true, maxLength="40")
	public void setName(String name) {
		group.setName(name);
	}

	public boolean duplicateValueExists(String formValue) {
		return !persistenceManager.uniqueNameAvailable(AssetTypeGroup.class, formValue.trim(), uniqueID, getTenantId());
	}

	public AssetTypeGroupRemovalSummary getRemovalSummary() {
		if (removalSummary == null) {
			removalSummary = assetManager.testDelete(group);
		}
		return removalSummary;
	}

	public List<AssetType> getAssetTypes() {
		if (assetTypes == null) {
			assetTypes = persistenceManager.findAll(new QueryBuilder<AssetType>(AssetType.class, getSecurityFilter()).addSimpleWhere("group", group).addOrder("name"));
		}
		return assetTypes;
	}

	public List<Long> getIndexes() {
		return indexes;
	}

}
