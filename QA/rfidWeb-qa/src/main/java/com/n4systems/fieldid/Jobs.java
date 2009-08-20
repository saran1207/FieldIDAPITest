package com.n4systems.fieldid;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import com.n4systems.fieldid.datatypes.Job;
import com.n4systems.fieldid.datatypes.ScheduleSearchCriteria;
import com.n4systems.fieldid.datatypes.SystemUser;

import static watij.finders.FinderFactory.*;
import watij.elements.Button;
import watij.elements.Checkbox;
import watij.elements.HtmlElement;
import watij.elements.Label;
import watij.elements.Labels;
import watij.elements.Link;
import watij.elements.Links;
import watij.elements.Option;
import watij.elements.Options;
import watij.elements.Radio;
import watij.elements.SelectList;
import watij.elements.Span;
import watij.elements.Spans;
import watij.elements.TextField;
import watij.finders.Finder;
import watij.runtime.ie.IE;
import junit.framework.TestCase;

public class Jobs extends TestCase {
	
	IE ie = null;
	FieldIDMisc misc;
	Properties p;
	InputStream in;
	String propertyFile = "jobs.properties";
	private Finder jobsFinder;
	private Finder jobsContentHeaderFinder;
	private Finder onlyMyJobsLinkFinder;
	private Finder allJobsLinkFinder;
	private Finder addJobLinkFinder;
	private Finder viewAllJobsLinkFinder;
	private Finder jobTypeLabelFinder;
	private Finder jobIDTextFieldFinder;
	private Finder jobTitleTextFieldFinder;
	private Finder customerFieldFinder;
	private Finder divisionFieldFinder;
	private Finder statusFieldFinder;
	private Finder openCheckboxFinder;
	private Finder descriptionFieldFinder;
	private Finder dateStartedFieldFinder;
	private Finder estimatedCompletionDateFieldFinder;
	private Finder actualCompletionDateFieldFinder;
	private Finder durationFieldFinder;
	private Finder poNumberFieldFinder;
	private Finder workPerformedFieldFinder;
	private Finder addJobSaveButtonFinder;
	private Finder jobContentHeaderFinder;
	private Finder jobTitleLinksFinder;
	private Finder eventJobTitleLinksFinder;
	private Finder assetJobTitleLinksFinder;
	private Finder assignResourceLinkFinder;
	private Finder assignResourceEmployeeSelectListFinder;
	private Finder assignEmployeeResourceButtonFinder;
	private Finder assignedResourcesFinder;
	private Finder jobEventsLinkFinder;
	private Finder jobEventsAddEventLinkFinder;
	private Finder scheduleStatusFinder;
	private Finder serialNumberFinder;
	private Finder customerFinder;
	private Finder divisionFinder;
	private Finder eventTypeGroupFinder;
	private Finder productTypeFinder;
	private Finder productStatusFinder;
	private Finder fromDateFinder;
	private Finder toDateFinder;
	private Finder scheduleCriteriaRunButtonFinder;
	private Finder scheduleCriteriaAssignAllToJobFinder;
	
