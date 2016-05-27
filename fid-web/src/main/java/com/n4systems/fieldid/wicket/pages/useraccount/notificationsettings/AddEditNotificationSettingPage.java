package com.n4systems.fieldid.wicket.pages.useraccount.notificationsettings;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.asset.AssetStatusService;
import com.n4systems.fieldid.service.asset.AssetTypeService;
import com.n4systems.fieldid.service.event.EventTypeService;
import com.n4systems.fieldid.service.notificationsetting.NotificationSettingService;
import com.n4systems.fieldid.wicket.behavior.UpdateComponentOnChange;
import com.n4systems.fieldid.wicket.behavior.validation.ValidationBehavior;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.assettype.GroupedAssetTypePicker;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.components.org.OrgLocationPicker;
import com.n4systems.fieldid.wicket.components.renderer.EventTypeChoiceRenderer;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.LocalizeModel;
import com.n4systems.fieldid.wicket.model.assetstatus.AssetStatusesForTenantModel;
import com.n4systems.fieldid.wicket.model.assettype.AssetTypeGroupsForTenantModel;
import com.n4systems.fieldid.wicket.model.assettype.GroupedAssetTypesForTenantModel;
import com.n4systems.fieldid.wicket.model.eventtype.EventTypeGroupsForTenantModel;
import com.n4systems.fieldid.wicket.model.eventtype.EventTypesForTenantModel;
import com.n4systems.fieldid.wicket.pages.useraccount.AccountSetupPage;
import com.n4systems.fieldid.wicket.pages.useraccount.mobileofflineprofile.MobileOfflineProfilePage;
import com.n4systems.model.*;
import com.n4systems.model.common.RelativeTime;
import com.n4systems.model.common.SimpleFrequency;
import com.n4systems.model.notificationsettings.NotificationSetting;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import java.util.List;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

public class AddEditNotificationSettingPage extends AccountSetupPage {


    @SpringBean
    private NotificationSettingService notificationSettingService;
    @SpringBean
    private AssetTypeService assetTypeService;
    @SpringBean
    private EventTypeService eventTypeService;
    @SpringBean
    private AssetStatusService assetStatusService;

    private Form form;

    private IModel<NotificationSetting> notificationSettingModel;
    private IModel<List<AssetType>> availableAssetTypesModel;
    private GroupedAssetTypePicker groupedAssetTypePicker;
    private IModel<List<? extends EventType>> availableEventTypesModel;

    private AssetType assetType;
    private EventType eventType;

    private FIDFeedbackPanel feedbackPanel;
    private FidDropDownChoice<EventType> eventTypeSelect;
    private WebMarkupContainer emailAddressesContainer;

    public AddEditNotificationSettingPage() {
        NotificationSetting notificationSetting = new NotificationSetting();
        notificationSetting.setUser(getCurrentUser());
        notificationSetting.setTenant(getTenant());
        notificationSettingModel = Model.of(notificationSetting);
    }

