package com.n4systems.handlers;

import static org.easymock.EasyMock.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.n4systems.model.InspectionType;
import com.n4systems.model.ProductType;
import com.n4systems.model.builders.InspectionTypeBuilder;
import com.n4systems.model.builders.ProductTypeBuilder;
import com.n4systems.model.inspectiontype.CommonProductTypeLoader;
import com.n4systems.persistence.Transaction;

public class CommonInspectionTypeHandlerTest {

	private static final Set<InspectionType> EMPTY_INSPECTION_TYPE_SET = new HashSet<InspectionType>();

	private class CommonProductTypeIdLoaderTestDouble implements CommonProductTypeLoader {
		List<ProductType> productTypes;
		
		public CommonProductTypeIdLoaderTestDouble() {
			this(new ArrayList<ProductType>());
		}
		public CommonProductTypeIdLoaderTestDouble(List<ProductType> productTypes) {
			super();
			this.productTypes = productTypes;
		}

		@Override
		public List<ProductType> load(Transaction transaction) {
			return productTypes;
		}

		@Override
		public List<ProductType> load() {
			return productTypes;
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

		CommonInspectionTypeHandler sut = new LoaderBackedCommonInspectionTypeHandler(productTypeIdLoader);

		sut.findCommonInspectionTypesFor(assetIds);

		verify(productTypeIdLoader);
	}

	@Test
	public void should_always_return_an_empty_list_of_inspection_types_when_given_an_empty_list_of_assets_ids() throws Exception {

		CommonInspectionTypeHandler sut = new LoaderBackedCommonInspectionTypeHandler(null);

		Set<InspectionType> inspectionTypes = sut.findCommonInspectionTypesFor(new ArrayList<Long>());

		assertThat(inspectionTypes, equalTo(EMPTY_INSPECTION_TYPE_SET));

	}

	@Test(expected = NullPointerException.class)
	public void should_throw_exception_when_a_null_list_of_ids_is_given() throws Exception {
		CommonInspectionTypeHandler sut = new LoaderBackedCommonInspectionTypeHandler(new CommonProductTypeIdLoaderTestDouble());

		sut.findCommonInspectionTypesFor(null);
	}

	@Test
	public void should_return_an_empty_list_of_inspection_types_when_there_is_one_product_type_with_no_associated_inspection_types() throws Exception {

		ProductType productType = ProductTypeBuilder.aProductType().build();

		CommonInspectionTypeHandler sut = new LoaderBackedCommonInspectionTypeHandler(new CommonProductTypeIdLoaderTestDouble(ImmutableList.of(productType)));
		
		Set<InspectionType> returnSet = sut.findCommonInspectionTypesFor(ImmutableList.of(1L));

		assertThat(returnSet, equalTo(EMPTY_INSPECTION_TYPE_SET));

	}

	@Test
	public void should_return_entire_inspection_type_list_given_one_product_type_with_many_inspection_types() {

		InspectionType inspectionType = InspectionTypeBuilder.anInspectionType().build();
		InspectionType inspectionType2 = InspectionTypeBuilder.anInspectionType().build();
		ProductType productType = ProductTypeBuilder.aProductType().withInspectionTypes(inspectionType, inspectionType2).build();

		CommonInspectionTypeHandler sut = new LoaderBackedCommonInspectionTypeHandler(new CommonProductTypeIdLoaderTestDouble(ImmutableList.of(productType)));
		Set<InspectionType> returnSet = sut.findCommonInspectionTypesFor(ImmutableList.of(1L));

		Set<InspectionType> expectedInspectionList = ImmutableSet.of(inspectionType, inspectionType2);
		assertThat(returnSet, equalTo(expectedInspectionList));

	}

	@Test
	public void should_return_common_inspection_types_of_two_or_more_product_types() {

		InspectionType inspectionType = InspectionTypeBuilder.anInspectionType().build();
		InspectionType inspectionType2 = InspectionTypeBuilder.anInspectionType().build();
		ProductType productType = ProductTypeBuilder.aProductType().withInspectionTypes(inspectionType, inspectionType2).build();
		ProductType productType2 = ProductTypeBuilder.aProductType().withInspectionTypes(inspectionType).build();

		CommonInspectionTypeHandler sut = new LoaderBackedCommonInspectionTypeHandler(new CommonProductTypeIdLoaderTestDouble(ImmutableList.of(productType, productType2)));
		Set<InspectionType> returnSet = sut.findCommonInspectionTypesFor(ImmutableList.of(1L));

		Set<InspectionType> expectedInspectionList = ImmutableSet.of(inspectionType);
		assertThat(returnSet, equalTo(expectedInspectionList));

	}

	@Test
	public void should_return_empty_set_from_multiple_product_types_with_no_common_inspections() {

		InspectionType inspectionType = InspectionTypeBuilder.anInspectionType().build();
		InspectionType inspectionType2 = InspectionTypeBuilder.anInspectionType().build();
		ProductType productType = ProductTypeBuilder.aProductType().withInspectionTypes(inspectionType).build();
		ProductType productType2 = ProductTypeBuilder.aProductType().withInspectionTypes(inspectionType2).build();

		CommonInspectionTypeHandler sut = new LoaderBackedCommonInspectionTypeHandler(new CommonProductTypeIdLoaderTestDouble(ImmutableList.of(productType, productType2)));
		Set<InspectionType> returnSet = sut.findCommonInspectionTypesFor(ImmutableList.of(1L));

		assertThat(returnSet, equalTo(EMPTY_INSPECTION_TYPE_SET));

	}

	@Test
	public void should_return_empty_set_given_no_product_types() {
		CommonInspectionTypeHandler sut = new LoaderBackedCommonInspectionTypeHandler(new CommonProductTypeIdLoaderTestDouble(new ArrayList<ProductType>()));
		
		Set<InspectionType> returnSet = sut.findCommonInspectionTypesFor(ImmutableList.of(1L));

		assertThat(returnSet, equalTo(EMPTY_INSPECTION_TYPE_SET));

	}

}
