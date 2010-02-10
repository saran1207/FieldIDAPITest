package com.n4systems.fieldid;

import static watij.finders.FinderFactory.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.n4systems.fieldid.datatypes.FieldIDUser;

import watij.elements.*;
import watij.finders.Finder;
import watij.runtime.ie.IE;
import junit.framework.TestCase;

public class Login extends TestCase {

	IE ie = null;
	Properties p;
	InputStream in;
	FieldIDMisc misc = null;
	String propertyFile = "login.properties";
	Finder loginUserNameFinder;
	Finder loginPasswordFinder;
	Finder loginRegularLoginFinder;
	Finder loginSecurityCardFinder;
	Finder loginButtonFinder;
	String loginURL;
	Finder loginContentHeaderFinder;
	Finder loginChooseCompanyFinder;
	Finder loginCompanyIDFinder;
	Finder chooseCompanyContentHeaderFinder;
	Finder chooseCompanyContinueButtonFinder;
	Finder rememberLoginInfoFinder;
	Finder n4systemsLinkFinder;
	String n4systemsTitle;
	String n4systemsURL;
	Finder loginForgotMyPasswordFinder;
	Finder forgotPasswordContentHeaderFinder;
	Finder forgotPasswordSendButtonFinder;
	Finder forgotPasswordLoginFinder;
	Finder forgotPasswordUserNameFinder;
	Finder forgotPasswordLoginButtonFinder;
	Finder loginRequestAnAccountFinder;
	Finder requestAccountContentHeaderFinder;
	Finder requestAccountUserIDFinder;
	Finder requestAccountEmailAddressFinder;
	Finder requestAccountFirstNameFinder;
	Finder requestAccountLastNameFinder;
	Finder requestAccountPositionFinder;
	Finder requestAccountCountryFinder;
	Finder requestAccountTimeZoneFinder;
	Finder requestAccountCompanyNameFinder;
	Finder requestAccountPhoneNumberFinder;
	Finder requestAccountPasswordFinder;
	Finder requestAccountVerifyPasswordFinder;
	Finder requestAccountCommentsFinder;
	Finder requestAccountSubmitFinder;
	Finder requestAccountReturnToLoginPageFinder;
	Finder requestAccountConfirmMessageFinder;
	private Finder passwordResetEmailSentContentHeaderFinder;
	private Finder securedByThawteLogoFinder;
	private Finder securedByThawteImageFinder;

