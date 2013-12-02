package com.n4systems.fieldid.wicket.pages.setup.org;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.n4systems.fieldid.wicket.components.navigation.BreadCrumbBar;
import com.n4systems.fieldid.wicket.components.tree.OrgTree;
import com.n4systems.fieldid.wicket.data.OrgsDataProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.NavigationItem;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.org.PlaceSummaryPage;
import com.n4systems.model.orgs.*;
import com.n4systems.util.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
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

import java.util.List;
import java.util.Map;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

public class OrgViewPage extends FieldIDTemplatePage {

    // TODO : refactor ajaxTextField into separate component.
    private static final String INIT_TEXT_FIELD_JS = "autoCompleter.createAjaxTextField(%s)";

    enum PageState { TREE, LIST };

    public static final int ITEMS_PER_PAGE = 20;
    private static final Map<Class<? extends BaseOrg>,IModel<String>> labelMap = Maps.newHashMap();

    static {
        labelMap.put(PrimaryOrg.class, new FIDLabelModel("label.primary_type"));
        labelMap.put(SecondaryOrg.class, new FIDLabelModel("label.secondary_type"));
        labelMap.put(DivisionOrg.class, new FIDLabelModel("label.division_type"));
        labelMap.put(CustomerOrg.class, new FIDLabelModel("label.customer_type"));
    }

    private Class<? extends BaseOrg> filterClass = null;
    private OrgTree orgTree;
    private final WebMarkupContainer buttons;
    private final Component filter;
    private WebMarkupContainer container;
    private AbstractDefaultAjaxBehavior listBehavior;
    private AbstractDefaultAjaxBehavior treeBehavior;
    private PageState pageState = PageState.LIST;
    private WebMarkupContainer tree;
    private String textFilter = null;
    private BaseOrg org;
    private DataView<BaseOrg> dataTable;
    private OrgsDataProvider provider = new OrgsDataProvider() {
        @Override protected String getTextFilter() {
            return textFilter;
        }
    };


    public OrgViewPage() {
        container = new WebMarkupContainer("container");
        add(container.setOutputMarkupId(true));
        add(buttons = new WebMarkupContainer("buttons"));
        buttons.add(new AjaxLink("tree") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                updatePage(target, PageState.TREE);
            }
        });
        buttons.add(new AjaxLink("list") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                updatePage(target, PageState.LIST);
            }
        });
        add(filter = new TextField("filter", new PropertyModel(this, "textFilter")).setOutputMarkupId(true));
        addTree();
        addList();
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.places"));
    }

    @Override
    protected void addBreadCrumbBar(String breadCrumbBarId) {
        List<NavigationItem> navItems = Lists.newArrayList();
        navItems.add(aNavItem().label("label.places").page(OrgViewPage.class).build());
        add(new BreadCrumbBar(breadCrumbBarId, navItems.toArray(new NavigationItem[0])));
    }

    private void updatePage(AjaxRequestTarget target, Object state) {
        textFilter = "";
        pageState = (PageState) state;
        target.add(container);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderJavaScriptReference("javascript/component/autoComplete.js");
        response.renderOnDomReadyJavaScript(String.format(INIT_TEXT_FIELD_JS, new Gson().toJson(new AjaxTextFieldOptions())));
    }


    private void addList() {
        final WebMarkupContainer list = new WebMarkupContainer("list") {
            @Override public boolean isVisible() {
                return PageState.LIST.equals(pageState);
            }
        };
        list.setOutputMarkupId(true);

        list.add(dataTable = new DataView<BaseOrg>("table", provider, ITEMS_PER_PAGE) {
            @Override
            protected void populateItem(Item<BaseOrg> item) {
                final BaseOrg org = item.getModelObject();
                item.add(createLink("name", org).add(new Label("label", getMatchingNameText(org.getName(), textFilter)).setEscapeModelStrings(false)));
                String id = org instanceof ExternalOrg ? ((ExternalOrg) org).getCode() : org.getName();
                item.add(createLink("id", org).add(new Label("label", Model.of(id))));
                item.add(new Label("parent", org.getParent() == null ? "-" : org.getParent().getName()));
            }

            private Link createLink(String id, final BaseOrg org) {
                return new Link(id) {
                    @Override
                    public void onClick() {
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
            @Override
            public boolean isVisible() {
                return dataTable.getRowCount() == 0;
            }
        });
        container.add(list);
    }

    private void addTree() {
        container.add(tree = new WebMarkupContainer("tree") {
            @Override public boolean isVisible() {
                return PageState.TREE.equals(pageState);
            }
        });
        tree.add(orgTree = new OrgTree("orgTree") {
            @Override public String getFilterComponent() {
                return "#"+filter.getMarkupId();
            }
        });
    }

    private String getMatchingNameText(String name, String textFilter) {
        int index = StringUtils.indexOfIgnoreCase(name, textFilter);
        if (index==-1) {
            return name;
        }
        String highlightedMatchFormat = "%s<span class='match'>%s</span>%s";
        return String.format(highlightedMatchFormat, name.substring(0,index), name.substring(index,index+textFilter.length()), name.substring(index+textFilter.length()));
    }


    class AjaxTextFieldOptions {
        String parent = "#"+container.getMarkupId();
        String target = "#"+container.getMarkupId();
        String callback = listBehavior.getCallbackUrl().toString();
        String child = ".input-combo";
        Integer delay = 500;
    }

}
