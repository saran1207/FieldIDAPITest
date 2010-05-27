package com.n4systems.ejb.legacy.impl;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import javax.persistence.Query;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;
import rfid.ejb.entity.ProductCodeMappingBean;

import com.n4systems.ejb.AutoAttributeManager;
import com.n4systems.ejb.InspectionScheduleManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.impl.AutoAttributeManagerImpl;
import com.n4systems.ejb.impl.InspectionScheduleManagerImpl;
import com.n4systems.ejb.impl.PersistenceManagerImpl;
import com.n4systems.ejb.legacy.LegacyProductSerial;
import com.n4systems.ejb.legacy.LegacyProductType;
import com.n4systems.ejb.legacy.ProductCodeMapping;
import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.ImageAttachmentException;
import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.Product;
import com.n4systems.model.ProductType;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.reporting.PathHandler;
import com.n4systems.tools.Page;
import com.n4systems.tools.Pager;
import com.n4systems.util.ListingPair;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameter.Comparator;

 
public class LegacyProductTypeManager implements LegacyProductType {
	private static Logger logger = Logger.getLogger( LegacyProductTypeManager.class );
	
	
	protected EntityManager em;

	 protected PersistenceManager persistenceManager;
	 protected ProductCodeMapping productCodeMappingManager;
	 protected AutoAttributeManager autoAttributeManager;	
	 protected LegacyProductSerial productSerialManager;
	 protected InspectionScheduleManager inspectionScheduleManager;
	
	
	
	
	
	public LegacyProductTypeManager(EntityManager em) {
		this.em = em;
		
		this.persistenceManager = new PersistenceManagerImpl(em);
		this.productCodeMappingManager = new ProductCodeMappingManager(em);
		this.autoAttributeManager = new AutoAttributeManagerImpl(em);	
		this.productSerialManager = new LegacyProductSerialManager(em);
		this.inspectionScheduleManager = new InspectionScheduleManagerImpl(em);
	}

	//TODO remove this only used by product crud to determine if the product type has changed.
	public ProductType findProductTypeForProduct(Long productId) throws InvalidQueryException {
		ProductType productTypeBean = null;
		try {
			
			QueryBuilder<ProductType> qbuilder = new QueryBuilder<ProductType>(Product.class, new OpenSecurityFilter(), "p");
			qbuilder.setSimpleSelect("type");
			qbuilder.addSimpleWhere("id", productId);
			
			productTypeBean = (ProductType)qbuilder.createQuery(em).getSingleResult();
			
		} catch(NoResultException e) {}
		
		return productTypeBean;
	}
	
	public ProductType findDefaultProductType(Long tenantId) {
		QueryBuilder<ProductType> query = new QueryBuilder<ProductType>(ProductType.class, new TenantOnlySecurityFilter(tenantId));
		query.addWhere(Comparator.EQ, "name", "name", ProductType.DEFAULT_ITEM_NUMBER, WhereParameter.IGNORE_CASE);
		
		return query.getSingleResult(em);
	}
	
	@SuppressWarnings("unchecked")
	public List<ProductType> getProductTypesForTenant(Long tenantId) {
		Query query = em.createQuery("from " + ProductType.class.getName() + " pi where pi.tenant.id = :tenantId AND " +
				"pi.state = :activeState");
		query.setParameter("tenantId", tenantId).setParameter("activeState", EntityState.ACTIVE);
		

		List<ProductType> productTypes = (List<ProductType>)query.getResultList();
		
		return (List<ProductType>) persistenceManager.postFetchFields(productTypes, "infoFields", "inspectionTypes", "subTypes");
	}
	
