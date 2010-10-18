package com.n4systems.fieldid.selenium.persistence;

import com.n4systems.fieldid.selenium.persistence.builder.SafetyNetworkConnectionBuilder;
import com.n4systems.model.ProductType;
import com.n4systems.model.Tenant;
import com.n4systems.model.builders.InspectionBookBuilder;
import com.n4systems.model.builders.ProductBuilder;
import com.n4systems.model.inspectionbook.InspectionBookSaver;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.orgs.PrimaryOrgByTenantLoader;
import com.n4systems.model.orgs.customer.CustomerOrgListLoader;
import com.n4systems.model.product.ProductSaver;
import com.n4systems.model.producttype.ProductTypeByNameLoader;
import com.n4systems.model.safetynetwork.OrgConnectionSaver;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.tenant.TenantByNameLoader;
import com.n4systems.persistence.Transaction;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;

import java.util.List;

public class Scenario {

    private Transaction trans;

    public Scenario(Transaction trans) {
        this.trans = trans;
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

    public ProductType productType(String tenantName, String productTypeName) {
        ProductTypeByNameLoader byNameLoader = new ProductTypeByNameLoader(new TenantOnlySecurityFilter(tenant(tenantName)));
        return byNameLoader.setName(productTypeName).load(trans);
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
        builder.setSaver(new OrgConnectionSaver(ConfigContext.getCurrentContext().getLong(ConfigEntry.HOUSE_ACCOUNT_PRIMARY_ORG_ID))).setTransaction(trans);
        return builder;
    }

    public ProductBuilder aProduct() {
        ProductBuilder builder = ProductBuilder.aProduct();
        builder.setSaver(new ProductSaver()).setTransaction(trans);
        return builder;
    }

    public InspectionBookBuilder anInspectionBook() {
        InspectionBookBuilder builder = InspectionBookBuilder.anInspectionBook();
        builder.setSaver(new InspectionBookSaver()).setTransaction(trans);
        return builder;
    }

    public void save(Object entity) {
        trans.getEntityManager().persist(entity);
    }

    public Transaction getTrans() {
        return trans;
    }

    public void setTrans(Transaction trans) {
        this.trans = trans;
    }

}
