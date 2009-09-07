package com.n4systems.fieldid.viewhelpers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.util.StringUtils;
import com.n4systems.util.persistence.search.BaseSearchDefiner;
import com.n4systems.util.persistence.search.SortTerm;
import com.n4systems.util.persistence.search.terms.DateRangeTerm;
import com.n4systems.util.persistence.search.terms.NullTerm;
import com.n4systems.util.persistence.search.terms.SearchTermDefiner;
import com.n4systems.util.persistence.search.terms.SimpleInTerm;
import com.n4systems.util.persistence.search.terms.SimpleTerm;
import com.n4systems.util.persistence.search.terms.SimpleTermOrNull;
import com.n4systems.util.persistence.search.terms.WildcardTerm;

abstract public class SearchContainer implements BaseSearchDefiner, Serializable {
	private static final long serialVersionUID = 1L;
	
	private final String searchClassIdField;
	private final Class<?> searchClass;
	private final SecurityFilter securityFilter;
	private final String[] joinColumns;
	private String searchId = String.valueOf(Math.abs((new Random()).nextLong()));
	private List<String> selectedColumns = new ArrayList<String>();
	private List<SortTerm> sortTerms = new ArrayList<SortTerm>();
	private List<SearchTermDefiner> searchTerms = new ArrayList<SearchTermDefiner>();
	private String sortColumn;
	private String sortDirection;
	
	public SearchContainer(Class<?> searchClass, String searchClassIdField, SecurityFilter securityFilter, String...joinColumns) {
		this.searchClass = searchClass;
		this.searchClassIdField = searchClassIdField;
		this.securityFilter = securityFilter;
		this.joinColumns = joinColumns;
		
	}
	
	// XXX - I don't like this system ... need to find something better which actually requires you to setup the search/sort terms
	abstract protected void evalSearchTerms();
	abstract protected String defaultSortColumn();
	abstract protected SortTerm.Direction defaultSortDirection();
	
	public Class<?> getSearchClass() {
		return searchClass;
	}

	public String getSearchClassIdField() {
		return searchClassIdField;
	}

	public SecurityFilter getSecurityFilter() {
		return securityFilter;
	}

	public String getSearchId() {
		return searchId;
	}
	
	public List<String> getSelectedColumns() {
		return selectedColumns;
	}

	public void setSelectedColumns(List<String> selectedColumns) {
		this.selectedColumns = selectedColumns;
	}
	
	public boolean isColumnSelected(String columnId) {
		return selectedColumns.contains(columnId);
	}
	
	public String getSortColumn() {
		return sortColumn;
	}

	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}

	public String getSortDirection() {
		return sortDirection;
	}

	public void setSortDirection(String sortDirection) {
		this.sortDirection = sortDirection;
	}

	public List<SortTerm> getSortTerms() {
		sortTerms.clear();
		
		// this persistence manager can take a list of sort terms but we currently only support one
		SortTerm.Direction dir = defaultSortDirection();
		if(sortColumn != null && sortDirection != null) {
			
			
			if(sortDirection.equals(SortTerm.Direction.ASC.getDisplayName())) {
				dir = SortTerm.Direction.ASC;
			} else if (sortDirection.equals(SortTerm.Direction.DESC.getDisplayName())) {
				dir = SortTerm.Direction.DESC;
			}
			
			// since the sort column is a path expression, it is possible for it to have meta tags used in filtering.
			// we need to remove those before sending it to hibernate search
			
			sortTerms.add(new SortTerm(sortColumn.replaceAll("\\{.*\\}", ""), dir));
		
		} else {	
			sortTerms.add(new SortTerm(defaultSortColumn(), dir));
			sortColumn = defaultSortColumn();
			sortDirection = defaultSortDirection().getDisplayName();
		}
		
		// to help with postgres randomly picking order on paginated result sets.
		sortTerms.add(new SortTerm("id", dir));
		return sortTerms;
	}
	
	public List<SearchTermDefiner> getSearchTerms() {
		searchTerms.clear();
		
		// always add in the tenant id ... this is really a term of last resort
		// as hibernate search needs at least one term.  Actual security is handled by 
		// a search filter.
		addSimpleTerm("tenant.id", securityFilter.getTenantId());
		evalSearchTerms();
		return searchTerms;
	}
	
	public String[] getJoinColumns() {
		return joinColumns;
	}
	
	protected <T> void addSimpleTermOrNull(String field, T value) {
		if (value != null) {
			searchTerms.add(new SimpleTermOrNull<T>(field, value));
		} 
	}
	
	protected <T> void addSimpleTerm(String field, T value) {
		if(value != null) {
			searchTerms.add(new SimpleTerm<T>(field, value));
		}
	}
	
	protected <T> void addSimpleInTerm(String field, List<T> value) {
		if(value != null) {
			searchTerms.add(new SimpleInTerm<T>(field, value));
		}
	}
	
	protected void addStringTerm(String field, String value) {
		addSimpleTerm(field, StringUtils.clean(value));
	}
	
	protected void addWildcardTerm(String field, String value) {
		String valueString = StringUtils.clean(value);
		
		if(valueString != null) {
			searchTerms.add(new WildcardTerm(field, valueString));
		}
	}
	
	protected void addNullTerm(String field) {
		searchTerms.add(new NullTerm(field));
	}
	
	protected void addDateRangeTerm(String field, Date start, Date end) {
		if (start != null || end != null) {
			searchTerms.add(new DateRangeTerm(field, start, end));
		}
	}
}
