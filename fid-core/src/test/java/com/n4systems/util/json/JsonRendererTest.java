package com.n4systems.util.json;


import static org.junit.Assert.*;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class JsonRendererTest {

	private JsonRenderer fixture = new JsonRenderer();

	private GsonBuilder gb;
	
	@Before
	public void setUp() throws Exception {
		gb = new GsonBuilder();
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test 
	public void testRender_SimpleObject() {
		Serializable bean = new FooBar();
		String actual = fixture.render(bean);
		String expected = "{" +
							"\"a\":{\"" +
								"show\":true,\"" +
								"stringTwo\":\"twoFish\",\"" +
								"map\":{\"7\":\"Dec 31, 1969 7:00:00 PM\",\"22\":\"Dec 31, 1969 7:00:00 PM\"}},\"" +
							"b\":{\"" +
								"interactive\":true,\"" +
								"o\":[\"apple\",\"orange\"],\"aLong\":12837123}" +
							"}";		
		assertEquals(expected, actual);
	}

	@Test 
	public void testRender_CustomMapSerializer() {
		// TODO DD : add code to jsonRenderer to register adapters. (currently all done at construction time...makes it difficult to test).
		FooBar bean = new FooBar();
		gb.registerTypeAdapter(ImmutableMap.class, new TestSerializer());
		Gson gson = gb.create();
		String actual = gson.toJson(bean.a);
		// note how "map" is replaced by bogus value.
		String expected = "{\"show\":true,\"stringTwo\":\"twoFish\",\"map\":[100,44]}";		
		assertEquals(expected,actual);		
	}

	
	class FooBar implements Serializable {
		public A a = new A();
		public B b = new B();

		class A { 
			public Boolean show = true;
			public String stringOne = null;
			public String stringTwo = "twoFish";
			public ImmutableMap<Integer,Date> map = ImmutableMap.of(7, new Date(0), 22, new Date(0));
		}
		
		class B { 
			public Boolean interactive = true;
			public Object o = Lists.newArrayList("apple", "orange");
			public Integer nullInt = null;
			public Long aLong = 12837123L;			
		}

		public FooBar() { 
			
		}
		
	}
	
	
	class TestSerializer implements JsonSerializer<ImmutableMap<FooBar,String>> {
		@Override
		public JsonElement serialize(ImmutableMap<FooBar,String> map, Type typeOfSrc, JsonSerializationContext context) {
			JsonArray a = new JsonArray();
			a.add(new JsonPrimitive(100));
			a.add(new JsonPrimitive(44));
	        return a;
		}
	}
		
	
		
	
}
