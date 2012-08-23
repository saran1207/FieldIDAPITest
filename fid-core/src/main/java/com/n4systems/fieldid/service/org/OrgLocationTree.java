package com.n4systems.fieldid.service.org;

import com.google.common.collect.Maps;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.parents.EntityWithTenant;

import java.util.*;

public class OrgLocationTree {

    private Map<EntityWithTenant,OrgLocationTreeNode> nodes;
    private OrgLocationTreeNode rootNode;

    public OrgLocationTree() {
        nodes = Maps.newHashMap();
        rootNode = new OrgLocationTreeNode(null,null);
    }

    public OrgLocationTree addOrgs(Iterable<BaseOrg> orgs) {
        for (BaseOrg org:orgs) {
            addOrg(org);
        }
        return this;
    }

    public OrgLocationTree addOrg(BaseOrg org) {
        OrgLocationTreeNode node = getOrgNode(org);
        return this;
    }

    private OrgLocationTreeNode getOrgNode(BaseOrg org) {
        if (org==null) {
            return rootNode;
        }
        OrgLocationTreeNode node = nodes.get(org);
        if (node==null) {
            OrgLocationTreeNode parentNode = getOrgNode(org.getParent());
            node=new OrgTreeNode(org, parentNode.getNodeEntity());
            parentNode.addChild(org);
            nodes.put(org,node);
        }
        return node;
    }

    public OrgLocationTree addPredefinedLocations(Iterable<PredefinedLocation> locations) {
        for (PredefinedLocation location:locations) {
            addPredefinedLocation(location);
        }
        return this;
    }

    public OrgLocationTree addPredefinedLocation(PredefinedLocation location) {
        OrgLocationTreeNode node = getPredefinedLocationNode(location);
        return this;
    }

    private OrgLocationTreeNode getPredefinedLocationNode(PredefinedLocation location) {
        OrgLocationTreeNode node = nodes.get(location);
        if (node==null) {
            OrgLocationTreeNode parent = getPredefinedLocationParentNode(location);
            node=new LocationTreeNode(location, parent.getNodeEntity());
            parent.addChild(location);
            nodes.put(location,node);
        }
        return node;
    }

    private OrgLocationTreeNode getPredefinedLocationParentNode(PredefinedLocation location) {
        EntityWithTenant parent = location.getParent() == null ? location.getOwner() : location.getParent();
        if (parent instanceof BaseOrg) {
            return getOrgNode((BaseOrg) parent);
        } else if (parent instanceof PredefinedLocation) {
            return getPredefinedLocationNode((PredefinedLocation) parent);
        }
        return null;
    }

    public Set<EntityWithTenant> getRootChildren() {
        return rootNode.getChildren();
    }

    public Set<EntityWithTenant> getChildrenOf(EntityWithTenant entity) {
        OrgLocationTreeNode node = nodes.get(entity);
        return node==null ? null : node.getChildren();
    }


    // ----------------------------------------------------------------

    class OrgLocationTreeNode<T extends EntityWithTenant> {
        private EntityWithTenant parent;
        private Set<EntityWithTenant> children;
        private T entity;

        OrgLocationTreeNode(T entity, EntityWithTenant parent) {
            this.entity = entity;
            this.parent = parent;
            children = new TreeSet<EntityWithTenant>(new OrgLocationComparator());
        }

        public T getNodeEntity() {
            return entity;
        }

        public void addChild(EntityWithTenant child) {
            children.add(child);
        }

        boolean isRootNode() {
            return parent==null;
        }

        public Set<EntityWithTenant> getChildren() {
            return children;
        }
    }

    class OrgTreeNode extends OrgLocationTreeNode<BaseOrg> {

        OrgTreeNode(BaseOrg entity, EntityWithTenant parent) {
            super(entity, parent);
        }
    }

    class LocationTreeNode extends OrgLocationTreeNode<PredefinedLocation> {

        LocationTreeNode(PredefinedLocation location, EntityWithTenant parent) {
            super(location, parent);
        }
    }

    class OrgLocationComparator implements Comparator<EntityWithTenant> {
        @Override
        public int compare(EntityWithTenant o1, EntityWithTenant o2) {
            if (o1==null) {
                return o2==null ? 0 : 1;
            }
            if (o2==null) {
                return -1;
            }
            int compare = comareAsOrg(o1,o2);
            if (compare==0) {
                compare = compareAsPredefinedLocation(o1,o2);
            }
            return compare;
        }

        private int comareAsOrg(EntityWithTenant o1, EntityWithTenant o2) {
            if (o1 instanceof BaseOrg && o2 instanceof  BaseOrg) {
                return ((BaseOrg)o1).getName().compareTo(((BaseOrg)o2).getName());
            }
            return 0;
        }

        private int compareAsPredefinedLocation(EntityWithTenant o1, EntityWithTenant o2) {
            if (o1 instanceof PredefinedLocation && o2 instanceof  PredefinedLocation) {
                return ((PredefinedLocation)o1).getName().compareTo(((PredefinedLocation)o2).getName());
            }
            return 0;
        }
    }

}
