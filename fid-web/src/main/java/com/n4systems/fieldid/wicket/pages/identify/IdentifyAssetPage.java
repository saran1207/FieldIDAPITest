package com.n4systems.fieldid.wicket.pages.identify;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.asset.AssetIdentifierService;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.behavior.validation.ValidationBehavior;
import com.n4systems.fieldid.wicket.components.Comment;
import com.n4systems.fieldid.wicket.components.DateTimePicker;
import com.n4systems.fieldid.wicket.components.IdentifierLabel;
import com.n4systems.fieldid.wicket.components.assettype.GroupedAssetTypePicker;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.feedback.classy.AssetCreatedFeedbackMessage;
import com.n4systems.fieldid.wicket.components.modal.DialogModalWindow;
import com.n4systems.fieldid.wicket.components.org.AutoCompleteOrgPicker;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.fieldid.wicket.components.schedule.SchedulePicker;
import com.n4systems.fieldid.wicket.components.user.GroupedUserPicker;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.assetstatus.AssetStatusesForTenantModel;
import com.n4systems.fieldid.wicket.model.assettype.GroupedAssetTypesForTenantModel;
import com.n4systems.fieldid.wicket.model.eventtype.EventTypesForAssetTypeModel;
import com.n4systems.fieldid.wicket.model.jobs.EventJobsForTenantModel;
import com.n4systems.fieldid.wicket.model.user.GroupedVisibleUsersModel;
import com.n4systems.fieldid.wicket.pages.DashboardPage;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.components.ModalLocationPicker;
import com.n4systems.fieldid.wicket.pages.identify.components.*;
import com.n4systems.fieldid.wicket.pages.identify.components.multi.MultiIdentifyStatusDisplayPanel;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.*;
import com.n4systems.model.asset.AssetAttachment;
import com.n4systems.model.user.User;
import com.n4systems.services.asset.AssetSaveServiceSpring;
import com.n4systems.services.date.DateService;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.spring.injection.annot.SpringBean;
import rfid.ejb.entity.InfoOptionBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ch.lambdaj.Lambda.on;

public class IdentifyAssetPage extends FieldIDFrontEndPage {

    @SpringBean private AssetIdentifierService assetIdentifierService;
    @SpringBean private AssetService assetService;
    @SpringBean private EventService eventService;
    @SpringBean private DateService dateService;
    @SpringBean private AssetSaveServiceSpring assetSaveService;
    @SpringBean private PersistenceService persistenceService;

    EventSchedulesPanel eventSchedulesPanel;
    AttributesEditPanel attributesEditPanel;
    AssetAttachmentsPanel assetAttachmentsPanel;
    AssetImagePanel assetImagePanel;
    WebMarkupContainer rfidContainer;
    WebMarkupContainer actionsContainer;
    WebMarkupContainer singleIdentifyContainer;
    Component multiIdentifyContainer;

    SchedulePicker schedulePicker;
    Event currentEventSchedule = new Event();
    List<Event> schedulesToAdd = new ArrayList<Event>();
    MultipleAssetConfiguration multiAssetConfig = new MultipleAssetConfiguration();
    IModel<MultipleAssetConfiguration> multiAssetConfigModel = new PropertyModel<MultipleAssetConfiguration>(this, "multiAssetConfig");
    DialogModalWindow multipleWindow;

    public IdentifyAssetPage() {
        Asset asset = assetService.createAssetWithHistory();
        asset.setIdentified(dateService.todayAsDate());
        IModel<Asset> assetModel = Model.of(asset);
        Form modalContainerForm = new Form("modalContainerForm");
        modalContainerForm.add(multipleWindow = new DialogModalWindow("multiAssetConfigurationWindow", Model.of("Multiple Assets...")));
        add(modalContainerForm);
        add(new FIDFeedbackPanel("feedbackPanel"));
        add(schedulePicker = createSchedulePicker(ProxyModel.of(assetModel, on(Asset.class).getType())));
        add(new IdentifyAssetForm("identifyAssetForm", assetModel));
    }