	/**
	 * Initialize the class and load up all the Finder information. This
	 * requires login.properties for a list of the Finder strings. If an id,
	 * xpath, text changes you should be able to modify it in the
	 * login.properties. If the way you recognize something changes, i.e. we
	 * were using xpath and now we are using id then you need to change this
	 * constructor.
	 * 
	 * @param ie - initialized instance of IE which is used to access Login page
	 */
	public Login(IE ie) {
		this.ie = ie;
		try {
			in = new FileInputStream(propertyFile);
			p = new Properties();
			p.load(in);
			misc = new FieldIDMisc(ie);
			securedByThawteLogoFinder = xpath(p.getProperty("securedbythawtelink", "NOT SET"));
			securedByThawteImageFinder = xpath(p.getProperty("securedbythawtelogo", "NOT SET"));
			loginUserNameFinder = xpath(p.getProperty("username", "NOT SET"));
			loginPasswordFinder = xpath(p.getProperty("password", "NOT SET"));
			loginRegularLoginFinder = xpath(p.getProperty("regularlogin", "NOT SET"));
			loginSecurityCardFinder = xpath(p.getProperty("securitycard", "NOT SET"));
			loginButtonFinder = xpath(p.getProperty("loginbutton", "NOT SET"));
			loginURL = p.getProperty("url", "NOT SET");
			loginContentHeaderFinder = xpath(p.getProperty("contentheader", "NOT SET"));
			loginChooseCompanyFinder = xpath(p.getProperty("choosecompany", "NOT SET"));
			loginCompanyIDFinder = id(p.getProperty("companyid", "NOT SET"));
			chooseCompanyContentHeaderFinder = xpath(p.getProperty("choosecontentheader", "NOT SET"));
			chooseCompanyContinueButtonFinder = xpath(p.getProperty("choosecontinuebutton", "NOT SET"));
			rememberLoginInfoFinder = xpath(p.getProperty("rememberlogininfo", "NOT SET"));
			n4systemsLinkFinder = xpath(p.getProperty("n4systems", "NOT SET"));
			n4systemsTitle = p.getProperty("n4systemstitle", "NOT SET");
			n4systemsURL = p.getProperty("n4systemsurl", "NOT SET");
			loginForgotMyPasswordFinder = xpath(p.getProperty("forgotpassword", "NOT SET"));
			forgotPasswordContentHeaderFinder = xpath(p.getProperty("forgotpasswordcontentheader", "NOT SET"));
			forgotPasswordSendButtonFinder = id(p.getProperty("forgotpasswordsendbutton", "NOT SET"));
			forgotPasswordLoginFinder = xpath(p.getProperty("forgotpasswordlogin", "NOT SET"));
			forgotPasswordUserNameFinder = id(p.getProperty("forgotpasswordusername", "NOT SET"));
			forgotPasswordLoginButtonFinder = xpath(p.getProperty("forgotpasswordloginbutton", "NOT SET"));
			loginRequestAnAccountFinder = xpath(p.getProperty("requestanaccount", "NOT SET"));
			requestAccountContentHeaderFinder = xpath(p.getProperty("registernewusercontentheader", "NOT SET"));
			requestAccountUserIDFinder = xpath(p.getProperty("registernewuseruserid", "NOT SET"));
			requestAccountEmailAddressFinder = xpath(p.getProperty("registernewuseremail", "NOT SET"));
			requestAccountFirstNameFinder = xpath(p.getProperty("registernewuserfirstname", "NOT SET"));
			requestAccountLastNameFinder = xpath(p.getProperty("registernewuserlastname", "NOT SET"));
			requestAccountPositionFinder = xpath(p.getProperty("registernewuserposition", "NOT SET"));
			requestAccountCountryFinder = xpath(p.getProperty("registernewusercountry", "NOT SET"));
			requestAccountTimeZoneFinder = xpath(p.getProperty("registernewusertimezone", "NOT SET"));
			requestAccountCompanyNameFinder = xpath(p.getProperty("registernewusercompanyname", "NOT SET"));
			requestAccountPhoneNumberFinder = xpath(p.getProperty("registernewuserphonenumber", "NOT SET"));
			requestAccountPasswordFinder = xpath(p.getProperty("registernewuserpassword", "NOT SET"));
			requestAccountVerifyPasswordFinder = xpath(p.getProperty("registernewuserverifypassword", "NOT SET"));
			requestAccountCommentsFinder = xpath(p.getProperty("registernewusercomments", "NOT SET"));
			requestAccountSubmitFinder = xpath(p.getProperty("registernewusersubmit", "NOT SET"));
			requestAccountReturnToLoginPageFinder = xpath(p.getProperty("registernewuserreturntologin", "NOT SET"));
			requestAccountConfirmMessageFinder = xpath(p.getProperty("registernewuserconfirmmessage", "NOT SET"));
			passwordResetEmailSentContentHeaderFinder = xpath(p.getProperty("passwordresetemailsentheader", "NOT SET"));
		} catch (FileNotFoundException e) {
			fail("Could not find the file '" + propertyFile	+ "' when initializing Home class");
		} catch (IOException e) {
			fail("File I/O error while trying to load '" + propertyFile + "'.");
		} catch (Exception e) {
			fail("Unknown exception occurred");
		}
	}

	/**
	 * Set the instance of IE used by this class for all interactions.
	 * 
	 * @param ie
	 * @throws Exception
	 */
	public void setIE(IE ie) throws Exception {
		this.ie = ie;
	}

	/**
	 * Get the instance of IE this class is currently interacting with.
	 * 
	 * @return
	 * @throws Exception
	 */
	public IE getIE() throws Exception {
		return ie;
	}

	/**
	 * Set the user name field on the Regular login page.
	 * 
	 * @param name
	 * @throws Exception
	 */
	public void setUserName(String name) throws Exception {
		assertNotNull(name);
		TextField userName = ie.textField(loginUserNameFinder);
		assertTrue("Could not find the Login User Name text field", userName.exists());
		userName.set(name);
		assertTrue("Value of user name was not set to " + name, userName.value().equals(name));
	}

