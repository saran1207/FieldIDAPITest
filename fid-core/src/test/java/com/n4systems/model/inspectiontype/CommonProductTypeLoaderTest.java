package com.n4systems.model.inspectiontype;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.n4systems.exceptions.IdListTooBigException;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigContextOverridableTestDouble;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.SimpleSelect;
import com.n4systems.util.persistence.TestingQueryBuilder;
import com.n4systems.util.persistence.TestingTransaction;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameter.Comparator;


public class CommonProductTypeLoaderTest {

	
	
	private final class CommonProductTypeLoaderExtension extends CommonProductTypeDatabaseLoader {
		private TestingQueryBuilder<AssetType> queryBuilder;

		
		private CommonProductTypeLoaderExtension(SecurityFilter filter, ConfigContext configContext) {
			super(filter, configContext);
		}

		@Override
		protected QueryBuilder<AssetType> getProductTypeQueryBuilder(SecurityFilter filter) {
			queryBuilder  = new TestingQueryBuilder<AssetType>(Asset.class);
			queryBuilder.setListResults(new ArrayList<AssetType>());
			return queryBuilder;
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void should_find_all_product_types_for_the_given_list_of_assets() throws Exception {
		CommonProductTypeLoaderExtension sut = new CommonProductTypeLoaderExtension(null,  new ConfigContextOverridableTestDouble());
		sut.forAssets(ImmutableList.of(1L));
		sut.load(new TestingTransaction());
		
		WhereParameter<?> assetIdWhereParameter = (WhereParameter<?>)sut.queryBuilder.getWhereParameter("assetIds");
		assertThat((List<Long>)(assetIdWhereParameter.getValue()), equalTo((List<Long>)ImmutableList.of(1L)));
		assertThat(assetIdWhereParameter.getComparator(), equalTo(Comparator.IN));
		assertThat(assetIdWhereParameter.getParam(), equalTo("id"));
	}
	
	
	@Test
	public void should_select_a_distinct_product_type_list_from_the_product() throws Exception {
		CommonProductTypeLoaderExtension sut = new CommonProductTypeLoaderExtension(null,  new ConfigContextOverridableTestDouble());
		sut.forAssets(ImmutableList.of(1L));
		sut.load(new TestingTransaction());
		
		SimpleSelect selectArgument = (SimpleSelect)sut.queryBuilder.getSelectArgument();
		assertThat(selectArgument.isDistinct(), is(true));
		assertThat(selectArgument.getParam(), equalTo("type"));
	}
	
	
	@Test
	public void should_post_fetch_associated_inspection_types() throws Exception {
		CommonProductTypeLoaderExtension sut = new CommonProductTypeLoaderExtension(null,  new ConfigContextOverridableTestDouble());
		sut.forAssets(ImmutableList.of(1L));
		sut.load(new TestingTransaction());
		
		List<String> postFetches = sut.queryBuilder.getPostFetchPaths();
		
		assertThat(postFetches, hasItem("inspectionTypes"));
	}
	
	
	@Test(expected=IdListTooBigException.class)
	public void should_throw_id_set_too_big_exception_if_the_asset_id_list_exceeds_the_configuration_setting() throws Exception {
		ConfigContextOverridableTestDouble configContext = new ConfigContextOverridableTestDouble();
		configContext.addConfigurationValue(ConfigEntry.MAX_SIZE_FOR_MULTI_INSPECT, "5");
		
		CommonProductTypeLoaderExtension sut = new CommonProductTypeLoaderExtension(null, configContext);
		sut.forAssets(ImmutableList.of(1L, 2L, 3L, 4L, 5L, 6L));
		sut.load(new TestingTransaction());
	}
	
	@Test
	public void should_allow_a_list_asset_ids_that_is_equal_to_the_configuration_setting() throws Exception {
		ConfigContextOverridableTestDouble configContext = new ConfigContextOverridableTestDouble();
		configContext.addConfigurationValue(ConfigEntry.MAX_SIZE_FOR_MULTI_INSPECT, "5");
		
		CommonProductTypeLoaderExtension sut = new CommonProductTypeLoaderExtension(null, configContext);
		sut.forAssets(ImmutableList.of(1L, 2L, 3L, 4L, 5L));
		assertThat(sut.load(new TestingTransaction()), is(not(nullValue())));
		
		
	}
}
