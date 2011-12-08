package com.n4systems.fieldid.wicket.pages;

import com.n4systems.fieldid.ui.seenit.SeenItRegistryDatabaseDataSource;
import com.n4systems.fieldid.ui.seenit.SeenItRegistryImpl;
import com.n4systems.fieldid.wicket.behavior.SimpleSortableAjaxBehavior;
import com.n4systems.fieldid.wicket.components.dashboard.AddWidgetPanel;
import com.n4systems.fieldid.wicket.components.modal.FIDModalWindow;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.dashboard.CurrentLayoutModel;
import com.n4systems.fieldid.wicket.pages.widgets.Widget;
import com.n4systems.fieldid.wicket.pages.widgets.WidgetFactory;
import com.n4systems.fieldid.wicket.util.JavascriptPackageResourceIE;
import com.n4systems.model.dashboard.DashboardColumn;
import com.n4systems.model.dashboard.DashboardLayout;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.WidgetType;
import com.n4systems.model.ui.seenit.SeenItItem;
import com.n4systems.services.dashboard.DashboardService;
import com.n4systems.util.ConfigurationProvider;
import org.apache.wicket.Component;
import org.apache.wicket.RedirectToUrlException;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.StringHeaderContributor;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.resource.ContextRelativeResource;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.ui.sortable.SortableAjaxBehavior;
import rfid.web.helper.SessionUser;

import java.util.List;

@SuppressWarnings("serial")
public class DashboardPage extends FieldIDFrontEndPage {

    @SpringBean
    private DashboardService dashboardService;
    
    @SpringBean
    private WidgetFactory widgetFactory;

    private AddWidgetPanel addWidgetPanel;

    private WebMarkupContainer columnsContainer;
    private WebMarkupContainer blankSlatePanel;
	private WebMarkupContainer content;

    IModel<DashboardLayout> currentLayoutModel;

    private ModalWindow configurationWindow;

    public DashboardPage() {
    	this(null);
    }

	@Deprecated // for testing only... need to find a generic way to override configProvider for all unit tests.
	public DashboardPage(ConfigurationProvider configProvider) {
    	super(configProvider);
    	
        redirectToSetupWizardIfNecessary();
        add(CSSPackageResource.getHeaderContribution("style/dashboard/dashboard.css"));

       	add(JavascriptPackageResourceIE.getHeaderContribution("javascript/flot/excanvas.min.js"));
        add(JavascriptPackageResource.getHeaderContribution("javascript/flot/jquery.flot.min.js"));                
        add(JavascriptPackageResource.getHeaderContribution("javascript/flot/jquery.flot.navigate.min.js"));        
        add(JavascriptPackageResource.getHeaderContribution("javascript/flot/jquery.flot.symbol.min.js"));        
        add(JavascriptPackageResource.getHeaderContribution("javascript/dashboard.js"));
        add(JavascriptPackageResource.getHeaderContribution("javascript/widget.js"));
        // override org picker with inline style...ugly but it works.  i'd suggest actually changing the default size (which is 450px)
        // but that would require regression testing.
        add(new StringHeaderContributor("<style type='text/css'>.orgSelector { width : 300px !important; }</style>")); 	
        
        currentLayoutModel = new CurrentLayoutModel();

        add(content = addContent("content"));
        add(CSSPackageResource.getHeaderContribution("style/dashboard/widgetconfig.css"));
    }

