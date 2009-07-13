package com.n4systems.fieldid.datatypes;

import java.util.ArrayList;
import java.util.List;

public class Notification {

	String name = null;
	String jobSite = null;
	String customer = null;
	String division = null;
	String assetTypes = null;
	String eventTypes = null;
	String frequency = null;
	String forEventsStarting = null;
	String forTheNext = null;
	List<String> emailAddresses = new ArrayList<String>();
	
	public Notification() {
	}

	public void setName(String s) {
		name = s;
	}
	
	public void setJobSite(String s) {
		jobSite = s;
	}
	
	public void setCustomer(String s) {
		customer = s;
	}
	
	public void setDivision(String s) {
		division = s;
	}
	
	public void setAssetTypes(String s) {
		assetTypes = s;
	}
	
	public void setEventTypes(String s) {
		eventTypes = s;
	}
	
	public void setFrequency(String s) {
		frequency = s;
	}
	
	public void setForEventsStarting(String s) {
		forEventsStarting = s;
	}
	
	public void setForTheNext(String s) {
		forTheNext = s;
	}
	
	public void setEmailAddresses(List<String> s) {
		emailAddresses = s;
	}
	
	public void addEmailAddress(String s) {
		emailAddresses.add(s);
	}

	public String getName() {
		return this.name;
	}
	
	public String getJobSite() {
		return this.jobSite;
	}
	
	public String getCustomer() {
		return this.customer;
	}
	
	public String getDivision() {
		return this.division;
	}
	
	public String getAssetTypes() {
		return this.assetTypes;
	}
	
	public String getEventTypes() {
		return this.eventTypes;
	}
	
	public String getFrequency() {
		return this.frequency;
	}
	
	public String getForEventsStarting() {
		return this.forEventsStarting;
	}
	
	public String getForTheNext() {
		return this.forTheNext;
	}
	
	public List<String> getEmailAddresses() {
		return this.emailAddresses;
	}
	
	public String getEmailAddress(int i) {
		return emailAddresses.get(i);
	}
}
