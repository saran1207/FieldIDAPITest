package com.n4systems.fieldid.actions.product;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.exceptions.SubAssetUniquenessException;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;


import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.LegacyProductSerial;
import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.helpers.AllInspectionHelper;
import com.n4systems.fieldid.actions.helpers.MasterInspection;
import com.n4systems.fieldid.actions.helpers.SubAssetHelper;
import com.n4systems.fieldid.actions.product.helpers.ProductLinkedHelper;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.utils.StrutsListHelper;
import com.n4systems.fieldid.validators.HasDuplicateValueValidator;
import com.n4systems.model.AssociatedInspectionType;
import com.n4systems.model.InspectionType;
import com.n4systems.model.SubInspection;
import com.n4systems.model.SubAsset;
import com.n4systems.model.utils.FindSubAssets;
import com.n4systems.security.Permissions;

@UserPermissionFilter(userRequiresOneOf={Permissions.Tag})
public class SubProductCrud extends AbstractCrud implements HasDuplicateValueValidator {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(SubProductCrud.class);

	protected Asset asset;
	private Long subAssetIndex;
	protected List<SubAssetHelper> subAssets;
	protected SubAssetHelper subAsset;
	protected LegacyProductSerial productManager;

	private AllInspectionHelper allInspectionHelper;
	
	private Long subAssetId;

	private String token;
	
	private List<Long> indexes = new ArrayList<Long>();

	public SubProductCrud(PersistenceManager persistenceManager, LegacyProductSerial productManager) {
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
			addActionErrorText("error.noproduct");
			return MISSING;
		}

		if (subAsset == null) {
			addActionErrorText("error.nosubproduct");
			return ERROR;
		}

		subAssets = SubAssetHelper.convert(asset.getSubAssets());
		
		Asset foundSubAsset = persistenceManager.find(Asset.class, subAsset.getAsset().getId(), getSecurityFilter(), "type.inspectionTypes");
		if (foundSubAsset == null) {
			addActionErrorText("error.nosubproduct");
			return ERROR;
		} 
		subAsset.setAsset(foundSubAsset);
		subAssets.add(subAsset);

		try {	
			processSubProducts();
			asset = productManager.update(asset, getUser());
			addFlashMessageText("message.productupdated");

			return "saved";
		} catch (SubAssetUniquenessException e) {
			addActionErrorText("error.samesubproduct");
		} catch (MissingEntityException e) {
			addActionErrorText("error.missingattachedproduct");
			logger.error("failed to save Asset, sub asset does not exist or security filter does not allow access", e);
		} catch (Exception e) {
			addActionErrorText("error.productsave");
			logger.error("failed to save Asset", e);
		}

		return INPUT;
	}

	@SkipValidation
	public String doShow() {
		if (asset == null || asset.isNew()) {
			addActionErrorText("error.noproduct");
			return MISSING;
		}
		subAssets = SubAssetHelper.convert(asset.getSubAssets());
		return SUCCESS;
	}

	
	public String doUpdate() {
		if (asset == null || asset.isNew()) {
			addActionErrorText("error.noproduct");
			return MISSING;
		}
		if (subAsset == null) {
			addActionErrorText("error.nosubproduct");
			return ERROR;
		}
		
		// this is to get the inspection types for this asset loaded correctly  gah!
		Asset foundSubAsset = persistenceManager.find(Asset.class, subAsset.getAsset().getId(), getSecurityFilter(), "type.inspectionTypes");
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
			addFlashMessageText("message.productupdated");

			return "saved";
		} catch (MissingEntityException e) {
			addActionErrorText("error.missingattachedproduct");
			logger.error("failed to save Asset, sub asset does not exist or security filter does not allow access", e);
		} catch (Exception e) {
			addActionErrorText("error.productsave");
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
			addActionErrorText("error.noproduct");
			return MISSING;
		}

		if (subAssetId == null) {
			addActionErrorText("error.noproduct");
			return MISSING;
		}
		SubAsset subAssetToRemove = null;
		
		for (SubAsset subAsset : asset.getSubAssets()) {
			if (subAsset.getAsset().getId().equals(subAssetId)) {
				subAssetToRemove = subAsset;
			}
		}

		if (subAssetToRemove == null) {
			addActionErrorText("error.noproduct");
			return MISSING;
		}

		try {
			asset.getSubAssets().remove(subAssetToRemove);
			productManager.update(asset, getUser());

			MasterInspection masterInspection = (MasterInspection) getSession().get("masterInspection");
			if (MasterInspection.matchingMasterInspection(masterInspection, token)) {
				masterInspection.removeInspectionsForAsset(subAsset.getAsset());
			}

			addActionMessageText("message.productupdated");
			return SUCCESS;
		} catch (Exception e) {
			logger.error("couldn't save the asset ", e);
			addActionErrorText("error.productupdate");
			return ERROR;
		}

	}

	private void processSubProducts() throws MissingEntityException {
		asset.getSubAssets().clear();
		StrutsListHelper.clearNulls(subAssets);

		if (subAssets != null && !subAssets.isEmpty()) {
			for (SubAssetHelper subAsset : subAssets) {
				Asset foundSubAsset = persistenceManager.find(Asset.class, subAsset.getAsset().getId(), getSecurityFilter(), "type.inspectionTypes");

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

	public List<InspectionType> getInspectionTypes() {		
		List<InspectionType> inspectionTypes = new ArrayList<InspectionType>();
		List<AssociatedInspectionType> associatedInspectionTypes = getLoaderFactory().createAssociatedInspectionTypesLoader().setProductType(subAsset.getAsset().getType()).load();
		for (AssociatedInspectionType associatedInspectionType : associatedInspectionTypes) {
			inspectionTypes.add(associatedInspectionType.getInspectionType());
		}
		return inspectionTypes;
		
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

	public List<SubInspection> getInspectionsFor(Asset product) {
		return new ArrayList<SubInspection>();
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
	
	public AllInspectionHelper getAllInspectionHelper() {
		if (allInspectionHelper == null)
			allInspectionHelper = new AllInspectionHelper(productManager, asset, getSecurityFilter());
		return allInspectionHelper;
	}
	
	
	public Long getInspectionCount() {
		return getAllInspectionHelper().getInspectionCount();
	}

	public boolean isLinked() {
		return ProductLinkedHelper.isLinked(asset, getLoaderFactory());
	}
	
}
