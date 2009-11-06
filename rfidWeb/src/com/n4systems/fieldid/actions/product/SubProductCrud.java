package com.n4systems.fieldid.actions.product;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.session.LegacyProductSerial;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.SubProductUniquenessException;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.helpers.AllInspectionHelper;
import com.n4systems.fieldid.actions.helpers.MasterInspection;
import com.n4systems.fieldid.actions.helpers.MissingEntityException;
import com.n4systems.fieldid.actions.helpers.SubProductHelper;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.utils.ListHelper;
import com.n4systems.fieldid.validators.HasDuplicateValueValidator;
import com.n4systems.model.AssociatedInspectionType;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Product;
import com.n4systems.model.ProductType;
import com.n4systems.model.SubInspection;
import com.n4systems.model.SubProduct;
import com.n4systems.model.safetynetwork.HasLinkedProductsLoader;
import com.n4systems.model.utils.FindSubProducts;
import com.n4systems.security.Permissions;

@UserPermissionFilter(userRequiresOneOf={Permissions.Tag})
public class SubProductCrud extends AbstractCrud implements HasDuplicateValueValidator {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(SubProductCrud.class);

	protected Product product;
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
		product = persistenceManager.find(Product.class, uniqueId, getSecurityFilter(), "infoOptions", "type.subTypes");
		product = new FindSubProducts(persistenceManager, product).fillInSubProducts();
	}

	
	

	@SkipValidation
	public String doCreate() {
		if (product == null || product.isNew()) {
			addActionErrorText("error.noproduct");
			return MISSING;
		}

		if (subProduct == null) {
			addActionErrorText("error.nosubproduct");
			return ERROR;
		}

		subProducts = SubProductHelper.convert(product.getSubProducts());
		
		Product foundSubProduct = persistenceManager.find(Product.class, subProduct.getProduct().getId(), getSecurityFilter(), "type.inspectionTypes");
		if (foundSubProduct == null) {
			addActionErrorText("error.nosubproduct");
			return ERROR;
		} 
		subProduct.setProduct(foundSubProduct);
		subProducts.add(subProduct);

		try {	
			processSubProducts();
			product = productManager.update(product, getUser());
			addFlashMessageText("message.productupdated");

			return "saved";
		} catch (SubProductUniquenessException e) {
			addActionErrorText("error.samesubproduct");
		} catch (MissingEntityException e) {
			addActionErrorText("error.missingattachedproduct");
			logger.error("failed to save Product, sub product does not exist or security filter does not allow access", e);
		} catch (Exception e) {
			addActionErrorText("error.productsave");
			logger.error("failed to save Product", e);
		}

		return INPUT;
	}

	@SkipValidation
	public String doShow() {
		if (product == null || product.isNew()) {
			addActionErrorText("error.noproduct");
			return MISSING;
		}
		subProducts = SubProductHelper.convert(product.getSubProducts());
		return SUCCESS;
	}

	
	public String doUpdate() {
		if (product == null || product.isNew()) {
			addActionErrorText("error.noproduct");
			return MISSING;
		}
		if (subProduct == null) {
			addActionErrorText("error.nosubproduct");
			return ERROR;
		}
		
		// this is to get the inspection types for this product loaded correctly  gah!
		Product foundSubProduct = persistenceManager.find(Product.class, subProduct.getProduct().getId(), getSecurityFilter(), "type.inspectionTypes");
		if (foundSubProduct == null) {
			addActionErrorText("error.nosubproduct");
			return ERROR;
		}
		subProduct.setProduct(foundSubProduct);
		
		SubProduct targetSubProduct = product.getSubProducts().get(product.getSubProducts().indexOf(new SubProduct(subProduct.getProduct(), product)));
		
		if (targetSubProduct == null) {
			addActionErrorText("error.nosubproduct");
			return ERROR;
		}
		try {	
			SubProduct target = product.getSubProducts().get(product.getSubProducts().indexOf(new SubProduct(subProduct.getProduct(), product)));
			target.setLabel(subProduct.getLabel());
			product = productManager.update(product, getUser());
			addFlashMessageText("message.productupdated");

			return "saved";
		} catch (MissingEntityException e) {
			addActionErrorText("error.missingattachedproduct");
			logger.error("failed to save Product, sub product does not exist or security filter does not allow access", e);
		} catch (Exception e) {
			addActionErrorText("error.productsave");
			logger.error("failed to save Product", e);
		}

		return INPUT;
		
	}
	
	
	@SkipValidation
	public String doUpdateOrder() {
		List<SubProduct> reorderedList = new ArrayList<SubProduct>();
		for (int i = 0; i < indexes.size(); i++) {
			Long id = indexes.get(i);
			for (SubProduct subProduct : product.getSubProducts()) {
				if (subProduct.getProduct().getId().equals(id)) {
					reorderedList.add(subProduct);
					product.getSubProducts().remove(subProduct);
					break;
				}
			}
		}
		reorderedList.addAll(product.getSubProducts());
		
		
		product.setSubProducts(reorderedList);
		try {
			product = productManager.update(product, getUser());
		} catch (SubProductUniquenessException e) {
			return null;
		} catch (Exception e) {
			return null;
		}
		
		return null;
	}

	@SkipValidation
	public String doRemove() {
		if (product == null || product.isNew()) {
			addActionErrorText("error.noproduct");
			return MISSING;
		}

		if (subProductId == null) {
			addActionErrorText("error.noproduct");
			return MISSING;
		}
		SubProduct subProductToRemove = null;
		
		for (SubProduct subProduct : product.getSubProducts()) {
			if (subProduct.getProduct().getId().equals(subProductId)) {
				subProductToRemove = subProduct;
			}
		}

		if (subProductToRemove == null) {
			addActionErrorText("error.noproduct");
			return MISSING;
		}

		try {
			product.getSubProducts().remove(subProductToRemove);
			productManager.update(product, getUser());

			MasterInspection masterInspection = (MasterInspection) getSession().get("masterInspection");
			if (MasterInspection.matchingMasterInspection(masterInspection, token)) {
				masterInspection.removeInspectionsForProduct(subProduct.getProduct());
			}

			addActionMessageText("message.productupdated");
			return SUCCESS;
		} catch (Exception e) {
			logger.error("couldn't save the product ", e);
			addActionErrorText("error.productupdate");
			return ERROR;
		}

	}

	private void processSubProducts() throws MissingEntityException {
		product.getSubProducts().clear();
		ListHelper.clearNulls(subProducts);

		if (subProducts != null && !subProducts.isEmpty()) {
			for (SubProductHelper subProduct : subProducts) {
				Product foundSubProduct = persistenceManager.find(Product.class, subProduct.getProduct().getId(), getSecurityFilter(), "type.inspectionTypes");

				if (foundSubProduct == null) {
					throw new MissingEntityException("product id " + subProduct.getProduct().getId().toString() + " missing");
				}

				product.getSubProducts().add(new SubProduct(subProduct.getLabel(), foundSubProduct, product));
				subProduct.setProduct(foundSubProduct);
			}
		}
	}

	public Long getSubProductIndex() {
		return subProductIndex;
	}

	public void setSubProductIndex(Long subProductIndex) {
		this.subProductIndex = subProductIndex;
	}

	public Product getProduct() {
		return product;
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

	public List<ProductType> getSubTypes() {
		return new ArrayList<ProductType>(product.getType().getSubTypes());
	}

	

	public Long getSubProductId() {
		return subProductId;
	}

	public void setSubProductId(Long subProductId) {
		this.subProductId = subProductId;
	}

	public List<InspectionType> getInspectionTypes() {		
		List<InspectionType> inspectionTypes = new ArrayList<InspectionType>();
		List<AssociatedInspectionType> associatedInspectionTypes = getLoaderFactory().createAssociatedInspectionTypesLoader().setProductType(subProduct.getProduct().getType()).load();
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

	public List<SubInspection> getInspectionsFor(Product product) {
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
			allInspectionHelper = new AllInspectionHelper(productManager, product, getSecurityFilter());
		return allInspectionHelper;
	}
	
	
	public Long getInspectionCount() {
		return getAllInspectionHelper().getInspectionCount();
	}

	public boolean isLinked() {
		if (product == null) {
			return false;
		} else if (product.isLinked()) {
			return true;
		}
		
		// this checks if there are any products linked to this product
		HasLinkedProductsLoader hasLinkedLoader = getLoaderFactory().createHasLinkedProductsLoader();
		hasLinkedLoader.setNetworkId(product.getNetworkId());
		hasLinkedLoader.setProductId(product.getId());
		
		boolean hasLinked = hasLinkedLoader.load();
		return hasLinked;
	}
	
}
