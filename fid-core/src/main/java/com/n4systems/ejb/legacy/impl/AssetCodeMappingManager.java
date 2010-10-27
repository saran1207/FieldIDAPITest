package com.n4systems.ejb.legacy.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.n4systems.ejb.legacy.AssetCodeMappingService;
import com.n4systems.model.AssetType;
import org.apache.log4j.Logger;

import rfid.ejb.entity.AssetCodeMapping;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.impl.PersistenceManagerImpl;
import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.persistence.QueryBuilder;


public class AssetCodeMappingManager implements AssetCodeMappingService {
	private Logger logger = Logger.getLogger(AssetCodeMappingService.class);
	
	
	private EntityManager em;
	
	private PersistenceManager persistenceManager;
		
	
	public AssetCodeMappingManager(EntityManager em) {
		this.em = em;
		this.persistenceManager = new PersistenceManagerImpl(em);
	}

	
	@SuppressWarnings("unchecked")
	public List<AssetCodeMapping> getAllAssetCodesByTenant( Long r_manufacturer ) {
		Query q = em.createQuery("from "+AssetCodeMapping.class.getName()+" as pcm where pcm.tenant.id = :manufacturer ORDER BY assetCode ");
		q.setParameter( "manufacturer", r_manufacturer );
		return q.getResultList();
	}
	
	public AssetCodeMapping getAssetCodeByAssetCodeAndTenant(String assetCode, Long tenantId ) {
		Query q = em.createQuery("SELECT DISTINCT pcm from "+AssetCodeMapping.class.getName()+" as pcm left join fetch pcm.assetInfo as pi left join fetch pi.infoFields where pcm.tenant.id = :manufacturer AND pcm.assetCode = :assetCode ");
		q.setParameter( "assetCode", assetCode);
		q.setParameter( "manufacturer", tenantId );
		
		QueryBuilder<AssetCodeMapping> builder = new QueryBuilder<AssetCodeMapping>(AssetCodeMapping.class, new OpenSecurityFilter());
		builder.addSimpleWhere("assetCode", assetCode);
		builder.addSimpleWhere("tenant.id", tenantId);
		
		AssetCodeMapping assetMapping = null;
		try {
			assetMapping = persistenceManager.find(builder);
		} catch(InvalidQueryException e) {
			logger.error("Failed loading AssetCodeMapping", e);
		}
		
		// if we were unable to find an asset code mapping, return the default
		if(assetMapping == null) {
			assetMapping = getDefaultMapping(tenantId);
		}
		
		return assetMapping;
	}
	
	private AssetCodeMapping getDefaultMapping(Long tenantId) {
		AssetCodeMapping defaultMapping = new AssetCodeMapping();
		defaultMapping.setAssetInfo(defaultAssetType(tenantId));
		return defaultMapping;
	}
	
	private AssetType defaultAssetType( Long tenantId ) {
		// find the default asset type name for this tenant
		String defaultTypeName = ConfigContext.getCurrentContext().getString(ConfigEntry.DEFAULT_PRODUCT_TYPE_NAME, tenantId);
		
		QueryBuilder<AssetType> builder = new QueryBuilder<AssetType>(AssetType.class, new OpenSecurityFilter());
		builder.addSimpleWhere("tenant.id", tenantId);
		builder.addSimpleWhere("name", defaultTypeName);
		
		AssetType type = null;
		try {
			type = persistenceManager.find(builder);
		} catch(InvalidQueryException e) {
			logger.error("Failed finding default AssetType", e);
		}
		
		return type;
	}


	
	public AssetCodeMapping getAssetCodeByUniqueIdAndTenant( Long id, Long manufacturer ) {
		Query q = em.createQuery("SELECT DISTINCT pcm from "+AssetCodeMapping.class.getName()+" as pcm left join fetch pcm.assetInfo as pi left join fetch pi.infoFields where pcm.tenant.id = :manufacturer AND pcm.uniqueID = :uniqueID ");
		q.setParameter( "uniqueID", id );
		q.setParameter( "manufacturer", manufacturer );
		AssetCodeMapping bean = null;
		try {
			bean = (AssetCodeMapping)q.getSingleResult();
			
		} catch( NoResultException e ) {
			// assetCode doesn't exist for this tenant return null
		}
		return bean;
		
	}
	
	public void update(AssetCodeMapping assetCodeMapping) {
		em.merge(assetCodeMapping);
	}

	
	public void deleteByIdAndTenant(Long uniqueID, Long r_manufacturer ) {
		Query q = em.createQuery("from "+AssetCodeMapping.class.getName()+" as pcm where pcm.tenant.id = :manufacturer AND pcm.uniqueID = :uniqueID ");
		q.setParameter( "uniqueID", uniqueID );
		q.setParameter( "manufacturer", r_manufacturer );
		try { 
			AssetCodeMapping bean = (AssetCodeMapping)q.getSingleResult();
			em.remove( bean );
		} catch( NoResultException e ) {
			// do nothing if the bean isn't found technically it isn't in the database.
		}
		
		
	}
	
	@SuppressWarnings("unchecked")
	public void clearRetiredInfoFields( AssetType assetType) {
		List<Long> retiredFields = new ArrayList<Long>();
		
		for (InfoFieldBean infoField : assetType.getInfoFields() ) {
			if( infoField.isRetired() ) {
				retiredFields.add( infoField.getUniqueID() );
			}
		}
		
		if( ! retiredFields.isEmpty() ) {
			Query q = em.createQuery("from "+AssetCodeMapping.class.getName()+" as pcm where pcm.assetInfo.id = :assetTypeId ");
			q.setParameter( "assetTypeId", assetType.getId() );
			
			List<AssetCodeMapping> beans = (List<AssetCodeMapping>)q.getResultList();

			for (AssetCodeMapping assetCodeMapping : beans) {
				List<InfoOptionBean> removedInfoOption = new ArrayList<InfoOptionBean>();
				for (InfoOptionBean infoOption : assetCodeMapping.getInfoOptions() ) {
					if( retiredFields.contains( infoOption.getInfoField().getUniqueID() ) ) {
						removedInfoOption.add( infoOption );
					}
				}
				assetCodeMapping.getInfoOptions().removeAll( removedInfoOption );
				update(assetCodeMapping);
			}
		}
	}
}
