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
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

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

        //If this is an event with an ObservationCountGroup, we want that data to end up in the root of the ReportMap.
        if(getEvent().getEventForm() != null && getEvent().getEventForm().getObservationCountGroup() != null) {
            //Using the mighty power of streams to remap that List to a List of Strings.
            List<String> observationCountGroup = getEvent().getEventForm()
                                                           .getObservationCountGroup()
                                                           .getObservationCounts()
                                                           .stream()
                                                           .map(ObservationCount::getName)
                                                           .collect(Collectors.toList());

            add("observationCountGroup", observationCountGroup);
        }


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

        /*
        The choice of what you should show should be based on the state of EventType.displayObservationPercentage.
        If true, you should be showing the totals as percentages.  Otherwise, you should just be showing the totals.
         */

        Map<ObservationCount, Integer> observationCountTotals = new EventFormHelper().getFormObservationTotals(getEvent());
        Integer overallTotal = new EventFormHelper().getObservationCountTotal(getEvent());

        StringBuilder concatenatedTotals = new StringBuilder();

        observationCountTotals.forEach((ObservationCount observationCount, Integer count) -> {
            concatenatedTotals.append(observationCount.getName()).append(":");

            concatenatedTotals.append(count);
            if(getEvent().getType().isDisplayObservationPercentage() && observationCount.isCounted()) {
                concatenatedTotals.append(" ");

                Double totalPercentage = overallTotal == 0 ? 0 : (count.doubleValue() / overallTotal.doubleValue()) * 100.0d;
                concatenatedTotals.append("(").append(totalPercentage.intValue()).append("%)");
            }
            concatenatedTotals.append("|");
        });

        if(concatenatedTotals.length() > 0) {
            add("observationCountTotalScores", concatenatedTotals.substring(0, concatenatedTotals.length() - 1));
        } else {
            add("observationCountTotalScores", "");
        }
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

        /*
            Below, we calculate the subtotals based on what was configured for the event type.  If we have percentages
            enabled, we'll show percentages, otherwise we'll show the total count.  We will not show percentages for
         */
        Map<CriteriaSection, String> observationCountSubtotals = generateObservationCountSubtotalMap();



        flattenCriteriaResults(resultMap, recommendations, deficiencies, help);
		//TODO : move criteria view to 
		// walk the section, and criteria tree and construct report views
        if (getEvent().getEventForm() != null) {
            for (CriteriaSection section : getEvent().getEventForm().getSections()) {
                for (Criteria criteria : section.getCriteria()) {
                    if(!section.getTitle().equalsIgnoreCase("summary")){
                        totalNumCriteria++;
                    }
                    if (resultMap.containsKey(criteria)) {
                        CriteriaStateView stateView = new CriteriaStateView(section, criteria, recommendations.get(criteria), deficiencies.get(criteria), help.get(criteria));
                        stateView.setSectionScoreTotal(sectionScoreMap.get(stateView.getSection()));
                        stateView.setSectionScorePercentage(sectionScorePercentageMap.get(stateView.getSection()));

                        /*
                            Unlike above, we'll respect configuration for the event and only output these values if
                            section subtotals has been enabled for the EventType...
                         */
                        if(getEvent().getType().isDisplayObservationSectionTotals()) {
                            stateView.setSectionObservationCountSubtotal(observationCountSubtotals.get(section));
                        }

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
                        } else if (result instanceof  ObservationCountCriteriaResult) {
                            //In order to provide the result the way we'd like to see it on the reports, we have to do
                            //a little bit of String manipulation for all ObservationCriteriaResult objects.
                            StringBuilder concatenatedObservationCounts = new StringBuilder();

                            ((ObservationCountCriteriaResult) result).getObservationCountResults().forEach(observationCountResult ->
                                            //Mash everything together into a StringBuilder
                                            concatenatedObservationCounts.append(observationCountResult.getObservationCount().getName())
                                                                         .append(":")
                                                                         .append(observationCountResult.getValue())
                                                                         .append("|"));

                            //Sweet, so now that we have the String, there's one more bit of work...  There's a pipe
                            //at the end of that String that would be nice to get rid of.
                            //This is going to reliably be the last character, so we'll just substring it out to reduce
                            //the operations we're performing on it.
                                stateView.setState(concatenatedObservationCounts.substring(0,
                                        concatenatedObservationCounts.length() > 1 ? concatenatedObservationCounts.length() - 1 : 0));
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

    private Map<CriteriaSection, String> generateObservationCountSubtotalMap() {
        //Okay... because of how this thing works, it seems to not actually keep reference to the internal map after
        //processing... but we need it for the next method call... so we need to make an instance of the form helper.
        EventFormHelper observationCountScoreKeeper = new EventFormHelper();

        Map<CriteriaSection, Map<ObservationCount, Integer>> criteriaSectionMap = observationCountScoreKeeper.getObservationsForSections(getEvent());
        Map<CriteriaSection, Integer> sectionTotalMap = new HashMap<>();

        //Sorry that's so ugly!  We're just crunching down the totals into a map that we're going to need when we
        //generate subtotals, should they be percentage-based.  There shouldn't be much effort in grinding these
        //numbers, so we'll just do it regardless of whether or not we need them.
        criteriaSectionMap.keySet()
                          .forEach(criteriaSection ->
                                  sectionTotalMap.put(criteriaSection,
                                          observationCountScoreKeeper.getObservationSectionTotal(getEvent(), criteriaSection)));

        //Now that we have all the data, we can crunch the numbers and return a Map of Strings keyed by CriteriaSections.
        //We could do String, String... but there's no real benefit to that unless it turns out we can't actually
        //compare CriteriaSections... I'm pretty sure we can, though.
        return reduceObservationCountSubtotalsToSimpleMap(criteriaSectionMap, sectionTotalMap);
    }

    private Map<CriteriaSection, String> reduceObservationCountSubtotalsToSimpleMap(Map<CriteriaSection, Map<ObservationCount, Integer>> complexMap, Map<CriteriaSection, Integer> sectionTotalMap) {
        Map<CriteriaSection, String> returnMe = new HashMap<>();

        //Use forEach to process the map in as few lines as possible (ie. 1... I couldn't do it in 0 lines)
        complexMap.forEach((CriteriaSection section, Map<ObservationCount, Integer> subtotalMap) -> returnMe.put(section, calculateAppropriateObservationCountSubtotal(subtotalMap, sectionTotalMap.get(section))));

        return returnMe;
    }

    private String calculateAppropriateObservationCountSubtotal(Map<ObservationCount, Integer> subtotalMap, Integer sectionTotal) {
        StringBuilder subtotalBuilder = new StringBuilder();

        subtotalMap.forEach((ObservationCount observationCount, Integer count) -> {
            subtotalBuilder.append(observationCount.getName()).append(":");

            //Now to determine what to display.
            //Here, we want the display to look like this: 42
            subtotalBuilder.append(count);
            if(getEvent().getType().isDisplayObservationPercentage() && observationCount.isCounted()) {
                //However if we make it here, we want the display to look like this: 42 (31%)
                subtotalBuilder.append(" ");

                Double sectionPercentage = sectionTotal == 0 ? 0 : (count.doubleValue() / sectionTotal.doubleValue()) * 100.0d;
                subtotalBuilder.append("(").append(sectionPercentage.intValue()).append("%)");
            }

            subtotalBuilder.append("|");
        });

        return subtotalBuilder.length() > 0 ? subtotalBuilder.substring(0, subtotalBuilder.length() - 1) : "";
    }

    private void populateResultActions(CriteriaStateView stateView, CriteriaResult result) {
        for (Event action : result.getActions()) {
            totalNumCriteriaWithActions++;
            CriteriaResultActionView actionView = new CriteriaResultActionView();
            actionView.setAssignee(action.getAssigneeName() == null ? null : action.getAssigneeName());
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
    }

    private void populateResultImages(CriteriaStateView stateView, CriteriaResult result) {
        for (CriteriaResultImage resultImage: result.getCriteriaImages()) {
            try {
                InputStream imageContents = s3Service.openCriteriaResultImageMedium(resultImage);

                if(imageContents != null) {
                    CriteriaResultImageView criteriaResultImageView = new CriteriaResultImageView();
                    criteriaResultImageView.setComments(resultImage.getComments());
                    criteriaResultImageView.setImage(imageContents);
                    criteriaResultImageView.setImageUrl(s3Service.getCriteriaResultImageMediumURL(resultImage));
                    stateView.getCriteriaImages().add(criteriaResultImageView);
                } else {
                    logger.warn("Image could not be located in S3 for CriteriaResultImage with ID " + resultImage.getId());
                }
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