    class IdentifyAssetForm extends Form<Asset> {
        public IdentifyAssetForm(String id, final IModel<Asset> assetModel) {
            super(id, assetModel);
            setMultiPart(true);

            IModel<AssetType> assetTypeModel = ProxyModel.of(assetModel, on(Asset.class).getType());
            GroupedAssetTypesForTenantModel allAssetTypesModel = new GroupedAssetTypesForTenantModel(Model.of((AssetTypeGroup) null));
            if (assetTypeModel.getObject() == null) {
                assetTypeModel.setObject(allAssetTypesModel.getObject().get(0));
            }

            autoSchedule(assetModel);

            GroupedAssetTypePicker assetTypePicker = new GroupedAssetTypePicker("assetType", assetTypeModel, allAssetTypesModel);
            add(assetTypePicker);

            add(new DateTimePicker("identifiedDate", ProxyModel.of(assetModel, on(Asset.class).getIdentified()), false).withNoAllDayCheckbox());

            singleIdentifyContainer = new WebMarkupContainer("singleIdentifyContainer");
            singleIdentifyContainer.setOutputMarkupPlaceholderTag(true);
            multiIdentifyContainer = new MultiIdentifyStatusDisplayPanel("multiIdentifyContainer", multiAssetConfigModel, multipleWindow);
            multiIdentifyContainer.setVisible(false);

            add(singleIdentifyContainer, multiIdentifyContainer);

            final WebMarkupContainer identifierContainer = createIdentifierContainer(assetModel);
            singleIdentifyContainer.add(identifierContainer);
            IModel<String> identifierModel = ProxyModel.of(assetModel, on(Asset.class).getIdentifier());
            TextField<String> identifierField = new TextField<String>("identifier", identifierModel);
            identifierField.add(new AjaxFormComponentUpdatingBehavior("onblur") {
                @Override
                protected void onUpdate(AjaxRequestTarget target) {
                    target.add(identifierContainer);
                }
            });
            ValidationBehavior.addValidationBehaviorToComponent(identifierField);
            identifierContainer.add(identifierField.setOutputMarkupId(true));
            addMultipleIdentificationControls(identifierContainer, assetTypeModel);

            final IdentifierLabel identifierLabel = new IdentifierLabel("identifierLabel", assetTypeModel);
            identifierContainer.add(identifierLabel);
            identifierContainer.add(createGenerateLink(identifierModel, identifierField, assetTypeModel));

            singleIdentifyContainer.add(rfidContainer = createRfidContainer(assetModel));
            rfidContainer.add(new TextField<String>("rfidNumber", ProxyModel.of(assetModel, on(Asset.class).getRfidNumber())).add(new AjaxFormComponentUpdatingBehavior("onblur") {
                @Override
                protected void onUpdate(AjaxRequestTarget target) {
                    target.add(rfidContainer);
                }
            }));
            singleIdentifyContainer.add(new TextField<String>("referenceNumber", ProxyModel.of(assetModel, on(Asset.class).getCustomerRefNumber())));


            final PropertyModel<User> assignedUserModel = ProxyModel.of(assetModel, on(Asset.class).getAssignedUser());
            final GroupedUserPicker assignedToSelect = new GroupedUserPicker("assignedToSelect", assignedUserModel, new GroupedVisibleUsersModel());
            add(assignedToSelect);

            add(new AutoCompleteOrgPicker("ownerPicker", ProxyModel.of(assetModel, on(Asset.class).getOwner())));

            add(new ModalLocationPicker("locationPicker", ProxyModel.of(assetModel, on(Asset.class).getAdvancedLocation())));

            add(new TextField<String>("purchaseOrder", ProxyModel.of(assetModel, on(Asset.class).getPurchaseOrder())));

            add(new DropDownChoice<AssetStatus>("assetStatusSelect", ProxyModel.of(assetModel, on(Asset.class).getAssetStatus()), new AssetStatusesForTenantModel(), new ListableChoiceRenderer<AssetStatus>()));

            add(new Comment("comments", ProxyModel.of(assetModel, on(Asset.class).getComments())));

            add(new DropDownChoice<Boolean>("visibilitySelect", ProxyModel.of(assetModel, on(Asset.class).isPublished()), Arrays.asList(false, true), new PublishOverSafetyNetworkChoiceRenderer()));

            add(new AjaxLink("assignToMeLink") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    assignedUserModel.setObject(getCurrentUser());
                    target.add(assignedToSelect);
                }
            });

            add(assetImagePanel = new AssetImagePanel("assetImagePanel"));

