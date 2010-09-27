package com.n4systems.model.safetynetwork;

import com.n4systems.model.Inspection;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

import javax.persistence.EntityManager;
import java.util.List;

public class InspectionsByProductIdLoader extends ListLoader<Inspection> {

    private long productId;

    public InspectionsByProductIdLoader(SecurityFilter filter) {
        super(filter);
    }

    @Override
    protected List<Inspection> load(EntityManager em, SecurityFilter filter) {
        // Throw an exception if we can't load this product over safety network
        new SafetyNetworkProductLoader(filter).setProductId(productId).load();

        QueryBuilder<Inspection> builder = new QueryBuilder<Inspection>(Inspection.class, new OpenSecurityFilter());
        builder.addWhere(WhereClauseFactory.create("product.id", productId));

        return builder.getResultList(em);
    }

    public InspectionsByProductIdLoader setProductId(long productId) {
        this.productId = productId;
        return this;
    }

}
