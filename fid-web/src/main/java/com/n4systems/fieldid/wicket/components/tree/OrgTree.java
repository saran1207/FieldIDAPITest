package com.n4systems.fieldid.wicket.components.tree;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.org.OrgLocationTree;
import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.util.StringUtils;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;
import java.util.Set;

public class OrgTree extends Tree {

    private static final String INIT_ORGTREE_JS = "var %s = orgTreeFactory.create(%s);";
    public static final String NODE_NAME_HTML = "<span>%s</span>";
    public static final String NODE_NAME_HIGHTLIGHTED_HTML = "%s<span class='match'>%s</span>%s";
    public static final String NODE_HTML = "<span class='org' >%s</span>";

    private @SpringBean OrgService orgService;
    private String lastSearch = null;

    // TODO : add "root node id".  if null get entire tree else get single root node.

    public OrgTree(String id) {
        super(id);
    }

    @Override
    protected List<JsonTreeNode> getNodes(String search) {
        return buildJsonTree(getOrgTree(lastSearch = search));
    }

    @Override
    protected List<JsonTreeNode> getChildNodes(Long parentNodeId, String type) {
        return buildJsonTree(getOrgTree(parentNodeId, getNodeType(type)));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderJavaScriptReference("javascript/component/orgTree.js");
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
        // NOTE :  Gson won't work with inner classes so instead of overriding the "isLeaf" method i have to awkwardly set it here.
        JsonTreeNode jsonNode = new JsonTreeNode(node, parent).withName(getHighlightedNodeName(node)).setLeafType(OrgLocationTree.NodeType.DIVISION_ORG);

        List<JsonTreeNode> nodes = Lists.newArrayList();
        for (OrgLocationTree.OrgLocationTreeNode child:children) {
            if (child.isIncluded()) {
                nodes.add(createNode(child, child.getChildren(), jsonNode));
            }
        }
        jsonNode.setChildren(nodes);
        if (Boolean.TRUE.equals(node.matches())) {
            openParents(parent);
        }
        return jsonNode;
    }

    private String getHighlightedNodeName(OrgLocationTree.OrgLocationTreeNode node) {
        String name = node.getName();
        int index = StringUtils.indexOfIgnoreCase(name, lastSearch);
        if (index==-1) {
            name = String.format(NODE_NAME_HTML,name);
        } else {
            name = String.format(NODE_NAME_HIGHTLIGHTED_HTML, name.substring(0, index), name.substring(index, index + lastSearch.length()), name.substring(index + lastSearch.length()));
        }
        // TODO : is "linked" information needed???
        String cssClass = node.isLinked() ? "linked" : "";
        return String.format(NODE_HTML, name);
    }

    private void openParents(JsonTreeNode parent) {
        while (parent!=null) {
            parent.setState("open");
            parent = parent.getParent();
        }
    }

    protected String getInitTreeJs() {
        return String.format(INIT_ORGTREE_JS,getJsVariableName(),convertToJson(createTreeOptions()));
    }

    protected TreeOptions createTreeOptions() {
        return new OrgTreeOptions();
    }

    // ------------------------------------------------------------

    class OrgTreeOptions extends TreeOptions {
        String clickCallback = getWebRequest().getContextPath() + "/w/placeSummary";

        OrgTreeOptions() {
            super();
        }
    }

}
