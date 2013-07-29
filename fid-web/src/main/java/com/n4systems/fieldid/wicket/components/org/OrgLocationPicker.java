package com.n4systems.fieldid.wicket.components.org;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.org.OrgLocationTree;
import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.fieldid.wicket.components.tree.JsonTreeNode;
import com.n4systems.fieldid.wicket.components.tree.Tree;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.parents.EntityWithTenant;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;
import java.util.Set;

public class OrgLocationPicker<T extends EntityWithTenant> extends FormComponentPanel<T> {

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


    public OrgLocationPicker(String id, IModel<T> orgModel) {
        super(id,orgModel);
        T entity = orgModel.getObject();
        if (entity!=null) {
            entityId = entity.getId() + "";
            entityType = OrgLocationTree.NodeType.fromClass(entity.getClass()).name();
            if (entity instanceof BaseOrg) {
                input = ((BaseOrg) entity).getName();
            } else if (entity instanceof PredefinedLocation) {
                input = ((PredefinedLocation)entity).getName();
            }
        }

        add(text = new TextField("text", new PropertyModel(this, "input")));
        add(_type = new HiddenField("type", new PropertyModel(this,"entityType")));
        add(_entityId = new HiddenField("entityId", new PropertyModel(this,"entityId")));
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
        add(new WebMarkupContainer("icon") {
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

    // this feature isn't totally finished yet.  the service stuff is 90% but the storing in the model part isn't.
    // need to beef up the convertInput method to fetch the proper entity. also need to have a model that contains an Org AND/OR Location in it.
    @Deprecated
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
        // TODO DD : refactor and make a OrgJsonTreeNode with this set.
        JsonTreeNode jsonNode = new JsonTreeNode(node, parent).setLeafType(OrgLocationTree.NodeType.DIVISION_ORG);

        List<JsonTreeNode> nodes = Lists.newArrayList();
        for (OrgLocationTree.OrgLocationTreeNode child:children) {
            if (child.isIncluded()) {
                nodes.add(createNode(child, child.getChildren(), jsonNode));
            }
        }
        jsonNode.setChildren(nodes);
        if (Boolean.TRUE.equals(node.matches())) {
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
        T entity = null;
        String rawInput = _type.getRawInput();
        if (!StringUtils.isBlank(rawInput)) {
            OrgLocationTree.NodeType type = OrgLocationTree.NodeType.valueOf(rawInput);
            switch (type) {
                case LOCATION:
                    entity = (T) persistenceService.findById(BaseOrg.class,  getEntityIdAsLong());
                    break;
                case INTERNAL_ORG:
                case CUSTOMER_ORG:
                case DIVISION_ORG:
                    entity = (T) persistenceService.findById(BaseOrg.class,  getEntityIdAsLong());
                    break;
                case VOID:
                    entity = null;
                    break;
                default:
                    throw new IllegalStateException("can't convert input of type " + type + "(" + entityType + ")");
            }
        }
        setConvertedInput(entity);
    }

    public String getEntityId() {
        return entityId;
    }

    public Long getEntityIdAsLong() {
        String id = _entityId.getRawInput();
        return StringUtils.isNotBlank(id) ? Long.parseLong(_entityId.getRawInput()) : null;
    }

    public String getInput() {
        return input;
    }
}
