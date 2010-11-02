package com.n4systems.persistence.utils;

import static com.n4systems.model.builders.EventBuilder.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.Event;
import org.junit.Assert;
import org.junit.Test;

import com.n4systems.handlers.TestUsesTransactionBase;
import com.n4systems.model.safetynetwork.IdLoader;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.test.helpers.FluentArrayList;

public class LazyLoadingListTest extends TestUsesTransactionBase {

	
	
	@Test
	public void should_find_size_of_list_to_match_id_list() throws Exception {
		assertEquals(3, new LazyLoadingList<Event>(new FluentArrayList<Long>(1L, 2L, 3L), null, null).size());
		assertEquals(2, new LazyLoadingList<Event>(new FluentArrayList<Long>(1L, 2L), null, null).size());
		assertEquals(1, new LazyLoadingList<Event>(new FluentArrayList<Long>(1L), null, null).size());
		assertEquals(0, new LazyLoadingList<Event>(new FluentArrayList<Long>(), null, null).size());
		
		assertTrue(new LazyLoadingList<Event>(new FluentArrayList<Long>(), null, null).isEmpty());
	}
	
	
	
	@Test
	public void should_take_the_id_list_by_value_not_reference() throws Exception {
		List<Long> inspectionIds = new FluentArrayList<Long>(1L, 2L, 3L);
		List<Event> sut = new LazyLoadingList<Event>(inspectionIds, null, null);
		
		inspectionIds.clear();
		assertEquals(3, sut.size());
	}

	@Test
	public void should_load_only_first_inspection_when_index_is_accessed() throws Exception {
		IdLoaderFake loader = new IdLoaderFake();
		mockTransaction();
		List<Long> inspectionIds = new FluentArrayList<Long>(1L, 2L, 3L);
		List<Event> sut = new LazyLoadingList<Event>(inspectionIds, loader, mockTransaction);
		sut.get(0);
		
		Assert.assertEquals(new FluentArrayList<Long>(1L), loader.loadedIds);
	}
	
	
	@Test
	public void should_load_each_inspection_when_iterated() throws Exception {
		IdLoaderFake loader = new IdLoaderFake();
		mockTransaction();
		List<Long> inspectionIds = new FluentArrayList<Long>(1L, 2L, 3L);
		List<Event> sut = new LazyLoadingList<Event>(inspectionIds, loader, mockTransaction);
		for (Event event : sut) {
			assertNotNull(event);
		}
		
		Assert.assertEquals(inspectionIds, loader.loadedIds);
	}
	
	@Test
	public void should_load_the_same_inspection_multiple_times_if_requested_multiple_times() throws Exception {
		IdLoaderFake loader = new IdLoaderFake();
		mockTransaction();
		List<Long> inspectionIds = new FluentArrayList<Long>(1L, 2L, 3L);
		List<Event> sut = new LazyLoadingList<Event>(inspectionIds, loader, mockTransaction);
	
		sut.get(0);
		sut.get(0);
		
		Assert.assertEquals(new FluentArrayList<Long>(1L, 1L), loader.loadedIds);
	}

	private final class IdLoaderFake extends Loader<Event> implements IdLoader<IdLoaderFake>{
		private Long id;
		
		List<Long> loadedIds = new ArrayList<Long>();


		
		
		public IdLoaderFake setId(Long id) {
			this.id = id;
			return this;
		}

		@Override
		protected Event load(EntityManager em) {
			return null;
		}

		@Override
		public Event load(Transaction transaction) {
			loadedIds.add(id);
			Event event = anEvent().build();
			event.setId(id);
			return event;
		}
	}
}
