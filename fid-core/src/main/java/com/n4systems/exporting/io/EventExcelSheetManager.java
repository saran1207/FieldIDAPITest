package com.n4systems.exporting.io;

import jxl.write.WritableSheet;

import com.n4systems.exporting.beanutils.CriteriaResultSerializationHandler;

public class EventExcelSheetManager extends ExcelSheetManager {
	private final String DEFICIENCIES = "Deficiencies";
	private final String RECOMMENDATIONS = "Recommendations";
	private final String EVENTS = "Events";
	private String[] sheetTitles = {EVENTS, RECOMMENDATIONS, DEFICIENCIES};			

	public EventExcelSheetManager() {
		super();			
	}
	
	@Override
	protected WritableSheet getSheetForColumn(String columnTitle) {			
		if (columnTitle.endsWith(CriteriaResultSerializationHandler.RECOMMENDATIONS_SUFFIX)) {
			return super.getSheetByName(RECOMMENDATIONS);
		} else if (columnTitle.endsWith(CriteriaResultSerializationHandler.DEFICIENCIES_SUFFIX)) {
			return super.getSheetByName(DEFICIENCIES);
		}
		return super.getSheetByName(EVENTS);
	}
	
	@Override
	protected String[] getSheetTitles() {
		return sheetTitles;
	}
	
}












