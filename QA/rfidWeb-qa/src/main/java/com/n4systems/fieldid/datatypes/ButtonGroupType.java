package com.n4systems.fieldid.datatypes;

public class ButtonGroupType {

	String name;
	final int numLabels = 6;
	String[] image = new String[numLabels];
	String[] label = new String[numLabels];
	String[] indicates = new String[numLabels];
	public final static String PASS_IMAGE = "btn0.png";	// green/check
	public final static String FAIL_IMAGE = "btn1.png";	// red/x
	public final static String NA_IMAGE = "btn2.png";	// red/green/blue/yellow
	public final static String PASS = "Pass";
	public final static String FAIL = "Fail";
	public final static String NA = "N/A";
	
	public ButtonGroupType() {
		super();
	}
	
	public ButtonGroupType(String name) {
		this.name = name;
	}
	
	public void setLabel(int position, String label) {
		if(position <  numLabels) {
			this.label[position] = label;
		}
	}
	
	public void setLabel(int position, String label, String indicates) {
		if(position <  numLabels) {
			this.label[position] = label;
			this.indicates[position] = indicates;
		}
	}
	
	public void setLabel(int position, String label, String indicates, String image) {
		if(position <  numLabels) {
			this.image[position] = image;
			this.label[position] = label;
			this.indicates[position] = indicates;
		}
	}
	
	public String getLabel(int position) {
		String result = null;
		if(position < numLabels) {
			result = label[position];
		}
		return result;
	}

	public String getIndicates(int position) {
		String result = null;
		if(position < numLabels) {
			result = indicates[position];
		}
		return result;
	}
}
