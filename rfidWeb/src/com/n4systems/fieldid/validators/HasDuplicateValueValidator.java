package com.n4systems.fieldid.validators;


public interface HasDuplicateValueValidator {
	public Long getUniqueID();
	public boolean duplicateValueExists( String formValue );
}
