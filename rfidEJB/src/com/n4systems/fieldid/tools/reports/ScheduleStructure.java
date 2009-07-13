package com.n4systems.fieldid.tools.reports;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Use {@link ColumnMapping} and {@link ColumnMappingFactory}
 */
@Deprecated
public class ScheduleStructure extends TableStructure {
	
	public ScheduleStructure() {}
	
	public static ScheduleStructure newInstance(boolean isAnEndUser, boolean usingSerialNumber) {
		ScheduleStructure structure = new ScheduleStructure();
		
		Collection<ColumnDefinition> columns = new ArrayList<ColumnDefinition>();
		
		columns.add(getSerialNumber(usingSerialNumber));
		columns.add(ColumnDefinition.Description);
		
		if(isAnEndUser) {
			columns.add(ColumnDefinition.CustomerReferenceNumber);
		} else {
			columns.add(ColumnDefinition.EndUserName);
		}
		
		columns.add(ColumnDefinition.ProductType);
		columns.add(ColumnDefinition.InspectionType);
		columns.add(ColumnDefinition.ScheduleLastInspectionDate);
		columns.add(ColumnDefinition.NextInspectionDate);
		columns.add(ColumnDefinition.DaysPastDue);
		columns.add(ColumnDefinition.EditScheduleLink);
		
		structure.setColumns(columns);
		
		return structure;
	}
}
