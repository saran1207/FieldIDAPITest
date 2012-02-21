package com.n4systems.fieldid.wicket.components.search.results;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.n4systems.fieldid.service.search.columns.AssetColumnsService;
import com.n4systems.fieldid.service.search.columns.DynamicColumnsService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.assettype.GroupedAssetTypesForTenantModel;
import com.n4systems.fieldid.wicket.pages.assetsearch.version2.components.CollapsiblePanel;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.search.AssetSearchCriteriaModel;
import com.n4systems.model.search.ColumnMappingGroupView;
import com.n4systems.model.search.ReportConfiguration;
import com.n4systems.model.search.SearchCriteriaModel;


@SuppressWarnings("serial")
public class SearchColumnsPanel extends Panel {

	@SpringBean private AssetColumnsService assetColumnsService;
	@SpringBean private DynamicColumnsService dynamicColumnsService;	
	
    private IModel<List<ColumnMappingGroupView>> dynamicAssetColumnsModel;
    WebMarkupContainer assignedUserContainer;
    List<ColumnMappingGroupView> configuredColumnGroups;
	private IModel<AssetSearchCriteriaModel> model;
	
	public SearchColumnsPanel(String id, IModel<AssetSearchCriteriaModel> model) {
		super(id, model);		
		setOutputMarkupId(true);
		setMarkupId(id);
		
		this.model = model;
		dynamicAssetColumnsModel = new PropertyModel<List<ColumnMappingGroupView>>(model, "dynamicAssetColumnGroups");
		
		// This has two functions - first to load the default report template configuration: columns, sort
		// Second to initialize the dynamic columns: Some tenants have each and every asset/event type with the same attributes.
		// They expect to see them without first selecting any event type group or event type.
		initializeConfiguredColumns(dynamicAssetColumnsModel);            
		
		PropertyModel<List<ColumnMappingGroupView>> columnsModel = new PropertyModel<List<ColumnMappingGroupView>>(model, "columnGroups");
		add(new ListView<ColumnMappingGroupView>("columnGroups", columnsModel)  {
			@Override
			protected void populateItem(ListItem<ColumnMappingGroupView> item) {
				item.add(createCollapsibleColumnsPanel(item));
			}
		});
		
		add(new ListView<ColumnMappingGroupView>("dynamicColumnGroups",  dynamicAssetColumnsModel) {
			@Override
			protected void populateItem(final ListItem<ColumnMappingGroupView> item) {                	
				item.add(createCollapsibleColumnsPanel(item));
			}
		});            
	}



//            super(id, new CompoundPropertyModel<AssetSearchCriteriaModel>(model));
            

        private Component createCollapsibleColumnsPanel(
        		final ListItem<ColumnMappingGroupView> item) {
        	CollapsiblePanel collapsiblePanel = new CollapsiblePanel("columnGroup",  new FIDLabelModel(new PropertyModel<String>(item.getModel(),"label"))) {
				@Override protected Panel createContainedPanel(String id) {
					return new ColumnGroupPanel(id, item.getModel());
				}        		
        	};
        	return collapsiblePanel;        	
        }
        
    private void initializeConfiguredColumns(IModel<List<ColumnMappingGroupView>> dynamicAssetColumnsModel) {
        if (!getAssetSearchCriteriaModel().isReportAlreadyRun()) {
            ReportConfiguration reportConfiguration = loadReportConfiguration();
            getAssetSearchCriteriaModel().setColumnGroups(reportConfiguration.getColumnGroups());
            getAssetSearchCriteriaModel().setSortColumn(reportConfiguration.getSortColumn());
            getAssetSearchCriteriaModel().setSortDirection(reportConfiguration.getSortDirection());
        }
        
        final IModel<AssetTypeGroup> assetTypeGroupModel = new PropertyModel<AssetTypeGroup>(getDefaultModel(), "assetTypeGroup");
        final IModel<AssetType> assetTypeModel = new PropertyModel<AssetType>(getDefaultModel(), "assetType");
        GroupedAssetTypesForTenantModel availableAssetTypesModel = new GroupedAssetTypesForTenantModel(assetTypeGroupModel);
        
        updateDynamicAssetColumns(null, availableAssetTypesModel.getObject());
    }


    private SearchCriteriaModel getAssetSearchCriteriaModel() {
			return model.getObject();
	}
    
    protected ReportConfiguration loadReportConfiguration() {    	
        return assetColumnsService.getReportConfiguration(FieldIDSession.get().getSessionUser().getSecurityFilter());
    }    

    public void updateDynamicAssetColumns(AssetType assetType, List<AssetType> availableAssetTypes) {
    	dynamicAssetColumnsModel.setObject(getDynamicAssetColumns(assetType, availableAssetTypes));
    }

    protected List<ColumnMappingGroupView> getDynamicAssetColumns(AssetType assetType, List<AssetType> availableAssetTypes) {
        return dynamicColumnsService.getDynamicAssetColumnsForSearch(assetType, availableAssetTypes);
    }
    
}
