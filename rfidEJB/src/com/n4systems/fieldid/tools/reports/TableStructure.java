package com.n4systems.fieldid.tools.reports;

import java.util.Collection;

/**
 * Use {@link ColumnMapping} and {@link ColumnMappingFactory}
 */
@Deprecated
public class TableStructure {

	private Collection<ColumnDefinition> columns;

	public TableStructure() {}
	
	public TableStructure(Collection<ColumnDefinition> columns) {
		this.columns = columns;
	}
	
	protected static ColumnDefinition getSerialNumber(boolean usingSerialNumber) {
		if( usingSerialNumber ) {
			return ColumnDefinition.SerialNumber;
		} else {
			return ColumnDefinition.ReelID;
		}
	}

	public Collection<ColumnDefinition> getColumns() {
		return columns;
	}

	public void setColumns(Collection<ColumnDefinition> columns) {
		this.columns = columns;
	}

}