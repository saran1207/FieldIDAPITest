package com.n4systems.fieldid;

import static watij.finders.FinderFactory.id;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import static watij.finders.FinderFactory.*;
import watij.elements.Button;
import watij.elements.HtmlElement;
import watij.elements.Link;
import watij.finders.Finder;
import watij.runtime.ie.IE;
import junit.framework.TestCase;

public class Schedule extends TestCase {
	
	IE ie = null;
	Properties p;
	InputStream in;
	Assets assets = null;
	String propertyFile = "schedule.properties";
	Finder scheduleFinder;
	Finder scheduleContentHeaderFinder;
	Finder scheduleSearchRunButtonFinder;
	Finder scheduleSearchResultsContentHeaderFinder;
	
	public Schedule(IE ie) {
		this.ie = ie;
		try {
			in = new FileInputStream(propertyFile);
			p = new Properties();
			p.load(in);
			assets = new Assets(ie);
			scheduleFinder = id(p.getProperty("link"));
			scheduleContentHeaderFinder = xpath(p.getProperty("contentheader"));
			scheduleSearchRunButtonFinder = id(p.getProperty("schedulerunbutton"));
			scheduleSearchResultsContentHeaderFinder = xpath(p.getProperty("searchresultscontentheader"));
		} catch (FileNotFoundException e) {
			fail("Could not find the file '" + propertyFile + "' when initializing Home class");
		} catch (IOException e) {
			fail("File I/O error while trying to load '" + propertyFile + "'.");
		} catch (Exception e) {
			fail("Unknown exception");
		}
	}
	
	public void gotoSchedule() throws Exception {
		Link scheduleLink = ie.link(scheduleFinder);
		assertTrue("Could not find the link to Schedule", scheduleLink.exists());
		scheduleLink.click();
		ie.waitUntilReady();
		checkSchedulePageContentHeader();
	}

	public void checkSchedulePageContentHeader() throws Exception {
		HtmlElement scheduleContentHeader = ie.htmlElement(scheduleContentHeaderFinder);
		assertTrue("Could not find Schedule page content header '" + p.getProperty("contentheader") + "'", scheduleContentHeader.exists());
	}

	public String getSelectDisplayColumnsHeader() throws Exception {
		return assets.getSelectDisplayColumnsHeader();
	}

	public void gotoScheduleSearchResults() throws Exception {
		Button run = ie.button(scheduleSearchRunButtonFinder);
		assertTrue("Could not find the Run button", run.exists());
		run.click();
		checkSchedulesearchResultsPageContentHeader();
	}

	private void checkSchedulesearchResultsPageContentHeader() throws Exception {
		HtmlElement scheduleSearchResultsContentHeader = ie.htmlElement(scheduleSearchResultsContentHeaderFinder);
		assertTrue("Could not find Schedule Search Results page content header '" + p.getProperty("searchresultscontentheader") + "'", scheduleSearchResultsContentHeader.exists());
	}

	public void expandScheduleSearchResultsSearchCriteria() throws Exception {
		assets.expandProductSearchResultsSearchCriteria();
	}
}
