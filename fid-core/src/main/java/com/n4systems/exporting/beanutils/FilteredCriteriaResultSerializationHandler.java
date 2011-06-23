package com.n4systems.exporting.beanutils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;

import com.n4systems.api.model.CriteriaResultView;

public class FilteredCriteriaResultSerializationHandler extends CollectionSerializationHandler<CriteriaResultView> {

	private static final String SEPARATOR = ":";
	private static final String CRITERIA_FORMAT = "%1$s:%2$s";
	
	public FilteredCriteriaResultSerializationHandler(Field field) {
		super(field);
	}			

	@Override
	public boolean handlesField(String title) {
		// TODO DD : may need a more strict check to validate that section/criteria parsed from title exist. 
		return (title!=null && title.indexOf(SEPARATOR)!=-1);
	}
	
	@Override
	protected Map<String, Object> marshalObject(CriteriaResultView value) {		
		Map<String,Object> result = new HashMap<String,Object>();
		String criteriaKey = String.format(CRITERIA_FORMAT,  value.getSection(), value.getDisplayText());
		result.put(criteriaKey, value.getResultString());		
		return result;		
	}

	@Override
	protected CriteriaResultView unmarshalObject(Object bean, String title, Object value) throws MarshalingException {
		try {
			ParsedCriteria parsedCriteria = new ParsedCriteria(title);
			CriteriaResultView result = getCriteriaResultView(parsedCriteria);
			populate(parsedCriteria, result, value);
			result.setSection(parsedCriteria.section);
			result.setDisplayText(parsedCriteria.criteria);
			return result;
		} catch (Exception e) { 
			throw new MarshalingException(e);
		}
	}

	private CriteriaResultView getCriteriaResultView(ParsedCriteria parsedCriteria) {
		CriteriaResultView existingCriteriaResultView = findCriteriaResultView(parsedCriteria);
		return (existingCriteriaResultView != null) ? existingCriteriaResultView : new CriteriaResultView();
	}

	private CriteriaResultView populate(ParsedCriteria parsedCriteria, CriteriaResultView criteriaResultView, Object value) {
		switch (parsedCriteria.type) {
		case D:
			criteriaResultView.setDeficiencyString(cleanString(value));
			break;			
		case R:
			criteriaResultView.setRecommendation(cleanString(value));
			break;
		case RESULT:
			criteriaResultView.setResultString(cleanString(value));
		}
		return criteriaResultView;
	}

	private CriteriaResultView findCriteriaResultView(ParsedCriteria parsedCriteria) {
		for (CriteriaResultView criteriaResultView:getCollection()) { 
			if (parsedCriteria.criteria.equals(criteriaResultView.getDisplayText())) {
				return criteriaResultView;
			}
		}
		return null;		
	}

	@Override
	protected Collection<CriteriaResultView> createCollection() {
		return new ArrayList<CriteriaResultView>();
	}


	enum CriteriaResultType {
		// example column text = 
		//  Motor:Brakes       <-- result  (i.e. column would contain values like "pass", "fail" etc..
		//  Motor:Brakes:R     <-- recommendation
		//  Motor:Brakes:D	   <-- deficiency
		R, D, RESULT;	// note : result is default value...doesn't have to be specified in column title. 
	}
	
	class ParsedCriteria { 
		String section;
		String criteria;
		CriteriaResultType type; 
		
		public ParsedCriteria(String title) {
			Validate.notNull(title);
			String[] split = title.split(SEPARATOR);
			Validate.isTrue(split.length>=2);	// need at least section & criteria. 
			section = split[0];
			criteria = split[1];
			type = split.length<3 ? CriteriaResultType.RESULT : CriteriaResultType.valueOf(split[2].toUpperCase());
		}
	}
	
	
}
