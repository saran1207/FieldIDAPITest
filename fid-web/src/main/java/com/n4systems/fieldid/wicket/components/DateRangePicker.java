package com.n4systems.fieldid.wicket.components;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.utils.EnumDropDownChoiceRenderer;
import com.n4systems.model.utils.DateRange;
import com.n4systems.util.chart.RangeType;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class DateRangePicker extends Panel {

	private DropDownChoice<RangeType> dropDownChoice;
	private DateTimePicker fromDatePicker;
	private DateTimePicker toDatePicker;

    public DateRangePicker(String id, IModel<DateRange> model) {
        this(id, new FIDLabelModel("label.daterange"), model);
    }

	public DateRangePicker(String id, IModel<String> rangeLabel, IModel<DateRange> model) {
		super(id, model);

        setOutputMarkupPlaceholderTag(true);
        add(new Label("dateRangeLabel", rangeLabel));

		dropDownChoice = new DropDownChoice<RangeType>("dateRange",
				new PropertyModel<RangeType>(model, "rangeType"),
				getDateRanges(), 
				new EnumDropDownChoiceRenderer<RangeType>());
		fromDatePicker = new DateTimePicker("fromDate", new PropertyModel<Date>(model, "fromDate")) {
            @Override
            public boolean isEnabled() {
                return RangeType.CUSTOM.equals(dropDownChoice.getModelObject());
            }
        };
		toDatePicker = new DateTimePicker("toDate", new PropertyModel<Date>(model, "toDate")) {
            @Override
            public boolean isEnabled() {
                return RangeType.CUSTOM.equals(dropDownChoice.getModelObject());
            }
        };

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
		return Arrays.asList(RangeType.rangeTypesWithCustom());
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
        response.renderCSSReference("style/newCss/component/dateRange.css");		
        super.renderHead(response);
	}

}
