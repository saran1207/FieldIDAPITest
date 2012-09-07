package com.n4systems.fieldid.viewhelpers;

import com.n4systems.fieldid.actions.asset.LocationWebModel;
import com.n4systems.model.location.Location;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.persistence.PersistenceManagerTransactor;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.uitags.views.HierarchicalNode;

import java.util.List;

public class AssetCrudHelper extends BaseActionHelper {


	private final LocationHelper locationHelper;
	
	
	public AssetCrudHelper(LoaderFactory factory) {
		this.locationHelper = new LocationHelper(factory, new PersistenceManagerTransactor());
	}


    public List<HierarchicalNode> getPredefinedLocationTree() {
        return locationHelper.getPredefinedLocationTree();
    }

    public List<HierarchicalNode> getPredefinedLocationTree(BaseOrg owner) {
        return locationHelper.getPredefinedLocationTree(owner);
    }

    public boolean hasPredefinedLocationTree() {
		return locationHelper.hasPredefinedLocationTree();
	}
	
	public String getFullNameOfLocation(Location location) {
		return locationHelper.getFullNameOfLocation(location);
	}


	public String getFullNameOfLocation(LocationWebModel location) {
		return locationHelper.getFullNameOfLocation(location);
	}

	
}
