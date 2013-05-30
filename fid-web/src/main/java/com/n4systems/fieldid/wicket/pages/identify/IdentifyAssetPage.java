package com.n4systems.fieldid.wicket.pages.identify;

import com.n4systems.fieldid.service.asset.AssetIdentifierService;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.Comment;
import com.n4systems.fieldid.wicket.components.DateTimePicker;
import com.n4systems.fieldid.wicket.components.IdentifierLabel;
import com.n4systems.fieldid.wicket.components.assettype.GroupedAssetTypePicker;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.location.LocationPicker;
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
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.identify.components.AssetAttachmentsPanel;
import com.n4systems.fieldid.wicket.pages.identify.components.AssetImagePanel;
import com.n4systems.fieldid.wicket.pages.identify.components.AttributesEditPanel;
import com.n4systems.fieldid.wicket.pages.identify.components.EventSchedulesPanel;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.*;
import com.n4systems.model.user.User;
import com.n4systems.services.date.DateService;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ch.lambdaj.Lambda.on;

public class IdentifyAssetPage extends FieldIDFrontEndPage {

    @SpringBean
    private AssetIdentifierService assetIdentifierService;

    @SpringBean
    private AssetService assetService;

    @SpringBean
    private DateService dateService;

    EventSchedulesPanel eventSchedulesPanel;

    SchedulePicker schedulePicker;
    Event currentEventSchedule = new Event();
    List<Event> schedulesToAdd = new ArrayList<Event>();

    public IdentifyAssetPage() {
        Asset asset = assetService.createAssetWithHistory();
        asset.setIdentified(dateService.todayAsDate());
        IModel<Asset> newAssetModel = Model.of(asset);
        add(new FIDFeedbackPanel("feedbackPanel"));
        add(schedulePicker = createSchedulePicker(ProxyModel.of(newAssetModel, on(Asset.class).getType())));
        add(new IdentifyAssetForm("identifyAssetForm", newAssetModel));
    }


    class IdentifyAssetForm extends Form<Asset> {
        public IdentifyAssetForm(String id, IModel<Asset> newAssetModel) {
            super(id, newAssetModel);
            IModel<AssetType> assetTypeModel = ProxyModel.of(newAssetModel, on(Asset.class).getType());
            GroupedAssetTypePicker assetTypePicker = new GroupedAssetTypePicker("assetType", assetTypeModel, new GroupedAssetTypesForTenantModel(Model.of((AssetTypeGroup) null)));
            add(assetTypePicker);
            IModel<String> identifierModel = ProxyModel.of(newAssetModel, on(Asset.class).getIdentifier());
            TextField<String> identifierField = new RequiredTextField<String>("identifier", identifierModel);
            add(identifierField.setOutputMarkupId(true));

            add(new TextField<String>("rfidNumber", identifierModel));
            add(new TextField<String>("referenceNumber", identifierModel));

            add(new DateTimePicker("identifiedDate", ProxyModel.of(newAssetModel, on(Asset.class).getIdentified()), false).withNoAllDayCheckbox());

            final IdentifierLabel identifierLabel = new IdentifierLabel("identifierLabel", assetTypeModel);
            add(identifierLabel);

            final PropertyModel<User> assignedUserModel = ProxyModel.of(newAssetModel, on(Asset.class).getAssignedUser());
            final GroupedUserPicker assignedToSelect = new GroupedUserPicker("assignedToSelect", assignedUserModel, new GroupedVisibleUsersModel());
            add(assignedToSelect);

            add(new AutoCompleteOrgPicker("ownerPicker", ProxyModel.of(newAssetModel, on(Asset.class).getOwner())));
            add(new LocationPicker("locationPicker", ProxyModel.of(newAssetModel, on(Asset.class).getAdvancedLocation())));

            add(new TextField<String>("purchaseOrder", ProxyModel.of(newAssetModel, on(Asset.class).getPurchaseOrder())));

            add(new DropDownChoice<AssetStatus>("assetStatusSelect", ProxyModel.of(newAssetModel, on(Asset.class).getAssetStatus()), new AssetStatusesForTenantModel(), new ListableChoiceRenderer<AssetStatus>()));

            add(new Comment("comments", ProxyModel.of(newAssetModel, on(Asset.class).getComments())));

            add(new DropDownChoice<Boolean>("visibilitySelect", ProxyModel.of(newAssetModel, on(Asset.class).isPublished()), Arrays.asList(false, true), new PublishOverSafetyNetworkChoiceRenderer()));

            add(new AjaxLink("assignToMeLink") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    assignedUserModel.setObject(getCurrentUser());
                    target.add(assignedToSelect);
                }
            });

            add(createGenerateLink(identifierModel, identifierField, assetTypeModel));

            final AssetImagePanel assetImagePanel = new AssetImagePanel("assetImagePanel");
            add(assetImagePanel);

            final AssetAttachmentsPanel assetAttachmentsPanel = new AssetAttachmentsPanel("assetAttachmentsPanel");
            add(assetAttachmentsPanel);

            eventSchedulesPanel = new EventSchedulesPanel("eventSchedulesPanel", schedulePicker, new PropertyModel<List<Event>>(IdentifyAssetPage.this, "schedulesToAdd"));
            add(eventSchedulesPanel);

            add(new AttributesEditPanel("attributesPanel", assetTypeModel));

            assetTypePicker.add(new OnChangeAjaxBehavior() {
                @Override
                protected void onUpdate(AjaxRequestTarget target) {
                    schedulesToAdd.clear();
                    target.add(identifierLabel, eventSchedulesPanel);
                }
            });

            add(new Button("saveButton") {
                @Override
                public void onSubmit() {
                }
            });

            add(new Button("saveAndStartEventButton") {
                @Override
                public void onSubmit() {
                }
            });
        }
    }

    private Component createGenerateLink(final IModel<String> identifierModel, final TextField<String> identifierField, final IModel<AssetType> assetTypeModel) {
        return new AjaxLink("generateIdentifierLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                String generatedIdentifier = assetIdentifierService.generateIdentifier(assetTypeModel.getObject());
                identifierModel.setObject(generatedIdentifier);
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

}
