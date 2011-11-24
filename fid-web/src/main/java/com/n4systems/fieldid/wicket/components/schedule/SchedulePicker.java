package com.n4systems.fieldid.wicket.components.schedule;

import com.n4systems.fieldid.wicket.components.DateTimePicker;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.EventType;
import com.n4systems.model.Project;
import com.n4systems.util.time.DateUtil;
import org.apache.commons.lang.time.DateUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.Date;
import java.util.List;

public class SchedulePicker extends Panel {

    private IModel<EventSchedule> scheduleModel;

    public SchedulePicker(String id, IModel<EventSchedule> scheduleModel, IModel<List<EventType>> eventTypeOptions, IModel<List<Project>> jobsOptions) {
        super(id);
        this.scheduleModel = scheduleModel;
        setOutputMarkupId(true);

        add(new ScheduleForm("scheduleForm", scheduleModel, eventTypeOptions, jobsOptions));
        add(CSSPackageResource.getHeaderContribution("style/newCss/component/schedule_picker.css"));
    }

    class ScheduleForm extends Form<EventSchedule> {

        AjaxButton openDialogButton;
        WebMarkupContainer editorContainer;
        DateTimePicker dateTimePicker;

        public ScheduleForm(String id, IModel<EventSchedule> eventScheduleModel, IModel<List<EventType>> eventTypeOptions, IModel<List<Project>> jobsOptions) {
            super(id, eventScheduleModel);

            add(openDialogButton = new AjaxButton("openDialogButton") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    setEditorVisible(target, true);
                    target.appendJavascript("translate($('#"+editorContainer.getMarkupId()+"'), $('#"+openDialogButton.getMarkupId()+"'),0,0)");
                }
            });

            setDefaultEventType(eventScheduleModel, eventTypeOptions);

            add(editorContainer = new WebMarkupContainer("scheduleEditorContainer"));
            editorContainer.setOutputMarkupPlaceholderTag(true);
            editorContainer.setVisible(false);

            editorContainer.add(dateTimePicker = new DateTimePicker("datePicker", new PropertyModel<Date>(eventScheduleModel, "nextDate")));

            DropDownChoice<EventType> eventTypeSelect = new DropDownChoice<EventType>("eventTypeSelect", new PropertyModel<EventType>(eventScheduleModel, "eventType"), eventTypeOptions, new ListableChoiceRenderer<EventType>());
            DropDownChoice<Project> jobSelect = new DropDownChoice<Project>("jobSelect", new PropertyModel<Project>(eventScheduleModel, "project"), jobsOptions, new ListableChoiceRenderer<Project>());

            eventTypeSelect.setNullValid(false);
            jobSelect.setNullValid(true);

            editorContainer.add(eventTypeSelect);
            editorContainer.add(jobSelect);

            editorContainer.add(new AjaxButton("addScheduleButton") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    onPickComplete();
                    setEditorVisible(target, false);
                }
            });

            editorContainer.add(createQuickDateLink("quickLinkToday", 0, 0, 0));
            editorContainer.add(createQuickDateLink("quickLinkTomorrow", 1, 0, 0));
            editorContainer.add(createQuickDateLink("quickLinkNextMonth", 0, 1, 0));
            editorContainer.add(createQuickDateLink("quickLinkSixMonths", 0, 6, 0));
            editorContainer.add(createQuickDateLink("quickLinkNextYear", 0, 0, 1));

            editorContainer.add(new AjaxLink("cancelLink") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    setEditorVisible(target, false);
                }
            });

        }

        private void setDefaultEventType(IModel<EventSchedule> eventScheduleModel, IModel<List<EventType>> eventTypeOptions) {
            List<EventType> availableEventTypes = eventTypeOptions.getObject();
            EventSchedule eventSchedule = eventScheduleModel.getObject();
            if (eventSchedule.getEventType() == null && availableEventTypes.size() > 0) {
                eventSchedule.setEventType(availableEventTypes.get(0));
            }
        }

        private void setEditorVisible(AjaxRequestTarget target, boolean visible) {
            editorContainer.setVisible(visible);
            target.addComponent(editorContainer);
        }

        private AjaxLink createQuickDateLink(String id, final int daysFromNow, final int monthsFromNow, final int yearsFromNow) {
            return new AjaxLink(id) {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    Date dateToSchedule = DateUtils.addYears(new Date(), yearsFromNow);
                    dateToSchedule = DateUtils.addMonths(dateToSchedule, monthsFromNow);
                    dateToSchedule = DateUtils.addDays(dateToSchedule, daysFromNow);
                    scheduleModel.getObject().setNextDate(dateToSchedule);
                    target.addComponent(dateTimePicker);
                }
            };
        }
    }

    protected void onPickComplete() { }

}
