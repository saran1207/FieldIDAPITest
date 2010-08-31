package com.n4systems.reporting;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import com.n4systems.fieldid.certificate.model.InspectionImage;
import com.n4systems.model.AbstractInspection;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.Deficiency;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.Inspection;
import com.n4systems.model.Observation;
import com.n4systems.model.Recommendation;
import com.n4systems.model.State;
import com.n4systems.util.DateTimeDefinition;
import com.n4systems.util.ReportMap;

public abstract class AbsractInspectionReportMapProducer extends ReportMapProducer {


	public AbsractInspectionReportMapProducer(DateTimeDefinition dateTimeDefinition) {
		super(dateTimeDefinition);
	}

	

	public void addParameters() {
		abstractInspectionParameters();
		inspectionParameter();
	}

	protected abstract void inspectionParameter();

	protected abstract File imagePath(FileAttachment imageAttachment);
		
	protected abstract AbstractInspection getInspection();

	
	private void abstractInspectionParameters() {
		add("type", getInspection().getType().getName());
		
		add("comments", getInspection().getComments());
		add("eventTypeDescription", getInspection().getType().getName());
		add("eventInfoOptionMap", eventInfoOptions());
		
		add("product", new ProductReportMapProducer(getInspection().getProduct(), dateTimeDefinition).produceMap());
		
		List<CriteriaStateView> createCriteriaViews = createCriteriaViews();
		add("resultsBeanList", createCriteriaViews);
		add("results", new JRBeanCollectionDataSource(createCriteriaViews));
		
		List<ObservationView> createObservationViews = createObservationViews();
		add("observationsBeanList", createObservationViews);
		add("observations", new JRBeanCollectionDataSource(createObservationViews));
		
		add("images", createInspectionImages());
	}

	private List<InspectionImage> createInspectionImages() {
		List<InspectionImage> imageList = new ArrayList<InspectionImage>();
		
		for (FileAttachment imageAttachment : getInspection().getImageAttachments()) {
			imageList.add(new InspectionImage(imagePath(imageAttachment), imageAttachment.getComments()));
		}
		
		return imageList;
	}

	

	private ReportMap<String> eventInfoOptions() {
		ReportMap<String> eventIOMap = new ReportMap<String>(getInspection().getType().getInfoFieldNames().size());
		for (String fieldName : getInspection().getType().getInfoFieldNames()) {
			eventIOMap.put(normalizeString(fieldName), getInspection().getInfoOptionMap().get(fieldName));
		}
		return eventIOMap;
	}

	/**
	 * Creates a list of ObservationViews to be used by the JasperEngine.
	 * Includes all sub inspections if there are any
	 * 
	 * @param inspections
	 *            An inspection
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
		 * collection datasource. Since it's not required that an inspectiontype
		 * have a criteria (and thus the inspection will have an empty result
		 * list) we need to add a dummy entry.
		 */
		if (observationViews.isEmpty()) {
			observationViews.add(new ObservationView());
		}
	
		return observationViews;
	}

	/**
	 * Creates ObservationViews for the Recommendations of an inspection's
	 * result list and adds them to observationViews
	 * 
	 * @param inspection
	 *            An inspection (could be master or sub)
	 * @param observationViews
	 *            The ObservationView list
	 */
	private void addRecommendationsToObservationView(List<ObservationView> observationViews) {
		for (CriteriaResult result : getInspection().getResults()) {
			for (Observation observation : result.getRecommendations()) {
				observationViews.add(new ObservationView(getInspection(), result, observation));
			}
		}
	}

	/**
	 * Creates ObservationViews for the Deficiencies of an inspection's result
	 * list and adds them to observationViews
	 * 
	 * @param inspection
	 *            An inspection (could be master or sub)
	 * @param observationViews
	 *            The ObservationView list
	 */
	private void addDeficienciesToObservationView(List<ObservationView> observationViews) {
		for (CriteriaResult result : getInspection().getResults()) {
			for (Observation observation : result.getDeficiencies()) {
				observationViews.add(new ObservationView(getInspection(), result, observation));
			}
		}
	}

	private List<CriteriaStateView> createCriteriaViews() {
		// add the main inspection to the views
		List<CriteriaStateView> criteriaViews = addToCriteriaView();
	
		/*
		 * Lame alert: Jasper requires that at least 1 element be in it's
		 * collection datasource. Since it's not required that an inspectiontype
		 * have a criteria (and thus the inspection will have an empty result
		 * list) we need to add a dummy entry.
		 */
		if (criteriaViews.isEmpty()) {
			criteriaViews.add(new CriteriaStateView());
		}
	
		return criteriaViews;
	}

	/**
	 * Creates CriteriaStateViews for an inspection and adds them to the
	 * criteriaView list.
	 * 
	 * @param inspection
	 *            inspection to add the views for
	 * @param criteriaViews
	 *            List of criteriaViews to append to
	 * @param stateMap
	 *            A state map of Criteria to their States
	 */
	private List<CriteriaStateView> addToCriteriaView() {
		List<CriteriaStateView> criteriaViews = new ArrayList<CriteriaStateView>(getInspection().getResults().size());
		Map<Criteria, State> stateMap = new HashMap<Criteria, State>(getInspection().getResults().size());
		Map<Criteria, List<Recommendation>> recommendations =  new HashMap<Criteria, List<Recommendation>>(getInspection().getResults().size());
		Map<Criteria, List<Deficiency>> deficiencies = new HashMap<Criteria, List<Deficiency>>(getInspection().getResults().size());
		
		
		flattenCriteriaResults(stateMap, recommendations, deficiencies);
		//TODO : move criteria view to 
		// walk the section, and criteria tree and construct report views
		for (CriteriaSection section : getInspection().getType().getSections()) {
			for (Criteria criteria : section.getCriteria()) {
				if (stateMap.containsKey(criteria)) {
					criteriaViews.add(new CriteriaStateView(section, criteria, stateMap.get(criteria), recommendations.get(criteria), deficiencies.get(criteria)));
				}
			}
		}
		
		return criteriaViews;
	}

	/**
	 * Given a list of CriteriaResults, returns a Map of the same results of
	 * Criteria to State
	 * 
	 * @param results
	 *            The CriteriaResult list
	 * @return The flattened map
	 */
	private void flattenCriteriaResults(Map<Criteria, State> stateMap, Map<Criteria, List<Recommendation>> recommendations, Map<Criteria, List<Deficiency>> deficiencies) {
		// set the map size to increase performance
		for (CriteriaResult result : getInspection().getResults()) {
			stateMap.put(result.getCriteria(), result.getState());
			deficiencies.put(result.getCriteria(), result.getDeficiencies());
			recommendations.put(result.getCriteria(), result.getRecommendations());
		}
		
	}

	protected ReportMap<Object> addProofTestInfoParams(Inspection inspection) {
		ReportMap<Object> proofTestInfo = new ReportMap<Object>();
		proofTestInfo.putEmpty("proofTest", "peakLoad", "testDuration", "peakLoadDuration");
		if (inspection.getProofTestInfo() != null) {
			proofTestInfo.put("peakLoad", inspection.getProofTestInfo().getPeakLoad());
			proofTestInfo.put("testDuration", inspection.getProofTestInfo().getDuration());
			proofTestInfo.put("chartPath", PathHandler.getChartImageFile(inspection).getAbsolutePath());
			proofTestInfo.put("peakLoadDuration", inspection.getProofTestInfo().getPeakLoadDuration());
		}
		return proofTestInfo;
	}




}