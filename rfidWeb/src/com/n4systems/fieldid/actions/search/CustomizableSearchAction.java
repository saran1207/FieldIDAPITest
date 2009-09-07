package com.n4systems.fieldid.actions.search;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import org.apache.log4j.Logger;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractPaginatedAction;
import com.n4systems.fieldid.actions.helpers.ProductTypeLister;
import com.n4systems.fieldid.viewhelpers.ColumnMapping;
import com.n4systems.fieldid.viewhelpers.ColumnMappingFactory;
import com.n4systems.fieldid.viewhelpers.ColumnMappingGroup;
import com.n4systems.fieldid.viewhelpers.SearchContainer;
import com.n4systems.fieldid.viewhelpers.handlers.CellHandlerFactory;
import com.n4systems.fieldid.viewhelpers.handlers.OutputHandler;
import com.n4systems.taskscheduling.TaskExecutor;
import com.n4systems.taskscheduling.task.ExcelReportExportTask;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.persistence.search.ImmutableSearchDefiner;
import com.n4systems.util.persistence.search.ResultTransformer;
import com.n4systems.util.persistence.search.SearchDefiner;
import com.n4systems.util.persistence.search.SortTerm;
import com.n4systems.util.persistence.search.TableViewTransformer;
import com.n4systems.util.persistence.search.terms.SearchTermDefiner;
import com.n4systems.util.views.TableView;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;

public abstract class CustomizableSearchAction<T extends SearchContainer> extends AbstractPaginatedAction implements SearchDefiner<TableView> {
	private static final long serialVersionUID = 1L;
	protected Logger logger = Logger.getLogger(CustomizableSearchAction.class);
	
	private final Class<? extends CustomizableSearchAction<T>> implementingClass;
	private final String containerSessionKey;
	private final String excelReportFileName;
	private String searchId;
	private SortedSet<ColumnMappingGroup> mappingGroups;
	private Map<String, OutputHandler> cellHandlers = new HashMap<String, OutputHandler>();
	private TableView resultsTable;
	private ProductTypeLister productTypes;

	public CustomizableSearchAction(
			final Class<? extends CustomizableSearchAction<T>> implementingClass, 
			final String sessionKey, 
			final String excelReportFileName, 
			final PersistenceManager persistenceManager) {
		
		super(persistenceManager);
		
		this.containerSessionKey = sessionKey;
		this.implementingClass = implementingClass;
		this.excelReportFileName = excelReportFileName;
	}

	abstract public List<ColumnMappingGroup> getDynamicGroups();
	abstract protected T createSearchContainer();
	
	private void initializeColumnMappings() {
		mappingGroups = ColumnMappingFactory.getMappings(implementingClass, getTenant());
		mappingGroups.addAll(getDynamicGroups());
		
		// initialize the output handlers 
		for (ColumnMappingGroup group: mappingGroups) {
			for (ColumnMapping mapping: group.getMappings()) {
				registerCellHandler(mapping.getId(), mapping.getOutputHandler());
			}
		}
	}
	
	private void registerCellHandler(String columnId, String className) {
		CellHandlerFactory handlerFactory = new CellHandlerFactory(getSessionUser().getDateFormat(), getSessionUser().getDateTimeFormat(), getSessionUser().getTimeZone());
		
		// ask our handlerFactory for a handler
		cellHandlers.put(columnId, handlerFactory.getHandler(className));
	}

	private List<String> getDefaultSelectedColumns() {
		// setup the default selected columns
		List<String> defaultColumns = new ArrayList<String>();
		for (ColumnMappingGroup group: getMappingGroups()) {
			for (ColumnMapping mapping: group.getMappings()) {
				if(mapping.isOnByDefault()) {
					defaultColumns.add(mapping.getId());
				}
			}
		}
		return sortColumnIds(defaultColumns);
	}
	
	public String doCreateSearch() {
		setSearchId(getContainer().getSearchId());	
		return SUCCESS;
	}
	
