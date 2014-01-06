package com.n4systems.fieldid.wicket.pages.org;


import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.GoogleMap;
import com.n4systems.fieldid.wicket.components.navigation.BreadCrumbBar;
import com.n4systems.fieldid.wicket.components.org.CreatePlacePanel;
import com.n4systems.fieldid.wicket.components.tree.OrgTree;
import com.n4systems.fieldid.wicket.data.OrgsDataProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.util.JavascriptUtil;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.util.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class OrgViewPage extends FieldIDTemplatePage {

    enum PageState { TREE, LIST };

    public static final int ITEMS_PER_PAGE = 20;

    private @SpringBean PersistenceService persistenceService;
    private @SpringBean JavascriptUtil javascriptUtil;

    private Component createForm;
    private OrgTree orgTree;
    private final WebMarkupContainer buttons;
    private final Component filter;
    private WebMarkupContainer container;
    private AbstractDefaultAjaxBehavior listBehavior;
    private PageState pageState = PageState.TREE;
    private WebMarkupContainer tree;
    private CreatePlacePanel createPanel;
    private WebMarkupContainer blankSlate;

    private String textFilter = null;
    private DataView<BaseOrg> dataTable;
    private OrgsDataProvider provider = new OrgsDataProvider() {
        @Override protected String getTextFilter() {
            return textFilter;
        }
    };

    public OrgViewPage() {
        container = new WebMarkupContainer("container");
        add(container.setOutputMarkupId(true));
        container.add(createTree("tree"));

        add(buttons = new WebMarkupContainer("buttons"));
        buttons.setOutputMarkupId(true);
        buttons.add(new AjaxLink("tree") {
            @Override public void onClick(AjaxRequestTarget target) {
                updatePage(target, PageState.TREE);
            }
        }.add(new AttributeAppender("class", getButtonModel(PageState.TREE), " ")));
        add(filter = new TextField("filter", new PropertyModel(this, "textFilter")).setOutputMarkupId(true));

        add(createPanel = new CreatePlacePanel("createPanel") {
            @Override protected IModel<String> getCssClass() {
                return Model.of("column-wide");
            }

            @Override protected void onCancel(AjaxRequestTarget target) {
                super.onCancel(target);
                toggleCreatePanel(target);
            }

            @Override protected void onCreate(BaseOrg childOrg, AjaxRequestTarget target) {
                super.onCreate(childOrg, target);
                persistenceService.save(childOrg);
                toggleCreatePanel(target);
                if(childOrg.isSecondary()) {
                    orgTree.refresh(target);
                } else {
                    orgTree.updateBranch(getParentOrg().getId(), childOrg.getId(), target);
                }
            }
        });

        add(blankSlate = new WebMarkupContainer("blankSlate"));
        blankSlate.setVisible(FieldIDSession.get().getUserSecurityGuard().isAllowedManageEndUsers());
        blankSlate.setOutputMarkupPlaceholderTag(true);
    }

    private void toggleCreatePanel(AjaxRequestTarget target) {
        blankSlate.setVisible(createPanel.isFormVisible());
        target.add(blankSlate, createPanel.toggle(), getTopFeedbackPanel());
        if (createPanel.isFormVisible()) {
            javascriptUtil.scrollToTop(target);
        }
    }

    private IModel<String> getButtonModel(final PageState state) {
        return new Model<String>() {
            @Override public String getObject() {
                return pageState.equals(state) ? "selected" : "";
            }
        };
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.places"));
    }

    @Override
    protected void addBreadCrumbBar(String breadCrumbBarId) {
        add(new BreadCrumbBar(breadCrumbBarId).setVisible(false));
    }

    private void updatePage(AjaxRequestTarget target, Object state) {
        if (!state.equals(pageState)) {
            textFilter = "";
            pageState = (PageState) state;
            target.add(container, buttons);
        }
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/pages/places.css");
        response.renderJavaScriptReference("javascript/component/autoComplete.js");
        response.renderJavaScriptReference("https://maps.googleapis.com/maps/api/js?sensor=false", GoogleMap.GOOGLE_MAP_API_ID);
        response.renderJavaScriptReference("javascript/googleMaps.js", GoogleMap.GOOGLE_MAPS_JS_ID);
    }

    private Component createTree(String id) {
        add(tree = new WebMarkupContainer(id) {
            @Override
            public boolean isVisible() {
                return PageState.TREE.equals(pageState);
            }
        });
        final AbstractDefaultAjaxBehavior actionsBehavior = new AbstractDefaultAjaxBehavior() {
            protected void respond(final AjaxRequestTarget target) {
                IRequestParameters params = RequestCycle.get().getRequest().getRequestParameters();
                Long parentOrgId = params.getParameterValue("orgId").toLong();
                BaseOrg parent = persistenceService.findById(BaseOrg.class,parentOrgId);
                blankSlate.setVisible(false);
                target.add(createPanel.resetModelObject(parent).show(), blankSlate);
                javascriptUtil.scrollToTop(target);
            }
        };

        tree.add(orgTree = new OrgTree("orgTree") {
            @Override
            public String getFilterComponent() {
                return "#" + filter.getMarkupId();
            }
        }.withMenuCallback(actionsBehavior));
        return tree;
    }

    private String getMatchingNameText(String name, String textFilter) {
        int index = StringUtils.indexOfIgnoreCase(name, textFilter);
        if (index==-1) {
            return name;
        }
        String highlightedMatchFormat = "%s<span class='match'>%s</span>%s";
        return String.format(highlightedMatchFormat, name.substring(0,index), name.substring(index,index+textFilter.length()), name.substring(index+textFilter.length()));
    }

}
