package com.n4systems.reporting;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.certificate.model.InspectionImage;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.event.LastEventDateService;
import com.n4systems.fieldid.util.EventFormHelper;
import com.n4systems.model.*;
import com.n4systems.model.criteriaresult.CriteriaResultImage;
import com.n4systems.model.utils.PlainDate;
import com.n4systems.util.DateTimeDefinition;
import com.n4systems.util.DoubleFormatter;
import com.n4systems.util.ServiceLocator;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.util.*;

public abstract class AbsractEventReportMapProducer extends ReportMapProducer {

    protected LastEventDateService lastEventDateService;

    protected Integer totalNumCriteria = 0;
    protected Integer totalNumCriteriaWithObservation = 0;
    protected Integer totalNumCriteriaWithActions = 0;
    protected Map<String, Integer> actionsByPriorityCode = new HashMap<>();

    public AbsractEventReportMapProducer(DateTimeDefinition dateTimeDefinition, S3Service s3Service, LastEventDateService lastEventDateService) {
		super(dateTimeDefinition, s3Service);
        this.lastEventDateService = lastEventDateService;
    }

	public void addParameters() {
		addAbstractEventParameters();
		eventParameter();
	}

	protected abstract void eventParameter();

	protected abstract File imagePath(FileAttachment imageAttachment);
		
	protected abstract AbstractEvent<ThingEventType,Asset> getEvent();

	
	private void addAbstractEventParameters() {
		add("type", getEvent().getType().getName());

        add("score", getEvent().getScore());
		add("comments", getEvent().getComments());
		add("eventTypeDescription", getEvent().getType().getName());
		add("eventInfoOptionMap", eventInfoOptions());

		add("product", new AssetReportMapProducer(getEvent().getTarget(), lastEventDateService, dateTimeDefinition, s3Service).produceMap());
		
		List<CriteriaStateView> criteriaViews = createCriteriaViews();
        populateTotalsAndPercentages();
		add("resultsBeanList", criteriaViews);
		add("results", new JRBeanCollectionDataSource(criteriaViews));

        // WEB-3577 Unfortunately we are having issues resetting data sources in our ireports. Our data source is
        // rewindable but apparently it's not working. We should remove this soon(tm) and figure out the right way to do it
        add("resultsCopy", new JRBeanCollectionDataSource(createCriteriaViews()));

        List<ObservationView> observationViews = createObservationViews();
        add("observationsBeanList", observationViews);
        add("observations", new JRBeanCollectionDataSource(observationViews));

		add("images", createEventImages());
		add("ownerLogo", getCustomerLogo(getEvent().getTarget().getOwner()));

        add("totalNumCriteria", totalNumCriteria.toString());
        add("totalNumCriteriaWithObservation", totalNumCriteriaWithObservation.toString());
        add("totalNumCriteriaWithActions", totalNumCriteriaWithActions.toString());

        add("actionsByPriorityCode", new JRBeanCollectionDataSource(createCollectionForActionsByPriorityCode()));

	}