	/**
	 * Set the password field on the Regular login page.
	 * 
	 * @param pw
	 * @throws Exception
	 */
	public void setPassword(String pw) throws Exception {
		assertNotNull(pw);
		TextField password = ie.textField(loginPasswordFinder);
		assertTrue("Could not find the Login Password text field", password.exists());
		password.set(pw);
	}

	/**
	 * Go to the Regular login page. Assumes you are on the Security Card login
	 * page.
	 * 
	 * @throws Exception
	 */
	public void gotoRegularLogin() throws Exception {
		Link regularLoginLink = ie.link(loginRegularLoginFinder);
		assertTrue("Could not find the Regular Login tab", regularLoginLink.exists());
		regularLoginLink.click();
	}

	/**
	 * Go to the Security Card login page. Assumes you are on the Regular login
	 * page.
	 * 
	 * @throws Exception
	 */
	public void gotoSecurityCardLogin() throws Exception {
		Link securityCardLink = ie.link(loginSecurityCardFinder);
		assertTrue("Could not find the Security Card Login tab", securityCardLink.exists());
		securityCardLink.click();
	}

	/**
	 * Click the login button. Does not set the user name or password
	 * information.
	 * 
	 * @throws Exception
	 */
	public void login() throws Exception {
		Button loginButton = ie.button(loginButtonFinder);
		assertTrue("Could not find the Login button", loginButton.exists());
		loginButton.click();
		ie.waitUntilReady();
	}

	/**
	 * Closes Internet Explorer and confirms it closed okay. May add more code
	 * here in the future, i.e. a kill process to guarantee iexplorer.exe is not
	 * running.
	 * 
	 * @throws Exception
	 */
	public void close() throws Exception {
		Thread.sleep(2000);	// give the thread a few seconds to stop before killing IE.
		ie.close();
		assertFalse("Closed Internet Explorer but it still exists.", ie.exists());
	}

	/**
	 * Goes to the Field ID login page using the default value set in class
	 * variable loginURL.
	 * 
	 * @throws Exception
	 */
	public void gotoLoginPage() throws Exception {
		String url = loginURL;
		gotoLoginPage(url);
	}

	/**
	 * Goes to the given URL but assumes it is a login page for Field ID. It
	 * will maximize the window and check for the Login page content header.
	 * 
	 * @param url
	 * @throws Exception
	 */
	public void gotoLoginPage(String url) throws Exception {
		assertNotNull(url);
		assertFalse(url.equals(""));
		ie.goTo(url);
		ie.maximize();
		checkLoginPageContentHeader();
	}

	public void checkForThawteLogo() throws Exception {
		Link securedByThawteLogo = ie.link(securedByThawteLogoFinder);
		assertTrue("Could not find the link for the Secured by Thawte logo", securedByThawteLogo.exists());
		HtmlElement ThawteImage = ie.htmlElement(securedByThawteImageFinder);
		assertTrue("Could not find the Secured by Thawte logo", ThawteImage.exists());
	}

	public void checkLoginPageContentHeader() throws Exception {
		HtmlElement loginContentHeader = ie.htmlElement(loginContentHeaderFinder);
		assertTrue("Could not find the Login page content header.", loginContentHeader.exists());
	}

	/**
	 * Goes to the link for choosing a different company.
	 * 
	 * @throws Exception
	 */
	public void gotoChooseCompany() throws Exception {
		Link chooseCompanyLink = ie.link(loginChooseCompanyFinder);
		assertTrue("Could not find the link to choose company on Login page", chooseCompanyLink.exists());
		chooseCompanyLink.click();
		HtmlElement chooseCompanyContentHeader = ie.htmlElement(chooseCompanyContentHeaderFinder);
		assertTrue("Could not find the Choose A Company page content header", chooseCompanyContentHeader.exists());
	}

