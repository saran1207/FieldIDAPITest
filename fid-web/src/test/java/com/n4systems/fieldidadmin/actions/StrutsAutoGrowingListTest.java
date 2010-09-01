package com.n4systems.fieldidadmin.actions;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldidadmin.actions.ObjectFactor;
import com.n4systems.fieldidadmin.actions.StrutsAutoGrowingList;
import com.n4systems.test.helpers.FluentArrayList;


public class StrutsAutoGrowingListTest {
	private FluentArrayList<String> startingList;

	private final class StringObjectFactory implements ObjectFactor<String> {
		public String create() {
			return "";
		}
	}

	@Before
	public void createStartingList() {
		startingList = new FluentArrayList<String>("first string", "second String");
	}
	
	
	@Test
	public void should_return_the_element_when_index_is_inside_the_list() throws Exception {
		List<String> sut = new StrutsAutoGrowingList<String>(startingList, new StringObjectFactory());
		
		assertEquals(startingList.get(0), sut.get(0));
		
	}
	
	@Test
	public void should_return_an_element_from_one_index_outside_the_start_list_boundary() throws Exception {
		List<String> sut = new StrutsAutoGrowingList<String>(startingList, new StringObjectFactory());
		
		assertEquals("", sut.get(indexOutsideListBy(1,startingList)));
	}
	
	@Test
	public void should_increase_list_size_by_one_when_getting_an_index_one_outside_the_end_of_the_list() throws Exception {
		int originalSize = startingList.size();
		List<String> sut = new StrutsAutoGrowingList<String>(startingList, new StringObjectFactory());
		
		sut.get(startingList.size());
		assertEquals(originalSize + 1, indexOutsideListBy(1, sut));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void should_use_object_factory_to_create_elements_when_extending_the_list() throws Exception {
		ObjectFactor<String> objectFactory = createMock(ObjectFactor.class);
		expect(objectFactory.create()).andReturn("");
		replay(objectFactory);
		
		
		List<String> sut = new StrutsAutoGrowingList<String>(startingList, objectFactory);
		
		sut.get(startingList.size());
		verify(objectFactory);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void should_use_object_factory_to_create_once_for_each_element_that_is_added_when_extending_the_list() throws Exception {
		int numberOfExtraElements = 4;
		
		ObjectFactor<String> objectFactory = createMock(ObjectFactor.class);
		expect(objectFactory.create()).andReturn("").times(numberOfExtraElements);
		replay(objectFactory);
		
		
		List<String> sut = new StrutsAutoGrowingList<String>(startingList, objectFactory);
		
		sut.get(indexOutsideListBy(numberOfExtraElements, startingList));
		verify(objectFactory);
	}
	
	
	
	
	@Test
	public void should_return_an_element_from_many_index_outside_the_start_list_boundary() throws Exception {
		List<String> sut = new StrutsAutoGrowingList<String>(startingList, new StringObjectFactory());
		
		assertEquals("", sut.get(indexOutsideListBy(20, sut)));
	}
	
	@Test
	public void should_increase_list_size_by_20_when_getting_an_index_20_outside_the_end_of_the_list() throws Exception {
		int originalSize = startingList.size();
		List<String> sut = new StrutsAutoGrowingList<String>(startingList, new StringObjectFactory());
		
		sut.get(startingList.size());
		assertEquals(originalSize + 20, indexOutsideListBy(20, sut) );
	}


	private int indexOutsideListBy(int numberOutsideList, List<String> sut) {
		return sut.size() - 1 + numberOutsideList;
	}
}
