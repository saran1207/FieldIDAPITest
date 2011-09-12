package com.n4systems.api.conversion.event;


import static com.google.common.base.Preconditions.checkArgument;

import com.n4systems.model.ComboBoxCriteriaResult;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.CriteriaType;
import com.n4systems.model.DateFieldCriteriaResult;
import com.n4systems.model.NumberFieldCriteriaResult;
import com.n4systems.model.OneClickCriteriaResult;
import com.n4systems.model.SelectCriteriaResult;
import com.n4systems.model.SignatureCriteriaResult;
import com.n4systems.model.TextFieldCriteriaResult;
import com.n4systems.model.UnitOfMeasureCriteriaResult;


// TODO DD : what package should this live in? 
//  also, need to refactor all code that creates criteria results to use this common code.
public class CriteriaResultFactory {
	
	private CriteriaResultPopulator populator;
	
	public CriteriaResultFactory(CriteriaResultPopulator populator) {
		checkArgument(populator!=null);
		this.populator = populator;
	}
	
	public CriteriaResultFactory() { 
		this(new CriteriaResultPopulatorAdaptor());
	}
		
	public  <T extends Criteria> CriteriaResult createCriteriaResultForCriteria(Class<T> criteriaClass) {
		return createCriteriaResult(CriteriaType.valueForCriteriaClass(criteriaClass));
	}
	
	public <T extends CriteriaResult> CriteriaResult createCriteriaResult(Class<T> criteriaResultClass) {
		return createCriteriaResult(CriteriaType.valueForResultClass(criteriaResultClass));
	}
	
	public CriteriaResult createCriteriaResult(CriteriaType criteriaType) { 
		return populator.populate(createSpecificCriteriaResult(criteriaType));
	}
	
	private CriteriaResult createSpecificCriteriaResult(CriteriaType criteriaType) {		
		switch (criteriaType) {
		case COMBO_BOX:			
			return populator.populate(new ComboBoxCriteriaResult());
		case DATE_FIELD:
			return populator.populate(new DateFieldCriteriaResult());
		case ONE_CLICK:
			return populator.populate(new OneClickCriteriaResult());
		case SELECT:
			return populator.populate(new SelectCriteriaResult());
		case SIGNATURE:
			return populator.populate(new SignatureCriteriaResult());
		case TEXT_FIELD:
			return populator.populate(new TextFieldCriteriaResult());
		case UNIT_OF_MEASURE:
			return populator.populate(new UnitOfMeasureCriteriaResult()); 		
		case NUMBER_FIELD:
			return populator.populate(new NumberFieldCriteriaResult());
		default:
			throw new IllegalStateException("can't create criteria result for type '" + criteriaType == null ? "NULL Type" : criteriaType +"'"); 
		}
	}
	
}
