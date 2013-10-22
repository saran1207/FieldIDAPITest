package com.n4systems.fieldid.service.org;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.orgs.*;
import com.n4systems.model.parents.EntityWithTenant;
import org.apache.commons.lang.StringUtils;

import java.util.*;

public class OrgLocationTree {

    public enum NodeType {
        LOCATION(PredefinedLocation.class), INTERNAL_ORG(InternalOrg.class),CUSTOMER_ORG(CustomerOrg.class), DIVISION_ORG(DivisionOrg.class),VOID(null);

        private final Class nodeClass;

        NodeType(Class nodeClass) {
            this.nodeClass = nodeClass;
        }

        public static NodeType fromClass(Class clazz) {
            if (clazz==null) {
                return VOID;
            }

            for (NodeType type:values()) {
                if (type.nodeClass.isAssignableFrom(clazz)) {
                    return type;
                }
            }
            return VOID;
        }
    }

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
        rootNode=new OrgLocationTreeNode();
    }

    public OrgLocationTree(List<? extends BaseOrg> orgs) {
        this();
        for (BaseOrg org:orgs) {
            OrgLocationTreeNode node=new OrgTreeNode(org);
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
            node=new OrgTreeNode(org);
            parentNode.addChild(node);
            nodes.put(org,node);
        }
        return node;
    }

    @Deprecated // locations not tested yet.  not required for functionality. consider implementation incomplete.
    public OrgLocationTree addPredefinedLocations(Iterable<PredefinedLocation> locations) {
        for (PredefinedLocation location:locations) {
            addPredefinedLocation(location);
        }
        return this;
    }

    @Deprecated // locations not tested yet.  not required for functionality. consider implementation incomplete.
    public OrgLocationTree addPredefinedLocation(PredefinedLocation location) {
        getPredefinedLocationNode(location);
        return this;
    }

    @Deprecated // locations not tested yet.  not required for functionality. consider implementation incomplete.
    private OrgLocationTreeNode getPredefinedLocationNode(PredefinedLocation location) {
        OrgLocationTreeNode node = nodes.get(location);
        if (node==null) {
            OrgLocationTreeNode parent = getPredefinedLocationParentNode(location);
            node=new LocationTreeNode(location);
            parent.addChild(node);
            nodes.put(location,node);
        }
        return node;
    }

    @Deprecated // locations not tested yet.  not required for functionality. consider implementation incomplete.
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

    private void filterIfNeeded() {
        if (filter!=null && isFiltered==false) {
            for (OrgLocationTreeNode node:nodes.values()) {
                switch (node.getType()) {
                    case DIVISION_ORG:
                        node.isLeaf = true;
                        break;
                    default:
                        node.isLeaf = false;
                        break;
                }
                if (node.matches()) {
                    node.included=Boolean.TRUE;
                    includeParents(node);
                }
            }
        }
        isFiltered = true;
    }

    public int size() {
        return nodes.size();
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
                    return input==null ? false : input.toLowerCase().trim().indexOf(n) >= 0;
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
        private Long id;
        private String name;
        private Boolean included;
        private Boolean isLeaf = null;
        private NodeType type;

        OrgLocationTreeNode() {
            this.id = -1L;
            this.name = "root";
            this.type = NodeType.VOID;
            this.children = new TreeSet<OrgLocationTreeNode>(new OrgLocationComparator());
        }

        OrgLocationTreeNode(T entity) {
            this.id = entity.getId();
            this.type = NodeType.fromClass(entity.getClass());
            this.name = entity instanceof BaseOrg ? ((BaseOrg)entity).getName() :
                        entity instanceof PredefinedLocation ? ((PredefinedLocation)entity).getName() :
                        "root";
            children = new TreeSet<OrgLocationTreeNode>(new OrgLocationComparator());
            Preconditions.checkArgument(entity!=null,"can't have null entity for tree node");
        }

        public boolean isIncluded() {
            // recall : if no matching performed, then ALL nodes are included.
            return filter==null || Boolean.TRUE.equals(included) || matches();
        }

        public void setParent(OrgLocationTreeNode parent) {
            this.parent = parent;
        }

        public void addChild(OrgLocationTreeNode child) {
            children.add(child);
            child.setParent(this);
        }

        public Long getId() {
            return id;
        }

        boolean isRootNode() {
            return parent==null;
        }

        public OrgLocationTreeNode getParent() {
            return parent;
        }

        public NodeType getType() {
            return type;
        }

        public Set<OrgLocationTreeNode> getChildren() {
            return children;
        }

        public String getName() {
            return name;
        }

        public boolean matches() {
            return (filter!=null) ? filter.apply(name) : false;
        }

        public boolean isLeaf() {
            return Boolean.TRUE.equals(isLeaf);
        }
    }

    class OrgTreeNode extends OrgLocationTreeNode<BaseOrg> {

        OrgTreeNode(BaseOrg entity) {
            super(entity);
        }

    }

    class LocationTreeNode extends OrgLocationTreeNode<PredefinedLocation> {

        LocationTreeNode(PredefinedLocation location) {
            super(location);
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
            if (o1.getType().equals(o2.getType())) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
            return NodeType.INTERNAL_ORG.equals(o1.getType()) ? -1 : 1;
        }

    }

}