	public Jobs(IE ie) {
		this.ie = ie;
		try {
			misc = new FieldIDMisc(ie);
			in = new FileInputStream(propertyFile);
			p = new Properties();
			p.load(in);
			scheduleCriteriaAssignAllToJobFinder = xpath(p.getProperty("assignalltojob"));
			scheduleCriteriaRunButtonFinder = xpath(p.getProperty("setscheduleschedulecriteriarunbutton"));
			scheduleStatusFinder = xpath(p.getProperty("setscheduleschedulestatus"));
			serialNumberFinder = xpath(p.getProperty("setscheduleserialnumber"));
			customerFinder = xpath(p.getProperty("setschedulecustomer"));
			divisionFinder = xpath(p.getProperty("setscheduledivision"));
			eventTypeGroupFinder = xpath(p.getProperty("setscheduleeventtypegroup"));
			productTypeFinder = xpath(p.getProperty("setscheduleproducttype"));
			productStatusFinder = xpath(p.getProperty("setscheduleproductstatus"));
			fromDateFinder = xpath(p.getProperty("setschedulefromdate"));
			toDateFinder = xpath(p.getProperty("setscheduletodate"));
			jobEventsAddEventLinkFinder = xpath(p.getProperty("jobeventsaddeventlink"));
			jobEventsLinkFinder = xpath(p.getProperty("jobevents"));
			jobsFinder = xpath(p.getProperty("link"));
			jobsContentHeaderFinder = xpath(p.getProperty("contentheader"));
			onlyMyJobsLinkFinder = xpath(p.getProperty("onlyjobsiamassignedto"));
			allJobsLinkFinder = xpath(p.getProperty("alljobslink"));
			addJobLinkFinder = xpath(p.getProperty("addjoblink"));
			viewAllJobsLinkFinder = xpath(p.getProperty("viewalljobslink"));
			jobTypeLabelFinder = xpath(p.getProperty("whattypeofjobisthis"));
			jobIDTextFieldFinder = xpath(p.getProperty("jobid"));
			jobTitleTextFieldFinder = xpath(p.getProperty("jobtitle"));
			customerFieldFinder = xpath(p.getProperty("customer"));
			divisionFieldFinder = xpath(p.getProperty("division"));
			statusFieldFinder = xpath(p.getProperty("status"));
			openCheckboxFinder = xpath(p.getProperty("open"));
			descriptionFieldFinder = xpath(p.getProperty("description"));
			dateStartedFieldFinder = xpath(p.getProperty("datestarted"));
			estimatedCompletionDateFieldFinder = xpath(p.getProperty("estimatecompletiondate"));
			actualCompletionDateFieldFinder = xpath(p.getProperty("actualcompletiondate"));
			durationFieldFinder = xpath(p.getProperty("duration"));
			poNumberFieldFinder = xpath(p.getProperty("ponumber"));
			workPerformedFieldFinder = xpath(p.getProperty("workperformed"));
			addJobSaveButtonFinder = xpath(p.getProperty("addjobsavebutton"));
			jobContentHeaderFinder = xpath(p.getProperty("jobpagecontentheader"));
			jobTitleLinksFinder = xpath(p.getProperty("jobtitlelinks"));
			eventJobTitleLinksFinder = xpath(p.getProperty("eventjobtitlelinks"));
			assetJobTitleLinksFinder = xpath(p.getProperty("assetjobtitlelinks"));
			assignResourceLinkFinder = xpath(p.getProperty("assignresourcelink"));
			assignResourceEmployeeSelectListFinder = xpath(p.getProperty("assignresourceemployeeselectlist"));
			assignEmployeeResourceButtonFinder = xpath(p.getProperty("assignemployeeresourcebutton"));
			assignedResourcesFinder = xpath(p.getProperty("assignedresources"));
		} catch (FileNotFoundException e) {
			fail("Could not find the file '" + propertyFile + "' when initializing Home class");
		} catch (IOException e) {
			fail("File I/O error while trying to load '" + propertyFile + "'.");
		} catch (Exception e) {
			fail("Unknown exception");
		}
	}
	
	public void gotoJobs() throws Exception {
		Link l = ie.link(jobsFinder);
		assertTrue("Could not find the link to Jobs", l.exists());
		l.click();
		checkPageContentHeader();
	}

	private void checkPageContentHeader() throws Exception {
		HtmlElement contentHeader = ie.htmlElement(jobsContentHeaderFinder);
		assertTrue("Could not find Jobs page content header '" + p.getProperty("contentheader") + "'", contentHeader.exists());
	}
	
