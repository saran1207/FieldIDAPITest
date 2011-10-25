package com.n4systems.util.json;


import java.io.Serializable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
		
	
}
