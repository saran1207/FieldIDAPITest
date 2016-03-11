package com.n4systems.ejb.legacy.impl;

import com.amazonaws.AmazonClientException;
import com.n4systems.ejb.AutoAttributeManager;
import com.n4systems.ejb.EventScheduleManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.impl.AutoAttributeManagerImpl;
import com.n4systems.ejb.impl.EventScheduleManagerImpl;
import com.n4systems.ejb.impl.PersistenceManagerImpl;
import com.n4systems.ejb.legacy.AssetCodeMappingService;
import com.n4systems.ejb.legacy.LegacyAsset;
import com.n4systems.ejb.legacy.LegacyAssetType;
import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.ImageAttachmentException;
import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.reporting.PathHandler;
import com.n4systems.tools.Page;
import com.n4systems.tools.Pager;
import com.n4systems.util.ListingPair;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import org.apache.log4j.Logger;
import rfid.ejb.entity.*;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

 
public class LegacyAssetTypeManager implements LegacyAssetType {
	private static Logger logger = Logger.getLogger( LegacyAssetTypeManager.class );
	
	
	protected EntityManager em;

	 protected PersistenceManager persistenceManager;
	 protected AssetCodeMappingService assetCodeMappingServiceManager;
	 protected AutoAttributeManager autoAttributeManager;	
	 protected LegacyAsset assetManager;
	 protected EventScheduleManager eventScheduleManager;
	 protected S3Service s3Service;
	
	
	
	
	public LegacyAssetTypeManager(EntityManager em) {
		this.em = em;
		
		this.persistenceManager = new PersistenceManagerImpl(em);
		this.assetCodeMappingServiceManager = new AssetCodeMappingManager(em);
		this.autoAttributeManager = new AutoAttributeManagerImpl(em);	
		this.assetManager = new LegacyAssetManager(em);
		this.eventScheduleManager = new EventScheduleManagerImpl(em);
        this.s3Service = ServiceLocator.getS3Service();
	}

	//TODO remove this only used by asset crud to determine if the asset type has changed.
	public AssetType findAssetTypeForAsset(Long assetId) throws InvalidQueryException {
		AssetType assetType = null;
		try {
			
			QueryBuilder<AssetType> qbuilder = new QueryBuilder<AssetType>(Asset.class, new OpenSecurityFilter(), "p");
			qbuilder.setSimpleSelect("type");
			qbuilder.addSimpleWhere("id", assetId);
			
			assetType = (AssetType)qbuilder.createQuery(em).getSingleResult();
			
		} catch(NoResultException e) {}
		
		return assetType;
	}
	
	public AssetType findDefaultAssetType(Long tenantId) {
		QueryBuilder<AssetType> query = new QueryBuilder<AssetType>(AssetType.class, new TenantOnlySecurityFilter(tenantId));
		query.addWhere(Comparator.EQ, "name", "name", AssetType.DEFAULT_ITEM_NUMBER, WhereParameter.IGNORE_CASE);
		
		return query.getSingleResult(em);
	}
	
	@SuppressWarnings("unchecked")
	public List<AssetType> getAssetTypesForTenant(Long tenantId) {
		Query query = em.createQuery("from " + AssetType.class.getName() + " pi where pi.tenant.id = :tenantId AND " +
				"pi.state = :activeState");
		query.setParameter("tenantId", tenantId).setParameter("activeState", EntityState.ACTIVE);
		

		List<AssetType> assetTypes = (List<AssetType>)query.getResultList();
		
		return (List<AssetType>) persistenceManager.postFetchFields(assetTypes, "infoFields", "eventTypes", "subTypes");
	}
	
