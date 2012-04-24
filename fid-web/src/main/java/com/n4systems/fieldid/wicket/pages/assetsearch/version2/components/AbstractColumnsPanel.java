package com.n4systems.fieldid.wicket.pages.assetsearch.version2.components;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.search.columns.AssetColumnsService;
import com.n4systems.fieldid.service.search.columns.DynamicColumnsService;
import com.n4systems.fieldid.wicket.components.search.results.ColumnGroupPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.assettype.GroupedAssetTypesForTenantModel;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.EventType;
import com.n4systems.model.search.ColumnMappingGroupView;
import com.n4systems.model.search.ReportConfiguration;
import com.n4systems.model.search.SearchCriteria;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;


public abstract class AbstractColumnsPanel<T extends SearchCriteria> extends Panel {

	@SpringBean protected AssetColumnsService assetColumnsService;
	@SpringBean protected DynamicColumnsService dynamicColumnsService;

    protected IModel<List<ColumnMappingGroupView>> dynamicAssetColumnsModel;
    protected IModel<List<ColumnMappingGroupView>> dynamicEventColumnsModel;
    protected IModel<T> model;

    public AbstractColumnsPanel(String id, IModel<T> model) {
		super(id, model);
        this.model = model;
        setOutputMarkupId(true);

		// load the default report template configuration: columns, sort
		initialize(loadReportConfiguration());

		// initialize the dynamic columns: Some tenants have each and every asset/event type with the same attributes.
		// They expect to see them without first selecting any event type group or event type.
		dynamicAssetColumnsModel = new PropertyModel<List<ColumnMappingGroupView>>(model, "dynamicAssetColumnGroups");
        dynamicEventColumnsModel = new PropertyModel<List<ColumnMappingGroupView>>(model, "dynamicEventColumnGroups");
        updateColumns(dynamicAssetColumnsModel, dynamicEventColumnsModel);

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

        add(new ListView<ColumnMappingGroupView>("dynamicEventColumnGroups",  dynamicEventColumnsModel) {
            @Override
            protected void populateItem(final ListItem<ColumnMappingGroupView> item) {
                item.add(createCollapsibleColumnsPanel(item));
            }
        });

    }

    protected void updateColumns(IModel<List<ColumnMappingGroupView>> dynamicAssetColumnsModel, IModel<List<ColumnMappingGroupView>> dynamicEventColumnsModel) {
        final IModel<AssetTypeGroup> assetTypeGroupModel = new PropertyModel<AssetTypeGroup>(getDefaultModel(), "assetTypeGroup");
        final IModel<AssetType> assetTypeModel = new PropertyModel<AssetType>(getDefaultModel(), "assetType");
        GroupedAssetTypesForTenantModel availableAssetTypesModel = new GroupedAssetTypesForTenantModel(assetTypeGroupModel);
        if (!model.getObject().isReportAlreadyRun()) {
            updateDynamicAssetColumns(null, availableAssetTypesModel.getObject());
        }
    }

    private Component createCollapsibleColumnsPanel(
    		final ListItem<ColumnMappingGroupView> item) {
    	CollapsiblePanel collapsiblePanel = new CollapsiblePanel("columnGroup",  new FIDLabelModel(new PropertyModel<String>(item.getModel(),"label"))) {
			@Override protected Panel createContainedPanel(String id) {
				return new ColumnGroupPanel(id, item.getModel());
			}        		
    	};
    	return collapsiblePanel;        	
    }

    protected final void initialize(ReportConfiguration reportConfiguration) {
        if (!getSearchCriteria().isReportAlreadyRun()) {
            getSearchCriteria().setColumnGroups(reportConfiguration.getColumnGroups());
            getSearchCriteria().setSortColumn(reportConfiguration.getSortColumn());
            getSearchCriteria().setSortDirection(reportConfiguration.getSortDirection());
        }
    }

     protected T getSearchCriteria() { 
        return model.getObject();
     }

    protected abstract ReportConfiguration loadReportConfiguration();


    protected List<ColumnMappingGroupView> getDynamicAssetColumns(AssetType assetType, List<AssetType> availableAssetTypes) {
        return Lists.newArrayList();
    }

    protected List<ColumnMappingGroupView> getDynamicEventColumns(EventType eventType, List<EventType> availableEventTypes) {
        return Lists.newArrayList();
    }

    protected void updateDynamicEventColumns(EventType eventType, List<EventType> availableEventTypes) {
        dynamicEventColumnsModel.setObject(getDynamicEventColumns(eventType,  availableEventTypes));
    }

    protected void updateDynamicAssetColumns(AssetType assetType, List<AssetType> availableAssetTypes) {
        dynamicAssetColumnsModel.setObject(getDynamicAssetColumns(assetType, availableAssetTypes));
    }


}
