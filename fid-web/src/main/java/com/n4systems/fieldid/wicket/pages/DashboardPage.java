package com.n4systems.fieldid.wicket.pages;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.ui.sortable.SortableAjaxBehavior;

import com.n4systems.fieldid.wicket.behavior.SimpleSortableAjaxBehavior;
import com.n4systems.fieldid.wicket.components.dashboard.AddWidgetPanel;
import com.n4systems.fieldid.wicket.components.dashboard.widgets.WidgetFactory;
import com.n4systems.fieldid.wicket.pages.widgets.Widget;
import com.n4systems.fieldid.wicket.util.JavascriptPackageResourceIE;
import com.n4systems.model.dashboard.DashboardColumn;
import com.n4systems.model.dashboard.DashboardLayout;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.WidgetType;
import com.n4systems.services.dashboard.DashboardService;

@SuppressWarnings("serial")
public class DashboardPage extends FieldIDFrontEndPage {

    @SpringBean
    private DashboardService dashboardService;
    
    @SpringBean
    private WidgetFactory widgetFactory;

    private WidgetDefinition movingWidget;

    private AddWidgetPanel addWidgetPanel;

    private WebMarkupContainer sortableColumn;
    private WebMarkupContainer sortableColumn2;

    private DashboardLayout currentLayout;

	public DashboardPage() {
        add(CSSPackageResource.getHeaderContribution("style/dashboard/dashboard.css"));
       	add(JavascriptPackageResourceIE.getHeaderContribution("javascript/flot/excanvas.js"));
        add(JavascriptPackageResource.getHeaderContribution("javascript/flot/jquery.flot.min.js"));                
        add(JavascriptPackageResource.getHeaderContribution("javascript/flot/jquery.flot.navigate.min.js"));        
        add(JavascriptPackageResource.getHeaderContribution("javascript/dashboard.js"));
        
        currentLayout = dashboardService.findLayout();

        add(addWidgetPanel = new AddWidgetPanel("addWidgetPanel", new PropertyModel<DashboardLayout>(this, "currentLayout")) {
            @Override
            protected void onWidgetTypeSelected(AjaxRequestTarget target, WidgetType type) {
                WidgetDefinition definition = new WidgetDefinition();
                definition.setWidgetType(type);
                definition.setName(type.getDisplayName());
                currentLayout.getColumns().get(0).getWidgets().add(definition);
                saveAndRepaintDashboard(target);
            }
        });

        add(sortableColumn = createColumnContainer("sortableColumn", new PropertyModel<List<WidgetDefinition>>(currentLayout, "columns[0].widgets"), 0));
        add(sortableColumn2 = createColumnContainer("sortableColumn2", new PropertyModel<List<WidgetDefinition>>(currentLayout, "columns[1].widgets"), 1));
    }

	private WebMarkupContainer createColumnContainer(String containerId, IModel<List<WidgetDefinition>> widgetsModel,  final int columnIndex) {
        WebMarkupContainer container = new WebMarkupContainer(containerId);

        container.add(new ListView<WidgetDefinition>("widgets", widgetsModel) {
            @Override
            protected void populateItem(final ListItem<WidgetDefinition> item) {
                item.setOutputMarkupId(true);                
                WidgetDefinition widgetDefinition = item.getModelObject();                
                final Widget widget = widgetFactory.createWidget(widgetDefinition).withRemoveBehaviour(new AjaxEventBehavior("onclick") {				
					@Override protected void onEvent(AjaxRequestTarget target) {
						removeWidgetFromColumn(columnIndex, item.getIndex());
						saveAndRepaintDashboard(target);
					}
				});                
				item.add(widget);			
            }

        });
       
        container.add(makeSortableBehavior(container, columnIndex));
        container.setOutputMarkupId(true);
        return container;
    }
 
    private SortableAjaxBehavior makeSortableBehavior(final Component container, final int columnIndex) {
        SimpleSortableAjaxBehavior simpleSortableAjaxBehavior = new SimpleSortableAjaxBehavior() {
            @Override
            public void onReceive(Component sortedComponent, int index, Component parentSortedComponent, AjaxRequestTarget target) {
                if (sortedComponent != null) {
                    addWidgetToColumn(movingWidget, columnIndex, index);
                    saveAndRepaintDashboard(target);
                }
            }

            @Override
            public void onRemove(Component sortedComponent, AjaxRequestTarget target) {
                if (sortedComponent != null) {
                    movingWidget = (WidgetDefinition) sortedComponent.getDefaultModelObject();
                    removeWidgetFromColumn(movingWidget, columnIndex);
                }
            }
        };
        simpleSortableAjaxBehavior.setConnectWith(".column");
        return simpleSortableAjaxBehavior;
    }

    private void saveAndRepaintDashboard(AjaxRequestTarget target) {
        currentLayout.setTenant(getTenant());
        dashboardService.saveLayout(currentLayout);
        target.addComponent(sortableColumn);
        target.addComponent(sortableColumn2);
        target.addComponent(addWidgetPanel);
    }

    private void removeWidgetFromColumn(WidgetDefinition widgetToRemove, int columnIndex) {
        DashboardColumn column = currentLayout.getColumns().get(columnIndex);
        List<WidgetDefinition> widgets = column.getWidgets();
        for (WidgetDefinition widgetDefinition : widgets) {
            if (widgetDefinition.getWidgetType() == widgetToRemove.getWidgetType()) {
                widgets.remove(widgetDefinition);
                break;
            }
        }
    }

    private void removeWidgetFromColumn(int columnIndex, int widgetIndex) {
        currentLayout.getColumns().get(columnIndex).getWidgets().remove(widgetIndex);
    }

    private void addWidgetToColumn(WidgetDefinition widgetToAdd, int columnIndex, int widgetIndex) {
        currentLayout.getColumns().get(columnIndex).getWidgets().add(widgetIndex, widgetToAdd);
    }

    @Override
    protected boolean useLegacyCss() {
        return false;
    }
     
}
