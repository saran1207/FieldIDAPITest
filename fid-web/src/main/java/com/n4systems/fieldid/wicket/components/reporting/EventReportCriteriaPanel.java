package com.n4systems.fieldid.wicket.components.reporting;

import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.fieldid.service.search.columns.DynamicColumnsService;
import com.n4systems.fieldid.service.search.columns.EventColumnsService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.DateTimePicker;
import com.n4systems.fieldid.wicket.components.IdentifierLabel;
import com.n4systems.fieldid.wicket.components.assettype.GroupedAssetTypePicker;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.location.LocationPicker;
import com.n4systems.fieldid.wicket.components.org.OrgPicker;
import com.n4systems.fieldid.wicket.components.renderer.EventTypeChoiceRenderer;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.fieldid.wicket.components.renderer.StatusChoiceRenderer;
import com.n4systems.fieldid.wicket.components.reporting.columns.SelectDisplayColumnsPanel;
import com.n4systems.fieldid.wicket.components.user.GroupedUserPicker;
import com.n4systems.fieldid.wicket.model.CombinedListModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.assetstatus.AssetStatusesForTenantModel;
import com.n4systems.fieldid.wicket.model.assettype.AssetTypeGroupsForTenantModel;
import com.n4systems.fieldid.wicket.model.assettype.GroupedAssetTypesForTenantModel;
import com.n4systems.fieldid.wicket.model.eventbook.EventBooksForTenantModel;
import com.n4systems.fieldid.wicket.model.eventtype.EventTypeGroupsForTenantModel;
import com.n4systems.fieldid.wicket.model.eventtype.EventTypesForTenantModel;
import com.n4systems.fieldid.wicket.model.jobs.EventJobsForTenantModel;
import com.n4systems.fieldid.wicket.model.user.GroupedUsersForTenantModel;
import com.n4systems.fieldid.wicket.model.user.UsersForTenantModel;
import com.n4systems.fieldid.wicket.pages.reporting.ReportingResultsPage;
import com.n4systems.fieldid.wicket.util.LegacyReportCriteriaStorage;
import com.n4systems.model.AssetStatus;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.EventBook;
import com.n4systems.model.EventType;
import com.n4systems.model.EventTypeGroup;
import com.n4systems.model.Project;
import com.n4systems.model.Status;
import com.n4systems.model.location.Location;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.search.ColumnMappingGroupView;
import com.n4systems.model.search.EventReportCriteriaModel;
import com.n4systems.model.search.ReportConfiguration;
import com.n4systems.model.user.User;
import com.n4systems.util.DateHelper;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.spring.injection.annot.SpringBean;
import rfid.web.helper.SessionUser;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

public class EventReportCriteriaPanel extends Panel implements IHeaderContributor {

    private @SpringBean DynamicColumnsService dynamicColumnsService;

    private SelectDisplayColumnsPanel selectDisplayColumnsPanel;

    private GroupedAssetTypePicker groupedAssetTypePicker;
    private DropDownChoice<EventType> eventTypeSelect;
    private ReportCriteriaForm reportCriteriaForm;

    public EventReportCriteriaPanel(String id) {
        this(id, new Model<EventReportCriteriaModel>(new EventReportCriteriaModel()));
    }

    public EventReportCriteriaPanel(String id, IModel<EventReportCriteriaModel> criteriaModel) {
        super(id);
        add(new FIDFeedbackPanel("feedbackPanel"));
        setOutputMarkupId(true);

        add(JavascriptPackageResource.getHeaderContribution("javascript/reportingForm.js"));

        add(CSSPackageResource.getHeaderContribution("style/pageStyles/search.css"));
        add(CSSPackageResource.getHeaderContribution("style/pageStyles/downloads.css"));

        add(new ContextImage("tipIcon", "images/tip-icon.png"));
        add(reportCriteriaForm = new ReportCriteriaForm("reportCriteriaForm", criteriaModel));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderOnDomReadyJavascript("observeFormChange('"+reportCriteriaForm.getMarkupId()+"');");
    }

    class ReportCriteriaForm extends Form<EventReportCriteriaModel> {

