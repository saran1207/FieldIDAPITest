package com.n4systems.fieldid.wicket.components.dashboard;


import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.dashboard.DashboardColumn;
import com.n4systems.model.dashboard.DashboardLayout;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.WidgetType;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by rrana on 2014-07-31.
 */
public class GeneralWidgetsTab extends Panel {

    private List<WidgetType> selectedTypes;

    @SpringBean
    EventService eventService;

    public GeneralWidgetsTab(String id, IModel<DashboardLayout> currentLayoutModel) {
        super(id);

        final WebMarkupContainer widgetsContainer = new WebMarkupContainer("widgets");
        final boolean hasEvents = eventService.hasEvents();

        DashboardLayout layout = currentLayoutModel.getObject();
        selectedTypes = getSelectedWidgets(layout);

        final ListView<WidgetType> widgetList = new ListView<WidgetType>("widgetList", createWidgetListModel()) {


            @Override
            protected void populateItem(ListItem<WidgetType> item) {

                final WidgetType type = item.getModelObject();
                // if event type and has no events - do not show widget
                if (type.isEventRelated() && !hasEvents) {
                    item.setVisible(false);
                } else {
                    // show widget.
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


            }
        };

        widgetsContainer.setOutputMarkupId(true);

        widgetsContainer.add(widgetList);

        add(widgetsContainer);

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
                List<WidgetType> widgetTypeList = new ArrayList(Arrays.asList(WidgetType.values()));
                List<WidgetType> generalList = new ArrayList<WidgetType>();
                for(WidgetType widgetType:widgetTypeList) {
                    if(widgetType.isGeneral()){
                        generalList.add(widgetType);
                    }
                }
                return generalList;
            }
        };
    }

    protected void onWidgetTypeSelected(AjaxRequestTarget target, WidgetType type) { }

    protected void onCloseWindow(AjaxRequestTarget target) {}

}
