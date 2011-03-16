package com.n4systems.fieldid.actions.search;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import com.n4systems.fieldid.viewhelpers.ColumnMappingGroupView;
import com.n4systems.fieldid.viewhelpers.ColumnMappingView;
import com.n4systems.fieldid.viewhelpers.ReportConfiguration;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.assettype.AssetTypesByAssetGroupIdLoader;
import com.n4systems.util.persistence.search.terms.SimpleInTerm;
import org.apache.log4j.Logger;

import com.n4systems.ejb.PageHolder;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.SearchPerformerWithReadOnlyTransactionManagement;
import com.n4systems.fieldid.actions.api.AbstractPaginatedAction;
import com.n4systems.fieldid.actions.helpers.InfoFieldDynamicGroupGenerator;
import com.n4systems.fieldid.reporting.helpers.DynamicColumnProvider;
import com.n4systems.fieldid.viewhelpers.SearchContainer;
import com.n4systems.fieldid.viewhelpers.handlers.CellHandlerFactory;
import com.n4systems.fieldid.viewhelpers.handlers.WebOutputHandler;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.DateHelper;
import com.n4systems.util.persistence.QueryFilter;
import com.n4systems.util.persistence.search.ImmutableSearchDefiner;
import com.n4systems.util.persistence.search.JoinTerm;
import com.n4systems.util.persistence.search.ResultTransformer;
import com.n4systems.util.persistence.search.SearchDefiner;
import com.n4systems.util.persistence.search.SortTerm;
import com.n4systems.util.persistence.search.TableViewTransformer;
import com.n4systems.util.persistence.search.terms.SearchTermDefiner;
import com.n4systems.util.views.ExcelOutputHandler;
import com.n4systems.util.views.TableView;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.Validation;

@Validation
public abstract class CustomizableSearchAction<T extends SearchContainer> extends AbstractPaginatedAction implements SearchDefiner<TableView> {
	private class CustomizableSearchActionDynamicColumnProvider implements DynamicColumnProvider {
		private final CustomizableSearchAction<T> action;

		private CustomizableSearchActionDynamicColumnProvider(CustomizableSearchAction<T> action) {
			this.action = action;
		}

		public SortedSet<ColumnMappingGroupView> getDynamicGroups() {
			return new TreeSet<ColumnMappingGroupView>(action.getDynamicGroups());
		}
	}

	private static final long serialVersionUID = 1L;
	protected Logger logger = Logger.getLogger(CustomizableSearchAction.class);
	
	private final Class<? extends CustomizableSearchAction<T>> implementingClass;
	private final String containerSessionKey;
	private final String excelReportFileName;
	private String searchId;
	private SortedSet<ColumnMappingGroupView> mappingGroups;
	private Map<String, WebOutputHandler> cellHandlers = new HashMap<String, WebOutputHandler>();
	private TableView resultsTable;
	private List<AssetType> assetTypes;
    private List<AssetTypeGroup> assetTypeGroups;
	protected final InfoFieldDynamicGroupGenerator infoGroupGen;
    private ReportConfiguration reportConfiguration;
	
	public CustomizableSearchAction(
			final Class<? extends CustomizableSearchAction<T>> implementingClass, 
			final String sessionKey, 
			final String excelReportFileName, 
			final PersistenceManager persistenceManager, InfoFieldDynamicGroupGenerator infoGroupGen) {
		
		super(persistenceManager);
		
		this.containerSessionKey = sessionKey;
		this.implementingClass = implementingClass;
		this.excelReportFileName = excelReportFileName;
		this.infoGroupGen = infoGroupGen;
	}

	public List<ColumnMappingGroupView> getDynamicGroups() {
		return infoGroupGen.getDynamicGroups(getContainer().getAssetType(), getAssetTypeIds());
	}

	abstract protected T createSearchContainer();
	
	private void initializeColumnMappings() {
		initializeAValueInMappingGroupsSoCallToGetContainerInGetDynamicGroupsDontStartLoopOfCallsThatResultInAStackOverflow();
		
        ReportConfiguration reportConfig = getReportConfiguration();
        mappingGroups.addAll(reportConfig.getColumnGroups());
        mappingGroups.addAll(getDynamicGroups());
        if (getSortDirection() == null) setSortDirection(reportConfig.getSortDirection().getDisplayName());
        if (getSortColumn() == null) setSortColumn(reportConfig.getSortColumn().getPathExpression());

		// initialize the output handlers 
		for (ColumnMappingGroupView group: mappingGroups) {
			for (ColumnMappingView mapping: group.getMappings()) {
				registerCellHandler(mapping.getId(), mapping.getOutputHandler());
			}
		}
	}

