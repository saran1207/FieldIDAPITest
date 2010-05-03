package com.n4systems.model.inspectiontype;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.n4systems.model.Product;
import com.n4systems.model.ProductType;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.SimpleSelect;
import com.n4systems.util.persistence.TestingQueryBuilder;
import com.n4systems.util.persistence.TestingTransaction;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameter.Comparator;


public class CommonProductTypeLoaderTest {

	
	
	private final class CommonProductTypeLoaderExtension extends CommonProductTypeDatabaseLoader {
		private TestingQueryBuilder<ProductType> queryBuilder;

		private CommonProductTypeLoaderExtension(SecurityFilter filter) {
			super(filter);
		}

		@Override
		protected QueryBuilder<ProductType> getProductTypeQueryBuilder(SecurityFilter filter) {
			queryBuilder  = new TestingQueryBuilder<ProductType>(Product.class); 
			return queryBuilder;
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void should_find_all_product_types_for_the_given_list_of_assets() throws Exception {
		CommonProductTypeLoaderExtension sut = new CommonProductTypeLoaderExtension(null);
		sut.forAssets(ImmutableList.of(1L));
		sut.load(new TestingTransaction());
		
		WhereParameter<?> assetIdWhereParameter = (WhereParameter<?>)sut.queryBuilder.getWhereParameter("assetIds");
		assertThat((List<Long>)(assetIdWhereParameter.getValue()), equalTo((List<Long>)ImmutableList.of(1L)));
		assertThat(assetIdWhereParameter.getComparator(), equalTo(Comparator.IN));
		assertThat(assetIdWhereParameter.getParam(), equalTo("id"));
	}
	
	
	@Test
	public void should_select_a_distinct_product_type_list_from_the_product() throws Exception {
		CommonProductTypeLoaderExtension sut = new CommonProductTypeLoaderExtension(null);
		sut.forAssets(ImmutableList.of(1L));
		sut.load(new TestingTransaction());
		
		SimpleSelect selectArgument = (SimpleSelect)sut.queryBuilder.getSelectArgument();
		assertThat(selectArgument.isDistinct(), is(true));
		assertThat(selectArgument.getParam(), equalTo("type"));
	}
	
	
	@Test
	public void should_post_fetch_associated_inspection_types() throws Exception {
		CommonProductTypeLoaderExtension sut = new CommonProductTypeLoaderExtension(null);
		sut.forAssets(ImmutableList.of(1L));
		sut.load(new TestingTransaction());
		
		List<String> postFetches = sut.queryBuilder.getPostFetchPaths();
		
		assertThat(postFetches, hasItem("inspectionTypes"));
	}
}
