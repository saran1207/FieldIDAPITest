package com.n4systems.fieldid.selenium.persistence;

import com.n4systems.model.AssetType;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.producttype.ProductTypeSaver;
import com.n4systems.model.user.User;
import com.n4systems.persistence.Transaction;
import com.n4systems.security.Permissions;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;

import javax.persistence.EntityManager;

public class MinimalTenantDataSetup {

    private Tenant tenant;
    private EntityManager em;
    private Transaction trans;

    public MinimalTenantDataSetup(Transaction trans, long tenantId) {
        this.em = trans.getEntityManager();
        this.trans = trans;
        tenant = em.find(Tenant.class, tenantId);
    }

    public void setupMinimalData() {
        PrimaryOrg org = createPrimaryOrgForTenant();
        createN4UserAccountForTenant(org);
    }

    private void createN4UserAccountForTenant(PrimaryOrg org) {
		User user = new User();
		user.setTenant(tenant);
		user.setOwner(org);
		user.setTimeZoneID("United States:New York - New York");
		user.setActive(true);
		user.setPermissions(Permissions.SYSTEM);
		user.setSystem(true);
		user.setUserID(ConfigContext.getCurrentContext().getString(ConfigEntry.SYSTEM_USER_USERNAME));
		user.setHashPassword(ConfigContext.getCurrentContext().getString(ConfigEntry.SYSTEM_USER_PASSWORD));
		user.setEmailAddress(ConfigContext.getCurrentContext().getString(ConfigEntry.SYSTEM_USER_ADDRESS));
		user.setFirstName("N4");
		user.setLastName("Admin");
        em.persist(user);
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
        org.getLimits().setAssetsUnlimited();
        org.getLimits().setDiskSpaceUnlimited();
        org.getLimits().setSecondaryOrgsUnlimited();
        org.getLimits().setUsersUnlimited();

        em.persist(org);
        return org;
    }

    public void createTestProductTypes(String[] testProductTypes) {
        for (String typeName : testProductTypes) {
            createSimpleProductType(em, typeName);
        }
    }
    
    private AssetType createSimpleProductType(EntityManager em, String name) {
        AssetType type = new AssetType();
        type.setTenant(tenant);
        type.setName(name);
        new ProductTypeSaver().save(trans, type);

        return type;
    }

}
