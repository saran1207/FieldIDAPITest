package com.n4systems.exporting.beanutils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.n4systems.api.model.CriteriaResultView;

public class CriteriaResultSerializationHandler extends CollectionSerializationHandler<CriteriaResultView> {

	public CriteriaResultSerializationHandler(Field field) {
		super(field);
	}	
	
	@Override
	public void unmarshal(Object bean, String title, Object value) throws MarshalingException {
		// FIXME DD : implement this.
	}

	@Override
	public boolean handlesField(String title) {
		// FIXME DD : implement this.
		return true;
	}
	
	@Override
	protected Map<String, Object> marshalObject(CriteriaResultView value) {
		Map<String,Object> result = new HashMap<String,Object>();
		String criteriaKey = String.format("%1$s:%2$s",  value.getSection(), value.getDisplayText());
		result.put(criteriaKey, value.getResult());
		String recommendationKey = String.format("%1$s:%2$s:R",  value.getSection(), value.getDisplayText());
		result.put(recommendationKey, value.getRecommendation());
		String deficiencyKey = String.format("%1$s:%2$s:D",  value.getSection(), value.getDisplayText());
		result.put(deficiencyKey, value.getDeficiency());
		return result;		
	}

}
