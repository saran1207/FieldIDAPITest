package com.n4systems.fieldid.wicket.pages.assetsearch.version2;

import java.util.Collections;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.n4systems.fieldid.service.search.columns.AssetColumnsService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.reporting.columns.SelectDisplayColumnsPanel;
import com.n4systems.fieldid.wicket.model.CombinedListModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.AssetType;
import com.n4systems.model.EventType;
import com.n4systems.model.search.AssetSearchCriteriaModel;
import com.n4systems.model.search.ColumnMappingGroupView;
import com.n4systems.model.search.ReportConfiguration;


@SuppressWarnings("serial")
public class SearchColumnsPanel extends AbstractConfigPanel {

	
	@SpringBean 
	private AssetColumnsService assetColumnsService;
	
	private SelectDisplayColumnsPanel selectDisplayColumnsPanel = null;	
	
	
	public SearchColumnsPanel(String id, Model<AssetSearchCriteriaModel> model, final Mediator mediator) {
		super(id, model, mediator);
        setOutputMarkupId(true);	
	}

	@Override
	protected Form<AssetSearchCriteriaModel> createForm(String id, Model<AssetSearchCriteriaModel> model) {
		return new ColumnsForm(id,model);
	}

	
	// ----------------------------------------------------------------------------------------

	
    public class ColumnsForm extends Form<AssetSearchCriteriaModel> {

        WebMarkupContainer assignedUserContainer;
        List<ColumnMappingGroupView> configuredColumnGroups;

        IModel<List<ColumnMappingGroupView>> dynamicAssetColumnsModel;
        IModel<List<ColumnMappingGroupView>> dynamicEventColumnsModel;

        public ColumnsForm(String id, IModel<AssetSearchCriteriaModel> criteriaModel) {
            super(id, new CompoundPropertyModel<AssetSearchCriteriaModel>(criteriaModel));
            setOutputMarkupId(true);

            dynamicAssetColumnsModel = new PropertyModel<List<ColumnMappingGroupView>>(getModel(), "dynamicAssetColumnGroups");
            dynamicEventColumnsModel = new PropertyModel<List<ColumnMappingGroupView>>(getModel(), "dynamicEventColumnGroups");

            // This has two functions - first to load the default report template configuration: columns, sort
            // Second to initialize the dynamic columns: Some tenants have each and every asset/event type with the same attributes.
            // They expect to see them without first selecting any event type group or event type.
            initializeConfiguredColumns(dynamicAssetColumnsModel, dynamicEventColumnsModel);            

            PropertyModel<List<ColumnMappingGroupView>> columnsModel = new PropertyModel<List<ColumnMappingGroupView>>(getModel(), "columnGroups");
            add(new ListView<ColumnMappingGroupView>("columnGroups", columnsModel)  {
                @Override
                protected void populateItem(ListItem<ColumnMappingGroupView> item) {
               		item.add(createCollapsibleColumnsPanel(item));
                }
            });

            CombinedListModel<ColumnMappingGroupView> dynamicColumsModel = new CombinedListModel<ColumnMappingGroupView>(dynamicEventColumnsModel, dynamicAssetColumnsModel);
			add(new ListView<ColumnMappingGroupView>("dynamicColumnGroups",  dynamicColumsModel) {
                @Override
                protected void populateItem(final ListItem<ColumnMappingGroupView> item) {                	
                	item.add(createCollapsibleColumnsPanel(item));
                }
            });            
            
        }

        private Component createCollapsibleColumnsPanel(
        		ListItem<ColumnMappingGroupView> item) {
        	CollapsiblePanel collapsiblePanel = new CollapsiblePanel("columnGroup",  new FIDLabelModel(new PropertyModel<String>(item.getModel(),"label")), "images/columnlayout/arrow-over.png", "images/columnlayout/arrow-down.png");
        	collapsiblePanel.addContainedPanel(new ColumnGroupPanel(collapsiblePanel.getContainedPanelMarkupId(), item.getModel()));        	
        	return collapsiblePanel;        	
        }
        
        private void initializeConfiguredColumns(IModel<List<ColumnMappingGroupView>> dynamicAssetColumnsModel, IModel<List<ColumnMappingGroupView>> dynamicEventColumnsModel) {
            if (!getModelObject().isReportAlreadyRun()) {
                ReportConfiguration reportConfiguration = loadReportConfiguration();
                getModelObject().setColumnGroups(reportConfiguration.getColumnGroups());
                getModelObject().setSortColumn(reportConfiguration.getSortColumn());
                getModelObject().setSortDirection(reportConfiguration.getSortDirection());
            }
        }

        @Override
        protected void onSubmit() {
            if (getModelObject().getSortedStaticAndDynamicColumns().isEmpty()) {
                error(new FIDLabelModel("error.nocolumnsselected").getObject());
                onNoDisplayColumnsSelected();
                return;
            }
            getModelObject().setReportAlreadyRun(true);
            getModelObject().getSelection().clear();
            //---make this refresh results object;
        }

    }
    
    protected ReportConfiguration loadReportConfiguration() {    	
        return assetColumnsService.getReportConfiguration(FieldIDSession.get().getSessionUser().getSecurityFilter());
    }    

//    private void updateDynamicEventColumns(IModel<List<ColumnMappingGroupView>> dynamicEventColumnsModel, EventType eventType, List<EventType> availableEventTypes) {
//        dynamicEventColumnsModel.setObject(getDynamicEventColumns(eventType,  availableEventTypes));
//    }
//
//    private void updateDynamicAssetColumns(IModel<List<ColumnMappingGroupView>> dynamicAssetColumnsModel, AssetType assetType, List<AssetType> availableAssetTypes) {
//        dynamicAssetColumnsModel.setObject(getDynamicAssetColumns(assetType, availableAssetTypes));
//    }
//
    protected List<ColumnMappingGroupView> getDynamicAssetColumns(AssetType assetType, List<AssetType> availableAssetTypes) {
        return Collections.<ColumnMappingGroupView>emptyList();
    }

    protected List<ColumnMappingGroupView> getDynamicEventColumns(EventType eventType, List<EventType> availableEventTypes) {
        return Collections.<ColumnMappingGroupView>emptyList();
    }

    protected void onNoDisplayColumnsSelected() {}

    @Override
    public void renderHead(IHeaderResponse response) {
//        response.renderJavaScriptReference("javascript/reportingForm.js");
    }
	
	@Override
	protected void updateMenu(Component... components ) {
		for (Component c:components) { 
			if (c.getId().equals("columns")) {				
				c.add(new AttributeAppender("class", new Model<String>("disabled"), " "));				
			} else if (c.getId().equals("filters")) { 
				c.add(createShowConfigBehavior("filter"));
			}			
		}
	}


    
}
