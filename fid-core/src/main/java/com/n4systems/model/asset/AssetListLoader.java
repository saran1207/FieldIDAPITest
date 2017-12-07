package com.n4systems.model.asset;

import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.*;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by agrabovskis on 2017-11-17.
 */
public class AssetListLoader extends ListLoader<Asset> {

    private AssetType assetType;
    private BaseOrg baseOrg;

    public AssetListLoader(SecurityFilter filter) {
        super(filter);
    }

    public AssetListLoader(SecurityFilter filter, AssetType assetType, BaseOrg baseOrg) {
        super(filter);
        this.assetType = assetType;
        this.baseOrg = baseOrg;
    }

    @Override
    protected List<Asset> load(EntityManager em, SecurityFilter filter) {
        QueryBuilder<Asset> builder = new QueryBuilder<>(Asset.class, filter);
        WhereParameterGroup group = new WhereParameterGroup("restrictsearch");
        if (assetType != null) {
            group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.EQ,
                    "type_id", "type.id", assetType.getId(), null, WhereClause.ChainOp.AND));
        }
        if (baseOrg != null) {
            group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.EQ,
                    "owner_id", "owner.id", baseOrg.getId(), null, WhereClause.ChainOp.AND));
        }
        if (assetType != null || baseOrg != null)
            builder.addWhere(group);
        EntityGraph<Asset> entityGraph = em.createEntityGraph(Asset.class);
        entityGraph.addAttributeNodes("infoOptions");
        entityGraph.addSubgraph("type").addAttributeNodes("infoFields");
        Query query = builder.createQuery(em);
        query.setHint("javax.persistence.loadgraph", entityGraph);
        List<Asset> result = query.getResultList();
        return result;
    }
}
