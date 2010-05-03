package com.n4systems.model.inspectiontype;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.Product;
import com.n4systems.model.ProductType;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class CommonProductTypeDatabaseLoader extends ListLoader<ProductType> implements CommonProductTypeLoader {


	private List<Long> assetIds;

	public CommonProductTypeDatabaseLoader(SecurityFilter filter) {
		super(filter);
	}
	
	@Override
	protected List<ProductType> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<ProductType> builder = getProductTypeQueryBuilder(filter);
		builder.addWhere(Comparator.IN, "assetIds", "id", assetIds);
		
		builder.addPostFetchPaths("inspectionTypes");
		
		builder.setSimpleSelect("type", true);
		return builder.getResultList(em);
	}
	
	protected QueryBuilder<ProductType> getProductTypeQueryBuilder(SecurityFilter filter) {
		return new QueryBuilder<ProductType>(Product.class, filter);
	}

	public CommonProductTypeLoader forAssets(List<Long> assetIds) {
		this.assetIds = assetIds;
		return this;
	}

	

}
