package com.n4systems.fieldid.wicket.pages.setup.org;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.TimeAgoLabel;
import com.n4systems.fieldid.wicket.components.navigation.MattBar;
import com.n4systems.fieldid.wicket.components.tree.OrgTree;
import com.n4systems.fieldid.wicket.data.OrgsDataProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.org.OrgSummaryPage;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.orgs.*;
import com.n4systems.services.date.DateService;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;
import java.util.Map;

import static ch.lambdaj.Lambda.on;

public class OrgViewPage extends FieldIDFrontEndPage {

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

    private @SpringBean DateService dateService;

    private OrgViewPanel panel;
    private Class<? extends BaseOrg> filterClass = null;
    private OrgTree orgTree;
    private WebMarkupContainer container;
    private AbstractDefaultAjaxBehavior listBehavior;
    private AbstractDefaultAjaxBehavior treeBehavior;
    private PageState pageState = PageState.LIST;
    private WebMarkupContainer tree;
    private TextField<String> listText;
    private TextField<String> treeText;
    private Class<BaseOrg> typeFilter;
    private String textFilter = null;
    private BaseOrg org;


    public OrgViewPage() {
        container = new WebMarkupContainer("container");
        add(container.setOutputMarkupId(true));

        container.add(new MattBar("buttons") {
                    @Override protected void onEnterState(AjaxRequestTarget target, Object state) {
                        buttonClicked(target, state);
                    }
                }
                .setCurrentState(PageState.LIST)
                .addLink(new FIDLabelModel("label.tree"), PageState.TREE)
                .addLink(new FIDLabelModel("label.list"), PageState.LIST)
        );
        container.add(panel=new OrgViewPanel("orgView"));
    }

    private void buttonClicked(AjaxRequestTarget target, Object state) {
        textFilter = "";
        pageState = (PageState) state;
        target.add(panel);
    }

    private IModel<String> getTypeString(BaseOrg org) {
        IModel<String> model = labelMap.get(org.getClass());
        return model==null ? Model.of("-") : model;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderJavaScriptReference("javascript/component/autoComplete.js");
        response.renderOnDomReadyJavaScript(String.format(INIT_TEXT_FIELD_JS, new Gson().toJson(new AjaxTextFieldOptions())));
    }


    class OrgViewPanel extends Fragment {
        private DataView<BaseOrg> dataTable;
        private IDataProvider provider = new OrgsDataProvider() {
            @Override protected String getTextFilter() {
                return textFilter;
            }
            @Override protected Class<? extends BaseOrg> getTypeFilter() {
                return typeFilter;
            }
        };

        public OrgViewPanel(String id) {
            super(id, "orgViewFragment", OrgViewPage.this);
            setOutputMarkupId(true);
            add(new AttributeAppender("class","org-view"));
            addTree();
            addList();
        }

        private void addList() {
            final WebMarkupContainer list = new WebMarkupContainer("list") {
                @Override public boolean isVisible() {
                    return PageState.LIST.equals(pageState);
                }
            };
            list.setOutputMarkupId(true);

            add(dataTable = new DataView<BaseOrg>("table", provider, ITEMS_PER_PAGE) {
                @Override
                protected void populateItem(Item<BaseOrg> item) {
                    final BaseOrg org = item.getModelObject();
                    item.add(createLink("name",org).add(new Label("label", ProxyModel.of(item.getModelObject(), on(BaseOrg.class).getName()))));
                    String id = org instanceof ExternalOrg ? ((ExternalOrg) org).getCode() : org.getName();
                    item.add(createLink("id",org).add(new Label("label", Model.of(id))));
                    item.add(new Label("type", getTypeString(org)));
                    item.add(new Label("parent", org.getParent() == null ? "-" : org.getParent().getName()));
                    item.add(new TimeAgoLabel("created", Model.of(org.getCreated()),dateService.getUserTimeZone()));
                    item.add(new TimeAgoLabel("modified", Model.of(org.getModified()),dateService.getUserTimeZone()));
                }

                private Link createLink(String id, final BaseOrg org) {
                    return new Link(id) {
                        @Override public void onClick() {
                            setResponsePage(new OrgSummaryPage(org));
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
            list.add(new WebMarkupContainer("noResults")  {
                @Override public boolean isVisible() {
                    return dataTable.getRowCount()==0;
                }
            });
            list.add(listText = new TextField<String>("filter", new PropertyModel(OrgViewPage.this, "textFilter")));
            listText.setOutputMarkupId(true);
            List<Class<? extends BaseOrg>> filterTypes = Lists.newArrayList(PrimaryOrg.class,SecondaryOrg.class, CustomerOrg.class, DivisionOrg.class);
            IChoiceRenderer<Class<? extends BaseOrg>> filterTypeRenderer = new IChoiceRenderer<Class<? extends BaseOrg>>() {
                @Override public Object getDisplayValue(Class<? extends BaseOrg> clazz) {
                    return labelMap.get(clazz).getObject();
                }
                @Override public String getIdValue(Class<? extends BaseOrg> clazz, int index) {
                    return clazz.getSimpleName();
                }
            };
            final FidDropDownChoice<Class<? extends BaseOrg>> dropDown = new FidDropDownChoice<Class<? extends BaseOrg>>("orgType", new PropertyModel<Class<? extends BaseOrg>>(OrgViewPage.this, "filterClass"), filterTypes, filterTypeRenderer);
            dropDown.add(new AjaxFormComponentUpdatingBehavior("onchange") {
                @Override protected void onUpdate(AjaxRequestTarget target) {
                    typeFilter = (Class<BaseOrg>) dropDown.getDefaultModelObject();
                    dataTable.setCurrentPage(0);
                    target.add(list);
                }
            });
            list.add(dropDown.setNullValid(true));
            add(list);
        }

        private void addTree() {
            add(tree = new WebMarkupContainer("tree") {
                @Override public boolean isVisible() {
                    return PageState.TREE.equals(pageState);
                }
            });
            tree.add(orgTree = new OrgTree("orgTree"));
            tree.add(treeText = new TextField<String>("filter", new PropertyModel(OrgViewPage.this, "textFilter"))).setOutputMarkupId(true);
        }

    }

    class AjaxTextFieldOptions {
        String parent = "#"+container.getMarkupId();
        String callback = listBehavior.getCallbackUrl().toString();
        String child = ".org-list input[type=text]";
        Integer delay = 500;
    }

}
