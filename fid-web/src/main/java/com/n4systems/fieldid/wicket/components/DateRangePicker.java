package com.n4systems.fieldid.wicket.components;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.util.EnumDropDownChoiceRenderer;
import com.n4systems.model.utils.DateRange;
import com.n4systems.util.chart.RangeType;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.Arrays;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

public class DateRangePicker extends Panel {

    private static final EnumSet<RangeType> DEFAULT_RANGE_TYPES = RangeType.allFloatingButFutureTypes();

    private FidDropDownChoice<RangeType> dropDownChoice;
	private DateTimePicker fromDatePicker;
	private DateTimePicker toDatePicker;
    private EnumSet<RangeType> rangeTypes;

    public DateRangePicker(String id, IModel<DateRange> model) {
        this(id, new FIDLabelModel("label.daterange"), model, DEFAULT_RANGE_TYPES);
    }

    public DateRangePicker(String id, IModel<String> rangeLabel, IModel<DateRange> model) {
        this(id,rangeLabel, model, DEFAULT_RANGE_TYPES);
    }

    public DateRangePicker(String id, IModel<String> rangeLabel, IModel<DateRange> model, EnumSet<RangeType> ranges) {
		super(id, model);
        this.rangeTypes = ranges;

        setOutputMarkupPlaceholderTag(true);
        add(new Label("dateRangeLabel", rangeLabel));

		dropDownChoice = new FidDropDownChoice<RangeType>("dateRange",
				new PropertyModel<RangeType>(model, "rangeType"),
				getDateRanges(),
				new EnumDropDownChoiceRenderer<RangeType>());
		fromDatePicker = new DateTimePicker("fromDate", new PropertyModel<Date>(model, "fromDate")) {
            @Override
            public boolean isEnabled() {
                return RangeType.CUSTOM.equals(dropDownChoice.getModelObject());
            }
        }.withNoAllDayCheckbox();
		toDatePicker = new DateTimePicker("toDate", new PropertyModel<Date>(model, "toDate")) {
            @Override
            public boolean isEnabled() {
                return RangeType.CUSTOM.equals(dropDownChoice.getModelObject());
            }
        }.withNoAllDayCheckbox();

		dropDownChoice.setOutputMarkupId(true);
		dropDownChoice.setNullValid(false);
		dropDownChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			@Override protected void onUpdate(AjaxRequestTarget target) {
				target.add(fromDatePicker, toDatePicker);
			}
		});

		add(dropDownChoice, fromDatePicker, toDatePicker);
	}

	protected List<RangeType> getDateRanges() {
		return Arrays.asList(rangeTypes.toArray(new RangeType[]{}));
	}

	@Override
	public void renderHead(IHeaderResponse response) {
        response.renderCSSReference("style/newCss/component/dateRange.css");		
        super.renderHead(response);
	}

}
