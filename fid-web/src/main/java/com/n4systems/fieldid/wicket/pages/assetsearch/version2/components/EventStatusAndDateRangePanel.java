package com.n4systems.fieldid.wicket.pages.assetsearch.version2.components;

import com.n4systems.fieldid.wicket.components.DateRangePicker;
import com.n4systems.fieldid.wicket.components.renderer.ListableLabelChoiceRenderer;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.search.EventStatus;
import com.n4systems.model.search.IncludeDueDateRange;
import com.n4systems.model.utils.DateRange;
import com.n4systems.util.chart.RangeType;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.Arrays;

public class EventStatusAndDateRangePanel extends Panel {

    private DateRangePicker allRangePicker;
    private DateRangePicker completedRangePicker;
    private DateRangePicker dueRangePicker;
    private WebMarkupContainer includeDueDateRangeContainer;
    private boolean useDueDate;

    public EventStatusAndDateRangePanel(String id, final IModel<EventStatus> eventStatusModel, final IModel<IncludeDueDateRange> includeDueRangeModel, IModel<DateRange> completedRange, IModel<DateRange> dueRange) {
        super(id);

        useDueDate = includeDueRangeModel.getObject() != null;

        DropDownChoice<EventStatus> eventStatusSelect = new DropDownChoice<EventStatus>("eventStatusSelect", eventStatusModel, Arrays.asList(EventStatus.values()), new ListableLabelChoiceRenderer<EventStatus>());
        eventStatusSelect.setNullValid(false);
        add(eventStatusSelect);

        eventStatusSelect.add(new OnChangeAjaxBehavior() {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                repaintComponents(target);
                onEventStatusChanged(target);
            }
        });

        includeDueDateRangeContainer = new WebMarkupContainer("includeDueDateRangeContainer") {
            @Override
            public boolean isVisible() {
                return EventStatus.COMPLETE.equals(eventStatusModel.getObject());
            }
        };
        includeDueDateRangeContainer.setOutputMarkupPlaceholderTag(true);

        final DropDownChoice<IncludeDueDateRange> includeDueDateRange = new DropDownChoice<IncludeDueDateRange>("includeDueDateRangeSelect", includeDueRangeModel, Arrays.asList(IncludeDueDateRange.values()), new ListableLabelChoiceRenderer<IncludeDueDateRange>()) {
            @Override
            public boolean isEnabled() {
                return useDueDate || includeDueRangeModel.getObject() != null;
            }
        };
        includeDueDateRange.add(new OnChangeAjaxBehavior() {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                repaintComponents(target);
            }
        });
        includeDueDateRange.setNullValid(true);
        includeDueDateRangeContainer.add(includeDueDateRange);

        AjaxCheckBox dueRangeCheckbox = new AjaxCheckBox("dueRangeCheckbox", new PropertyModel<Boolean>(this, "useDueDate")) {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                repaintComponents(target);
                if (!useDueDate) {
                    includeDueRangeModel.setObject(null);
                }
            }
        };
        includeDueDateRangeContainer.add(dueRangeCheckbox);

        add(includeDueDateRangeContainer);

        add(completedRangePicker = new DateRangePicker("completeRangePicker", new FIDLabelModel("label.completed_date"), completedRange) {
            @Override
            public boolean isVisible() {
                return EventStatus.COMPLETE.equals(eventStatusModel.getObject());
            }
        });
        add(dueRangePicker = new DateRangePicker("dueRangePicker", new FIDLabelModel("label.due_date"), dueRange, RangeType.allFloatingTypes()) {
            @Override
            public boolean isVisible() {
                return EventStatus.INCOMPLETE.equals(eventStatusModel.getObject())
                        || (EventStatus.COMPLETE.equals(eventStatusModel.getObject()) && IncludeDueDateRange.SELECT_DUE_DATE_RANGE.equals(includeDueRangeModel.getObject()));
            }
        });
        add(allRangePicker = new DateRangePicker("allRangePicker", new FIDLabelModel("label.completed_or_due_date"), completedRange) {
            @Override
            public boolean isVisible() {
                return EventStatus.ALL.equals(eventStatusModel.getObject());
            }
        });
    }

    private void repaintComponents(AjaxRequestTarget target) {
        target.add(completedRangePicker);
        target.add(dueRangePicker);
        target.add(allRangePicker);
        target.add(includeDueDateRangeContainer);
    }

    protected void onEventStatusChanged(AjaxRequestTarget target) {}

}
