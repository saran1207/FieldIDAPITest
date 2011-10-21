package com.n4systems.fieldid.wicket.pages;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.ui.sortable.SortableAjaxBehavior;

import com.n4systems.fieldid.wicket.behavior.SimpleSortableAjaxBehavior;
import com.n4systems.fieldid.wicket.components.dashboard.AddWidgetPanel;
import com.n4systems.fieldid.wicket.components.dashboard.widgets.CommonLinksPanel;
import com.n4systems.fieldid.wicket.components.dashboard.widgets.JobsAssignedPanel;
import com.n4systems.fieldid.wicket.components.dashboard.widgets.NewsPanel;
import com.n4systems.fieldid.wicket.components.dashboard.widgets.SamplePanel;
import com.n4systems.fieldid.wicket.pages.widgets.Widget;
import com.n4systems.model.dashboard.DashboardLayout;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.WidgetType;
import com.n4systems.services.dashboard.DashboardService;

public class DashboardPage extends FieldIDFrontEndPage {

    @SpringBean
    private DashboardService dashboardService;

    private WebMarkupContainer sortableColumn;
    private WebMarkupContainer sortableColumn2;
    private DashboardLayout currentLayout;

    public DashboardPage() {
        add(CSSPackageResource.getHeaderContribution("style/dashboard/dashboard.css"));

        currentLayout = dashboardService.findLayout();

        add(new AddWidgetPanel("addWidgetPanel", new PropertyModel<DashboardLayout>(this, "currentLayout")) {
            @Override
            protected void onWidgetTypeSelected(AjaxRequestTarget target, WidgetType type) {
                WidgetDefinition definition = new WidgetDefinition();
                definition.setWidgetType(type);
                definition.setName(type.getDisplayName());
                currentLayout.getColumns().get(0).getWidgets().add(definition);
                target.addComponent(sortableColumn);
            }
        });

        add(sortableColumn = createColumnContainer("sortableColumn", new PropertyModel<List<WidgetDefinition>>(currentLayout, "columns[0].widgets")));
        add(sortableColumn2 = createColumnContainer("sortableColumn2", new PropertyModel<List<WidgetDefinition>>(currentLayout, "columns[1].widgets")));
    }

    private WebMarkupContainer createColumnContainer(String containerId, IModel<List<WidgetDefinition>> widgetsModel) {
        WebMarkupContainer container = new WebMarkupContainer(containerId);

        container.add(new ListView<WidgetDefinition>("widgets", widgetsModel) {
            @Override
            protected void populateItem(ListItem<WidgetDefinition> item) {
                item.setOutputMarkupId(true);

                WidgetDefinition widgetDefinition = item.getModelObject();
                Widget widget = new Widget("widget", new PropertyModel<String>(item.getModel(), "name"));

                // TODO: Extract this to someplace
                if (widgetDefinition.getWidgetType() == WidgetType.JOBS_ASSIGNED) {
                    widget.addContent(new JobsAssignedPanel(widget.getContentId()));
                } else if (widgetDefinition.getWidgetType() == WidgetType.SAMPLE) {
                    widget.addContent(new SamplePanel(widget.getContentId()));
                } else if (widgetDefinition.getWidgetType() == WidgetType.COMMON_LINKS) {
                	widget.addContent(new CommonLinksPanel(widget.getContentId()));
                }else if (widgetDefinition.getWidgetType() == WidgetType.NEWS) {
                	widget.addContent(new NewsPanel(widget.getContentId()));
                }


                item.add(widget);
            }
        });

        container.add(makeSortableBehavior(container));
        container.setOutputMarkupId(true);
        return container;
    }

    private SortableAjaxBehavior makeSortableBehavior(final Component container) {
        SimpleSortableAjaxBehavior simpleSortableAjaxBehavior = new SimpleSortableAjaxBehavior() {
            @Override
            public void onUpdate(Component sortedComponent, int index, AjaxRequestTarget target) {
                if (sortedComponent != null) {
                    System.out.println("Update for component: " + sortedComponent.getMarkupId() + " of container: " + container.getMarkupId());
                } else {
                    System.out.println("Null update");
                }
            }

            @Override
            public void onReceive(Component sortedComponent, int index, Component parentSortedComponent, AjaxRequestTarget ajaxRequestTarget) {
                if (sortedComponent != null) {
                    System.out.println("Receive for component: " + sortedComponent.getMarkupId() + " of container: " + container.getMarkupId());
                } else {
                    System.out.println("Null receive");
                }
            }

            @Override
            public void onRemove(Component sortedComponent, AjaxRequestTarget ajaxRequestTarget) {
                if (sortedComponent != null) {
                    System.out.println("Remove for component: " + sortedComponent.getMarkupId() + " of container: " + container.getMarkupId());
                } else {
                    System.out.println("Null remove!");
                }
            }
        };
        simpleSortableAjaxBehavior.setConnectWith(".column");
        return simpleSortableAjaxBehavior;
    }

    @Override
    protected boolean useLegacyCss() {
        return false;
    }
}
