package com.n4systems.fieldid.wicket.components.tree;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.org.OrgLocationTree;
import com.n4systems.fieldid.service.org.OrgService;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;
import java.util.Set;

public class OrgTree extends Tree {

    private static final String INIT_ORGTREE_JS = "var %s = orgTreeFactory.create(%s);";
    public static final String NODE_HTML = "<a href='www.google.com'>%s</a>" +
            "<span>%s</span>" +
            "<span class='timeago' title='%s'>xx</span>" +
            "<span class='timeago' title='%s'>xx</span>";

    private @SpringBean OrgService orgService;

    public OrgTree(String id) {
        super(id);
    }

    @Override
    protected List<JsonTreeNode> getNodes(String search) {
        return buildJsonTree(getOrgTree(search));
    }

    @Override
    protected List<JsonTreeNode> getChildNodes(Long parentNodeId, String type) {
        return buildJsonTree(getOrgTree(parentNodeId, getNodeType(type)));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderJavaScriptReference("javascript/component/orgTree.js");
        response.renderJavaScriptReference("javascript/jquery.timeago.js");
        response.renderOnDomReadyJavaScript("jQuery.timeago.settings.allowFuture = true;");
    }

    protected OrgLocationTree getOrgTree(String search) {
        return orgService.getOrgTree(search);
    }

    protected OrgLocationTree getOrgTree(Long parentNodeId, OrgLocationTree.NodeType type) {
        return orgService.getOrgTree(parentNodeId, type);
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

    private OrgLocationTree.NodeType getNodeType(String type) {
        return OrgLocationTree.NodeType.valueOf(type);
    }

    private JsonTreeNode createNode(OrgLocationTree.OrgLocationTreeNode node, Set<OrgLocationTree.OrgLocationTreeNode> children, JsonTreeNode parent) {
        // arggh.  Gson won't work with inner classes so instead of overriding the "isLeaf" method i have to awkwardly set it here.
        // TODO DD : refactor and make a OrgJsonTreeNode with this set.
        JsonTreeNode jsonNode = new JsonTreeNode(node, parent).withName(String.format(NODE_HTML, node.getName(), node.getIdentifier(), node.getCreated(), node.getModified())).setLeafType(OrgLocationTree.NodeType.DIVISION_ORG);


        List<JsonTreeNode> nodes = Lists.newArrayList();
        for (OrgLocationTree.OrgLocationTreeNode child:children) {
            if (child.isIncluded()) {
                nodes.add(createNode(child, child.getChildren(), jsonNode));
            }
        }
        jsonNode.setChildren(nodes);
        if (Boolean.TRUE.equals(node.matches())) {
            jsonNode.addAttribute("class", "match");
            openParents(parent);
        }
        return jsonNode;
    }

    private void openParents(JsonTreeNode parent) {
        while (parent!=null) {
            parent.setState("open");
            parent = parent.getParent();
        }
    }

    protected String getInitTreeJs() {
        return String.format(INIT_ORGTREE_JS,getJsVariableName(),convertToJson(new TreeOptions()));
    }

    // ------------------------------------------------------------

    class TreeOptions {
        String updateCallback = ajaxBehavior.getCallbackUrl().toString();
        String id = OrgTree.this.getMarkupId();
        String clickCallback = getWebRequest().getContextPath() + "/w/orgSummary";
    }



}