	public void validate(SystemUser u) throws Exception {
		gotoJobs();
		gotoOnlyJobsIamAssignedTo();
		gotoAllJobs();
		gotoAddJob();
		int n = misc.getRandomInteger();
		String jobID = "val-event-" + n;
		String jobTitle = "val-event-" + n;
		String jobType = Job.event;
		Job j = new Job(jobID, jobTitle, jobType);
		setJob(j);
		addJob(jobTitle);
		gotoAddJob();
		int n2 = misc.getRandomInteger();
		String jobID2 = "val-event-" + n2;
		String jobTitle2 = "val-event-" + n2;
		String jobType2 = Job.asset;
		Job j2 = new Job(jobID2, jobTitle2, jobType2);
		String status = "" + n2;
		String description = status;
		String now = misc.getDateStringWithTime();
		String duration = status;
		String poNumber = status;
		String workPerformed = status;
		j2.setStatus(status);
		j2.setDescription(description);
		j2.setDateStarted(now);
		j2.setDateEstimateCompleted(now);
		j2.setDateActualCompleted(now);
		j2.setDuration(duration);
		j2.setPONumber(poNumber);
		j2.setWorkPerformed(workPerformed);
		setJob(j2);
		addJob(jobTitle2);
		gotoViewAll();
		List<String> jobTitles = getJobTitles();
		assertTrue("Could not find '" + jobTitle + "' in list of jobs", jobTitles.contains(jobTitle));
		assertTrue("Could not find '" + jobTitle2 + "' in list of jobs", jobTitles.contains(jobTitle2));
		if(!misc.isFirstPage())
			misc.gotoFirstPage();
		List<String> eventJobTitles = getEventJobTitles();
		assertTrue("Could not find '" + jobTitle + "' in list of event jobs", eventJobTitles.contains(jobTitle));
		if(!misc.isFirstPage())
			misc.gotoFirstPage();
		List<String> assetJobTitles = getAssetJobTitles();
		assertTrue("Could not find '" + jobTitle2 + "' in list of asset jobs", assetJobTitles.contains(jobTitle2));
		if(!misc.isFirstPage())
			misc.gotoFirstPage();
		gotoJob(jobTitle);
		FieldIDMisc.stopMonitor();
		gotoAddResource(jobTitle);
		String qauser = u.getFirstName() + " " + u.getLastName();
		List<String> employees = getEmployeesFromAddResourceList();
		assertTrue("Could not find the user '" + qauser + "' on list of assignable resources", employees.contains(qauser));
		List<String> assigned = getAssignedEmployees();
		Iterator<String> i = assigned.iterator();
		while(i.hasNext()) {
			String s = i.next();
			assertFalse("Assigned employee '" + s + "' is still available on the employee list of assignable resources", employees.contains(s));
		}
		addEmployeeResource(qauser);
		FieldIDMisc.startMonitor();
	}

	public List<String> getAssignedEmployees() throws Exception {
		List<String> results = new ArrayList<String>();
		Spans assignedEmployees = ie.spans(assignedResourcesFinder);
		Iterator<Span> i = assignedEmployees.iterator();
		while(i.hasNext()) {
			Span employee = i.next();
			String s = employee.text().trim();
			results.add(s);
		}
		return results;
	}

	public void addEmployeeResource(String employeeName) throws Exception {
		SelectList employeeList = ie.selectList(assignResourceEmployeeSelectListFinder);
		assertTrue("Could not find the list of employees for assign resource", employeeList.exists());
		Option o = employeeList.option(xpath("OPTION[contains(text(),'" + employeeName + "')]"));
		assertTrue("Could not find employee '" + employeeName + "' in list of employees", o.exists());
		o.select();
		Button assign = ie.button(assignEmployeeResourceButtonFinder);
		assertTrue("Could not find the button to assign an employee to a job", assign.exists());
		assign.click();
		misc.waitForJavascript();
		misc.checkForErrorMessagesOnCurrentPage();
	}

	public List<String> getEmployeesFromAddResourceList() throws Exception {
		List<String> results = new ArrayList<String>();
		SelectList employeeList = ie.selectList(assignResourceEmployeeSelectListFinder);
		assertTrue("Could not find the list of employees for assign resource", employeeList.exists());
		Options employees = employeeList.options();
		Iterator<Option> i = employees.iterator();
		while(i.hasNext()) {
			Option employee = i.next();
			String s = employee.text();
			results.add(s);
		}
		return results;
	}