            add(assetAttachmentsPanel = new AssetAttachmentsPanel("assetAttachmentsPanel"));

            eventSchedulesPanel = new EventSchedulesPanel("eventSchedulesPanel", schedulePicker, new PropertyModel<List<Event>>(IdentifyAssetPage.this, "schedulesToAdd"));
            add(eventSchedulesPanel);

            add(attributesEditPanel = new AttributesEditPanel("attributesPanel", assetTypeModel));

            assetTypePicker.add(new AjaxFormComponentUpdatingBehavior("onchange") {
                @Override
                protected void onUpdate(AjaxRequestTarget target) {
                    autoSchedule(assetModel);
                    attributesEditPanel.refreshInfoOptions();
                    target.add(identifierLabel, eventSchedulesPanel, attributesEditPanel);
                }
            });

            add(actionsContainer = new WebMarkupContainer("actionsContainer"));
            actionsContainer.add(new Button("saveButton") {
                @Override
                public void onSubmit() {
                    if (multiAssetConfig.isConfigurationComplete()) {
                        Integer assetsCreated = processMultiSave(assetModel);
                        getSession().info(new FIDLabelModel("message.multiple_assets_created", assetsCreated).getObject());
                    } else {
                        Asset createdAsset = processAssetSave(assetModel);
                        getSession().info(new AssetCreatedFeedbackMessage(createdAsset.getId(), createdAsset.getIdentifier()));
                    }
                    setResponsePage(IdentifyAssetPage.class);
                }
            });

            actionsContainer.add(new Button("saveAndStartEventButton") {
                @Override
                public void onSubmit() {
                    Asset asset = processAssetSave(assetModel);
                    throw new RedirectToUrlException("/quickEvent.action?assetId=" + asset.getId());
                }
            });

            actionsContainer.add(new Button("saveAndPrintButton") {
                @Override
                public void onSubmit() {
                    Asset asset = processAssetSave(assetModel);
                    setResponsePage(new PrintIdentifiedCertPage(asset.getId()));
                }

                @Override
                public boolean isVisible() {
                    // Print not available for multi identify
                    return !multiAssetConfig.isConfigurationComplete();
                }
            });

