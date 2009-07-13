package com.n4systems.fieldid.actions.product;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.session.LegacyProductType;

import com.n4systems.ejb.CustomerManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.validators.HasDuplicateValueValidator;
import com.n4systems.model.Customer;
import com.n4systems.model.InspectionType;
import com.n4systems.model.ProductType;
import com.n4systems.model.ProductTypeSchedule;
import com.n4systems.util.ListingPair;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;

public class ProductTypeScheduleCrud extends AbstractCrud implements HasDuplicateValueValidator {
	private static final long serialVersionUID = 1L;
	
	private Long productTypeId;
	private Long inspectionTypeId;
	private ProductType productType;
	private InspectionType inspectionType;
	private Customer customer;
	private boolean customerForm = false;
	private ProductTypeSchedule schedule;
	
	private List<InspectionType> inspectionTypes;
	private Map<String,ProductTypeSchedule> schedules;
	private Map<String,List<ProductTypeSchedule>> customerOverrideSchedules;
	private Collection<ListingPair> customers;
	
	private LegacyProductType productTypeManager;
	private CustomerManager customerManager; 

	public ProductTypeScheduleCrud( LegacyProductType productTypeManager, PersistenceManager persistenceManager, CustomerManager customerManager ) {
		super(persistenceManager);
		this.productTypeManager = productTypeManager;
		this.customerManager = customerManager;
	}

	@Override
	protected void initMemberFields() {
		schedule = new ProductTypeSchedule();
		schedule.setTenant( getTenant() );
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		getProductType();
		for (ProductTypeSchedule schedule : productType.getSchedules() ) {
			if( schedule.getId().equals( uniqueId ) ) {
				this.schedule = schedule;
			}
		}
	}
	
	
	@SkipValidation
	public String doList() {
		return SUCCESS;
		
	}
	
	@SkipValidation
	public String doEdit() {
		return INPUT;
	}
	
	@SkipValidation
	public String doShow() {
		
		return SUCCESS;
	}
	
	
	@SkipValidation
	public String doDelete() {
		if( schedule.getId() != null ) {
			productType.getSchedules().remove( schedule );
			
		}
		try {
			productTypeManager.updateProductType( productType );
			schedule.setId( null );
			schedules = null;
			addFlashMessage( getText( "message.schdeuledeleted"  ) );
			
		} catch (Exception e) {
			addActionError( getText( "error.failedtodelete" ) );
			return ERROR;
		}
		return SUCCESS;
	}
	
	
	public String doSave() {
		getProductType();
		getInspectionType();
		if( schedule.getId() == null ) {
			schedule.setProductType( productType );
			schedule.setInspectionType( inspectionType );
			schedule.setTenant( getTenant() ); 
			schedule.setCustomer( customer );
			productType.getSchedules().add( schedule );
		}
		
		try {
			productType = productTypeManager.updateProductType( productType );
			schedule = productType.getSchedule( schedule.getInspectionType(), schedule.getCustomer() );
		} catch (Exception e) {
			addActionError( getText( "error.failedtosave" ) );
			return ERROR;
		}
		
		addFlashMessage( getText( "message.schedulesaved" ) );
		return SUCCESS;
	}
	

	public Long getProductTypeId() {
		return productTypeId;
	}

	public void setProductTypeId(Long productTypeId) {
		this.productTypeId = productTypeId;
	}

	public ProductType getProductType() {
		if( productType == null ) {
			productType = productTypeManager.findProductTypeAllFields( productTypeId, getTenantId() );
		}
		return productType;
	}

	public InspectionType getInspectionType() {
		if( inspectionType == null ) {
			inspectionType = persistenceManager.find( InspectionType.class, inspectionTypeId, getTenant() );
		}
		return inspectionType;
	}

	public List<InspectionType> getInspectionTypes() {
		if( inspectionTypes == null ) {
			getProductType();
			inspectionTypes = new ArrayList<InspectionType>( productType.getInspectionTypes() );
		}
		return inspectionTypes;
	}
	
	
	public Map<String,ProductTypeSchedule> getSchedules() {
		if( schedules == null ){
			getProductType();
			schedules = new HashMap<String, ProductTypeSchedule>();
			for ( ProductTypeSchedule schedule : productType.getSchedules() ) {
				if( schedule.getCustomer() == null ) {
					schedules.put( schedule.getInspectionType().getName(), schedule );
				}
			}
		}
		return schedules;
	}
	
	
	public Map<String, List<ProductTypeSchedule>> getCustomerOverrideSchedules() {
		if( customerOverrideSchedules == null ){
			getProductType();
			customerOverrideSchedules = new HashMap<String, List<ProductTypeSchedule>>();
			for ( ProductTypeSchedule schedule : productType.getSchedules() ) {
				if( schedule.getCustomer() != null ) {
					if( customerOverrideSchedules.get( schedule.getInspectionType().getName() ) == null ) {
						customerOverrideSchedules.put( schedule.getInspectionType().getName(), new ArrayList<ProductTypeSchedule>() ) ;
					}
					customerOverrideSchedules.get( schedule.getInspectionType().getName() ).add( schedule );
				}
			}
		}
		return customerOverrideSchedules;
	}

	public Long getInspectionTypeId() {
		return inspectionTypeId;
	}

	public void setInspectionTypeId(Long inspectionTypeId) {
		this.inspectionTypeId = inspectionTypeId;
	}

	public boolean isAutoSchedule() {
		return schedule.isAutoSchedule();
	}
	
	public void setAutoSchedule( boolean autoSchedule ) {
		schedule.setAutoSchedule( autoSchedule );
		
	}
	
	public Long getFrequency() {
		return schedule.getFrequency();
	}
	
	@RequiredFieldValidator( message="", key="error.frequencyisrequired" )
	public void setFrequency(Long frequency) {
		schedule.setFrequency(frequency);
	}

	
	public ProductTypeSchedule getSchedule( ) {
		return schedule;
	}

	public Long getCustomer() {
		return ( customer != null ) ? customer.getId() : null;
	}
	
	public Customer getCustomerBean() {
		return customer;
	}
	
	@CustomValidator(type="uniqueValue", message = "", key="errors.overrideexists")
	public void setCustomer(Long customerId) {
		if( customerId == null ) {
			customer = null;
		} else if( customer == null || !this.customer.getId().equals( customerId ) ) {
			this.customer = customerManager.findCustomer(customerId, getSecurityFilter());
		}
	}

	public boolean isCustomerForm() {
		return customerForm;
	}

	
	public void setCustomerForm(boolean customerForm) {
		this.customerForm = customerForm;
	}

	public Collection<ListingPair> getCustomers() {
		if( customers == null ) {
			customers = customerManager.findCustomersLP( getTenantId(), getSecurityFilter() );
		}
		return customers;
	}
	
	public ProductTypeSchedule newSchedule() {
		return new ProductTypeSchedule();
		
	}


	public boolean duplicateValueExists(String formValue) {
		if( customer == null ) {
			return false;
		}
		
		// check if there is already an override for this customer.
		getProductType();
		getInspectionType();
		
		getCustomerOverrideSchedules();
		List<ProductTypeSchedule> existingSchedules = customerOverrideSchedules.get( inspectionType.getName() );
		if( existingSchedules == null ){
			return false;
		}
		for( ProductTypeSchedule existingSchedule : existingSchedules ) {
			if( existingSchedule.getCustomer().getId().equals( customer.getId() ) 
					&& !existingSchedule.getId().equals( schedule.getId() ) ) {
				return true;
			}
		}
		
		
		return false;
	}
}
