package com.n4systems.fieldid.handler.password;

import com.n4systems.model.security.PasswordPolicy;
import com.n4systems.model.user.User;
import com.n4systems.security.PasswordComplexityChecker;
import org.apache.commons.lang.time.DateUtils;

import java.util.Date;
import java.util.List;

public class PasswordHelper {

	private PasswordPolicy passwordPolicy;

	public PasswordHelper(PasswordPolicy passwordPolicy) {
		this.passwordPolicy=passwordPolicy;
	}

	public boolean isPasswordExpired(User loginUser) {
		Integer expiryDays = passwordPolicy.getExpiryDays();
		if (expiryDays==0 || expiryDays==null || loginUser.getPasswordChanged()==null) {
			return false;
		}
		Date expiry = DateUtils.addDays(loginUser.getPasswordChanged(), expiryDays);		
		return expiry.before(new Date());
	}

	public boolean isValidPassword(String newPassword) {
		PasswordComplexityChecker passwordChecker = new PasswordComplexityChecker(passwordPolicy);		
		return passwordChecker.isValid(newPassword);
	}
	
	public boolean isPasswordUnique(User user, String newPassword) {
		if (passwordPolicy.getUniqueness()==0) { 
			return true;
		}
		List<String> previousPasswords = user.getPreviousPasswords();
		int start = Math.max(0, previousPasswords.size()-passwordPolicy.getUniqueness());
		previousPasswords = previousPasswords.subList(start, previousPasswords.size());		
		return !previousPasswords.contains(User.hashPassword(newPassword));
	}

    public boolean containsName(User user, String newPassword) {
        if(!passwordPolicy.isCheckName()) {
            return false;
        }
        String firstName = user.getFirstName().trim().toLowerCase();
        String lastName = user.getLastName().trim().toLowerCase();

        if(newPassword.trim().toLowerCase().contains(firstName) || newPassword.trim().toLowerCase().contains(lastName)) {
            return true;
        } else {
            return false;
        }
    }
	
	public PasswordPolicy getPasswordPolicy() {
		return passwordPolicy;
	}
	
}
