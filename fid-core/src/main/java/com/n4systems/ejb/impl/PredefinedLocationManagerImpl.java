package com.n4systems.ejb.impl;

import com.n4systems.ejb.PredefinedLocationManager;
import com.n4systems.model.location.*;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.SecurityFilter;

import javax.persistence.EntityManager;

public class PredefinedLocationManagerImpl implements PredefinedLocationManager {

    private EntityManager em;
    private PredefinedLocationSaver saver;

    public PredefinedLocationManagerImpl(EntityManager em) {
        this.em = em;
        this.saver = createSaver();
    }

    protected PredefinedLocationSaver createSaver() {
        return new PredefinedLocationSaver();
    }

    @Override
    public void updateChildrenOwner(SecurityFilter securityFilter, PredefinedLocation parentNode) {
        BaseOrg owner = parentNode.getOwner();
        PredefinedLocationTree locationTree = createPredefinedLocationTreeLoader(securityFilter).load(em);
        for (PredefinedLocationTreeNode node:locationTree.getNodes()) {
            if (parentNode.getId().equals(node.getId())) {
                updateOwnerForChildren(node,owner);
            }
        }
    }

    protected PredefinedLocationTreeLoader createPredefinedLocationTreeLoader(SecurityFilter securityFilter) {
        return new PredefinedLocationTreeLoader(createPredefinedLocationListLoader(securityFilter).withParentFirstOrder());
    }

    protected PredefinedLocationListLoader createPredefinedLocationListLoader(SecurityFilter securityFilter) {
        return new PredefinedLocationListLoader(securityFilter);
    }

    private void updateOwnerForChildren(PredefinedLocationTreeNode node, BaseOrg owner) {
        node.getNodeValue().setOwner(owner);
        for (PredefinedLocationTreeNode child:node.getChildren()) {
            child.getNodeValue().setOwner(owner);
            saver.update(child.getNodeValue());
            updateOwnerForChildren(child, owner);
        }
    }

}
