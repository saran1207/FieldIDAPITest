package com.n4systems.fieldid.actions.search;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.entity.ProductStatusBean;
import rfid.ejb.session.LegacyProductSerial;
import rfid.ejb.session.LegacyProductType;
import rfid.ejb.session.User;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.fieldid.actions.helpers.InfoFieldDynamicGroupGenerator;
import com.n4systems.fieldid.viewhelpers.ColumnMappingGroup;
import com.n4systems.fieldid.viewhelpers.ProductSearchContainer;
import com.n4systems.taskscheduling.TaskExecutor;
import com.n4systems.taskscheduling.task.PrintAllProductCertificatesTask;
import com.n4systems.util.ListingPair;

public class ProductSearchAction extends CustomizableSearchAction<ProductSearchContainer> {
	public static final String SEARCH_CRITERIA = "searchCriteria";
	private static final long serialVersionUID = 1L;
	
	private final InfoFieldDynamicGroupGenerator infoGroupGen;
	private final LegacyProductSerial productSerialManager;
	private final User userManager;
	private List<ListingPair> employees;
	
	public ProductSearchAction( 
			final LegacyProductType productTypeManager, 
			final LegacyProductSerial productSerialManager, 
			final PersistenceManager persistenceManager, 
			final User userManager, 
			final ProductManager productManager) {
		
		super(ProductSearchAction.class, SEARCH_CRITERIA, "ProductReport", persistenceManager);

		this.productSerialManager = productSerialManager;
		this.userManager = userManager;
		
		infoGroupGen = new InfoFieldDynamicGroupGenerator(persistenceManager, productManager);
	}
	
	@Override
	public List<ColumnMappingGroup> getDynamicGroups() {
		return infoGroupGen.getDynamicGroups(getContainer().getProductType(), "product_search", getSecurityFilter());
	}
	
	@Override
	protected ProductSearchContainer createSearchContainer() {
		return new ProductSearchContainer(getSecurityFilter());
	}

	@SkipValidation
	public String doSearchCriteria() {
		clearContainer();
		if (getSessionUser().getOwner().isExternalOrg()) {
			getContainer().setOwner(getSessionUser().getOwner().getId());
		}
		return INPUT;
	}
	
	public String doPrintAllCerts() {
		String status = SUCCESS;
		try {
			if (isSearchIdValid()) {

				String tenantName = getTenant().getDisplayName().replace(" ", "_");
				String dateString = new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
				String reportFileName = "manufacturer_certificate_report_" + tenantName + "_" + dateString;

				PrintAllProductCertificatesTask printAllTask = new PrintAllProductCertificatesTask();
				
				printAllTask.setDateFormat(getSessionUser().getDateFormat());
				printAllTask.setDownloadLocation(createActionURI("download.action").toString());
				printAllTask.setPackageName(reportFileName);
				printAllTask.setProductIdList(persistenceManager.idSearch(this));
				printAllTask.setUserId(getSessionUser().getUniqueID());

				TaskExecutor.getInstance().execute(printAllTask);
				addActionMessage( getText( "message.emailshortly" ) );
				
			} else {
				addFlashError( getText( "error.searchexpired" ) );
				status = INPUT;
			}
		
		} catch(InvalidQueryException e) {
			addFlashError( getText( "error.reportgeneration" ) );
			logger.error(e);
			status = ERROR;
		}
		
		return status;
	}
	
	@SkipValidation
	public String doGetDynamicColumnOptions() {
		return SUCCESS;
	}
	
	public String getFromDate() {
		return convertDate(getContainer().getFromDate());
	}

	public void setFromDate(String fromDate) {
		getContainer().setFromDate(convertDate(fromDate));
	}

	public String getToDate() {
		return convertDate(getContainer().getToDate());
	}

	public void setToDate(String toDate) {
		getContainer().setToDate(convertToEndOfDay(toDate));
	}

	public Collection<ProductStatusBean> getProductStatuses() {
		return productSerialManager.getAllProductStatus(getTenantId());
	}
	
	public List<ListingPair> getEmployees() {
		if(employees == null) {
			employees = userManager.getEmployeeList(getSecurityFilter());
		}
		return employees;
	}
	
}
