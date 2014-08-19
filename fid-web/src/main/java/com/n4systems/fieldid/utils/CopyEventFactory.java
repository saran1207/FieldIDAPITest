package com.n4systems.fieldid.utils;

import com.n4systems.model.*;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.utils.AssetEvent;

import java.util.*;

public class CopyEventFactory {

	public static ThingEvent copyEvent( ThingEvent event) {
        ThingEvent newEvent = new ThingEvent();
		
		copyAbstractEvent(newEvent, event);
        copyAssetEvent(newEvent, event);

        newEvent.setSectionResults(event.getSectionResults());
        newEvent.setAssignee(event.getAssignee());
		newEvent.setAssignedTo(event.getAssignedTo());
		newEvent.setOwner( event.getOwner() );
		newEvent.setBook( event.getBook() );
		newEvent.setPerformedBy( event.getPerformedBy() );
		newEvent.setAdvancedLocation(event.getAdvancedLocation());
		newEvent.setPrintable( event.isPrintable() );
		if( event.isRetired() ) {
			newEvent.retireEntity();
		} else {
			newEvent.activateEntity();
		}
		newEvent.setEventResult(event.getEventResult());
		
		newEvent.setDate( ( event.getDate() != null ) ? new Date( event.getDate().getTime() ) : null );
        newEvent.setProofTestInfo(new ThingEventProofTest());
        //copy the prooftest info
        ThingEventProofTest newProofTest = copyProofTestInfo(event.getProofTestInfo());
        //set the parent event
        newEvent.setProofTestInfo(newProofTest);
        if(newProofTest != null){
            newProofTest.setThingEvent(newEvent);
        }

		newEvent.setSubEvents( copySubEvents( event.getSubEvents() ) );
        newEvent.setEventForm(event.getEventForm());
        newEvent.setEventStatus(event.getEventStatus());
        newEvent.setWorkflowState(event.getWorkflowState());
        newEvent.setDueDate(event.getDueDate());
        newEvent.setProject(event.getProject());

		return newEvent;
	}

    public static void copyEventForMassEvents(ThingEvent copyTo, ThingEvent copyFrom) {
        copyAbstractEventWithOutEntity(copyTo, copyFrom);

        copyTo.setAssetStatus(copyFrom.getAssetStatus());
        copyTo.setPerformedBy( copyFrom.getPerformedBy() );
        copyTo.setDate( ( copyFrom.getDate() != null ) ? new Date( copyFrom.getDate().getTime() ) : null );
        copyTo.setSectionResults(copyFrom.getSectionResults());
        copyTo.setEventResult(copyFrom.getEventResult());
        copyTo.setSubEvents( copySubEvents( copyFrom.getSubEvents() ) );
        copyTo.setEventForm(copyFrom.getEventForm());
        copyTo.setEventStatus(copyFrom.getEventStatus());
        copyTo.setWorkflowState(copyFrom.getWorkflowState());
        copyTo.setDueDate(copyFrom.getDueDate());
        copyTo.setProject(copyFrom.getProject());
    }

	protected static List<SubEvent> copySubEvents( List<SubEvent> oldSubEvents) {
		List<SubEvent> newSubEvents = new ArrayList<SubEvent>();
		
		for( SubEvent oldSubEvent : oldSubEvents) {
			newSubEvents.add( copySubEvent(oldSubEvent) );
		}
		
		return newSubEvents;
	}
	
	protected static ThingEventProofTest copyProofTestInfo( ThingEventProofTest oldProofTestInfo ) {
		if( oldProofTestInfo == null ) { return null; }

        ThingEventProofTest newProofTestInfo = new ThingEventProofTest();
        newProofTestInfo.copyDataFrom(oldProofTestInfo);
		return newProofTestInfo;
	}
	
	public static SubEvent copySubEvent( SubEvent oldSubEvent) {
		SubEvent newSubEvent = new SubEvent();
		copyAbstractEvent(newSubEvent, oldSubEvent);
        copyAssetEvent(newSubEvent, oldSubEvent);
		newSubEvent.setName( oldSubEvent.getName() );

		return newSubEvent;
	}

    protected static void copyAssetEvent(AssetEvent newEvent, AssetEvent originalEvent) {
        newEvent.setAssetStatus(originalEvent.getAssetStatus());
        newEvent.setAsset( originalEvent.getAsset() );
    }


    protected static void copyAbstractEventWithOutEntity( AbstractEvent newEvent, AbstractEvent originalEvent) {
        //copyEntity(newEvent, originalEvent);

        newEvent.setType( originalEvent.getType() );
        newEvent.setComments( originalEvent.getComments() );
        newEvent.setEventForm( originalEvent.getEventForm() );

        newEvent.setAttachments( copyFileAttachments( originalEvent.getAttachments() ) );
        newEvent.setInfoOptionMap( new HashMap<String, String>( originalEvent.getInfoOptionMap() ) );

        newEvent.setCriteriaResults( copyCriteriaResults( originalEvent.getResults(), newEvent) );
        newEvent.setEditable(originalEvent.isEditable());
        newEvent.setEventStatus(originalEvent.getEventStatus());
    }

