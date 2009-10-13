package com.n4systems.fieldid.actions.search;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.entity.ProductStatusBean;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.fieldid.actions.helpers.InfoFieldDynamicGroupGenerator;
import com.n4systems.fieldid.actions.utils.DummyOwnerHolder;
import com.n4systems.fieldid.actions.utils.OwnerPicker;
import com.n4systems.fieldid.viewhelpers.ColumnMappingGroup;
import com.n4systems.fieldid.viewhelpers.ProductSearchContainer;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.UserListableLoader;
import com.n4systems.taskscheduling.TaskExecutor;
import com.n4systems.taskscheduling.task.PrintAllProductCertificatesTask;
import com.n4systems.util.ListHelper;
import com.n4systems.util.ListingPair;
import com.opensymphony.xwork2.Preparable;

public class ProductSearchAction extends CustomizableSearchAction<ProductSearchContainer> implements Preparable {
	public static final String SEARCH_CRITERIA = "searchCriteria";
	private static final long serialVersionUID = 1L;
	
	private final InfoFieldDynamicGroupGenerator infoGroupGen;
	private List<ListingPair> employees;
	
	private OwnerPicker ownerPicker;
	
	public ProductSearchAction( 
			final PersistenceManager persistenceManager, 
			final ProductManager productManager) {
		
		super(ProductSearchAction.class, SEARCH_CRITERIA, "ProductReport", persistenceManager);

		
		infoGroupGen = new InfoFieldDynamicGroupGenerator(persistenceManager, productManager);
	}
	
	public void prepare() throws Exception {
		ownerPicker = new OwnerPicker(getLoaderFactory().createFilteredIdLoader(BaseOrg.class), new DummyOwnerHolder());
		ownerPicker.setOwnerId(getContainer().getOwnerId());
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
		return INPUT;
	}
	
	@Override
	protected void clearContainer() {
		super.clearContainer();
		setOwnerId(null);
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
				printAllTask.setProductIdList(persistenceManager.idSearch(this, getContainer().getSecurityFilter()));
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

	public List<ProductStatusBean> getProductStatuses() {
		return getLoaderFactory().createProductStatusListLoader().load();
	}
	
	public List<ListingPair> getEmployees() {
		if(employees == null) {
			UserListableLoader loader = getLoaderFactory().createUserListableLoader();
			employees = ListHelper.longListableToListingPair(loader.load());
		}
		return employees;
	}

	public BaseOrg getOwner() {
		return ownerPicker.getOwner();
	}

	public Long getOwnerId() {
		return ownerPicker.getOwnerId();
	}

	public void setOwnerId(Long id) {
		ownerPicker.setOwnerId(id);
		getContainer().setOwner(ownerPicker.getOwner());
		
	}

	
	
}
