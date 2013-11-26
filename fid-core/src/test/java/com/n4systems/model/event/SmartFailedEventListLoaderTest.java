package com.n4systems.model.event;

import com.google.common.collect.Lists;
import com.n4systems.model.Asset;
import com.n4systems.model.EventResult;
import com.n4systems.model.ThingEvent;
import com.n4systems.model.ThingEventType;
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
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;


public class SmartFailedEventListLoaderTest {

	
	private EntityManager mockEntityManager;
	private QueryBuilder<ThingEvent> mockQueryBuilder;
	private StoppedClock clock = new StoppedClock(new PlainDate());
	private ThingEventType typeA = new ThingEventType("A");
	private ThingEventType typeB = new ThingEventType("B");
	private ThingEventType typeC = new ThingEventType("C");
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
		List<ThingEvent> results = createResults(new TestEvent(typeA, EventResult.FAIL, asset1),
				new TestEvent(typeA, EventResult.PASS, asset1)
		);
		withExpectations(results);
				
		SmartFailedEventListLoader smartFailedEventListLoader = createSmartFailedEventListLoader(filter);
		
		List<ThingEvent> actual = smartFailedEventListLoader.load(mockEntityManager, filter);
		
		assertEquals(0, actual.size());

		//verify
		verify(mockQueryBuilder);
		verify(mockEntityManager);
	}
	
	
	@Test
	public void test_load_pass_then_fail() {
		//setup
		List<ThingEvent> results = createResults(new TestEvent(typeA, EventResult.PASS, asset1),
											new TestEvent(typeA, EventResult.FAIL, asset1)
										);
		withExpectations(results);
				
		SmartFailedEventListLoader smartFailedEventListLoader = createSmartFailedEventListLoader(filter);
		
		List<ThingEvent> actual = smartFailedEventListLoader.load(mockEntityManager, filter);
		
		assertEquals(1, actual.size());

		//verify
		verify(mockQueryBuilder);
		verify(mockEntityManager);
	}
		
	
	@Test
	public void test_load_different_event_types() {
		//setup
		List<ThingEvent> results = createResults(new TestEvent(typeA, EventResult.FAIL, asset1),
											new TestEvent(typeB, EventResult.FAIL, asset1),
											new TestEvent(typeC, EventResult.FAIL, asset1)
										);
		withExpectations(results);
				
		SmartFailedEventListLoader smartFailedEventListLoader = createSmartFailedEventListLoader(filter);
		
		List<ThingEvent> actual = smartFailedEventListLoader.load(mockEntityManager, filter);
		
		assertEquals(3, actual.size());

		//verify
		verify(mockQueryBuilder);
		verify(mockEntityManager);
	}
		
	
	
	@Test
	public void test_load_different_event_types_pass_and_fail() {
		//setup
		List<ThingEvent> results = createResults(new TestEvent(typeA, EventResult.FAIL, asset1),
											new TestEvent(typeB, EventResult.PASS, asset1),
											new TestEvent(typeC, EventResult.FAIL, asset1)
										);
		withExpectations(results);
				
		SmartFailedEventListLoader smartFailedEventListLoader = createSmartFailedEventListLoader(filter);
		
		List<ThingEvent> actual = smartFailedEventListLoader.load(mockEntityManager, filter);
		
		assertEquals(2, actual.size());

		//verify
		verify(mockQueryBuilder);
		verify(mockEntityManager);
	}

	
	@Test
	public void test_load_different_assets() {
		//setup
		List<ThingEvent> results = createResults(new TestEvent(typeA, EventResult.FAIL, asset1),
											new TestEvent(typeA, EventResult.FAIL, asset2),
											new TestEvent(typeA, EventResult.FAIL, asset3)
										);
		withExpectations(results);
				
		SmartFailedEventListLoader smartFailedEventListLoader = createSmartFailedEventListLoader(filter);
		
		List<ThingEvent> actual = smartFailedEventListLoader.load(mockEntityManager, filter);
		
		assertEquals(3, actual.size());

		//verify
		verify(mockQueryBuilder);
		verify(mockEntityManager);
	}

	@Test
	public void test_load_different_assets_pass_fail() {
		//setup
		List<ThingEvent> results = createResults(new TestEvent(typeA, EventResult.PASS, asset1),
											new TestEvent(typeA, EventResult.FAIL, asset2),
											new TestEvent(typeA, EventResult.PASS, asset2),
											new TestEvent(typeA, EventResult.FAIL, asset3),
											new TestEvent(typeA, EventResult.PASS, asset3)
										);
		withExpectations(results);
				
		SmartFailedEventListLoader smartFailedEventListLoader = createSmartFailedEventListLoader(filter);
		
		List<ThingEvent> actual = smartFailedEventListLoader.load(mockEntityManager, filter);

		assertEquals(0, actual.size());

		//verify
		verify(mockQueryBuilder);
		verify(mockEntityManager);
	}

	
	@Test
	public void test_load_na_after_fail() {
		//setup
		List<ThingEvent> results = createResults(new TestEvent(typeA, EventResult.FAIL, asset1),
											new TestEvent(typeA, EventResult.NA, asset1)
										);
		withExpectations(results);
				
		SmartFailedEventListLoader smartFailedEventListLoader = createSmartFailedEventListLoader(filter);
		
		List<ThingEvent> actual = smartFailedEventListLoader.load(mockEntityManager, filter);

		assertEquals(1, actual.size());

		//verify
		verify(mockQueryBuilder);
		verify(mockEntityManager);
	}

	
	@Test
	public void test_load_na_after_pass() {
		//setup
		List<ThingEvent> results = createResults(new TestEvent(typeA, EventResult.PASS, asset1),
											new TestEvent(typeA, EventResult.NA, asset1)
										);
		withExpectations(results);
				
		SmartFailedEventListLoader smartFailedEventListLoader = createSmartFailedEventListLoader(filter);
		
		List<ThingEvent> actual = smartFailedEventListLoader.load(mockEntityManager, filter);

		assertEquals(0, actual.size());

		//verify
		verify(mockQueryBuilder);
		verify(mockEntityManager);
	}

	
	
	
	private SmartFailedEventListLoader createSmartFailedEventListLoader(OpenSecurityFilter filter) {
		SmartFailedEventListLoader smartFailedEventListLoader = new SmartFailedEventListLoader(filter) { 
			@Override QueryBuilder<ThingEvent> createQueryBuilder(SecurityFilter filter) {
				return mockQueryBuilder;
			};
		};
		smartFailedEventListLoader.setClock(clock);
		smartFailedEventListLoader.setFrequency(SimpleFrequency.DAILY);
		return smartFailedEventListLoader;
	}

	private void withExpectations(List<ThingEvent> results) {
		Date fromDate = DateHelper.increment(clock.currentTime(), DateHelper.DAY, -1);
		List<String> postFetchPaths = Lists.newArrayList();
		
		expect(mockQueryBuilder.addOrder("completedDate")).andReturn(mockQueryBuilder);
		expect(mockQueryBuilder.addWhere(Comparator.GE, "date", "completedDate", fromDate)).andReturn(mockQueryBuilder);
		expect(mockQueryBuilder.addWhere(anyObject(WhereParameterGroup.class))).andReturn(mockQueryBuilder);
		expect(mockQueryBuilder.getResultList(mockEntityManager)).andReturn(results);
		expect(mockQueryBuilder.getPostFetchPaths()).andReturn(postFetchPaths);
		replay(mockQueryBuilder);		
	}

	private List<ThingEvent> createResults(TestEvent... events) {
		List<ThingEvent> results = new ArrayList<ThingEvent>();
		for (TestEvent te:events) { 
			results.add(EventBuilder.anEvent().on(te.asset).withResult(te.eventResult).ofType(te.type).build());
		}
		return results;
	}


	class TestEvent {
		
		ThingEventType type;
		EventResult eventResult;
		Asset asset;

		public TestEvent(ThingEventType type, EventResult eventResult, Asset asset) {
			this.type = type;
			this.eventResult = eventResult;
			this.asset = asset;
		}
	}
	
	
}
