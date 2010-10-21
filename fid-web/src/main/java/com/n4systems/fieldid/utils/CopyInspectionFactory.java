package com.n4systems.fieldid.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.n4systems.model.AbstractInspection;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.Deficiency;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.Inspection;
import com.n4systems.model.ProofTestInfo;
import com.n4systems.model.Recommendation;
import com.n4systems.model.SubInspection;
import com.n4systems.model.parents.EntityWithTenant;

public class CopyInspectionFactory {

	public static Inspection copyInspection( Inspection inspection ) {
		Inspection newInspection = new Inspection();
		
		copyAbstractInspection( newInspection, inspection );
		
		newInspection.setAssignedTo(inspection.getAssignedTo());
		newInspection.setOwner( inspection.getOwner() );
		newInspection.setBook( inspection.getBook() );
		newInspection.setGroup( inspection.getGroup() );
		newInspection.setPerformedBy( inspection.getPerformedBy() );
		newInspection.setAdvancedLocation(inspection.getAdvancedLocation());
		newInspection.setPrintable( inspection.isPrintable() );
		if( inspection.isRetired() ) {
			newInspection.retireEntity();
		} else {
			newInspection.activateEntity();
		}
		newInspection.setStatus( inspection.getStatus() );
		
		newInspection.setDate( ( inspection.getDate() != null ) ? new Date( inspection.getDate().getTime() ) : null );
		newInspection.setProofTestInfo( copyProofTestInfo( inspection.getProofTestInfo() ) );
		newInspection.setSubInspections( copySubInspections( inspection.getSubInspections() ) );
		
		return newInspection;
	}
	
	protected static List<SubInspection> copySubInspections( List<SubInspection> oldSubInspections ) {
		List<SubInspection> newSubInspections = new ArrayList<SubInspection>();
		
		for( SubInspection oldSubInspection : oldSubInspections ) {
			newSubInspections.add( copySubInspection( oldSubInspection ) );
		}
		
		return newSubInspections;
	}
	
	
	protected static ProofTestInfo copyProofTestInfo( ProofTestInfo oldProofTestInfo ) {
		if( oldProofTestInfo == null ) { return null; }
		
		ProofTestInfo newProofTestInfo = new ProofTestInfo();
		newProofTestInfo.setDuration( oldProofTestInfo.getDuration() );
		newProofTestInfo.setPeakLoad( oldProofTestInfo.getPeakLoad() );
		newProofTestInfo.setProofTestType( oldProofTestInfo.getProofTestType() );
		
		return newProofTestInfo;
	}
	
	public static SubInspection copySubInspection( SubInspection oldSubInspection ) {
		SubInspection newSubInspection = new SubInspection();
		copyAbstractInspection( newSubInspection, oldSubInspection );
		newSubInspection.setName( oldSubInspection.getName() );
		
		
		return newSubInspection;
	}
	
	
	protected static void copyAbstractInspection( AbstractInspection newInspection, AbstractInspection originalInspection ) {
		copyEntity( newInspection, originalInspection );
		
		newInspection.setAssetStatus(originalInspection.getAssetStatus());
		newInspection.setAsset( originalInspection.getAsset() );
		newInspection.setType( originalInspection.getType() );
		newInspection.setComments( originalInspection.getComments() );
		
		
		newInspection.setAttachments( copyFileAttachments( originalInspection.getAttachments() ) );
		newInspection.setInfoOptionMap( new HashMap<String, String>( originalInspection.getInfoOptionMap() ) );
		
		newInspection.setResults( copyCriteriaResults( originalInspection.getResults(), newInspection ) );
		
		newInspection.setFormVersion(originalInspection.getFormVersion());
	}
	
	protected static List<FileAttachment> copyFileAttachments( List<FileAttachment> oldFileAttachments ) {
		List<FileAttachment> newFileAttachments = new ArrayList<FileAttachment>();
		
		for( FileAttachment oldFileAttachment : oldFileAttachments ) {
			FileAttachment newFileAttachment = new FileAttachment();
			copyEntity( newFileAttachment, oldFileAttachment );
			newFileAttachment.setComments( oldFileAttachment.getComments() );
			newFileAttachment.setFileName( oldFileAttachment.getFileName() );
			newFileAttachments.add( newFileAttachment );
		}
		
		
		return newFileAttachments;
	}

	
	protected static void copyEntity( EntityWithTenant newEntity, EntityWithTenant oldEntity ) {
		newEntity.setId( oldEntity.getId() );
		newEntity.setCreated( oldEntity.getCreated());
		newEntity.setModified( oldEntity.getModified());
		newEntity.setModifiedBy( oldEntity.getModifiedBy() );
		newEntity.setTenant( oldEntity.getTenant() );
	}
	
	protected static Set<CriteriaResult> copyCriteriaResults( Set<CriteriaResult> oldResults, AbstractInspection newInspection ) {
		Set<CriteriaResult> newResults = new HashSet<CriteriaResult>();
		
		for( CriteriaResult oldResult : oldResults ) {
			CriteriaResult newResult = new CriteriaResult();
			copyEntity( newResult, oldResult );
			newResult.setCriteria( oldResult.getCriteria() );
			newResult.setState( oldResult.getState() );
			newResult.setInspection( newInspection );
			newResult.setRecommendations( copyRecommendations( oldResult.getRecommendations() ) );
			newResult.setDeficiencies( copyDeficiencies( oldResult.getDeficiencies() ) );
			
			newResults.add( newResult );
			
		}
		
		
		return newResults;
	}
	
	protected static List<Recommendation> copyRecommendations( List<Recommendation> oldRecommendations ) {
		List<Recommendation> newRecommendations = new ArrayList<Recommendation>();
		for( Recommendation oldRecommendation : oldRecommendations ) {
			Recommendation newRecommendation = new Recommendation();
			copyEntity( newRecommendation, oldRecommendation );
			newRecommendation.setState( oldRecommendation.getState() );
			newRecommendation.setText( oldRecommendation.getText() );
			newRecommendations.add( newRecommendation );
		}
		return newRecommendations;
	}
	
	
	protected static List<Deficiency> copyDeficiencies( List<Deficiency> oldDeficiencies ) {
		List<Deficiency> newDeficiencies = new ArrayList<Deficiency>();
		for( Deficiency oldDeficiency : oldDeficiencies ) {
			Deficiency newDeficiency = new Deficiency();
			copyEntity( newDeficiency, oldDeficiency );
			newDeficiency.setState( oldDeficiency.getState() );
			newDeficiency.setText( oldDeficiency.getText() );
			newDeficiencies.add( newDeficiency );
		}
		return newDeficiencies;
	}
}