	public void gotoAddResource(String jobTitle) throws Exception {
		Link l = ie.link(assignResourceLinkFinder);
		assertTrue("Could not find the link to add a resource", l.exists());
		l.click();
		misc.checkForErrorMessagesOnCurrentPage();
		checkJobPageContentHeader(jobTitle);
	}

	public void gotoJob(String jobTitle) throws Exception {
		int pages = misc.getNumberOfPages();
		for(int page = 0; page < pages; page++) {
			Link l = ie.link(text(jobTitle));
			if(l.exists()) {
				l.click();
				misc.checkForErrorMessagesOnCurrentPage();
				checkJobPageContentHeader(jobTitle);
				return;
			}
			if(!misc.isLastPage())
				misc.gotoNextPage();
		}
		fail("Did not find a link for '" + jobTitle + "'");
	}

	public List<String> getJobTitles() throws Exception {
		List<String> results = getJobTitles(jobTitleLinksFinder);
		return results;
	}

	public List<String> getEventJobTitles() throws Exception {
		List<String> results = getJobTitles(eventJobTitleLinksFinder);
		return results;
	}

	public List<String> getAssetJobTitles() throws Exception {
		List<String> results = getJobTitles(assetJobTitleLinksFinder);
		return results;
	}

	private List<String> getJobTitles(Finder f) throws Exception {
		List<String> results = new ArrayList<String>();
		int pages = misc.getNumberOfPages();
		for(int page = 0; page < pages; page++) {
			List<String> tmp = getJobTitlesCurrentPage(f);
			results.addAll(tmp);
			if(!misc.isLastPage())
				misc.gotoNextPage();
		}
		return results;
	}

	private List<String> getJobTitlesCurrentPage(Finder f) throws Exception {
		List<String> tmp = new ArrayList<String>();
		FieldIDMisc.stopMonitor();
		Links jobTitles = ie.links(f);
		assertNotNull(jobTitles);
		Iterator<Link> i = jobTitles.iterator();
		while(i.hasNext()) {
			Link l = i.next();
			String title = l.text().trim();
			tmp.add(title);
		}
		FieldIDMisc.startMonitor();
		return tmp;
	}

	public void gotoViewAll() throws Exception {
		Link l = ie.link(viewAllJobsLinkFinder);
		assertTrue("Could not find the link to 'View All' jobs", l.exists());
		l.click();
		checkPageContentHeader();
	}

	public void gotoAddJob() throws Exception {
		Link l = ie.link(addJobLinkFinder);
		assertTrue("Could not find the link to 'Add' a job", l.exists());
		l.click();
		checkPageContentHeader();
	}

	public void gotoAllJobs() throws Exception {
		Link l = ie.link(allJobsLinkFinder);
		assertTrue("Could not find the link to 'All Jobs'", l.exists());
		l.click();
		checkPageContentHeader();
	}

	public void gotoOnlyJobsIamAssignedTo() throws Exception {
		Link l = ie.link(onlyMyJobsLinkFinder);
		assertTrue("Could not find the link to 'Only Jobs I am assigned to'", l.exists());
		l.click();
		checkPageContentHeader();
	}

