package com.n4systems.fieldid.wicket.components.org;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.org.OrgLocationTree;
import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.fieldid.wicket.components.tree.JsonTreeNode;
import com.n4systems.fieldid.wicket.components.tree.Tree;
import com.n4systems.model.orgs.BaseOrg;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.TextField;
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
        });
        setOutputMarkupPlaceholderTag(true);
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

    private List<JsonTreeNode> buildJsonTree(OrgLocationTree tree) {
        List<JsonTreeNode> result = Lists.newArrayList();
        for (OrgLocationTree.OrgLocationTreeNode node:tree.getRootChildren()) {
            if (node.isIncluded()) {
                result.add(createNode(node, node.getChildren()));
            }
        }
        return result;
    }

    private JsonTreeNode createNode(OrgLocationTree.OrgLocationTreeNode node, Set<OrgLocationTree.OrgLocationTreeNode> children) {
        JsonTreeNode jsonNode = new JsonTreeNode(node.getEntity());

        List<JsonTreeNode> nodes = Lists.newArrayList();
        for (OrgLocationTree.OrgLocationTreeNode child:children) {
            if (child.isIncluded()) {
                nodes.add(createNode(child, child.getChildren()));
            }
        }
        jsonNode.setChildren(nodes);
        if (node.matches()) {
            jsonNode.addAttribute("class", "match");
        }
        return jsonNode;
    }

}
