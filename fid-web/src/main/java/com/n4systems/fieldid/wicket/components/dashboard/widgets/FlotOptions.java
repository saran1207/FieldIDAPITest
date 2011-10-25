package com.n4systems.fieldid.wicket.components.dashboard.widgets;

import java.io.Serializable;

@SuppressWarnings("serial")
public class FlotOptions implements Serializable {
	
	public Lines lines = new Lines();
	public Points points = new Points();
	public Axis yaxis = new Axis();
	public Axis xaxis = new Axis();
	public Grid grid = new Grid();
	public Pan pan = new Pan();

	class Lines implements Serializable { 
		public Boolean show = true;
	}
	
	class Points  implements Serializable { 
		public Boolean show = true;
	}
	
	class Axis implements Serializable  {
		public Long[] panRange = new Long[0];	//{952732800000L,1321660800000L};
		public Long min = Long.MAX_VALUE;			//1321660800000L-((1321660800000L-952732800000L)/2);
		public String mode = "time";
		public String timeFormat = "%b %d, %y";
		public String decimals = "0";
		public String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};				
	}
	
	class Grid implements Serializable  {
		public Boolean hoverable = true;
		public Boolean clickable = true;		
	}
	
	class Pan implements Serializable  { 
		public Boolean interactive = true;
	}

	public FlotOptions() { 
		xaxis.decimals = null;
		
		yaxis.panRange = null;
		yaxis.min = null;
		yaxis.mode = null;
		yaxis.timeFormat = null;
		yaxis.monthNames = null;
	}
	
}