	public void setJob(Job j) throws Exception {
		FieldIDMisc.stopMonitor();
		Radio jobType = null;
		Labels jobTypeLabels = ie.labels(jobTypeLabelFinder);
		assertNotNull(jobTypeLabels);
		assertTrue("Could not find any labels for job types", jobTypeLabels.length() > 0);
		Iterator<Label> i = jobTypeLabels.iterator();
		while(i.hasNext()) {
			Label jt = i.next();
			assertNotNull(jt);
			if(jt.text().contains(j.getJobType())) {
				String id = jt.htmlFor();
				jobType = jt.radio(xpath("//INPUT[@id='" + id + "']"));
				break;
			}
		}
		assertNotNull("Could not find the job type '" + j.getJobType() + "'", jobType);
		jobType.set();
		TextField jobID = ie.textField(jobIDTextFieldFinder);
		assertTrue("Could not find the Job ID text field", jobID.exists());
		jobID.set(j.getJobID());
		TextField jobTitle = ie.textField(jobTitleTextFieldFinder);
		assertTrue("Could not find the Title text field", jobTitle.exists());
		jobTitle.set(j.getTitle());
		SelectList customer = ie.selectList(customerFieldFinder);
		assertTrue("Could not find the Customer select list", customer.exists());
		if(j.getCustomer() != null) {
			Option o = customer.option(text(j.getCustomer()));
			assertTrue("Could not find the customer '" + j.getCustomer() + "' in list", o.exists());
			o.select();
			misc.waitForJavascript();
		}
		SelectList division = ie.selectList(divisionFieldFinder);
		assertTrue("Could not find the Division select list", division.exists());
		if(j.getDivision() != null) {
			Option o = division.option(text(j.getDivision()));
			assertTrue("Could not find the division '" + j.getDivision() + "' in list", o.exists());
			o.select();
		}
		TextField status = ie.textField(statusFieldFinder);
		assertTrue("Could not find the Status text field", status.exists());
		if(j.getStatus() != null) {
			status.set(j.getStatus());
		}
		Checkbox open = ie.checkbox(openCheckboxFinder);
		assertTrue("Could not find the Open checkbox", open.exists());
		open.set(j.getOpen());
		TextField description = ie.textField(descriptionFieldFinder);
		assertTrue("Could not find the Description text field", description.exists());
		if(j.getDescription() != null) {
			description.set(j.getDescription());
		}
		TextField dateStarted = ie.textField(dateStartedFieldFinder);
		assertTrue("Could not find the Date Started text field", dateStarted.exists());
		if(j.getDateStarted() != null) {
			dateStarted.set(j.getDateStarted());
		}
		TextField estimatedCompletionDate = ie.textField(estimatedCompletionDateFieldFinder);
		assertTrue("Could not find the Estimated Completion Date text field", estimatedCompletionDate.exists());
		if(j.getDateEstimateCompleted() != null) {
			estimatedCompletionDate.set(j.getDateEstimateCompleted());
		}
		TextField actualCompletionDate = ie.textField(actualCompletionDateFieldFinder);
		assertTrue("Could not find the Actual Completion Date text field", actualCompletionDate.exists());
		if(j.getDateActualCompleted() != null) {
			actualCompletionDate.set(j.getDateActualCompleted());
		}
		TextField duration = ie.textField(durationFieldFinder);
		assertTrue("Could not find the Duration text field", duration.exists());
		if(j.getDuration() != null) {
			duration.set(j.getDuration());
		}
		TextField poNumber = ie.textField(poNumberFieldFinder);
		assertTrue("Could not find the PO Number text field", poNumber.exists());
		if(j.getPONumber() != null) {
			poNumber.set(j.getPONumber());
		}
		TextField workPerformed = ie.textField(workPerformedFieldFinder);
		assertTrue("Could not find the Work Performed text field", workPerformed.exists());
		if(j.getWorkPerformed() != null) {
			workPerformed.set(j.getWorkPerformed());
		}
		FieldIDMisc.startMonitor();
	}

	public void addJob(String jobTitle) throws Exception {
		Button save = ie.button(addJobSaveButtonFinder);
		assertTrue("Could not find the Save button", save.exists());
		save.click();
		misc.checkForErrorMessagesOnCurrentPage();
		checkJobPageContentHeader(jobTitle);
	}

	private void checkJobPageContentHeader(String jobTitle) throws Exception {
		HtmlElement contentHeader = ie.htmlElement(jobContentHeaderFinder);
		assertTrue("Could not find Job page content header", contentHeader.exists());
		String s = contentHeader.text();
		assertTrue("Page content header does not contain job title '" + jobTitle + "'", s.contains(jobTitle));
	}

	private void checkJobPageContentHeader() throws Exception {
		HtmlElement contentHeader = ie.htmlElement(jobContentHeaderFinder);
		assertTrue("Could not find Job page content header", contentHeader.exists());
	}

