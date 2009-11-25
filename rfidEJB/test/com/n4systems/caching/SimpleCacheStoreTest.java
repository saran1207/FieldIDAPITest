package com.n4systems.caching;

import static org.junit.Assert.*;

import org.easymock.classextension.EasyMock;
import org.junit.Before;
import org.junit.Test;

public class SimpleCacheStoreTest {
	
	private class TestKey extends CacheKey { 
		private final Integer key;
		
		public TestKey(Integer key) {
			this.key = key;
		}
		
		@Override
		public boolean equals(Object obj) {
			return (obj instanceof TestKey) ? ((TestKey)obj).key.equals(key) : false;
		}
		
		@Override
		public int hashCode() {
			return key.hashCode();
		}
		
		public Integer getKey() {
			return key;
		}
	}
	
	private class TestCacheStore extends SimpleCacheStore<TestKey, Integer> {
		public TestCacheStore(CacheLoader<TestKey, Integer> loader) {
			super(loader);
		}
	}
	
	private TestCacheStore trivialLoaderStore;
	private TestCacheStore mockedLoaderStore;
	private CacheLoader<TestKey, Integer> mockedLoader;
	
	@SuppressWarnings("unchecked")
	@Before
	public void setup_stores() {
		trivialLoaderStore= new TestCacheStore(new CacheLoader<TestKey, Integer>() {
			@Override
			public Integer load(TestKey key) {
				return key.getKey();
			}
		});
		
		mockedLoader = EasyMock.createMock(CacheLoader.class);
		mockedLoaderStore = new TestCacheStore(mockedLoader);
	}
	
	@Test(expected=NullPointerException.class)
	public void get_throws_exception_on_null_key() {
		trivialLoaderStore.get(null);
	}
	
	@Test(expected=NullPointerException.class)
	public void load_throws_exception_on_null_key() {
		trivialLoaderStore.load(null);
	}

	@Test(expected=NullPointerException.class)
	public void put_throws_exception_on_null_key() {
		trivialLoaderStore.put(null, 5);
	}

	@Test
	public void get_loads_and_caches_value() {
		TestKey key = new TestKey(10);
		
		EasyMock.expect(mockedLoader.load(key)).andReturn(key.getKey());
		EasyMock.replay(mockedLoader);
				
		assertSame(key.getKey(), mockedLoaderStore.get(key));
		assertSame(key.getKey(), mockedLoaderStore.get(key));
		
		EasyMock.verify(mockedLoader);
	}
	
	@Test
	public void load_caches_value() {
		TestKey key = new TestKey(10);
		
		EasyMock.expect(mockedLoader.load(key)).andReturn(key.getKey());
		EasyMock.replay(mockedLoader);
		
		mockedLoaderStore.load(key);
		assertSame(key.getKey(), mockedLoaderStore.get(key));
		
		EasyMock.verify(mockedLoader);
	}
	
	@Test
	public void put_overrites_last_value() {
		EasyMock.replay(mockedLoader);
		
		TestKey key = new TestKey(10);
		
		mockedLoaderStore.put(key, 50);
		assertSame(50, mockedLoaderStore.get(key));
		
		mockedLoaderStore.put(key, 42);
		assertSame(42, mockedLoaderStore.get(key));
		
		EasyMock.verify(mockedLoader);
	}
	
	@Test
	public void contains_checks_for_key() {
		EasyMock.replay(mockedLoader);
		
		TestKey key = new TestKey(10);
		
		assertFalse(mockedLoaderStore.contains(key));
		
		mockedLoaderStore.put(key, key.getKey());
		
		assertTrue(mockedLoaderStore.contains(key));
		
		EasyMock.verify(mockedLoader);
	}
	
	@Test
	public void expire_removes_element() {
		EasyMock.replay(mockedLoader);
		
		TestKey key = new TestKey(10);
		
		mockedLoaderStore.put(key, key.getKey());
		
		assertTrue(mockedLoaderStore.contains(key));
		
		mockedLoaderStore.expire(key);
		
		assertFalse(mockedLoaderStore.contains(key));
		
		EasyMock.verify(mockedLoader);
	}
	
	@Test
	public void expire_all_removes_all_elements() {
		EasyMock.replay(mockedLoader);
		
		TestKey key1 = new TestKey(10);
		TestKey key2 = new TestKey(50);
		
		mockedLoaderStore.put(key1, key1.getKey());
		mockedLoaderStore.put(key2, key2.getKey());
		
		assertTrue(mockedLoaderStore.contains(key1));
		assertTrue(mockedLoaderStore.contains(key2));
		
		mockedLoaderStore.expireAll();
		
		assertFalse(mockedLoaderStore.contains(key1));
		assertFalse(mockedLoaderStore.contains(key2));
		
		EasyMock.verify(mockedLoader);
	}
	
	@Test
	public void size_calculates_correctly() {
		EasyMock.replay(mockedLoader);
		
		assertEquals(0, mockedLoaderStore.size());
		
		mockedLoaderStore.put(new TestKey(10), 10);
		assertEquals(1, mockedLoaderStore.size());

		mockedLoaderStore.put(new TestKey(20), 20);
		assertEquals(2, mockedLoaderStore.size());
		
		// note key didn't change so size shouldn't either
		mockedLoaderStore.put(new TestKey(20), 30);
		assertEquals(2, mockedLoaderStore.size());
		
		mockedLoaderStore.expireAll();
		
		assertEquals(0, mockedLoaderStore.size());
		
		EasyMock.verify(mockedLoader);
	}
}
