package com.n4systems.api.conversion.event;

import com.n4systems.model.ComboBoxCriteriaResult;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.DateFieldCriteriaResult;
import com.n4systems.model.OneClickCriteriaResult;
import com.n4systems.model.SelectCriteriaResult;
import com.n4systems.model.SignatureCriteriaResult;
import com.n4systems.model.TextFieldCriteriaResult;
import com.n4systems.model.UnitOfMeasureCriteriaResult;

public class CriteriaResultPopulatorAdaptor implements CriteriaResultPopulator {

	@Override
	public CriteriaResult populate(CriteriaResult result) {
		return result;
	}

	@Override
	public CriteriaResult populate(OneClickCriteriaResult result) {
		return result;
	}

	@Override
	public CriteriaResult populate(ComboBoxCriteriaResult result) {
		return result;
	}

	@Override
	public CriteriaResult populate(DateFieldCriteriaResult result) {
		return result;
	}

	@Override
	public CriteriaResult populate(SelectCriteriaResult result) {
		return result;
	}

	@Override
	public CriteriaResult populate(UnitOfMeasureCriteriaResult result) {
		return result;
	}

	@Override
	public CriteriaResult populate(TextFieldCriteriaResult result) {
		return result;
	}

	@Override
	public CriteriaResult populate(SignatureCriteriaResult result) {
		return result;
	}

}
