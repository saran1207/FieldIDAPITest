package com.n4systems.fieldid.selenium;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;

import com.n4systems.fieldid.selenium.lib.DefaultFieldIdSelenium;
import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.mail.MailServer;
import com.n4systems.fieldid.selenium.pages.ChooseCompanyPage;
import com.n4systems.fieldid.selenium.pages.LoginPage;
import com.n4systems.fieldid.selenium.pages.SelectPackagePage;
import com.n4systems.fieldid.selenium.pages.SignUpPage;
import com.n4systems.fieldid.selenium.pages.WebEntity;
import com.n4systems.fieldid.selenium.pages.admin.AdminLoginPage;
import com.n4systems.fieldid.selenium.util.TimeLogger;
import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.SeleniumException;

public abstract class FieldIDTestCase extends DBTestCase {
	private static final Logger logger = Logger.getLogger(FieldIDTestCase.class);
	
	public static boolean runningInsideSuite = false;

    private static final int SHUTDOWN_ATTEMPTS = 5;
    private static final int SHUTDOWN_RETRY_INTERVAL_MS = 5000;

    public static FieldIdSelenium selenium;
	protected Properties p;
	public static final String badProperty = "INVALID";
    protected MailServer mailServer;
	
    @Before
    public void startMailServer() {
    	TimeLogger timeLogger = new TimeLogger(logger, "startMailServer()");
    	
        mailServer = new MailServer(getSeleniumConfig());
        mailServer.start();
        
        timeLogger.stop();
    }

    @After
    public void shutdownMailServer() {
    	TimeLogger timeLogger = new TimeLogger(logger, "shutdownMailServer()");
    	
        if (mailServer != null)
            mailServer.stop();
        
        timeLogger.stop();
    }
    	
	@Before
	public final void setupSelenium() throws Exception {
		TimeLogger timeLogger = new TimeLogger(logger, "setupSelenium()");
		
		loadProperties();
		if (!runningInsideSuite || selenium == null) {
			selenium = createWebBrowser();
		}
		openBaseSite(selenium);
		setWebBrowserSpeed();
		
		timeLogger.stop();
	}

	private void openBaseSite(Selenium selenium) {
		String url = generateUrl();
		selenium.open(url);
		selenium.open(getSeleniumConfig().getTestServerContextRoot());
		selenium.waitForPageToLoad(WebEntity.DEFAULT_TIMEOUT);
		selenium.windowMaximize();
	}
	
	protected FieldIdSelenium createOpenedWebBrowser() {
		FieldIdSelenium sel = createWebBrowser();
		openBaseSite(sel);
		return sel;
	}

	@After
	public final void tearDownSelenium() throws Exception {
		TimeLogger timeLogger = new TimeLogger(logger, "tearDownSelenium()");
		
        if (selenium == null)
            return;

		if (!runningInsideSuite) {
			shutDownSelenium(selenium);
			selenium = null;
		} else {
			selenium.deleteAllVisibleCookies();
		}
		
		timeLogger.stop();
	}
	
	public static void shutDownAllSeleniums() {
        boolean shutdownSuccess = false;
        for (int i = 0; i < SHUTDOWN_ATTEMPTS; i++) {
            try {
                shutDownSelenium(selenium);
                shutdownSuccess = true;
                break;
            } catch (SeleniumException e) {
            	logger.info("Exception shutting down selenium on attempt " + (i+1), e);
                try { Thread.sleep(SHUTDOWN_RETRY_INTERVAL_MS); } catch (InterruptedException e1) { }
            }
        }

        if (!shutdownSuccess) {
            throw new RuntimeException("Unable to shutdown selenium after " + SHUTDOWN_ATTEMPTS + " attempts.");
        }
	}

	protected static void shutDownSelenium(FieldIdSelenium selenium) {
        if (selenium != null) {
            try {
                selenium.close();
            } finally {
                selenium.stop();
            }
        }
	}

	private void setWebBrowserSpeed() {
		if(getSeleniumConfig().getActionDelay() != null) {
			selenium.setSpeed(getSeleniumConfig().getActionDelay());
		}
	}

