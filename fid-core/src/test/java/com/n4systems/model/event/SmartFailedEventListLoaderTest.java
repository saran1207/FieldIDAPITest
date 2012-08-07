package com.n4systems.model.event;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.EventType;
import com.n4systems.model.Status;
import com.n4systems.model.builders.EventBuilder;
import com.n4systems.model.common.SimpleFrequency;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.utils.PlainDate;
import com.n4systems.util.DateHelper;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import com.n4systems.util.persistence.WhereParameterGroup;
import com.n4systems.util.time.StoppedClock;


public class SmartFailedEventListLoaderTest {

	
	private EntityManager mockEntityManager;
	private QueryBuilder<Event> mockQueryBuilder;
	private StoppedClock clock = new StoppedClock(new PlainDate());
	private EventType typeA = new EventType("A");
	private EventType typeB = new EventType("B");
	private EventType typeC = new EventType("C");
	private OpenSecurityFilter filter = new OpenSecurityFilter();	
	private Asset asset1= new Asset();
	private Asset asset2= new Asset();
	private Asset asset3= new Asset();
	
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() {
		mockEntityManager = createMock(EntityManager.class);
		asset1.setId(1L);
		asset2.setId(2L);
		asset3.setId(3L);
		typeA.setId(111L);
		typeB.setId(222L);
		typeC.setId(333L);
		replay(mockEntityManager);
		mockQueryBuilder = createMock(QueryBuilder.class);
	}
	
	@Test
	public void test_load_fail_then_pass() {
		//setup
		List<Event> results = createResults(new TestEvent(typeA, Status.FAIL, asset1),
				new TestEvent(typeA, Status.PASS, asset1)
		);
		withExpectations(results);
				
		SmartFailedEventListLoader smartFailedEventListLoader = createSmartFailedEventListLoader(filter);
		
		List<Event> actual = smartFailedEventListLoader.load(mockEntityManager, filter);
		
		assertEquals(0, actual.size());

		//verify
		verify(mockQueryBuilder);
		verify(mockEntityManager);
	}
	
	
	@Test
	public void test_load_pass_then_fail() {
		//setup
		List<Event> results = createResults(new TestEvent(typeA, Status.PASS, asset1),
											new TestEvent(typeA, Status.FAIL, asset1)
										);
		withExpectations(results);
				
		SmartFailedEventListLoader smartFailedEventListLoader = createSmartFailedEventListLoader(filter);
		
		List<Event> actual = smartFailedEventListLoader.load(mockEntityManager, filter);
		
		assertEquals(1, actual.size());

		//verify
		verify(mockQueryBuilder);
		verify(mockEntityManager);
	}
		
	
	@Test
	public void test_load_different_event_types() {
		//setup
		List<Event> results = createResults(new TestEvent(typeA, Status.FAIL, asset1),
											new TestEvent(typeB, Status.FAIL, asset1),
											new TestEvent(typeC, Status.FAIL, asset1)
										);
		withExpectations(results);
				
		SmartFailedEventListLoader smartFailedEventListLoader = createSmartFailedEventListLoader(filter);
		
		List<Event> actual = smartFailedEventListLoader.load(mockEntityManager, filter);
		
		assertEquals(3, actual.size());

		//verify
		verify(mockQueryBuilder);
		verify(mockEntityManager);
	}
		
	
	
	@Test
	public void test_load_different_event_types_pass_and_fail() {
		//setup
		List<Event> results = createResults(new TestEvent(typeA, Status.FAIL, asset1),
											new TestEvent(typeB, Status.PASS, asset1),
											new TestEvent(typeC, Status.FAIL, asset1)
										);
		withExpectations(results);
				
		SmartFailedEventListLoader smartFailedEventListLoader = createSmartFailedEventListLoader(filter);
		
		List<Event> actual = smartFailedEventListLoader.load(mockEntityManager, filter);
		
		assertEquals(2, actual.size());

		//verify
		verify(mockQueryBuilder);
		verify(mockEntityManager);
	}

	
	@Test
	public void test_load_different_assets() {
		//setup
		List<Event> results = createResults(new TestEvent(typeA, Status.FAIL, asset1),
											new TestEvent(typeA, Status.FAIL, asset2),
											new TestEvent(typeA, Status.FAIL, asset3)
										);
		withExpectations(results);
				
		SmartFailedEventListLoader smartFailedEventListLoader = createSmartFailedEventListLoader(filter);
		
		List<Event> actual = smartFailedEventListLoader.load(mockEntityManager, filter);
		
		assertEquals(3, actual.size());

		//verify
		verify(mockQueryBuilder);
		verify(mockEntityManager);
	}

