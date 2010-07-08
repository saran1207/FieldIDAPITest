package com.n4systems.fieldid.viewhelpers;

import java.util.List;
import java.util.Stack;

import com.n4systems.fieldid.actions.product.LocationWebModel;
import com.n4systems.model.location.Location;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.location.PredefinedLocationLevels;
import com.n4systems.model.location.PredefinedLocationTree;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.Transactor;
import com.n4systems.persistence.UnitOfWork;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.uitags.views.HeirarchicalNode;

public class LocationHelper {

	private final LoaderFactory factory;
	private final Transactor transactor;

	public LocationHelper(LoaderFactory factory, Transactor transactor) {
		this.factory = factory;
		this.transactor = transactor;
	}

	public List<HeirarchicalNode> getPredefinedLocationTree() {
		return  transactor.execute(new UnitOfWork<List<HeirarchicalNode>>() {
			public List<HeirarchicalNode> run(Transaction transaction) {
				PredefinedLocationTree locationTree = factory.createPredefinedLocationTreeLoader().load(transaction);
				PredefinedLocationLevels levels = factory.createPredefinedLocationLevelsLoader().load(transaction);
				return new LocationTreeToHeirarchicalNodesConverter().convert(locationTree, levels);
			}
		});
	 
	}
	
	public boolean hasPredefinedLocationTree() {
		return transactor.execute(new UnitOfWork<Boolean>() {
			public Boolean run(Transaction transaction) {
				return !factory.createPredefinedLocationListLoader().load(transaction).isEmpty();
			}
		});
	}


	public String getFullNameOfLocation(LocationWebModel location) {
		return getFullNameOfLocation(location.createLocation());
	}
	
	public String getFullNameOfLocation(Location location) {
		if (location == null) {
			return "";
		}
		
		Stack<String> names = new Stack<String>();
		
		addFreeform(names, location);
		
		addPredefined(names, location);
		
		return convertStackToString(names);
	}
	
	
	private String convertStackToString(Stack<String> names) {
		String fullName = "";
		while (!names.isEmpty()) {
			fullName += names.pop();
			if (!names.isEmpty()) {
				fullName += " ";
			}
		}
		return fullName;
	}



	private void addPredefined(Stack<String> names, Location location) {
		if (location.hasPredefinedLocation()) {
			PredefinedLocation predefinedLocation = location.getPredefinedLocation();
			
			names.push(predefinedLocation.getName());
			
			while (predefinedLocation.hasParent()) {
				names.push(predefinedLocation.getParent().getName());
				
				predefinedLocation = predefinedLocation.getParent();
			}
		}
	}



	private void addFreeform(Stack<String> names, Location location) {
		if (location.hasFreeForm()) { 
			names.push(location.getFreeformLocation());
		}
	}


	

	
	
}
