package com.n4systems.fieldid.wicket.pages.setup.org;


import com.google.common.collect.Maps;
import com.n4systems.fieldid.wicket.components.navigation.MattBar;
import com.n4systems.fieldid.wicket.data.OrgsDataProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.orgs.*;
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

    private static final Map<Class<? extends BaseOrg>,IModel<String>> labelMap = Maps.newHashMap();

    static {
        labelMap.put(PrimaryOrg.class, new FIDLabelModel("label.primary_type"));
        labelMap.put(SecondaryOrg.class, new FIDLabelModel("label.secondary_type"));
        labelMap.put(DivisionOrg.class, new FIDLabelModel("label.division_type"));
        labelMap.put(CustomerOrg.class, new FIDLabelModel("label.customer_type"));
    }

    public static final int ITEMS_PER_PAGE = 20;

    private String search = null;


    public OrgViewPage() {
        add(new OrgViewPanel("orgView"));
    }


    class OrgViewPanel extends Fragment {
        private DataView<BaseOrg> dataTable;
        private IDataProvider provider = new OrgsDataProvider();

        public OrgViewPanel(String id) {
            super(id, "orgViewFragment", OrgViewPage.this);
            add(new AttributeAppender("class","org-view"));
            add(new WebMarkupContainer("tree"));
            add(new MattBar("buttons")
                    .addLink(new FIDLabelModel("label.tree"), 1)
                    .addLink(new FIDLabelModel("label.list"), 2)
            );
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
            });
            dataTable.setCurrentPage(0);

            add(new PagingNavigator("navigator", dataTable));
        }
    }

    private IModel<String> getTypeString(BaseOrg org) {
        IModel<String> model = labelMap.get(org.getClass());
        return model==null ? Model.of("-") : model;
    }

}
