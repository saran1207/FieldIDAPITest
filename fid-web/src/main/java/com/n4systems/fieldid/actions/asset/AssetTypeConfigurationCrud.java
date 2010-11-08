package com.n4systems.fieldid.actions.asset;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.ejb.legacy.LegacyAssetType;
import com.n4systems.model.AssetType;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;


import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.AssetManager;
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
	
	private LegacyAssetType assetTypeManager;
	private AssetManager assetManager;
	private AssetType assetType;
	
	private List<ListingPair> assetTypes;
	private List<ListingPair> subAssets;
	private List<Long> subAssetIds = new ArrayList<Long>();
	public AssetTypeConfigurationCrud( LegacyAssetType assetTypeManager, PersistenceManager persistenceManager, AssetManager assetManager) {
		super(persistenceManager);
		this.assetTypeManager = assetTypeManager;
		this.assetManager = assetManager;
	}

	@Override
	protected void initMemberFields() {
	}

	@Override
	protected void loadMemberFields( Long uniqueId ) {
		assetType =  getLoaderFactory().createAssetTypeLoader().setId(uniqueId).setStandardPostFetches().load();
	}

	@SkipValidation
	public String doEdit() {
		if( assetType == null ) {
			addActionError( getText( "error.noassettype" ) );
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
			addActionError( getText( "error.noassettype" ) );
			return MISSING;
		}
		if( isPartOfMasterAsset() ) {
			addActionError( getText( "error.alreadyasubassettype" ) );
			return ERROR;
		}
		
		try {
			assetType.getSubTypes().clear();
			if( !subAssetIds.isEmpty() ) {
				StrutsListHelper.clearNulls(subAssetIds);
				QueryBuilder<AssetType> subTypeQuery = new QueryBuilder<AssetType>(AssetType.class, getSecurityFilter());
				subTypeQuery.addWhere( Comparator.IN, "assetIds", "id", subAssetIds);
				subTypeQuery.setSimpleSelect();
				subTypeQuery.setOrder( "name" );
				assetType.getSubTypes().addAll( persistenceManager.findAll( subTypeQuery ) );
			}
			
			assetType = assetTypeManager.updateAssetType(assetType);
			addFlashMessage( getText( "message.assetconfigurationsaved" ) );
		} catch ( Exception e ) {
			logger.error( "could not update the asset configuration", e );
			addActionError( getText( "error.assetconfiguration" ) );
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
			assetTypes = assetManager.getAllowedSubTypes( getSecurityFilter(), assetType);
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
			for( ListingPair type : assetManager.getAllowedSubTypes( getSecurityFilter(), assetType) ) {
				for( Long id : subAssetIds) {
					if( id != null && id.equals( type.getId() ) ) {
						subAssets.add( type );
					}
				}
			}
		}
		return subAssets;
	}

	
	@CustomValidator( type="subAssetValidator", message="", key="error.parentasset" )
	public List<Long> getSubAssetIds() {
		return subAssetIds;
	}

	
	public void setSubAssetIds( List<Long> subAssetIds) {
		this.subAssetIds = subAssetIds;
	}
	
	public boolean isParentType( Long typeId ) {
		
		return  !assetManager.getAllowedSubTypes( getSecurityFilter(), assetType).contains( new ListingPair( typeId, "placeholder" ) ) ;
		
	}
	
	public boolean isPartOfMasterAsset() {
		return assetManager.partOfAMasterAsset( uniqueID );
	}
}
