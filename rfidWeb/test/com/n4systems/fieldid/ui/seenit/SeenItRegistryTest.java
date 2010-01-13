package com.n4systems.fieldid.ui.seenit;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

import com.n4systems.model.ui.seenit.SeenItItem;


public class SeenItRegistryTest {
	private static final SeenItItem SOME_SEEN_IT_ITEM = SeenItItem.SetupWizard;

	@Test
	public void should_respond_with_no_to_any_type_of_seen_it_item_on_a_fresh_registry() throws Exception {
		SeenItRegistry sut = createValidSeenItRegistry();
		
		assertTrue("Expected to not have seen it, but response that I have seen it.", !sut.haveISeen(SOME_SEEN_IT_ITEM));
	}
	
	
	@Test
	public void should_allow_the_setting_that_i_have_seen_something() throws Exception {
		SeenItRegistry sut = createValidSeenItRegistry();
		
		sut.iHaveSeen(SOME_SEEN_IT_ITEM);
		assertTrue(sut.haveISeen(SOME_SEEN_IT_ITEM));
	}
	
	
	@Test
	public void should_beable_to_forget_that_I_have_seen_something() throws Exception {
		SeenItRegistry sut = createValidSeenItRegistry();
		sut.iHaveSeen(SOME_SEEN_IT_ITEM);
		sut.iHaveNotSeen(SOME_SEEN_IT_ITEM);

		assertTrue("Expected to not have seen it, but response that I have seen it.", !sut.haveISeen(SOME_SEEN_IT_ITEM));
	}

	
	@Test
	public void should_retreive_starting_registry_data_from_the_data_source_durning_construction() throws Exception {
		SeenItRegistryDataSource dataSource = createMock(SeenItRegistryDataSource.class);
		expect(dataSource.loadExisting()).andReturn(new HashMap<SeenItItem, Boolean>());
		replay(dataSource);
		
		new SeenItRegistryImpl(dataSource);
		
		verify(dataSource);
	}
	
	
	@Test
	public void should_push_change_to_data_source_durning_an_update_of_the_seen_setting() throws Exception {
		SeenItRegistryDataSource dataSource = createMock(SeenItRegistryDataSource.class);
			expect(dataSource.loadExisting()).andReturn(new HashMap<SeenItItem, Boolean>());
			dataSource.updateItem(SOME_SEEN_IT_ITEM, true);
			dataSource.updateItem(SOME_SEEN_IT_ITEM, false);
			replay(dataSource);
		
		SeenItRegistryImpl sut = new SeenItRegistryImpl(dataSource);
		
		sut.iHaveSeen(SOME_SEEN_IT_ITEM);
		sut.iHaveNotSeen(SOME_SEEN_IT_ITEM);
		
		
		verify(dataSource);
	}
	

	private SeenItRegistry createValidSeenItRegistry() {
		return new SeenItRegistryImpl(new SeenItRegistryEmpyDataSource());
	}
}
