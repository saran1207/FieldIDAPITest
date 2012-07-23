package com.n4systems.util.chart;

import java.io.Serializable;


/**
 *  NOTE : don't use primitives for fields....use objects so they can be null.
 *  
 *     if a field is null it will not be rendered.
 * e.g.   public boolean showImage;    // BOO-URNS!
 *        public Boolean showImage;    // Yay
 *
 * in general, think of this as a javascript object.  this is why it is laid out with a bunch of public fields in inner classes     
 *   and why objects are used instead of primitives.   that way you can more easily use a javascript like syntax. 
 * for example : 
 *    options.xaxis.color = "blue";       // looks like javascript.  instead of getXaxis().setColor(Color.BLUE);   
 *    options.series.points.show = true;  
 *    options.lines.lineWidth = null;     // note that null variables won't be serialized. 
 *    
 * so if you see something in the FLOT API documentation that requires you set a property, 
 * 1 : add the appropriate subclasses.   e.g. if you need options.help.faq, then create a "Help" inner class 
 * 2 : add the required field.                if you need options.help.faq, then add public String[] faq.     (or whatever faq requires). 
 * 3 : set this value in the constructor or in the hook when options are updated. 
 * 
 * if you need to add your own custom, non-FLOT option then i'd recommend a custom sub-object. 
 *   e.g. options.fieldid.assetId          // put all custom options under a new "fieldId" class.       
 *
 * if you add values to this field that aren't part of the options, then MARK THEM AS TRANSIENT. 
 * e.g.   public int tempCounter = 0;         		BOO!      
 * 		  public transient int tempCounter = 0;     YAY!      
 *     
 */

@SuppressWarnings("serial")
public class FlotOptions<X> implements Serializable {

	public static transient final String[] QUARTER_NAMES = new String[]{"Q1", "Q1", "Q1", "Q2", "Q2", "Q2", "Q3", "Q3", "Q3", "Q4", "Q4", "Q4"};
	public static transient final String[] MONTH_NAMES = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    public static final String TOOLTIP_WITH_DAY = "<p>{month} {day}, {year}: <b>{y}</b></p>";
    public static final String TOOLTIP_WITHOUT_DAY = "<p>{month} {year}: <b>{y}</b></p>";
    public static final String TOOLTIP_WEEK = "<p>{month} {day}-{weekEndDay}, {year}: <b>{y}</b></p>";
    public static final String TOOLTIP_YEAR = "<p>{year}: <b>{y}</b></p>";

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
    public String tooltipFormat = TOOLTIP_WITH_DAY;
    public FieldIdOptions fieldIdOptions = new FieldIdOptions();
	
	
	public static class Lines implements Serializable { 
		public Boolean show;
		public Boolean fill;
		public String fillColor;
		public Boolean steps;
		public Integer lineWidth;
        public Boolean clickable;  // custom attribute. handled by dashboard.js, not by FLOT engine.
    }
	
	public static class Points  implements Serializable { 
		public Boolean show;
		public String symbol = "circle";
		public Integer radius = 3;
	}
	
	public static class Axis implements Serializable  {
		public Long[] panRange;
		public Long min;
		public String mode;
		public String timeformat = "%m/%y";
        public Long tickDecimals;
		public String[] monthNames;
		public String[][] ticks;
		public Integer tickLength;
		public String color = "#999999";
		public Integer labelWidth;
		public String[] minTickSize;
		public String[] tickSize;		
	}
	
	public static class Grid implements Serializable  {
		public Boolean hoverable;
		public Boolean clickable;	
		public String colour;
		public String tickColor;
		public Boolean show;
		public String borderColor = "#CDCDCD";
		public Integer borderWidth = 1; 
	}
	
	public static class Pan implements Serializable  { 
		public Boolean interactive;
	}
	
	public static class Series implements Serializable {
		public Long stack;
		public Lines lines = new Lines();
		public Bars bars = new Bars();
		public Points points = new Points();
		public Integer shadowSize;
	}
	
	public static class Bars implements Serializable { 
		public Boolean show;
		public Double barWidth;
		public Boolean horizontal;
		public String align;
		public Integer lineWidth;
		public Boolean clickable;
	}
	
	public static class Legend implements Serializable { 
		public Boolean show;
		public String labelFormatter;
		public String position;
		public Integer margin = 4;
		public String backgroundColor;
		public String backgroundOpacity;
		public String container;
		public Integer noColumns;
	}
	
	// any non-FLOT stuff should be put here so it's easier to understand and port/migrate. 
	public static class FieldIdOptions implements Serializable { 
		public Boolean clickable = false;
		public String url;
		public Long widgetConfigId;
		public String clicked;		
	}
	
	// ------------------------------------------------------------------------------------

	public FlotOptions() { 
		yaxis.panRange = null;
		yaxis.mode = null;
		yaxis.timeformat = null;
		yaxis.monthNames = null;
        yaxis.min = 0L;
        yaxis.tickDecimals = 0L;
		colors = new String[] {"#32578B"};
	}
	
	

}
