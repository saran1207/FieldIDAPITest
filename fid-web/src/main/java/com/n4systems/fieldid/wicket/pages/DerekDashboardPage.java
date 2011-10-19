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
import com.n4systems.fieldid.wicket.components.dashboard.widgets.AssetsIdentifiedPanel;
import com.n4systems.fieldid.wicket.pages.widgets.Widget;
import com.n4systems.model.dashboard.DashboardColumn;
import com.n4systems.model.dashboard.DashboardLayout;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.WidgetType;
import com.n4systems.services.dashboard.DashboardService;

// FIXME DD : temporary page used to develop widget and avoid conflict with other dashboard development. 
//  this *must* be deleted by end of iteration and merged.
public class DerekDashboardPage extends FieldIDFrontEndPage {

    @SpringBean
    private DashboardService dashboardService;

    private WebMarkupContainer sortableColumn;
    private WebMarkupContainer sortableColumn2;
    private DashboardLayout currentLayout;

    public DerekDashboardPage() {
        add(CSSPackageResource.getHeaderContribution("style/dashboard/dashboard.css"));        
        
        currentLayout = makeLayoutForWidget();
        
        add(sortableColumn = createColumnContainer("sortableColumn", new PropertyModel<List<WidgetDefinition>>(currentLayout, "columns[0].widgets")));
        add(sortableColumn2 = createColumnContainer("sortableColumn2", new PropertyModel<List<WidgetDefinition>>(currentLayout, "columns[1].widgets")));
    }

	private DashboardLayout makeLayoutForWidget() {
		DashboardLayout layout = new DashboardLayout();

        WidgetDefinition definition = new WidgetDefinition();
        definition.setWidgetType(WidgetType.ASSETS_IDENTIFIED);
        definition.setName(WidgetType.ASSETS_IDENTIFIED.getDisplayName());

        DashboardColumn dashboardColumn = new DashboardColumn();
        dashboardColumn.getWidgets().add(definition);

        layout.getColumns().add(dashboardColumn);
        layout.getColumns().add(new DashboardColumn());
        
        return layout;
	}

    @SuppressWarnings("serial")
	private WebMarkupContainer createColumnContainer(String containerId, IModel<List<WidgetDefinition>> widgetsModel) {
        WebMarkupContainer container = new WebMarkupContainer(containerId);

        container.add(new ListView<WidgetDefinition>("widgets", widgetsModel) {
            @Override
            protected void populateItem(ListItem<WidgetDefinition> item) {
                item.setOutputMarkupId(true);
                WidgetDefinition widgetDefinition = item.getModelObject();
                // FIXME DD : add widget factory here.
                Widget widget = new Widget("widget", new PropertyModel<String>(item.getModel(), "name"));
                widget.addContent(new AssetsIdentifiedPanel(widget.getContentId()));
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