	private void loadProperties() {
		try {
			p = new Properties();
			// if className.properties exists, load it
			String propertyFile = getClass().getSimpleName() + ".properties";
			InputStream in = getClass().getResourceAsStream("/"+propertyFile);
			if (in != null) {
				p.load(in);
				in.close();
			}
		} catch(Exception e) {
			fail("Error while loading properties file.");
			e.printStackTrace();
		}
	}
	
	public String getStringProperty(String key) {
		String result = System.getProperty(key, badProperty);
		if(result.equals(badProperty)) {
			result = p.getProperty(key, badProperty);
		}
		if(result.equals(badProperty)) {
			fail("The key '" + key + "' was missing or set incorrectly.");
		}
		return result;
	}
	
	protected FieldIdSelenium createWebBrowser() {
		String url = generateUrl();
		FieldIdSelenium selenium = new DefaultFieldIdSelenium(
                new DefaultSelenium(
                        getSeleniumConfig().getSeleniumServerHost(),
                        getSeleniumConfig().getSeleniumServerPort(),
                        getSeleniumConfig().getSeleniumBrowser(),
                        url));
		
		selenium.start();
		selenium.setTimeout(WebEntity.DEFAULT_TIMEOUT);
		
		return selenium;
	}
	
	protected String generateUrl() {
		return getSeleniumConfig().getProtocol()
                + "://" + getSeleniumConfig().getInitCompany()
                + "." + getSeleniumConfig().getTestServerDomain();
	}
	
	public String getFieldIDDomain() {
		return getSeleniumConfig().getTestServerDomain();
	}

	public String getFieldIDProtocol() {
		return getSeleniumConfig().getProtocol();
	}

	public String getFieldIDContextRoot() {
		return getSeleniumConfig().getTestServerContextRoot();
	}

	protected LoginPage startAsCompany(String companyID, Selenium selenium) {
        doStart(companyID, selenium);
		return new LoginPage(selenium);
	}

	protected ChooseCompanyPage startAsCompanyExpectingChoose(String companyID) {
        doStart(companyID, selenium);
		return new ChooseCompanyPage(selenium);
	}

    protected ChooseCompanyPage startAtChooseCompany() {
        String url = getFieldIDProtocol() + "://www." + getFieldIDDomain() + getFieldIDContextRoot() +"/chooseCompany.action";
        startAtUrl(selenium, url);
        return new ChooseCompanyPage(selenium);
    }

    private void doStart(String companyID, Selenium selenium) {
        String url = getFieldIDProtocol() + "://" + companyID + "." + getFieldIDDomain() + getFieldIDContextRoot();
        startAtUrl(selenium, url);
    }

    private void startAtUrl(Selenium selenium, String url) {
        selenium.deleteAllVisibleCookies();
        selenium.open(url);
    }

    protected LoginPage startAsCompany(String companyID) {
        return startAsCompany(companyID, selenium);
	}

    protected AdminLoginPage startAdmin() {
        String url = getFieldIDProtocol() + "://n4." + getFieldIDDomain() + getFieldIDContextRoot() + "admin/signIn.action";
        startAtUrl(selenium, url);
        return new AdminLoginPage(selenium);
    }
	
	public LoginPage start() {
		return startAsCompany("n4");
	}
	
	protected SelectPackagePage gotoReferralLink(String companyID, String referralCode) {
		String url = getFieldIDProtocol() + "://" + companyID + "." + getFieldIDDomain() +  "/signup/" + referralCode;
		selenium.open(url);
		return new SelectPackagePage(selenium);
	}

	protected SignUpPage gotoSignUpPage(String companyID, String pakageId, String referralCode) {
		String url = getFieldIDProtocol() + "://" + companyID + "." + getFieldIDDomain() +  "/fieldid/public/signUpAdd.action?packageId=" + pakageId  + "&refCode=" + referralCode;
		
		selenium.open(url);
		return new SignUpPage(selenium);
	}
	
    protected <K> Set<K> setOf(K... items) {
        Set<K> set = new HashSet<K>();
        set.addAll(Arrays.asList(items));
        return set;
    }

    protected void killSession() {
        selenium.deleteAllVisibleCookies();
    }

}
