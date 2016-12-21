package com.n4systems.model.orgs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rrana on 2016-12-13.
 */
public class OrgIdTree {
    Long id;
    List<OrgIdTree> children;

    public OrgIdTree() {
        children = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<OrgIdTree> getChildren() {
        return children;
    }

    public void setChildren(List<OrgIdTree> children) {
        this.children = children;
    }
}
