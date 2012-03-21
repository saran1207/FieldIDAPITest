package com.n4systems.fieldid.wicket.components.search;

import com.n4systems.fieldid.wicket.components.assettype.GroupedAssetTypePicker;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.reporting.SlidingCollapsibleContainer;
import com.n4systems.fieldid.wicket.components.reporting.columns.SelectDisplayColumnsPanel;
import com.n4systems.fieldid.wicket.model.CombinedListModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.AssetType;
import com.n4systems.model.EventType;
import com.n4systems.model.saveditem.SavedItem;
import com.n4systems.model.search.ColumnMappingGroupView;
import com.n4systems.model.search.ReportConfiguration;
import com.n4systems.model.search.SearchCriteria;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.Collections;
import java.util.List;

@Deprecated  //copy & pasted into new class for version2 of search/reporting pages.  this class should be removed when switchover is complete.  (dd-03/2012)
public abstract class SRSCriteriaPanel<S extends SavedItem, T extends SearchCriteria> extends Panel {

    private SelectDisplayColumnsPanel selectDisplayColumnsPanel;

    private GroupedAssetTypePicker groupedAssetTypePicker;
    private DropDownChoice<EventType> eventTypeSelect;
    protected SearchCriteriaForm searchCriteriaForm;
    protected IModel<T> criteriaModel;
    protected S savedItem;

    public SRSCriteriaPanel(String id, IModel<T> criteriaModel, S savedItem) {
        super(id);
        this.criteriaModel = criteriaModel;
        this.savedItem = savedItem;
        add(new FIDFeedbackPanel("feedbackPanel"));
        setOutputMarkupId(true);

        add(new ContextImage("tipIcon", "images/tip-icon.png"));
        add(searchCriteriaForm = new SearchCriteriaForm("reportCriteriaForm", criteriaModel));
    }

    protected abstract void populateForm(SearchCriteriaForm form);
    protected abstract T createNewCriteriaModel();
    protected abstract WebPage createResultsPage(T criteria, S savedItem);
    protected abstract ReportConfiguration loadReportConfiguration();

    public class SearchCriteriaForm extends Form<T> {

        WebMarkupContainer assignedUserContainer;
        List<ColumnMappingGroupView> configuredColumnGroups;

        AssetDetailsCriteriaPanel assetDetailsCriteriaPanel;
        EventDetailsCriteriaPanel eventDetailsCriteriaPanel;

        IModel<List<ColumnMappingGroupView>> dynamicAssetColumnsModel;
        IModel<List<ColumnMappingGroupView>> dynamicEventColumnsModel;

        public SearchCriteriaForm(String id, IModel<T> criteriaModel) {
            super(id, new CompoundPropertyModel<T>(criteriaModel));
            setOutputMarkupId(true);

            dynamicAssetColumnsModel = new PropertyModel<List<ColumnMappingGroupView>>(getModel(), "dynamicAssetColumnGroups");
            dynamicEventColumnsModel = new PropertyModel<List<ColumnMappingGroupView>>(getModel(), "dynamicEventColumnGroups");

            // Initialize the specific form components for entering the search criteria.
            // This may include an asset details criteria panel, an event details criteria panel, or both
            populateForm(this);

            // This has two functions - first to load the default report template configuration: columns, sort
            // Second to initialize the dynamic columns: Some tenants have each and every asset/event type with the same attributes.
            // They expect to see them without first selecting any event type group or event type.
            initializeConfiguredColumns(dynamicAssetColumnsModel, dynamicEventColumnsModel);

            add(new Button("submitButton"));
            add(new AjaxLink("clearFormLink") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    SearchCriteriaForm.this.getModel().setObject(createNewCriteriaModel());
                    initializeConfiguredColumns(dynamicAssetColumnsModel, dynamicEventColumnsModel);
                    target.add(SRSCriteriaPanel.this);
                }
            });

            SlidingCollapsibleContainer container = new SlidingCollapsibleContainer("displayColumnsContainer", new FIDLabelModel("label.selectcolumns"));

