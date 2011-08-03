package com.n4systems.fieldid.viewhelpers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.n4systems.fieldid.actions.api.LoaderFactoryProvider;
import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.OwnerAndDownFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.util.StringUtils;
import com.n4systems.util.persistence.QueryFilter;
import com.n4systems.util.persistence.search.BaseSearchDefiner;
import com.n4systems.util.persistence.search.JoinTerm;
import com.n4systems.util.persistence.search.SortDirection;
import com.n4systems.util.persistence.search.SortTerm;
import com.n4systems.util.persistence.search.JoinTerm.JoinTermType;
import com.n4systems.util.persistence.search.terms.DateRangeTerm;
import com.n4systems.util.persistence.search.terms.NullTerm;
import com.n4systems.util.persistence.search.terms.SearchTermDefiner;
import com.n4systems.util.persistence.search.terms.SimpleInTerm;
import com.n4systems.util.persistence.search.terms.SimpleTerm;
import com.n4systems.util.persistence.search.terms.SimpleTermOrNull;
import com.n4systems.util.persistence.search.terms.WildcardTerm;
import com.n4systems.util.selection.MultiIdSelection;

abstract public class SearchContainer implements BaseSearchDefiner, Serializable, AssetTypeFilteredSearchContainer, LoaderFactoryProvider {
	private static final String STRUTS_VALUE_WHEN_YOU_SELECT_NO_VALUES_FROM_CHECKBOXES = "false";

	private static final long serialVersionUID = 1L;
	
	private final LoaderFactory loaderFactory;
	private final String searchClassIdField;
	private final Class<?> searchClass;
	private final SecurityFilter securityFilter;
	protected final SystemSecurityGuard systemSecurityGuard;
	private String searchId = String.valueOf(Math.abs((new Random()).nextLong()));
	private List<String> selectedColumns = new ArrayList<String>();
	private List<SortTerm> sortTerms = new ArrayList<SortTerm>();
	private List<SearchTermDefiner> searchTerms = new ArrayList<SearchTermDefiner>();
	private List<QueryFilter> searchFilters = new ArrayList<QueryFilter>();
	private List<JoinTerm> joinTerms = new ArrayList<JoinTerm>();
	private String sortColumn;
	private String sortDirection;
    private Long sortColumnId;
    private String sortJoinExpression;

    private MultiIdSelection multiIdSelection = new MultiIdSelection();
	
	public SearchContainer(Class<?> searchClass, String searchClassIdField, SecurityFilter securityFilter, LoaderFactory loaderFactory, SystemSecurityGuard systemSecurityGuard) {
		this.searchClass = searchClass;
		this.searchClassIdField = searchClassIdField;
		this.securityFilter = securityFilter;
		this.loaderFactory = loaderFactory;
		this.systemSecurityGuard = systemSecurityGuard;
	}
	
	// XXX - I don't like this system ... need to find something better which actually requires you to setup the search/sort terms
	abstract protected void evalSearchTerms();
	abstract protected void evalSearchFilters();
	abstract protected void evalJoinTerms();
	abstract public String defaultSortColumn();
	abstract public SortDirection defaultSortDirection();
	
	abstract public Long getAssetType();
	abstract public Long getAssetTypeGroup();
	
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
		if (selectedColumns.size() == 1 && selectedColumns.get(0).equalsIgnoreCase(STRUTS_VALUE_WHEN_YOU_SELECT_NO_VALUES_FROM_CHECKBOXES)) {
			selectedColumns.clear();
		}
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
		SortDirection dir = defaultSortDirection();
		if(sortColumn != null && sortDirection != null) {

			if(sortDirection.equals(SortDirection.ASC.getDisplayName())) {
				dir = SortDirection.ASC;
			} else if (sortDirection.equals(SortDirection.DESC.getDisplayName())) {
				dir = SortDirection.DESC;
			}
			
			// since the sort column is a path expression, it is possible for it to have meta tags used in filtering.
			// we need to remove those before sending it to hibernate search

            if (sortJoinExpression == null) {
                sortTerms.add(new SortTerm(sortColumn.replaceAll("\\{.*\\}", ""), dir));
            } else {
                SortTerm sortTerm = new SortTerm(JoinTerm.DEFAULT_SORT_JOIN_ALIAS, dir);
                sortTerm.setAlwaysDropAlias(true);
                sortTerm.setFieldAfterAlias(sortColumn.substring(sortColumn.lastIndexOf(".") + 1));
                sortTerms.add(sortTerm);
            }

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
		evalSearchTerms();
		return searchTerms;
	}
	
	public List<JoinTerm> getJoinTerms() {
		joinTerms.clear();
		evalJoinTerms();
        if (sortJoinExpression != null) {
            addRequiredLeftJoin(sortJoinExpression, JoinTerm.DEFAULT_SORT_JOIN_ALIAS);
        }
		return joinTerms;
	}
	
	protected void addCustomTerm(SearchTermDefiner term) {
		searchTerms.add(term);
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
	
	protected void addWildcardOrStringTerm(String field, String value) {
		String valueString = StringUtils.clean(value);
		
		if(valueString != null && !"*".equals(value)) {
			if (isWildcard(value)) {
				searchTerms.add(new WildcardTerm(field, valueString));				
			} else {
				addSimpleTerm(field, valueString);
			}
		}
	}
	
	private boolean isWildcard(String value) {
		return value.startsWith("*") || value.endsWith("*");
	}
	
	protected void addNullTerm(String field) {
		searchTerms.add(new NullTerm(field));
	}
	
	protected void addDateRangeTerm(String field, Date start, Date end) {
		if (start != null || end != null) {
			searchTerms.add(new DateRangeTerm(field, start, end));
		}
	}
	
	public List<QueryFilter> getSearchFilters() {
		searchFilters.clear();
		evalSearchFilters();
		return searchFilters;
	}
	
	protected void addOwnerFilter(BaseOrg owner) {
		if (owner != null) {
			searchFilters.add(new OwnerAndDownFilter(owner));
		}
	}
	
	protected void addRequiredLeftJoin(String path, String alias) {
		joinTerms.add(new JoinTerm(JoinTermType.LEFT, path, alias, true));
	}
	
	protected void addLeftJoinTerm(String path) {
		joinTerms.add(new JoinTerm(JoinTermType.LEFT, path, null, false));
	}
	
	protected void addLeftJoinTerms(String...paths) {
		for (String path: paths) {
			addLeftJoinTerm(path);
		}
	}
	
	@Override
	public LoaderFactory getLoaderFactory() {
		return loaderFactory;
	}

    public MultiIdSelection getMultiIdSelection() {
        return multiIdSelection;
    }

    public void setMultiIdSelection(MultiIdSelection multiIdSelection) {
        this.multiIdSelection = multiIdSelection;
    }

    public Long getSortColumnId() {
        return sortColumnId;
    }

    public void setSortColumnId(Long sortColumnId) {
        this.sortColumnId = sortColumnId;
    }

    public String getSortJoinExpression() {
        return sortJoinExpression;
    }

    public void setSortJoinExpression(String sortJoinExpression) {
        this.sortJoinExpression = sortJoinExpression;
    }
}
