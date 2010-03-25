package com.n4systems.fieldid.actions.product;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.validators.HasDuplicateValueValidator;
import com.n4systems.model.ProductType;
import com.n4systems.model.ProductTypeGroup;
import com.n4systems.security.Permissions;
import com.n4systems.util.ProductTypeGroupRemovalSummary;
import com.n4systems.util.persistence.QueryBuilder;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;

@UserPermissionFilter(userRequiresOneOf={Permissions.ManageSystemConfig})
public class ProductTypeGroupCrud extends AbstractCrud implements HasDuplicateValueValidator {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(ProductTypeGroupCrud.class);

	private ProductManager productManager;
	
	private List<ProductTypeGroup> groups;
	private ProductTypeGroup group;
	private ProductTypeGroupRemovalSummary removalSummary;
	private List<ProductType> productTypes;
	
	private List<Long> indexes = new ArrayList<Long>();
	

	public ProductTypeGroupCrud(PersistenceManager persistenceManager, ProductManager productManager) {
		super(persistenceManager);
		this.productManager = productManager;
	}
	
	@Override
	protected void initMemberFields() {
		group = new ProductTypeGroup();
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		group = persistenceManager.find(ProductTypeGroup.class, uniqueId, getTenantId());
	}
	
	private void testRequiredEntities(boolean existing) {
		if (group == null || (existing && group.isNew())) {
			addActionErrorText("error.noproducttypegroup");
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
			addFlashMessageText("message.producttypegroupsaved");
			logger.info(getLogLinePrefix() + "saved product type group " + group.getName());
		} catch (Exception e) {
			addActionErrorText("error.savingproducttypegroup");
			logger.error(getLogLinePrefix() + "could not save product type group", e);
			return ERROR;
		}
		
		return SUCCESS;
	}
	
	private Long getNextAvailableIndex() {
		QueryBuilder<ProductTypeGroup> query = new QueryBuilder<ProductTypeGroup>(ProductTypeGroup.class, getSecurityFilter());
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
			addFlashMessageText("message.producttypegroupsaved");
			logger.info(getLogLinePrefix() + "updated product type group " + group.getName());
		} catch (Exception e) {
			addActionErrorText("error.savingproducttypegroup");
			logger.error(getLogLinePrefix() + "could not update product type group", e);
			return ERROR;
		}
		return SUCCESS;
	}
	
	
	@SkipValidation
	public String doUpdateOrder() {
		List<ProductTypeGroup> reorderedList = new ArrayList<ProductTypeGroup>();
		for (int i = 0; i < indexes.size(); i++) {
			Long id = indexes.get(i);
			for (ProductTypeGroup group : getGroups()) {
				if (group.getId().equals(id)) {
					reorderedList.add(group);
					getGroups().remove(group);
					break;
				}
			}
		}
		
		reorderedList.addAll(getGroups());
		
		for (int i = 0; i < reorderedList.size(); i++) {
			ProductTypeGroup group = reorderedList.get(i);
			group.setOrderIdx(new Long(i));
		}
		
		persistenceManager.updateAll(reorderedList, getSessionUser().getId());
		return null;
	}
	
	@SkipValidation
	public String doDeleteConfirm() {
		testRequiredEntities(true);
		
		removalSummary = productManager.testDelete(group);
		logger.info(getLogLinePrefix() + " confirming delete product type group " + group.getName());
		return SUCCESS;
	}
	
	@SkipValidation
	public String doDelete() {
		testRequiredEntities(true);
		
		try {
			productManager.deleteProductTypeGroup(group);
			addFlashMessageText("message.producttypegroupdeleted");
		} catch (Exception e) {
			logger.error(getLogLinePrefix() + " could not delete product type group",e);
			addActionErrorText("error.deletingproducttypegroup");
			return ERROR;
		}
		
		return SUCCESS;
	}

	public List<ProductTypeGroup> getGroups() {
		if (groups == null) {
			QueryBuilder<ProductTypeGroup> query = new QueryBuilder<ProductTypeGroup>(ProductTypeGroup.class, getSecurityFilter()).addOrder("orderIdx");
			groups = persistenceManager.findAll(query);
		}
		return groups;
	}

	public ProductTypeGroup getGroup() {
		return group;
	}

	public String getName() {
		return group.getName();
	}

	@RequiredStringValidator(message="", key="error.namerequired")
	@StringLengthFieldValidator(message="", key="error.producttypegroup.name.length", trim=true, maxLength="40")
	public void setName(String name) {
		group.setName(name);
	}

	public boolean duplicateValueExists(String formValue) {
		return !persistenceManager.uniqueNameAvailable(ProductTypeGroup.class, formValue.trim(), uniqueID, getTenantId());
	}

	public ProductTypeGroupRemovalSummary getRemovalSummary() {
		if (removalSummary == null) {
			removalSummary = productManager.testDelete(group);
		}
		return removalSummary;
	}

	public List<ProductType> getProductTypes() {
		if (productTypes == null) {
			productTypes = persistenceManager.findAll(new QueryBuilder<ProductType>(ProductType.class, getSecurityFilter()).addSimpleWhere("group", group).addOrder("name"));
		}
		return productTypes;
	}

	public List<Long> getIndexes() {
		return indexes;
	}

}
