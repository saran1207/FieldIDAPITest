package com.n4systems.fieldid.validators.inspection;

import java.util.Collection;

import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaSection;
import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

/**
 * Validates that all CriteriaSections have at least one non-retired Criteria
 */
public class CriteriaSetValidator extends FieldValidatorSupport {

	@SuppressWarnings("unchecked")
	public void validate(Object object) throws ValidationException {
		String fieldName = getFieldName();
		AbstractAction action = (AbstractAction)object;
		
		Collection<CriteriaSection> sections = (Collection<CriteriaSection>)getFieldValue(fieldName, action);
		if (sections == null) {
			return;
		}
	
		for(CriteriaSection section: sections) {
			if (section == null || section.isRetired() || section.getCriteria() == null) {
				continue;
			}
			
			boolean hadNoValidCriteria = true;
			for(Criteria criteria : section.getCriteria()) {
				if(criteria != null && !criteria.isRetired()) {
					hadNoValidCriteria = false;
					break;
				}
			}
			
			if (hadNoValidCriteria) {
				// since we arn't targeting the sections directly for our errors
				// we can exit after we find the first section with no valid criteria 
				addFieldError(fieldName, action);
				return;
			}
		}
	}
	
}