    private ReportConfiguration getReportConfiguration() {
        if (reportConfiguration == null) {
            reportConfiguration = loadReportConfiguration();
        }
        return reportConfiguration;
    }

    protected abstract ReportConfiguration loadReportConfiguration();

	private void initializeAValueInMappingGroupsSoCallToGetContainerInGetDynamicGroupsDontStartLoopOfCallsThatResultInAStackOverflow() {
		mappingGroups = new TreeSet<ColumnMappingGroupView>();
	}
	
	private void registerCellHandler(String columnId, String className) {
		CellHandlerFactory handlerFactory = new CellHandlerFactory(this);
		
		// ask our handlerFactory for a handler
		cellHandlers.put(columnId, handlerFactory.getHandler(className));
	}

	private List<String> getDefaultSelectedColumns() {
		// setup the default selected columns
		List<String> defaultColumns = new ArrayList<String>();
		for (ColumnMappingGroupView group: getMappingGroups()) {
			for (ColumnMappingView mapping: group.getMappings()) {
				if(mapping.isOnByDefault()) {
					defaultColumns.add(mapping.getId());
				}
			}
		}
		return sortColumnIds(defaultColumns);
	}
	
	public String doCreateSearch() {
		setSearchId(getContainer().getSearchId());
        getContainer().getMultiIdSelection().clear();
		return SUCCESS;
	}
	
	public String doSearch() {
		String status = SUCCESS;
		try {
			if(isSearchIdValid()) {
				resultsTable = getSearchResults();
				
			} else {
				addFlashErrorText("error.searchexpired");
				status = INPUT;
			}
		} catch(Exception e) {
			addFlashErrorText("error.reportgeneration");
			logger.error("Unable to generate report", e);
			status = ERROR;
		}

		return status;
	}

	public TableView getSearchResults() {
		PageHolder<TableView> searchResults = new SearchPerformerWithReadOnlyTransactionManagement().search(new ImmutableSearchDefiner<TableView>(this), getContainer().getSecurityFilter());
		setTotalResults(searchResults.getTotalsResults());
		return searchResults.getPageResults();
	}

	private ImmutableSearchDefiner<TableView> immutableSearchDefiner() {
		return new ImmutableSearchDefiner<TableView>(this);
	}

	protected ExcelOutputHandler[] prepareExcelHandlers() {
		List<String> columnIds = getSelectedColumns();
		
		ExcelOutputHandler[] excelHandlers = new ExcelOutputHandler[columnIds.size()];
		
		for (int col = 0; col < excelHandlers.length; col++) {
			excelHandlers[col] = cellHandlers.get(columnIds.get(col));
		}
		
		return excelHandlers;
	}
	
	public String doExport() {
		if (!isSearchIdValid()) {
			addFlashErrorText("error.searchexpired");
			return INPUT;
		}
		String reportName = String.format("%s - %s", excelReportFileName, DateHelper.getFormattedCurrentDate(getUser()));
		
		try {
            List<Long> selectedIds = getContainer().getMultiIdSelection().getSelectedIds();
            ImmutableSearchDefiner<TableView> searchDefiner = immutableSearchDefiner();
            searchDefiner.getSearchTerms().add(new SimpleInTerm<Long>("id", selectedIds));

            getDownloadCoordinator().generateExcel(reportName, getDownloadLinkUrl(), searchDefiner, buildExcelColumnTitles(), prepareExcelHandlers());
		} catch (RuntimeException e) {
			logger.error("Unable to execute ExcelExportTask", e);
			addActionErrorText("error.cannotschedule");
			return ERROR;
		}
		
		return SUCCESS;
	}

	private List<String> buildExcelColumnTitles() {
		List<String> titles = new ArrayList<String>();
		for(ColumnMappingView mapping: getSelectedMappings()) {
			titles.add(getText(mapping.getLabel()));
		}
		return titles;
	}
	
