package com.n4systems.api.validation.validators;

import java.util.Map;

import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.exporting.beanutils.ExportField;
import com.n4systems.model.security.SecurityFilter;

public class AccountTypeValidator implements FieldValidator {

	@Override
	public <V extends ExternalModelView> ValidationResult validate(Object fieldValue, V view, String fieldName, SecurityFilter filter, ExportField field, Map<String, Object> validationContext) {
		String typeString = (String) fieldValue;
		if (typeString==null) { 
			return ValidationResult.pass();
		}
				
		AccountType type = AccountType.valueFromName(typeString);
		return type==null ? ValidationResult.fail("invalid account type " + fieldValue) : ValidationResult.pass();				
	}
		
	
	// TODO DD : where should this enum live?   is it ok to have inner class? probably not. 
	enum AccountType {
		Full("Full"), Lite("Lite"), ReadOnly("Read-Only");
		
		private final String name;

		 AccountType(String name) { 
			this.name=name;
		}
	
		public String getName() {
			return name;
		}
		
		public static AccountType valueFromName(String name) {
			if (name==null) { 
				return null;
			}
			for (AccountType type:values()) { 
				if(name.equals(type.getName())) {
					return type;
				}
			}
			return null;
		}
	}	

}