	public ProductType updateProductType(ProductType productTypeBean, List<FileAttachment> uploadedFiles, File productImage ) throws FileAttachmentException, ImageAttachmentException {
			ProductType oldPI = null;
			if( productTypeBean.getId() != null ) {
				oldPI = (ProductType)em.find( ProductType.class, productTypeBean.getId() );
			}
			if( oldPI != null ) {
				cleanInfoFields( productTypeBean, oldPI );
			}
			
			productTypeBean.touch();
			productTypeBean = em.merge( productTypeBean );
			processUploadedFiles( productTypeBean, uploadedFiles );
 			processProductImage( productTypeBean, productImage );
			return productTypeBean;
		
	}

	
	
	private void cleanInfoFields( ProductType productTypeBean, ProductType oldPI ) {
		productCodeMappingManager.clearRetiredInfoFields( productTypeBean );
		autoAttributeManager.clearRetiredInfoFields( productTypeBean );
		
		// the removal of old infofields needs to be done after the clearing of retired fields otherwise
		// they will get a persit with deleted entity exception.
		for( InfoFieldBean field : oldPI.getInfoFields() ) {
		    if (!productTypeBean.getInfoFields().contains( field )) {
		    	for (InfoOptionBean infoOpiton : field.getUnfilteredInfoOptions() ) {
		    		em.remove(infoOpiton);
				}
		        em.remove(field);
		    }
		}
	}
	
	
	private void processProductImage( ProductType productType, File productImage ) throws ImageAttachmentException{
		File imageDirectory = PathHandler.getProductTypeImageFile( productType );
		// clear the old file if we have a new one uploaded or the image has been removed.
		if( productType.getImageName() == null || productImage != null ) {
			if( imageDirectory.exists() && imageDirectory.isDirectory() ) {
				try {
					FileUtils.cleanDirectory( imageDirectory );
				} catch (Exception e) {
					throw new ImageAttachmentException( e );
				}
			}
		}
		
		if( productImage != null ) {
			try {
				File imageFile = new File( imageDirectory, productType.getImageName() );
				FileUtils.copyFile( productImage, imageFile );
				productImage.delete();
			} catch (Exception e) {
				throw new ImageAttachmentException( e );
			}
		}
	}
	
	
	

		
	@SuppressWarnings("unchecked")
	public List<ListingPair> getProductTypeListForTenant(Long tenantId) {
		Query query = em.createQuery("select new " + ListingPair.class.getName() + "( pi.id, pi.name ) from " + ProductType.class.getName() + " pi where pi.tenant.id = :tenantId AND state=:activeState order by pi.name ");
		query.setParameter("tenantId", tenantId).setParameter("activeState", EntityState.ACTIVE);
		
		List<ListingPair> piList = (List<ListingPair>)query.getResultList();
		
		return piList;
	}
	
