package com.n4systems.model.safetynetwork;

import java.util.List;
import com.n4systems.model.Product;
import com.n4systems.model.SubProduct;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.product.SmartSearchWhereClause;
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
	private QueryBuilder<Product> builder;
	private boolean preAssignedAssetsOnly = true;

	public UnregisteredAssetQueryHelper(PrimaryOrg vendor, PrimaryOrg customer, boolean preAssignedAssetsOnly) {
		this.customer = customer;
		this.vendor = vendor;
		this.preAssignedAssetsOnly = preAssignedAssetsOnly;
		initializeQuery();
	}

	public void initializeQuery() {

		builder = new QueryBuilder<Product>(Product.class, new OpenSecurityFilter());
		builder.setTableAlias("outerProduct");

		if (preAssignedAssetsOnly) {
			builder.addSimpleWhere("owner.customerOrg.linkedOrg", customer);
		}

		builder.addSimpleWhere("published", true);
		builder.addSimpleWhere("tenant", vendor.getTenant());

		QueryBuilder<Product> subSelect = new QueryBuilder<Product>(Product.class, new OpenSecurityFilter());
		WhereParameter<String> whereClause = new WhereParameter<String>(WhereParameter.Comparator.EQ, "outerProduct", "linkedProduct", "IGNORED");
		whereClause.setLiteralParameter(true);

		subSelect.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.EQ, "tenant.id", customer.getTenant().getId()));
		subSelect.addWhere(whereClause);

		SubSelectNotExistsClause subClause = new SubSelectNotExistsClause("linkedProductSubClause", subSelect);
		builder.addWhere(subClause);

		QueryBuilder<Product> subSelect1 = new QueryBuilder<Product>(SubProduct.class, new OpenSecurityFilter());
		WhereParameter<String> whereClause1 = new WhereParameter<String>(WhereParameter.Comparator.EQ, "outerProduct", "product", "IGNORED");
		whereClause1.setKey("noProductWhereParameter");
		whereClause1.setLiteralParameter(true);
		subSelect1.addWhere(whereClause1);
		SubSelectNotExistsClause subClause1 = new SubSelectNotExistsClause("subProductSubClause", subSelect1);
		builder.addWhere(subClause1);

		QueryBuilder<Product> subSelect2 = new QueryBuilder<Product>(SubProduct.class, new OpenSecurityFilter());
		WhereParameter<String> whereClause2 = new WhereParameter<String>(WhereParameter.Comparator.EQ, "outerProduct", "masterProduct", "IGNORED");
		whereClause2.setKey("noMasterProductWhereParameter");
		whereClause2.setLiteralParameter(true);
		subSelect2.addWhere(whereClause2);
		SubSelectNotExistsClause subClause2 = new SubSelectNotExistsClause("subProductSubClause2", subSelect2);
		builder.addWhere(subClause2);
	}

	public List<Product> getList(EntityManager em) {
		return builder.getResultList(em);
	}

	public Pager<Product> getPager(EntityManager em, Integer first, Integer pageSize, String[] postFetchPaths) {
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