	@Test
	public void test_load_different_assets_pass_fail() {
		//setup
		List<Event> results = createResults(new TestEvent(typeA, Status.PASS, asset1),
											new TestEvent(typeA, Status.FAIL, asset2),
											new TestEvent(typeA, Status.PASS, asset2),
											new TestEvent(typeA, Status.FAIL, asset3),
											new TestEvent(typeA, Status.PASS, asset3)
										);
		withExpectations(results);
				
		SmartFailedEventListLoader smartFailedEventListLoader = createSmartFailedEventListLoader(filter);
		
		List<Event> actual = smartFailedEventListLoader.load(mockEntityManager, filter);

		assertEquals(0, actual.size());

		//verify
		verify(mockQueryBuilder);
		verify(mockEntityManager);
	}

	
	@Test
	public void test_load_na_after_fail() {
		//setup
		List<Event> results = createResults(new TestEvent(typeA, Status.FAIL, asset1),
											new TestEvent(typeA, Status.NA, asset1)
										);
		withExpectations(results);
				
		SmartFailedEventListLoader smartFailedEventListLoader = createSmartFailedEventListLoader(filter);
		
		List<Event> actual = smartFailedEventListLoader.load(mockEntityManager, filter);

		assertEquals(1, actual.size());

		//verify
		verify(mockQueryBuilder);
		verify(mockEntityManager);
	}

	
	@Test
	public void test_load_na_after_pass() {
		//setup
		List<Event> results = createResults(new TestEvent(typeA, Status.PASS, asset1),
											new TestEvent(typeA, Status.NA, asset1)
										);
		withExpectations(results);
				
		SmartFailedEventListLoader smartFailedEventListLoader = createSmartFailedEventListLoader(filter);
		
		List<Event> actual = smartFailedEventListLoader.load(mockEntityManager, filter);

		assertEquals(0, actual.size());

		//verify
		verify(mockQueryBuilder);
		verify(mockEntityManager);
	}

	
	
	
	private SmartFailedEventListLoader createSmartFailedEventListLoader(OpenSecurityFilter filter) {
		SmartFailedEventListLoader smartFailedEventListLoader = new SmartFailedEventListLoader(filter) { 
			@Override QueryBuilder<Event> createQueryBuilder(SecurityFilter filter) {
				return mockQueryBuilder;
			};
		};
		smartFailedEventListLoader.setClock(clock);
		smartFailedEventListLoader.setFrequency(SimpleFrequency.DAILY);
		return smartFailedEventListLoader;
	}

	private void withExpectations(List<Event> results) {
		Date fromDate = DateHelper.increment(clock.currentTime(), DateHelper.DAY, -1);
		List<String> postFetchPaths = Lists.newArrayList();
		
		expect(mockQueryBuilder.addOrder("completedDate")).andReturn(mockQueryBuilder);
		expect(mockQueryBuilder.addWhere(Comparator.GE, "date", "completedDate", fromDate)).andReturn(mockQueryBuilder);
		expect(mockQueryBuilder.addWhere(anyObject(WhereParameterGroup.class))).andReturn(mockQueryBuilder);
		expect(mockQueryBuilder.getResultList(mockEntityManager)).andReturn(results);
		expect(mockQueryBuilder.getPostFetchPaths()).andReturn(postFetchPaths);
		replay(mockQueryBuilder);		
	}

	private List<Event> createResults(TestEvent... events) {
		List<Event> results = new ArrayList<Event>();
		for (TestEvent te:events) { 
			results.add(EventBuilder.anEvent().on(te.asset).withResult(te.status).ofType(te.type).build());			
		}
		return results;
	}


	class TestEvent {
		
		EventType type;
		Status status;
		Asset asset;

		public TestEvent(EventType type, Status status, Asset asset) {
			this.type = type;
			this.status = status;
			this.asset = asset;
		}
	}
	
	
}