	public AssetType updateAssetType(AssetType assetType, List<FileAttachment> uploadedFiles, File assetImage ) throws FileAttachmentException, ImageAttachmentException {
			AssetType oldPI = null;
			if( assetType.getId() != null ) {
				oldPI = (AssetType)em.find( AssetType.class, assetType.getId() );
			}
			if( oldPI != null ) {
				cleanInfoFields(assetType, oldPI );
			}
			
			assetType.touch();
			assetType = em.merge(assetType);
			processUploadedFiles(assetType, uploadedFiles );
 			processAssetImage(assetType, assetImage );
			return assetType;
		
	}

	
	
	private void cleanInfoFields( AssetType assetType, AssetType oldPI ) {
		assetCodeMappingServiceManager.clearRetiredInfoFields(assetType);
		autoAttributeManager.clearRetiredInfoFields(assetType);
		
		// the removal of old infofields needs to be done after the clearing of retired fields otherwise
		// they will get a persit with deleted entity exception.
		for( InfoFieldBean field : oldPI.getInfoFields() ) {
		    if (!assetType.getInfoFields().contains( field )) {
		    	for (InfoOptionBean infoOpiton : field.getUnfilteredInfoOptions() ) {
		    		em.remove(infoOpiton);
				}
		        em.remove(field);
		    }
		}
	}
	
	
	private void processAssetImage( AssetType assetType, File assetImage ) throws ImageAttachmentException{
		if( assetImage != null ) {
			try {
                s3Service.uploadAssetTypeProfileImage(assetImage, assetType);
				assetImage.delete();
			} catch (Exception e) {
				throw new ImageAttachmentException( e );
			}
		}
	}
	
	
	

		
	@SuppressWarnings("unchecked")
	public List<ListingPair> getAssetTypeListForTenant(Long tenantId) {
		Query query = em.createQuery("select new " + ListingPair.class.getName() + "( pi.id, pi.name ) from " + AssetType.class.getName() + " pi where pi.tenant.id = :tenantId AND state=:activeState order by pi.name ");
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
			Query query = em.createQuery("select DISTINCT ifb.uniqueID  from " + Asset.class.getName() + " ps inner join  ps.infoOptions as io inner join io.infoField as ifb where ps.type.id = :assetInfo and ifb in( :infoFieldIds )" );
			
			query.setParameter("assetInfo", infoFields.iterator().next().getAssetInfo().getId() );
			query.setParameter("infoFieldIds", infoFields );
			
			infoFieldsCurrentlyInUse.addAll( query.getResultList() );
			
						
			query = em.createQuery("select DISTINCT ifb.uniqueID from " + AssetCodeMapping.class.getName() + " as pcm inner join pcm.infoOptions as io inner join io.infoField as ifb where pcm.assetInfo.id = :assetInfo and ifb in( :infoFieldIds )" );
			
			query.setParameter("assetInfo", infoFields.iterator().next().getAssetInfo().getId() );
			query.setParameter("infoFieldIds", infoFields );
			
			infoFieldsCurrentlyInUse.addAll( query.getResultList() );
			
			
			query = em.createQuery("select DISTINCT ifb.uniqueID from " + AutoAttributeCriteria.class.getName() + " as aac inner join aac.inputs as ifb where aac.assetType.id = :assetInfo and ifb in( :infoFieldIds )" );
			
			query.setParameter("assetInfo", infoFields.iterator().next().getAssetInfo().getId() );
			query.setParameter("infoFieldIds", infoFields );
			
			
			query = em.createQuery("select DISTINCT ifb.uniqueID from " + AutoAttributeCriteria.class.getName() + " as aac inner join aac.outputs as ifb where aac.assetType.id = :assetInfo and ifb in( :infoFieldIds )" );
			
			query.setParameter("assetInfo", infoFields.iterator().next().getAssetInfo().getId() );
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
	 * removes all orphaned info options and updates the modify time of the asset type.
	 */
	@SuppressWarnings("unchecked")
	public boolean cleanInfoOptions( int pageNumber, int pageSize ) {
		Collection<Long> orphanInfoOptionIds;
		
		Pager<Long> infoOptionsToScan = findInfoOptions(pageNumber, pageSize);
		
		// look for info options that don't have a connection to an asset serial.
		Query query = em.createQuery( "select DISTINCT io.uniqueID  from "+ AssetInfoOption.class.getName()+" as psio right join psio.infoOption as io where io.uniqueID IN (:infoOptions) AND psio.uniqueID IS NULL " );
		query.setParameter( "infoOptions", infoOptionsToScan.getList() );
		orphanInfoOptionIds = (Collection<Long>)query.getResultList();
		if( orphanInfoOptionIds.size() > 0 ) {
			// look for info options that are not on asset info histories.
			query = em.createQuery( "select DISTINCT io.uniqueID from InfoOptionBean as io where io.uniqueID IN (:infoOptions) AND io.uniqueID NOT IN ( select io.uniqueID from "+ AddAssetHistory.class.getName()+" as aph left join aph.infoOptions as io where io.uniqueID IN (:infoOptions)  )  " );
			query.setParameter( "infoOptions", orphanInfoOptionIds );
			orphanInfoOptionIds = (Collection<Long>)query.getResultList();
		}
	
		
		if( orphanInfoOptionIds.size() > 0 ) {
			// look for info options that are not used on a ps template and where not used by any asset serial.
			query = em.createQuery( "select DISTINCT io.uniqueID from InfoOptionBean as io where io.uniqueID IN (:infoOptions) AND io.uniqueID NOT IN ( select pcmio.uniqueID from "+AssetCodeMapping.class.getName()+" as pcm inner join pcm.infoOptions as pcmio )  " );
			query.setParameter( "infoOptions", orphanInfoOptionIds );
			orphanInfoOptionIds = query.getResultList();
		}
		
		if( orphanInfoOptionIds.size() > 0 ) {
			// look for info options that are not used on a ps template and where not used by any asset serial.
			query = em.createQuery( "select DISTINCT io.uniqueID from InfoOptionBean as io where io.uniqueID IN (:infoOptions) AND io.uniqueID NOT IN ( select aadio.uniqueID from AutoAttributeDefinition as aad inner join aad.inputs as aadio )  " );
			query.setParameter( "infoOptions", orphanInfoOptionIds );
			orphanInfoOptionIds = query.getResultList();
		}
		
		if( orphanInfoOptionIds.size() > 0 ) {
			// look for info options that are not used on a ps template and where not used by any asset serial.
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

	private void processUploadedFiles( AssetType assetType, List<FileAttachment> uploadedFiles ) throws FileAttachmentException {
		File tmpDirectory = PathHandler.getTempRoot();
		
		if( uploadedFiles != null ) {
			File tmpFile;
			// move and attach each uploaded file
			for(FileAttachment uploadedFile: uploadedFiles) {

				try {	
					// move the file to it's new location, note that it's location is currently relative to the tmpDirectory
					tmpFile = new File(tmpDirectory, uploadedFile.getFileName());
                    //FileUtils.copyFileToDirectory(tmpFile, attachmentDirectory);
                    uploadedFile.setTenant(assetType.getTenant());
                    uploadedFile.setModifiedBy(assetType.getModifiedBy());
                    uploadedFile.ensureMobileIdIsSet();
                    uploadedFile.setFileName(s3Service.getFileAttachmentPath(uploadedFile));
                    s3Service.uploadFileAttachment(tmpFile, uploadedFile);

					// clean up the temp file
					tmpFile.delete();

					// attach the attachment
					assetType.getAttachments().add(uploadedFile);
				} catch (AmazonClientException e) {
					logger.error("failed to copy uploaded file ", e);
					throw new FileAttachmentException(e);
				}
				
			}
			
			persistenceManager.update(assetType);
		}

	}
	
	public AssetType updateAssetType(AssetType assetType) throws FileAttachmentException, ImageAttachmentException {
		return updateAssetType(assetType, (List<FileAttachment>)null, null );
	}
	
}
