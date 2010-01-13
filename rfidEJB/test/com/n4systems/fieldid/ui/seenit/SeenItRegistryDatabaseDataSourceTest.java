package com.n4systems.fieldid.ui.seenit;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.easymock.Capture;
import org.junit.Test;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.ui.seenit.SeenItItem;
import com.n4systems.model.ui.seenit.SeenItStorageItem;
import com.n4systems.test.helpers.FluentHashSet;

public class SeenItRegistryDatabaseDataSourceTest {



	private static final long SOME_USER_ID = 1L;


	@Test(expected=InvalidArgumentException.class)
	public void should_require_user_id_to_create() {
		new SeenItRegistryDatabaseDataSource(null);
	}
	
	
	@Test
	public void should_load_empty_map_of_seen_it_items_for_the_user_that_has_no_storage_item_was_found() throws Exception {
		
		SeenItItemLoaderStub loader = createSeenItItemStubThatLoads(null);
		
		SeenItRegistryDatabaseDataSourceWithDependancyAccess sut = new SeenItRegistryDatabaseDataSourceWithDependancyAccess(SOME_USER_ID);
		sut.seenItLoader = loader;
		
		Map<SeenItItem, Boolean> actualMapOfSeenItItems = sut.loadExisting();
		assertTrue(actualMapOfSeenItItems.isEmpty());
	}
	
	@Test
	public void should_load_empty_map_of_seen_it_items_for_the_user_that_has_no_seen_it_items_stored() throws Exception {
		
		SeenItItemLoaderStub loader = createSeenItItemStubThatLoads(new SeenItStorageItem(SOME_USER_ID));
		
		SeenItRegistryDatabaseDataSourceWithDependancyAccess sut = new SeenItRegistryDatabaseDataSourceWithDependancyAccess(SOME_USER_ID);
		sut.seenItLoader = loader;
		
		Map<SeenItItem, Boolean> actualMapOfSeenItItems = sut.loadExisting();
		assertTrue(actualMapOfSeenItItems.isEmpty());
	}
	
	
	@Test
	public void should_push_user_id_to_loader() throws Exception {
		
		SeenItItemLoader loader = createMock(SeenItItemLoader.class);
		expect(loader.setUserId(SOME_USER_ID)).andReturn(loader);
		expect(loader.load()).andReturn(new SeenItStorageItem(SOME_USER_ID));
		replay(loader);
		
		
		SeenItRegistryDatabaseDataSourceWithDependancyAccess sut = new SeenItRegistryDatabaseDataSourceWithDependancyAccess(SOME_USER_ID);
		sut.seenItLoader = loader;
		
		sut.loadExisting();
		
		verify(loader);
	}

	@Test
	public void should_load_seen_it_items_for_the_user_that_has_seen_items() throws Exception {
		
		Map<SeenItItem, Boolean> expectedMapOfSeenItItems = new HashMap<SeenItItem, Boolean>();
		expectedMapOfSeenItItems.put(SeenItItem.SetupWizard, true);
		
		SeenItStorageItem storageItem = new SeenItStorageItem(SOME_USER_ID);
		storageItem.getItemsSeen().add(SeenItItem.SetupWizard);
		
		SeenItItemLoaderStub loader = createSeenItItemStubThatLoads(storageItem);
		
		
		SeenItRegistryDatabaseDataSourceWithDependancyAccess sut = new SeenItRegistryDatabaseDataSourceWithDependancyAccess(SOME_USER_ID);
		sut.seenItLoader = loader;
		
		Map<SeenItItem, Boolean> actualMapOfSeenItItems = sut.loadExisting();
		
		assertEquals(expectedMapOfSeenItItems, actualMapOfSeenItItems);
	}


	private SeenItItemLoaderStub createSeenItItemStubThatLoads(SeenItStorageItem storageItem) {
		SeenItItemLoaderStub loader = new SeenItItemLoaderStub();
		loader.responseForLoad = storageItem;
		return loader;
	}

	
	@Test
	public void should_save_seen_it_storage_item_when_setting_is_changed() throws Exception {
		SeenItItemSaver saver = createMock(SeenItItemSaver.class);
		expect(saver.saveOrUpdate((SeenItStorageItem)anyObject())).andReturn(null);
		replay(saver);
		
		SeenItRegistryDatabaseDataSourceWithDependancyAccess sut = new SeenItRegistryDatabaseDataSourceWithDependancyAccess(SOME_USER_ID);
		sut.seenItSaver = saver;
		sut.seenItLoader = createSeenItItemStubThatLoads(new SeenItStorageItem(SOME_USER_ID));
		
		sut.updateItem(SeenItItem.SetupWizard, true);
		
		verify(saver);
	} 
	
	@Test
	public void should_add_the_seen_it_item_to_the_storage_item_when_updated_as_seen() throws Exception {
		Capture<SeenItStorageItem> captured = new Capture<SeenItStorageItem>();
		
		SeenItItemSaver saver = createMock(SeenItItemSaver.class);
		expect(saver.saveOrUpdate((SeenItStorageItem)capture(captured))).andReturn(null);
		replay(saver);
		
		SeenItRegistryDatabaseDataSourceWithDependancyAccess sut = new SeenItRegistryDatabaseDataSourceWithDependancyAccess(SOME_USER_ID);
		sut.seenItSaver = saver;
		sut.seenItLoader = createSeenItItemStubThatLoads(new SeenItStorageItem(SOME_USER_ID));
		
		sut.updateItem(SeenItItem.SetupWizard, true);
		
		
		assertEquals(new FluentHashSet<SeenItItem>(SeenItItem.SetupWizard), captured.getValue().getItemsSeen());
	} 
	
	@Test
	public void should_remove_the_seen_it_item_from_the_storage_item_when_updated_as_not_seen() throws Exception {
		Capture<SeenItStorageItem> captured = new Capture<SeenItStorageItem>();
		
		SeenItItemSaver saver = createMock(SeenItItemSaver.class);
		expect(saver.saveOrUpdate((SeenItStorageItem)capture(captured))).andReturn(null);
		replay(saver);
		
		SeenItStorageItem storageItem = new SeenItStorageItem(SOME_USER_ID);
		storageItem.getItemsSeen().add(SeenItItem.SetupWizard);
		
		SeenItRegistryDatabaseDataSourceWithDependancyAccess sut = new SeenItRegistryDatabaseDataSourceWithDependancyAccess(SOME_USER_ID);
		sut.seenItSaver = saver;
		sut.seenItLoader = createSeenItItemStubThatLoads(storageItem);
		
		sut.updateItem(SeenItItem.SetupWizard, false);
		
		assertEquals(new HashSet<SeenItItem>(), captured.getValue().getItemsSeen());
	} 
	
	
	private class SeenItItemLoaderStub extends SeenItItemLoader {
		private SeenItStorageItem responseForLoad;
		
		public SeenItStorageItem load() { 
			return responseForLoad; 
		} 
		
	} 
	
	private class SeenItRegistryDatabaseDataSourceWithDependancyAccess extends SeenItRegistryDatabaseDataSource {

	
		private SeenItItemLoader seenItLoader;
		private SeenItItemSaver seenItSaver;

		public SeenItRegistryDatabaseDataSourceWithDependancyAccess(Long userId) {
			super(userId);
		}

		
		@Override
		protected SeenItItemLoader getLoader() {
			return seenItLoader;
		}

		@Override
		protected SeenItItemSaver getSaver() {
			return seenItSaver;
		}
		
	}

}
