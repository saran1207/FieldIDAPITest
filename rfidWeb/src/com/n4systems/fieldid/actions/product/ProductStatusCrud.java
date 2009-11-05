package com.n4systems.fieldid.actions.product;

import java.util.Collection;

import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.entity.ProductStatusBean;
import rfid.ejb.session.LegacyProductSerial;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.security.Permissions;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@UserPermissionFilter(userRequiresOneOf={Permissions.ManageSystemConfig})
public class ProductStatusCrud extends AbstractCrud {

	private static final long serialVersionUID = 1L;
	
	private LegacyProductSerial productSerialManager;
	private ProductStatusBean productStatus;
	
	private Collection<ProductStatusBean> productStatuses;
	
	public ProductStatusCrud(LegacyProductSerial productSerialManager, PersistenceManager persistenceManager) {
		super(persistenceManager);
		this.productSerialManager = productSerialManager; 
	}
	
	@Override
	protected void initMemberFields() {
		productStatus = new ProductStatusBean();
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		productStatus = productSerialManager.findProductStatus( uniqueID, getTenantId() );
	}
	
	
	@SkipValidation
	public String doList() {
		return SUCCESS;
	}

	@SkipValidation
	public String doLoadEdit() {
		if ( productStatus == null ) {
			addActionError("Product Status not found");
			return ERROR;
		}
		
		return INPUT;
	}
		
	public String doSave() {
		productStatus.setModifiedBy( getSessionUser().getName() );
		if ( productStatus.getUniqueID() == null ) {
			productStatus.setTenant(getTenant());
			
			productSerialManager.createProductStatus(productStatus);
		} else {
			productSerialManager.updateProductStatus( productStatus );
		}
		
		addActionMessage("Data has been updated.");
		
		return SUCCESS;
	}
	
	@SkipValidation
	public String doRemove() {
		if (productStatus == null) {
			addActionError("Product Status not found");
			return ERROR;
		}
		try {
			productSerialManager.removeProductStatus( productStatus );
		} catch ( Exception e ) {
			addActionError("Product Status can not be removed");
			return ERROR;
		}
		addActionMessage("Product Status has be removed");
		return SUCCESS;
	}

	public Collection<ProductStatusBean> getProductStatuses() {
		if( productStatuses == null ) {
			productStatuses = productSerialManager.getAllProductStatus( getTenantId() );
		}
		return productStatuses;
	}

	public ProductStatusBean getProductStatus() {
		return productStatus;
	}
	
	
	@RequiredStringValidator(type=ValidatorType.FIELD, message = "", key="error.required")
	public void setName(String name) {
		productStatus.setName(name);
	}
	
	public String getName() {
		return productStatus.getName();
	}

}
