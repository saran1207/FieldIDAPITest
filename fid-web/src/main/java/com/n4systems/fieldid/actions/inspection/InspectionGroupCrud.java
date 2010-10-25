package com.n4systems.fieldid.actions.inspection;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.model.Asset;
import org.apache.log4j.Logger;

import com.n4systems.ejb.InspectionManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.model.AssociatedInspectionType;
import com.n4systems.model.InspectionGroup;
import com.n4systems.model.InspectionType;

public class InspectionGroupCrud extends AbstractCrud {

	private static final long serialVersionUID = 1L;

	private static Logger logger = Logger.getLogger(InspectionGroupCrud.class);

	private InspectionGroup inspectionGroup;
	private Asset asset;

	private List<Asset> assets;

	private List<InspectionGroup> inspectionGroups;

	private String search;

	private InspectionManager inspectionManager;
	private ProductManager productManager;

	private List<InspectionType> inspectionTypes;

	public InspectionGroupCrud(InspectionManager inspectionManager, ProductManager productManager, PersistenceManager persistenceManager) {
		super(persistenceManager);
		this.inspectionManager = inspectionManager;
		this.productManager = productManager;
	}

	@Override
	protected void initMemberFields() {

	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		asset = productManager.findAsset(uniqueId, getSecurityFilter());
	}

	public String doList() {
		// if no search param came just show the form.
		if (search != null && search.length() > 0) {
			try {
				assets = productManager.findProductByIdentifiers(getSecurityFilter(), search);
				// if there is only one forward. directly to the group view
				// screen.
				if (assets.size() == 1) {
					asset = assets.get(0);
					uniqueID = asset.getId();
					return "oneFound";
				}
			} catch (Exception e) {
				logger.error("Failed to look up Products", e);
				addActionError(getText("error.failedtoload"));
				return ERROR;
			}

		}

		return SUCCESS;
	}

	public String doShow() {
		if (asset == null) {
			addActionError(getText("error.noproduct"));
			return MISSING;
		}

		inspectionGroups = inspectionManager.findAllInspectionGroups(getSecurityFilter(), uniqueID, "inspections");
		return SUCCESS;
	}

	public String doDelete() {
		return SUCCESS;
	}

	public Asset getAsset() {
		return asset;
	}

	public void setAsset(Asset asset) {
		this.asset = asset;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		if (search != null) {
			search = search.trim();
		}
		this.search = search;
	}

	public InspectionGroup getInspectionGroup() {
		return inspectionGroup;
	}

	public List<Asset> getAssets() {
		return assets;
	}

	public List<InspectionGroup> getInspectionGroups() {
		return inspectionGroups;
	}

	public List<InspectionType> getInspectionTypes() {
		if (inspectionTypes == null) {
			inspectionTypes = new ArrayList<InspectionType>();
			List<AssociatedInspectionType> associatedInspectionTypes = getLoaderFactory().createAssociatedInspectionTypesLoader().setProductType(getAsset().getType()).load();
			for (AssociatedInspectionType associatedInspectionType : associatedInspectionTypes) {
				inspectionTypes.add(associatedInspectionType.getInspectionType());
			}
		}
		return inspectionTypes;
		
	}

	public boolean isMasterInspection(Long id) {
		return inspectionManager.isMasterInspection(id);
	}

}
