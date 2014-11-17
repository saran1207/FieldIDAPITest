package com.n4systems.fieldid.service.predefinedlocation;


import com.n4systems.fieldid.service.CrudService;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.location.PredefinedLocationTree;
import com.n4systems.model.location.PredefinedLocationTreeNode;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;

public class PredefinedLocationService extends CrudService<PredefinedLocation> {

	public PredefinedLocationService() {
		super(PredefinedLocation.class);
	}

	public PredefinedLocationTree loadLocationTree() {
		PredefinedLocationTree tree = new PredefinedLocationTree();
		persistenceService
				.findAll(createUserSecurityBuilder(PredefinedLocation.class)
				.addWhere(WhereClauseFactory.createIsNull("parent")))
				.stream()
				.forEach(node -> tree.addNode(loadLocationTreeNode(node)));
		return tree;
	}

	public PredefinedLocationTreeNode loadLocationTreeNode(PredefinedLocation parent) {
		PredefinedLocationTreeNode parentNode = new PredefinedLocationTreeNode(parent);
		persistenceService
				.findAll(createUserSecurityBuilder(PredefinedLocation.class)
						.addLeftJoin("searchIds", "s")
						.addWhere(WhereClauseFactory.createPassthru("s = :parentSearchId", parent.getId()))        // find all nodes under this parent
						.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.NE, "id", parent.getId()))    // do not include the parent
						.addOrder("id"))																			// Order by id.  Since trees cannot be reorganized, higher nodes will always have lower ids.
				.stream()
				.forEachOrdered(parentNode::placeNodeInTree);	// Place each node in the parent tree
		return parentNode;
	}

	@Override
	public PredefinedLocation save(PredefinedLocation model) {
		if (model.getParent() == null) {
			if (model.getOwner() == null) {
				model.setOwner(getCurrentUser().getOwner());
			}
		} else {
			model.setOwner(model.getParent().getOwner());
		}

		PredefinedLocation savedModel = super.save(model);
		savedModel.rebuildSearchIds(getEntityManager());
		return savedModel;
	}

	@Override
	public PredefinedLocation update(PredefinedLocation location) {
		PredefinedLocation updatedLocation = super.update(location);
		// All child nodes must have their owners updated to match the parent
		loadLocationTreeNode(updatedLocation).forEach(node -> {
			// the parent node has already been updated
			if (!node.getId().equals(updatedLocation.getId()) && !node.getNodeValue().getOwner().equals(updatedLocation.getOwner())) {
				node.getNodeValue().setOwner(updatedLocation.getOwner());
				super.update(node.getNodeValue());
			}
		});
		return updatedLocation;
	}
}
