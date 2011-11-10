package com.n4systems.fieldid.wicket.pages;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.StringHeaderContributor;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.ui.sortable.SortableAjaxBehavior;

import com.n4systems.fieldid.wicket.behavior.SimpleSortableAjaxBehavior;
import com.n4systems.fieldid.wicket.components.dashboard.AddWidgetPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.dashboard.CurrentLayoutModel;
import com.n4systems.fieldid.wicket.pages.widgets.Widget;
import com.n4systems.fieldid.wicket.pages.widgets.WidgetFactory;
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


    private AddWidgetPanel addWidgetPanel;

    private DashboardColumnContainer sortableColumn;
    private DashboardColumnContainer sortableColumn2;

    IModel<DashboardLayout> currentLayoutModel;

	public DashboardPage() {
        add(CSSPackageResource.getHeaderContribution("style/dashboard/dashboard.css"));
        add(CSSPackageResource.getHeaderContribution("style/dashboard/widgetconfig.css"));
       	add(JavascriptPackageResourceIE.getHeaderContribution("javascript/flot/excanvas.min.js"));
        add(JavascriptPackageResource.getHeaderContribution("javascript/flot/jquery.flot.min.js"));                
        add(JavascriptPackageResource.getHeaderContribution("javascript/flot/jquery.flot.navigate.min.js"));        
        add(JavascriptPackageResource.getHeaderContribution("javascript/flot/jquery.flot.symbol.min.js"));        
        add(JavascriptPackageResource.getHeaderContribution("javascript/dashboard.js"));
        add(JavascriptPackageResource.getHeaderContribution("javascript/widget.js"));
        // override org picker with inline style...ugly but it works.
        add(new StringHeaderContributor("<style type='text/css'>.orgSelector { width : 300px !important; }</style>")); 	
        
        currentLayoutModel = new CurrentLayoutModel();

        add(addWidgetPanel = new AddWidgetPanel("addWidgetPanel", currentLayoutModel) {
            @Override
            protected void onWidgetTypeSelected(AjaxRequestTarget target, WidgetType type) {
                WidgetDefinition definition = new WidgetDefinition(type);
                currentLayoutModel.getObject().getColumns().get(0).getWidgets().add(0, definition);
                saveAndRepaintDashboard(target);
            }
        });

        add(sortableColumn = createColumnContainer("sortableColumn", new PropertyModel<List<WidgetDefinition>>(currentLayoutModel, "columns[0].widgets"), 0));
        add(sortableColumn2 = createColumnContainer("sortableColumn2", new PropertyModel<List<WidgetDefinition>>(currentLayoutModel, "columns[1].widgets"), 1));
    }

	private DashboardColumnContainer createColumnContainer(String containerId, IModel<List<WidgetDefinition>> widgetsModel,  final int columnIndex) {
        DashboardColumnContainer container = new DashboardColumnContainer(containerId);

        container.add(new ListView<WidgetDefinition>("widgets", widgetsModel) {
            @Override
            protected void populateItem(final ListItem<WidgetDefinition> item) {
                item.setOutputMarkupId(true);                
                WidgetDefinition widgetDefinition = item.getModelObject();                
                final Widget widget = widgetFactory.createWidget(widgetDefinition);
                widget.setRemoveBehaviour(new AjaxEventBehavior("onclick") {
                    @Override
                    protected void onEvent(AjaxRequestTarget target) {
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

    // We get two calls (and a detach in between) when a widget is moved, so these state variables track necessary info
    // These need to live outside the sortable behavior because there are two instances (one for each column).
    private WidgetDefinition movingWidget;
    private int sourceColumn;

    private SortableAjaxBehavior makeSortableBehavior(final Component container, final int columnIndex) {
        SimpleSortableAjaxBehavior simpleSortableAjaxBehavior = new SimpleSortableAjaxBehavior() {
            @Override
            public void onReceive(Component sortedComponent, int index, Component parentSortedComponent, AjaxRequestTarget target) {
                if (sortedComponent != null) {
                    removeWidgetFromColumn(movingWidget, sourceColumn);
                    addWidgetToColumn(movingWidget, columnIndex, index);
                    saveAndRepaintDashboard(target);
                }
            }

            @Override
            public void onRemove(Component sortedComponent, AjaxRequestTarget target) {
                if (sortedComponent != null) {
                    sourceColumn = columnIndex;
                    movingWidget = (WidgetDefinition) sortedComponent.getDefaultModelObject();
                }
            }
        };
        simpleSortableAjaxBehavior.setCursor("pointer");
        simpleSortableAjaxBehavior.setHandle(".widget-draggable");
        simpleSortableAjaxBehavior.setConnectWith(".column");
        return simpleSortableAjaxBehavior;
    }

    private void saveAndRepaintDashboard(AjaxRequestTarget target) {
        currentLayoutModel.getObject().setTenant(getTenant());
        dashboardService.saveLayout(currentLayoutModel.getObject());
        target.addComponent(sortableColumn);
        target.addComponent(sortableColumn2);
        target.addComponent(addWidgetPanel);
    }

    private void removeWidgetFromColumn(WidgetDefinition widgetToRemove, int columnIndex) {
        DashboardColumn column = currentLayoutModel.getObject().getColumns().get(columnIndex);
        List<WidgetDefinition> widgets = column.getWidgets();
        for (WidgetDefinition widgetDefinition : widgets) {
            if (widgetDefinition.getWidgetType() == widgetToRemove.getWidgetType()) {
                widgets.remove(widgetDefinition);
                break;
            }
        }
    }

    private void removeWidgetFromColumn(int columnIndex, int widgetIndex) {
        currentLayoutModel.getObject().getColumns().get(columnIndex).getWidgets().remove(widgetIndex);
    }

    private void addWidgetToColumn(WidgetDefinition widgetToAdd, int columnIndex, int widgetIndex) {
        currentLayoutModel.getObject().getColumns().get(columnIndex).getWidgets().add(widgetIndex, widgetToAdd);
    }

    @Override
    protected boolean useLegacyCss() {
        return false;
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.dashboard"));
    }

    public static class DashboardColumnContainer extends WebMarkupContainer {
        public DashboardColumnContainer(String id) {
            super(id);
        }
    }

}
