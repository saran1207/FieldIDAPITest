package com.n4systems.fieldid.wicket.components.assettype;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.event.AssociatedEventTypesService;
import com.n4systems.fieldid.service.schedule.AssetTypeScheduleService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.behavior.UpdateComponentOnChange;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.org.OrgLocationPicker;
import com.n4systems.fieldid.wicket.components.renderer.EventTypeChoiceRenderer;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeSchedule;
import com.n4systems.model.AssociatedEventType;
import com.n4systems.model.EventType;
import com.n4systems.model.orgs.BaseOrg;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.MinimumValidator;

import java.util.List;


public class FrequencyFormPanel extends Panel {

    @SpringBean
    private AssociatedEventTypesService associatedEventTypesService;

    @SpringBean
    private AssetTypeScheduleService assetTypeScheduleService;

    private IModel<AssetType> assetType;
    private IModel<AssetTypeSchedule> assetTypeSchedule;
    private FIDFeedbackPanel feedbackPanel;

    public FrequencyFormPanel(String id, IModel<AssetType> model) {
        super(id, model);
        this.assetType = model;
        assetTypeSchedule = Model.of(new AssetTypeSchedule());
        add(feedbackPanel = new FIDFeedbackPanel("feedbackPanel"));
        add(new FrequencyForm("form", assetTypeSchedule));
    }

    private class FrequencyForm extends Form<AssetTypeSchedule> {

        FidDropDownChoice<EventType> eventTypeChoice;
        RequiredTextField<Long> frequencyField;
        OrgLocationPicker orgLocationPicker;
        CheckBox autoScheduleCheck;
        public FrequencyForm(String id, final IModel<AssetTypeSchedule> model) {
            super(id, model);

            model.getObject().setTenant(FieldIDSession.get().getTenant());
            List<EventType> eventTypes = getEventTypes();
            if(!getEventTypes().isEmpty())
                model.getObject().setEventType(eventTypes.get(0));

            add(eventTypeChoice = new FidDropDownChoice<EventType>("eventType", new PropertyModel<EventType>(model, "eventType"), eventTypes, new EventTypeChoiceRenderer()));
            eventTypeChoice.setRequired(true);
            eventTypeChoice.add(new UpdateComponentOnChange());
            add(frequencyField = new RequiredTextField<Long>("frequencyInDays", new PropertyModel<Long>(model, "frequencyInDays"), Long.class));
            frequencyField.add(new MinimumValidator<Long>(1L));
            frequencyField.add(new UpdateComponentOnChange());
            add(orgLocationPicker = new OrgLocationPicker("owner", new PropertyModel<BaseOrg>(model, "owner")).withAutoUpdate());
            add(autoScheduleCheck = new CheckBox("autoSchedule", new PropertyModel<Boolean>(model, "autoSchedule")));
            autoScheduleCheck.add(new UpdateComponentOnChange());
            add(new Label("autoScheduleMessage", new FIDLabelModel("label.automatically_schedule_checkbox", assetType.getObject().getDisplayName())));

            add(new IFormValidator() {
                @Override
                public FormComponent<?>[] getDependentFormComponents() {
                    List<FormComponent> formComponents = Lists.newArrayList();
                    formComponents.add(eventTypeChoice);
                    formComponents.add(frequencyField);
                    formComponents.add(orgLocationPicker);
                    formComponents.add(autoScheduleCheck);
                    return formComponents.toArray(new FormComponent[formComponents.size()]);
                }

                @Override
                public void validate(Form<?> form) {
                    if (assetTypeScheduleService.scheduleExists(assetTypeSchedule.getObject()))
                        form.error(getString("error.existing_asset_type_schedule"));
                }
            });

            add(new AjaxSubmitLink("submit") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    AssetTypeSchedule schedule = assetTypeSchedule.getObject();
                    schedule.setAssetType(assetType.getObject());
                    if(schedule.getOwner() == null) {
                        //we need to revisit this logic since it makes it impossible to create an override for the primary org
                        //@see AssetType.getSchedule
                        schedule.setOwner(FieldIDSession.get().getPrimaryOrg());
                    }
                    onSaveSchedule(target, schedule);
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedbackPanel);
                }
            });
        }
    }

    protected void onSaveSchedule(AjaxRequestTarget target, AssetTypeSchedule schedule) {}

    private List<EventType> getEventTypes() {
        List<EventType> eventTypes = Lists.newArrayList();
        List<AssociatedEventType> associatedEventTypes = associatedEventTypesService.getAssociatedEventTypes(assetType.getObject(), null);
        for (AssociatedEventType type: associatedEventTypes) {
            eventTypes.add(type.getEventType());
        }
        return eventTypes;
    }

}
