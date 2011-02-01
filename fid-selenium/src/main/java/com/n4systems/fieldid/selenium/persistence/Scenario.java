package com.n4systems.fieldid.selenium.persistence;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.model.Configuration;
import com.n4systems.model.UnitOfMeasure;
import com.n4systems.model.builders.InfoFieldBeanBuilder;
import com.n4systems.model.builders.OneClickCriteriaBuilder;
import com.n4systems.model.builders.CriteriaSectionBuilder;
import com.n4systems.model.builders.EventFormBuilder;
import com.n4systems.util.ConfigEntry;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.n4systems.fieldid.selenium.persistence.builder.SafetyNetworkConnectionBuilder;
import com.n4systems.fieldid.selenium.persistence.builder.SimpleEventBuilder;
import com.n4systems.model.AssetType;
import com.n4systems.model.Tenant;
import com.n4systems.model.api.Saveable;
import com.n4systems.model.assettype.AssetTypeByNameLoader;
import com.n4systems.model.builders.AbstractEntityBuilder;
import com.n4systems.model.builders.AssetBuilder;
import com.n4systems.model.builders.AssetStatusBuilder;
import com.n4systems.model.builders.AssetTypeBuilder;
import com.n4systems.model.builders.BaseBuilder;
import com.n4systems.model.builders.EntityWithOwnerBuilder;
import com.n4systems.model.builders.EntityWithTenantBuilder;
import com.n4systems.model.builders.EventBookBuilder;
import com.n4systems.model.builders.EventBuilder;
import com.n4systems.model.builders.EventGroupBuilder;
import com.n4systems.model.builders.EventTypeBuilder;
import com.n4systems.model.builders.EventTypeGroupBuilder;
import com.n4systems.model.builders.InfoFieldBuilder;
import com.n4systems.model.builders.InfoOptionBeanBuilder;
import com.n4systems.model.builders.OrgBuilder;
import com.n4systems.model.builders.UserBuilder;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.orgs.PrimaryOrgByTenantLoader;
import com.n4systems.model.orgs.customer.CustomerOrgListLoader;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.tenant.TenantByNameLoader;
import com.n4systems.model.user.User;
import com.n4systems.persistence.Transaction;
import rfid.ejb.entity.InfoFieldBean;

