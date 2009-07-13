package com.n4systems.fieldid.validators.inspection;

import java.util.Collection;

import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaSection;
import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

public class CriteriaSetValidator extends FieldValidatorSupport {

	@SuppressWarnings("unchecked")
	public void validate( Object object ) throws ValidationException {
		String fieldName = getFieldName();
		AbstractAction action = (AbstractAction)object;
		Collection<CriteriaSection> set = (Collection<CriteriaSection>)this.getFieldValue(fieldName, action);
		
		if( set != null && !set.isEmpty() ) {
			for( CriteriaSection section : set ) {
				if( section.getCriteria() != null && !section.getCriteria().isEmpty() && !section.isRetired()  ) {
					boolean foundNonRetiredCriteria = false;
					for( Criteria criteria : section.getCriteria() ) {
						if( criteria != null && !criteria.isRetired() ) {
							foundNonRetiredCriteria = true;	
							return;
						}
					}
					if( foundNonRetiredCriteria == false ) {
						addFieldError( fieldName, action );
					}
				}
			}
		}
	}

}
