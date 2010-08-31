package com.n4systems.ejb.legacy.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;
import rfid.ejb.entity.ProductCodeMappingBean;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.impl.PersistenceManagerImpl;
import com.n4systems.ejb.legacy.ProductCodeMapping;
import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.model.ProductType;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.persistence.QueryBuilder;


public class ProductCodeMappingManager implements ProductCodeMapping {
	private Logger logger = Logger.getLogger(ProductCodeMapping.class);
	
	
	private EntityManager em;
	
	private PersistenceManager persistenceManager;
		
	
	public ProductCodeMappingManager(EntityManager em) {
		this.em = em;
		this.persistenceManager = new PersistenceManagerImpl(em);
	}

	
	@SuppressWarnings("unchecked")
	public List<ProductCodeMappingBean> getAllProductCodesByTenant( Long r_manufacturer ) {
		Query q = em.createQuery("from ProductCodeMappingBean as pcm where pcm.tenant.id = :manufacturer ORDER BY productCode ");
		q.setParameter( "manufacturer", r_manufacturer );
		return q.getResultList();
	}
	
	public ProductCodeMappingBean getProductCodeByProductCodeAndTenant(String productCode, Long tenantId ) {
		Query q = em.createQuery("SELECT DISTINCT pcm from ProductCodeMappingBean as pcm left join fetch pcm.productInfo as pi left join fetch pi.infoFields where pcm.tenant.id = :manufacturer AND pcm.productCode = :productCode ");
		q.setParameter( "productCode", productCode );
		q.setParameter( "manufacturer", tenantId );
		
		QueryBuilder<ProductCodeMappingBean> builder = new QueryBuilder<ProductCodeMappingBean>(ProductCodeMappingBean.class, new OpenSecurityFilter());
		builder.addSimpleWhere("productCode", productCode);
		builder.addSimpleWhere("tenant.id", tenantId);
		
		ProductCodeMappingBean productMapping = null;
		try {
			productMapping = persistenceManager.find(builder);
		} catch(InvalidQueryException e) {
			logger.error("Failed loading ProductCodeMappingBean", e);
		}
		
		// if we were unable to find a product code mapping, return the default
		if(productMapping == null) {
			productMapping = getDefaultMapping(tenantId);
		}
		
		return productMapping;
	}
	
	private ProductCodeMappingBean getDefaultMapping(Long tenantId) {
		ProductCodeMappingBean defaultMapping = new ProductCodeMappingBean();
		defaultMapping.setProductInfo(defaultProductType(tenantId));
		return defaultMapping;
	}
	
	private ProductType defaultProductType( Long tenantId ) {
		// find the default product type name for this tenant
		String defaultTypeName = ConfigContext.getCurrentContext().getString(ConfigEntry.DEFAULT_PRODUCT_TYPE_NAME, tenantId);
		
		QueryBuilder<ProductType> builder = new QueryBuilder<ProductType>(ProductType.class, new OpenSecurityFilter());
		builder.addSimpleWhere("tenant.id", tenantId);
		builder.addSimpleWhere("name", defaultTypeName);
		
		ProductType type = null;
		try {
			type = persistenceManager.find(builder);
		} catch(InvalidQueryException e) {
			logger.error("Failed finding default ProductType", e);
		}
		
		return type;
	}


	
	public ProductCodeMappingBean getProductCodeByUniqueIdAndTenant( Long id, Long manufacturer ) {
		Query q = em.createQuery("SELECT DISTINCT pcm from ProductCodeMappingBean as pcm left join fetch pcm.productInfo as pi left join fetch pi.infoFields where pcm.tenant.id = :manufacturer AND pcm.uniqueID = :uniqueID ");
		q.setParameter( "uniqueID", id );
		q.setParameter( "manufacturer", manufacturer );
		ProductCodeMappingBean bean = null;
		try {
			bean = (ProductCodeMappingBean)q.getSingleResult();
			
		} catch( NoResultException e ) {
			// productCode doesn't exist for this tenant return null
		}
		return bean;
		
	}
	
	public void update(ProductCodeMappingBean productCodeMapping) {
		em.merge( productCodeMapping );
	}

	
	public void deleteByIdAndTenant(Long uniqueID, Long r_manufacturer ) {
		Query q = em.createQuery("from ProductCodeMappingBean as pcm where pcm.tenant.id = :manufacturer AND pcm.uniqueID = :uniqueID ");
		q.setParameter( "uniqueID", uniqueID );
		q.setParameter( "manufacturer", r_manufacturer );
		try { 
			ProductCodeMappingBean bean = (ProductCodeMappingBean)q.getSingleResult();
			em.remove( bean );
		} catch( NoResultException e ) {
			// do nothing if the bean isn't found technically it isn't in the database.
		}
		
		
	}
	
	@SuppressWarnings("unchecked")
	public void clearRetiredInfoFields( ProductType productType ) {
		List<Long> retiredFields = new ArrayList<Long>();
		
		for (InfoFieldBean infoField : productType.getInfoFields() ) {
			if( infoField.isRetired() ) {
				retiredFields.add( infoField.getUniqueID() );
			}
		}
		
		if( ! retiredFields.isEmpty() ) {
			Query q = em.createQuery("from ProductCodeMappingBean as pcm where pcm.productInfo.id = :productTypeId ");
			q.setParameter( "productTypeId", productType.getId() );
			
			List<ProductCodeMappingBean> beans = (List<ProductCodeMappingBean>)q.getResultList();

			for (ProductCodeMappingBean productCodeMappingBean : beans) {
				List<InfoOptionBean> removedInfoOption = new ArrayList<InfoOptionBean>();
				for (InfoOptionBean infoOption : productCodeMappingBean.getInfoOptions() ) {
					if( retiredFields.contains( infoOption.getInfoField().getUniqueID() ) ) {
						removedInfoOption.add( infoOption );
					}
				}
				productCodeMappingBean.getInfoOptions().removeAll( removedInfoOption );
				update( productCodeMappingBean );
			}
		}
	}
}
