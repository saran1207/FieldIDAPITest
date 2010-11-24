package com.n4systems.fieldid.selenium;

public class SeleniumConfig {

    private String seleniumServerHost;
    private int seleniumServerPort;
    private String seleniumBrowser;
    private String protocol;
    private String initCompany;
    private String testServerDomain;
    private String testServerContextRoot;
    private String actionDelay;
    private String databaseUrl;
    private String databaseUser;
    private String databasePassword;

    public String getSeleniumServerHost() {
        return seleniumServerHost;
    }

    public void setSeleniumServerHost(String seleniumServerHost) {
        this.seleniumServerHost = seleniumServerHost;
    }

    public int getSeleniumServerPort() {
        return seleniumServerPort;
    }

    public void setSeleniumServerPort(int seleniumServerPort) {
        this.seleniumServerPort = seleniumServerPort;
    }

    public String getSeleniumBrowser() {
        return seleniumBrowser;
    }

    public void setSeleniumBrowser(String seleniumBrowser) {
        this.seleniumBrowser = seleniumBrowser;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getInitCompany() {
        return initCompany;
    }

    public void setInitCompany(String initCompany) {
        this.initCompany = initCompany;
    }

    public String getTestServerDomain() {
        return testServerDomain;
    }

    public void setTestServerDomain(String testServerDomain) {
        this.testServerDomain = testServerDomain;
    }

    public String getTestServerContextRoot() {
        return testServerContextRoot;
    }

    public void setTestServerContextRoot(String testServerContextRoot) {
        this.testServerContextRoot = testServerContextRoot;
    }

    public String getActionDelay() {
        return actionDelay;
    }

    public void setActionDelay(String actionDelay) {
        this.actionDelay = actionDelay;
    }

    public String getDatabaseUrl() {
        return databaseUrl;
    }

    public void setDatabaseUrl(String databaseUrl) {
        this.databaseUrl = databaseUrl;
    }

    public String getDatabaseUser() {
        return databaseUser;
    }

    public void setDatabaseUser(String databaseUser) {
        this.databaseUser = databaseUser;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

    public void setDatabasePassword(String databasePassword) {
        this.databasePassword = databasePassword;
    }
}
