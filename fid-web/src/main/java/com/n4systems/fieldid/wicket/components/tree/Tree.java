package com.n4systems.fieldid.wicket.components.tree;

import com.google.common.base.CaseFormat;
import com.google.gson.GsonBuilder;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.TextRequestHandler;

import java.util.List;

public abstract class Tree extends Panel {

    private static final String INIT_TREE_JS = "%s = treeFactory.create('%s',%s);";

    private final AbstractDefaultAjaxBehavior ajaxBehavior;
    private String search = null;


    public Tree(String id) {
        super(id);
        setOutputMarkupPlaceholderTag(true);
        add(ajaxBehavior = createAjaxHandler());
        // CAVEAT : if you change class, be forewarned that you may have to change CSS!
        add(new AttributeAppender("class", CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, getClass().getSimpleName().toString())));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderJavaScriptReference("javascript/jstree/_lib/jquery.hotkeys.js");
        response.renderJavaScriptReference("javascript/jstree/jquery.jstree.js");
        response.renderJavaScriptReference("javascript/component/tree.js");
        response.renderOnDomReadyJavaScript(getInitTreeJs());
    }

    public AbstractDefaultAjaxBehavior createAjaxHandler() {
        return new AbstractDefaultAjaxBehavior() {

            protected void respond(final AjaxRequestTarget target) {
                IRequestParameters params = RequestCycle.get().getRequest().getRequestParameters();
                List<JsonTreeNode> data = null;
                if (!params.getParameterValue("nodeId").isEmpty()) {
                    long nodeId = params.getParameterValue("nodeId").toLong();
                    String type = params.getParameterValue("type").toString();
//                    System.out.println("getting children for " + type + " : " + nodeId);
                    data = getChildNodes(nodeId, type);
                } else {
                    search = params.getParameterValue("search").toString();
//                    System.out.println("searching for " + search);
                    data = getNodes(search);
                }
                TextRequestHandler handler = new TextRequestHandler("application/json","UTF-8", convertToJson(data));
                RequestCycle.get().scheduleRequestHandlerAfterCurrent(handler);
            }

        };
    }

    private String getInitTreeJs() {
        String url = ajaxBehavior.getCallbackUrl().toString();
        return String.format(INIT_TREE_JS,
                getJsVariableName(),
                getParentMarkupId(),
                convertToJson(new TreeOptions(url).withData(getInitialNodes())));
    }

    private String getParentMarkupId() {
        return getParent().getMarkupId();
    }

    private final String convertToJson(Object o) {
        return new GsonBuilder().create().toJson(o);
    }

    public String getJsVariableName() {
        return "tree_" + getMarkupId();
    }

    protected abstract List<JsonTreeNode> getNodes(String search);

    protected abstract List<JsonTreeNode> getChildNodes(Long parentNodeId,String type);

    protected List<JsonTreeNode> getInitialNodes() {
        return null;
    }


    // ----------------------------------------------------------------------------------------

    class TreeOptions {
        String url;
        List<JsonTreeNode> data;
//        List<String> selected;

        TreeOptions(String url) {
            this.url = url;
        }

//        TreeOptions withSelectedNodes(List<JsonTreeNode> data) {
//            List<String> result = Lists.newArrayList();
//            for (JsonTreeNode node:data) {
//                result.add(node.getId());
//            }
//            selected = result;
//            return this;
//        }
//
        TreeOptions withData(List<JsonTreeNode> data) {
            this.data = data;
            return this;
        }

    }

}