	private ColumnMappingView findMapping(String id) {
		ColumnMappingView colMapping = null;
		for (ColumnMappingGroupView group: getMappingGroups()) {
			for (ColumnMappingView mapping: group.getMappings()) {
				if(mapping.getId().equals(id)) {
					colMapping = mapping;
					break;
				}
			}
		}
		return colMapping;
	}
	
	public SortedSet<ColumnMappingGroupView> getMappingGroups() {
		if (mappingGroups == null) {
			initializeColumnMappings();
		}
		
		return mappingGroups;
	}
	
	protected List<ColumnMappingView> getSelectedMappings() {
		List<ColumnMappingView> mappings = new ArrayList<ColumnMappingView>();
		for(String mappingId: getContainer().getSelectedColumns()) {
			mappings.add(findMapping(mappingId));
		}
		return mappings;
	}
	
	public Class<?> getSearchClass() {
		return getContainer().getSearchClass();
	}

	public List<SortTerm> getSortTerms() {
		return getContainer().getSortTerms();
	}

	public List<SearchTermDefiner> getSearchTerms() {
		return getContainer().getSearchTerms();
	}
	
	public List<JoinTerm> getJoinTerms() {
		return getContainer().getJoinTerms();
	}

	public ResultTransformer<TableView> getTransformer() {
		List<String> columns = new ArrayList<String>();
		for(ColumnMappingView mapping: getSelectedMappings()) {
			if (mapping != null) {
				columns.add(mapping.getPathExpression());
			}
		}
		
		ResultTransformer<TableView> transformer = null;
		try {
			transformer = new TableViewTransformer(getContainer().getSearchClassIdField(), columns);
		} catch (ParseException e) {
			logger.error("Could not create result transformer", e);
		}
		
		return transformer;
	}

	@Deprecated
	public T getCriteria() {
		return getContainer();
	}
	
	@SuppressWarnings("unchecked")
	public T getContainer() {
		T container = (T)getSession().get(containerSessionKey);
		if (container == null) {
			container = createSearchContainer();		
			container.setSelectedColumns(getDefaultSelectedColumns());
			setCriteria(container);
		}
		return container;
	}
	
	public void setCriteria(T container) {
		getSession().put(containerSessionKey, container);
	}
	
	protected void clearContainer() {
		getSession().remove(containerSessionKey);
	}
	
	protected void resetSelectedColumns() {
		getContainer().setSelectedColumns(getDefaultSelectedColumns());
	}
	
	public String getSortColumn() {
		return getContainer().getSortColumn();
	}
	
	public void setSortColumn(String column) {
		getContainer().setSortColumn(column);
	}
	
	public String getSortDirection() {
		return getContainer().getSortDirection();
	}
	
	public void setSortDirection(String direction) {
		getContainer().setSortDirection(direction);
	}
	
	public List<String> getSelectedColumns() {
		return getContainer().getSelectedColumns();
	}

	public List<QueryFilter> getSearchFilters() {
		return getContainer().getSearchFilters();
	}

	@CustomValidator(type = "listNotEmptyValidator", message = "", key = "error.nocolumnsselected")
	public void setSelectedColumns(List<String> selectedColumns) {
		getContainer().setSelectedColumns(sortColumnIds(selectedColumns));
	}
	
	public String isColumnSelected(String columnId) {
		return Boolean.toString(getContainer().isColumnSelected(columnId));
	}
	
	/**
	 * Sorts a list of column ids
	 * @param selectedColumns	List of columns ids
	 * @return					List of sorted column ids
	 */
	private List<String> sortColumnIds(List<String> selectedColumns) {
		List<String> sortedColumns = new ArrayList<String>(selectedColumns);
		
		Collections.sort(sortedColumns, new Comparator<String>() {
			public int compare(String cid1, String cid2) {
	            return findMapping(cid1).compareTo(findMapping(cid2));
            }
		});
		
		return sortedColumns;
	}
	
	/**
	 * Finds the ColumnMapping object for a columnId
	 * @param columnId	Uniqueid of the column
	 * @return			A ColumnMapping
	 */
	public ColumnMappingView getColumnMapping(String columnId) {
		return findMapping(columnId);
	}
	
	public String getSearchId() {
		return searchId;
	}

	public void setSearchId(String searchId) {
		this.searchId = searchId;
	}
	
	public boolean isSearchIdValid() {
		return (searchId != null && searchId.equals(getContainer().getSearchId()));
	}
	
	public TableView getResultsTable() {
		return resultsTable;
	}