	/**
	 * sets the company name field and clicks
	 * the Continue button. If a Cancel button is added to the choose a company
	 * page, this needs to be refactored to just set the company field and a
	 * separate clickChooseACompanyContinue method needs to be created.
	 * 
	 * @param company
	 * @throws Exception
	 */
	public void setCompany(String company) throws Exception {
		assertNotNull(company);
		assertFalse(company.equals(""));
		String url = ie.url();
		url = url.replaceFirst("//[^\\.]*", "//" + company);
		ie.goTo(url);
	}

	/**
	 * Enable the check box for remembering login information.
	 * 
	 * @throws Exception
	 */
	public void setRememberLoginInformation() throws Exception {
		setRememberLoginInformation(true);
	}

	/**
	 * Disable the check box for remembering login information.
	 * 
	 * @throws Exception
	 */
	public void unsetRememberLoginInformation() throws Exception {
		setRememberLoginInformation(false);
	}

	/**
	 * Toggle the remember login information field based on the input.
	 * 
	 * @param value
	 * @throws Exception
	 */
	public void setRememberLoginInformation(boolean value) throws Exception {
		Checkbox rememberLoginInfo = ie.checkbox(rememberLoginInfoFinder);
		assertTrue("Could not find the 'remember my login information' checkbox on Login page", rememberLoginInfo.exists());
		rememberLoginInfo.set(value);
	}

	/**
	 * Find the link to www.n4systems.com and click it. A second browser window
	 * should open up. This will confirm it is the correct window then close it.
	 * 
	 * @throws Exception
	 */
	public void gotoN4Systems() throws Exception {
		Link n4systemsLink = ie.link(n4systemsLinkFinder);
		assertTrue("Could not find the link to N4 Systems", n4systemsLink.exists());
		n4systemsLink.click();
		ie.waitUntilReady();
		IE ie2 = ie.childBrowser();
		String title = ie2.title();
		assertTrue("Title of the N4 Systems website is '" + title + "' but expected '" + n4systemsTitle + "'", title.equals(n4systemsTitle));
		String url = ie2.url();
		assertTrue("URL of the N4 Systems website is '" + url + "' but expected '" + n4systemsURL + "'", url.equals(n4systemsURL));
		ie2.close();
	}

	/**
	 * Go to the Forgot Password link on the login page.
	 * 
	 * @throws Exception
	 */
	public void gotoForgotMyPassword() throws Exception {
		Link forgotMyPasswordLink = ie.link(loginForgotMyPasswordFinder);
		assertTrue("Could not find the link to 'I forgot my password' on Login page", forgotMyPasswordLink.exists());
		forgotMyPasswordLink.click();
		ie.waitUntilReady();
		checkForgotMyPasswordContentHeader();
	}

	public void checkForgotMyPasswordContentHeader() throws Exception {
		HtmlElement forgotContentHeader = ie.htmlElement(forgotPasswordContentHeaderFinder);
		assertTrue("Could not find the Forgot Password content header", forgotContentHeader.exists());
	}

	/**
	 * Click on the Login link on the Forgot Password page and confirm it takes
	 * you back to the login page.
	 * 
	 * @throws Exception
	 */
	public void gotoLoginFromForgotPassword() throws Exception {
		Link loginLink = ie.link(forgotPasswordLoginFinder);
		assertTrue("Could not find the link to Login on Forgot Password page", loginLink.exists());
		loginLink.click();
	}

	/**
	 * Set the user name field on the Forgot Password page.
	 * 
	 * @param name
	 * @throws Exception
	 */
	public void setForgotPasswordUserName(String name) throws Exception {
		assertNotNull(name);
		assertFalse(name.equals(""));
		TextField userNameTextField = ie.textField(forgotPasswordUserNameFinder);
		assertTrue("Could not find the User Name text field on Forgot Password page", userNameTextField.exists());
		userNameTextField.set(name);
	}

	/**
	 * This method starts from the Login Page, goes to the Forgot Password
	 * page, sets the user name and submits the request.
	 * 
	 * @param name
	 * @throws Exception
	 */
	public void setForgotPassword(String name) throws Exception {
		assertNotNull(name);
		assertFalse(name.equals(""));
		gotoForgotMyPassword();
		setForgotPasswordUserName(name);
		Button send = ie.button(forgotPasswordSendButtonFinder);
		assertTrue("Could not find the Send button on Forgot Password page", send.exists());
		send.click();
		ie.waitUntilReady();
		checkPasswordResetEmailSentContentHeader();
	}

