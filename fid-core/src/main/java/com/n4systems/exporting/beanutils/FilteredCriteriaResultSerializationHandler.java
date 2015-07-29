package com.n4systems.exporting.beanutils;

import com.n4systems.api.model.CriteriaResultView;
import org.apache.commons.lang.Validate;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class FilteredCriteriaResultSerializationHandler extends CollectionSerializationHandler<CriteriaResultView> {

	//Instead of using escapes in this String, where it is used for unmarshalling, we call Pattern.quote
    //This is because using escapes in the String would effectively break the marshalling process.  This is the
    //only way to win.
	public static final String SEPARATOR = "||";
	public static final String CRITERIA_FORMAT = "%1$s" + SEPARATOR + "%2$s";
	
	public FilteredCriteriaResultSerializationHandler(Field field) {
		super(field);
	}			

	@Override
	public boolean handlesField(String title) {
		return (title!=null && title.contains(SEPARATOR));
	}
	
	@Override
	protected Map<String, Object> marshalObject(CriteriaResultView value) {		
		Map<String,Object> result = new HashMap<>();
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
			criteriaResultView.setResult(cleanImportValue(value));  // this might be Date or String while other values are known to be only strings.
		}
		return criteriaResultView;
	}

	private CriteriaResultView findCriteriaResultView(ParsedCriteria parsedCriteria) {
		for (CriteriaResultView criteriaResultView:getCollection()) { 
			if (parsedCriteria.criteria.equals(criteriaResultView.getDisplayText()) &&
					parsedCriteria.section.equals(criteriaResultView.getSection())) {
				return criteriaResultView;
			}
		}
		return null;		
	}

	@Override
	protected Collection<CriteriaResultView> createCollection() {
		return new ArrayList<>();
	}


	enum CriteriaResultType {
		// example column text = 
		//  Motor:Brakes       <-- result  (i.e. column would contain values like "pass", "fail" etc..
		//  Motor:Brakes:R     <-- recommendation
		//  Motor:Brakes:D	   <-- deficiency
		R, D, RESULT    // note : result is default value...doesn't have to be specified in column title.
	}
	
	class ParsedCriteria { 
		String section;
		String criteria;
		CriteriaResultType type; 
		
		public ParsedCriteria(String title) {
			Validate.notNull(title);
            //We use Pattern.quote here, because we can't use escapes on the SEPARATOR constant if we want to also
            //use that same constant during the marshalling process.
			String[] split = title.split(Pattern.quote(SEPARATOR));
			Validate.isTrue(split.length>=2);	// need at least section & criteria. 
			section = split[0];
			criteria = split[1];
			type = split.length<3 ? CriteriaResultType.RESULT : CriteriaResultType.valueOf(split[2].toUpperCase());
		}
	}
	
	
}
