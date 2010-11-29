package com.n4systems.fieldid.selenium;

import com.n4systems.fieldid.selenium.lib.DefaultFieldIdSelenium;
import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;
import com.n4systems.fieldid.selenium.misc.MiscDriver;
import com.n4systems.fieldid.selenium.pages.LoginPage;
import com.n4systems.fieldid.selenium.persistence.MinimalTenantDataSetup;
import com.n4systems.fieldid.selenium.persistence.PersistenceCallback;
import com.n4systems.fieldid.selenium.persistence.PersistenceTemplate;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.fieldid.selenium.persistence.TenantCleaner;
import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.Transaction;
import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.SeleniumException;
import org.junit.After;
import org.junit.Before;

import java.io.InputStream;
import java.util.Properties;

import static org.junit.Assert.fail;

public abstract class FieldIDTestCase {
	
	public static boolean runningInsideSuite = false;

    private static final int SHUTDOWN_ATTEMPTS = 5;
    private static final int SHUTDOWN_RETRY_INTERVAL_MS = 5000;

    private SeleniumConfig seleniumConfig;

    public static FieldIdSelenium selenium;
	protected MiscDriver misc;
	protected Properties p;
	public static final String badProperty = "INVALID";
	public SystemDriverFactory systemDriverFactory;
	
	protected void setInitialCompany(String initCompany) {
		this.seleniumConfig.setInitCompany(initCompany);
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

    private SeleniumConfig getSeleniumConfig() {
        if (seleniumConfig == null) {
            seleniumConfig = new SeleniumConfigLoader().loadConfig();
        }
        return seleniumConfig;
    }
	
	private void openBaseSite(Selenium selenium) {
		String url = generateUrl();
		selenium.open(url);
		selenium.open(getSeleniumConfig().getTestServerContextRoot());
		selenium.waitForPageToLoad(MiscDriver.DEFAULT_TIMEOUT);
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

	/**
	 * Uses the selenium.setSpeed(milliseconds) to slow the system down
	 * if the actionDelay variable is set. This variable is set via the
	 * property fieldid-delay. If this property is not set there will be
	 * no delay.
	 */
	private void setWebBrowserSpeed() {
		if(getSeleniumConfig().getActionDelay() != null) {
			selenium.setSpeed(getSeleniumConfig().getActionDelay());
		}
	}

	/**
	 * If a property file exists, load the key/value pairs
	 * into the Test accessible variable p. These properties
	 * are not available to the libraries. The name of the
	 * property file will be the name of the class running
	 * the test. For example,
	 * 
	 * 		class SmokeTests {
	 * 			@Test
	 * 			public void Some_Test() {
	 * 				// your code here
	 * 			}
	 * 		}
	 * 
	 * The property file will be SmokeTests.properties.
	 */
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
	
	/**
	 * If a System property exists, this will return the System property.
	 * Otherwise it will check to see if the property was loaded by the
	 * loadProperties() method. If the key is not set this will cause the
	 * test case to <STRONG>fail</STRONG>.
	 * 
	 * @param key
	 * @return the associated value for the given key.
	 * @see loadingProperties
	 */
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
	
	/**
	 * Create an instance of Selenium with the pre-determined web browser
	 * pointing to the /fieldid/ application on the test machine. I also
	 * have this method maximize the window in case we want to do a screen
	 * capture or someone wants to watch the automation run.
	 */
	protected FieldIdSelenium createWebBrowser() {
		String url = generateUrl();
		FieldIdSelenium selenium = new DefaultFieldIdSelenium(
                new DefaultSelenium(
                        seleniumConfig.getSeleniumServerHost(),
                        seleniumConfig.getSeleniumServerPort(),
                        seleniumConfig.getSeleniumBrowser(),
                        url));
		
		selenium.start();
		selenium.setTimeout(MiscDriver.DEFAULT_TIMEOUT);
		
		return selenium;
	}
	
	protected String generateUrl() {
		return getSeleniumConfig().getProtocol()
                + "://" + getSeleniumConfig().getInitCompany()
                + "." + getSeleniumConfig().getTestServerDomain();
	}
	
	/**
	 * Get the domain we are currently using. This is typically
	 * team.n4systems.net or grumpy.n4systems.net.
	 * 
	 * @return
	 */
	public String getFieldIDDomain() {
		return getSeleniumConfig().getTestServerDomain();
	}

	/**
	 * Get the protocol we are currently using. This is typically
	 * https but can be http as well.
	 * 
	 * @return
	 */
	public String getFieldIDProtocol() {
		return getSeleniumConfig().getProtocol();
	}

	/**
	 * Get the context root of the Field ID application. This is typically
	 * /fieldid/ but it can be /fieldidadmin/ or we might change the name
	 * entirely.
	 * 
	 * @return
	 */
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
		selenium.waitForPageToLoad(MiscDriver.DEFAULT_TIMEOUT);
	}

    public static final String[] TEST_TENANT_NAMES = { "test1", "test2" };

    public static final String TEST_ASSET_TYPE_1 = "TestType1";
    public static final String TEST_ASSET_TYPE_2 = "TestType2";
    public static final String[] TEST_ASSET_TYPES = {TEST_ASSET_TYPE_1, TEST_ASSET_TYPE_2};

    @Before
    public void doDatabaseSetup() throws Exception {

        PersistenceManager.persistenceUnit = PersistenceManager.TESTING_PERSISTENCE_UNIT;
        PersistenceManager.testProperties.put("hibernate.connection.url", getSeleniumConfig().getDatabaseUrl());
        PersistenceManager.testProperties.put("hibernate.connection.username", getSeleniumConfig().getDatabaseUser());
        PersistenceManager.testProperties.put("hibernate.connection.password", getSeleniumConfig().getDatabasePassword());

        new PersistenceTemplate(new PersistenceCallback() {
            @Override
            public void doInTransaction(Transaction transaction) throws Exception {
                for (String tenantName : TEST_TENANT_NAMES) {
                    TenantCleaner cleaner = new TenantCleaner();
                    cleaner.cleanTenant(transaction.getEntityManager(), tenantName);
                }
            }
        }).execute();

        new PersistenceTemplate(new PersistenceCallback() {
            @Override
            public void doInTransaction(Transaction transaction) throws Exception {
                for (String tenantName : TEST_TENANT_NAMES) {
                    MinimalTenantDataSetup dataSetup  = new MinimalTenantDataSetup(transaction, tenantName);
                    dataSetup.setupMinimalData();
                    dataSetup.createTestAssetTypes(TEST_ASSET_TYPES);
                }
            }
        }).execute();

        new PersistenceTemplate(new PersistenceCallback() {
            @Override
            public void doInTransaction(Transaction transaction) throws Exception {
                Scenario scenario = new Scenario(transaction);
                setupScenario(scenario);
                scenario.persistAllBuiltObjects();
            }
        }).execute();
    }

    public void setupScenario(Scenario scenario) {}

}