	private WebMarkupContainer addContent(String id) {
		WebMarkupContainer content = new WebMarkupContainer(id);
		content.setOutputMarkupId(true);
        content.add(addWidgetPanel = new AddWidgetPanel("addWidgetPanel", currentLayoutModel) {
            @Override
            protected void onWidgetTypeSelected(AjaxRequestTarget target, WidgetType type) {
                WidgetDefinition definition = new WidgetDefinition(type);
                currentLayoutModel.getObject().getColumns().get(0).getWidgets().add(0, definition);
                saveAndRepaintDashboard(target);
            }
        });

        columnsContainer = new WebMarkupContainer("columnsContainer");
        columnsContainer.add(createColumnContainer("sortableColumn", new PropertyModel<List<WidgetDefinition>>(currentLayoutModel, "columns[0].widgets"), 0));
        columnsContainer.add(createColumnContainer("sortableColumn2", new PropertyModel<List<WidgetDefinition>>(currentLayoutModel, "columns[1].widgets"), 1));
        columnsContainer.add(configurationWindow = new FIDModalWindow("configWindow"));
        configurationWindow.setInitialWidth(500);
        configurationWindow.setInitialHeight(390);

        configurationWindow.setTitle(new FIDLabelModel("label.widget_configuration"));

        columnsContainer.setOutputMarkupId(true);
        content.add(columnsContainer);
        content.add(blankSlatePanel = createBlankSlate("blankSlate"));   
        
        setContentVisibility();
        
        return content;
	}

	private void setContentVisibility() {
		boolean noWidgets = currentLayoutModel.getObject().getWidgetCount()==0; 
		blankSlatePanel.setVisible(noWidgets);
		columnsContainer.setVisible(!noWidgets);
	}

	private WebMarkupContainer createBlankSlate(String id) {
		WebMarkupContainer panel = new WebMarkupContainer(id);
		panel.setOutputMarkupId(true);
		panel.add(new Image("step1", new ContextRelativeResource("/images/dashboard/step1.png")));
		panel.add(new Image("step2", new ContextRelativeResource("/images/dashboard/step2.png")));
		panel.add(new Image("step3", new ContextRelativeResource("/images/dashboard/step3.png")));
		return panel;
	}

	private WebMarkupContainer createColumnContainer(String containerId, IModel<List<WidgetDefinition>> widgetsModel,  final int columnIndex) {
        WebMarkupContainer container = new WebMarkupContainer(containerId);

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
                widget.setConfigureBehavior(new AjaxEventBehavior("onclick") {
                    @Override
                    protected void onEvent(AjaxRequestTarget target) {
                        configurationWindow.setContent(widget.createConfigPanel(configurationWindow.getContentId()));
                        configurationWindow.show(target);
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

            @Override
            public void onUpdate(Component sortedComponent, int index, AjaxRequestTarget target) {
                if (sortedComponent != null && index != -1) {
                    WidgetDefinition movingWidget = (WidgetDefinition) sortedComponent.getDefaultModelObject();
                    moveWidgetInsideColumn(movingWidget, columnIndex, index);
                    saveAndRepaintDashboard(target);
                }
            }
        };
        simpleSortableAjaxBehavior.setCursor("pointer");
        simpleSortableAjaxBehavior.setHandle(".widget-draggable");
        simpleSortableAjaxBehavior.setConnectWith(".column");
        return simpleSortableAjaxBehavior;
    }

    private void moveWidgetInsideColumn(WidgetDefinition movingWidget, int columnIndex, int destinationIndex) {
        DashboardColumn column = currentLayoutModel.getObject().getColumns().get(columnIndex);
        int sourceIndex = column.getWidgets().indexOf(movingWidget);
        column.getWidgets().remove(sourceIndex);
        column.getWidgets().add(destinationIndex, movingWidget);
    }

    private void saveAndRepaintDashboard(AjaxRequestTarget target) {
        currentLayoutModel.getObject().setTenant(getTenant());
        dashboardService.saveLayout(currentLayoutModel.getObject());
        setContentVisibility();            	
        target.addComponent(content);        
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

    protected void redirectToSetupWizardIfNecessary() {
        SessionUser sessionUser = getSessionUser();
        SeenItRegistryImpl seenItRegistry = new SeenItRegistryImpl(new SeenItRegistryDatabaseDataSource(getSessionUser().getId()));
        boolean shouldRedirect = sessionUser.isAdmin() && !seenItRegistry.haveISeen(SeenItItem.SetupWizard);
        if (shouldRedirect) {
            throw new RedirectToUrlException("/quickSetupWizard/startWizard.action");
        }
    }

    public void closeConfigWindow(AjaxRequestTarget target) {
        configurationWindow.close(target);
        target.addComponent(columnsContainer);
    }

}
