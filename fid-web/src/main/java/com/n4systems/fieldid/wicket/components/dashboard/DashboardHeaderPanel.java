package com.n4systems.fieldid.wicket.components.dashboard;

import com.n4systems.fieldid.wicket.pages.DashboardPage;
import com.n4systems.model.dashboard.DashboardLayout;
import com.n4systems.services.dashboard.DashboardService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class DashboardHeaderPanel extends Panel {

    @SpringBean
    private DashboardService dashboardService;

    public DashboardHeaderPanel(String id) {
        super(id);

        final DashboardLayout selectedLayout = dashboardService.findLayout();

        add(new Label("name", new PropertyModel<DashboardLayout>(selectedLayout, "name")));

        ListView<DashboardLayout> layoutListView = new ListView<DashboardLayout>("layoutList", createDashboardLayoutsModel()) {
            @Override
            protected void populateItem(ListItem<DashboardLayout> item) {
                final DashboardLayout layout = item.getModelObject();
                Link<Void> selectLink = new Link<Void>("link") {
                    @Override
                    public void onClick() {
                        selectedLayout.setSelected(false);
                        layout.setSelected(true);
                        dashboardService.saveLayout(selectedLayout);
                        dashboardService.saveLayout(layout);
                        setResponsePage(DashboardPage.class);
                    }
                };
                item.add(selectLink);
                selectLink.add(new Label("name", new PropertyModel<DashboardLayout>(layout, "name")));
            }
        };

        layoutListView.setOutputMarkupPlaceholderTag(true);
        add(layoutListView);


        add(new AjaxLink<Void>("manageDashboardLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                onManageDashboard(target);
            }
        });

    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderJavaScriptReference("javascript/dashboard_layout_list.js");
    }

    protected void onManageDashboard(AjaxRequestTarget target) { }

    private LoadableDetachableModel<List<DashboardLayout>> createDashboardLayoutsModel() {
        return new LoadableDetachableModel<List<DashboardLayout>>() {
            @Override
            protected List<DashboardLayout> load() {
                return dashboardService.findDashboardLayouts(true);
            }
        };
    }
}
