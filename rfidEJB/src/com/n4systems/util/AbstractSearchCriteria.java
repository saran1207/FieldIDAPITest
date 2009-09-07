package com.n4systems.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.n4systems.fieldid.tools.reports.ColumnDefinition;
import com.n4systems.fieldid.tools.reports.TableStructure;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

/**
 * Use {@link SearchContainer}
 */
@Deprecated
abstract public class AbstractSearchCriteria<T> {
	private String searchId;
	private Long pageNumber;
	
	private SecurityFilter securityFilter;
	private Map<ColumnDefinition, String> columnMap;
	private TableStructure reportStructure;
	
	protected AbstractSearchCriteria(SecurityFilter filter, TableStructure reportStructure) {
		this.securityFilter = filter;
		this.reportStructure = reportStructure;
		this.columnMap = initColumnMap();
	}

	public SecurityFilter getSecurityFilter() {
		return securityFilter;
	}
	
	public Map<ColumnDefinition, String> getColumnMap() {
		return columnMap;
	}
	
	protected String blankValue(String value) {
		if( value == null || value.trim().equals("") ) {
			return null;
		}
		return value.trim();
	}

	public String getSearchId() {
		return searchId;
	}
	
	public void newSearchId() {
		searchId = String.valueOf(Math.abs((new Random()).nextLong()));
	}

	public Long getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Long pageNumber) {
		this.pageNumber = pageNumber;
	}

	public String getColumnMapping(ColumnDefinition column) {
		return columnMap.get(column);
	}
	
	public boolean hasColumnMapping(ColumnDefinition column) {
		return columnMap.containsKey(column);
	}

	public TableStructure getReportStructure() {
		return reportStructure;
	}
	
	public void setReportStructure(TableStructure reportStructure) {
		this.reportStructure = reportStructure;
	}
	
	public List<String> getReportColumns() {
		List<String> reportColumns = new ArrayList<String>();
		
		for(ColumnDefinition column: reportStructure.getColumns()) {
			if(column.isPrintable() && hasColumnMapping(column)) {
				reportColumns.add(getColumnMapping(column));
			}
		}
		
		return reportColumns;
	}
	
	abstract protected Map<ColumnDefinition, String> initColumnMap();
	abstract public QueryBuilder<T> getSearchQueryBuilder();
	abstract public QueryBuilder<T> getCountQueryBuilder();
	abstract public QueryBuilder<T> getIdQueryBuilder();

}