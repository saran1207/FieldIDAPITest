package com.n4systems.model.savedreports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SavedReportAssignedToRemovalTest {

	private List<SavedReport> reportsList;
	private SavedReport reportNormal;
	private SavedReport reportWithColumnToRemove;
	private SavedReport reportWithCriteriaToRemove;
	static final String ASSIGNED_TO_COLUMN = "inspection_search_assignedto";
	static final String ASSIGNED_TO_CRITERIA = "assignedUser";
	
	private List<String> columns;
	private Map<String, String> criteria;
	
	@Before
	public void setup() {
		reportsList = new ArrayList<SavedReport>();
		criteria = new HashMap<String,String>();
		columns = new ArrayList<String>();
		
		reportNormal = new SavedReport();
		reportWithColumnToRemove = new SavedReport();
		reportWithCriteriaToRemove = new SavedReport();

		columns.add(ASSIGNED_TO_COLUMN);
		criteria.put(ASSIGNED_TO_CRITERIA, "123");
		
		reportWithColumnToRemove.setColumns(columns);
		reportWithCriteriaToRemove.setCriteria(criteria);
	}

	@Test
	public void should_return_an_empty_list_when_given_null_or_empty_report_list() {
		assertTrue(SavedReportAssignedToTrimmer.extractAssignedToReferences(reportsList).isEmpty());
		assertTrue(SavedReportAssignedToTrimmer.extractAssignedToReferences(null).isEmpty());
	}
	
	@Test
	public void should_return_an_empty_list_when_having_no_assigned_to_columns_or_criteria() {
		reportsList.add(reportNormal);
		assertTrue(SavedReportAssignedToTrimmer.extractAssignedToReferences(reportsList).isEmpty());
	}

	@Test
	public void should_return_a_report_that_has_an_assignedto_column() {
		reportsList.add(reportNormal);
		reportsList.add(reportWithColumnToRemove);
		assertEquals(SavedReportAssignedToTrimmer.extractAssignedToReferences(reportsList).size(), 1);
	}

	@Test
	public void should_return_a_report_that_has_an_assignedto_criteria() {
		reportsList.add(reportNormal);
		reportsList.add(reportWithCriteriaToRemove);
		assertEquals(SavedReportAssignedToTrimmer.extractAssignedToReferences(reportsList).size(), 1);
	}
	
	@Test
	public void should_return_two_reports_that_have_an_assignedto_criteria_and_column_respectively() {
		reportsList.add(reportNormal);
		reportsList.add(reportWithColumnToRemove);
		reportsList.add(reportWithCriteriaToRemove);
		assertEquals(SavedReportAssignedToTrimmer.extractAssignedToReferences(reportsList).size(), 2);
	}

}
