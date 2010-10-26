package com.n4systems.handlers;

import static org.easymock.EasyMock.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.n4systems.model.AssetType;
import com.n4systems.model.builders.AssetTypeBuilder;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.n4systems.model.InspectionType;
import com.n4systems.model.builders.InspectionTypeBuilder;
import com.n4systems.model.inspectiontype.CommonProductTypeLoader;
import com.n4systems.persistence.Transaction;

public class CommonInspectionTypeHandlerTest {

	private static final Set<InspectionType> EMPTY_INSPECTION_TYPE_SET = new HashSet<InspectionType>();

	private class CommonProductTypeIdLoaderTestDouble implements CommonProductTypeLoader {
		List<AssetType> assetTypes;
		
		public CommonProductTypeIdLoaderTestDouble() {
			this(new ArrayList<AssetType>());
		}
		public CommonProductTypeIdLoaderTestDouble(List<AssetType> assetTypes) {
			super();
			this.assetTypes = assetTypes;
		}

		@Override
		public List<AssetType> load(Transaction transaction) {
			return assetTypes;
		}

		@Override
		public List<AssetType> load() {
			return assetTypes;
		}

		@Override
		public CommonProductTypeLoader forAssets(List<Long> assetIds) {
			return this;
		}
	}

	@Test
	public void should_load_asset_types_from_the_asset_type_id_looder() throws Exception {
		ImmutableList<Long> assetIds = ImmutableList.of(1L);

		CommonProductTypeLoader productTypeIdLoader = createMock(CommonProductTypeLoader.class);
		expect(productTypeIdLoader.forAssets(assetIds)).andReturn(productTypeIdLoader);
		expect(productTypeIdLoader.load()).andReturn(new ArrayList<AssetType>());
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
	public void should_return_an_empty_list_of_inspection_types_when_there_is_one_asset_type_with_no_associated_inspection_types() throws Exception {

		AssetType assetType = AssetTypeBuilder.anAssetType().build();

		CommonInspectionTypeHandler sut = new LoaderBackedCommonInspectionTypeHandler(new CommonProductTypeIdLoaderTestDouble(ImmutableList.of(assetType)));
		
		Set<InspectionType> returnSet = sut.findCommonInspectionTypesFor(ImmutableList.of(1L));

		assertThat(returnSet, equalTo(EMPTY_INSPECTION_TYPE_SET));

	}

	@Test
	public void should_return_entire_inspection_type_list_given_one_asset_type_with_many_inspection_types() {

		InspectionType inspectionType = InspectionTypeBuilder.anInspectionType().build();
		InspectionType inspectionType2 = InspectionTypeBuilder.anInspectionType().build();
		AssetType assetType = AssetTypeBuilder.anAssetType().withInspectionTypes(inspectionType, inspectionType2).build();

		CommonInspectionTypeHandler sut = new LoaderBackedCommonInspectionTypeHandler(new CommonProductTypeIdLoaderTestDouble(ImmutableList.of(assetType)));
		Set<InspectionType> returnSet = sut.findCommonInspectionTypesFor(ImmutableList.of(1L));

		Set<InspectionType> expectedInspectionList = ImmutableSet.of(inspectionType, inspectionType2);
		assertThat(returnSet, equalTo(expectedInspectionList));

	}

	@Test
	public void should_return_common_inspection_types_of_two_or_more_asset_types() {

		InspectionType inspectionType = InspectionTypeBuilder.anInspectionType().build();
		InspectionType inspectionType2 = InspectionTypeBuilder.anInspectionType().build();
		AssetType assetType = AssetTypeBuilder.anAssetType().withInspectionTypes(inspectionType, inspectionType2).build();
		AssetType assetType2 = AssetTypeBuilder.anAssetType().withInspectionTypes(inspectionType).build();

		CommonInspectionTypeHandler sut = new LoaderBackedCommonInspectionTypeHandler(new CommonProductTypeIdLoaderTestDouble(ImmutableList.of(assetType, assetType2)));
		Set<InspectionType> returnSet = sut.findCommonInspectionTypesFor(ImmutableList.of(1L));

		Set<InspectionType> expectedInspectionList = ImmutableSet.of(inspectionType);
		assertThat(returnSet, equalTo(expectedInspectionList));

	}

	@Test
	public void should_return_empty_set_from_multiple_asset_types_with_no_common_inspections() {

		InspectionType inspectionType = InspectionTypeBuilder.anInspectionType().build();
		InspectionType inspectionType2 = InspectionTypeBuilder.anInspectionType().build();
		AssetType assetType = AssetTypeBuilder.anAssetType().withInspectionTypes(inspectionType).build();
		AssetType assetType2 = AssetTypeBuilder.anAssetType().withInspectionTypes(inspectionType2).build();

		CommonInspectionTypeHandler sut = new LoaderBackedCommonInspectionTypeHandler(new CommonProductTypeIdLoaderTestDouble(ImmutableList.of(assetType, assetType2)));
		Set<InspectionType> returnSet = sut.findCommonInspectionTypesFor(ImmutableList.of(1L));

		assertThat(returnSet, equalTo(EMPTY_INSPECTION_TYPE_SET));

	}

	@Test
	public void should_return_empty_set_given_no_asset_types() {
		CommonInspectionTypeHandler sut = new LoaderBackedCommonInspectionTypeHandler(new CommonProductTypeIdLoaderTestDouble(new ArrayList<AssetType>()));
		
		Set<InspectionType> returnSet = sut.findCommonInspectionTypesFor(ImmutableList.of(1L));

		assertThat(returnSet, equalTo(EMPTY_INSPECTION_TYPE_SET));

	}

}
