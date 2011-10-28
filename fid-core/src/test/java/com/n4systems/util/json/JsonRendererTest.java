package com.n4systems.util.json;


import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.TreeMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class JsonRendererTest {

	// TODO DD : add tests here.
	private JsonRenderer fixture = new JsonRenderer();
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test 
	public void testRender_SimpleObject() {
		Serializable bean = new FooBar();
		String render = fixture.render(bean);
		System.out.println(render);
	}

	@Test 
	public void testRender_CustomMapSerializer() {
		Serializable bean = new FooBar();
		TreeMap<FooBar,Date> map = new TreeMap<FooBar,Date>();
		map.put(new FooBar(), new Date());
		GsonBuilder gb = new GsonBuilder();
		gb.registerTypeAdapter(TreeMap.class, new TestSerializer());
		Gson gson = gb.create();
		String x = gson.toJson(map);
		System.out.println(x);
		
	}

	class FooBar implements Serializable {
		public Lines lines = new Lines();
		public Points points = new Points();
		public Axis yaxis = new Axis();
		public Axis xaxis = new Axis();
		public Grid grid = new Grid();
		public Pan pan = new Pan();

		class Lines { 
			public Boolean show = true;
		}
		
		class Points { 
			public Boolean show = true;
		}
		
		class Axis {
			public Long[] panRange = {952732800000L,1321660800000L};
			public Long min = 1321660800000L-((1321660800000L-952732800000L)/2);
			public String mode = "time";
			public String timeFormat = "%b %d, %y";
			public Integer decimals = 0;
			public String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};				
		}
		
		class Grid {
			public Boolean hoverable = true;
			public Boolean clickable = true;		
		}
		
		class Pan { 
			public Boolean interactive = true;
		}

		public FooBar() { 
			yaxis.decimals = 0;
		}
		
	}
	
	
	class TestSerializer implements JsonSerializer<TreeMap<FooBar,String>> {
		@Override
		public JsonElement serialize(TreeMap<FooBar,String> map, Type typeOfSrc, JsonSerializationContext context) {
			JsonArray a = new JsonArray();
			a.add(new JsonPrimitive(100));
			a.add(new JsonPrimitive(44));
	        return a;
		}
	}
		
	
		
	
}
