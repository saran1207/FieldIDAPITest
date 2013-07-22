package com.n4systems.fieldid.wicket.components.tree;

import com.google.common.base.CaseFormat;
import com.google.common.collect.Maps;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.parents.EntityWithTenant;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;

public class JsonTreeNode {
    private transient JsonTreeNode parent;
    private transient boolean isLeaf = false;

    String data;
    Map<String,String> attr;
    String state = "closed";
    JsonTreeNode[] children = null;


    public JsonTreeNode(EntityWithTenant entity, JsonTreeNode parent) {
        this.data = getName(entity);
        addAttribute("id", entity.getId() + "");
        addAttribute("data", entity.getClass().getName());
        addAttribute("class", CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, entity.getClass().getSimpleName()));
        this.parent = parent;
    }

    public void setChildren(List<JsonTreeNode> nodes) {
        if (nodes!=null && nodes.size()>0) {
            children =  nodes.toArray(new JsonTreeNode[nodes.size()]);
        } else {
            children = isLeaf() ? null : new JsonTreeNode[0];  // don't do this if i know it's leaf level?  how do i know that???
            state = isLeaf() ? null : "closed";
        }
    }

    public Class getClassFromData() {
        try {
            return Class.forName(attr.get("data"));
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    protected boolean isLeaf() {
        return isLeaf;
    }

    protected String getName(EntityWithTenant entity) {
        if (entity instanceof BaseOrg) {
            return ((BaseOrg)entity).getName();
        } else if (entity instanceof PredefinedLocation) {
            return ((PredefinedLocation)entity).getName();
        }
        return entity.getId()+"";
    }

    public void addAttribute(String key, String value) {
        if (attr==null) {
            attr = Maps.newHashMap();
        }
        String v = attr.get(key);
        // CAVEAT : assume space delimiter for now.
        attr.put(key,StringUtils.isBlank(v) ? value : v + " " + value);
    }

    public String getData() {
        return data;
    }

    public void setState(String state) {
        this.state = state;
    }

    public JsonTreeNode getParent() {
        return parent;
    }

    public JsonTreeNode setLeafClass(Class leafClass) {
        this.isLeaf = leafClass.isAssignableFrom(getClassFromData());
        if (isLeaf) {
            children = null;
            state = null;
        }
        return this;
    }
}