        WebMarkupContainer assignedUserContainer;
        List<ColumnMappingGroupView> configuredColumnGroups;

        public ReportCriteriaForm(String id, IModel<EventReportCriteriaModel> criteriaModel) {
            super(id, new CompoundPropertyModel<EventReportCriteriaModel>(criteriaModel));
            setOutputMarkupId(true);

            final IModel<List<ColumnMappingGroupView>> dynamicEventColumnsModel = new PropertyModel<List<ColumnMappingGroupView>>(getModel(), "dynamicEventColumnGroups");
            final IModel<List<ColumnMappingGroupView>> dynamicAssetColumnsModel = new PropertyModel<List<ColumnMappingGroupView>>(getModel(), "dynamicAssetColumnGroups");

            SessionUser sessionUser = FieldIDSession.get().getSessionUser();
            SystemSecurityGuard securityGuard = FieldIDSession.get().getSecurityGuard();

            initializeConfiguredColumns();

            WebMarkupContainer jobContainer = new WebMarkupContainer("jobContainer");
            add(jobContainer.setVisible(securityGuard.isProjectsEnabled()));
            add(new WebMarkupContainer("assignedToContainer").setVisible(securityGuard.isAssignedToEnabled()));

            WebMarkupContainer includeNetworkResultsContainer = new WebMarkupContainer("includeNetworkResultsContainer");
            add(includeNetworkResultsContainer.setVisible(sessionUser.isEmployeeUser() || sessionUser.isSystemUser()));
            add(new WebMarkupContainer("includeNetworkResultsContainerAlt").setVisible(!(sessionUser.isEmployeeUser() || sessionUser.isSystemUser())));
            add(new WebMarkupContainer("noJobsButAssignedToContainer").setVisible(!securityGuard.isProjectsEnabled() && securityGuard.isAssignedToEnabled()));
            add(new WebMarkupContainer("noAssignedToButJobsContainer").setVisible(securityGuard.isProjectsEnabled() && !securityGuard.isAssignedToEnabled()));

            add(assignedUserContainer = new WebMarkupContainer("assignedUserContainer"));
            assignedUserContainer.setVisible(securityGuard.isAssignedToEnabled());

            add(new OrgPicker("owner", new PropertyModel<BaseOrg>(getModel(), "owner")));
            add(new DateTimePicker("fromDate", new PropertyModel<Date>(getModel(), "fromDate")));
            add(new DateTimePicker("toDate", new PropertyModel<Date>(getModel(), "toDate") {
                @Override
                public void setObject(Date date) {
                    super.setObject(date == null ? null : DateHelper.convertToUTC(DateHelper.getEndOfDay(date), FieldIDSession.get().getSessionUser().getTimeZone()));
                }
            }));
            add(new LocationPicker("location", new PropertyModel<Location>(getModel(), "location")));

            final IModel<AssetTypeGroup> assetTypeGroupModel = new PropertyModel<AssetTypeGroup>(getModel(), "assetTypeGroup");
            final IModel<AssetType> assetTypeModel = new PropertyModel<AssetType>(getModel(), "assetType");
            final GroupedAssetTypesForTenantModel availableAssetTypesModel = new GroupedAssetTypesForTenantModel(assetTypeGroupModel);
            add(createAssetTypeGroupChoice(assetTypeGroupModel, dynamicAssetColumnsModel, assetTypeModel, availableAssetTypesModel));
            add(groupedAssetTypePicker = new GroupedAssetTypePicker("assetType", new PropertyModel<AssetType>(getModel(), "assetType"), availableAssetTypesModel));
            groupedAssetTypePicker.add(new AjaxFormComponentUpdatingBehavior("onchange") {
                @Override
                protected void onUpdate(AjaxRequestTarget target) {
                    updateDynamicAssetColumns(dynamicAssetColumnsModel, assetTypeModel, availableAssetTypesModel);
                    target.addComponent(selectDisplayColumnsPanel);
                }
            });
            groupedAssetTypePicker.setNullValid(true);
            // Initially, update the dynamic columns causing an empty list to be put into our model, unless it's already set
            if (!criteriaModel.getObject().isReportAlreadyRun()) {
                updateDynamicAssetColumns(dynamicAssetColumnsModel, assetTypeModel, availableAssetTypesModel);
            }
            groupedAssetTypePicker.setOutputMarkupId(true);

            GroupedUserPicker groupedUserPicker = new GroupedUserPicker("assignedTo", new PropertyModel<User>(getModel(), "assignedTo"), new GroupedUsersForTenantModel());
            groupedUserPicker.setNullValid(true);
            assignedUserContainer.add(groupedUserPicker);

            final IModel<EventTypeGroup> eventTypeGroupModel = new PropertyModel<EventTypeGroup>(getModel(), "eventTypeGroup");
            final IModel<EventType> eventTypeModel = new PropertyModel<EventType>(getModel(), "eventType");
            final EventTypesForTenantModel availableEventTypesModel = new EventTypesForTenantModel(eventTypeGroupModel);
            add(eventTypeSelect = new DropDownChoice<EventType>("eventType", availableEventTypesModel, new EventTypeChoiceRenderer()));
            eventTypeSelect.add(new AjaxFormComponentUpdatingBehavior("onchange") {
                @Override
                protected void onUpdate(AjaxRequestTarget target) {
                    updateDynamicEventColumns(dynamicEventColumnsModel, eventTypeModel, availableEventTypesModel);
                    target.addComponent(selectDisplayColumnsPanel);
                }
            });
            eventTypeSelect.setNullValid(true);
            // Initially, update the dynamic columns causing an empty list to be put into our model
            if (!criteriaModel.getObject().isReportAlreadyRun()) {
                updateDynamicEventColumns(dynamicEventColumnsModel, eventTypeModel, availableEventTypesModel);
            }
            eventTypeSelect.setOutputMarkupId(true);

            add(createEventTypeGroupChoice(eventTypeGroupModel, dynamicEventColumnsModel, eventTypeModel, availableEventTypesModel));

            add(new DropDownChoice<AssetStatus>("assetStatus", new AssetStatusesForTenantModel(), new ListableChoiceRenderer<AssetStatus>()).setNullValid(true));
            add(new DropDownChoice<EventBook>("eventBook", new EventBooksForTenantModel().addNullOption(true), new ListableChoiceRenderer<EventBook>()).setNullValid(true));
            add(new DropDownChoice<Status>("result", Arrays.asList(Status.values()), new StatusChoiceRenderer()).setNullValid(true));
            add(new DropDownChoice<User>("performedBy", new UsersForTenantModel(), new ListableChoiceRenderer<User>()).setNullValid(true));
            jobContainer.add(new DropDownChoice<Project>("job", new EventJobsForTenantModel(), new ListableChoiceRenderer<Project>()).setNullValid(true));

            add(new TextField<String>("rfidNumber"));
            add(new TextField<String>("identifier"));
            add(new TextField<String>("referenceNumber"));

            add(new TextField<String>("purchaseOrder"));
            add(new TextField<String>("orderNumber"));

            add(new IdentifierLabel("identifierLabel"));

            includeNetworkResultsContainer.add(new CheckBox("includeSafetyNetwork"));

            add(new Button("submitButton"));
            add(new AjaxLink("clearFormLink") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    ReportCriteriaForm.this.getModel().setObject(new EventReportCriteriaModel());
                    initializeConfiguredColumns();
                    target.addComponent(EventReportCriteriaPanel.this);
                }
            });

