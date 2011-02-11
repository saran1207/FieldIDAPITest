package com.n4systems.model.location;

import java.util.HashMap;
import java.util.List;

import com.n4systems.persistence.loaders.Loader;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.n4systems.persistence.Transaction;

import javax.persistence.EntityManager;

public class PredefinedLocationTreeLoader extends Loader<PredefinedLocationTree> {

	private final PredefinedLocationListLoader loader;

	public PredefinedLocationTreeLoader(PredefinedLocationListLoader loader) {
		this.loader = loader;
	}

    @Override
    protected PredefinedLocationTree load(EntityManager em) {
        return load((Transaction)null);
    }

    public PredefinedLocationTree load(Transaction transaction) {
		PredefinedLocationTree root = new PredefinedLocationTree();
		HashMap<PredefinedLocation, PredefinedLocationTreeNode> locationParents = new HashMap<PredefinedLocation, PredefinedLocationTreeNode>();

		List<PredefinedLocation> locations = loader.load();
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
