package com.n4systems.fieldid.viewhelpers;

import java.util.List;

import com.n4systems.model.location.Location;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.location.PredefinedLocationTree;
import com.n4systems.persistence.PersistenceManagerTransactor;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.Transactor;
import com.n4systems.persistence.UnitOfWork;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.uitags.views.HierarchicalNode;

public class ProductCrudHelper extends BaseActionHelper {

	private final LoaderFactory loaderFactory;
	private final Transactor transactor;

	public ProductCrudHelper(LoaderFactory factory) {
		this.loaderFactory = factory;
		this.transactor = new PersistenceManagerTransactor();
	}
	
	
	
	public List<HierarchicalNode> getPredefinedLocationTree() {
		PredefinedLocationTree locationTree = transactor.execute(new UnitOfWork<PredefinedLocationTree>() {
			public PredefinedLocationTree run(Transaction transaction) {
				return loaderFactory.createPredefinedLocationTreeLoader().load(transaction);
			}
		});
		
		return new LocationTreeToHierarchicalNodesConverter().convert(locationTree);
	}
	
	
	public boolean hasPredefinedLocationTree() {
		return transactor.execute(new UnitOfWork<Boolean>() {
			public Boolean run(Transaction transaction) {
				return !loaderFactory.createPredefinedLocationListLoader().load(transaction).isEmpty();
			}
		});
	}
	
	public String getFullNameOfLocation(Location location) {
		if (location == null) {
			return "";
		}
		
		PredefinedLocation predefinedLocation = location.getPredefinedLocation();
		
		String fullName = predefinedLocation.getName();
		
		while (predefinedLocation.hasParent()) {
			fullName = predefinedLocation.getParent().getName() + " " + fullName;
			
			predefinedLocation = predefinedLocation.getParent();
		}
		
		
		fullName += location.getFreeformLocation() != null ? " " + location.getFreeformLocation() : "";
		
		return fullName;
	}
	
}