            actionsContainer.setOutputMarkupId(true);
            actionsContainer.add(new BookmarkablePageLink<Void>("cancelLink", DashboardPage.class));
        }

        private WebMarkupContainer createRfidContainer(final IModel<Asset> newAssetModel) {
            WebMarkupContainer rfidContainer = new WebMarkupContainer("rfidContainer");
            rfidContainer.setOutputMarkupPlaceholderTag(true);
            rfidContainer.add(new AttributeAppender("class", Model.of("duplicate-field"), " ") {
                @Override
                public boolean isEnabled(Component component) {
                    return assetService.rfidExists(newAssetModel.getObject().getRfidNumber());
                }
            });
            rfidContainer.add(new WebMarkupContainer("duplicateRfidWarning") {
                @Override
                public boolean isVisible() {
                    return assetService.rfidExists(newAssetModel.getObject().getRfidNumber());
                }
            });
            return rfidContainer;
        }
        private WebMarkupContainer createIdentifierContainer(final IModel<Asset> newAssetModel) {
            WebMarkupContainer identifierContainer = new WebMarkupContainer("identifierContainer");
            identifierContainer.setOutputMarkupPlaceholderTag(true);
            identifierContainer.add(new AttributeAppender("class", Model.of("duplicate-field"), " ") {
                @Override
                public boolean isEnabled(Component component) {
                    return assetService.identifierExists(newAssetModel.getObject().getIdentifier());
                }
            });
            identifierContainer.add(new WebMarkupContainer("duplicateIdentifierWarning") {
                @Override
                public boolean isVisible() {
                    return assetService.identifierExists(newAssetModel.getObject().getIdentifier());
                }
            });
            return identifierContainer;
        }
    }

    private void addMultipleIdentificationControls(WebMarkupContainer component, IModel<AssetType> assetTypeModel) {
        multipleWindow.setContent(new MultipleAssetConfigurationPanel(multipleWindow.getContentId(), assetTypeModel,
                new PropertyModel<MultipleAssetConfiguration>(this, "multiAssetConfig")));
        component.add(new AjaxLink("identifyMultiple") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                multipleWindow.show(target);
            }
        });
        multipleWindow.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            @Override
            public void onClose(AjaxRequestTarget target) {
                if (multiAssetConfig.isConfigurationComplete()) {
                    singleIdentifyContainer.setVisible(false);
                    multiIdentifyContainer.setVisible(true);
                    target.add(singleIdentifyContainer, multiIdentifyContainer, actionsContainer);
                }
            }
        });
    }

    private void autoSchedule(IModel<Asset> assetModel) {
        schedulesToAdd.clear();
        if (assetModel.getObject().getType() != null) {
            schedulesToAdd.addAll(eventService.getAutoEventSchedules(assetModel.getObject()));
        }
    }

    private Asset processAssetSave(IModel<Asset> newAssetModel) {
        Asset asset = newAssetModel.getObject();
        asset.setTenant(getTenant());
        List<InfoOptionBean> enteredInfoOptions = attributesEditPanel.getEnteredInfoOptions();
        List<AssetAttachment> attachments = assetAttachmentsPanel.getAttachments();
        byte[] assetImageBytes = assetImagePanel.getAssetImageBytes();

        asset.getInfoOptions().clear();
        asset.getInfoOptions().addAll(enteredInfoOptions);

        Asset createdAsset = assetSaveService.create(asset, attachments, assetImageBytes);

        for (Event event : schedulesToAdd) {
            event.setAsset(createdAsset);
            event.setTenant(getTenant());
            event.setOwner(asset.getOwner());
            persistenceService.save(event);
        }

        return createdAsset;
    }

    private int processMultiSave(IModel<Asset> newAssetModel) {
        List<MultipleAssetConfiguration.AssetConfiguration> assetConfigs = multiAssetConfig.getAssetConfigs();
        Asset assetToCreate = newAssetModel.getObject();
        assetToCreate.setTenant(getTenant());
        List<InfoOptionBean> enteredInfoOptions = attributesEditPanel.getEnteredInfoOptions();
        List<AssetAttachment> attachments = assetAttachmentsPanel.getAttachments();
        byte[] assetImageBytes = assetImagePanel.getAssetImageBytes();

        for (MultipleAssetConfiguration.AssetConfiguration assetConfig : assetConfigs) {
            assetToCreate.reset();
            assetToCreate.getInfoOptions().clear();
            assetToCreate.getInfoOptions().addAll(enteredInfoOptions);
            assetToCreate.setIdentifier(assetConfig.getIdentifier());
            assetToCreate.setRfidNumber(assetConfig.getRfidNumber());
            assetToCreate.setCustomerRefNumber(assetConfig.getCustomerRefNumber());
            assetSaveService.create(assetToCreate, attachments, assetImageBytes);
        }

        return assetConfigs.size();
    }

    private Component createGenerateLink(final IModel<String> identifierModel, final TextField<String> identifierField, final IModel<AssetType> assetTypeModel) {
        return new AjaxLink("generateIdentifierLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                String generatedIdentifier = assetIdentifierService.generateIdentifier(assetTypeModel.getObject());
                identifierModel.setObject(generatedIdentifier);
                identifierField.clearInput();
                target.add(identifierField);
            }
        };
    }

    private SchedulePicker createSchedulePicker(IModel<AssetType> assetTypeModel) {
        return new SchedulePicker("schedulePicker", new PropertyModel<Event>(this, "currentEventSchedule"), new EventTypesForAssetTypeModel(assetTypeModel), new EventJobsForTenantModel()) {
            {
                setWindowClosedCallback(new WindowClosedCallback() {
                    @Override
                    public void onClose(AjaxRequestTarget target) {
                        Event previouslyStoredEventSchedule = FieldIDSession.get().getPreviouslyStoredEventSchedule();
                        if (previouslyStoredEventSchedule != null) {
                            FieldIDSession.get().setPreviouslyStoredEventSchedule(null);
                            schedulesToAdd.add(previouslyStoredEventSchedule);
                            currentEventSchedule = new Event();
                            target.add(eventSchedulesPanel);
                        }
                    }
                });
            }
            @Override
            protected void onPickComplete(AjaxRequestTarget target) {
                // Have to use session to pass this into the window close callback due to multiple pagemaps
                FieldIDSession.get().setPreviouslyStoredEventSchedule(currentEventSchedule);
            }
        };
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.identify"));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/asset/identify_asset.css");
    }

}