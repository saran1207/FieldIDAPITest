package com.n4systems.fieldid.actions.asset;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.ejb.legacy.LegacyAsset;
import com.n4systems.exceptions.SubAssetUniquenessException;
import com.n4systems.fieldid.actions.asset.helpers.AssetLinkedHelper;
import com.n4systems.fieldid.actions.helpers.AllEventHelper;
import com.n4systems.fieldid.actions.helpers.MasterEvent;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import com.n4systems.model.EventType;
import com.n4systems.model.SubEvent;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;


import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.helpers.SubAssetHelper;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.utils.StrutsListHelper;
import com.n4systems.fieldid.validators.HasDuplicateValueValidator;
import com.n4systems.model.AssociatedEventType;
import com.n4systems.model.SubAsset;
import com.n4systems.model.utils.FindSubAssets;
import com.n4systems.security.Permissions;

@UserPermissionFilter(userRequiresOneOf={Permissions.Tag})
public class SubAssetCrud extends AbstractCrud implements HasDuplicateValueValidator {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(SubAssetCrud.class);

	protected Asset asset;
	private Long subAssetIndex;
	protected List<SubAssetHelper> subAssets;
	protected SubAssetHelper subAsset;
	protected LegacyAsset productManager;

	private AllEventHelper allEventHelper;
	
	private Long subAssetId;

	private String token;
	
	private List<Long> indexes = new ArrayList<Long>();

	public SubAssetCrud(PersistenceManager persistenceManager, LegacyAsset productManager) {
		super(persistenceManager);
		this.productManager = productManager;
	}

	@Override
	protected void initMemberFields() {
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		asset = persistenceManager.find(Asset.class, uniqueId, getSecurityFilter(), "infoOptions", "type.subTypes");
		asset = new FindSubAssets(persistenceManager, asset).fillInSubAssets();
	}

	@SkipValidation
	public String doCreate() {
		if (asset == null || asset.isNew()) {
			addActionErrorText("error.noasset");
			return MISSING;
		}

		if (subAsset == null) {
			addActionErrorText("error.nosubasset");
			return ERROR;
		}

		subAssets = SubAssetHelper.convert(asset.getSubAssets());
		
		Asset foundSubAsset = persistenceManager.find(Asset.class, subAsset.getAsset().getId(), getSecurityFilter(), "type.eventTypes");
		if (foundSubAsset == null) {
			addActionErrorText("error.nosubasset");
			return ERROR;
		} 
		subAsset.setAsset(foundSubAsset);
		subAssets.add(subAsset);

		try {	
			processSubProducts();
			asset = productManager.update(asset, getUser());
			addFlashMessageText("message.assetupdated");

			return "saved";
		} catch (SubAssetUniquenessException e) {
			addActionErrorText("error.samesubasset");
		} catch (MissingEntityException e) {
			addActionErrorText("error.missingattachedasset");
			logger.error("failed to save Asset, sub asset does not exist or security filter does not allow access", e);
		} catch (Exception e) {
			addActionErrorText("error.assetsave");
			logger.error("failed to save Asset", e);
		}

		return INPUT;
	}

	@SkipValidation
	public String doShow() {
		if (asset == null || asset.isNew()) {
			addActionErrorText("error.noasset");
			return MISSING;
		}
		subAssets = SubAssetHelper.convert(asset.getSubAssets());
		return SUCCESS;
	}

	
	public String doUpdate() {
		if (asset == null || asset.isNew()) {
			addActionErrorText("error.noasset");
			return MISSING;
		}
		if (subAsset == null) {
			addActionErrorText("error.nosubproduct");
			return ERROR;
		}
		
		// this is to get the inspection types for this asset loaded correctly  gah!
		Asset foundSubAsset = persistenceManager.find(Asset.class, subAsset.getAsset().getId(), getSecurityFilter(), "type.eventTypes");
		if (foundSubAsset == null) {
			addActionErrorText("error.nosubproduct");
			return ERROR;
		}
		subAsset.setAsset(foundSubAsset);
		
		SubAsset targetSubAsset = asset.getSubAssets().get(asset.getSubAssets().indexOf(new SubAsset(subAsset.getAsset(), asset)));
		
		if (targetSubAsset == null) {
			addActionErrorText("error.nosubproduct");
			return ERROR;
		}
		try {	
			SubAsset target = asset.getSubAssets().get(asset.getSubAssets().indexOf(new SubAsset(subAsset.getAsset(), asset)));
			target.setLabel(subAsset.getLabel());
			asset = productManager.update(asset, getUser());
			addFlashMessageText("message.assetupdated");

			return "saved";
		} catch (MissingEntityException e) {
			addActionErrorText("error.missingattachedasset");
			logger.error("failed to save Asset, sub asset does not exist or security filter does not allow access", e);
		} catch (Exception e) {
			addActionErrorText("error.assetsave");
			logger.error("failed to save Asset", e);
		}

		return INPUT;
		
	}
	
	
	@SkipValidation
	public String doUpdateOrder() {
		List<SubAsset> reorderedList = new ArrayList<SubAsset>();
		for (int i = 0; i < indexes.size(); i++) {
			Long id = indexes.get(i);
			for (SubAsset subAsset : asset.getSubAssets()) {
				if (subAsset.getAsset().getId().equals(id)) {
					reorderedList.add(subAsset);
					asset.getSubAssets().remove(subAsset);
					break;
				}
			}
		}
		reorderedList.addAll(asset.getSubAssets());
		
		
		asset.setSubAssets(reorderedList);
		try {
			asset = productManager.update(asset, getUser());
		} catch (SubAssetUniquenessException e) {
			return null;
		} catch (Exception e) {
			return null;
		}
		
		return null;
	}

