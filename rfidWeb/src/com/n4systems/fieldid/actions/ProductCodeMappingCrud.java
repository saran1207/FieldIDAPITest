package com.n4systems.fieldid.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;
import rfid.ejb.entity.ProductCodeMappingBean;
import rfid.ejb.session.LegacyProductType;
import rfid.ejb.session.ProductCodeMapping;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.helpers.InfoFieldInput;
import com.n4systems.fieldid.actions.helpers.InfoOptionInput;
import com.n4systems.fieldid.actions.helpers.ProductTypeLister;
import com.n4systems.fieldid.permissions.ExtendedFeatureFilter;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.ProductType;
import com.n4systems.util.StringListingPair;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;


@ExtendedFeatureFilter(requiredFeature=ExtendedFeature.Integration)
public class ProductCodeMappingCrud extends AbstractCrud {


	private static final long serialVersionUID = 1L;

	private ProductCodeMapping productCodeMappingManager;
	
	private List<ProductCodeMappingBean> productCodeMappings;
	private ProductCodeMappingBean productCodeMapping;
	private LegacyProductType productTypeManager;
	
	private ProductTypeLister productTypes;
	
	private List<InfoOptionInput> productInfoOptions;
	
	private ProductType productType;
	
	private boolean productTypeUpdate = false;
	
	public ProductCodeMappingCrud(ProductCodeMapping productCodeMappingManager, LegacyProductType productTypeManager, PersistenceManager persistenceManager) {
		super(persistenceManager);
		this.productCodeMappingManager = productCodeMappingManager;
		this.productTypeManager = productTypeManager;
		
	}
	
	
	@Override
	protected void initMemberFields() {
		productCodeMapping = new ProductCodeMappingBean();
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		productCodeMapping = productCodeMappingManager.getProductCodeByUniqueIdAndTenant( uniqueId, getTenantId() );
	}


	@SkipValidation
	public String doEdit() {
		if( productCodeMapping == null ) {
			addActionError("Product Code Mapping not found.");
			return ERROR;
		}
		return INPUT;
	}
	
	
	public String doSave() {
		if (productCodeMapping == null) {
			addActionError("Product Code Mapping not found.");
			return ERROR;
		}

		if( productTypeUpdate ) {
			productInfoOptions = null;
			return INPUT;
		}
		
		productCodeMapping.setTenant(getTenant());
		
		convertInputsToInfoOptions();
		try {
			productCodeMappingManager.update( productCodeMapping );
			addActionMessage("Data has been updated.");
			return SUCCESS;
		} catch (Exception e) {
			addActionError("Failed to update.");
			return INPUT;
		}
		
	}
	
	private void convertInputsToInfoOptions() {
		List<InfoOptionBean> options = InfoOptionInput.convertInputInfoOptionsToInfoOptions( productInfoOptions, productCodeMapping.getProductInfo().getInfoFields() );
		productCodeMapping.setInfoOptions( options );
	}
	
	
	@SkipValidation
	public String doList() {
		return SUCCESS;
	}
	
	@SkipValidation
	public String doRemove() {
		if ( productCodeMapping == null) {
			addActionError("Product Code Mapping not found");
			return ERROR;
		}
		try {
			productCodeMappingManager.deleteByIdAndTenant( uniqueID, getTenantId() );
		} catch ( Exception e ) {
			addActionError("Product Code Mapping can not be removed");
			return ERROR;
		}
		addActionMessage("Product Code Mapping has be removed");
		return SUCCESS;
	}
	
	
	public List<ProductCodeMappingBean> getProductCodeMappings() {
		if( productCodeMappings == null ) {
			productCodeMappings = productCodeMappingManager.getAllProductCodesByTenant( getTenantId() );
		}
		return productCodeMappings;
	}


	public ProductCodeMappingBean getProductCodeMapping() {
		return productCodeMapping;
	}
	
	
	public ProductTypeLister getProductTypes() {
		if (productTypes == null) {
			productTypes = new ProductTypeLister(persistenceManager, getSecurityFilter());
		}

		return productTypes;
	}


	public void setProductCodeMapping(ProductCodeMappingBean productCodeMapping) {
		this.productCodeMapping = productCodeMapping;
	}

	@RequiredStringValidator(type = ValidatorType.FIELD, message = "", key = "error.productcoderequired")
	public String getProductCode() {
		return productCodeMapping.getProductCode();
	}


	public void setProductCode(String productCode) {
		this.productCodeMapping.setProductCode( productCode ); 
	}
	
	public String getCustomerRefNumber() {
		return productCodeMapping.getCustomerRefNumber();
	}

	public void setCustomerRefNumber(String customerRefNumber) {
		productCodeMapping.setCustomerRefNumber(customerRefNumber);
	}
	
	public ProductType getProductTypeBean() {
		if( productType == null && productCodeMapping.getProductInfo() != null ) {
			productType = productCodeMapping.getProductInfo();
		}
		return productType;
	}
	
	@RequiredFieldValidator(type = ValidatorType.FIELD, message = "", key = "error.producttyperequired")
	public Long getProductType() {
		return (productCodeMapping.getProductInfo() != null ) ? productCodeMapping.getProductInfo().getId() : null ;
	}
	
	public Collection<InfoFieldBean> getProductInfoFields() {
		if( productCodeMapping.getProductInfo() != null ) {
			return productCodeMapping.getProductInfo().getInfoFields();
		}
		return new ArrayList<InfoFieldBean>(); 
	}

	
	public void setProductType(Long productTypeId ) {
		ProductType productType = null ;
		if( productTypeId != null ) {
			productType = productTypeManager.findProductTypeAllFields( productTypeId, getTenantId() );
		}
		this.productCodeMapping.setProductInfo( productType );
	}


	public List<InfoOptionInput> getProductInfoOptions() {
		if( productInfoOptions == null ) {
			productInfoOptions = new ArrayList<InfoOptionInput>();
			if( productCodeMapping.getProductInfo() != null ) {
				productInfoOptions = InfoOptionInput.convertInfoOptionsToInputInfoOptions( productCodeMapping.getInfoOptions(), productCodeMapping.getProductInfo().getInfoFields() ); 
			}
		}
		return productInfoOptions;
	}

	
	
	public void setProductInfoOptions(List<InfoOptionInput> infoOptions) {
		this.productInfoOptions = infoOptions;
	}


	public boolean isProductTypeUpdate() {
		return productTypeUpdate;
	}


	public void setProductTypeUpdate(boolean productTypeUpdate) {
		this.productTypeUpdate = productTypeUpdate;
	}

	public List<StringListingPair> getComboBoxInfoOptions( InfoFieldBean field , InfoOptionInput inputOption ) {
		return InfoFieldInput.getComboBoxInfoOptions( field, inputOption  );
	}

		
	
}
