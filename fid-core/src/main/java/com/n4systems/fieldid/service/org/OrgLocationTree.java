package com.n4systems.fieldid.service.org;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.SecondaryOrg;
import com.n4systems.model.parents.EntityWithTenant;
import org.apache.commons.lang.StringUtils;

import java.util.*;

public class OrgLocationTree {

    private Map<EntityWithTenant,OrgLocationTreeNode> nodes = Maps.newHashMap();
    private OrgLocationTreeNode rootNode;
    private Predicate<String> filter;
    private boolean isFiltered = false;
    private Predicate<OrgLocationTreeNode> predicate = new Predicate<OrgLocationTreeNode>() {
        @Override public boolean apply(OrgLocationTreeNode input) {
            return Boolean.TRUE.equals(input.included) ? false : true;
        }

        @Override public boolean equals(Object object) {
            return this == object;
        }
    };

    public OrgLocationTree() {
        rootNode=new OrgLocationTreeNode(null,null);
    }

    public OrgLocationTree(List<? extends BaseOrg> orgs) {
        this();
        for (BaseOrg org:orgs) {
            OrgLocationTreeNode node=new OrgTreeNode(org, null);
            rootNode.addChild(node);
            nodes.put(org,node);
        }
    }

    public OrgLocationTree addOrgs(Iterable<? extends BaseOrg> orgs) {
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
        if (org==null) {  // TODO DD : or if org=rootNode?
            return rootNode;
        }
        OrgLocationTreeNode node = nodes.get(org);
        if (node==null) {
            // even tho secondary orgs have parent, we treat them as top level orgs (on same level as primary org).
            OrgLocationTreeNode parentNode = org instanceof SecondaryOrg ? rootNode : getOrgNode(org.getParent());
            node=new OrgTreeNode(org, (BaseOrg) parentNode.getNodeEntity());
            parentNode.addChild(node);
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
        getPredefinedLocationNode(location);
        return this;
    }

    private OrgLocationTreeNode getPredefinedLocationNode(PredefinedLocation location) {
        OrgLocationTreeNode node = nodes.get(location);
        if (node==null) {
            OrgLocationTreeNode parent = getPredefinedLocationParentNode(location);
            node=new LocationTreeNode(location, parent.getNodeEntity());
            parent.addChild(node);
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

    public Set<OrgLocationTreeNode> getRootChildren() {
        filterIfNeeded();
        return rootNode.getChildren();
    }

    // TODO DD : optimize this...
    private void filterIfNeeded() {
        if (filter!=null && isFiltered==false) {
            for (OrgLocationTreeNode node:nodes.values()) {
                if (node.matches) {
                    node.included=Boolean.TRUE;
                    includeParents(node);
                }
            }
        }
        isFiltered = true;
    }

    private void includeParents(OrgLocationTreeNode node) {
        OrgLocationTreeNode parent = node.getParent();
        while (parent!=null) {
            parent.included = Boolean.TRUE;
            parent = parent.getParent();
        }
    }

    public OrgLocationTree withFilter(final String name) {
        if (!StringUtils.isBlank(name)) {
            final String n = name.toLowerCase();
            filter = new Predicate<String>() {
                @Override public boolean apply(String input) {
                    return input.toLowerCase().trim().indexOf(n) >= 0;
                }
            };
        }
        return this;
    }

    // ----------------------------------------------------------------

    public class OrgLocationTreeNode<T extends EntityWithTenant> {
        private OrgLocationTreeNode parent;
        private Set<OrgLocationTreeNode> children;
        // TODO DD : do i need to store entity or just id?  entire entity might be way too heavy?
        private T entity;
        private String name;
        private Boolean matches = null;
        private Boolean included;

        OrgLocationTreeNode(T entity, EntityWithTenant parent) {
            this.entity = entity;
            this.name = entity instanceof BaseOrg ? ((BaseOrg)entity).getName() :
                    entity instanceof PredefinedLocation ? ((PredefinedLocation)entity).getName() :
                            "root";
            children = new TreeSet<OrgLocationTreeNode>(new OrgLocationComparator());
            matches = (filter!=null) ? filter.apply(name) : null;
        }

        public boolean isIncluded() {
            // recall : if no matching performed, then ALL nodes are included.
            return matches==null  || Boolean.TRUE.equals(included);
        }

        public T getEntity() {
            return entity;
        }

        public void setParent(OrgLocationTreeNode parent) {
            this.parent = parent;
        }

        public T getNodeEntity() {
            return entity;
        }

        public void addChild(OrgLocationTreeNode child) {
            children.add(child);
            child.setParent(this);
        }


        boolean isRootNode() {
            return parent==null;
        }

        public OrgLocationTreeNode getParent() {
            return parent;
        }

        public Set<OrgLocationTreeNode> getChildren() {
            return children;
        }

        public String getName() {
            return name;
        }

        public boolean matches() {
            return Boolean.TRUE.equals(matches);
        }


    }

    class OrgTreeNode extends OrgLocationTreeNode<BaseOrg> {

        OrgTreeNode(BaseOrg entity, BaseOrg parent) {
            super(entity, parent);
        }

    }

    class LocationTreeNode extends OrgLocationTreeNode<PredefinedLocation> {

        LocationTreeNode(PredefinedLocation location, EntityWithTenant parent) {
            super(location, parent);
        }
    }

    class OrgLocationComparator implements Comparator<OrgLocationTreeNode> {
        @Override
        public int compare(OrgLocationTreeNode o1, OrgLocationTreeNode o2) {
            if (o1==null) {
                return o2==null ? 0 : 1;
            }
            if (o2==null) {
                return -1;
            }
            if (o1.getClass().equals(o2.getClass())) {
                return o1.getClass().equals(LocationTreeNode.class) ? compareAsPredefinedLocation(((LocationTreeNode) o1).getNodeEntity(), ((LocationTreeNode) o2).getNodeEntity()) : compareAsOrg(((OrgTreeNode) o1).getNodeEntity(), ((OrgTreeNode) o2).getNodeEntity());
            }
            return o1 instanceof OrgTreeNode ? -1 : 1;
        }

        private int compareAsOrg(BaseOrg org1, BaseOrg org2) {
            return org1.getName().compareTo(org2.getName());
        }

        private int compareAsPredefinedLocation(PredefinedLocation loc1, PredefinedLocation loc2) {
            return loc1.getName().compareTo(loc2.getName());
        }
    }

}
