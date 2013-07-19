package com.n4systems.fieldid.wicket.components.tree;

import com.google.common.base.CaseFormat;
import com.google.gson.Gson;
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

    // TODO DD: refactor this into separate abstract jsTree component.
    private static final String INIT_TREE_JS = "treeFactory.createAndShow('%s', '%s');";
//    private static final String UPDATE_TREE_JS = "%s.update(%s);";
    private final AbstractDefaultAjaxBehavior ajaxBehavior;
    private String search = null;


    public Tree(String id) {
        super(id);
        setOutputMarkupPlaceholderTag(true);
        add(ajaxBehavior=createAjaxHandler());
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
                search = params.getParameterValue("search").toString();
                TextRequestHandler handler = new TextRequestHandler("application/json","UTF-8", getTreeJsonData(search));
                RequestCycle.get().scheduleRequestHandlerAfterCurrent(handler);
            }

        };
    }

    private String getInitTreeJs() {
        String result = String.format(INIT_TREE_JS, getParentMarkupId(), ajaxBehavior.getCallbackUrl().toString());
        return result;
    }

    private String getParentMarkupId() {
        return getParent().getMarkupId();
    }

    protected String getTreeJsonData(String search) {
        return new Gson().toJson(getTreeData(search));
    }

    protected JsonTreeNode[] getTreeData(String search) {
        List<JsonTreeNode> nodes = getNodes(search);
        return nodes.toArray(new JsonTreeNode[nodes.size()]);
    }

    protected abstract List<JsonTreeNode> getNodes(String search);

}
