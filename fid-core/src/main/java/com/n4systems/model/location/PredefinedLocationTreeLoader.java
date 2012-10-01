package com.n4systems.model.location;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;

public class PredefinedLocationTreeLoader extends SecurityFilteredLoader<PredefinedLocationTree> {

	private final PredefinedLocationListLoader loader;

	public PredefinedLocationTreeLoader(PredefinedLocationListLoader loader) {
        super(loader.getFilter());
		this.loader = loader;
	}

    public PredefinedLocationTree load(Transaction transaction) {
        return load(transaction.getEntityManager());
    }

    @Override
    protected PredefinedLocationTree load(EntityManager em, SecurityFilter filter) {
        return load(em);
    }

    public PredefinedLocationTree load(EntityManager em) {
        PredefinedLocationTree root = new PredefinedLocationTree();
        HashMap<PredefinedLocation, PredefinedLocationTreeNode> locationParents = new HashMap<PredefinedLocation, PredefinedLocationTreeNode>();

        List<PredefinedLocation> locations = loader.load(em);
        for (PredefinedLocation location : locations) {
            if (location.getParent() == null) {
                PredefinedLocationTreeNode node = new PredefinedLocationTreeNode(location);
                root.addNode(node);
                locationParents.put(location, node);
            }
        }

        for (PredefinedLocation location : locations) {
            if (location.getParent() != null) {
                PredefinedLocationTreeNode node = new PredefinedLocationTreeNode(location);
                locationParents.get(location.getParent()).addChild(node);
                locationParents.put(location, node);
            }
        }

        return root;
    }

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

    public PredefinedLocationTreeLoader withPrimaryOrgFiltering() {
        loader.withPrimaryOrgFiltering();
        return this;
    }
}
