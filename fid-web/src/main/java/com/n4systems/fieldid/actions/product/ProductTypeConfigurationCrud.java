package com.n4systems.fieldid.actions.product;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.model.AssetType;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;


import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.ejb.legacy.LegacyProductType;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.utils.StrutsListHelper;
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
	private AssetType assetType;
	
	private List<ListingPair> assetTypes;
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
		assetType =  getLoaderFactory().createProductTypeLoader().setId(uniqueId).setStandardPostFetches().load();
	}

	@SkipValidation
	public String doEdit() {
		if( assetType == null ) {
			addActionError( getText( "error.noproducttype" ) );
			return MISSING;
		}
		
		subProductIds = new ArrayList<Long>();
		for( AssetType subAssetType : assetType.getSubTypes() ) {
			subProductIds.add( subAssetType.getId() );
		}
		
		return SUCCESS;
	}
	
	public String doSave() {
		
		if( assetType == null ) {
			addActionError( getText( "error.noproducttype" ) );
			return MISSING;
		}
		if( isPartOfMasterProduct() ) {
			addActionError( getText( "error.alreadyasubproducttype" ) );
			return ERROR;
		}
		
		try {
			assetType.getSubTypes().clear();
			if( !subProductIds.isEmpty() ) {
				StrutsListHelper.clearNulls( subProductIds );
				QueryBuilder<AssetType> subTypeQuery = new QueryBuilder<AssetType>(AssetType.class, getSecurityFilter());
				subTypeQuery.addWhere( Comparator.IN, "productIds", "id", subProductIds );
				subTypeQuery.setSimpleSelect();
				subTypeQuery.setOrder( "name" );
				assetType.getSubTypes().addAll( persistenceManager.findAll( subTypeQuery ) );
			}
			
			assetType = productTypeManager.updateProductType(assetType);
			addFlashMessage( getText( "message.productconfigurationsaved" ) );
		} catch ( Exception e ) {
			logger.error( "could not update the asset configuration", e );
			addActionError( getText( "error.productconfiguration" ) );
		}
		
		return SUCCESS;
	}

	public AssetType getAssetType() {
		return assetType;
	}

	public void setAssetType( AssetType assetType) {
		this.assetType = assetType;
	}

	public List<ListingPair> getAssetTypes() {
		if( assetTypes == null ) {
			assetTypes = productManager.getAllowedSubTypes( getSecurityFilter(), assetType);
			List<ListingPair> typesToBeRemoved = new ArrayList<ListingPair>();
			for( ListingPair type : assetTypes) {
				for( Long id : subProductIds ) {
					if( id != null && id.equals( type.getId() ) ) {
						typesToBeRemoved.add( type );
					}
				}
			
			}
			assetTypes.removeAll( typesToBeRemoved );
		}
		return assetTypes;
	}

	public List<ListingPair> getSubProducts() {
		if( subProducts == null ) {
			subProducts = new ArrayList<ListingPair>();
			for( ListingPair type : productManager.getAllowedSubTypes( getSecurityFilter(), assetType) ) {
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
		
		return  !productManager.getAllowedSubTypes( getSecurityFilter(), assetType).contains( new ListingPair( typeId, "placeholder" ) ) ;
		
	}
	
	public boolean isPartOfMasterProduct() {
		return productManager.partOfAMasterProduct( uniqueID );
	}
}
