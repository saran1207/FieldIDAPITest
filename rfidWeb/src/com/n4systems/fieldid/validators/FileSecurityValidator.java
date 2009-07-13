package com.n4systems.fieldid.validators;

import java.io.File;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

public class FileSecurityValidator extends FieldValidatorSupport {

	private static final Logger logger = Logger.getLogger( FileSecurityValidator.class );
	
	public void validate( Object action ) throws ValidationException {
		String fileName = (String)getFieldValue( getFieldName(), action );
		if( fileName != null && fileName.trim().length() > 0 ) {
			fileNamePasses( fileName, action );
		}
	}
	
	protected boolean fileNamePasses( String fileName, Object action ) {
		File targetFile = new File( fileName );
		try {
			if(!targetFile.getCanonicalFile().equals(targetFile.getAbsoluteFile())) {
				logger.warn( "Attached file tried to target non uploaded file " + fileName );
				addFieldError(getFieldName(), action);
				return false;
			}
		} catch(Exception e) {
			logger.warn("Unable to resolve canonical name of file [" + targetFile.getAbsolutePath() + "]", e);
			addFieldError(getFieldName(), action);
			return false;
		}
		return true;
	}

}
