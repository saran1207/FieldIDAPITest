package com.n4systems.exporting.beanutils;

import java.lang.reflect.Field;
import java.util.Map;

import com.n4systems.api.model.CriteriaResultView;

public class CriteriaResultSerializationHandler extends FilteredCriteriaResultSerializationHandler {

	public static final String RECOMMENDATIONS_SUFFIX = ":R";
	public static final String DEFICIENCIES_SUFFIX = ":D";
	private static final String RECOMMENDATION_FORMAT = "%1$s:%2$s"+RECOMMENDATIONS_SUFFIX;
	private static final String DEFICIENCY_FORMAT = "%1$s:%2$s"+DEFICIENCIES_SUFFIX;
	
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
