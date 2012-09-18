package com.n4systems.fieldid.viewhelpers;

import com.n4systems.fieldid.actions.asset.LocationWebModel;
import com.n4systems.model.location.*;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.UserSecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.Transactor;
import com.n4systems.persistence.UnitOfWork;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.uitags.views.HierarchicalNode;

import java.util.List;
import java.util.TimeZone;

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

    public List<HierarchicalNode> getPredefinedLocationTree(final BaseOrg owner, final User user) {
        return transactor.execute(new UnitOfWork<List<HierarchicalNode>>() {
            public List<HierarchicalNode> run(Transaction transaction) {
                UserSecurityFilter filter = new UserSecurityFilter(owner, user.getId(), TimeZone.getDefault());
                PredefinedLocationTree locationTree = new PredefinedLocationTreeLoader(new PredefinedLocationListLoader(filter)).load(transaction);
                PredefinedLocationLevels levels = factory.createPredefinedLocationLevelsLoader().load(transaction);
                List<HierarchicalNode> tree = new LocationTreeToHierarchicalNodesConverter().convert(locationTree, levels);
                return tree;
            }
        });
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