	public void gotoEvents() throws Exception {
		Link l = ie.link(jobEventsLinkFinder);
		assertTrue("Could not find the link to Events", l.exists());
		l.click();
		checkJobPageContentHeader();
	}

	public void gotoAddEvent() throws Exception {
		Link l = ie.link(jobEventsAddEventLinkFinder);
		assertTrue("Could not find the link to add an event", l.exists());
		l.click();
		this.checkJobPageContentHeader();
	}

	public void setScheduleCriteria(ScheduleSearchCriteria sc) throws Exception {
		assertNotNull(sc);
		FieldIDMisc.stopMonitor();
		String s;
		SelectList scheduleStatus = ie.selectList(scheduleStatusFinder);
		assertTrue("Could not find the Schedule Status select list", scheduleStatus.exists());
		s = sc.getScheduleStatus();
		if(s != null) {
			Option o = scheduleStatus.option(text(s));
			assertTrue("Could not find the option '" + s + "' in list.", o.exists());
			o.select();
		}
		TextField serialNumber = ie.textField(serialNumberFinder);
		assertTrue("Could not find the Serial Number text field", serialNumber.exists());
		s = sc.getSerialNumber();
		if(s != null) {
			serialNumber.set(s);
		}
		SelectList customer = ie.selectList(customerFinder);
		assertTrue("Could not find the Customer select list", customer.exists());
		s = sc.getCustomer();
		if(s != null) {
			Option o = customer.option(text(s));
			assertTrue("Could not find the option '" + s + "' in list.", o.exists());
			o.select();
			misc.waitForJavascript();
		}
		SelectList division = ie.selectList(divisionFinder);
		assertTrue("Could not find the Division select list", division.exists());
		s = sc.getDivision();
		if(s != null) {
			Option o = division.option(text(s));
			assertTrue("Could not find the option '" + s + "' in list.", o.exists());
			o.select();
		}
		SelectList eventTypeGroup = ie.selectList(eventTypeGroupFinder);
		assertTrue("Could not find the Event Type Group select list", eventTypeGroup.exists());
		s = sc.getEventTypeGroup();
		if(s != null) {
			Option o = eventTypeGroup.option(text(s));
			assertTrue("Could not find the option '" + s + "' in list.", o.exists());
			o.select();
		}
		SelectList productType = ie.selectList(productTypeFinder);
		assertTrue("Could not find the Product Type select list", productType.exists());
		s = sc.getProductType();
		if(s != null) {
			Option o = productType.option(text(s));
			assertTrue("Could not find the option '" + s + "' in list.", o.exists());
			o.select();
		}
		SelectList productStatus = ie.selectList(productStatusFinder);
		assertTrue("Could not find the Product Status select list", productStatus.exists());
		s = sc.getProductStatus();
		if(s != null) {
			Option o = productStatus.option(text(s));
			assertTrue("Could not find the option '" + s + "' in list.", o.exists());
			o.select();
		}
		TextField fromDate = ie.textField(fromDateFinder);
		assertTrue("Could not find the From Date text field", fromDate.exists());
		s = sc.getFromDate();
		if(s != null) {
			fromDate.set(s);
		}
		TextField toDate = ie.textField(toDateFinder);
		assertTrue("Could not find the To Date text field", toDate.exists());
		s = sc.getToDate();
		if(s != null) {
			toDate.set(s);
		}
		FieldIDMisc.startMonitor();
	}

	public void gotoRunScheduleCriteria() throws Exception {
		Button run = ie.button(scheduleCriteriaRunButtonFinder);
		assertTrue("Could not find the Run button", run.exists());
		run.click();
		checkJobPageContentHeader();
	}

	public void assignAllToJob() throws Exception {
		Link l = ie.link(scheduleCriteriaAssignAllToJobFinder);
		assertTrue("Could not find the 'Assign all to Job' link", l.exists());
		l.click();
		misc.checkForErrorMessagesOnCurrentPage();
	}
}
