package com.n4systems.fieldid.ws.v1.resources.event;

import com.n4systems.api.conversion.event.CriteriaResultFactory;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.event.ObservationCountService;
import com.n4systems.fieldid.ws.v1.exceptions.InternalErrorException;
import com.n4systems.fieldid.ws.v1.resources.eventschedule.ApiEventSchedule;
import com.n4systems.fieldid.ws.v1.resources.eventschedule.ApiEventScheduleResource;
import com.n4systems.model.*;
import org.apache.commons.lang.NullArgumentException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

public class ApiExistingEventFormResultResource extends FieldIdPersistenceService {
	@Autowired private ApiEventScheduleResource apiEventScheduleResource;
    @Autowired private ObservationCountService observationCountService;
    private CriteriaResultFactory criteriaResultFactory;

    private static final Logger logger = Logger.getLogger(ApiExistingEventFormResultResource.class);

    public ApiExistingEventFormResultResource() {
        criteriaResultFactory = new CriteriaResultFactory();
    }

    /**
     * Convert the results from an ApiEventForm into the JPA model so that it can be put in the DB.  While doing this,
     * also remove any results from hidden form sections... we no longer care about those results.
     *
     * In order to limit additional queries to the table, processing is done in memory with a boatload of streams. If
     * this service begins to perform poorly, this may be the first place to look for further tuning.
     *
     * @param eventFormResult - An ApiEventFormResult object, which needs to be converted to the JPA Model.
     * @param event - An AbstractEvent, which needs to have the contents of the ApiEventFormResult merged into it.
     */
    @Transactional
	public void convertApiEventFormResults(ApiEventFormResult eventFormResult, AbstractEvent<?,?> event) {
//        event.setEventForm(persistenceService.find(EventForm.class, eventFormResult.getFormId()));

	    //First, we want to build a list of CriteriaResults that don't exist in the server-side model, but need
        //to be added.  This happens when form sections are unhidden when we perform an update.
        //To do that, we first need a list of what results the Server has in the model...
        List<Long> serverSideResults = event.getResults().stream().map(result -> result.getCriteria().getId()).collect(Collectors.toList());

        //...as well as a map of Criteria (not results) that the Server has in the model.  We'll need these to recreate
        //missing criteria for unhidden sections... We'll map by Criteria ID and look it up later if/when it matters.
        Map<Long, Criteria> eventCriteria = new HashMap<>();

        event.getEventForm()
             .getSections()
             //Note that calling .stream() is not necessary for calling .forEach(...)
             .forEach(section -> section.getCriteria()
                                        .forEach(criteria -> eventCriteria.put(criteria.getId(), criteria)));


        //Now we determine which results are not in the server side model but are in sections currently listed as
        //not hidden.  This happens when we have unhidden a form and are updating an event.
        Map<Long, ApiCriteriaResult> resultsToAdd = new HashMap<>();

        eventFormResult.getSections()
                       .stream()
                        //Only unhidden sections...
                       .filter(section -> !section.isHidden())
                       .forEach(section -> section.getCriteria()
                                                  .stream()
                                                  //Only results not on the server side... and only if they are Criteria we find on the event.
                                                  .filter(result -> !serverSideResults.contains(result.getCriteriaId()) && eventCriteria.containsKey(result.getCriteriaId()))
                                                  .forEach(result -> resultsToAdd.put(result.getCriteriaId(), result)));


        // Flatten ApiEventFormResult Sections into a Hashtable so it can be looked up easily by mobileGuid.
		Hashtable<String, ApiCriteriaResult> apiCriteriaResults = new Hashtable<>();


        eventFormResult.getSections()
                       .stream()
                       .filter(apiSectionResult -> !apiSectionResult.isHidden())
                       .forEach(apiSectionResult ->
                                apiSectionResult.getCriteria()
                                                .stream()
                                                //Only bother with elements we're not adding via resultsToAdd... that's just wasting
                                                //space and processing time...
                                                .filter(apiCriteriaResult -> !resultsToAdd.containsKey(apiCriteriaResult.getCriteriaId()))
                                                .forEach(apiCriteriaResult -> apiCriteriaResults.put(apiCriteriaResult.getSid(), apiCriteriaResult)));

        //Create a temp holder for the criteria results that we will be deleting/removing.
        Set<CriteriaResult> criteriaResults = new HashSet<>();

        //We don't use a stream here, because we need to alter the List while processing it.  Streams don't like this.
		for(CriteriaResult criteriaResult : event.getResults()) {
            ApiCriteriaResult apiCriteriaResult = apiCriteriaResults.get(criteriaResult.getMobileId());
            if(apiCriteriaResult != null) {
                //If apiCriteriaResult is not null, that means that there's a CriteriaResult on both sides and we want
                //to merge them...
                convertApiCriteriaResult(apiCriteriaResult, criteriaResult, event);
            } else {
                //If it IS null, then that means that the server-side is trying to hold on to results from a hidden
                //section... we can't allow that.  They must be forcibly removed from the DB and removed from the model
                //as well.  We will add this to the set and delete afterwards to avoid a ConcurrentModificationException exception.
                criteriaResults.add(criteriaResult);
            }
        }

        //Now delete them and remove them from the results
        for(CriteriaResult result:criteriaResults) {
            event.getResults().remove(result);
            persistenceService.delete(result);
        }

        //Find all existing CriteriaResults that are results of existing Criteria, but not in the server-side model.
        //This happens when we unhide a section and do an update from mobile...
        resultsToAdd.forEach((Long criteriaId, ApiCriteriaResult criteria) -> {
            Criteria serverSideCriteria = eventCriteria.get(criteriaId);
            CriteriaResult serverSideResult = criteriaResultFactory.createCriteriaResultForCriteria(serverSideCriteria.getClass());
            serverSideResult.setCriteria(serverSideCriteria);
            convertApiCriteriaResult(criteria, serverSideResult, event);
            serverSideResult.setEvent(event);
            event.getResults().add(serverSideResult);
        });
	}
	
