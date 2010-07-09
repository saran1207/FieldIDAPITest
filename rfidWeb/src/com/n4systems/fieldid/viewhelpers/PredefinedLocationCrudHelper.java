/**
 * 
 */
package com.n4systems.fieldid.viewhelpers;

import java.util.List;

import com.n4systems.model.location.Location;
import com.n4systems.persistence.PersistenceManagerTransactor;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.uitags.views.HierarchicalNode;

public class PredefinedLocationCrudHelper extends BaseActionHelper {
	private LocationHelper locationHelper;

	public PredefinedLocationCrudHelper(LoaderFactory factory) {
		this.locationHelper = new LocationHelper(factory, new PersistenceManagerTransactor());
	}
	public String getFullNameOfLocation(Location location) {
		return locationHelper.getFullNameOfLocation(location);
	}

	public List<HierarchicalNode> getPredefinedLocationTree() {
		return locationHelper.getPredefinedLocationTree();
	}

	public boolean hasPredefinedLocationTree() {
		return locationHelper.hasPredefinedLocationTree();
	}
}