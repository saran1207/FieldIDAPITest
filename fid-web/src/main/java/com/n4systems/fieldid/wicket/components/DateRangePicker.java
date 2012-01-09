package com.n4systems.fieldid.wicket.components;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
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
	
	@SuppressWarnings("serial")
	public DateRangePicker(String id, IModel<T> model) {
		super(id, model);
		setOutputMarkupId(true);
		dropDownChoice = new DropDownChoice<ChartDateRange>("dateRange", new PropertyModel<ChartDateRange>(model,dateRangeProperty), getDateRanges(), new EnumDropDownChoiceRenderer<ChartDateRange>());
		final DateTimePicker fromDatePicker = new DateTimePicker("fromDate", new UserToUTCDateModel(new PropertyModel<Date>(model, fromDateProperty)));
		final DateTimePicker toDatePicker = new DateTimePicker("toDate", new UserToUTCDateModel(new EndOfDayDateModel(new PropertyModel<Date>(model, toDateProperty))));
		
        dropDownChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			@Override protected void onUpdate(AjaxRequestTarget target) {
				fromDatePicker.setEnabled(ChartDateRange.CUSTOM.equals(dropDownChoice.getModelObject()));
				toDatePicker.setEnabled(ChartDateRange.CUSTOM.equals(dropDownChoice.getModelObject()));
				target.add(fromDatePicker, toDatePicker);
			}        	
        });
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
	
}

