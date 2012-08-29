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
        this.saver = new PredefinedLocationSaver();
    }

    @Override
    public void updateChildrenOwner(SecurityFilter securityFilter, PredefinedLocation parentNode) {
        BaseOrg owner = parentNode.getOwner();
        PredefinedLocationTree locationTree = new PredefinedLocationTreeLoader(new PredefinedLocationListLoader(securityFilter).withParentFirstOrder()).load(em);
        for (PredefinedLocationTreeNode node:locationTree.getNodes()) {
            if (parentNode.getId().equals(node.getId())) {
                updateOwnerForChildren(node,owner);
            }
        }
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
