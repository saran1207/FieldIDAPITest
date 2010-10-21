package com.n4systems.fieldid.actions.product;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;


import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.LegacyProductSerial;
import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.exceptions.SubProductUniquenessException;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.helpers.AllInspectionHelper;
import com.n4systems.fieldid.actions.helpers.MasterInspection;
import com.n4systems.fieldid.actions.helpers.SubProductHelper;
import com.n4systems.fieldid.actions.product.helpers.ProductLinkedHelper;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.utils.StrutsListHelper;
import com.n4systems.fieldid.validators.HasDuplicateValueValidator;
import com.n4systems.model.AssociatedInspectionType;
import com.n4systems.model.InspectionType;
import com.n4systems.model.SubInspection;
import com.n4systems.model.SubProduct;
import com.n4systems.model.utils.FindSubProducts;
import com.n4systems.security.Permissions;

@UserPermissionFilter(userRequiresOneOf={Permissions.Tag})
public class SubProductCrud extends AbstractCrud implements HasDuplicateValueValidator {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(SubProductCrud.class);

	protected Asset asset;
	private Long subProductIndex;
	protected List<SubProductHelper> subProducts;
	protected SubProductHelper subProduct;
	protected LegacyProductSerial productManager;

	private AllInspectionHelper allInspectionHelper;
	
	private Long subProductId;

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
		asset = new FindSubProducts(persistenceManager, asset).fillInSubProducts();
	}

	@SkipValidation
	public String doCreate() {
		if (asset == null || asset.isNew()) {
			addActionErrorText("error.noproduct");
			return MISSING;
		}

		if (subProduct == null) {
			addActionErrorText("error.nosubproduct");
			return ERROR;
		}

		subProducts = SubProductHelper.convert(asset.getSubProducts());
		
		Asset foundSubAsset = persistenceManager.find(Asset.class, subProduct.getAsset().getId(), getSecurityFilter(), "type.inspectionTypes");
		if (foundSubAsset == null) {
			addActionErrorText("error.nosubproduct");
			return ERROR;
		} 
		subProduct.setAsset(foundSubAsset);
		subProducts.add(subProduct);

		try {	
			processSubProducts();
			asset = productManager.update(asset, getUser());
			addFlashMessageText("message.productupdated");

			return "saved";
		} catch (SubProductUniquenessException e) {
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
		subProducts = SubProductHelper.convert(asset.getSubProducts());
		return SUCCESS;
	}

	
	public String doUpdate() {
		if (asset == null || asset.isNew()) {
			addActionErrorText("error.noproduct");
			return MISSING;
		}
		if (subProduct == null) {
			addActionErrorText("error.nosubproduct");
			return ERROR;
		}
		
		// this is to get the inspection types for this asset loaded correctly  gah!
		Asset foundSubAsset = persistenceManager.find(Asset.class, subProduct.getAsset().getId(), getSecurityFilter(), "type.inspectionTypes");
		if (foundSubAsset == null) {
			addActionErrorText("error.nosubproduct");
			return ERROR;
		}
		subProduct.setAsset(foundSubAsset);
		
		SubProduct targetSubProduct = asset.getSubProducts().get(asset.getSubProducts().indexOf(new SubProduct(subProduct.getAsset(), asset)));
		
		if (targetSubProduct == null) {
			addActionErrorText("error.nosubproduct");
			return ERROR;
		}
		try {	
			SubProduct target = asset.getSubProducts().get(asset.getSubProducts().indexOf(new SubProduct(subProduct.getAsset(), asset)));
			target.setLabel(subProduct.getLabel());
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
		List<SubProduct> reorderedList = new ArrayList<SubProduct>();
		for (int i = 0; i < indexes.size(); i++) {
			Long id = indexes.get(i);
			for (SubProduct subProduct : asset.getSubProducts()) {
				if (subProduct.getAsset().getId().equals(id)) {
					reorderedList.add(subProduct);
					asset.getSubProducts().remove(subProduct);
					break;
				}
			}
		}
		reorderedList.addAll(asset.getSubProducts());
		
		
		asset.setSubProducts(reorderedList);
		try {
			asset = productManager.update(asset, getUser());
		} catch (SubProductUniquenessException e) {
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

		if (subProductId == null) {
			addActionErrorText("error.noproduct");
			return MISSING;
		}
		SubProduct subProductToRemove = null;
		
		for (SubProduct subProduct : asset.getSubProducts()) {
			if (subProduct.getAsset().getId().equals(subProductId)) {
				subProductToRemove = subProduct;
			}
		}

		if (subProductToRemove == null) {
			addActionErrorText("error.noproduct");
			return MISSING;
		}

		try {
			asset.getSubProducts().remove(subProductToRemove);
			productManager.update(asset, getUser());

			MasterInspection masterInspection = (MasterInspection) getSession().get("masterInspection");
			if (MasterInspection.matchingMasterInspection(masterInspection, token)) {
				masterInspection.removeInspectionsForProduct(subProduct.getAsset());
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
		asset.getSubProducts().clear();
		StrutsListHelper.clearNulls(subProducts);

		if (subProducts != null && !subProducts.isEmpty()) {
			for (SubProductHelper subProduct : subProducts) {
				Asset foundSubAsset = persistenceManager.find(Asset.class, subProduct.getAsset().getId(), getSecurityFilter(), "type.inspectionTypes");

				if (foundSubAsset == null) {
					throw new MissingEntityException("asset id " + subProduct.getAsset().getId().toString() + " missing");
				}

				asset.getSubProducts().add(new SubProduct(subProduct.getLabel(), foundSubAsset, asset));
				subProduct.setAsset(foundSubAsset);
			}
		}
	}

	public Long getSubProductIndex() {
		return subProductIndex;
	}

	public void setSubProductIndex(Long subProductIndex) {
		this.subProductIndex = subProductIndex;
	}

	public Asset getAsset() {
		return asset;
	}

	public SubProductHelper getSubProduct() {
		return subProduct;
	}

	public void setSubProduct(SubProductHelper subProduct) {
		this.subProduct = subProduct;
	}

	public List<SubProductHelper> getSubProducts() {
		if (subProducts == null) {
			subProducts = new ArrayList<SubProductHelper>();
		}
		return subProducts;
	}

	public void setSubProducts(List<SubProductHelper> subProducts) {
		this.subProducts = subProducts;
	}

	public List<AssetType> getSubTypes() {
		return new ArrayList<AssetType>(asset.getType().getSubTypes());
	}

	public Long getSubProductId() {
		return subProductId;
	}

	public void setSubProductId(Long subProductId) {
		this.subProductId = subProductId;
	}

	public List<InspectionType> getInspectionTypes() {		
		List<InspectionType> inspectionTypes = new ArrayList<InspectionType>();
		List<AssociatedInspectionType> associatedInspectionTypes = getLoaderFactory().createAssociatedInspectionTypesLoader().setProductType(subProduct.getAsset().getType()).load();
		for (AssociatedInspectionType associatedInspectionType : associatedInspectionTypes) {
			inspectionTypes.add(associatedInspectionType.getInspectionType());
		}
		return inspectionTypes;
		
	}

	public boolean duplicateValueExists(String formValue) {
		for (SubProductHelper subProduct : subProducts) {
			if (subProduct != null) {
				int count = 0;

				for (SubProductHelper subProduct2 : subProducts) {
					if (subProduct2 != null && subProduct.getLabel().equals(subProduct2.getLabel())) {
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
