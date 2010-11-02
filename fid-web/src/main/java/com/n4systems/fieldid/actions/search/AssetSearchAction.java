package com.n4systems.fieldid.actions.search;

import static com.n4systems.fieldid.viewhelpers.EventSearchContainer.*;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.ejb.AssetManager;
import com.n4systems.fieldid.actions.helpers.ProductManagerBackedCommonAssetAttributeFinder;
import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.entity.AssetStatus;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.SearchPerformerWithReadOnlyTransactionManagement;
import com.n4systems.fieldid.actions.helpers.InfoFieldDynamicGroupGenerator;
import com.n4systems.fieldid.actions.utils.DummyOwnerHolder;
import com.n4systems.fieldid.actions.utils.OwnerPicker;
import com.n4systems.fieldid.viewhelpers.AssetSearchContainer;
import com.n4systems.fieldid.viewhelpers.SearchHelper;
import com.n4systems.model.api.Listable;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.util.DateHelper;
import com.n4systems.util.persistence.SimpleListable;
import com.n4systems.util.persistence.search.ImmutableBaseSearchDefiner;
import com.opensymphony.xwork2.Preparable;

public class AssetSearchAction extends CustomizableSearchAction<AssetSearchContainer> implements Preparable {
	public static final String SEARCH_CRITERIA = "searchCriteria";
	private static final long serialVersionUID = 1L;

	private OwnerPicker ownerPicker;
	private List<Listable<Long>> employees;
	private List<Long> searchIds;

	public AssetSearchAction(final PersistenceManager persistenceManager, final AssetManager assetManager) {
		super(AssetSearchAction.class, SEARCH_CRITERIA, "Asset Report", persistenceManager, new InfoFieldDynamicGroupGenerator(new ProductManagerBackedCommonAssetAttributeFinder(assetManager), "asset_search"));
	}

	public void prepare() throws Exception {
		ownerPicker = new OwnerPicker(getLoaderFactory().createFilteredIdLoader(BaseOrg.class), new DummyOwnerHolder());
		ownerPicker.setOwnerId(getContainer().getOwnerId());
		
		overrideHelper(new SearchHelper(getLoaderFactory()));
	}
	
	@Override
	protected AssetSearchContainer createSearchContainer() {
		return new AssetSearchContainer(getSecurityFilter(), getLoaderFactory());
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
			addFlashErrorText("error.searchexpired");
			return INPUT;
		}
		String reportName = String.format("Manufacturer Certificate Report - %s", DateHelper.getFormattedCurrentDate(getUser()));

		try {
			List<Long> assetIds = getSearchIds();

			getDownloadCoordinator().generateAllAssetCertificates(reportName, getDownloadLinkUrl(), assetIds);
		} catch (Exception e) {
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

	public List<AssetStatus> getAssetStatuses() {
		return getLoaderFactory().createAssetStatusListLoader().load();
	}

	public List<Listable<Long>> getEmployees() {
		if (employees == null) {
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
}
