package com.n4systems.fieldid.actions.utils;

import com.opensymphony.xwork2.validator.annotations.FieldExpressionValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

public class PasswordEntry {

	private String password;
	private String passwordVerify;
	
	
	public String getPassword() {
		return password;
	}
	@RequiredStringValidator(type = ValidatorType.FIELD, message = "", key = "error.passwordrequired")
	@StringLengthFieldValidator(type = ValidatorType.FIELD, message = "", key = "errors.passwordlength", minLength = "5")
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	public String getPasswordVerify() {
		return passwordVerify;
	}
	
	@FieldExpressionValidator(expression = "passwordVerify == password", message = "", key = "error.passwordsmustmatch")
	public void setPasswordVerify(String passwordVerify) {
		this.passwordVerify = passwordVerify;
	}
	
	

}
