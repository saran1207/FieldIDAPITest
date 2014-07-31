package com.n4systems.fieldid.wicket.components.dashboard;

import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.dashboard.DashboardColumn;
import com.n4systems.model.dashboard.DashboardLayout;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.WidgetType;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.List;

public class AddWidgetPanel extends Panel {

    private List<WidgetType> selectedTypes;

    @SpringBean EventService eventService;

    public AddWidgetPanel(String id, final IModel<DashboardLayout> currentLayoutModel) {
        super(id);

        DashboardLayout layout = currentLayoutModel.getObject();
        selectedTypes = getSelectedWidgets(layout);

        final WebMarkupContainer widgetsContainer = new WebMarkupContainer("widgets");

        List<ITab>  tabs = new ArrayList<ITab>();
        tabs.add(new AbstractTab(new FIDLabelModel("label.general")) {
            @Override
            public WebMarkupContainer getPanel(String panelId) {
                return new GeneralWidgetsTab(panelId, currentLayoutModel){
                    @Override
                    protected void onWidgetTypeSelected(AjaxRequestTarget target, WidgetType type) {
                        AddWidgetPanel.this.onWidgetTypeSelected(target, type);
                    }
                    @Override
                    protected void onCloseWindow(AjaxRequestTarget target) {
                        AddWidgetPanel.this.onCloseWindow(target);
                    }
                };
            }
            @Override
            public boolean isVisible(){
                return true;
            }
        });


        tabs.add(new AbstractTab(new FIDLabelModel("label.inspection_and_audits")) {
            @Override
            public WebMarkupContainer getPanel(String panelId) {
                //return new GeneralWidgetsTab(panelId, currentLayoutModel);
                return new InspectionWidgetsTab(panelId, currentLayoutModel){
                    @Override
                    protected void onWidgetTypeSelected(AjaxRequestTarget target, WidgetType type) {
                        AddWidgetPanel.this.onWidgetTypeSelected(target, type);
                    }
                    @Override
                    protected void onCloseWindow(AjaxRequestTarget target) {
                        AddWidgetPanel.this.onCloseWindow(target);
                    }
                };
            }
            @Override
            public boolean isVisible(){
                return true;
            }
        });

        tabs.add(new AbstractTab(new FIDLabelModel("speed.lockout_tagout")) {
            @Override
            public WebMarkupContainer getPanel(String panelId) {
                //return new GeneralWidgetsTab(panelId, currentLayoutModel);
                return new LockoutWidgetsTab(panelId, currentLayoutModel){
                    @Override
                    protected void onWidgetTypeSelected(AjaxRequestTarget target, WidgetType type) {
                        AddWidgetPanel.this.onWidgetTypeSelected(target, type);
                    }
                    @Override
                    protected void onCloseWindow(AjaxRequestTarget target) {
                        AddWidgetPanel.this.onCloseWindow(target);
                    }
                };
            }
            @Override
            public boolean isVisible(){
                return true;
            }
        });
        widgetsContainer.add(new AjaxTabbedPanel("tabs", tabs));
        widgetsContainer.setOutputMarkupId(true);
        add(widgetsContainer);

        add(new AjaxLink<Void>("closeLink") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                onCloseWindow(target);
            }
        });

    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderCSSReference("style/legacy/dashboard/add_widget.css");
    }

    private List<WidgetType> getSelectedWidgets(DashboardLayout layout) {
        List<WidgetType> selectedTypes = new ArrayList<WidgetType>();

        for (DashboardColumn column: layout.getColumns()) {
            for (WidgetDefinition widgetDef : column.getWidgets()) {
                selectedTypes.add(widgetDef.getWidgetType());
            }
        }
        return selectedTypes;
    }

    protected void onWidgetTypeSelected(AjaxRequestTarget target, WidgetType type) { }

    protected void onCloseWindow(AjaxRequestTarget target) {}
}
