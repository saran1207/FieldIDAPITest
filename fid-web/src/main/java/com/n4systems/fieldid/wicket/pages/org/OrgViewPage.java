package com.n4systems.fieldid.wicket.pages.org;


import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.wicket.components.GoogleMap;
import com.n4systems.fieldid.wicket.components.navigation.BreadCrumbBar;
import com.n4systems.fieldid.wicket.components.org.CreatePlacePanel;
import com.n4systems.fieldid.wicket.components.tree.OrgTree;
import com.n4systems.fieldid.wicket.data.OrgsDataProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.ExternalOrg;
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
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class OrgViewPage extends FieldIDTemplatePage {

    // TODO : refactor ajaxTextField into separate component.
//    private static final String INIT_TEXT_FIELD_JS = "autoCompleter.createAjaxTextField(%s)";

    enum PageState { TREE, LIST };

    public static final int ITEMS_PER_PAGE = 20;

    private @SpringBean PersistenceService persistenceService;


    private Component createForm;
    private OrgTree orgTree;
    private final WebMarkupContainer buttons;
    private final Component filter;
    private WebMarkupContainer container;
    private AbstractDefaultAjaxBehavior listBehavior;
    private PageState pageState = PageState.TREE;
    private WebMarkupContainer tree;
    private CreatePlacePanel createPanel;

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
//        container.add(createList("list"));

        add(buttons = new WebMarkupContainer("buttons"));
        buttons.setOutputMarkupId(true);
        buttons.add(new AjaxLink("tree") {
            @Override public void onClick(AjaxRequestTarget target) {
                updatePage(target, PageState.TREE);
            }
        }.add(new AttributeAppender("class", getButtonModel(PageState.TREE), " ")));
//        buttons.add(new AjaxLink("list") {
//            @Override public void onClick(AjaxRequestTarget target) {
//                updatePage(target, PageState.LIST);
//            }
//        }.add(new AttributeAppender("class", getButtonModel(PageState.LIST), " " )));
        add(filter = new TextField("filter", new PropertyModel(this, "textFilter")).setOutputMarkupId(true));

        add(createPanel = new CreatePlacePanel("createPanel") {
            @Override protected IModel<String> getCssClass() {
                return Model.of("column-wide narrow");
            }

            @Override protected void onCancel(AjaxRequestTarget target) {
                super.onCancel(target);
                toggleCreatePanel(target);
            }

            @Override protected void onCreate(BaseOrg org, AjaxRequestTarget target) {
                super.onCreate(org,target);
                toggleCreatePanel(target);
            }
        });

    }

    private void toggleCreatePanel(AjaxRequestTarget target) {
        // need to add some sort of feedback panel here.
        target.add(createPanel.toggle());
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
        response.renderCSSReference("style/pages/places.css");
        response.renderJavaScriptReference("javascript/component/autoComplete.js");
        response.renderJavaScriptReference("https://maps.googleapis.com/maps/api/js?sensor=false", GoogleMap.GOOGLE_MAP_API_ID);
        response.renderJavaScriptReference("javascript/googleMaps.js", GoogleMap.GOOGLE_MAPS_JS_ID);
//        response.renderOnDomReadyJavaScript(String.format(INIT_TEXT_FIELD_JS, new Gson().toJson(new AjaxTextFieldOptions())));
    }

    private Component  createList(String id) {
        final WebMarkupContainer list = new WebMarkupContainer(id) {
            @Override public boolean isVisible() {
                return PageState.LIST.equals(pageState);
            }
        };
        list.setOutputMarkupId(true);

        list.add(dataTable = new DataView<BaseOrg>("table", provider, ITEMS_PER_PAGE) {
            @Override protected void populateItem(Item<BaseOrg> item) {
                final BaseOrg org = item.getModelObject();
                item.add(createLink("name", org).add(new Label("label", getMatchingNameText(org.getName(), textFilter)).setEscapeModelStrings(false)));
                String id = org instanceof ExternalOrg ? ((ExternalOrg) org).getCode() : org.getName();
                item.add(createLink("id", org).add(new Label("label", Model.of(id))));
                item.add(new Label("parent", org.getParent() == null ? "-" : org.getParent().getName()));
            }

            private Link createLink(String id, final BaseOrg org) {
                return new Link(id) {
                    @Override public void onClick() {
                        setResponsePage(PlaceSummaryPage.class, new PageParameters().add("id", org.getId()));
                    }
                };
            }
        });
        dataTable.setCurrentPage(0);
        listBehavior = new AbstractDefaultAjaxBehavior() {
            protected void respond(final AjaxRequestTarget target) {
                IRequestParameters params = RequestCycle.get().getRequest().getRequestParameters();
                textFilter = params.getParameterValue("text").toString();
                dataTable.setCurrentPage(0);
                target.add(list);
            }
        };
        dataTable.add(listBehavior);

        list.add(new PagingNavigator("navigator", dataTable) {
            @Override public boolean isVisible() {
                return PageState.LIST.equals(pageState) && dataTable.getRowCount()>ITEMS_PER_PAGE; // if only one page, don't need this.
            }
        });
        list.add(dataTable);
        list.add(new WebMarkupContainer("noResults") {
            @Override public boolean isVisible() {
                return dataTable.getRowCount() == 0;
            }
        });
        return list;
    }

    private Component createTree(String id) {
        add(tree = new WebMarkupContainer(id) {
            @Override public boolean isVisible() {
                return PageState.TREE.equals(pageState);
            }
        });
        final AbstractDefaultAjaxBehavior actionsBehavior = new AbstractDefaultAjaxBehavior() {
            protected void respond(final AjaxRequestTarget target) {
                IRequestParameters params = RequestCycle.get().getRequest().getRequestParameters();
                Long parentOrgId = params.getParameterValue("orgId").toLong();
                BaseOrg parent = persistenceService.findById(BaseOrg.class,parentOrgId);
                target.add(createPanel.resetModelObject(parent).show());
            }
        };

        tree.add(orgTree = new OrgTree("orgTree") {
            @Override public String getFilterComponent() {
                return "#"+filter.getMarkupId();
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

//    class AjaxTextFieldOptions {
//        String parent = "#"+container.getMarkupId();
//        String target = "#"+container.getMarkupId();
//        String callback = listBehavior.getCallbackUrl().toString();
//        String child = ".input-combo";
//        Integer delay = 500;
//    }


}
