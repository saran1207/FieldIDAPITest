package com.n4systems.model.safetynetwork;

import com.n4systems.model.Event;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

import javax.persistence.EntityManager;
import java.util.List;

public class InspectionsByAssetIdLoader extends ListLoader<Event> {

    private long assetId;

    public InspectionsByAssetIdLoader(SecurityFilter filter) {
        super(filter);
    }

    @Override
    protected List<Event> load(EntityManager em, SecurityFilter filter) {
        // Throw an exception if we can't load this asset over safety network
        new SafetyNetworkAssetLoader(filter).setAssetId(assetId).load();

        QueryBuilder<Event> builder = new QueryBuilder<Event>(Event.class, new OpenSecurityFilter());
        builder.addWhere(WhereClauseFactory.create("asset.id", assetId));

        return builder.getResultList(em);
    }

    public InspectionsByAssetIdLoader setAssetId(long assetId) {
        this.assetId = assetId;
        return this;
    }

}