            container.addContainedPanel(selectDisplayColumnsPanel = new SelectDisplayColumnsPanel("selectDisplayColumnsPanel",
                    new PropertyModel<List<ColumnMappingGroupView>>(getModel(), "columnGroups"),
                    new CombinedListModel<ColumnMappingGroupView>(dynamicEventColumnsModel, dynamicAssetColumnsModel)));

            add(container);
        }

        private void initializeConfiguredColumns(IModel<List<ColumnMappingGroupView>> dynamicAssetColumnsModel, IModel<List<ColumnMappingGroupView>> dynamicEventColumnsModel) {
            if (!getModelObject().isReportAlreadyRun()) {
                ReportConfiguration reportConfiguration = loadReportConfiguration();
                getModelObject().setColumnGroups(reportConfiguration.getColumnGroups());
                getModelObject().setSortColumn(reportConfiguration.getSortColumn());
                getModelObject().setSortDirection(reportConfiguration.getSortDirection());
                if (assetDetailsCriteriaPanel != null) {
                    updateDynamicAssetColumns(dynamicAssetColumnsModel, null, assetDetailsCriteriaPanel.getAvailableAssetTypesModel().getObject());
                }
                if (eventDetailsCriteriaPanel != null) {
                    updateDynamicEventColumns(dynamicEventColumnsModel, null, eventDetailsCriteriaPanel.getAvailableEventTypesModel().getObject());
                }
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
            setResponsePage(createResultsPage(getModelObject(), savedItem));
        }

        public void addAssetDetailsPanel(String id) {
            assetDetailsCriteriaPanel = new AssetDetailsCriteriaPanel(id, getModel()) {
                @Override
                protected void onAssetTypeOrGroupUpdated(AjaxRequestTarget target, AssetType selectedAssetType, List<AssetType> availableAssetTypes) {
                    updateDynamicAssetColumns(dynamicAssetColumnsModel, selectedAssetType, availableAssetTypes);
                    target.add(selectDisplayColumnsPanel);
                }
            };
            add(assetDetailsCriteriaPanel);
        }

        public void addEventDetailsPanel(String id) {
            eventDetailsCriteriaPanel = new EventDetailsCriteriaPanel(id, getModel()) {
                @Override
                protected void onEventTypeOrGroupUpdated(AjaxRequestTarget target, EventType selectedEventType, List<EventType> availableEventTypes) {
                    updateDynamicEventColumns(dynamicEventColumnsModel, selectedEventType, availableEventTypes);
                    target.add(selectDisplayColumnsPanel);
                }
            };
            add(eventDetailsCriteriaPanel);
        }

    }

    private void updateDynamicEventColumns(IModel<List<ColumnMappingGroupView>> dynamicEventColumnsModel, EventType eventType, List<EventType> availableEventTypes) {
        dynamicEventColumnsModel.setObject(getDynamicEventColumns(eventType,  availableEventTypes));
    }

    private void updateDynamicAssetColumns(IModel<List<ColumnMappingGroupView>> dynamicAssetColumnsModel, AssetType assetType, List<AssetType> availableAssetTypes) {
        dynamicAssetColumnsModel.setObject(getDynamicAssetColumns(assetType, availableAssetTypes));
    }

    protected List<ColumnMappingGroupView> getDynamicAssetColumns(AssetType assetType, List<AssetType> availableAssetTypes) {
        return Collections.<ColumnMappingGroupView>emptyList();
    }

    protected List<ColumnMappingGroupView> getDynamicEventColumns(EventType eventType, List<EventType> availableEventTypes) {
        return Collections.<ColumnMappingGroupView>emptyList();
    }

    protected void onNoDisplayColumnsSelected() {}

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderJavaScriptReference("javascript/reportingForm.js");
        response.renderCSSReference("style/pageStyles/search.css");
        response.renderCSSReference("style/pageStyles/downloads.css");
    }
}