	private void convertApiCriteriaResult(ApiCriteriaResult apiResult, CriteriaResult result, AbstractEvent event) {
		switch (result.getCriteria().getCriteriaType()) {
			case ONE_CLICK:
				Button button = persistenceService.find(Button.class, apiResult.getOneClickValue());
				((OneClickCriteriaResult) result).setButton(button);
				break;
			case TEXT_FIELD:
				((TextFieldCriteriaResult) result).setValue(apiResult.getTextValue());
				break;
			case COMBO_BOX:
				((ComboBoxCriteriaResult) result).setValue(apiResult.getComboBoxValue());
				break;
			case SELECT:
				((SelectCriteriaResult) result).setValue(apiResult.getSelectValue());
				break;
			case UNIT_OF_MEASURE:
				((UnitOfMeasureCriteriaResult) result).setPrimaryValue(apiResult.getUnitOfMeasurePrimaryValue());
				((UnitOfMeasureCriteriaResult) result).setSecondaryValue(apiResult.getUnitOfMeasureSecondaryValue());
				break;
			case SIGNATURE:
				((SignatureCriteriaResult) result).setSigned(apiResult.getSignatureValue() != null);
				((SignatureCriteriaResult) result).setImage(apiResult.getSignatureValue());
				break;
			case DATE_FIELD:
				((DateFieldCriteriaResult) result).setValue(apiResult.getDateValue());
				break;
			case SCORE:		
				if(apiResult.getScoreValue() == null)
					throw new NullArgumentException("Score value cannot be null. Client need to set a value.");				
				((ScoreCriteriaResult) result).setScore(persistenceService.find(Score.class, apiResult.getScoreValue()));
				break;
			case NUMBER_FIELD:
				((NumberFieldCriteriaResult) result).setValue(apiResult.getNumberValue());
				break;
            case OBSERVATION_COUNT:
                //NOTE: The assumption here is that we don't need a mobileGUID, since we can more or less get the same
                //      information by extrapolation.  We know that there can only be one of any given type of
                //      ObservationCount for an ObservationCountCriteriaResult.  We know that these are tied to
                //      ObservationCountResult objects, which lack a mobileGUID directly.  However, we can use the ID
                //      from the ObservationCount and ApiObservationCount objects to determine relationships.
                result = observationCountService.getObservationCountCriteriaResultByMobileId(apiResult.getSid());

                List<ObservationCountResult> observationCountResults = new ArrayList<>();

                //If we get a null back, this means that these CriteriaResults haven't been recorded yet!!
                if(result == null) {
                    //New Entry Mode... this is where we build the data from scratch, more or less.

                    result = new ObservationCountCriteriaResult();

                    apiResult.getObservationCountValue().forEach(apiObservationCountResult -> {
                        //For each of these objects, we need to process it into the new data structure.  This is made
                        //easy by the fact that we should just be able to load up the ReadOnly portion of the model
                        //directly.
                        ObservationCount observationCount = persistenceService.find(ObservationCount.class, apiObservationCountResult.getObservationCount().getSid());

                        //Now we need to build the result...
                        ObservationCountResult observationCountResult = new ObservationCountResult();
                        observationCountResult.setObservationCount(observationCount); //We'll just put this here...
                        observationCountResult.setValue(apiObservationCountResult.getValue()); //...and take that value.

                        //After all of this, we bold it on to the result, like so:
                        observationCountResults.add(observationCountResult);
                    });
                } else {
                    //Existing Work Mode... this is where we have to carefully match provided data with existing data.
                    //We will try to replace the whole list, if that is possible...

                    ((ObservationCountCriteriaResult) result).getObservationCountResults()
                            .forEach(observationCountResult -> {
                                ObservationCountResult newResult = persistenceService.find(ObservationCountResult.class, observationCountResult.getId());

                                //Trust me, it looks like we're processing more than one entry below, but we're not.  Just like
                                //highlander, there can be only one.
                                apiResult.getObservationCountValue()
                                        .stream()
                                                //Notice how we're filtering in a way that makes it impossible to return more than
                                                //one result (unless the data is corrupt, in which case this might have problems).
                                        .filter(api -> api.getObservationCount().getSid().equals(observationCountResult.getObservationCount().getId()))
                                        .forEach(apiObservationCountResult -> newResult.setValue(apiObservationCountResult.getValue()));

                                observationCountResults.add(newResult);
                            });

                }

                ((ObservationCountCriteriaResult) result).setObservationCountResults(observationCountResults);

                break;
			default:
                logger.error("Unhandled Criteria type: " + result.getCriteria().getCriteriaType().name());
				throw new InternalErrorException("Unhandled Criteria type: " + result.getCriteria().getCriteriaType().name());
		}
		
		try {
			convertObservations(Recommendation.class, apiResult.getRecommendations(), result.getRecommendations(), event.getTenant());
			convertObservations(Deficiency.class, apiResult.getDeficiencies(), result.getDeficiencies(), event.getTenant());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		for(ApiEventSchedule action: apiResult.getActions()) {
			Event actionEvent = apiEventScheduleResource.converApiEventSchedule(action);
			result.getActions().add(actionEvent);
		}
	}
	
	private <T extends Observation> void convertObservations(Class<T> observstionClass, List<ApiObservation> apiObservations, List<T> observations, Tenant tenant) 
		throws InstantiationException, IllegalAccessException {
		// Create a Hashtable of apiObservations for easy lookup (to avoid repeated for loops)
		Hashtable<String, ApiObservation> apiObservationTable = new Hashtable<String, ApiObservation>();
		for(ApiObservation apiObservation : apiObservations) {
			apiObservationTable.put(apiObservation.getSid(), apiObservation);
		}
		
		for(int i = 0; i < observations.size(); i++) {
			Observation observation = observations.get(i);
			ApiObservation apiObservation = apiObservationTable.get(observation.getMobileId());
			if(apiObservation == null) {
				// Delete from observations list since its not in the incoming apiObservations.
				observations.remove(i);
				i--;
			} else {
				// Convert the apiObservation updates to the existing observation.
				// Then Remove it from the apiObservationTable.
				convertExistingObservation(apiObservation, observation);
				apiObservationTable.remove(observation.getMobileId());
			}
		}
		
		// Now anything that is left in the apiObservationTable is new observations.
		Enumeration<ApiObservation> newApiObservations = apiObservationTable.elements();
		while(newApiObservations.hasMoreElements()) {
			T observation = observstionClass.newInstance();
			convertNewObservation(newApiObservations.nextElement(), observation, tenant);
			observations.add(observation);
		}
	}
	
	private void convertNewObservation(ApiObservation apiObservation, Observation observation, Tenant tenant) {
		observation.setMobileId(apiObservation.getSid());
		observation.setText(apiObservation.getText());
		observation.setTenant(tenant);
		observation.setState(Observation.State.valueOf(apiObservation.getState()));
	}
	
	private void convertExistingObservation(ApiObservation apiObservation, Observation observation) {
		observation.setText(apiObservation.getText());
		observation.setState(Observation.State.valueOf(apiObservation.getState()));
	}
}
