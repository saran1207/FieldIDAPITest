package com.n4systems.handlers;

import static org.easymock.EasyMock.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.n4systems.model.InspectionType;
import com.n4systems.model.ProductType;
import com.n4systems.model.inspectiontype.CommonProductTypeLoader;
import com.n4systems.persistence.Transaction;


public class CommonInspectionTypeHandlerTest {

	
	private final class CommonProductTypeIdLoaderTestDouble implements CommonProductTypeLoader {
		@Override
		public List<ProductType> load(Transaction transaction) {
			return null;
		}

		@Override
		public List<ProductType> load() {
			return null;
		}

		@Override
		public CommonProductTypeLoader forAssets(List<Long> assetIds) {
			return this;
		}
	}

	@Test
	public void should_load_product_types_from_the_product_type_id_looder() throws Exception {
		ImmutableList<Long> assetIds = ImmutableList.of(1L);
		
		CommonProductTypeLoader productTypeIdLoader = createMock(CommonProductTypeLoader.class);
		expect(productTypeIdLoader.forAssets(assetIds)).andReturn(productTypeIdLoader);
		expect(productTypeIdLoader.load()).andReturn(new ArrayList<ProductType>());
		replay(productTypeIdLoader);
	
		
		CommonInspectionTypeHandler sut = new CommonInspectionTypeHandler(productTypeIdLoader);
		
		sut.findCommonInspectionTypesFor(assetIds);
		
		verify(productTypeIdLoader);
	}
	
	
	@Test
	public void should_always_return_an_empty_list_of_inspection_types_when_given_an_empty_list_of_assets_ids() throws Exception {
		
		CommonInspectionTypeHandler sut = new CommonInspectionTypeHandler(null);
		
		List<InspectionType> inspectionTypes = sut.findCommonInspectionTypesFor(new ArrayList<Long>());
		
		List<InspectionType> emptyList = new ArrayList<InspectionType>();
		assertThat(inspectionTypes, equalTo(emptyList));
		
	}
	
	@Test(expected=NullPointerException.class)
	public void should_throw_exception_when_a_null_list_of_ids_is_given() throws Exception {
		CommonInspectionTypeHandler sut = new CommonInspectionTypeHandler(new CommonProductTypeIdLoaderTestDouble());
		
		sut.findCommonInspectionTypesFor(null);
	}
	
	
	@Test
	public void should_return_an_empty_list_of_inspection_types_when_there_is_one_product_type_with_no_associated_inspection_types() throws Exception {
		
	}
	
	
	
	
}
