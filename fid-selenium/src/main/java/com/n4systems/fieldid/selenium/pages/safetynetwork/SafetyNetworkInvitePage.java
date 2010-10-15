package com.n4systems.fieldid.selenium.pages.safetynetwork;

import com.n4systems.fieldid.selenium.pages.FieldIDPage;
import com.thoughtworks.selenium.Selenium;
import static org.junit.Assert.fail;

public class SafetyNetworkInvitePage extends FieldIDPage {

	public SafetyNetworkInvitePage(Selenium selenium) {
		super(selenium);
	}

	
	public void sendEmail(){
		if (selenium.isElementPresent("//input[@id='sendInvite_email']")){
			selenium.type("//input[@id='sendInvite_email']", "123hurf@123durf.com");
			selenium.click("//.[@id='sendinvitation']");
		}else{
			fail("Couldn't find input box.");
		}
	}
	
	public boolean isEmailSent(){
		return selenium.isElementPresent("//span[.='Invitation Sent']"); 	
	}
	
}
