/**
 * 
 */
package com.n4systems.fieldid.viewhelpers;

import com.n4systems.model.location.Location;
import com.n4systems.persistence.PersistenceManagerTransactor;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.uitags.views.HierarchicalNode;

import java.util.List;

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

	public List<HierarchicalNode> findSiblingsByParent(Long parentId) {
		return locationHelper.findSiblingsByParent(parentId);
	}
}