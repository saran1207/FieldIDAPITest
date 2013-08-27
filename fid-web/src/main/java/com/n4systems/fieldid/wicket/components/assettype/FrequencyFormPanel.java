package com.n4systems.fieldid.wicket.components.assettype;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.event.AssociatedEventTypesService;
import com.n4systems.fieldid.wicket.FieldIDSession;
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
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;


public class FrequencyFormPanel extends Panel {

    @SpringBean
    private AssociatedEventTypesService associatedEventTypesService;

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
        public FrequencyForm(String id, IModel<AssetTypeSchedule> model) {
            super(id, model);

            add(new FidDropDownChoice<EventType>("eventType", new PropertyModel<EventType>(model, "eventType"), getEventTypes(), new EventTypeChoiceRenderer()).setRequired(true));
            add(new RequiredTextField<Long>("frequencyInDays", new PropertyModel<Long>(model, "frequencyInDays")));
            add(new OrgLocationPicker("owner", new PropertyModel<BaseOrg>(model, "owner")));
            add(new CheckBox("autoSchedule", new PropertyModel<Boolean>(model, "autoSchedule")));
            add(new Label("autoScheduleMessage", new FIDLabelModel("label.automatically_schedule_checkbox", assetType.getObject().getDisplayName())));

            add(new AjaxSubmitLink("submit") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    AssetTypeSchedule schedule = assetTypeSchedule.getObject();
                    schedule.setTenant(FieldIDSession.get().getTenant());
                    schedule.setAssetType(assetType.getObject());
                    onSaveSchedule(target, schedule);
                    //assetTypeSchedule = Model.of(new AssetTypeSchedule());
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
