package com.n4systems.fieldid.viewhelpers;

import java.util.List;

import com.n4systems.fieldid.actions.product.LocationWebModel;
import com.n4systems.model.location.Location;
import com.n4systems.model.location.PredefinedLocationLevels;
import com.n4systems.model.location.PredefinedLocationTree;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.Transactor;
import com.n4systems.persistence.UnitOfWork;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.uitags.views.HierarchicalNode;

public class LocationHelper {

	private final LoaderFactory factory;
	private final Transactor transactor;

	public LocationHelper(LoaderFactory factory, Transactor transactor) {
		this.factory = factory;
		this.transactor = transactor;
	}

	public List<HierarchicalNode> findSiblingsByParent(Long parentId) {
		List<HierarchicalNode> firstLevel = getPredefinedLocationTree();

		HierarchicalNode parentNode = findNodeById(parentId, firstLevel);

		return (parentNode == null) ? firstLevel : parentNode.getChildren();
	}

	public HierarchicalNode findNodeById(Long id, List<HierarchicalNode> firstLevel) {
		HierarchicalNode findNode = null;
		for (HierarchicalNode node : firstLevel) {
			findNode = node.findById(id);

			if (findNode != null) {
				break;
			}
		}
		return findNode;
	}

	public List<HierarchicalNode> getPredefinedLocationTree() {
		return transactor.execute(new UnitOfWork<List<HierarchicalNode>>() {
			public List<HierarchicalNode> run(Transaction transaction) {
				PredefinedLocationTree locationTree = factory.createPredefinedLocationTreeLoader().load(transaction);
				PredefinedLocationLevels levels = factory.createPredefinedLocationLevelsLoader().load(transaction);
				return new LocationTreeToHierarchicalNodesConverter().convert(locationTree, levels);
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
		return location.getFullName();
	}
}
