package com.n4systems.fieldid.wicket.components;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import com.n4systems.fieldid.wicket.utils.EnumDropDownChoiceRenderer;
import com.n4systems.model.utils.DateRange;
import com.n4systems.util.chart.FloatingDateRange;


@SuppressWarnings("serial")
public class DateRangePicker extends Panel { //implements IFormModelUpdateListener {

	private DropDownChoice<FloatingDateRange> dropDownChoice;
	private DateTimePicker fromDatePicker;
	private DateTimePicker toDatePicker;
	// local fields used as temporary model.
	private Date from;
	private Date to;
	private FloatingDateRange dateRange;

	public DateRangePicker(String id, IModel<DateRange> model) {
		super(id, model);		
		dropDownChoice = new DropDownChoice<FloatingDateRange>("dateRange",
				new PropertyModel<FloatingDateRange>(model, "floatingDateRange"),
				getDateRanges(), 
				new EnumDropDownChoiceRenderer<FloatingDateRange>());
		fromDatePicker = new DateTimePicker("fromDate", new PropertyModel<Date>(this, "from"));
		toDatePicker = new DateTimePicker("toDate", new PropertyModel<Date>(this, "from"));
		
		dropDownChoice.setOutputMarkupId(true);
		dropDownChoice.setNullValid(false);
		
		add(dropDownChoice);    	
		add(fromDatePicker);
		add(toDatePicker);
	}		
	
	protected List<FloatingDateRange> getDateRanges() {
		return Arrays.asList(FloatingDateRange.chartDateRangesWithCustom());	
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
        response.renderJavaScriptReference("javascript/dateRange.js");		
        String javascript = "dateRangePicker.init('%s');";
        response.renderOnDomReadyJavaScript(String.format(javascript, dropDownChoice.getMarkupId()));
        super.renderHead(response);
	}
}

