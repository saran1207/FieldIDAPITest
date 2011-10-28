package com.n4systems.fieldid.wicket.components.dashboard.widgets;

//TODO DD : move this to wicket.pages pkg along with other stuff OR to general wicket.util.chart pkg?

import java.io.Serializable;
import java.util.List;

import com.n4systems.util.chart.ChartData;

// NOTE : don't use primitives for fields....use objects so they can be null. 
//  if a field is null it will not be rendered.
// e.g.   public boolean showImage;    // BOO-URNS!
//        public Boolean showImage;    // Yay

@SuppressWarnings("serial")
public class FlotOptions<X> implements Serializable {

	public Series series = new Series();
	public Lines lines = new Lines();
	public Points points = new Points();
	public Axis yaxis = new Axis();
	public Axis xaxis = new Axis();
	public Grid grid = new Grid();
	public Pan pan = new Pan();
	public Bars bars = new Bars();

	class Lines implements Serializable { 
		public Boolean show;
	}
	
	class Points  implements Serializable { 
		public Boolean show;
	}
	
	class Axis implements Serializable  {
		public Long[] panRange;	
		public Long min;		
		public String mode;
		public String timeFormat;
		public String decimals;
		public String[] monthNames;
		public String[][] ticks;
		public Integer tickLength;				
	}
	
	class Grid implements Serializable  {
		public Boolean hoverable;
		public Boolean clickable;	
		public String colour;
		public String tickColor;
		public Boolean show;
	}
	
	class Pan implements Serializable  { 
		public Boolean interactive;
	}
	
	class Series implements Serializable {
		public Long stack;
		public Lines lines;
		public Bars bars;		
	}
	
	class Bars implements Serializable { 
		public Boolean show;
		public Double barWidth;
		public Boolean horizontal;
	}

	public FlotOptions() { 
		xaxis.decimals = null;		
		yaxis.panRange = null;
		yaxis.min = null;
		yaxis.mode = null;
		yaxis.timeFormat = null;
		yaxis.monthNames = null;
	}
	
	public FlotOptions<X> update(List<ChartData<X>> list) { 
		return this;
	}
	 
}
