package com.n4systems.fieldid.wicket.pages.assetsearch.version2;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.n4systems.fieldid.service.search.columns.AssetColumnsService;
import com.n4systems.fieldid.service.search.columns.DynamicColumnsService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.assettype.GroupedAssetTypesForTenantModel;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.search.AssetSearchCriteriaModel;
import com.n4systems.model.search.ColumnMappingGroupView;
import com.n4systems.model.search.ReportConfiguration;


@SuppressWarnings("serial")
public class SearchColumnsPanel extends AbstractSearchConfigPanel {

	@SpringBean private AssetColumnsService assetColumnsService;
	@SpringBean private DynamicColumnsService dynamicColumnsService;	
	
    private IModel<List<ColumnMappingGroupView>> dynamicAssetColumnsModel;
	
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


        public ColumnsForm(String id, IModel<AssetSearchCriteriaModel> model) {
            super(id, new CompoundPropertyModel<AssetSearchCriteriaModel>(model));
            setOutputMarkupId(true);

            dynamicAssetColumnsModel = new PropertyModel<List<ColumnMappingGroupView>>(getModel(), "dynamicAssetColumnGroups");

            // This has two functions - first to load the default report template configuration: columns, sort
            // Second to initialize the dynamic columns: Some tenants have each and every asset/event type with the same attributes.
            // They expect to see them without first selecting any event type group or event type.
            initializeConfiguredColumns(dynamicAssetColumnsModel);            

            PropertyModel<List<ColumnMappingGroupView>> columnsModel = new PropertyModel<List<ColumnMappingGroupView>>(getModel(), "columnGroups");
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

        private Component createCollapsibleColumnsPanel(
        		final ListItem<ColumnMappingGroupView> item) {
        	CollapsiblePanel collapsiblePanel = new CollapsiblePanel("columnGroup",  new FIDLabelModel(new PropertyModel<String>(item.getModel(),"label")), EXPAND_IMG, COLLAPSE_IMG) {
				@Override protected Panel createContainedPanel(String id) {
					return new ColumnGroupPanel(id, item.getModel());
				}
        		
        	};
        	return collapsiblePanel;        	
        }
        
        private void initializeConfiguredColumns(IModel<List<ColumnMappingGroupView>> dynamicAssetColumnsModel) {
            if (!getModelObject().isReportAlreadyRun()) {
                ReportConfiguration reportConfiguration = loadReportConfiguration();
                getModelObject().setColumnGroups(reportConfiguration.getColumnGroups());
                getModelObject().setSortColumn(reportConfiguration.getSortColumn());
                getModelObject().setSortDirection(reportConfiguration.getSortDirection());
            }
            
            final IModel<AssetTypeGroup> assetTypeGroupModel = new PropertyModel<AssetTypeGroup>(getDefaultModel(), "assetTypeGroup");
            final IModel<AssetType> assetTypeModel = new PropertyModel<AssetType>(getDefaultModel(), "assetType");
            GroupedAssetTypesForTenantModel availableAssetTypesModel = new GroupedAssetTypesForTenantModel(assetTypeGroupModel);
            
            updateDynamicAssetColumns(null, availableAssetTypesModel.getObject());
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

    public void updateDynamicAssetColumns(AssetType assetType, List<AssetType> availableAssetTypes) {
    	dynamicAssetColumnsModel.setObject(getDynamicAssetColumns(assetType, availableAssetTypes));
    }

    protected List<ColumnMappingGroupView> getDynamicAssetColumns(AssetType assetType, List<AssetType> availableAssetTypes) {
        return dynamicColumnsService.getDynamicAssetColumnsForSearch(assetType, availableAssetTypes);
    }

    protected void onNoDisplayColumnsSelected() {}

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
