package com.n4systems.fieldid.validators;

import java.util.Collection;
import java.util.List;

import com.n4systems.fieldid.actions.helpers.InfoFieldInput;
import com.n4systems.fieldid.actions.helpers.InfoOptionInput;

class InfoOptionValidation {
	private final Collection<InfoOptionInput> infoOptionInputs;
	private final List<InfoFieldInput> infoFieldInputs;

	public InfoOptionValidation(Collection<InfoOptionInput> infoOptions, List<InfoFieldInput> infoFields) {
		this.infoOptionInputs = infoOptions;
		this.infoFieldInputs = infoFields;
	}

	public boolean isValid() {
		if (infoOptionInputs != null) {
			for (InfoOptionInput infoOption : infoOptionInputs) {
				// only validate if the info field hasn't been deleted.
				InfoFieldInput infoField = getInfoFieldForOption(infoFieldInputs, infoOption);
				
				if (infoFieldRequiresInfoOptionValidation(infoField)) {
					if (isInfoOptionInvalid(infoOption)) {
						return false;
					}
				}
			}
		}

		return true;
	}

	private boolean infoFieldRequiresInfoOptionValidation(InfoFieldInput infoField) {
		return !infoField.isDeleted() && infoField.hasStaticOptions();
	}

	private boolean isInfoOptionInvalid(InfoOptionInput infoOption) {
		return infoOption.getName() != null && infoOption.getName().trim().equals("") && !infoOption.isDeleted();
	}

	private InfoFieldInput getInfoFieldForOption(List<InfoFieldInput> infoFields, InfoOptionInput infoOption) {
		return infoFields.get(infoOption.getInfoFieldIndex().intValue());
	}
}