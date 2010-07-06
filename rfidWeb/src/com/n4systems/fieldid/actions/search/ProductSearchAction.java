package com.n4systems.fieldid.actions.search;

import static com.n4systems.fieldid.viewhelpers.InspectionSearchContainer.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.entity.ProductStatusBean;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.ejb.SearchPerformerWithReadOnlyTransactionManagement;
import com.n4systems.fieldid.actions.helpers.InfoFieldDynamicGroupGenerator;
import com.n4systems.fieldid.actions.helpers.ProductManagerBackedCommonProductAttributeFinder;
import com.n4systems.fieldid.actions.utils.DummyOwnerHolder;
import com.n4systems.fieldid.actions.utils.OwnerPicker;
import com.n4systems.fieldid.viewhelpers.LocationHelper;
import com.n4systems.fieldid.viewhelpers.ProductSearchContainer;
import com.n4systems.model.api.Listable;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.uitags.views.Node;
import com.n4systems.util.DateHelper;
import com.n4systems.util.persistence.SimpleListable;
import com.n4systems.util.persistence.search.ImmutableBaseSearchDefiner;
import com.opensymphony.xwork2.Preparable;

public class ProductSearchAction extends CustomizableSearchAction<ProductSearchContainer> implements Preparable {
	public static final String SEARCH_CRITERIA = "searchCriteria";
	private static final long serialVersionUID = 1L;
	
	private List<Listable<Long>> employees;
	private LocationHelper locationHelper = new LocationHelper();
	private OwnerPicker ownerPicker;
	private List<Long> searchIds;
	private List<Node> nodes;
	public ProductSearchAction( 
			final PersistenceManager persistenceManager, 
			final ProductManager productManager) {
		
		super(ProductSearchAction.class, SEARCH_CRITERIA, "Product Report", persistenceManager, 
				new InfoFieldDynamicGroupGenerator(new ProductManagerBackedCommonProductAttributeFinder(productManager), "product_search"));

	}
	
	public void prepare() throws Exception {
		ownerPicker = new OwnerPicker(getLoaderFactory().createFilteredIdLoader(BaseOrg.class), new DummyOwnerHolder());
		ownerPicker.setOwnerId(getContainer().getOwnerId());
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
		if (!isSearchIdValid()) {
			addFlashErrorText( "error.searchexpired");
			return INPUT;
		}
		String reportName = String.format("Manufacturer Certificate Report - %s", DateHelper.getFormattedCurrentDate(getUser()));

		try {
			List<Long> productIds = getSearchIds();
			
			getDownloadCoordinator().generateAllProductCertificates(reportName, getDownloadLinkUrl(), productIds);
		} catch(Exception e) {
			logger.error("Failed to print all manufacturer certs", e);
			addFlashErrorText("error.reportgeneration");
			return ERROR;
		}
		
		return SUCCESS;
	}

	public List<Long> getSearchIds() {
		if (searchIds == null) {
			searchIds = new SearchPerformerWithReadOnlyTransactionManagement().idSearch(new ImmutableBaseSearchDefiner(this), getSecurityFilter());
		}
		
		return searchIds;
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
	
	public List<Listable<Long>> getEmployees() {
		if(employees == null) {
			employees = new ArrayList<Listable<Long>>();
			employees.add(new SimpleListable<Long>(UNASSIGNED_USER, getText("label.unassigned")));
			employees.addAll(getLoaderFactory().createHistoricalEmployeesListableLoader().load());
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
	
	public List<Node> getNodes(){
		nodes = new ArrayList<Node>( locationHelper.createNodes());
		return nodes;
	}
	
}
