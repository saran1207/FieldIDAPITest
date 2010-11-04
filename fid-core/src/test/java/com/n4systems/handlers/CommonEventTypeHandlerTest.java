package com.n4systems.handlers;

import static org.easymock.EasyMock.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.n4systems.model.AssetType;
import com.n4systems.model.EventType;
import com.n4systems.model.builders.AssetTypeBuilder;
import com.n4systems.model.builders.EventTypeBuilder;
import com.n4systems.model.eventtype.CommonAssetTypeLoader;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.n4systems.persistence.Transaction;

public class CommonEventTypeHandlerTest {

	private static final Set<EventType> EMPTY_EVENT_TYPE_SET = new HashSet<EventType>();

	private class CommonAssetTypeIdLoaderTestDouble implements CommonAssetTypeLoader {
		List<AssetType> assetTypes;
		
		public CommonAssetTypeIdLoaderTestDouble() {
			this(new ArrayList<AssetType>());
		}
		public CommonAssetTypeIdLoaderTestDouble(List<AssetType> assetTypes) {
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
		public CommonAssetTypeLoader forAssets(List<Long> assetIds) {
			return this;
		}
	}

	@Test
	public void should_load_asset_types_from_the_asset_type_id_looder() throws Exception {
		ImmutableList<Long> assetIds = ImmutableList.of(1L);

		CommonAssetTypeLoader assetTypeIdLoader = createMock(CommonAssetTypeLoader.class);
		expect(assetTypeIdLoader.forAssets(assetIds)).andReturn(assetTypeIdLoader);
		expect(assetTypeIdLoader.load()).andReturn(new ArrayList<AssetType>());
		replay(assetTypeIdLoader);

		CommonEventTypeHandler sut = new LoaderBackedCommonEventTypeHandler(assetTypeIdLoader);

		sut.findCommonEventTypesFor(assetIds);

		verify(assetTypeIdLoader);
	}

	@Test
	public void should_always_return_an_empty_list_of_event_types_when_given_an_empty_list_of_assets_ids() throws Exception {

		CommonEventTypeHandler sut = new LoaderBackedCommonEventTypeHandler(null);

		Set<EventType> eventTypes = sut.findCommonEventTypesFor(new ArrayList<Long>());

		assertThat(eventTypes, equalTo(EMPTY_EVENT_TYPE_SET));

	}

	@Test(expected = NullPointerException.class)
	public void should_throw_exception_when_a_null_list_of_ids_is_given() throws Exception {
		CommonEventTypeHandler sut = new LoaderBackedCommonEventTypeHandler(new CommonAssetTypeIdLoaderTestDouble());

		sut.findCommonEventTypesFor(null);
	}

	@Test
	public void should_return_an_empty_list_of_event_types_when_there_is_one_asset_type_with_no_associated_event_types() throws Exception {

		AssetType assetType = AssetTypeBuilder.anAssetType().build();

		CommonEventTypeHandler sut = new LoaderBackedCommonEventTypeHandler(new CommonAssetTypeIdLoaderTestDouble(ImmutableList.of(assetType)));
		
		Set<EventType> returnSet = sut.findCommonEventTypesFor(ImmutableList.of(1L));

		assertThat(returnSet, equalTo(EMPTY_EVENT_TYPE_SET));

	}

	@Test
	public void should_return_entire_event_type_list_given_one_asset_type_with_many_event_types() {

		EventType eventType = EventTypeBuilder.anEventType().build();
		EventType eventType2 = EventTypeBuilder.anEventType().build();
		AssetType assetType = AssetTypeBuilder.anAssetType().withEventTypes(eventType, eventType2).build();

		CommonEventTypeHandler sut = new LoaderBackedCommonEventTypeHandler(new CommonAssetTypeIdLoaderTestDouble(ImmutableList.of(assetType)));
		Set<EventType> returnSet = sut.findCommonEventTypesFor(ImmutableList.of(1L));

		Set<EventType> expectedEventList = ImmutableSet.of(eventType, eventType2);
		assertThat(returnSet, equalTo(expectedEventList));

	}

	@Test
	public void should_return_common_event_types_of_two_or_more_asset_types() {

		EventType eventType = EventTypeBuilder.anEventType().build();
		EventType eventType2 = EventTypeBuilder.anEventType().build();
		AssetType assetType = AssetTypeBuilder.anAssetType().withEventTypes(eventType, eventType2).build();
		AssetType assetType2 = AssetTypeBuilder.anAssetType().withEventTypes(eventType).build();

		CommonEventTypeHandler sut = new LoaderBackedCommonEventTypeHandler(new CommonAssetTypeIdLoaderTestDouble(ImmutableList.of(assetType, assetType2)));
		Set<EventType> returnSet = sut.findCommonEventTypesFor(ImmutableList.of(1L));

		Set<EventType> expectedEventList = ImmutableSet.of(eventType);
		assertThat(returnSet, equalTo(expectedEventList));

	}

	@Test
	public void should_return_empty_set_from_multiple_asset_types_with_no_common_events() {

		EventType eventType = EventTypeBuilder.anEventType().build();
		EventType eventType2 = EventTypeBuilder.anEventType().build();
		AssetType assetType = AssetTypeBuilder.anAssetType().withEventTypes(eventType).build();
		AssetType assetType2 = AssetTypeBuilder.anAssetType().withEventTypes(eventType2).build();

		CommonEventTypeHandler sut = new LoaderBackedCommonEventTypeHandler(new CommonAssetTypeIdLoaderTestDouble(ImmutableList.of(assetType, assetType2)));
		Set<EventType> returnSet = sut.findCommonEventTypesFor(ImmutableList.of(1L));

		assertThat(returnSet, equalTo(EMPTY_EVENT_TYPE_SET));

	}

	@Test
	public void should_return_empty_set_given_no_asset_types() {
		CommonEventTypeHandler sut = new LoaderBackedCommonEventTypeHandler(new CommonAssetTypeIdLoaderTestDouble(new ArrayList<AssetType>()));
		
		Set<EventType> returnSet = sut.findCommonEventTypesFor(ImmutableList.of(1L));

		assertThat(returnSet, equalTo(EMPTY_EVENT_TYPE_SET));

	}

}