	private void checkPasswordResetEmailSentContentHeader() throws Exception {
		HtmlElement contentHeader = ie.htmlElement(passwordResetEmailSentContentHeaderFinder);
		assertTrue("Could not find the Forgot Password content header", contentHeader.exists());
	}

	/**
	 * After you submit your user name for Forgot Password it takes you
	 * to a second Forgot Password page. This method will click the Login
	 * link on the second Forgot Password page.
	 * 
	 * @throws Exception
	 */
	public void gotoLoginFromForgotPassword2() throws Exception {
		Button login = ie.button(forgotPasswordLoginButtonFinder);
		assertTrue("Could not find the Login button on the second Forgot Password page (after Send)", login.exists());
		login.click();
		ie.waitUntilReady();
		checkLoginPageContentHeader();
	}

	/**
	 * From the login page, this goes to the Request An Account page.
	 * 
	 * @throws Exception
	 */
	public void gotoRequestAnAccount() throws Exception {
		Link requestAccountLink = ie.link(loginRequestAnAccountFinder);
		assertTrue("Could not find the link to request an account on Login page", requestAccountLink.exists());
		requestAccountLink.click();
		checkRequestAnAccountContentHeader();
	}

	public void checkRequestAnAccountContentHeader() throws Exception {
		HtmlElement requestAccountContentHeader = ie.htmlElement(requestAccountContentHeaderFinder);
		assertTrue("Could not find the Register New User content header", requestAccountContentHeader.exists());
	}

	/**
	 * This fills in the Request Account page.
	 * 
	 * @param newuser
	 * @throws Exception
	 */
	public void setRequestAnAccount(FieldIDUser newuser) throws Exception {
		// Required fields
		assertNotNull(newuser.getUserID());
		assertFalse(newuser.getUserID().equals(""));
		assertNotNull(newuser.getEmailAddress());
		assertFalse(newuser.getEmailAddress().equals(""));
		assertNotNull(newuser.getFirstName());
		assertFalse(newuser.getFirstName().equals(""));
		assertNotNull(newuser.getLastName());
		assertFalse(newuser.getLastName().equals(""));
		assertNotNull(newuser.getCompanyName());
		assertFalse(newuser.getCompanyName().equals(""));
		assertNotNull(newuser.getPhoneNumber());
		assertFalse(newuser.getPhoneNumber().equals(""));
		assertNotNull(newuser.getPassword());
		assertFalse(newuser.getPassword().equals(""));

		TextField userID = ie.textField(requestAccountUserIDFinder);
		assertTrue("Could not find the UserID text field on Register New User page", userID.exists());
		userID.set(newuser.getUserID());

		TextField emailAddress = ie.textField(requestAccountEmailAddressFinder);
		assertTrue("Could not find the email text field on Register New User page", emailAddress.exists());
		emailAddress.set(newuser.getEmailAddress());

		TextField firstName = ie.textField(requestAccountFirstNameFinder);
		assertTrue("Could not find the first name text field on Register New User page", firstName.exists());
		firstName.set(newuser.getFirstName());

		TextField lastName = ie.textField(requestAccountLastNameFinder);
		assertTrue("Could not find the last name text field on Register New User page", lastName.exists());
		lastName.set(newuser.getLastName());

		TextField position = ie.textField(requestAccountPositionFinder);
		assertTrue("Could not find the position text field on Register New User page", position.exists());
		if (newuser.getPosition() != null) {
			position.set(newuser.getUserID());
		}
		
		SelectList country = ie.selectList(requestAccountCountryFinder);
		assertTrue("Could not find the country select list on Register New User page", country.exists());
		String c = newuser.getCountry();
		if (c != null) {
			c = "/" + c + "/";	// change to regex
			Option cc = country.option(text(c));
			assertTrue("Could not find the option '" + c + "' in the country list.", cc.exists());
			cc.select();
			misc.waitForJavascript();
		}

		SelectList timeZone = ie.selectList(requestAccountTimeZoneFinder);
		assertTrue("Could not find the time zone select list on Register New User page", timeZone.exists());
		String tZone = newuser.getTimeZone();
		if (tZone != null) {
			tZone = "/" + tZone + "/";	// change to regex
			Option tz = timeZone.option(text(tZone));
			assertTrue("Could not find the option '" + tZone + "' in the time zone list.", tz.exists());
			tz.select();
		}

		TextField companyName = ie.textField(requestAccountCompanyNameFinder);
		assertTrue("Could not find the company name text field on Register New User page", companyName.exists());
		companyName.set(newuser.getCompanyName());

		TextField phoneNumber = ie.textField(requestAccountPhoneNumberFinder);
		assertTrue("Could not find the phone number text field on Register New User page", phoneNumber.exists());
		phoneNumber.set(newuser.getPhoneNumber());

		TextField password = ie.textField(requestAccountPasswordFinder);
		assertTrue("Could not find the password text field on Register New User page", password.exists());
		password.set(newuser.getPassword());

		TextField verifyPassword = ie.textField(requestAccountVerifyPasswordFinder);
		assertTrue("Could not find the verify password text field on Register New User page", verifyPassword.exists());
		verifyPassword.set(newuser.getPassword());

		TextField comments = ie.textField(requestAccountCommentsFinder);
		assertTrue("Could not find the comments text field on Register New User page", comments.exists());
		if (newuser.getComments() != null) {
			comments.set(newuser.getComments());
		}

		Button submit = ie.button(requestAccountSubmitFinder);
		assertTrue("Could not find the submit button on the Register New User page", submit.exists());
		submit.click();
		ie.waitUntilReady();
		misc.checkForErrorMessagesOnCurrentPage();

		// Confirm we got the correct message
		HtmlElement confirm = ie.htmlElement(requestAccountConfirmMessageFinder);
		assertTrue("Could not find the confirmation message that the account request was submitted.", confirm.exists());
		String confirmMessage1 = "Your account has been created with the user name " + newuser.getUserID();
		String confirmMessage2 = "and email address " + newuser.getEmailAddress();
		String confirmMessage3 = "The administrator of the account will need to verify your information before you can log in.";
		String confirmMessage4 = "You will receive an email when your login has been activated.";
		assertTrue(confirm.text().contains(confirmMessage1));
		assertTrue(confirm.text().contains(confirmMessage2));
		assertTrue(confirm.text().contains(confirmMessage3));
		assertTrue(confirm.text().contains(confirmMessage4));

		gotoLoginPageFromRequestAccount();
	}

