package com.n4systems.model.location;

import com.n4systems.persistence.Transaction;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;

public class PredefinedLocationTreeLoader {

	private final PredefinedLocationListLoader loader;

	public PredefinedLocationTreeLoader(PredefinedLocationListLoader loader) {
		this.loader = loader;
	}

    public PredefinedLocationTree load(Transaction transaction) {
        return load(transaction.getEntityManager());
    }

    public PredefinedLocationTree load(EntityManager em) {
        PredefinedLocationTree root = new PredefinedLocationTree();
        HashMap<PredefinedLocation, PredefinedLocationTreeNode> locationParents = new HashMap<PredefinedLocation, PredefinedLocationTreeNode>();

        List<PredefinedLocation> locations = loader.load(em);
        for (PredefinedLocation location : locations) {
            PredefinedLocationTreeNode node = new PredefinedLocationTreeNode(location);
            if (location.getParent() == null) {
                root.addNode(node);
            } else {
                locationParents.get(location.getParent()).addChild(node);
            }
            locationParents.put(location, node);
        }

        return root;
    }



	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