import javax.persistence.Query;

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
    
    public void updatePrimaryOrg(PrimaryOrg primaryOrg) {
    	save(primaryOrg);
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
    
    public UserBuilder aLiteUser() {
        UserBuilder builder = UserBuilder.anLiteUser();
        builder = builder.withOwner(defaultPrimaryOrg);
        builder = builder.withUserId("test_user"+(++nextUserId));
        return createPersistentBuilder(builder);
    }
    
    public UserBuilder aReadOnlyUser() {
        UserBuilder builder = UserBuilder.aReadOnlyUser();
        builder = builder.withOwner(defaultPrimaryOrg);
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

    public AssetStatusBuilder anAssetStatus() {
        AssetStatusBuilder builder = AssetStatusBuilder.anAssetStatus();
        return createPersistentBuilder(builder);
    }

    public InfoFieldBuilder anInfoField() {
        InfoFieldBuilder builder = InfoFieldBuilder.anInfoField();
        return createPersistentBuilder(builder);
    }

    public InfoOptionBeanBuilder anInfoOption() {
        InfoOptionBeanBuilder builder = InfoOptionBeanBuilder.aStaticInfoOption();
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

    public EventFormBuilder anEventForm() {
        EventFormBuilder builder = EventFormBuilder.anEventForm();
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

    public OrgBuilder aCustomerOrg() {
        OrgBuilder builder = OrgBuilder.aCustomerOrg();
        return createPersistentBuilder(builder);
    }

    public CriteriaSectionBuilder aCriteriaSection() {
        CriteriaSectionBuilder builder = CriteriaSectionBuilder.aCriteriaSection();
        return createPersistentBuilder(builder);
    }

    public OneClickCriteriaBuilder aCriteria() {
        OneClickCriteriaBuilder builder = OneClickCriteriaBuilder.aCriteria();
        return createPersistentBuilder(builder);
    }

    public void save(Object entity) {
        System.out.println("Save: " + ToStringBuilder.reflectionToString(entity));
        if (entity instanceof Saveable) {
            Saveable saveable = (Saveable) entity;
            SaverMap.makeSaverFor(saveable.getClass()).save(trans, saveable);
        }
    }

    private <T extends BaseBuilder> T createPersistentBuilder(T builder) {
        builder.setAlwaysUseNullId(true);
        builder.setBuilderCallback(new ScenarioBuilderCallback(this));
        return builder;
    }

    public void addBuiltObject(Object o) {
        builtObjects.add(o);
    }

    public void persistAllBuiltObjects() {
        for (Object o : builtObjects) {
            System.out.println("Final persist on: " + ToStringBuilder.reflectionToString(o));
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

    public BaseOrg defaultPrimaryOrg() {
        return defaultPrimaryOrg;
    }

    public void onBeforeBuild(BaseBuilder builder) {
        if (builder instanceof AbstractEntityBuilder) {
            AbstractEntityBuilder entBuilder = (AbstractEntityBuilder) builder;
            entBuilder.modifiedBy(createAppropriateUser(entBuilder));
        }
        if (builder instanceof EntityWithTenantBuilder) {
            EntityWithTenantBuilder withTenantBuilder = (EntityWithTenantBuilder) builder;
            if (withTenantBuilder.getTenant() == null) {
                withTenantBuilder.setTenant(defaultTenant);
            }
        }
        if (builder instanceof EntityWithOwnerBuilder) {
            EntityWithOwnerBuilder withOwnerBuilder = (EntityWithOwnerBuilder) builder;
            if (withOwnerBuilder.getOwner() == null) {
                withOwnerBuilder.setOwner(defaultPrimaryOrg);
            }
            withOwnerBuilder.setTenant(withOwnerBuilder.getOwner().getTenant());
        }
        if (builder instanceof AssetStatusBuilder) {
            AssetStatusBuilder statusBuilder = (AssetStatusBuilder) builder;
            if (statusBuilder.getTenant() == null) {
                statusBuilder.setTenant(defaultTenant);
            }
        }
    }

    private User createAppropriateUser(AbstractEntityBuilder entBuilder) {
        Tenant tenant = defaultTenant;
        if (entBuilder instanceof EntityWithTenantBuilder) {
            Tenant setTenant = ((EntityWithTenantBuilder) entBuilder).getTenant();
            if (setTenant != null) {
                tenant = setTenant;
            }
        }
        BaseOrg owner = new PrimaryOrgByTenantLoader().setTenantId(tenant.getId()).load(trans);
        return aUser().withOwner(owner).build();
    }

    public UnitOfMeasure unitOfMeasure(String uomName) {
        Query query = trans.getEntityManager().createQuery("from " + UnitOfMeasure.class.getName() + " where name = :name");
        query.setParameter("name", uomName);
        return (UnitOfMeasure) query.getSingleResult();
    }

    public void updateConfigurationForTenant(String tenantName, ConfigEntry entry, String newValue) {
        Tenant tenant = tenant(tenantName);
        Query query = trans.getEntityManager().createQuery("from " + Configuration.class.getName() + " where tenantId = :tenantId and identifier = :identifier");

        query.setParameter("tenantId", tenant.getId());
        query.setParameter("identifier", entry);

        List<Configuration> configs = query.getResultList();
        if (!configs.isEmpty()) {
            configs.get(0).setValue(newValue);
            trans.getEntityManager().merge(configs.get(0));
        } else {
            Configuration config = new Configuration(entry, newValue, tenant.getId());
            trans.getEntityManager().persist(config);
        }
    }

}
