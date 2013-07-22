package com.n4systems.fieldid.wicket.components.org;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.org.OrgLocationTree;
import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.fieldid.wicket.components.tree.JsonTreeNode;
import com.n4systems.fieldid.wicket.components.tree.Tree;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.DivisionOrg;
import org.apache.wicket.Component;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;
import java.util.Set;

@Deprecated  //R&D component to try out JS tree
public class OrgLocationPicker extends Panel {

    private @SpringBean OrgService orgService;

    private Tree tree;
    private Component text;
    private final HiddenField type;
    private final HiddenField entityId;
    private WebMarkupContainer icon;
    private boolean includeLocations = false;

    // TODO DD : need to store org & location into resulting model.
    // .: use OrgLocationTreeNode as underlying model???
    public OrgLocationPicker(String id, IModel<BaseOrg> orgModel) {
        super(id);
        add(text = new TextField("text", orgModel));
        add(type = new HiddenField("type"));
        add(entityId = new HiddenField("entityId"));
        add(tree = new Tree("tree") {
            @Override protected List<JsonTreeNode> getNodes(String search) {
                return buildJsonTree(getOrgLocationTree(search));
            }

            @Override protected List<JsonTreeNode> getChildNodes(Long parentNodeId,String type) {
                return buildJsonTree(getOrgLocationTree(parentNodeId,getNodeType(type)));
            }

            @Override protected List<JsonTreeNode> getInitialNodes() {
                return buildJsonTree(getInitialOrgTree());
            }

        });
        add(new ContextImage("icon","images/search-icon.png") {
            @Override protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                tag.put("onClick",String.format("%s.toggleTree()",tree.getJsVariableName()));
            }
        });
        setOutputMarkupPlaceholderTag(true);
    }

    private Class<?> getNodeType(String type) {
        try {
            return Class.forName(type);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("can't figure out type of node " + type);
        }
    }

    public OrgLocationPicker withLocations() {
        includeLocations = true;
        return this;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderCSSReference("style/component/orgLocationPicker.css");
    }

    protected OrgLocationTree getOrgLocationTree(String search) {
        return includeLocations ? orgService.getOrgLocationTree(search) : orgService.getOrgTree(search);
    }

    protected OrgLocationTree getOrgLocationTree(Long parentNodeId, Class type) {
        return includeLocations ? orgService.getOrgLocationTree(parentNodeId,type) : orgService.getOrgTree(parentNodeId,type);
    }

    private OrgLocationTree getInitialOrgTree() {
        return orgService.getTopLevelOrgTree();
    }

    private List<JsonTreeNode> buildJsonTree(OrgLocationTree tree) {
        List<JsonTreeNode> result = Lists.newArrayList();
        for (OrgLocationTree.OrgLocationTreeNode node:tree.getRootChildren()) {
            if (node.isIncluded()) {
                result.add(createNode(node, node.getChildren(), null));
            }
        }
        return result;
    }

    private JsonTreeNode createNode(OrgLocationTree.OrgLocationTreeNode node, Set<OrgLocationTree.OrgLocationTreeNode> children, JsonTreeNode parent) {
        // arggh.  Gson won't work with inner classes so instead of overriding the "isLeaf" method i have to awkwardly set it here.
        JsonTreeNode jsonNode = new JsonTreeNode(node.getEntity(), parent).setLeafClass(DivisionOrg.class);

        List<JsonTreeNode> nodes = Lists.newArrayList();
        for (OrgLocationTree.OrgLocationTreeNode child:children) {
            if (child.isIncluded()) {
                nodes.add(createNode(child, child.getChildren(), jsonNode));
            }
        }
        jsonNode.setChildren(nodes);
        if (node.matches()) {
            jsonNode.addAttribute("class", "match");
            setParentsToOpen(parent);
        }
        return jsonNode;
    }

    private void setParentsToOpen(JsonTreeNode parent) {
        while (parent!=null) {
            parent.setState("open");
            parent = parent.getParent();
        }
    }

}
