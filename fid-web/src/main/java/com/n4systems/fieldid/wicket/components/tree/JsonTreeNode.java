package com.n4systems.fieldid.wicket.components.tree;

import com.google.common.base.CaseFormat;
import com.google.common.collect.Maps;
import com.n4systems.fieldid.service.org.OrgLocationTree;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;

public class JsonTreeNode {
    private transient JsonTreeNode parent;
    private transient boolean isLeaf = false;

    // TODO DD : add matching substring.  i.e. aBCDe  where BCD is matched part of string.  need this if going to highlight it in browser.
    String data;
    Map<String,String> attr;
    String state = "closed";
    JsonTreeNode[] children = null;
    String id;

    public JsonTreeNode(OrgLocationTree.OrgLocationTreeNode node, JsonTreeNode parent) {
        this.data = node.getName();
        this.id = node.getId() + "";
        addAttribute("id", node.getId() + "");
        addAttribute("data", node.getType().name());
        addAttribute("class", getClass(node));
        this.parent = parent;
        this.isLeaf = node.isLeaf();
    }

    private String getClass(OrgLocationTree.OrgLocationTreeNode node) {
        String cssClass = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, node.getType().name());
        if (Boolean.TRUE.equals(node.isPrimary())) {
            cssClass += " primary";
        }
        return cssClass;
    }

    public void setChildren(List<JsonTreeNode> nodes) {
        if (nodes!=null && nodes.size()>0) {
            children =  nodes.toArray(new JsonTreeNode[nodes.size()]);
        } else {
            children = isLeaf() ? null : new JsonTreeNode[0];  // don't do this if i know it's leaf level?  how do i know that???
            state = isLeaf() ? null : "closed";
        }
    }

    public OrgLocationTree.NodeType getNodeTypeFromData() {
        return OrgLocationTree.NodeType.valueOf(attr.get("data"));
    }

    protected boolean isLeaf() {
        return isLeaf;
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

    public JsonTreeNode setLeafType(OrgLocationTree.NodeType type) {
        if (type!=null) {
            if (type.equals(getNodeTypeFromData())) {
                isLeaf = true;
            }
            if (isLeaf) {
                children = null;
                state = null;
            }
        }
        return this;
    }

    public String getId() {
        return attr.get("id");
    }

    public JsonTreeNode withName(String name) {
        this.data = name;
        return this;
    }
}
