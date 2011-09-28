package com.n4systems.exporting.io;

import java.util.Map;

import jxl.write.WritableCell;
import jxl.write.WritableCellFeatures;
import jxl.write.WritableSheet;

import org.apache.commons.lang.StringUtils;

import com.n4systems.exporting.beanutils.CriteriaResultSerializationHandler;

public class EventExcelSheetManager extends ExcelSheetManager {
	private final String DEFICIENCIES = "Deficiencies";
	private final String RECOMMENDATIONS = "Recommendations";
	private final String EVENTS = "Events";
	private String[] sheetTitles = {EVENTS, RECOMMENDATIONS, DEFICIENCIES};			

	public EventExcelSheetManager() {
		super();		
		excelCellManager = new EventExcelCellManager();
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
	
	
	class EventExcelCellManager extends ExcelCellManager { 
		@Override
		public WritableCell getCell(int row, WritableSheet sheet, String[] titles, int titleIndex, Map<String, ?> rowMap) {
			// basically the same as default but we'll look for recommendations & deficiences and add them to comment of cell if they exist.
			WritableCell cell = super.getCell(row, sheet, titles, titleIndex, rowMap);
			
			String title = titles[titleIndex];
			StringBuffer comment = new StringBuffer();
			if (row > 0 && !title.endsWith(CriteriaResultSerializationHandler.RECOMMENDATIONS_SUFFIX) &&
					!title.endsWith(CriteriaResultSerializationHandler.DEFICIENCIES_SUFFIX)) {

				// lets look for one...providing this isn't already a :R or :D column.

				String deficiencyTitle = title+CriteriaResultSerializationHandler.DEFICIENCIES_SUFFIX;
				String recommendationTitle = title+CriteriaResultSerializationHandler.RECOMMENDATIONS_SUFFIX;
				Object r = rowMap.get(recommendationTitle);
				Object d = rowMap.get(deficiencyTitle);
				if (r!=null && StringUtils.isNotBlank(r.toString())) { 
					comment.append("recommend : " + rowMap.get(recommendationTitle)+"\n");
				}
				if (d!=null && StringUtils.isNotBlank(d.toString())) {  
					comment.append("deficiency : " + rowMap.get(deficiencyTitle));
				}
			}
			
			if (comment.length()>0) {
				WritableCellFeatures writableCellFeatures = new WritableCellFeatures();
				writableCellFeatures.setComment(comment.toString());
				cell.setCellFeatures(writableCellFeatures);
			}	
			return cell;
			
		}
	}
	
}












