package com.n4systems.fieldid.testcase;

import static watij.finders.FinderFactory.*;
import com.n4systems.fieldid.*;
import watij.elements.*;
import watij.runtime.ie.IE;
import junit.framework.TestCase;

public class EmbeddedLogin extends TestCase {

	protected String embeddedLoginURL = "https://www.team.n4systems.com/static/html/embedded.html";
	protected String userID = "n4systems";
	protected String passwd = "makemore$";
	
	protected Frame newLoginFrame;
	protected Frame oldLoginFrame;
	protected TextField userName;
	protected TextField password;
	protected Checkbox rememberMe;
	protected Button signIn;
	protected Link forgotPassword;
	protected Link requestAccount;
	protected Link n4systems;
	protected static boolean once = true;
	protected static String timestamp;
	protected IE ie;
	protected FieldIDMisc misc;
	protected Login login;
	protected Home home;
	
	protected void setUp() throws Exception {
		super.setUp();
		ie = new IE();
		
		if(once) {
			once = false;
			timestamp = misc.createTimestampDirectory() + "/";
		}
		
		misc = new FieldIDMisc(ie);
		login = new Login(ie);
		home = new Home(ie);
		
		ie.start(embeddedLoginURL);
		ie.maximize();
		
		newLoginFrame = ie.frame(0);
		oldLoginFrame = ie.frame(1);
	}

	private void initializeHtmlElements(Frame f) throws Exception {
		userName = f.textField(xpath("//INPUT[@id='userName']"));
		assertNotNull("Could not find the 'User Name' text field", userName);
		
		password = f.textField(xpath("//INPUT[@id='password']"));
		assertNotNull("Could not find the 'Password' text field", password);
		
		rememberMe = f.checkbox(xpath("//INPUT[@id='logIntoSystem_rememberMe']"));
		assertNotNull("Could not find the 'remember my sign in information' checkbox", rememberMe);
		
		signIn = f.button(xpath("//INPUT[@id='loginButton']"));
		assertNotNull("Could not find the 'Sign In' button", signIn);
		
		forgotPassword = f.link(xpath("//A[contains(text(),'I forgot my password')]"));
		assertNotNull("Could not find the 'I forgot my password' link", forgotPassword);
		
		requestAccount = f.link(xpath("//A[contains(text(),'I want to request an account')]"));
		assertNotNull("Could not find the 'I want to request an account' link", requestAccount);
		
		n4systems = f.link(xpath("//DIV[@id='pageFooter']/DIV/A"));
		assertNotNull("Could not find the 'POWERED BY FIELDiD' link", n4systems);
	}
	
	public void testNewEmbeddedLoginSignIn() throws Exception {
		try {
			initializeHtmlElements(newLoginFrame);
			userName.set(userID);
			password.set(passwd);
			signIn.click();
			home.checkHomePageContentHeader();
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}

	}

	public void testNewEmbeddedLoginForgotPassword() throws Exception {
		try {
			initializeHtmlElements(newLoginFrame);
			forgotPassword.click();
			login.checkForgotMyPasswordContentHeader();
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}

	}

	public void testNewEmbeddedLoginRequestAnAccount() throws Exception {
		try {
			initializeHtmlElements(newLoginFrame);
			requestAccount.click();
			login.checkRequestAnAccountContentHeader();
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}

	}

	public void testOldEmbeddedLoginSignIn() throws Exception {
		try {
			initializeHtmlElements(oldLoginFrame);
			userName.set(userID);
			password.set(passwd);
			signIn.click();
			home.checkHomePageContentHeader();
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}

	}

	public void testOldEmbeddedLoginForgotPassword() throws Exception {
		try {
			initializeHtmlElements(oldLoginFrame);
			forgotPassword.click();
			login.checkForgotMyPasswordContentHeader();
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}

	}

	public void testOldEmbeddedLoginRequestAnAccount() throws Exception {
		try {
			initializeHtmlElements(oldLoginFrame);
			requestAccount.click();
			login.checkRequestAnAccountContentHeader();
		} catch (Exception e) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw e;
		} catch (Error err) {
			misc.myWindowCapture(timestamp + "/FAILURE-" + getName() + ".png");
			throw err;
		}

	}

	protected void tearDown() throws Exception {
		super.tearDown();
		login.close();
	}
}