    private List<PriorityCodeListView> createCollectionForActionsByPriorityCode() {
        List<PriorityCodeListView> list = new ArrayList<>();

        Iterator it = actionsByPriorityCode.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            PriorityCodeListView temp = new PriorityCodeListView();
            temp.setPriorityCode(pairs.getKey().toString());
            temp.setCount(pairs.getValue().toString());
            list.add(temp);
            it.remove(); // avoids a ConcurrentModificationException
        }
        return list;
    }

    private void populateTotalsAndPercentages() {
        add("maximumPossibleScore", new EventFormHelper().calculateMaxScoreForEvent(getEvent()));
        add("totalScorePercentage", new EventFormHelper().getEventFormScorePercentage(getEvent()));
    }

    private List<InspectionImage> createEventImages() {
		List<InspectionImage> imageList = new ArrayList<InspectionImage>();
		
		for (FileAttachment imageAttachment : getEvent().getImageAttachments()) {
			imageList.add(new InspectionImage(imagePath(imageAttachment), imageAttachment.getComments()));
		}
		
		return imageList;
	}

	private Map<String, Object> eventInfoOptions() {
		Map<String, Object> eventIOMap = new HashMap<String, Object>();
		for (String fieldName : getEvent().getType().getInfoFieldNames()) {
			eventIOMap.put(normalizeString(fieldName), getEvent().getInfoOptionMap().get(fieldName));
		}
		return eventIOMap;
	}

    private List<String> getHelp() {
        List<String> help = Lists.newArrayList();
        // note that result help instructions are in HTML.  we just want the un-formatted text.
        for (CriteriaResult result:getEvent().getResults()) {
            Document doc = Jsoup.parseBodyFragment(result.getCriteria().getInstructions());
            help.add(doc.text());
        }
        return help;
    }

	/**
	 * Creates a list of ObservationViews to be used by the JasperEngine.
	 * Includes all sub events if there are any
	 *
	 * @return A flattened view of observations
	 */
	private List<ObservationView> createObservationViews() {
		// 100 is a rough guess
		List<ObservationView> observationViews = new ArrayList<ObservationView>(100);

		// first we'll gather the deficiencies. We need to do this in two steps
		// so that the ObservationView's type's are grouped (needed for iReports
		// )
		addDeficienciesToObservationView(observationViews);

		// now the recommendations
		addRecommendationsToObservationView(observationViews);

		/*
		 * Lame alert: Jasper requires that at least 1 element be in it's
		 * collection datasource. Since it's not required that an eventtype
		 * have a criteria (and thus the event will have an empty result
		 * list) we need to add a dummy entry.
		 */
		if (observationViews.isEmpty()) {
			observationViews.add(new ObservationView());
		}

		return observationViews;
	}

	/**
	 * Creates ObservationViews for the Recommendations of an event's
	 * result list and adds them to observationViews
	 * 
	 * @param observationViews
	 *            The ObservationView list
	 */
	private void addRecommendationsToObservationView(List<ObservationView> observationViews) {
		for (CriteriaResult result : getEvent().getResults()) {
			for (Observation observation : result.getRecommendations()) {
				observationViews.add(new ObservationView(getEvent(), result, observation));
                totalNumCriteriaWithObservation++;
			}
		}
	}

	/**
	 * Creates ObservationViews for the Deficiencies of an event's result
	 * list and adds them to observationViews
	 * 
	 * @param observationViews
	 *            The ObservationView list
	 */
	private void addDeficienciesToObservationView(List<ObservationView> observationViews) {
		for (CriteriaResult result : getEvent().getResults()) {
			for (Observation observation : result.getDeficiencies()) {
				observationViews.add(new ObservationView(getEvent(), result, observation));
                totalNumCriteriaWithObservation++;
			}
		}
	}

	private List<CriteriaStateView> createCriteriaViews() {
        //Reset values
        totalNumCriteria = 0;
        totalNumCriteriaWithObservation = 0;
        totalNumCriteriaWithActions = 0;
        actionsByPriorityCode = new HashMap<>();

		// add the main event to the views
		List<CriteriaStateView> criteriaViews = addToCriteriaView();
	
		/*
		 * Lame alert: Jasper requires that at least 1 element be in its
		 * collection datasource. Since it's not required that an eventtype
		 * have a criteria (and thus the event will have an empty result
		 * list) we need to add a dummy entry.
		 */
		if (criteriaViews.isEmpty()) {
			criteriaViews.add(new CriteriaStateView());
		}
	
		return criteriaViews;
	}


    /**
	 * Creates CriteriaStateViews for an event and adds them to the
	 * criteriaView list.
	 * 
	 */
	private List<CriteriaStateView> addToCriteriaView() {
		List<CriteriaStateView> criteriaViews = new ArrayList<CriteriaStateView>(getEvent().getResults().size());
		Map<Criteria, CriteriaResult> resultMap = new HashMap<Criteria, CriteriaResult>(getEvent().getResults().size());
		Map<Criteria, List<Recommendation>> recommendations =  new HashMap<Criteria, List<Recommendation>>(getEvent().getResults().size());
        Map<Criteria, List<Deficiency>> deficiencies = new HashMap<Criteria, List<Deficiency>>(getEvent().getResults().size());
        Map<Criteria, String> help = new HashMap<Criteria, String>(getEvent().getResults().size());

        Map<String, Double> sectionScoreMap = convertSectionScoreMapToNameScoreMap(new EventFormHelper().getScoresForSections(getEvent()));
        Map<String, Double> sectionScorePercentageMap = convertSectionScoreMapToNameScoreMap(new EventFormHelper().getScorePercentageForSections(getEvent()));

        flattenCriteriaResults(resultMap, recommendations, deficiencies, help);
		//TODO : move criteria view to 
		// walk the section, and criteria tree and construct report views
        if (getEvent().getEventForm() != null) {
            for (CriteriaSection section : getEvent().getEventForm().getSections()) {
                for (Criteria criteria : section.getCriteria()) {
                    totalNumCriteria++;
                    if (resultMap.containsKey(criteria)) {
                        CriteriaStateView stateView = new CriteriaStateView(section, criteria, recommendations.get(criteria), deficiencies.get(criteria), help.get(criteria));
                        stateView.setSectionScoreTotal(sectionScoreMap.get(stateView.getSection()));
                        stateView.setSectionScorePercentage(sectionScorePercentageMap.get(stateView.getSection()));
                        CriteriaResult result = resultMap.get(criteria);

                        if (result instanceof OneClickCriteriaResult) {
                            stateView.setStateButtonGroup(((OneClickCriteriaResult)result).getButton());
                        } else if (result instanceof TextFieldCriteriaResult) {
                            stateView.setState(((TextFieldCriteriaResult)result).getValue());
                        } else if (result instanceof SelectCriteriaResult) {
                            stateView.setState(((SelectCriteriaResult) result).getValue());
                        } else if (result instanceof ComboBoxCriteriaResult) {
                            stateView.setState(((ComboBoxCriteriaResult) result).getValue());
                        } else if (result instanceof UnitOfMeasureCriteriaResult) {
                            String unitOfMeasureValueStr = getUnitOfMeasureStringValue((UnitOfMeasureCriteriaResult)result);
                            stateView.setState(unitOfMeasureValueStr);
                        } else if (result instanceof SignatureCriteriaResult) {
                            if (((SignatureCriteriaResult) result).isSigned()) {
                                stateView.setStateImage(ServiceLocator.getSignatureService().getSignatureFileFor(getEvent().getTenant(), getEvent().getId(), result.getCriteria().getId()));
                            }
                        } else if (result instanceof NumberFieldCriteriaResult) {
                            stateView.setState(getNumberStringValue(result));
                        } else if (result instanceof DateFieldCriteriaResult) {
                        	stateView.setState(getDateStringValue((DateFieldCriteriaResult)result));
                        } else if (result instanceof ScoreCriteriaResult) {
                            stateView.setState(getScoreStringValue((ScoreCriteriaResult) result));
                            stateView.setLabel(((ScoreCriteriaResult) result).getScore().getName());
                        }
                        stateView.setType(criteria.getCriteriaType().getReportIdentifier());
                        populateResultImages(stateView, result);
                        populateResultActions(stateView, result);
                        criteriaViews.add(stateView);
                    }
                }
            }
        }

		return criteriaViews;
	}

    private void populateResultActions(CriteriaStateView stateView, CriteriaResult result) {
        for (Event action : result.getActions()) {
            totalNumCriteriaWithActions++;
            CriteriaResultActionView actionView = new CriteriaResultActionView();
            actionView.setAssignee(action.getAssignee() == null ? null : action.getAssignee().getFullName());
            actionView.setPerformedBy(action.getPerformedBy() == null ? null : action.getPerformedBy().getFullName());
            actionView.setDueDate(action.getDueDate() == null ? null : formatDate(action.getDueDate(), true));
            actionView.setCompletedDate(action.getCompletedDate() == null ? null : formatDate(action.getCompletedDate(), true));
            actionView.setEventType(action.getEventType().getName());
            actionView.setWorkflowState(action.getWorkflowState().getLabel());
            actionView.setNotes(action.getNotes());
            actionView.setPriority(action.getPriority().getDisplayName());
            int count = actionsByPriorityCode.containsKey(action.getPriority().getDisplayName()) ? actionsByPriorityCode.get(action.getPriority().getDisplayName()) : 0;
            actionsByPriorityCode.put(action.getPriority().getDisplayName(), count + 1);
            stateView.getCriteriaActions().add(actionView);
        }

        stateView.getCritActionsDataSource().put("critActionsDataSource", stateView.getCriteriaActions());
        stateView.getCritImagesDataSource().put("critImagesDataSource", stateView.getCriteriaImages());

        stateView.getActions().add(stateView.getCritActionsDataSource());
        stateView.getImages().add(stateView.getCritImagesDataSource());
    }

    private void populateResultImages(CriteriaStateView stateView, CriteriaResult result) {
        for (CriteriaResultImage resultImage: result.getCriteriaImages()) {
            try {
                CriteriaResultImageView criteriaResultImageView = new CriteriaResultImageView();
                criteriaResultImageView.setComments(resultImage.getComments());
                criteriaResultImageView.setImage(s3Service.openCriteriaResultImageMedium(resultImage));
                criteriaResultImageView.setImageUrl(s3Service.getCriteriaResultImageMediumURL(resultImage));
                stateView.getCriteriaImages().add(criteriaResultImageView);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Map<String,Double> convertSectionScoreMapToNameScoreMap(Map<CriteriaSection,Double> sectionMap) {
        Map<String,Double> convertedMap = new HashMap<String, Double>();
        for (CriteriaSection criteriaSection : sectionMap.keySet()) {
            convertedMap.put(criteriaSection.getTitle(), sectionMap.get(criteriaSection));
        }
        return convertedMap;
    }

    private String getScoreStringValue(ScoreCriteriaResult result) {
        if (result.getScore().isNa()) {
            return "N/A";
        }
        return DoubleFormatter.simplifyDouble(result.getScore().getValue());
    }

	private String getDateStringValue(DateFieldCriteriaResult result) {
		boolean includeTime = ((DateFieldCriteria)result.getCriteria()).isIncludeTime();
		if(includeTime) {
			return formatDate(result.getValue(), includeTime);
        } else {
			return formatDate(result.getValue() != null ? new PlainDate(result.getValue()) : null, includeTime);
        }
	}

	private String getNumberStringValue(CriteriaResult result) {
		int decimalPlaces = ((NumberFieldCriteria) result.getCriteria()).getDecimalPlaces();
		return String.format("%." + decimalPlaces + "f", ((NumberFieldCriteriaResult) result).getValue());
	}

    private String getUnitOfMeasureStringValue(UnitOfMeasureCriteriaResult result) {
        UnitOfMeasureCriteria criteria = (UnitOfMeasureCriteria) result.getCriteria();
        String unitOfMeasureValue = "";
        if (result.getPrimaryValue() != null) {
            unitOfMeasureValue += result.getPrimaryValue() + " " + criteria.getPrimaryUnit().getName();
        }
        if (result.getSecondaryValue() != null) {
            unitOfMeasureValue += " " + result.getSecondaryValue() + " " + criteria.getSecondaryUnit().getName();
        }
        return unitOfMeasureValue.trim();
    }

    /**
	 * Given a list of CriteriaResults, returns a Map of the same results of
	 * Criteria to Result
	 *
	 *            The CriteriaResult list
	 * @return The flattened map
	 */
	private void flattenCriteriaResults(Map<Criteria, CriteriaResult> stateMap, Map<Criteria, List<Recommendation>> recommendations, Map<Criteria, List<Deficiency>> deficiencies, Map<Criteria,String> help) {
		for (CriteriaResult result : getEvent().getResults()) {
            stateMap.put(result.getCriteria(), result);
			deficiencies.put(result.getCriteria(), result.getDeficiencies());
			recommendations.put(result.getCriteria(), result.getRecommendations());
            help.put(result.getCriteria(), result.getCriteria().getInstructions());
		}
	}

	protected Map<String, Object> addProofTestInfoParams(ThingEvent event) {
        ThingEventProofTest proofTest = event.getProofTestInfo();
		Map<String, Object> proofTestInfo = new HashMap<String, Object>();
		if (proofTest != null) {
            proofTestInfo.put("peakLoad", proofTest.getPeakLoad());
            proofTestInfo.put("testDuration", proofTest.getDuration());
            proofTestInfo.put("peakLoadDuration", proofTest.getPeakLoadDuration());
            if(s3Service.assetProofTestChartExists(proofTest)){
                proofTestInfo.put("chartPath", s3Service.downloadAssetProofTestChart(proofTest).getAbsolutePath());
            }
            else {
                proofTestInfo.put("chartPath", PathHandler.getChartImageFile(event).getAbsolutePath());
            }
		}
		return proofTestInfo;
	}

}
