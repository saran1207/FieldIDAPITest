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
    String data;
    Map<String,String> attr;
    String icon;
    Map<String,Object> metadata = Maps.newHashMap();
    String language;
    public String state = "open";
    JsonTreeNode[] children = null;

    public JsonTreeNode(EntityWithTenant entity) {
        this.data = getName(entity);
        metadata.put("entityId", entity.getId());
        metadata.put("type", entity instanceof BaseOrg ? "org" : "loc");
        metadata.put("css", CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, entity.getClass().getSimpleName()));
        //addAttribute();  add dynamic css class here...!!
    }

    public void setChildren(List<JsonTreeNode> nodes) {
        if (nodes!=null && nodes.size()>0) {
            children =  nodes.toArray(new JsonTreeNode[0]);
        } else {
            icon = "/fieldid/images/asset-statuses-icon.png";
            addAttribute("class","jstree-leaf");
        }
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

    public JsonTreeNode[] getChildren() {
        return children;
    }

    public String getData() {
        return data;
    }

}
