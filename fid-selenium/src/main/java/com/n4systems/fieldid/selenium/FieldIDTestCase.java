package com.n4systems.fieldid.selenium;

import com.n4systems.fieldid.selenium.lib.DefaultFieldIdSelenium;
import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.mail.MailServer;
import com.n4systems.fieldid.selenium.misc.MiscDriver;
import com.n4systems.fieldid.selenium.pages.LoginPage;
import com.n4systems.fieldid.selenium.pages.WebEntity;
import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.SeleniumException;
import org.junit.After;
import org.junit.Before;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import static org.junit.Assert.fail;

public abstract class FieldIDTestCase extends DBTestCase {
	
	public static boolean runningInsideSuite = false;

    private static final int SHUTDOWN_ATTEMPTS = 5;
    private static final int SHUTDOWN_RETRY_INTERVAL_MS = 5000;



    public static FieldIdSelenium selenium;
	protected MiscDriver misc;
	protected Properties p;
	public static final String badProperty = "INVALID";
	public SystemDriverFactory systemDriverFactory;
    protected MailServer mailServer;
	
	protected void setInitialCompany(String initCompany) {
		getSeleniumConfig().setInitCompany(initCompany);
	}

    @Before
    public void startMailServer() {
        mailServer = new MailServer(getSeleniumConfig());
        mailServer.start();
    }

    @After
    public void shutdownMailServer() {
        if (mailServer != null)
            mailServer.stop();
    }
    
    @After
    public void deleteAllCookies() {
    	selenium.deleteAllVisibleCookies();
    }
	
	@Before
	public final void setupSelenium() throws Exception {
		loadProperties();
		if (!runningInsideSuite || selenium == null) {
			selenium = createWebBrowser();
		}
		openBaseSite(selenium);
		setWebBrowserSpeed();
		initializeSystemDrivers();
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
        if (selenium == null)
            return;

		if (!runningInsideSuite) {
			shutDownSelenium(selenium);
			selenium = null;
		} else {
			selenium.deleteAllVisibleCookies();
		}
	}
	
	public static void shutDownAllSeleniums() {
        boolean shutdownSuccess = false;
        for (int i = 0; i < SHUTDOWN_ATTEMPTS; i++) {
            try {
                shutDownSelenium(selenium);
                shutdownSuccess = true;
                break;
            } catch (SeleniumException e) {
                System.out.println("Exception shutting down selenium on attempt " + (i+1));
                e.printStackTrace();
                try { Thread.sleep(SHUTDOWN_RETRY_INTERVAL_MS); } catch (InterruptedException e1) { }
            }
        }

        if (!shutdownSuccess) {
            throw new RuntimeException("Unable to shutdown selenium after " + SHUTDOWN_ATTEMPTS + " attempts.");
        }
	}

	protected static void shutDownSelenium(FieldIdSelenium selenium) {
		selenium.close();
		selenium.stop();
	}

	private void initializeSystemDrivers() {
		systemDriverFactory = new SystemDriverFactory(selenium);
		misc = systemDriverFactory.createMiscDriver();
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

	protected LoginPage startAsCompany(String companyID) {
		String url = getFieldIDProtocol() + "://" + companyID + "." + getFieldIDDomain() + getFieldIDContextRoot();
		selenium.deleteAllVisibleCookies();
		selenium.open(url);
		return new LoginPage(selenium);
	}
	
	public LoginPage start() {
		return startAsCompany("n4");
	}
	
	protected void gotoReferralLink(String companyID, String referralCode) {
		String url = getFieldIDProtocol() + "://" + companyID + "." + getFieldIDDomain() +  "/signup/" + referralCode;
		selenium.open(url);
		selenium.waitForPageToLoad(WebEntity.DEFAULT_TIMEOUT);
	}

    protected <K> Set<K> setOf(K... items) {
        Set<K> set = new HashSet<K>();
        set.addAll(Arrays.asList(items));
        return set;
    }

}
