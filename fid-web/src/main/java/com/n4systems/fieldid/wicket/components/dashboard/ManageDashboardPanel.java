package com.n4systems.fieldid.wicket.components.dashboard;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.dashboard.DashboardColumn;
import com.n4systems.model.dashboard.DashboardLayout;
import com.n4systems.model.dashboard.WidgetType;
import com.n4systems.services.dashboard.DashboardService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class ManageDashboardPanel extends Panel {

    @SpringBean
    private DashboardService dashboardService;

    private String name;

    public ManageDashboardPanel(String id) {
        super(id);

        ListView<DashboardLayout> layoutListView = new ListView<DashboardLayout>("layoutListView", createDashboardLayoutsModel()) {
            @Override
            protected void populateItem(ListItem<DashboardLayout> item) {
                final DashboardLayout layout = item.getModelObject();
                final Label name;
                item.add(name = new Label("name", new PropertyModel<DashboardLayout>(layout, "name")));
                name.setOutputMarkupId(true);

                item.add(new Label("widgetCount", new FIDLabelModel("label.widget_count", layout.getWidgetCount(), WidgetType.values().length)));



                final FIDFeedbackPanel feedbackPanel;

                final Form<Void> editForm = new Form("editForm");
                final AjaxLink editLink;
                final AjaxLink deleteLink;

                item.add(deleteLink = new AjaxLink("delete") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        if(layout.isSelected()) {
                            DashboardLayout defaultLayout = dashboardService.findDashboardLayouts(true).get(0);
                            defaultLayout.setSelected(true);
                            dashboardService.saveLayout(defaultLayout);
                        }
                        dashboardService.delete(dashboardService.getDashboardLayout(layout.getId()));
                        target.add(ManageDashboardPanel.this);
                    }
                });

                item.add(editLink = new AjaxLink("edit") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        editForm.setVisible(true);
                        name.setVisible(false);
                        setVisible(false);
                        deleteLink.setVisible(false);
                        target.add(name);
                        target.add(editForm);
                        target.add(this);
                        target.add(deleteLink);
                    }
                });

                if(item.getIndex() == 0) {
                    deleteLink.setVisible(false);
                    editLink.add(new AttributeAppender("class", "rename_only").setSeparator(" "));
                }

                editForm.add(feedbackPanel = new FIDFeedbackPanel("feedbackPanel"));

                RequiredTextField nameField;
                editForm.add(nameField = new RequiredTextField("nameField", new PropertyModel(layout, "name")));
                editForm.add(new AjaxSubmitLink("saveButton") {
                    @Override
                    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                        dashboardService.saveLayout(layout);
                        editForm.setVisible(false);
                        name.setVisible(true);
                        editLink.setVisible(true);
                        deleteLink.setVisible(true);
                        feedbackPanel.setVisible(false);
                        target.add(ManageDashboardPanel.this);
                        onAddNewDashboard(target);
                    }

                    @Override
                    protected void onError(AjaxRequestTarget target, Form<?> form) {
                        feedbackPanel.setVisible(true);
                        target.add(feedbackPanel);
                    }
                });

                editForm.setVisible(false);
                editForm.setOutputMarkupPlaceholderTag(true);
                item.add(editForm);
            }
        };
        add(layoutListView);

        Form<Void> addLayoutForm = new Form<Void>("addForm");
        final FIDFeedbackPanel feedbackPanel;
        addLayoutForm.add(feedbackPanel = new FIDFeedbackPanel("feedbackPanel"));

        final RequiredTextField nameField;
        addLayoutForm.add(nameField = new RequiredTextField("nameField", new PropertyModel(this, "name")));

        addLayoutForm.add(new AjaxSubmitLink("add") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                dashboardService.saveLayout(createNewDasboardLayout(name));
                name = null;
                target.add(ManageDashboardPanel.this);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                feedbackPanel.setVisible(true);
                target.add(feedbackPanel);

            }
        });

        add(addLayoutForm);

    }

    private DashboardLayout createNewDasboardLayout(String name) {
        DashboardLayout layout = new DashboardLayout();
        DashboardColumn firstColumn = new DashboardColumn();
        DashboardColumn secondColumn = new DashboardColumn();
        layout.getColumns().add(firstColumn);
        layout.getColumns().add(secondColumn);
        layout.setTenant(FieldIDSession.get().getTenant());
        layout.setSelected(false);
        layout.setName(name);
        return layout;
    }

    private LoadableDetachableModel<List<DashboardLayout>> createDashboardLayoutsModel() {
        return new LoadableDetachableModel<List<DashboardLayout>>() {
            @Override
            protected List<DashboardLayout> load() {
                return dashboardService.findDashboardLayouts(false);
            }
        };
    }

    public void onAddNewDashboard(AjaxRequestTarget target) {}
}
