/**
 * 
 */
package com.n4systems.fieldid.actions.asset;

import com.n4systems.fieldid.viewhelpers.BaseActionHelper;
import com.n4systems.fieldid.viewhelpers.LocationHelper;
import com.n4systems.model.location.Location;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.User;
import com.n4systems.persistence.PersistenceManagerTransactor;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.uitags.views.HierarchicalNode;

import java.util.List;

public class CustomerInformationCrudHelper extends BaseActionHelper {

	private final LocationHelper locationHelper;
	CustomerInformationCrudHelper(LoaderFactory loaderFactory) {
		locationHelper = new LocationHelper(loaderFactory, new PersistenceManagerTransactor());
	}



	public String getFullNameOfLocation(Location location) {
		return locationHelper.getFullNameOfLocation(location);
	}

	public String getFullNameOfLocation(LocationWebModel location) {
		return locationHelper.getFullNameOfLocation(location);
	}

	public List<HierarchicalNode> getPredefinedLocationTree() {
		return locationHelper.getPredefinedLocationTree();
	}

    public List<HierarchicalNode> getPredefinedLocationTree(BaseOrg owner, User user) {
        return locationHelper.getPredefinedLocationTree(owner, user);
    }

	public boolean hasPredefinedLocationTree() {
		return locationHelper.hasPredefinedLocationTree();
	}
}