package com.n4systems.fieldid.validators.event;

import java.util.Collection;

import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.model.OneClickCriteria;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaSection;
import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

public class CriteriaStateSetValidator extends FieldValidatorSupport {

	@SuppressWarnings("unchecked")
	public void validate( Object object ) throws ValidationException {
		String fieldName = getFieldName();
		AbstractAction action = (AbstractAction)object;
		Collection<CriteriaSection> set = (Collection<CriteriaSection>)this.getFieldValue(fieldName, action);
		
		
		
		if( set != null && !set.isEmpty() ) {
			for( CriteriaSection section : set ) {
				if( section != null && section.getCriteria() != null && !section.getCriteria().isEmpty() ) {
					for( Criteria criteria : section.getCriteria() ) {
                        if (criteria instanceof OneClickCriteria) {
                            OneClickCriteria oneClickCriteria = (OneClickCriteria) criteria;
                            if( oneClickCriteria.getStates() == null || oneClickCriteria.getStates().getId() == null ) {
                                addFieldError( fieldName, action );
                                return;
                            }
                        }
					}
				}
			}
		}
	}

}
