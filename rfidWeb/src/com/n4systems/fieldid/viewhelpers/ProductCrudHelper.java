package com.n4systems.fieldid.viewhelpers;

import java.util.List;

import com.n4systems.model.location.Location;
import com.n4systems.persistence.PersistenceManagerTransactor;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.uitags.views.HeirarchicalNode;

public class ProductCrudHelper extends BaseActionHelper {


	private final LocationHelper locationHelper;
	
	
	public ProductCrudHelper(LoaderFactory factory) {
		this.locationHelper = new LocationHelper(factory, new PersistenceManagerTransactor());
	}
	
	
	public List<HeirarchicalNode> getPredefinedLocationTree() {
		return locationHelper.getPredefinedLocationTree();
	}
	
	
	public boolean hasPredefinedLocationTree() {
		return locationHelper.hasPredefinedLocationTree();
	}
	
	public String getFullNameOfLocation(Location location) {
		return locationHelper.getFullNameOfLocation(location);
	}

	
}
