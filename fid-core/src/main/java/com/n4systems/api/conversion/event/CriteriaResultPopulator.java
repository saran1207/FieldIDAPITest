package com.n4systems.api.conversion.event;

import com.n4systems.model.*;

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
    public CriteriaResult populate(ScoreCriteriaResult result);
    public CriteriaResult populate(ObservationCountCriteriaResult result);

}
