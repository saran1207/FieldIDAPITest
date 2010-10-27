package com.n4systems.model.safetynetwork;

import java.util.List;

import com.n4systems.model.Asset;
import com.n4systems.model.SubAsset;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.asset.SmartSearchWhereClause;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.tools.Pager;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.SubSelectNotExistsClause;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;

import javax.persistence.EntityManager;

public class UnregisteredAssetQueryHelper {

	private PrimaryOrg customer;
	private PrimaryOrg vendor;
	private QueryBuilder<Asset> builder;
	private boolean preAssignedAssetsOnly = true;

	public UnregisteredAssetQueryHelper(PrimaryOrg vendor, PrimaryOrg customer, boolean preAssignedAssetsOnly) {
		this.customer = customer;
		this.vendor = vendor;
		this.preAssignedAssetsOnly = preAssignedAssetsOnly;
		initializeQuery();
	}

	public void initializeQuery() {

		builder = new QueryBuilder<Asset>(Asset.class, new OpenSecurityFilter());
		builder.setTableAlias("outerAsset");

		if (preAssignedAssetsOnly) {
			builder.addSimpleWhere("owner.customerOrg.linkedOrg", customer);
		}

		builder.addSimpleWhere("published", true);
		builder.addSimpleWhere("tenant", vendor.getTenant());

		QueryBuilder<Asset> subSelect = new QueryBuilder<Asset>(Asset.class, new OpenSecurityFilter());
		WhereParameter<String> whereClause = new WhereParameter<String>(WhereParameter.Comparator.EQ, "outerAsset", "linkedAsset", "IGNORED");
		whereClause.setLiteralParameter(true);

		subSelect.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.EQ, "tenant.id", customer.getTenant().getId()));
		subSelect.addWhere(whereClause);

		SubSelectNotExistsClause subClause = new SubSelectNotExistsClause("linkedAssetSubClause", subSelect);
		builder.addWhere(subClause);

		QueryBuilder<Asset> subSelect1 = new QueryBuilder<Asset>(SubAsset.class, new OpenSecurityFilter());
		WhereParameter<String> whereClause1 = new WhereParameter<String>(WhereParameter.Comparator.EQ, "outerAsset", "asset", "IGNORED");
		whereClause1.setKey("noAssetWhereParameter");
		whereClause1.setLiteralParameter(true);
		subSelect1.addWhere(whereClause1);
		SubSelectNotExistsClause subClause1 = new SubSelectNotExistsClause("subAssetSubClause", subSelect1);
		builder.addWhere(subClause1);

		QueryBuilder<Asset> subSelect2 = new QueryBuilder<Asset>(SubAsset.class, new OpenSecurityFilter());
		WhereParameter<String> whereClause2 = new WhereParameter<String>(WhereParameter.Comparator.EQ, "outerAsset", "masterAsset", "IGNORED");
		whereClause2.setKey("noMasterAssetWhereParameter");
		whereClause2.setLiteralParameter(true);
		subSelect2.addWhere(whereClause2);
		SubSelectNotExistsClause subClause2 = new SubSelectNotExistsClause("subAssetSubClause2", subSelect2);
		builder.addWhere(subClause2);
	}

	public List<Asset> getList(EntityManager em) {
		return builder.getResultList(em);
	}

	public Pager<Asset> getPager(EntityManager em, Integer first, Integer pageSize, String[] postFetchPaths) {
		builder.addPostFetchPaths(postFetchPaths);
		return builder.getPaginatedResults(em, first, pageSize);
	}

	public Long getCount(EntityManager em) {
		return builder.getCount(em);
	}

	public void setSmartSearchParameters(SmartSearchWhereClause smartSearchWhereClause) {
		builder.addWhere(smartSearchWhereClause);
		builder.addOrder("created");
	}
}
