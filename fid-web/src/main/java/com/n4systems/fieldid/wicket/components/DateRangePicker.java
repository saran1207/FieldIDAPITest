package com.n4systems.fieldid.wicket.components;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import com.n4systems.fieldid.wicket.model.EndOfDayDateModel;
import com.n4systems.fieldid.wicket.model.UserToUTCDateModel;
import com.n4systems.fieldid.wicket.utils.EnumDropDownChoiceRenderer;
import com.n4systems.util.chart.ChartDateRange;




public class DateRangePicker<T> extends Panel {

	private DropDownChoice<ChartDateRange> dropDownChoice;
	private String dateRangeProperty = "dateRange";
	private String fromDateProperty = "fromDate";
	private String toDateProperty = "toDate";
	private DateTimePicker fromDatePicker;
	private DateTimePicker toDatePicker;
	
	public DateRangePicker(String id, IModel<T> model) {
		super(id, model);
		dropDownChoice = new DropDownChoice<ChartDateRange>("dateRange", new PropertyModel<ChartDateRange>(model,dateRangeProperty), getDateRanges(), new EnumDropDownChoiceRenderer<ChartDateRange>());
		fromDatePicker = new DateTimePicker("fromDate", new UserToUTCDateModel(new PropertyModel<Date>(model, fromDateProperty)));
		toDatePicker = new DateTimePicker("toDate", new UserToUTCDateModel(new EndOfDayDateModel(new PropertyModel<Date>(model, toDateProperty))));

		dropDownChoice.setOutputMarkupId(true);
		
        add(dropDownChoice);    	
        add(fromDatePicker);
		add(toDatePicker);		
	}
	
	protected List<ChartDateRange> getDateRanges() {
		return Arrays.asList(ChartDateRange.chartDateRangesWithCustom());
	}

	
	
	public void setRenderer(IChoiceRenderer<ChartDateRange> renderer) {
		dropDownChoice.setChoiceRenderer(renderer);
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
        response.renderJavaScriptReference("javascript/dateRange.js");		
        String javascript = "dateRangePicker.init('%s');";
        response.renderOnDomReadyJavaScript(String.format(javascript, dropDownChoice.getMarkupId()));
        super.renderHead(response);
	}
	
}