	@SkipValidation
	public String doRemove() {
		if (asset == null || asset.isNew()) {
			addActionErrorText("error.noasset");
			return MISSING;
		}

		if (subAssetId == null) {
			addActionErrorText("error.noasset");
			return MISSING;
		}
		SubAsset subAssetToRemove = null;
		
		for (SubAsset subAsset : asset.getSubAssets()) {
			if (subAsset.getAsset().getId().equals(subAssetId)) {
				subAssetToRemove = subAsset;
			}
		}

		if (subAssetToRemove == null) {
			addActionErrorText("error.noasset");
			return MISSING;
		}

		try {
			asset.getSubAssets().remove(subAssetToRemove);
			productManager.update(asset, getUser());

			MasterEvent masterEvent = (MasterEvent) getSession().get("masterInspection");
			if (MasterEvent.matchingMasterInspection(masterEvent, token)) {
				masterEvent.removeInspectionsForAsset(subAsset.getAsset());
			}

			addActionMessageText("message.assetupdated");
			return SUCCESS;
		} catch (Exception e) {
			logger.error("couldn't save the asset ", e);
			addActionErrorText("error.assetupdate");
			return ERROR;
		}

	}

	private void processSubProducts() throws MissingEntityException {
		asset.getSubAssets().clear();
		StrutsListHelper.clearNulls(subAssets);

		if (subAssets != null && !subAssets.isEmpty()) {
			for (SubAssetHelper subAsset : subAssets) {
				Asset foundSubAsset = persistenceManager.find(Asset.class, subAsset.getAsset().getId(), getSecurityFilter(), "type.eventTypes");

				if (foundSubAsset == null) {
					throw new MissingEntityException("asset id " + subAsset.getAsset().getId().toString() + " missing");
				}

				asset.getSubAssets().add(new SubAsset(subAsset.getLabel(), foundSubAsset, asset));
				subAsset.setAsset(foundSubAsset);
			}
		}
	}

	public Long getSubAssetIndex() {
		return subAssetIndex;
	}

	public void setSubAssetIndex(Long subAssetIndex) {
		this.subAssetIndex = subAssetIndex;
	}

	public Asset getAsset() {
		return asset;
	}

	public SubAssetHelper getSubAsset() {
		return subAsset;
	}

	public void setSubAsset(SubAssetHelper subAsset) {
		this.subAsset = subAsset;
	}

	public List<SubAssetHelper> getSubAssets() {
		if (subAssets == null) {
			subAssets = new ArrayList<SubAssetHelper>();
		}
		return subAssets;
	}

	public void setSubAssets(List<SubAssetHelper> subAssets) {
		this.subAssets = subAssets;
	}

	public List<AssetType> getSubTypes() {
		return new ArrayList<AssetType>(asset.getType().getSubTypes());
	}

	public Long getSubAssetId() {
		return subAssetId;
	}

	public void setSubAssetId(Long subAssetId) {
		this.subAssetId = subAssetId;
	}

	public List<EventType> getInspectionTypes() {
		List<EventType> eventTypes = new ArrayList<EventType>();
		List<AssociatedEventType> associatedEventTypes = getLoaderFactory().createAssociatedEventTypesLoader().setAssetType(subAsset.getAsset().getType()).load();
		for (AssociatedEventType associatedEventType : associatedEventTypes) {
			eventTypes.add(associatedEventType.getEventType());
		}
		return eventTypes;
		
	}

	public boolean duplicateValueExists(String formValue) {
		for (SubAssetHelper subAsset : subAssets) {
			if (subAsset != null) {
				int count = 0;

				for (SubAssetHelper subProduct2 : subAssets) {
					if (subProduct2 != null && subAsset.getLabel().equals(subProduct2.getLabel())) {
						count++;
					}
				}
				if (count > 1) {
					return true;
				}
			}
		}
		return false;
	}

	public List<SubEvent> getInspectionsFor(Asset product) {
		return new ArrayList<SubEvent>();
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public List<Long> getIndexes() {
		return indexes;
	}

	public void setIndexes(List<Long> indexes) {
		this.indexes = indexes;
	}
	
	public AllEventHelper getAllInspectionHelper() {
		if (allEventHelper == null)
			allEventHelper = new AllEventHelper(productManager, asset, getSecurityFilter());
		return allEventHelper;
	}
	
	
	public Long getInspectionCount() {
		return getAllInspectionHelper().getEventCount();
	}

	public boolean isLinked() {
		return AssetLinkedHelper.isLinked(asset, getLoaderFactory());
	}
	
}
