package com.n4systems.fieldid.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.n4systems.model.AbstractEvent;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.Deficiency;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.Event;
import com.n4systems.model.ProofTestInfo;
import com.n4systems.model.Recommendation;
import com.n4systems.model.SubEvent;
import com.n4systems.model.parents.EntityWithTenant;

public class CopyEventFactory {

	public static Event copyInspection( Event event) {
		Event newEvent = new Event();
		
		copyAbstractInspection(newEvent, event);
		
		newEvent.setAssignedTo(event.getAssignedTo());
		newEvent.setOwner( event.getOwner() );
		newEvent.setBook( event.getBook() );
		newEvent.setGroup( event.getGroup() );
		newEvent.setPerformedBy( event.getPerformedBy() );
		newEvent.setAdvancedLocation(event.getAdvancedLocation());
		newEvent.setPrintable( event.isPrintable() );
		if( event.isRetired() ) {
			newEvent.retireEntity();
		} else {
			newEvent.activateEntity();
		}
		newEvent.setStatus( event.getStatus() );
		
		newEvent.setDate( ( event.getDate() != null ) ? new Date( event.getDate().getTime() ) : null );
		newEvent.setProofTestInfo( copyProofTestInfo( event.getProofTestInfo() ) );
		newEvent.setSubEvents( copySubInspections( event.getSubEvents() ) );
		
		return newEvent;
	}
	
	protected static List<SubEvent> copySubInspections( List<SubEvent> oldSubEvents) {
		List<SubEvent> newSubEvents = new ArrayList<SubEvent>();
		
		for( SubEvent oldSubEvent : oldSubEvents) {
			newSubEvents.add( copySubInspection(oldSubEvent) );
		}
		
		return newSubEvents;
	}
	
	
	protected static ProofTestInfo copyProofTestInfo( ProofTestInfo oldProofTestInfo ) {
		if( oldProofTestInfo == null ) { return null; }
		
		ProofTestInfo newProofTestInfo = new ProofTestInfo();
		newProofTestInfo.setDuration( oldProofTestInfo.getDuration() );
		newProofTestInfo.setPeakLoad( oldProofTestInfo.getPeakLoad() );
		newProofTestInfo.setProofTestType( oldProofTestInfo.getProofTestType() );
		
		return newProofTestInfo;
	}
	
	public static SubEvent copySubInspection( SubEvent oldSubEvent) {
		SubEvent newSubEvent = new SubEvent();
		copyAbstractInspection(newSubEvent, oldSubEvent);
		newSubEvent.setName( oldSubEvent.getName() );
		
		
		return newSubEvent;
	}
	
	
	protected static void copyAbstractInspection( AbstractEvent newEvent, AbstractEvent originalEvent) {
		copyEntity(newEvent, originalEvent);
		
		newEvent.setAssetStatus(originalEvent.getAssetStatus());
		newEvent.setAsset( originalEvent.getAsset() );
		newEvent.setType( originalEvent.getType() );
		newEvent.setComments( originalEvent.getComments() );
		
		
		newEvent.setAttachments( copyFileAttachments( originalEvent.getAttachments() ) );
		newEvent.setInfoOptionMap( new HashMap<String, String>( originalEvent.getInfoOptionMap() ) );
		
		newEvent.setResults( copyCriteriaResults( originalEvent.getResults(), newEvent) );
		
		newEvent.setFormVersion(originalEvent.getFormVersion());
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
	
	protected static Set<CriteriaResult> copyCriteriaResults( Set<CriteriaResult> oldResults, AbstractEvent newEvent) {
		Set<CriteriaResult> newResults = new HashSet<CriteriaResult>();
		
		for( CriteriaResult oldResult : oldResults ) {
			CriteriaResult newResult = new CriteriaResult();
			copyEntity( newResult, oldResult );
			newResult.setCriteria( oldResult.getCriteria() );
			newResult.setState( oldResult.getState() );
			newResult.setInspection(newEvent);
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