	protected static void copyAbstractEvent( AbstractEvent newEvent, AbstractEvent originalEvent) {
		copyEntity(newEvent, originalEvent);
		
		newEvent.setType( originalEvent.getType() );
		newEvent.setComments( originalEvent.getComments() );
        newEvent.setEventForm( originalEvent.getEventForm() );
		
		newEvent.setAttachments( copyFileAttachments( originalEvent.getAttachments() ) );
		newEvent.setInfoOptionMap( new HashMap<String, String>( originalEvent.getInfoOptionMap() ) );
		
		newEvent.setCriteriaResults( copyCriteriaResults( originalEvent.getResults(), newEvent) );
        newEvent.setEditable(originalEvent.isEditable());
        newEvent.setEventStatus(originalEvent.getEventStatus());
	}
	
	protected static List<FileAttachment> copyFileAttachments( List<FileAttachment> oldFileAttachments ) {
		List<FileAttachment> newFileAttachments = new ArrayList<FileAttachment>();
		
		for( FileAttachment oldFileAttachment : oldFileAttachments ) {
			FileAttachment newFileAttachment = new FileAttachment();
			copyEntity( newFileAttachment, oldFileAttachment );
			newFileAttachment.setComments( oldFileAttachment.getComments() );
            newFileAttachment.setMobileId(oldFileAttachment.getMobileId());
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
			CriteriaResult newResult = createBasicCopy(oldResult);
			copyEntity( newResult, oldResult );
			newResult.setCriteria( oldResult.getCriteria() );
			newResult.setEvent(newEvent);
			newResult.setRecommendations( copyRecommendations( oldResult.getRecommendations() ) );
			newResult.setDeficiencies( copyDeficiencies( oldResult.getDeficiencies() ) );
            newResult.setActions( oldResult.getActions() );
            newResult.setCriteriaImages( oldResult.getCriteriaImages() );
			
			newResults.add( newResult );
			
		}

		return newResults;
	}

    private static CriteriaResult createBasicCopy(CriteriaResult oldResult) {
        if (oldResult instanceof OneClickCriteriaResult) {
            OneClickCriteriaResult oneClickResult = new OneClickCriteriaResult();
            oneClickResult.setButton(((OneClickCriteriaResult) oldResult).getButton());
            return oneClickResult;
        } else if (oldResult instanceof TextFieldCriteriaResult) {
            TextFieldCriteriaResult textFieldResult = new TextFieldCriteriaResult();
            textFieldResult.setValue(((TextFieldCriteriaResult)oldResult).getValue());
            return textFieldResult;
        } else if (oldResult instanceof SelectCriteriaResult) {
            SelectCriteriaResult selectResult = new SelectCriteriaResult();
            selectResult.setValue(((SelectCriteriaResult)oldResult).getValue());
            return selectResult;
        } else if (oldResult instanceof ComboBoxCriteriaResult) {
        	ComboBoxCriteriaResult comboboxResult = new ComboBoxCriteriaResult();
            comboboxResult.setValue(((ComboBoxCriteriaResult)oldResult).getValue());
            return comboboxResult;
        } else if (oldResult instanceof UnitOfMeasureCriteriaResult) {
        	UnitOfMeasureCriteriaResult uomResult = new UnitOfMeasureCriteriaResult();
            uomResult.setPrimaryValue(((UnitOfMeasureCriteriaResult) oldResult).getPrimaryValue());
            uomResult.setSecondaryValue(((UnitOfMeasureCriteriaResult) oldResult).getSecondaryValue());
            return uomResult;
        } else if (oldResult instanceof SignatureCriteriaResult) {
            SignatureCriteriaResult signatureResult = new SignatureCriteriaResult();
            signatureResult.setSigned(((SignatureCriteriaResult) oldResult).isSigned());
            signatureResult.setImage(((SignatureCriteriaResult) oldResult).getImage());
            return signatureResult;
        } else if (oldResult instanceof DateFieldCriteriaResult) {
        	DateFieldCriteriaResult dateFieldResult = new DateFieldCriteriaResult();
            dateFieldResult.setValue(((DateFieldCriteriaResult)oldResult).getValue());
            return dateFieldResult;
        } else if (oldResult instanceof ScoreCriteriaResult) {
            ScoreCriteriaResult scoreResult = new ScoreCriteriaResult();
            scoreResult.setScore(((ScoreCriteriaResult) oldResult).getScore());
            return scoreResult;
        } else if (oldResult instanceof NumberFieldCriteriaResult) {
        	NumberFieldCriteriaResult numberFieldResult = new NumberFieldCriteriaResult();
        	numberFieldResult.setValue(((NumberFieldCriteriaResult)oldResult).getValue());
            return numberFieldResult;
        } else {
            throw new RuntimeException("Don't know how to copy: " + oldResult);
        }
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
