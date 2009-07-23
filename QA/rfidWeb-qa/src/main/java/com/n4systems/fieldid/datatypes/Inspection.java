package com.n4systems.fieldid.datatypes;

public class Inspection {
	String customer = null;
	String division = null;
	String location = null;
	String type = null;
	String inspector = null;
	String inspectionDate = null;	// date & time
	String scheduleFor = null;
	String book;
	// recommendations
	// deficiencies
	// proof test
	String comments = null;
	boolean printable = true;
	String productStatus = null;
	String nextInspectionDate = null;
	// attached files
	
	public Inspection() {
		super();
	}

	public void setCustomer(String s) {
		this.customer = s;
	}

	public void setDivision(String s) {
		this.division = s;
	}
	
	public void setLocation(String s) {
		this.location = s;
	}
	
	public void setType(String s) {
		this.type = s;
	}
	
	public void setInspector(String s) {
		this.inspector = s;
	}
	
	public void setInspectionDate(String s) {
		this.inspectionDate = s;
	}
	
	public void setScheduleFor(String s) {
		this.scheduleFor = s;
	}
	
	public void setBook(String s) {
		this.book = s;
	}
	
	public void setComments(String s) {
		this.comments = s;
	}
	
	public void setPrintable(boolean b) {
		this.printable = b;
	}
	
	public void setProductStatus(String s) {
		this.productStatus = s;
	}
	
	public void setNextInspectionDate(String s) {
		this.nextInspectionDate = s;
	}

	public String getCustomer() {
		return customer;
	}

	public String getDivision() {
		return division;
	}
	
	public String getLocation() {
		return location;
	}
	
	public String getType() {
		return type;
	}
	
	public String getInspector() {
		return inspector;
	}
	
	public String getInspectionDate() {
		return inspectionDate;
	}
	
	public String getScheduleFor() {
		return scheduleFor;
	}
	
	public String getBook() {
		return book;
	}
	
	public String getComments() {
		return comments;
	}
	
	public boolean getPrintable() {
		return printable;
	}
	
	public String getProductStatus() {
		return productStatus;
	}
	
	public String getNextInspectionDate() {
		return nextInspectionDate;
	}
}
