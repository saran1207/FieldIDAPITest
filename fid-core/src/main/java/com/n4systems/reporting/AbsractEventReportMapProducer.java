package com.n4systems.reporting;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.certificate.model.InspectionImage;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.util.EventFormHelper;
import com.n4systems.model.*;
import com.n4systems.model.criteriaresult.CriteriaResultImage;
import com.n4systems.model.utils.PlainDate;
import com.n4systems.services.signature.SignatureService;
import com.n4systems.util.DateTimeDefinition;
import com.n4systems.util.DoubleFormatter;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbsractEventReportMapProducer extends ReportMapProducer {

	public AbsractEventReportMapProducer(DateTimeDefinition dateTimeDefinition, S3Service s3Service) {
		super(dateTimeDefinition, s3Service);
	}

	public void addParameters() {
		addAbstractEventParameters();
		eventParameter();
	}

	protected abstract void eventParameter();

	protected abstract File imagePath(FileAttachment imageAttachment);
		
	protected abstract AbstractEvent getEvent();

	
	private void addAbstractEventParameters() {
		add("type", getEvent().getType().getName());

        add("score", getEvent().getScore());
		add("comments", getEvent().getComments());
		add("eventTypeDescription", getEvent().getType().getName());
		add("eventInfoOptionMap", eventInfoOptions());
		
		add("product", new AssetReportMapProducer(getEvent().getAsset(), dateTimeDefinition, s3Service).produceMap());
		
		List<CriteriaStateView> createCriteriaViews = createCriteriaViews();
        populateTotalsAndPercentages();
		add("resultsBeanList", createCriteriaViews);
		add("results", new JRBeanCollectionDataSource(createCriteriaViews));

        List<ObservationView> createObservationViews = createObservationViews();
        add("observationsBeanList", createObservationViews);
        add("observations", new JRBeanCollectionDataSource(createObservationViews));

		add("images", createEventImages());
		add("ownerLogo", getCustomerLogo(getEvent().getAsset().getOwner()));
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
			}
		}
	}

	private List<CriteriaStateView> createCriteriaViews() {
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
                    if (resultMap.containsKey(criteria)) {
                        CriteriaStateView stateView = new CriteriaStateView(section, criteria, recommendations.get(criteria), deficiencies.get(criteria), help.get(criteria));
                        stateView.setSectionScoreTotal(sectionScoreMap.get(stateView.getSection()));
                        stateView.setSectionScorePercentage(sectionScorePercentageMap.get(stateView.getSection()));
                        CriteriaResult result = resultMap.get(criteria);
                        if (result instanceof OneClickCriteriaResult) {
                            stateView.setStateButtonGroup(((OneClickCriteriaResult)result).getState());
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
                                stateView.setStateImage(new SignatureService().getSignatureFileFor(getEvent().getTenant(), getEvent().getId(), result.getCriteria().getId()));
                            }
                        } else if (result instanceof NumberFieldCriteriaResult) {
                            stateView.setState(getNumberStringValue(result));
                        } else if (result instanceof DateFieldCriteriaResult) {
                        	stateView.setState(getDateStringValue((DateFieldCriteriaResult)result));
                        } else if (result instanceof ScoreCriteriaResult) {
                            stateView.setState(getScoreStringValue((ScoreCriteriaResult) result));
                            stateView.setLabel(((ScoreCriteria)criteria).getScoreGroup().getDisplayName());
                        }
                        stateView.setType(criteria.getCriteriaType().getReportIdentifier());
						CriteriaResultImageView criteriaResultImageView;
						for (CriteriaResultImage resultImage: result.getCriteriaImages()) {
							try {
								criteriaResultImageView = new CriteriaResultImageView();
								criteriaResultImageView.setComments(resultImage.getComments());
								criteriaResultImageView.setImage(s3Service.openCriteriaResultImageMedium(resultImage));
								stateView.getCriteriaImages().add(criteriaResultImageView);
							} catch (IOException e) {
								throw new RuntimeException(e);
							}
						}
                        criteriaViews.add(stateView);
                    }
                }
            }
        }

		return criteriaViews;
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

	protected Map<String, Object> addProofTestInfoParams(Event event) {
		Map<String, Object> proofTestInfo = new HashMap<String, Object>();
		if (event.getProofTestInfo() != null) {
			proofTestInfo.put("peakLoad", event.getProofTestInfo().getPeakLoad());
			proofTestInfo.put("testDuration", event.getProofTestInfo().getDuration());
			proofTestInfo.put("chartPath", PathHandler.getChartImageFile(event).getAbsolutePath());
			proofTestInfo.put("peakLoadDuration", event.getProofTestInfo().getPeakLoadDuration());
		}
		return proofTestInfo;
	}

}