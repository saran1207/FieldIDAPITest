package com.n4systems.fieldid.wicket.pages.assetsearch.version2.components;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.search.columns.AssetColumnsService;
import com.n4systems.fieldid.service.search.columns.DynamicColumnsService;
import com.n4systems.fieldid.wicket.components.search.results.ColumnGroupPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.AssetType;
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
    private IModel<T> model;

    public AbstractColumnsPanel(String id, IModel<T> model) {
		super(id, model);
        this.model = model;
        setOutputMarkupId(true);
        setMarkupId(id);
        
		dynamicAssetColumnsModel = new PropertyModel<List<ColumnMappingGroupView>>(model, "dynamicAssetColumnGroups");
        dynamicEventColumnsModel = new PropertyModel<List<ColumnMappingGroupView>>(model, "dynamicEventColumnGroups");
		
		// This has two functions - first to load the default report template configuration: columns, sort
		// Second to initialize the dynamic columns: Some tenants have each and every asset/event type with the same attributes.
		// They expect to see them without first selecting any event type group or event type.
		initialize(loadReportConfiguration());
        udpateColumns(dynamicAssetColumnsModel, dynamicEventColumnsModel);

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

    protected void udpateColumns(IModel<List<ColumnMappingGroupView>> dynamicAssetColumnsModel, IModel<List<ColumnMappingGroupView>> dynamicEventColumnsModel) {
//        dynamicEventColumnsModel.setObject(...);
//        dynamicAssetColumnsModel.setObject(...);

//        updateDynamicAssetColumns(dynamicAssetColumnsModel, null, assetDetailsCriteriaPanel.getAvailableAssetTypesModel().getObject());
//        updateDynamicEventColumns(dynamicEventColumnsModel, null, eventDetailsCriteriaPanel.getAvailableEventTypesModel().getObject());
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

    protected void updateDynamicEventColumns(IModel<List<ColumnMappingGroupView>> dynamicEventColumnsModel, EventType eventType, List<EventType> availableEventTypes) {
        dynamicEventColumnsModel.setObject(getDynamicEventColumns(eventType,  availableEventTypes));
    }

    protected void updateDynamicAssetColumns(AssetType assetType, List<AssetType> availableAssetTypes) {
        dynamicAssetColumnsModel.setObject(getDynamicAssetColumns(assetType, availableAssetTypes));
    }



//    @Override
//    protected void populateForm(SRSCriteriaPanel.SearchCriteriaForm form) {
//        PropertyModel<EventStatus> eventStatusModel = new PropertyModel<EventStatus>(form.getModel(), "eventStatus");
//        PropertyModel<IncludeDueDateRange> includeDueDateRangeModel = new PropertyModel<IncludeDueDateRange>(form.getModel(), "includeDueDateRange");
//        PropertyModel<DateRange> completedDateRange = new PropertyModel<DateRange>(form.getModel(), "dateRange");
//        PropertyModel<DateRange> dueDateRange = new PropertyModel<DateRange>(form.getModel(), "dueDateRange");
//
//        form.add(new EventStatusAndDateRangePanel("eventStatusAndDateRangePanel", eventStatusModel, includeDueDateRangeModel, completedDateRange, dueDateRange) {
//            @Override
//            protected void onEventStatusChanged(AjaxRequestTarget target) {
//                criteriaModel.getObject().clearDateRanges();
//            }
//        });
//
//        form.addAssetDetailsPanel("assetDetailsCriteriaPanel");
//        form.addEventDetailsPanel("eventDetailsCriteriaPanel");
//
//        form.add(new com.n4systems.fieldid.wicket.components.search.IdentifiersCriteriaPanel("identifiersCriteriaPanel", form.getModel()));
//
//        form.add(new OwnershipCriteriaPanel("ownershipCriteriaPanel", form.getModel()));
//
//        form.add(new OrderDetailsCriteriaPanel("orderDetailsCriteriaPanel"));
//    }
//
//    @Override
//    protected EventReportCriteria createNewCriteriaModel() {
//        return new EventReportCriteria();
//    }
//
//    @Override
//    protected WebPage createResultsPage(EventReportCriteria criteria, SavedReportItem savedItem) {
//        HttpSession session = ((ServletWebRequest) getRequest()).getContainerRequest().getSession();
//        new LegacyReportCriteriaStorage().storeCriteria(criteria, session);
//        return new ReportingResultsPage(criteria, savedItem);
//    }
//
//    @Override
//    protected ReportConfiguration loadReportConfiguration() {
//        return new EventColumnsService().getReportConfiguration(FieldIDSession.get().getSessionUser().getSecurityFilter());
//    }
//
//    @Override
//    protected List<ColumnMappingGroupView> getDynamicAssetColumns(AssetType assetType, List<AssetType> availableAssetTypes) {
//        return dynamicColumnsService.getDynamicAssetColumnsForReporting(assetType, availableAssetTypes);
//    }
//
//    @Override
//    protected List<ColumnMappingGroupView> getDynamicEventColumns(EventType eventType, List<EventType> availableEventTypes) {
//        return dynamicColumnsService.getDynamicEventColumnsForReporting(eventType, availableEventTypes);
//    }



}