	/**
	 * Goes back to the login page after requesting an account.
	 * 
	 * @throws Exception
	 */
	public void gotoLoginPageFromRequestAccount() throws Exception {
		Button returnToLoginPage = ie.button(requestAccountReturnToLoginPageFinder);
		assertTrue("Could not find the link to return to the login page", returnToLoginPage.exists());
		returnToLoginPage.click();
	}
	
	/**
	 * 
	 * @param company - the name of a valid tenant
	 * @param name - login name
	 * @param password - for requesting a new account
	 * @throws Exception
	 */
	public void validate(String company, String name, String password) throws Exception {
		IE ie2 = getIE();
		setIE(ie2);
		gotoLoginPage();
		gotoLoginPage(loginURL);
		setRememberLoginInformation();
		unsetRememberLoginInformation();
		setRememberLoginInformation(false);
		gotoSecurityCardLogin();
		gotoRegularLogin();
		setCompany(company);
		gotoForgotMyPassword();
		gotoLoginFromForgotPassword();
		setForgotPassword(name);
		gotoLoginFromForgotPassword2();
		gotoN4Systems();
		String userID = "val-" + misc.getRandomString(10);
		String email = "dev@n4systems.com";
		String firstName = "Dev";
		String lastName = "Validate";
		String position = "seated";
		String timeZone = "Toronto";
		String phoneNumber = "416-599-6464";
		String comments = "This is an account requested by the Login.validate() method.";
		FieldIDUser newuser = new FieldIDUser(userID, email, firstName, lastName, position, timeZone, company, phoneNumber, password, comments);
		gotoRequestAnAccount();
		setRequestAnAccount(newuser);
		setUserName(name);
		setPassword(password);
		login();
		misc.logout();
	}
}
