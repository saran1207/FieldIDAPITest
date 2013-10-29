package com.n4systems.fieldid.wicket.pages.setup.org;


import com.google.common.collect.Maps;
import com.n4systems.fieldid.wicket.components.navigation.MattBar;
import com.n4systems.fieldid.wicket.data.OrgsDataProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.orgs.*;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.util.Map;

public class OrgViewPage extends FieldIDFrontEndPage {

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
    private String search = null;
    private Component tree;
    private PageState pageState = PageState.LIST;


    public OrgViewPage() {
        add(new MattBar("buttons") {
                    @Override protected void onEnterState(AjaxRequestTarget target, Object state) {
                        buttonClicked(target,state);
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


    class OrgViewPanel extends Fragment {
        private DataView<BaseOrg> dataTable;
        private IDataProvider provider = new OrgsDataProvider();

        public OrgViewPanel(String id) {
            super(id, "orgViewFragment", OrgViewPage.this);
            setOutputMarkupId(true);
            add(new AttributeAppender("class","org-view"));
            add(tree = new WebMarkupContainer("tree") {
                @Override public boolean isVisible() {
                    return PageState.TREE.equals(pageState);
                }
            });
            add(dataTable = new DataView<BaseOrg>("table", provider, ITEMS_PER_PAGE) {
                @Override protected void populateItem(Item<BaseOrg> item) {
                    BaseOrg org = item.getModelObject();
                    item.add(new Label("name", org.getName() ));
                    item.add(new Label("id", org instanceof ExternalOrg ? ((ExternalOrg)org).getCode() : org.getName() ));
                    item.add(new Label("type", getTypeString(org) ));
                    item.add(new Label("parent", org.getParent()==null ? "-" : org.getParent().getName() ));
                    item.add(new Label("created", org.getCreated().toString()));
                    item.add(new Label("modified", org.getModified().toString() ));
                }
                @Override public boolean isVisible() {
                    return PageState.LIST.equals(pageState);
                }
            });
            dataTable.setCurrentPage(0);

            add(new PagingNavigator("navigator", dataTable) {
                @Override public boolean isVisible() {
                    return PageState.LIST.equals(pageState);
                }
            });
        }

    }

    private IModel<String> getTypeString(BaseOrg org) {
        IModel<String> model = labelMap.get(org.getClass());
        return model==null ? Model.of("-") : model;
    }

}
