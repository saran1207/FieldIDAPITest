package com.n4systems.fieldid.actions.product;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.session.LegacyProductType;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.utils.ListHelper;
import com.n4systems.model.ProductType;
import com.n4systems.security.Permissions;
import com.n4systems.util.ListingPair;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;

@UserPermissionFilter(userRequiresOneOf={Permissions.ManageSystemConfig})
public class ProductTypeConfigurationCrud extends AbstractCrud {
		     
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger( ProductTypeConfigurationCrud.class );
	
	private LegacyProductType productTypeManager;
	private ProductManager productManager;
	private ProductType productType;
	
	private List<ListingPair> productTypes;
	private List<ListingPair> subProducts;
	private List<Long> subProductIds = new ArrayList<Long>();
	public ProductTypeConfigurationCrud( LegacyProductType productTypeManager, PersistenceManager persistenceManager, ProductManager productManager ) {
		super(persistenceManager);
		this.productTypeManager = productTypeManager;
		this.productManager = productManager;
	}

	@Override
	protected void initMemberFields() {
	}

	@Override
	protected void loadMemberFields( Long uniqueId ) {
		productType =  getLoaderFactory().createProductTypeLoader().setId(uniqueId).setStandardPostFetches().load();
	}

	@SkipValidation
	public String doEdit() {
		if( productType == null ) {
			addActionError( getText( "error.noproducttype" ) );
			return MISSING;
		}
		
		subProductIds = new ArrayList<Long>();
		for( ProductType subProductType : productType.getSubTypes() ) {
			subProductIds.add( subProductType.getId() );
		}
		
		return SUCCESS;
	}
	
	public String doSave() {
		
		if( productType == null ) {
			addActionError( getText( "error.noproducttype" ) );
			return MISSING;
		}
		if( isPartOfMasterProduct() ) {
			addActionError( getText( "error.alreadyasubproducttype" ) );
			return ERROR;
		}
		
		try {
			productType.getSubTypes().clear();
			if( !subProductIds.isEmpty() ) {
				ListHelper.clearNulls( subProductIds );
				QueryBuilder<ProductType> subTypeQuery = new QueryBuilder<ProductType>(ProductType.class, getSecurityFilter());
				subTypeQuery.addWhere( Comparator.IN, "productIds", "id", subProductIds );
				subTypeQuery.setSimpleSelect();
				subTypeQuery.setOrder( "name" );
				productType.getSubTypes().addAll( persistenceManager.findAll( subTypeQuery ) );
			}
			
			productType = productTypeManager.updateProductType( productType );
			addFlashMessage( getText( "message.productconfigurationsaved" ) );
		} catch ( Exception e ) {
			logger.error( "could not update the product configuration", e );
			addActionError( getText( "error.productconfiguration" ) );
		}
		
		return SUCCESS;
	}

	public ProductType getProductType() {
		return productType;
	}

	public void setProductType( ProductType productType ) {
		this.productType = productType;
	}

	public List<ListingPair> getProductTypes() {
		if( productTypes == null ) {
			productTypes = productManager.getAllowedSubTypes( getSecurityFilter(), productType );
			List<ListingPair> typesToBeRemoved = new ArrayList<ListingPair>();
			for( ListingPair type : productTypes ) {
				for( Long id : subProductIds ) {
					if( id != null && id.equals( type.getId() ) ) {
						typesToBeRemoved.add( type );
					}
				}
			
			}
			productTypes.removeAll( typesToBeRemoved );
		}
		return productTypes;
	}

	public List<ListingPair> getSubProducts() {
		if( subProducts == null ) {
			subProducts = new ArrayList<ListingPair>();
			for( ListingPair type : productManager.getAllowedSubTypes( getSecurityFilter(), productType ) ) {
				for( Long id : subProductIds ) {
					if( id != null && id.equals( type.getId() ) ) {
						subProducts.add( type );
					}
				}
			}
		}
		return subProducts;
	}

	
	@CustomValidator( type="subProductValidator", message="", key="error.parentproduct" )
	public List<Long> getSubProductIds() {
		return subProductIds;
	}

	
	public void setSubProductIds( List<Long> subProductIds ) {
		this.subProductIds = subProductIds;
	}
	
	public boolean isParentType( Long typeId ) {
		
		return  !productManager.getAllowedSubTypes( getSecurityFilter(), productType ).contains( new ListingPair( typeId, "placeholder" ) ) ;
		
	}
	
	public boolean isPartOfMasterProduct() {
		return productManager.partOfAMasterProduct( uniqueID );
	}
}
