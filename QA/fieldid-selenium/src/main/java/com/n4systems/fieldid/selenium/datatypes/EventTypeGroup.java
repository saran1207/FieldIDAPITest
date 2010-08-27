package com.n4systems.fieldid.selenium.datatypes;

public class EventTypeGroup {

	private String name;
	
	private String reportName;
	
	private String pdfReportStyle;
	
	private String observationReportStyle;
	
	public EventTypeGroup() {}

	public EventTypeGroup(String name, String reportName) {
		this(name, reportName, null, null);
	}

	public EventTypeGroup(String name, String reportName, String pdfReportStyle, String observationReportStyle) {
		this.name = name;
		this.reportName = reportName;
		this.pdfReportStyle = pdfReportStyle;
		this.observationReportStyle = observationReportStyle;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getPdfReportStyle() {
		return pdfReportStyle;
	}

	public void setPdfReportStyle(String pdfReportStyle) {
		this.pdfReportStyle = pdfReportStyle;
	}

	public String getObservationReportStyle() {
		return observationReportStyle;
	}

	public void setObservationReportStyle(String observationReportStyle) {
		this.observationReportStyle = observationReportStyle;
	}
	
	
}