	/**
	 * finds the infoFields that are currently in Use.
	 * @param infoFields
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Collection<Long> infoFieldsInUse( Collection<InfoFieldBean> infoFields ) {

		Collection<Long> infoFieldsCurrentlyInUse = new ArrayList<Long>();
		
		
		if( infoFields == null ) {
			return infoFieldsCurrentlyInUse;
		}
		
		// make sure all the info fields have been persisted and remove the ones that aren't.
		Collection<InfoFieldBean> cleanedInfoFields = new ArrayList<InfoFieldBean>();
		for (InfoFieldBean infoFieldBean : infoFields ) {
			if( infoFieldBean.getUniqueID() != null ) {
				cleanedInfoFields.add( infoFieldBean );
			}
		}

		if( infoFields != null && cleanedInfoFields.size() > 0 ) {
			Query query = em.createQuery("select DISTINCT ifb.uniqueID  from " + Product.class.getName() + " ps inner join  ps.infoOptions as io inner join io.infoField as ifb where ps.type.id = :productInfo and ifb in( :infoFieldIds )" );
			
			query.setParameter("productInfo", infoFields.iterator().next().getProductInfo().getId() );
			query.setParameter("infoFieldIds", infoFields );
			
			infoFieldsCurrentlyInUse.addAll( query.getResultList() );
			
						
			query = em.createQuery("select DISTINCT ifb.uniqueID from " + ProductCodeMappingBean.class.getName() + " as pcm inner join pcm.infoOptions as io inner join io.infoField as ifb where pcm.productInfo.id = :productInfo and ifb in( :infoFieldIds )" );
			
			query.setParameter("productInfo", infoFields.iterator().next().getProductInfo().getId() );
			query.setParameter("infoFieldIds", infoFields );
			
			infoFieldsCurrentlyInUse.addAll( query.getResultList() );
			
			
			query = em.createQuery("select DISTINCT ifb.uniqueID from " + AutoAttributeCriteria.class.getName() + " as aac inner join aac.inputs as ifb where aac.productType.id = :productInfo and ifb in( :infoFieldIds )" );
			
			query.setParameter("productInfo", infoFields.iterator().next().getProductInfo().getId() );
			query.setParameter("infoFieldIds", infoFields );
			
			
			query = em.createQuery("select DISTINCT ifb.uniqueID from " + AutoAttributeCriteria.class.getName() + " as aac inner join aac.outputs as ifb where aac.productType.id = :productInfo and ifb in( :infoFieldIds )" );
			
			query.setParameter("productInfo", infoFields.iterator().next().getProductInfo().getId() );
			query.setParameter("infoFieldIds", infoFields );
			infoFieldsCurrentlyInUse.addAll( query.getResultList() );
		}

		return infoFieldsCurrentlyInUse;
	}
	
	/**
	 * returns the set of non static info option ids
	 * based on the page number and size sent in.
	 *
	 * @return
	 */
	private Pager<Long> findInfoOptions( int pageNumber, int pageSize ) {
		Query idQuery;
		Query countQuery;
		idQuery = em.createQuery( "select io.uniqueID FROM InfoOptionBean as io WHERE io.staticData = false ORDER BY uniqueID" );
		countQuery = em.createQuery( "select count(*) FROM InfoOptionBean as io WHERE io.staticData = false" );
		return new Page<Long>( idQuery, countQuery, pageNumber, pageSize );
	}
	
	
	/**
	 * removes all orphaned info options and updates the modify time of the product type.
	 */
	@SuppressWarnings("unchecked")
	public boolean cleanInfoOptions( int pageNumber, int pageSize ) {
		Collection<Long> orphanInfoOptionIds;
		
		Pager<Long> infoOptionsToScan = findInfoOptions(pageNumber, pageSize);
		
		// look for info options that don't have a connection to a product serial.
		Query query = em.createQuery( "select DISTINCT io.uniqueID  from ProductSerialInfoOptionBean as psio right join psio.infoOption as io where io.uniqueID IN (:infoOptions) AND psio.uniqueID IS NULL " );
		query.setParameter( "infoOptions", infoOptionsToScan.getList() );
		orphanInfoOptionIds = (Collection<Long>)query.getResultList();
		if( orphanInfoOptionIds.size() > 0 ) {
			// look for info options that are not on product info histories.
			query = em.createQuery( "select DISTINCT io.uniqueID from InfoOptionBean as io where io.uniqueID IN (:infoOptions) AND io.uniqueID NOT IN ( select io.uniqueID from AddProductHistoryBean as aph left join aph.infoOptions as io where io.uniqueID IN (:infoOptions)  )  " );
			query.setParameter( "infoOptions", orphanInfoOptionIds );
			orphanInfoOptionIds = (Collection<Long>)query.getResultList();
		}
	
		
		if( orphanInfoOptionIds.size() > 0 ) {
			// look for info options that are not used on a ps template and where not used by any product serial.
			query = em.createQuery( "select DISTINCT io.uniqueID from InfoOptionBean as io where io.uniqueID IN (:infoOptions) AND io.uniqueID NOT IN ( select pcmio.uniqueID from ProductCodeMappingBean as pcm inner join pcm.infoOptions as pcmio )  " );
			query.setParameter( "infoOptions", orphanInfoOptionIds );
			orphanInfoOptionIds = query.getResultList();
		}
		
		if( orphanInfoOptionIds.size() > 0 ) {
			// look for info options that are not used on a ps template and where not used by any product serial.
			query = em.createQuery( "select DISTINCT io.uniqueID from InfoOptionBean as io where io.uniqueID IN (:infoOptions) AND io.uniqueID NOT IN ( select aadio.uniqueID from AutoAttributeDefinition as aad inner join aad.inputs as aadio )  " );
			query.setParameter( "infoOptions", orphanInfoOptionIds );
			orphanInfoOptionIds = query.getResultList();
		}
		
		if( orphanInfoOptionIds.size() > 0 ) {
			// look for info options that are not used on a ps template and where not used by any product serial.
			query = em.createQuery( "select DISTINCT io.uniqueID from InfoOptionBean as io where io.uniqueID IN (:infoOptions) AND io.uniqueID NOT IN ( select aadio.uniqueID from AutoAttributeDefinition as aad inner join aad.outputs as aadio )  " );
			query.setParameter( "infoOptions", orphanInfoOptionIds );
			orphanInfoOptionIds = query.getResultList();
		}
		
		// remove the orphans.
		for ( Long infoOptionId : orphanInfoOptionIds) {
			InfoOptionBean infoOption = em.find(InfoOptionBean.class, infoOptionId );
			em.remove( infoOption ) ;
		}
		 
		return infoOptionsToScan.isHasNextPage();
	}