    public boolean isItemSelected(Long itemId) {
        return getContainer().getMultiIdSelection().containsId(itemId);
    }

	/**
	 * Returns the entity for this row
	 * @param rowIndex	index of the row
	 * @return			entity object
	 */
	protected Object getEntityForRow(int rowIndex) {
		return resultsTable.getEntity(rowIndex);
	}
	
	/**
	 * Returns the entity id for a given row
	 * @param rowIndex	index of the row
	 * @return			Id of the entity for that row
	 */
	protected Long getIdForRow(int rowIndex) {
		return resultsTable.getId(rowIndex);
	}
	
	/**
	 * Provides a blank holder for sub-classes to return 
	 * custom css classes based on entity information.
	 * Extending actions can override to provide this functionality.
	 * @param rowIndex	The index of the current row
	 * @return			A String CSS classname or null to use no class.
	 */
	public String getRowClass(int rowIndex) {
        String rowClass = "";
        if (isRowIndexSelected(rowIndex)) {
            rowClass += "multiSelected";
        }
		return rowClass;
	}

    public boolean isRowIndexSelected(int rowIndex) {
        return isSearchIdValid() && getContainer().getMultiIdSelection().containsId(getIdForRow(rowIndex));
    }
	
	/**
	 * Returns the value for a cell, passing it through the OutputHandler defined for this column.
	 * @param row	row index
	 * @param col	column index
	 * @return		A String value for this row as returned from the specified handler
	 */
	public String getCell(int row, int col) {
		String cellValue;
		try {
			String columnId = getSelectedColumns().get(col);
			Long entityId = getIdForRow(row);
			Object cell = resultsTable.getCell(row, col);
			
			WebOutputHandler outputHandler = cellHandlers.get(columnId);
			cellValue = outputHandler.handleWeb(entityId, cell);
		} catch(Exception e) {
			logger.error(String.format("Failed handling cell (%d, %d)", row, col), e);
			cellValue = "";
		}
		
		return cellValue;
	}
	
	public List<AssetType> getAssetTypes() {
        if (assetTypes == null) {
            AssetTypesByAssetGroupIdLoader typesByGroupListLoader = getLoaderFactory().createAssetTypesByGroupListLoader();
            typesByGroupListLoader.setAssetTypeGroupId(getContainer().getAssetTypeGroup());

            assetTypes = typesByGroupListLoader.load();
        }
        return assetTypes;
	}

	public List<Long> getAssetTypeIds() {
        List<Long> assetTypeIds = new ArrayList<Long>();
        for (AssetType type : getAssetTypes()) {
            assetTypeIds.add(type.getId());
        }
        return assetTypeIds;
	}

    public List<AssetTypeGroup> getAssetTypeGroups() {
        if (assetTypeGroups == null) {
            assetTypeGroups = getLoaderFactory().createAssetTypeGroupsLoader().load();
        }
        return assetTypeGroups;
    }

	public Integer getMaxSizeForExcelExport() {
		return getConfigContext().getInteger(ConfigEntry.MAX_SIZE_FOR_EXCEL_EXPORT, getTenantId());
	}
	
	public Integer getMaxSizeForPDFPrintOuts() {
		return getConfigContext().getInteger(ConfigEntry.MAX_SIZE_FOR_PDF_PRINT_OUTS, getTenantId());
	}
	
	public Integer getMaxSizeForSummaryReport() {
		return getConfigContext().getInteger(ConfigEntry.MAX_SIZE_FOR_SUMMARY_REPORT, getTenantId());
	}
	
	public Integer getMaxSizeForMassUpdate() {
		return getConfigContext().getInteger(ConfigEntry.MAX_SIZE_FOR_MASS_UPDATE, getTenantId());
	}
	
	public Integer getMaxSizeForAssigningEventsToJobs() {
		return getConfigContext().getInteger(ConfigEntry.MAX_SIZE_FOR_ASSIGNING_INSPECTIONS_TO_JOBS, getTenantId());
	}
	
	public Integer getMaxSizeForMultiEvent() {
		return getConfigContext().getInteger(ConfigEntry.MAX_SIZE_FOR_MULTI_INSPECT, getTenantId());
	}

    public String getSearchContainerKey() {
        return containerSessionKey;
    }

    public int getNumSelectedItems() {
        return getContainer().getMultiIdSelection().getNumSelectedIds();
    }
	
}
