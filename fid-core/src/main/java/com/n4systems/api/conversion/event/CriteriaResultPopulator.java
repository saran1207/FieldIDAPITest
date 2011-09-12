package com.n4systems.api.conversion.event;

import com.n4systems.model.ComboBoxCriteriaResult;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.DateFieldCriteriaResult;
import com.n4systems.model.NumberFieldCriteriaResult;
import com.n4systems.model.OneClickCriteriaResult;
import com.n4systems.model.SelectCriteriaResult;
import com.n4systems.model.SignatureCriteriaResult;
import com.n4systems.model.TextFieldCriteriaResult;
import com.n4systems.model.UnitOfMeasureCriteriaResult;

public interface CriteriaResultPopulator {

	public CriteriaResult populate(CriteriaResult criteriaResult);
	
	public CriteriaResult populate(OneClickCriteriaResult result);  
	public CriteriaResult populate(ComboBoxCriteriaResult result);  
	public CriteriaResult populate(DateFieldCriteriaResult result);  
	public CriteriaResult populate(SelectCriteriaResult result);  
	public CriteriaResult populate(UnitOfMeasureCriteriaResult result);  
	public CriteriaResult populate(TextFieldCriteriaResult result);
	public CriteriaResult populate(SignatureCriteriaResult result);
	public CriteriaResult populate(NumberFieldCriteriaResult result);
}
