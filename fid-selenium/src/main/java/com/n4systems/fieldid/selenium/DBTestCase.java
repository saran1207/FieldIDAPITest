package com.n4systems.fieldid.selenium;

import com.n4systems.fieldid.context.ThreadLocalUserContext;
import com.n4systems.fieldid.selenium.persistence.MinimalTenantDataSetup;
import com.n4systems.fieldid.selenium.persistence.PersistenceCallback;
import com.n4systems.fieldid.selenium.persistence.PersistenceTemplate;
import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.fieldid.selenium.persistence.TenantCleaner;
import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.Transaction;
import org.junit.Before;

public abstract class DBTestCase {

    public static final String TEST_ASSET_TYPE_1 = "TestType1";
    public static final String TEST_ASSET_TYPE_2 = "TestType2";

    public static final String[] TEST_TENANT_NAMES = { "test1", "test2" };
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
        PersistenceManager.persistenceUnit = PersistenceManager.TESTING_PERSISTENCE_UNIT;
        PersistenceManager.testProperties.put("hibernate.connection.url", getSeleniumConfig().getDatabaseUrl());
        PersistenceManager.testProperties.put("hibernate.connection.username", getSeleniumConfig().getDatabaseUser());
        PersistenceManager.testProperties.put("hibernate.connection.password", getSeleniumConfig().getDatabasePassword());

        new PersistenceTemplate(new PersistenceCallback() {
            @Override
            public void doInTransaction(Transaction transaction) throws Exception {
                TenantCleaner cleaner = new TenantCleaner();
                cleaner.cleanTenants(transaction.getEntityManager(), TEST_TENANT_NAMES);
            }
        }).execute();

        new PersistenceTemplate(new PersistenceCallback() {
            @Override
            public void doInTransaction(Transaction transaction) throws Exception {
                for (String tenantName : TEST_TENANT_NAMES) {
                    ThreadLocalUserContext.getInstance().setCurrentUser(null);
                    MinimalTenantDataSetup dataSetup  = new MinimalTenantDataSetup(transaction, tenantName);
                    dataSetup.setupMinimalData();
                    ThreadLocalUserContext.getInstance().setCurrentUser(dataSetup.getCreatedUser());
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
