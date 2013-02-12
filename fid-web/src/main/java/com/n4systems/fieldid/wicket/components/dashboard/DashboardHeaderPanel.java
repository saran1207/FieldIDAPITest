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

        add(new Label("name", new PropertyModel<DashboardLayout>(new CurrentLayoutModel(), "name")));

        ListView<DashboardLayout> layoutListView = new ListView<DashboardLayout>("layoutList", createDashboardLayoutsModel()) {
            @Override
            protected void populateItem(ListItem<DashboardLayout> item) {
                final DashboardLayout layout = item.getModelObject();
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

        AjaxLink manageDashboardLink;
        add(manageDashboardLink = new AjaxLink<Void>("manageDashboardLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                onManageDashboard(target);
            }
        });
        manageDashboardLink.add(new AttributeAppender("title", new FIDLabelModel("label.tooltip.manage_dashboard")));

    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderJavaScriptReference("javascript/dashboard_layout_list.js");
        response.renderCSSReference("style/dashboard/header.css");
        response.renderCSSReference("style/tipsy/tipsy.css");
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
                return dashboardService.findDashboardLayouts(true);
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
