package com.n4systems.exporting.beanutils;

import com.n4systems.api.model.CriteriaResultView;

import java.lang.reflect.Field;
import java.util.Map;

public class CriteriaResultSerializationHandler extends FilteredCriteriaResultSerializationHandler {

	public static final String RECOMMENDATIONS_SUFFIX = SEPARATOR + "R";
	public static final String DEFICIENCIES_SUFFIX = SEPARATOR + "D";
	public static final String RECOMMENDATION_FORMAT = CRITERIA_FORMAT + RECOMMENDATIONS_SUFFIX;
	public static final String DEFICIENCY_FORMAT = CRITERIA_FORMAT + DEFICIENCIES_SUFFIX;
	
	public CriteriaResultSerializationHandler(Field field) {
		super(field);
	}			

	@Override
	protected Map<String, Object> marshalObject(CriteriaResultView value) {
		Map<String, Object> result = super.marshalObject(value);
		String recommendationKey = String.format(RECOMMENDATION_FORMAT,  value.getSection(), value.getDisplayText());
		result.put(recommendationKey, value.getRecommendationString());
		String deficiencyKey = String.format(DEFICIENCY_FORMAT, value.getSection(), value.getDisplayText());
		result.put(deficiencyKey, value.getDeficiencyString());
		return result;		
	}
	
}
