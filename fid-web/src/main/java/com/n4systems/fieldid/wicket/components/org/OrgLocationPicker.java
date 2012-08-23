package com.n4systems.fieldid.wicket.components.org;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.n4systems.fieldid.service.org.OrgLocationTree;
import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.parents.EntityWithTenant;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.events.Event;
import org.odlabs.wiquery.core.events.MouseEvent;
import org.odlabs.wiquery.core.events.WiQueryEventBehavior;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.ui.core.JsScopeUiEvent;

import java.util.List;
import java.util.Map;
import java.util.Set;


public class OrgLocationPicker extends Panel {

    private @SpringBean OrgService orgService;

    // TODO DD: refactor this into separate jsTree component.
    private static final String TREE_JS = "%s = treeFactory.create('%s','%s','%s',%s);";


    private Component tree;
    private AutoCompleteOrgPicker autoComplete;
    private WebMarkupContainer icon;

    public OrgLocationPicker(String id, IModel<BaseOrg> orgModel, IModel<PredefinedLocation> locationModel) {
        super(id);
        add(autoComplete = new AutoCompleteOrgPicker("autoComplete", orgModel, locationModel));
        add(tree=new WebMarkupContainer("tree").setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true));
        add(icon = new WebMarkupContainer("icon"));
        icon.add(new WiQueryEventBehavior(new Event(MouseEvent.CLICK) {
            @Override public JsScope callback() {
                return JsScopeUiEvent.quickScope(" $('#" + tree.getMarkupId() + "').toggle();");
            }
        }));
        icon.add(new ContextImage("image", "images/setup.png"));
        setOutputMarkupPlaceholderTag(true);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderJavaScriptReference("javascript/jstree/_lib/jquery.hotkeys.js");
        response.renderJavaScriptReference("javascript/jstree/jquery.jstree.js");
        response.renderJavaScriptReference("javascript/component/tree.js");
        response.renderCSSReference("style/component/orgLocationPicker.css");
        response.renderOnDomReadyJavaScript(getInitTreeJs());
    }

    private String getInitTreeJs() {
        String result = String.format(TREE_JS, getJsVariableName(), tree.getMarkupId(), autoComplete.getTextMarkupId(), autoComplete.getHiddenMarkupId(), getTreeJsonData());
        return result;
    }

    protected String getTreeJsonData() {
        Object data = getTreeData();
        return new Gson().toJson(data);
    }

    protected Object getTreeData() {
        OrgLocationTree result = orgService.getOrgLocationTree();
        return buildJsonTree(result);
    }

    private JsonTreeNode[] buildJsonTree(OrgLocationTree tree) {
        List<JsonTreeNode> result = Lists.newArrayList();
        result.addAll(buildJsonTree(tree, tree.getRootChildren()));
        return result.toArray(new JsonTreeNode[0]);
    }

    private List<JsonTreeNode> buildJsonTree(OrgLocationTree tree, Set<EntityWithTenant> children) {
        List<JsonTreeNode> result = Lists.newArrayList();
        for (EntityWithTenant entity:children) {
            result.add(createNode(tree,entity, tree.getChildrenOf(entity), "open"));
        }
        return result;
    }

    private JsonTreeNode createNode(OrgLocationTree tree, EntityWithTenant entity, Set<EntityWithTenant> children, String state) {
        JsonTreeNode node = new JsonTreeNode(entity);
        List<JsonTreeNode> nodes = Lists.newArrayList();
        for (EntityWithTenant child:children) {
            nodes.add(createNode(tree, child, tree.getChildrenOf(child), "closed"));
        }
        node.state = state;
        node.setChildren(nodes);
        return node;
    }

    protected String getName(EntityWithTenant entity) {
        if (entity instanceof BaseOrg) {
            return ((BaseOrg)entity).getName();
        } else if (entity instanceof  PredefinedLocation) {
            return ((PredefinedLocation)entity).getName();
        }
        return entity.getId()+"";
    }

    public String getJsVariableName() {
        return get("tree").getMarkupId()+"_tree";
    }


    public class JsonTreeNode {
        private String data;
        private Map<String,Object> attr;
        private String icon;
        private Map<String,Object> metadata = Maps.newHashMap();
        private String language;
        private String state = "closed";
        private JsonTreeNode[] children = null;

        public JsonTreeNode(EntityWithTenant entity) {
            this.data = getName(entity);
            metadata.put("id", entity.getId());
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

        private void addAttribute(String key, Object value) {
            if (attr==null) {
                attr = Maps.newHashMap();
            }
            attr.put(key,value);
        }

    }


}