            SlidingReportSectionCollapseContainer container = new SlidingReportSectionCollapseContainer("displayColumnsContainer", new FIDLabelModel("label.selectcolumns"));

            container.addContainedPanel(selectDisplayColumnsPanel = new SelectDisplayColumnsPanel("selectDisplayColumnsPanel",
                    new PropertyModel<List<ColumnMappingGroupView>>(getModel(), "columnGroups"),
                    new CombinedListModel<ColumnMappingGroupView>(dynamicEventColumnsModel, dynamicAssetColumnsModel)));

            add(container);
        }

        private void initializeConfiguredColumns() {
            if (!getModelObject().isReportAlreadyRun()) {
                ReportConfiguration reportConfiguration = new EventColumnsService().getReportConfiguration(FieldIDSession.get().getSessionUser().getSecurityFilter());
                getModelObject().setColumnGroups(reportConfiguration.getColumnGroups());
                getModelObject().setSortColumn(reportConfiguration.getSortColumn());
                getModelObject().setSortDirection(reportConfiguration.getSortDirection());
            }
        }

        @Override
        protected void onSubmit() {
            if (getModelObject().getSortedStaticAndDynamicColumns().isEmpty()) {
                error(new FIDLabelModel("error.nocolumnsselected").getObject());
                return;
            }
            HttpSession session = ((WebRequest) getRequest()).getHttpServletRequest().getSession();
            getModelObject().setReportAlreadyRun(true);
            getModelObject().getSelection().clear();
            new LegacyReportCriteriaStorage().storeCriteria(getModelObject(), session);
            setResponsePage(new ReportingResultsPage(getModelObject()));
        }

    }

    private void updateDynamicEventColumns(IModel<List<ColumnMappingGroupView>> dynamicEventColumnsModel, IModel<EventType> eventTypeModel, EventTypesForTenantModel availableEventTypesModel) {
        List<ColumnMappingGroupView> dynamicColumns = dynamicColumnsService.getDynamicEventColumns(eventTypeModel.getObject(), availableEventTypesModel.getObject());
        dynamicEventColumnsModel.setObject(dynamicColumns);
    }

    private void updateDynamicAssetColumns(IModel<List<ColumnMappingGroupView>> dynamicAssetColumnsModel, IModel<AssetType> assetTypeModel, GroupedAssetTypesForTenantModel availableAssetTypesModel) {
        List<ColumnMappingGroupView> dynamicColumns = dynamicColumnsService.getDynamicAssetColumns(assetTypeModel.getObject(), availableAssetTypesModel.getObject());
        dynamicAssetColumnsModel.setObject(dynamicColumns);
    }

    private DropDownChoice<AssetTypeGroup> createAssetTypeGroupChoice(IModel<AssetTypeGroup> assetTypeGroupModel, final IModel<List<ColumnMappingGroupView>> dynamicAssetColumnsModel, final IModel<AssetType> assetTypeModel, final GroupedAssetTypesForTenantModel availableAssetTypesModel) {
        DropDownChoice<AssetTypeGroup> assetTypeGroupDropDownChoice = new DropDownChoice<AssetTypeGroup>("assetTypeGroup",
                assetTypeGroupModel, new AssetTypeGroupsForTenantModel(), new ListableChoiceRenderer<AssetTypeGroup>());
        assetTypeGroupDropDownChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                List<ColumnMappingGroupView> dynamicColumns = dynamicColumnsService.getDynamicAssetColumns(assetTypeModel.getObject(), availableAssetTypesModel.getObject());
                dynamicAssetColumnsModel.setObject(dynamicColumns);
                target.addComponent(selectDisplayColumnsPanel);
                target.addComponent(groupedAssetTypePicker);
            }
        });
        assetTypeGroupDropDownChoice.setNullValid(true);
        return assetTypeGroupDropDownChoice;
    }

    private DropDownChoice<EventTypeGroup> createEventTypeGroupChoice(IModel<EventTypeGroup> eventTypeGroupModel, final IModel<List<ColumnMappingGroupView>> dynamicEventColumnsModel, final IModel<EventType> eventTypeModel, final IModel<List<EventType>> availableEventTypesModel) {
        DropDownChoice<EventTypeGroup> eventTypeGroupDropDownChoice = new DropDownChoice<EventTypeGroup>("eventTypeGroup",
                eventTypeGroupModel, new EventTypeGroupsForTenantModel(), new ListableChoiceRenderer<EventTypeGroup>());
        eventTypeGroupDropDownChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                List<ColumnMappingGroupView> dynamicColumns = dynamicColumnsService.getDynamicEventColumns(eventTypeModel.getObject(), availableEventTypesModel.getObject());
                dynamicEventColumnsModel.setObject(dynamicColumns);
                target.addComponent(selectDisplayColumnsPanel);
                target.addComponent(eventTypeSelect);
            }
        });
        eventTypeGroupDropDownChoice.setNullValid(true);
        return eventTypeGroupDropDownChoice;
    }

}