    public AddEditNotificationSettingPage(PageParameters params) {
        super(params);
        Long id = params.get("uniqueID").toLong();
        notificationSettingModel = Model.of(notificationSettingService.findById(id));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        add(feedbackPanel = new FIDFeedbackPanel("feedbackPanel"));
        add(form = new Form<>("form"));

        form.add(new RequiredTextField<>("name", new PropertyModel<>(notificationSettingModel, "name")));
        form.add(new FidDropDownChoice<>("frequency",
                new PropertyModel<>(notificationSettingModel, "frequency"),
                Lists.newArrayList(SimpleFrequency.values()),
                new IChoiceRenderer<SimpleFrequency>() {
                    @Override
                    public Object getDisplayValue(SimpleFrequency object) {
                        return new FIDLabelModel(object.getLabel()).getObject();
                    }

                    @Override
                    public String getIdValue(SimpleFrequency object, int index) {
                        return object.name();
                    }
                }));
        form.add(new CheckBox("sendBlankReport", new PropertyModel<>(notificationSettingModel, "sendBlankReport")));

        form.add(new CheckBox("includeUpcoming", new PropertyModel<>(notificationSettingModel, "upcomingReport.includeUpcoming")));

        IChoiceRenderer<RelativeTime> relativeTimeChoiceRenderer = new IChoiceRenderer<RelativeTime>() {

            @Override
            public Object getDisplayValue(RelativeTime object) {
                return new FIDLabelModel(object.getLabel()).getObject();
            }

            @Override
            public String getIdValue(RelativeTime object, int index) {
                return object.name();
            }
        };

        form.add(new FidDropDownChoice<>("periodStart",
                                    new PropertyModel<>(notificationSettingModel, "upcomingReport.periodStart"),
                                    Lists.newArrayList(RelativeTime.START_TIME),
                                    relativeTimeChoiceRenderer));

        form.add(new FidDropDownChoice<>("periodEnd",
                                    new PropertyModel<>(notificationSettingModel, "upcomingReport.periodEnd"),
                                    Lists.newArrayList(RelativeTime.PERIOD_LENGTH),
                                    relativeTimeChoiceRenderer));

        form.add(new CheckBox("includeOverdue", new PropertyModel<>(notificationSettingModel, "overdueReport.includeOverdue")));

        CheckBox smartFailCheck;
        form.add(smartFailCheck = new CheckBox("smartFail", new PropertyModel<>(notificationSettingModel, "failedReport.smartFailure")));
        smartFailCheck.setEnabled(false);
        smartFailCheck.setOutputMarkupId(true);
        form.add(new CheckBox("includeFailed", new PropertyModel<Boolean>(notificationSettingModel, "failedReport.includeFailed")).add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                if(notificationSettingModel.getObject().isIncludeFailed()) {
                    smartFailCheck.setEnabled(true);
                } else {
                    smartFailCheck.setEnabled(false);
                }
                target.add(smartFailCheck);
            }
        }));

        form.add(new OrgLocationPicker("ownerPicker", new PropertyModel<>(notificationSettingModel, "owner")));

        IModel<AssetTypeGroup> assetTypeGroupModel = new PropertyModel<AssetTypeGroup>(notificationSettingModel, "assetTypeGroup");
        availableAssetTypesModel = new LocalizeModel<List<AssetType>>(new GroupedAssetTypesForTenantModel(assetTypeGroupModel));
        form.add(createAssetTypeGroupChoice(assetTypeGroupModel));
        form.add(groupedAssetTypePicker = new GroupedAssetTypePicker("assetType",
                                                        new PropertyModel<AssetType>(notificationSettingModel,"assetType"),
                                                        availableAssetTypesModel));
        groupedAssetTypePicker.setNullValid(true);

        form.add(new FidDropDownChoice<>("assetStatus",
                new PropertyModel<>(notificationSettingModel, "assetStatus"),
                new LocalizeModel<List<AssetStatus>>(new AssetStatusesForTenantModel()),
                new ListableChoiceRenderer<>()).setNullValid(true));

        IModel<EventTypeGroup> eventTypeGroupModel = new PropertyModel<EventTypeGroup>(notificationSettingModel, "eventTypeGroup");
        availableEventTypesModel = new LocalizeModel<List<? extends EventType>>(new EventTypesForTenantModel(eventTypeGroupModel));
        form.add(eventTypeSelect = new FidDropDownChoice<EventType>("eventType",
                                            new PropertyModel<EventType>(notificationSettingModel, "eventType"),
                                            availableEventTypesModel, new EventTypeChoiceRenderer()));
        eventTypeSelect.setNullValid(true);

        form.add(createEventTypeGroupChoice(eventTypeGroupModel));

        emailAddressesContainer = new WebMarkupContainer("emailAddressesContainer");
        emailAddressesContainer.setOutputMarkupPlaceholderTag(true);
        form.add(emailAddressesContainer);

        if (notificationSettingModel.getObject().getAddresses().isEmpty()) {
            notificationSettingModel.getObject().getAddresses().add("");
        }

        emailAddressesContainer.add(new ListView<String>("emailAddresses", new PropertyModel<List<String>>(notificationSettingModel, "addresses")) {
            @Override
            protected void populateItem(final ListItem<String> item) {
                EmailTextField addressField = new EmailTextField("address", item.getModel());
                addressField.setRequired(true);
                addressField.add(new UpdateComponentOnChange() {
                    @Override
                    protected void onUpdate(AjaxRequestTarget target) {
                        target.add(feedbackPanel);
                    }
                });
                ValidationBehavior.addValidationBehaviorToComponent(addressField);
                addressField.add(new StringValidator.MaximumLengthValidator(255));
                item.add(addressField);
                item.add(new AjaxLink("deleteLink") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        notificationSettingModel.getObject().getAddresses().remove(item.getIndex());
                        target.add(emailAddressesContainer);
                    }
                });
            }
        });

        emailAddressesContainer.add(new AjaxLink("addEmailAddressLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                notificationSettingModel.getObject().getAddresses().add("");
                target.add(emailAddressesContainer);
            }
        });

        form.add(new SubmitLink("saveLink") {
            @Override
            public void onSubmit() {
                notificationSettingService.saveOrUpdate(notificationSettingModel.getObject());
                setResponsePage(NotificationSettingsListPage.class);
            }
        });

        form.add(new Link("cancelLink") {
            @Override
            public void onClick() {
                setResponsePage(NotificationSettingsListPage.class);
            }
        });
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("nav.notification_settings"));
    }

    private FidDropDownChoice<AssetTypeGroup> createAssetTypeGroupChoice(IModel<AssetTypeGroup> assetTypeGroupModel) {
        FidDropDownChoice<AssetTypeGroup> assetTypeGroupDropDownChoice = new FidDropDownChoice<AssetTypeGroup>("assetTypeGroup",
                assetTypeGroupModel, new LocalizeModel<List<AssetTypeGroup>>(new AssetTypeGroupsForTenantModel()), new ListableChoiceRenderer<AssetTypeGroup>());
        assetTypeGroupDropDownChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.add(groupedAssetTypePicker);
            }
        });
        assetTypeGroupDropDownChoice.setNullValid(true);
        return assetTypeGroupDropDownChoice;
    }

    private FidDropDownChoice<EventTypeGroup> createEventTypeGroupChoice(IModel<EventTypeGroup> eventTypeGroupModel) {
        FidDropDownChoice<EventTypeGroup> eventTypeGroupDropDownChoice = new FidDropDownChoice<EventTypeGroup>("eventTypeGroup",
                eventTypeGroupModel, new LocalizeModel<List<EventTypeGroup>>(new EventTypeGroupsForTenantModel()), new ListableChoiceRenderer<EventTypeGroup>());
        eventTypeGroupDropDownChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.add(eventTypeSelect);
            }
        });
        eventTypeGroupDropDownChoice.setNullValid(true);
        return eventTypeGroupDropDownChoice;
    }

    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                aNavItem().label("nav.details").page("myAccount.action").build(),
                aNavItem().label("nav.notification_settings").page(AddEditNotificationSettingPage.class).cond(hasEmailAlerts()).build(),
                aNavItem().label("nav.change_password").page("editPassword.action").build(),
                aNavItem().label("nav.mobile_passcode").page("viewMobilePasscode.action").build(),
                aNavItem().label("nav.mobile_profile").page(MobileOfflineProfilePage.class).build(),
                aNavItem().label("nav.downloads").page("showDownloads.action").build(),
                aNavItem().label("nav.excel_export").page("exportEvent.action").build()
        ));
    }

    @Override
    protected Component createBackToLink(String linkId, String linkLabelId) {
        return new BookmarkablePageLink<NotificationSettingsListPage>(linkId, NotificationSettingsListPage.class)
                .add(new FlatLabel(linkLabelId, new FIDLabelModel("label.back_to_list")));
    }
}
