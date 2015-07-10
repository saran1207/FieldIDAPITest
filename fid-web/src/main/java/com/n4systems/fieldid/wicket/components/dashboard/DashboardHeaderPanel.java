package com.n4systems.fieldid.wicket.components.dashboard;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.dashboard.CurrentLayoutModel;
import com.n4systems.fieldid.wicket.pages.DashboardPage;
import com.n4systems.model.dashboard.DashboardLayout;
import com.n4systems.services.dashboard.DashboardService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class DashboardHeaderPanel extends Panel {

    @SpringBean
    private DashboardService dashboardService;

    ContextImage menuImage;
    ContextImage hideMenuImage;

    public DashboardHeaderPanel(String id) {
        super(id);

        add(new ContextImage("printIcon", "images/print-icon.png"));

        menuImage = new ContextImage("menuButton", "images/menu_button_16.png");
        add(menuImage);


        hideMenuImage = new ContextImage("hideMenuImage", "images/collapse-menu.png");
        add(hideMenuImage);

        add(new Label("name", new PropertyModel<DashboardLayout>(new CurrentLayoutModel(), "name")));



        ListView<DashboardLayout> layoutListView = new ListView<DashboardLayout>("layoutList", createDashboardLayoutsModel()) {
            @Override
            protected void populateItem(ListItem<DashboardLayout> item) {
                final DashboardLayout layout = item.getModelObject();
                if (!layout.getName().equals("Manage Dashboards")) {
                    Link<Void> selectLink = new Link<Void>("link") {
                        @Override
                        public void onClick() {
                            DashboardLayout selectedLayout = dashboardService.findLayout();
                            selectedLayout.setSelected(false);
                            layout.setSelected(true);
                            dashboardService.saveLayout(selectedLayout);
                            dashboardService.saveLayout(layout);
                            setResponsePage(DashboardPage.class);
                        }
                    };
                    item.add(selectLink);
                    selectLink.add(new Label("name", new PropertyModel<DashboardLayout>(layout, "name")));
                } else {
                    AjaxLink selectLink = new AjaxLink<Void>("link") {
                        @Override
                        public void onClick(AjaxRequestTarget target) {
                            onManageDashboard(target);
                        }
                    };
                    item.add(selectLink);
                    selectLink.add(new Label("name", new PropertyModel<DashboardLayout>(layout, "name")));
                    item.add(new AttributeAppender("class", new Model<String>(" manageDashboard"), ""));
                }
            }
        };

        layoutListView.setOutputMarkupPlaceholderTag(true);
        add(layoutListView);


        HiddenField<Long> layoutCount;
        add(layoutCount = new HiddenField<Long>("layoutCount", createDashboardLayoutCountModel()));
        layoutCount.setMarkupId("layoutCount");

        AjaxLink addWidgetsLink;
        add(addWidgetsLink = new AjaxLink<Void>("addWidgetsLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                onAddWidgets(target);
            }
        });
        addWidgetsLink.setMarkupId("addWidgetsLink");
        addWidgetsLink.add(new AttributeAppender("title", new FIDLabelModel("label.tooltip.add_new_widget")));


        /*
        AjaxLink manageDashboardLink;
        add(manageDashboardLink = new AjaxLink<Void>("manageDashboardLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                onManageDashboard(target);
            }
        });
        manageDashboardLink.add(new AttributeAppender("title", new FIDLabelModel("label.tooltip.manage_dashboard")));
        //manageDashboardLink.add(new ContextImage("menuButton", "images/menu_button_16.png"));
        */

    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderJavaScriptReference("javascript/dashboard_layout_list.js");
        response.renderCSSReference("style/legacy/dashboard/header.css");
        response.renderCSSReference("style/legacy/tipsy/tipsy.css");
        response.renderJavaScriptReference("javascript/tipsy/jquery.tipsy.js");
        // CAVEAT : https://github.com/jaz303/tipsy/issues/19
        // after ajax call, tipsy tooltips will remain around so need to remove them explicitly.
        response.renderOnDomReadyJavaScript("$('.tipsy').remove(); $('.tipsy-tooltip').tipsy({gravity: 'nw', fade:true, delayIn:150})");
    }

    protected void onManageDashboard(AjaxRequestTarget target) { }
    protected void onAddWidgets(AjaxRequestTarget target) { }

    private LoadableDetachableModel<List<DashboardLayout>> createDashboardLayoutsModel() {
        return new LoadableDetachableModel<List<DashboardLayout>>() {
            @Override
            protected List<DashboardLayout> load() {
                List<DashboardLayout> list = dashboardService.findDashboardLayouts(true);

                //Add the manage dashboards link to the list of layouts.
                DashboardLayout manageLink = new DashboardLayout();
                manageLink.setName("Manage Dashboards");
                list.add(manageLink);

                return list;
            }
        };
    }

    private LoadableDetachableModel<Long> createDashboardLayoutCountModel() {
        return new LoadableDetachableModel<Long>() {
            @Override
            protected Long load() {

                return dashboardService.countDashboardLayouts();

            }
        };
    }
}