	private void processUploadedFiles( ProductType productType, List<FileAttachment> uploadedFiles ) throws FileAttachmentException {
		File attachmentDirectory = PathHandler.getProductTypeAttachmentFile(productType);
		File tmpDirectory = PathHandler.getTempRoot();
		
		if( uploadedFiles != null ) {
			File tmpFile;
			// move and attach each uploaded file
			for(FileAttachment uploadedFile: uploadedFiles) {

				try {	
					// move the file to it's new location, note that it's location is currently relative to the tmpDirectory
					tmpFile = new File(tmpDirectory, uploadedFile.getFileName());
					FileUtils.copyFileToDirectory(tmpFile, attachmentDirectory);
					
					// clean up the temp file
					tmpFile.delete();
					
					// now we need to set the correct file name for the attachment and set the modifiedBy
					uploadedFile.setFileName(tmpFile.getName());
					uploadedFile.setTenant(productType.getTenant());
					uploadedFile.setModifiedBy(productType.getModifiedBy());
					
					// attach the attachment
					productType.getAttachments().add(uploadedFile);
				} catch (IOException e) {
					logger.error("failed to copy uploaded file ", e);
					throw new FileAttachmentException(e);
				}
				
			}
			
			persistenceManager.update(productType);
		}
		
		// Now we need to cleanup any files that are no longer attached to the producttype
		if(attachmentDirectory.exists()) {
			
			/*
			 * We'll start by constructing a list of attached file names which will be used in 
			 * a directory filter
			 */
			final List<String> attachedFiles = new ArrayList<String>();
			for(FileAttachment file: productType.getAttachments()) {
				attachedFiles.add(file.getFileName());
			}
		
			/*
			 * This lists all files in the attachment directory 
			 */
			for(File detachedFile: attachmentDirectory.listFiles(
					new FilenameFilter() {
						public boolean accept(File dir, String name) {
							// accept only files that are not in our attachedFiles list
							return !attachedFiles.contains(name);
						}
					}
			)) {
				/* 
				 * any file returned from our fileNotAttachedFilter, is not in our attached file list
				 * and should be removed
				 */
				detachedFile.delete();
				
			}
		}
		
	}
	
	public ProductType updateProductType(ProductType productTypeBean ) throws FileAttachmentException, ImageAttachmentException {
		return updateProductType(productTypeBean, (List<FileAttachment>)null, null );
	}
	
}
