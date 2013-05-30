package com.n4systems.fieldid.selenium.persistence;

import com.n4systems.model.*;
import com.n4systems.model.assettype.AssetTypeSaver;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.user.User;
import com.n4systems.persistence.Transaction;
import com.n4systems.security.Permissions;
import com.n4systems.security.UserType;
import rfid.ejb.entity.IdentifierCounter;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Date;

public class MinimalTenantDataSetup {

    private Tenant tenant;
    private EntityManager em;
    private Transaction trans;

    private User createdUser;

    public MinimalTenantDataSetup(Transaction trans, String tenantName) {
        this.em = trans.getEntityManager();
        this.trans = trans;
        Query query = em.createQuery("from " + Tenant.class.getName() + " where name = '" + tenantName + "'");
        this.tenant = (Tenant) query.getSingleResult();
    }

    public void setupMinimalData() {
        PrimaryOrg org = createPrimaryOrgForTenant();
        createN4UserAccountForTenant(org);
        createPassFailButtonGroup();
        createIdentifierFormat();
    }

    private void createIdentifierFormat() {
        IdentifierCounter identifierCounter = new IdentifierCounter();
        identifierCounter.setTenant(tenant);
        identifierCounter.setDecimalFormat("000000");
        identifierCounter.setCounter(1L);
        identifierCounter.setDaysToReset(366L);
        identifierCounter.setLastReset(new Date());
        em.persist(identifierCounter);
    }

    private void createPassFailButtonGroup() {
        ButtonGroup passFailButtonGroup = new ButtonGroup();

        Button failButton = new Button();
        failButton.setButtonName("btn1");
        failButton.setDisplayText("Fail");
        failButton.setEventResult(EventResult.FAIL);
        failButton.setTenant(tenant);

        Button passButton = new Button();
        passButton.setButtonName("btn0");
        passButton.setDisplayText("Pass");
        passButton.setEventResult(EventResult.PASS);
        passButton.setTenant(tenant);

        passFailButtonGroup.setName("Pass, Fail");
        passFailButtonGroup.getButtons().add(passButton);
        passFailButtonGroup.getButtons().add(failButton);

        passFailButtonGroup.setTenant(tenant);

        em.persist(passFailButtonGroup);
        em.persist(failButton);
        em.persist(passButton);
    }

    private void createN4UserAccountForTenant(PrimaryOrg org) {
		User user = new User();
		user.setTenant(tenant);
		user.setOwner(org);
		user.setTimeZoneID("United States:New York - New York");
		user.setRegistered(true);
		user.setPermissions(Permissions.SYSTEM);
		user.setUserType(UserType.SYSTEM);
		user.setUserID("n4systems");
		user.setHashPassword("223a7acbb5cc50fb97a38ff6043d3580ff672ae0"); //R2d2>C3p0
		user.setEmailAddress("at@dot.com");
		user.setFirstName("N4");
		user.setLastName("Admin");
        em.persist(user);
        createdUser = user;
    }

    private PrimaryOrg createPrimaryOrgForTenant() {
        PrimaryOrg org = new PrimaryOrg();
        org.setTenant(tenant);
        org.setName(tenant.getName());
        org.setAutoAcceptConnections(false);
        org.setAutoPublish(false);
        org.setSearchableOnSafetyNetwork(true);
        org.setPlansAndPricingAvailable(true);
        org.setDefaultTimeZone("United States:New York - New York");
        org.setModified(new Date());
        org.setIdentifierLabel("Serial Number");

        em.persist(org);
        return org;
    }

    public void createTestAssetTypes(String[] testAssetTypes) {
        for (String typeName : testAssetTypes) {
            createSimpleAssetType(typeName);
        }
    }
    
    private AssetType createSimpleAssetType(String name) {
        AssetType type = new AssetType();
        type.setTenant(tenant);
        type.setName(name);
        new AssetTypeSaver().save(trans, type);

        return type;
    }

    public User getCreatedUser() {
        return createdUser;
    }
}
