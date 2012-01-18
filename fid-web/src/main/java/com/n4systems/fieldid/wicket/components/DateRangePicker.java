package com.n4systems.fieldid.wicket.components;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.n4systems.util.chart.RangeType;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import com.n4systems.fieldid.wicket.utils.EnumDropDownChoiceRenderer;
import com.n4systems.model.utils.DateRange;


@SuppressWarnings("serial")
public class DateRangePicker extends Panel { //implements IFormModelUpdateListener {

	private DropDownChoice<RangeType> dropDownChoice;
	private DateTimePicker fromDatePicker;
	private DateTimePicker toDatePicker;
	// local fields used as temporary model.
	private Date from;
	private Date to;
	private RangeType dateRangeType;

	public DateRangePicker(String id, IModel<DateRange> model) {
		super(id, model);		
		dropDownChoice = new DropDownChoice<RangeType>("dateRange",
				new PropertyModel<RangeType>(model, "rangeType"),
				getDateRanges(), 
				new EnumDropDownChoiceRenderer<RangeType>());
		fromDatePicker = new DateTimePicker("fromDate", new PropertyModel<Date>(model, "fromDate"));
		toDatePicker = new DateTimePicker("toDate", new PropertyModel<Date>(model, "toDate"));
		
		dropDownChoice.setOutputMarkupId(true);
		dropDownChoice.setNullValid(false);
		
		add(dropDownChoice);    	
		add(fromDatePicker);
		add(toDatePicker);
	}		
	
	protected List<RangeType> getDateRanges() {
		return Arrays.asList(RangeType.rangeTypesWithCustom());
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
        response.renderJavaScriptReference("javascript/dateRange.js");		
        String javascript = "dateRangePicker.init('%s');";
        response.renderOnDomReadyJavaScript(String.format(javascript, dropDownChoice.getMarkupId()));
        super.renderHead(response);
	}
}

