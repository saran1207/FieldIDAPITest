package com.n4systems.fieldid.actions.asset;

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
public class AssetTypeConfigurationCrud extends AbstractCrud {
		     
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger( AssetTypeConfigurationCrud.class );
	
	private LegacyProductType productTypeManager;
	private ProductManager productManager;
	private AssetType assetType;
	
	private List<ListingPair> assetTypes;
	private List<ListingPair> subAssets;
	private List<Long> subAssetIds = new ArrayList<Long>();
	public AssetTypeConfigurationCrud( LegacyProductType productTypeManager, PersistenceManager persistenceManager, ProductManager productManager ) {
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
		
		subAssetIds = new ArrayList<Long>();
		for( AssetType subAssetType : assetType.getSubTypes() ) {
			subAssetIds.add( subAssetType.getId() );
		}
		
		return SUCCESS;
	}
	
	public String doSave() {
		
		if( assetType == null ) {
			addActionError( getText( "error.noproducttype" ) );
			return MISSING;
		}
		if( isPartOfMasterAsset() ) {
			addActionError( getText( "error.alreadyasubproducttype" ) );
			return ERROR;
		}
		
		try {
			assetType.getSubTypes().clear();
			if( !subAssetIds.isEmpty() ) {
				StrutsListHelper.clearNulls(subAssetIds);
				QueryBuilder<AssetType> subTypeQuery = new QueryBuilder<AssetType>(AssetType.class, getSecurityFilter());
				subTypeQuery.addWhere( Comparator.IN, "productIds", "id", subAssetIds);
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
				for( Long id : subAssetIds) {
					if( id != null && id.equals( type.getId() ) ) {
						typesToBeRemoved.add( type );
					}
				}
			
			}
			assetTypes.removeAll( typesToBeRemoved );
		}
		return assetTypes;
	}

	public List<ListingPair> getSubAssets() {
		if( subAssets == null ) {
			subAssets = new ArrayList<ListingPair>();
			for( ListingPair type : productManager.getAllowedSubTypes( getSecurityFilter(), assetType) ) {
				for( Long id : subAssetIds) {
					if( id != null && id.equals( type.getId() ) ) {
						subAssets.add( type );
					}
				}
			}
		}
		return subAssets;
	}

	
	@CustomValidator( type="subAssetValidator", message="", key="error.parentproduct" )
	public List<Long> getSubAssetIds() {
		return subAssetIds;
	}

	
	public void setSubAssetIds( List<Long> subAssetIds) {
		this.subAssetIds = subAssetIds;
	}
	
	public boolean isParentType( Long typeId ) {
		
		return  !productManager.getAllowedSubTypes( getSecurityFilter(), assetType).contains( new ListingPair( typeId, "placeholder" ) ) ;
		
	}
	
	public boolean isPartOfMasterAsset() {
		return productManager.partOfAMasterAsset( uniqueID );
	}
}
