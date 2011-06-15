package com.n4systems.api.validation.validators;

import java.util.Map;

import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.exporting.beanutils.SerializableField;
import com.n4systems.model.api.Exportable;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.OrgByNameLoader;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.GlobalIdExistsLoader;

public class OrgNameValidator implements FieldValidator {
	
	@Override
	public <V extends ExternalModelView> ValidationResult validate(Object fieldValue, V view, String fieldName, SecurityFilter filter, SerializableField field, Map<String, Object> validationContext) {
		OrgByNameLoader nameExistsLoader = createOrgByNameLoader(filter).setOrganizationName((String) fieldValue);

		BaseOrg org = nameExistsLoader.load();
		if (org != null) {
			System.out.println(org.getDisplayName() + " " + org.getID());
			return ValidationResult.pass();
		} else {
			System.out.println("org not found " + fieldValue);
			return ValidationResult.fail(OrgWithNameNotFoundValidationFail, fieldValue);
		}

	}

	private OrgByNameLoader createOrgByNameLoader(SecurityFilter filter) {
		return new OrgByNameLoader(filter);
	}

	protected GlobalIdExistsLoader getGlobalIdExistsLoader(Class<? extends Exportable> clazz, SecurityFilter filter) {
		return new GlobalIdExistsLoader(filter, clazz);
	}
}
