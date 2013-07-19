package com.n4systems.fieldid.wicket.components.tree;

import java.util.Map;

public class TreeNodeJsonData {
    private String data;
    private Map<String,String> attr = null;
    private String icon;
    private String metadata = null;
    private String language;
    private String state = null;
    private TreeNodeJsonData[] children;

    public TreeNodeJsonData(String data) {
        this(data,null);
    }

    public TreeNodeJsonData(String data, TreeNodeJsonData[] children) {
        this.data = data;
        this.children = children;
        state = (children!=null && children.length>0) ? getDefaultState(data) : null;
    }

    protected String getDefaultState(String data) {
        return "closed";
    }
}

