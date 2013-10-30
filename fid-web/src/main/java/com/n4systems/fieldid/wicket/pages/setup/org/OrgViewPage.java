package com.n4systems.fieldid.wicket.pages.setup.org;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.navigation.MattBar;
import com.n4systems.fieldid.wicket.data.OrgsDataProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.orgs.*;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextField;
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

import java.util.List;
import java.util.Map;

public class OrgViewPage extends FieldIDFrontEndPage {

    // TODO : refactor ajaxTextField into separate component.
    private static final String INIT_TEXT_FIELD_JS = "var %s = autoCompleter.createAjaxTextField(%s)";
    private TextField<String> listText;
    private Class<BaseOrg> typeFilter;

    enum PageState { TREE, LIST };

    public static final int ITEMS_PER_PAGE = 20;
    private static final Map<Class<? extends BaseOrg>,IModel<String>> labelMap = Maps.newHashMap();

    static {
        labelMap.put(PrimaryOrg.class, new FIDLabelModel("label.primary_type"));
        labelMap.put(SecondaryOrg.class, new FIDLabelModel("label.secondary_type"));
        labelMap.put(DivisionOrg.class, new FIDLabelModel("label.division_type"));
        labelMap.put(CustomerOrg.class, new FIDLabelModel("label.customer_type"));
    }


    private OrgViewPanel panel;
    private String textFilter = null;
    private Class<? extends BaseOrg> filterClass = null;
    private Component tree;
    private AbstractDefaultAjaxBehavior behaviour;
    private PageState pageState = PageState.LIST;


    public OrgViewPage() {
        add(new MattBar("buttons") {
                    @Override protected void onEnterState(AjaxRequestTarget target, Object state) {
                        buttonClicked(target, state);
                    }
                }
                .setCurrentState(PageState.LIST)
                .addLink(new FIDLabelModel("label.tree"), PageState.TREE)
                .addLink(new FIDLabelModel("label.list"), PageState.LIST)
        );
        add(panel=new OrgViewPanel("orgView"));
    }

    private void buttonClicked(AjaxRequestTarget target, Object state) {
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
        AjaxTextFieldOptions options = new AjaxTextFieldOptions(panel.getMarkupId(), behaviour.getCallbackUrl().toString());
        response.renderOnDomReadyJavaScript(String.format(INIT_TEXT_FIELD_JS, getJsVariableName(), new Gson().toJson(options)));
    }

    private String getJsVariableName() {
        return getMarkupId()+"_text";
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
                    BaseOrg org = item.getModelObject();
                    item.add(new Label("name", org.getName()));
                    item.add(new Label("id", org instanceof ExternalOrg ? ((ExternalOrg) org).getCode() : org.getName()));
                    item.add(new Label("type", getTypeString(org)));
                    item.add(new Label("parent", org.getParent() == null ? "-" : org.getParent().getName()));
                    item.add(new Label("created", org.getCreated().toString()));
                    item.add(new Label("modified", org.getModified().toString()));
                }
            });
            dataTable.setCurrentPage(0);
            behaviour = new AbstractDefaultAjaxBehavior() {
                protected void respond(final AjaxRequestTarget target) {
                    IRequestParameters params = RequestCycle.get().getRequest().getRequestParameters();
                    textFilter = params.getParameterValue("text").toString();
                    target.add(list);
                }
            };
            dataTable.add(behaviour);

            list.add(new PagingNavigator("navigator", dataTable) {
                @Override public boolean isVisible() {
                    return PageState.LIST.equals(pageState) && dataTable.getRowCount()>1;
                }
            });
            list.add(dataTable);
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
        }

    }

    class AjaxTextFieldOptions {
        String callback;
        Integer delay = 500;
        String id;
        String selector = ".org-list input[type=text]";
        
        public AjaxTextFieldOptions(String id, String url) {
            this.id = id;
            this.callback = url;
        }
    }

}
