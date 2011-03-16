package com.n4systems.fieldid.selenium;

import java.util.Arrays;
import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.junit.Before;

import com.n4systems.fieldid.context.ThreadLocalUserContext;
import com.n4systems.fieldid.selenium.persistence.MinimalTenantDataSetup;
import com.n4systems.fieldid.selenium.persistence.PersistenceCallback;
import com.n4systems.fieldid.selenium.persistence.PersistenceTemplate;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.fieldid.selenium.persistence.TenantCleaner;
import com.n4systems.fieldid.selenium.util.TimeLogger;
import com.n4systems.model.Tenant;
import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.Transaction;

public abstract class DBTestCase {
	private static final Logger logger = Logger.getLogger(DBTestCase.class);
	
    public static final String TEST_ASSET_TYPE_1 = "TestType1";
    public static final String TEST_ASSET_TYPE_2 = "TestType2";

    public static final String[] TEST_TENANT_NAMES = { "test1", "test2" };
    // Tests that need to sign up as a new tenant may use one of these:
    public static final String[] TEST_CREATED_TENANT_NAMES = { "newtest1", "newtest2" };
    public static final String[] TEST_ASSET_TYPES = {TEST_ASSET_TYPE_1, TEST_ASSET_TYPE_2};

    private SeleniumConfig seleniumConfig;

    protected SeleniumConfig getSeleniumConfig() {
        if (seleniumConfig == null) {
            seleniumConfig = new SeleniumConfigLoader().loadConfig();
        }
        return seleniumConfig;
    }

    @Before
    public void doDatabaseSetup() throws Exception {
    	TimeLogger timeLogger = new TimeLogger(logger, "doDatabaseSetup()");
    	
        PersistenceManager.persistenceUnit = PersistenceManager.TESTING_PERSISTENCE_UNIT;
        PersistenceManager.testProperties.put("hibernate.connection.url", getSeleniumConfig().getDatabaseUrl());
        PersistenceManager.testProperties.put("hibernate.connection.username", getSeleniumConfig().getDatabaseUser());
        PersistenceManager.testProperties.put("hibernate.connection.password", getSeleniumConfig().getDatabasePassword());

        new PersistenceTemplate(new PersistenceCallback() {
            @Override
            public void doInTransaction(Transaction transaction) throws Exception {
                TenantCleaner cleaner = new TenantCleaner(transaction.getEntityManager());
                cleaner.cleanTenants(TEST_TENANT_NAMES);
                cleaner.cleanTenants(TEST_CREATED_TENANT_NAMES);
                
                Query q = transaction.getEntityManager().createQuery("from " + Tenant.class.getName() + " where name in (:tenantNames)");
                q.setParameter("tenantNames", Arrays.asList(TEST_CREATED_TENANT_NAMES));
                for (Tenant t : (List<Tenant>)q.getResultList()) {
                    transaction.getEntityManager().remove(t);
                }
            }
        }).execute();

        new PersistenceTemplate(new PersistenceCallback() {
            @Override
            public void doInTransaction(Transaction transaction) throws Exception {
                for (String tenantName : TEST_TENANT_NAMES) {
                    ThreadLocalUserContext.getInstance().setCurrentUser(null);
                    MinimalTenantDataSetup dataSetup  = new MinimalTenantDataSetup(transaction, tenantName);
                    dataSetup.setupMinimalData();
                    // TODO: Testing whether we need a current user set for these items, might make tenant cleaner easier
                    ThreadLocalUserContext.getInstance().setCurrentUser(null);
//                    ThreadLocalUserContext.getInstance().setCurrentUser(dataSetup.getCreatedUser());
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
        
        timeLogger.stop();
    }

    public void setupScenario(Scenario scenario) {}

}
