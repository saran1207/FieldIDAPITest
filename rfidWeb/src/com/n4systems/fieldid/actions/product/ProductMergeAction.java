package com.n4systems.fieldid.actions.product;

import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.session.LegacyProductSerial;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.helpers.AllInspectionHelper;
import com.n4systems.fieldid.actions.helpers.MissingEntityException;
import com.n4systems.fieldid.actions.product.helpers.ProductLinkedHelper;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.Inspection;
import com.n4systems.model.Product;
import com.n4systems.security.Permissions;
import com.n4systems.taskscheduling.TaskExecutor;
import com.n4systems.taskscheduling.task.ProductMergeTask;

@UserPermissionFilter(userRequiresOneOf={Permissions.Tag})
public class ProductMergeAction extends AbstractCrud {

	private static final long serialVersionUID = 1L;
	
	private final ProductManager productManager;
	private final LegacyProductSerial legacyProductManager;
	
	private AllInspectionHelper allInspectionHelper;
	
	private Product losingProduct;
	private Product winningProduct;
	

	public ProductMergeAction(PersistenceManager persistenceManager, ProductManager productManager, LegacyProductSerial legacyProductSerialManager) {
		super(persistenceManager);
		this.productManager = productManager;
		this.legacyProductManager = legacyProductSerialManager;
	}


	@Override
	protected void initMemberFields() {
		losingProduct = new Product();
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		losingProduct = productManager.findProductAllFields(uniqueId, getSecurityFilter());
	}
	
	private void testRequiredEntities() {
		if (losingProduct == null || losingProduct.isNew()) {
			addActionErrorText("error.noproduct");
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
		if (winningProduct == null) {
			addActionErrorText("error.you_must_choose_a_valid_product_to_merge_into");
			return INPUT;
		}
		
		ProductMergeTask task = new ProductMergeTask(winningProduct, losingProduct, fetchCurrentUser());
		TaskExecutor.getInstance().execute(task);
		
		return SUCCESS;
	}


	public Product getLosingProduct() {
		return losingProduct;
	}
	
	public Product getProduct() {
		return losingProduct;
	}


	public Product getWinningProduct() {
		return winningProduct;
	}
	
	public Long getWinngingProductId() {
		return (winningProduct != null) ? winningProduct.getId() : null;
	}
		
	public void setWinningProductId(Long productId) {
		if (productId == null) {
			winningProduct = null;
		} else if (winningProduct == null || !productId.equals(winningProduct.getId())){ 
			winningProduct = productManager.findProductAllFields(productId, getSecurityFilter());
		}
	}

	public AllInspectionHelper getAllInspectionHelper() {
		if (allInspectionHelper == null)
			allInspectionHelper = new AllInspectionHelper(legacyProductManager, losingProduct, getSecurityFilter());
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
		return ProductLinkedHelper.isLinked(losingProduct, getLoaderFactory());
	}
}
