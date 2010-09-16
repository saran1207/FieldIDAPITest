package com.n4systems.model.safetynetwork;

import com.n4systems.model.Product;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.security.OrgOnlySecurityFilter;
import com.n4systems.model.security.SecurityFilter;

import javax.persistence.Query;

public class UnregisteredAssetQueryHelper {

    private PrimaryOrg customer;
    private SecurityFilter filter;

    public UnregisteredAssetQueryHelper(PrimaryOrg vendor, PrimaryOrg customer) {
        this.customer = customer;
        filter = new OrgOnlySecurityFilter(vendor);
    }

    public String createBaseQuery() {

        // A pre assigned product is as-yet unregistered if there are no product entries out there whose linkedProduct
        // property points to it (when registered something preassigned, you get a new Product entry with linkedProduct
        // set to the pre assigned asset.

        // The goal of this query is to determine the assigned assets that haven't been registered for a vendor/customer pair
        // We do this by using the linkedProduct relationship in Product. We use a right outer join to check
        // whether there's any actual product out there with a "linkedProduct" equal to the one that was assigned.

        // The clauses applied to the "right" of the join:
        // lp.owner.customerOrg.linkedOrg = <OWNER>  (applied below, ensures the owner of the product is the customer, ie the one it was preassigned to)
        // lp.tenant.id = <ID>   (comes from security filter, ensures the linkedProduct belongs to the vendor)
        // lp.state = <ACTIVE>   (also from security filter)

        // On the left there will be a product whose linked product is on the right, or null if no such product exists
        // Since we're looking for pre assigned assets that are as yet unregistered, we filter by obj.id (leftside.id) is null

        String query = "FROM " + Product.class.getName() + " obj RIGHT OUTER JOIN obj.linkedProduct lp "
                +   " WHERE lp.owner.customerOrg.linkedOrg = :owner"
                +   " AND lp.published = true"
                +   " AND obj.id IS NULL";

        String securityConditions = filter.produceWhereClause(Product.class, "lp");

        return query + " AND " + securityConditions;
    }

    public void applyParameters(Query query) {
        query.setParameter("owner", customer);
        filter.applyParameters(query, Product.class);
    }

}
