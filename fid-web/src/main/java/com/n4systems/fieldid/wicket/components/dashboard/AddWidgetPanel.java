package com.n4systems.fieldid.wicket.components.dashboard;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.dashboard.DashboardColumn;
import com.n4systems.model.dashboard.DashboardLayout;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.WidgetType;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddWidgetPanel extends Panel {

    private List<WidgetType> selectedTypes;

    public AddWidgetPanel(String id, final IModel<DashboardLayout> currentLayoutModel) {
        super(id);

        DashboardLayout layout = currentLayoutModel.getObject();
        selectedTypes = getSelectedWidgets(layout);

        final WebMarkupContainer widgetsContainer = new WebMarkupContainer("widgets");

        ListView<WidgetType> widgetList = new ListView<WidgetType>("widgetList", createWidgetListModel()) {
            @Override
            protected void populateItem(ListItem<WidgetType> item) {
                final WidgetType type = item.getModelObject();

                item.add(new Label("name", new PropertyModel<WidgetType>(type, "name")));
                item.add(new Label("description", new PropertyModel<WidgetType>(type, "description")));

                final AjaxLink addLink = new AjaxLink<Void>("add") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        onWidgetTypeSelected(target, type);
                        selectedTypes.add(type);
                        target.add(widgetsContainer);
                    }
                };
                addLink.setOutputMarkupId(true);
                item.add(addLink);

                if(selectedTypes.contains(type)) {
                    addLink.setEnabled(false);
                    addLink.add(new Label("addLabel", new FIDLabelModel("label.added")));
                } else {
                    addLink.add(new Label("addLabel", new FIDLabelModel("label.add")));
                }
            }
        };
        widgetsContainer.setOutputMarkupId(true);

        widgetsContainer.add(widgetList);
        add(widgetsContainer);

    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderCSSReference("style/dashboard/add_widget.css");
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

    private LoadableDetachableModel<List<WidgetType>> createWidgetListModel() {
        return new LoadableDetachableModel<List<WidgetType>>() {
            @Override
            protected List<WidgetType> load() {
                return Arrays.asList(WidgetType.values());
            }
        };
    }

    protected void onWidgetTypeSelected(AjaxRequestTarget target, WidgetType type) { }
}