	public String doSearch() {
		String status = SUCCESS;
		try {
			if(isSearchIdValid()) {

				resultsTable = persistenceManager.search(this);
				
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

	public String doExport() {
		String status = SUCCESS;

		if (isSearchIdValid()) {
			
			// setup the title of the report
			String tenantName = getTenant().getDisplayName().replace(" ", "_");	
			String fileDateFormat = "MM-dd-yyyy";
			String dateString = new SimpleDateFormat(fileDateFormat).format(new java.util.Date());
			String reportFileName = excelReportFileName + "_" + tenantName + "_" + dateString;

			// build the list of column titles from our package properties
			List<String> titles = new ArrayList<String>();
			for(ColumnMapping mapping: getSelectedMappings()) {
				titles.add(getText(mapping.getLabel()));
			}
			
			try {
				ExcelReportExportTask exportTask = new ExcelReportExportTask();
				
				exportTask.setColumnTitles(titles);
				exportTask.setUserId(getSessionUser().getUniqueID());
				exportTask.setPackageName(reportFileName);
				exportTask.setSearchDefiner(new ImmutableSearchDefiner<TableView>(this));

				TaskExecutor.getInstance().execute(exportTask);
				
				addActionMessage( getText( "message.emailshortly" ) );
			} catch (Exception e) {
				logger.error("Unable to execute ExcelExportTask", e);
				addActionError( getText( "error.cannotschedule" ) );
				status =  ERROR;
			}
		} else {
			addFlashError( getText( "error.searchexpired" ) );
			status = INPUT;
		}
		
		return status;
	}
	
	private ColumnMapping findMapping(String id) {
		ColumnMapping colMapping = null;
		for (ColumnMappingGroup group: getMappingGroups()) {
			for (ColumnMapping mapping: group.getMappings()) {
				if(mapping.getId().equals(id)) {
					colMapping = mapping;
					break;
				}
			}
		}
		return colMapping;
	}
	
	public SortedSet<ColumnMappingGroup> getMappingGroups() {
		if (mappingGroups == null) {
			initializeColumnMappings();
		}
		
		return mappingGroups;
	}
	
	protected List<ColumnMapping> getSelectedMappings() {
		List<ColumnMapping> mappings = new ArrayList<ColumnMapping>();
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
	
	public String[] getJoinColumns() {
		return getContainer().getJoinColumns();
	}

	public ResultTransformer<TableView> getTransformer() {
		List<String> columns = new ArrayList<String>();
		for(ColumnMapping mapping: getSelectedMappings()) {
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
		T container = (T)getSessionVar(containerSessionKey);
		if(container == null) {
			container = createSearchContainer();		
			container.setSelectedColumns(getDefaultSelectedColumns());
			setCriteria(container);
		}
		return container;
	}
	
	public void setCriteria(T container) {
		setSessionVar(containerSessionKey, container);
	}
	
	protected void clearContainer() {
		clearSessionVar(containerSessionKey);
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

	@CustomValidator(type = "listNotEmptyValidator", message = "", key = "error.nocolumnsselected")
	public void setSelectedColumns(List<String> selectedColumns) {
		getContainer().setSelectedColumns(sortColumnIds(selectedColumns));
	}
	
	public String isColumnSelected(String columnId) {
		return Boolean.toString(getContainer().isColumnSelected(columnId));
	}
	
	/**
	 * Sorts a list of column ids by their {@link ColumnMapping.getOrder()}s
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
	public ColumnMapping getColumnMapping(String columnId) {
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
		return null;
	}
	
	/**
	 * Returns the value for a cell, passing it through the OutputHandler defined for this column.
	 * @param row	row index
	 * @param col	column index
	 * @return		A String value for this row as returned from the specified handler
	 */
	public String getCell(int row, int col) {
		String columnId = getSelectedColumns().get(col);
		Long entityId = getIdForRow(row);
		Object cell = resultsTable.getCell(row, col);
		
		OutputHandler outputHandler = cellHandlers.get(columnId);
		String cellValue = outputHandler.handle(entityId, cell);
		if (outputHandler.isLabel()) {
			cellValue = getText(cellValue);
		}
		return cellValue;
	}
	
	public ProductTypeLister getProductTypes() {
		if (productTypes == null) {
			productTypes = new ProductTypeLister(persistenceManager, getSecurityFilter());
		}

		return productTypes;
	}
	
	
	public Integer getMaxSizeForExcelExport() {
		return ConfigContext.getCurrentContext().getInteger(ConfigEntry.MAX_SIZE_FOR_EXCEL_EXPORT, getTenantId());
	}
	
	public Integer getMaxSizeForPDFPrintOuts() {
		return ConfigContext.getCurrentContext().getInteger(ConfigEntry.MAX_SIZE_FOR_PDF_PRINT_OUTS, getTenantId());
	}
	
	public Integer getMaxSizeForSummaryReport() {
		return ConfigContext.getCurrentContext().getInteger(ConfigEntry.MAX_SIZE_FOR_SUMMARY_REPORT, getTenantId());
	}
	public Integer getMaxSizeForMassUpdate() {
		return ConfigContext.getCurrentContext().getInteger(ConfigEntry.MAX_SIZE_FOR_MASS_UPDATE, getTenantId());
	}
	public Integer getMaxSizeForAssigningInspectionsToJobs() {
		return ConfigContext.getCurrentContext().getInteger(ConfigEntry.MAX_SIZE_FOR_ASSIGNING_INSPECTIONS_TO_JOBS, getTenantId());
	}
}
