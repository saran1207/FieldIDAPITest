package com.n4systems.fieldid.wicket.components.org;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.org.OrgLocationTree;
import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.fieldid.wicket.components.tree.JsonTreeNode;
import com.n4systems.fieldid.wicket.components.tree.Tree;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.parents.EntityWithTenant;
import org.apache.wicket.Component;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;
import java.util.Set;

@Deprecated  //R&D component to try out JS tree
public class OrgLocationPicker extends FormComponentPanel<EntityWithTenant> {

    private @SpringBean OrgService orgService;
    private @SpringBean PersistenceService persistenceService;

    private Tree tree;
    private String input;
    private Component text;
    private final HiddenField _type;
    private final HiddenField _entityId;
    private WebMarkupContainer icon;
    private boolean includeLocations = false;
    private String entityId;
    private String entityType;

    // TODO DD : need to store org & location into resulting model.
    // .: use OrgLocationTreeNode as underlying model???
    public OrgLocationPicker(String id, IModel<EntityWithTenant> orgModel) {
        super(id,orgModel);
        add(text = new TextField("text", new PropertyModel(this,"input")));
        add(_type = new HiddenField("type", new PropertyModel(this,"entityType")));
        add(_entityId = new HiddenField("entityId", new PropertyModel(this,"id")));
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

    private OrgLocationTree.NodeType getNodeType(String type) {
        return OrgLocationTree.NodeType.valueOf(type);
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

    protected OrgLocationTree getOrgLocationTree(Long parentNodeId, OrgLocationTree.NodeType type) {
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
        JsonTreeNode jsonNode = new JsonTreeNode(node, parent).setLeafType(OrgLocationTree.NodeType.DIVISION_ORG);

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

    @Override
    protected void convertInput() {
        // load proper entity...
        EntityWithTenant entity = null;
        OrgLocationTree.NodeType type = OrgLocationTree.NodeType.valueOf(_type.getRawInput());
        switch (type) {
            case LOCATION:
                entity = persistenceService.findById(BaseOrg.class,  getEntityId());
                break;
            case INTERNAL_ORG:
            case CUSTOMER_ORG:
            case DIVISION_ORG:
                entity = persistenceService.findById(BaseOrg.class,  getEntityId());
                break;
            case VOID:
                entity = null;
                break;
            default:
                throw new IllegalStateException("can't convert input of type " + type + "(" + entityType + ")");
        }
        setConvertedInput(entity);
    }

    public Long getEntityId() {
        return Long.parseLong(_entityId.getRawInput());
    }


}
