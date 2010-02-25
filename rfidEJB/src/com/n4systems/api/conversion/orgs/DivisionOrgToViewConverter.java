package com.n4systems.api.conversion.orgs;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.model.FullExternalOrgView;
import com.n4systems.model.orgs.DivisionOrg;

public class DivisionOrgToViewConverter extends ExternalOrgToViewConverter<DivisionOrg> {

	public DivisionOrgToViewConverter() {
		super();
	}

	@Override
	public void copyProperties(DivisionOrg from, FullExternalOrgView to) throws ConversionException {
		super.copyProperties(from, to);
		to.setTypeToDivision();
	}

}
