package com.n4systems.fieldid.actions.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import rfid.ejb.session.LegacyProductType;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.model.InspectionType;
import com.n4systems.model.ProductType;

public class ProductTypeInspectionTypeCrud extends AbstractCrud {
	
	
	private static Logger logger = Logger.getLogger( ProductTypeInspectionTypeCrud.class );
	private static final long serialVersionUID = 1L;
	
	private ProductType productType;
	private List<InspectionType> inspectionTypes;
	private List<Boolean> productTypeInspections;
	
	private LegacyProductType productTypeManager;
	public ProductTypeInspectionTypeCrud( LegacyProductType productTypeManager, PersistenceManager persistenceManager ) {
		super(persistenceManager);
		this.productTypeManager = productTypeManager;
	}

	@Override
	protected void initMemberFields() {
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
	}
	
	public String doList() {
		return SUCCESS;
	}
	
	public String doSave() {
		List<InspectionType> selectedInspectionTypes = new ArrayList<InspectionType>();
		
		
		for( InspectionType inspectionType : getInspectionTypes() ) {
			if( productTypeInspections.get( getInspectionTypes().indexOf( inspectionType ) ) ) {
				selectedInspectionTypes.add( inspectionType );
			}
		}
		
		productType.getInspectionTypes().clear();
		productType.getInspectionTypes().addAll( selectedInspectionTypes );
		
		
		try{
			productType = productTypeManager.updateProductType(productType);
			addFlashMessage( getText( "message.inspectiontypesselected" ) );
		} catch (Exception e) {
			addActionError( getText( "error.failedtosaveinspectiontypeselection" ) );
			logger.error( "failed to change the inspection type selection", e );
			return ERROR;
		}
		
		
		return SUCCESS;
	}

	/**
	 * @return the productType
	 */
	public Long getProductTypeId() {
		return ( productType != null ) ? productType.getId() : null;
	}

	/**
	 * @param productType the productType to set
	 */
	public void setProductTypeId( Long productType) {
		if( productType == null ) {
			this.productType = null;
		} else if( this.productType == null || productType.equals( this.productType.getId() ) ) {
			this.productType = productTypeManager.findProductTypeAllFields( productType, getTenantId() );
		}
	}
	
	public ProductType getProductType() {
		return productType;
	}

	/**
	 * @return the productTypeEvents
	 */
	public List<Boolean> getProductTypeInspectionTypes() {
		if( productTypeInspections == null ) {
			productTypeInspections = new ArrayList<Boolean>();
			for (InspectionType inspectionType : getInspectionTypes() ) {
				boolean found= false;
				for( InspectionType event : productType.getInspectionTypes() ) {
					if( inspectionType.getId().equals( event.getId() ) ) {
						productTypeInspections.add( true );
						found = true;
						break;
					} 
				}	
				if( !found ) {
					productTypeInspections.add( false );
				}
			}
		}
		return productTypeInspections;
	}

	/**
	 * @param productTypeEvents the productTypeEvents to set
	 */
	public void setProductTypeInspectionTypes(List<Boolean> productTypeInspections) {
		this.productTypeInspections = productTypeInspections;
	}

	/**
	 * @return the eventTypes
	 */
	public List<InspectionType> getInspectionTypes() {
		if( inspectionTypes == null ) {
			Map<String,Boolean> orderBy = new HashMap<String, Boolean>();
			orderBy.put( "name", true );
			inspectionTypes = persistenceManager.findAll( InspectionType.class, getTenantId(), orderBy );
		}
		return inspectionTypes;
	}

}
