package com.n4systems.util.chart;

import java.io.Serializable;


// NOTE : don't use primitives for fields....use objects so they can be null. 
//  if a field is null it will not be rendered.
// e.g.   public boolean showImage;    // BOO-URNS!
//        public Boolean showImage;    // Yay

@SuppressWarnings("serial")
public class FlotOptions<X> implements Serializable {

	public static transient final String[] QUARTER_NAMES = new String[]{"Q1", "Q1", "Q1", "Q2", "Q2", "Q2", "Q3", "Q3", "Q3", "Q4", "Q4", "Q4"};
	public static transient final String[] MONTH_NAMES = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};		
	
	public Series series = new Series();
	public Lines lines = new Lines();
	public Points points = new Points();
	public Axis yaxis = new Axis();
	public Axis xaxis = new Axis();
	public Grid grid = new Grid();
	public Pan pan = new Pan();
	public Bars bars = new Bars();
	public Legend legend = new Legend();
	public String[] colors;
	
	public class Lines implements Serializable { 
		public Boolean show;
		public Boolean fill;
		public String fillColor;
		public Boolean steps;
	}
	
	public class Points  implements Serializable { 
		public Boolean show;
		public String symbol = "circle";
		public Integer radius = 3;
	}
	
	public class Axis implements Serializable  {
		public Long[] panRange;	
		public Long min;		
		public String mode;
		public String timeformat = "%m/%y";
		public String decimals;
		public String[] monthNames;
		public String[][] ticks;
		public Integer tickLength;
		public String color = "#999999";
		public Integer labelWidth;
		public String[] minTickSize;
		public String[] tickSize;		
	}
	
	public class Grid implements Serializable  {
		public Boolean hoverable;
		public Boolean clickable;	
		public String colour;
		public String tickColor;
		public Boolean show;
		public String borderColor = "#CDCDCD";
		public Integer borderWidth = 1; 
	}
	
	public class Pan implements Serializable  { 
		public Boolean interactive;
	}
	
	public class Series implements Serializable {
		public Long stack;
		public Lines lines = new Lines();
		public Bars bars = new Bars();
		public Points points = new Points();
	}
	
	public class Bars implements Serializable { 
		public Boolean show;
		public Double barWidth;
		public Boolean horizontal;
		public String align;
		public Integer lineWidth;
	}
	
	public class Legend implements Serializable { 
		public Boolean show;
		public String labelFormatter;
		public String position;
		public Integer margin = 4;
		public String backgroundColor;
		public String backgroundOpacity;
		public String container;
		public Integer noColumns;
	}
	
	// ------------------------------------------------------------------------------------

	public FlotOptions() { 
		xaxis.decimals = null;		
		yaxis.panRange = null;
		yaxis.min = null;
		yaxis.mode = null;
		yaxis.timeformat = null;
		yaxis.monthNames = null;
		colors = new String[] {"#32578B"};
	}
		 
}
