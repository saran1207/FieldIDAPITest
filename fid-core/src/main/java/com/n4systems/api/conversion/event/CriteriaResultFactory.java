package com.n4systems.api.conversion.event;

import com.n4systems.model.ComboBoxCriteriaResult;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.CriteriaType;
import com.n4systems.model.DateFieldCriteriaResult;
import com.n4systems.model.OneClickCriteriaResult;
import com.n4systems.model.SelectCriteriaResult;
import com.n4systems.model.SignatureCriteriaResult;
import com.n4systems.model.TextFieldCriteriaResult;
import com.n4systems.model.UnitOfMeasureCriteriaResult;

public class CriteriaResultFactory {
	
	private CriteriaResultPopulator populator;
	
	public CriteriaResultFactory(CriteriaResultPopulator populator) { 
		this.populator = populator;
	}
		
	public  <T extends Criteria> CriteriaResult createCriteriaResultForCriteria(Class<T> criteriaClass) {
		return createCriteriaResult(CriteriaType.valueForCriteriaClass(criteriaClass));
	}
	
	public <T extends CriteriaResult> CriteriaResult createCriteriaResult(Class<T> criteriaResultClass) {
		return createCriteriaResult(CriteriaType.valueForResultClass(criteriaResultClass));
	}
	
	public CriteriaResult createCriteriaResult(CriteriaType criteriaType) { 
		CriteriaResult result = null;
		switch (criteriaType) {
		case COMBO_BOX:			
			result = populator.populate(new ComboBoxCriteriaResult());
			break;
		case DATE_FIELD:
			result = populator.populate(new DateFieldCriteriaResult());
			break;
		case ONE_CLICK:
			result = populator.populate(new OneClickCriteriaResult());
			break;
		case SELECT:
			result = populator.populate(new SelectCriteriaResult());
			break;
		case SIGNATURE:
			result = populator.populate(new SignatureCriteriaResult());
			break;
		case TEXT_FIELD:
			result = populator.populate(new TextFieldCriteriaResult());
			break;
		case UNIT_OF_MEASURE:
			result = populator.populate(new UnitOfMeasureCriteriaResult()); 		
			break;
		default:
			throw new IllegalStateException("can't create criteria result for type '" + criteriaType == null ? "NULL Type" : criteriaType +"'"); 
		}
		// do default stuff here
		return populator.populate(result);
	}

	
}
