package com.n4systems.reporting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import com.n4systems.model.AbstractInspection;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.Deficiency;
import com.n4systems.model.Inspection;
import com.n4systems.model.Observation;
import com.n4systems.model.Recommendation;
import com.n4systems.model.State;
import com.n4systems.model.SubInspection;
import com.n4systems.util.DateHelper;
import com.n4systems.util.DateTimeDefinition;
import com.n4systems.util.ReportMap;

public class InspectionReportMapProducer extends ReportMapProducer {
	
	private final AbstractInspection inspection;
	private ReportMap<Object> reportMap = new ReportMap<Object>();
	
	public InspectionReportMapProducer(AbstractInspection inspection, DateTimeDefinition dateTimeDefinition) {
		super(dateTimeDefinition);
		this.inspection = inspection;
	}
	
	public ReportMap<Object> produceMap() {
		if (inspection instanceof SubInspection) {
			addSubInspectionValues();
		} else {
			addInspectionValues();
		}
		
		addAbstractInspectionValues();
		
		return reportMap;
	}

	private void addAbstractInspectionValues() {
		reportMap.put("type", inspection.getType().getName());
		
		reportMap.put("comments", inspection.getComments());
		reportMap.put("eventTypeDescription", inspection.getType().getName());
		reportMap.put("eventInfoOptionMap", eventInfoOptions());
		
		reportMap.put("product", new ProductReportMapProducer(inspection.getProduct(), dateTimeDefinition).produceMap());
		
		List<CriteriaStateView> createCriteriaViews = createCriteriaViews();
		reportMap.put("resultsBeanList", createCriteriaViews);
		reportMap.put("results", new JRBeanCollectionDataSource(createCriteriaViews));
		
		List<ObservationView> createObservationViews = createObservationViews();
		reportMap.put("observationsBeanList", createObservationViews);
		reportMap.put("observations", new JRBeanCollectionDataSource(createObservationViews));
	}

	

	private void addInspectionValues() {
		Inspection inspection = (Inspection) this.inspection;
		reportMap.put("productLabel", null);
		reportMap.put("inspectionDate", formatDate(inspection.getDate(), true));
		reportMap.put("inspectionDate_date", DateHelper.convertToUserTimeZone(inspection.getDate(), dateTimeDefinition.getTimeZone()));
		reportMap.put("location", inspection.getLocation());
		reportMap.put("inspectionBook", (inspection.getBook() != null) ? inspection.getBook().getName() : null);
		reportMap.put("inspectionResult", inspection.getStatus().getDisplayName());
		reportMap.put("proofTestInfo", addProofTestInfoParams(inspection));
		
		
	}

	private void addSubInspectionValues() {
		SubInspection subInspection = (SubInspection) inspection;
		reportMap.put("productLabel", subInspection.getName());
	}
	
	
	private ReportMap<String> eventInfoOptions() {
		ReportMap<String> eventIOMap = new ReportMap<String>(inspection.getType().getInfoFieldNames().size());
		for (String fieldName : inspection.getType().getInfoFieldNames()) {
			eventIOMap.put(normalizeString(fieldName), inspection.getInfoOptionMap().get(fieldName));
		}
		return eventIOMap;
	}
	
	/**
	 * Creates a list of ObservationViews to be used by the JasperEngine.
	 * Includes all sub inspections if there are any
	 * 
	 * @param inspection
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
		for (CriteriaResult result : inspection.getResults()) {
			for (Observation observation : result.getRecommendations()) {
				observationViews.add(new ObservationView(inspection, result, observation));
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
		for (CriteriaResult result : inspection.getResults()) {
			for (Observation observation : result.getDeficiencies()) {
				observationViews.add(new ObservationView(inspection, result, observation));
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
	private List<CriteriaStateView>  addToCriteriaView() {
		List<CriteriaStateView> criteriaViews = new ArrayList<CriteriaStateView>(inspection.getResults().size());
		Map<Criteria, State> stateMap = new HashMap<Criteria, State>(inspection.getResults().size());
		Map<Criteria, List<Recommendation>> recommendations =  new HashMap<Criteria, List<Recommendation>>(inspection.getResults().size());
		Map<Criteria, List<Deficiency>> deficiencies = new HashMap<Criteria, List<Deficiency>>(inspection.getResults().size());
		
		
		flattenCriteriaResults(stateMap, recommendations, deficiencies);
		//TODO : move criteria view to 
		// walk the section, and criteria tree and construct report views
		for (CriteriaSection section : inspection.getType().getSections()) {
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
		for (CriteriaResult result : inspection.getResults()) {
			stateMap.put(result.getCriteria(), result.getState());
			deficiencies.put(result.getCriteria(), result.getDeficiencies());
			recommendations.put(result.getCriteria(), result.getRecommendations());
		}
		
	}
	
	
	private ReportMap<Object> addProofTestInfoParams(Inspection inspection) {
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
