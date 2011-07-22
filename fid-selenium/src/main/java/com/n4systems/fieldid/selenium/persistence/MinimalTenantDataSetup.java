package com.n4systems.fieldid.selenium.persistence;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import rfid.ejb.entity.SerialNumberCounterBean;

import com.n4systems.model.AssetType;
import com.n4systems.model.State;
import com.n4systems.model.StateSet;
import com.n4systems.model.Status;
import com.n4systems.model.Tenant;
import com.n4systems.model.assettype.AssetTypeSaver;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.user.User;
import com.n4systems.persistence.Transaction;
import com.n4systems.security.Permissions;
import com.n4systems.security.UserType;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;

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
        createSerialNumberFormat();
    }

    private void createSerialNumberFormat() {
        SerialNumberCounterBean serialNumberCounterBean = new SerialNumberCounterBean();
        serialNumberCounterBean.setTenant(tenant);
        serialNumberCounterBean.setDecimalFormat("000000");
        serialNumberCounterBean.setCounter(1L);
        serialNumberCounterBean.setDaysToReset(366L);
        serialNumberCounterBean.setLastReset(new Date());
        em.persist(serialNumberCounterBean);
    }

    private void createPassFailButtonGroup() {
        StateSet passFailButtonGroup = new StateSet();

        State failState = new State();
        failState.setButtonName("btn1");
        failState.setDisplayText("Fail");
        failState.setStatus(Status.FAIL);
        failState.setTenant(tenant);

        State passState = new State();
        passState.setButtonName("btn0");
        passState.setDisplayText("Pass");
        passState.setStatus(Status.PASS);
        passState.setTenant(tenant);

        passFailButtonGroup.setName("Pass, Fail");
        passFailButtonGroup.getStates().add(passState);
        passFailButtonGroup.getStates().add(failState);

        passFailButtonGroup.setTenant(tenant);

        em.persist(passFailButtonGroup);
        em.persist(failState);
        em.persist(passState);
    }

    private void createN4UserAccountForTenant(PrimaryOrg org) {
		User user = new User();
		user.setTenant(tenant);
		user.setOwner(org);
		user.setTimeZoneID("United States:New York - New York");
		user.setRegistered(true);
		user.setPermissions(Permissions.SYSTEM);
		user.setUserType(UserType.SYSTEM);
		user.setUserID(ConfigContext.getCurrentContext().getString(ConfigEntry.SYSTEM_USER_USERNAME));
		user.setHashPassword(ConfigContext.getCurrentContext().getString(ConfigEntry.SYSTEM_USER_PASSWORD));
		user.setEmailAddress(ConfigContext.getCurrentContext().getString(ConfigEntry.SYSTEM_USER_ADDRESS));
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
