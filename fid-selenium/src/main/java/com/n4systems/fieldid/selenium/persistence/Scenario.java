package com.n4systems.fieldid.selenium.persistence;

import com.n4systems.fieldid.selenium.persistence.builder.SafetyNetworkConnectionBuilder;
import com.n4systems.fieldid.selenium.persistence.builder.SimpleEventBuilder;
import com.n4systems.model.AssetType;
import com.n4systems.model.Tenant;
import com.n4systems.model.api.Saveable;
import com.n4systems.model.assettype.AssetTypeByNameLoader;
import com.n4systems.model.builders.AbstractEntityBuilder;
import com.n4systems.model.builders.AssetBuilder;
import com.n4systems.model.builders.AssetTypeBuilder;
import com.n4systems.model.builders.BaseBuilder;
import com.n4systems.model.builders.EntityWithTenantBuilder;
import com.n4systems.model.builders.EventBookBuilder;
import com.n4systems.model.builders.EventBuilder;
import com.n4systems.model.builders.EventGroupBuilder;
import com.n4systems.model.builders.EventTypeBuilder;
import com.n4systems.model.builders.EventTypeGroupBuilder;
import com.n4systems.model.builders.UserBuilder;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.orgs.PrimaryOrgByTenantLoader;
import com.n4systems.model.orgs.customer.CustomerOrgListLoader;
import com.n4systems.model.safetynetwork.OrgConnectionSaver;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.tenant.TenantByNameLoader;
import com.n4systems.model.user.User;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.savers.Saver;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class Scenario {

    private Transaction trans;

    private Tenant defaultTenant;
    private PrimaryOrg defaultPrimaryOrg;
    private User defaultUser;
    private int nextUserId = 0;

    private List<Object> builtObjects = new ArrayList<Object>();

    public Scenario(Transaction trans) {
        this.trans = trans;

        defaultTenant = tenant("test1");
        defaultPrimaryOrg = primaryOrgFor("test1");
        defaultUser = aUser().build();
    }

    public Tenant tenant(String tenantName) {
        TenantByNameLoader byNameLoader = new TenantByNameLoader();
        byNameLoader.setTenantName(tenantName);
        return byNameLoader.load(trans);
    }

    public PrimaryOrg primaryOrgFor(String tenantName) {
        Tenant tenant = tenant(tenantName);
        return new PrimaryOrgByTenantLoader().setTenantId(tenant.getId()).load(trans);
    }

    public AssetType assetType(String tenantName, String assetTypeName) {
        AssetTypeByNameLoader byNameLoader = new AssetTypeByNameLoader(new TenantOnlySecurityFilter(tenant(tenantName)));
        return byNameLoader.setName(assetTypeName).load(trans);
    }

    public BaseOrg customerOrg(String tenantName, String customer) {
        TenantOnlySecurityFilter filter = new TenantOnlySecurityFilter(tenant(tenantName));
        CustomerOrgListLoader loader = new CustomerOrgListLoader(filter);

        PrimaryOrg customersPrimaryOrg = primaryOrgFor(customer);
        trans.getEntityManager().flush();

        List<CustomerOrg> orgs = loader.load(trans);
        for (CustomerOrg org : orgs) {
            if (org.getLinkedOrg().getId().equals(customersPrimaryOrg.getId())) {
                return org;
            }
        }

        throw new RuntimeException("Couldn't find customer org!");
    }

    public SafetyNetworkConnectionBuilder aSafetyNetworkConnection() {
        SafetyNetworkConnectionBuilder builder = SafetyNetworkConnectionBuilder.aSafetyNetworkConnection();
        return createPersistentBuilder(builder);
    }

    public UserBuilder aUser() {
        UserBuilder builder = UserBuilder.aUser();
        builder = builder.withOwner(defaultPrimaryOrg);
        builder = builder.withUserId("test_user"+(++nextUserId));
        return createPersistentBuilder(builder);
    }

    public AssetBuilder anAsset() {
        AssetBuilder builder = AssetBuilder.anAsset();
        return createPersistentBuilder(builder);
    }

    public AssetTypeBuilder anAssetType() {
        AssetTypeBuilder builder = AssetTypeBuilder.anAssetType();
        return createPersistentBuilder(builder);
    }

    public EventBookBuilder anEventBook() {
        EventBookBuilder builder = EventBookBuilder.anEventBook();
        return createPersistentBuilder(builder);
    }

    public EventBuilder anEvent() {
        EventBuilder builder = EventBuilder.anEvent(anEventType(), anEventGroup());
        return createPersistentBuilder(builder);
    }

    public EventGroupBuilder anEventGroup() {
        EventGroupBuilder builder = EventGroupBuilder.anEventGroup();
        return createPersistentBuilder(builder);
    }

    public EventTypeBuilder anEventType() {
        EventTypeBuilder builder = EventTypeBuilder.anEventType(anEventTypeGroup());
        return createPersistentBuilder(builder);
    }

    public SimpleEventBuilder aSimpleEvent() {
        SimpleEventBuilder builder = SimpleEventBuilder.aSimpleEvent(this);
        return createPersistentBuilder(builder);
    }

    public EventTypeGroupBuilder anEventTypeGroup() {
        EventTypeGroupBuilder builder = EventTypeGroupBuilder.anEventTypeGroup();
        return createPersistentBuilder(builder);
    }

    public void save(Saveable entity) {
        System.out.println("Save: " + ToStringBuilder.reflectionToString(entity));
        SaverMap.makeSaverFor(entity.getClass()).save(trans, entity);
    }

    private <T extends BaseBuilder> T createPersistentBuilder(T builder) {
        builder.setAlwaysUseNullId(true);
        builder.setBuilderCallback(new ScenarioBuilderCallback(this));
        if (builder instanceof AbstractEntityBuilder) {
            AbstractEntityBuilder entBuilder = (AbstractEntityBuilder) builder;
            entBuilder.modifiedBy(aUser().build());
        }
        if (builder instanceof EntityWithTenantBuilder) {
            EntityWithTenantBuilder withTenantBuilder = (EntityWithTenantBuilder) builder;
            withTenantBuilder.withTenant(defaultTenant);
        }
        return builder;
    }

    public void addBuiltObject(Object o) {
        builtObjects.add(o);
    }

    public void persistAllBuiltObjects() {
        for (Object o : builtObjects) {
            if (!(o instanceof Saveable)) {
                throw new RuntimeException("Cannot save non saveable object!");
            }
            trans.getEntityManager().merge(o);
        }
    }

    protected void sortByPersistOrder() {
    }

    public User defaultUser() {
        return defaultUser;
    }

    public Tenant defaultTenant() {
        return defaultTenant;
    }

}
