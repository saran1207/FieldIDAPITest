package com.n4systems.fieldid.wicket.components;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IFormModelUpdateListener;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.joda.time.LocalDate;

import com.n4systems.fieldid.wicket.model.EndOfDayDateModel;
import com.n4systems.fieldid.wicket.model.UserToUTCDateModel;
import com.n4systems.fieldid.wicket.utils.EnumDropDownChoiceRenderer;
import com.n4systems.model.utils.DateRange;
import com.n4systems.util.chart.ChartDateRange;


public class DateRangePicker<T> extends Panel implements IFormModelUpdateListener {

	private static final String DATE_RANGE_PROPERTY = "dateRange";
	
	private String dateRangeProperty = DATE_RANGE_PROPERTY;
	private DropDownChoice<ChartDateRange> dropDownChoice;
	private DateTimePicker fromDatePicker;
	private DateTimePicker toDatePicker;
	// local fields used as temporary model.
	private Date from;
	private Date to;
	private ChartDateRange dateRange;

	public DateRangePicker(String id, IModel<T> model) {
		super(id, model);		
		dropDownChoice = new DropDownChoice<ChartDateRange>(dateRangeProperty, 
				new PropertyModel<ChartDateRange>(this,dateRangeProperty), 
				getDateRanges(), 
				new EnumDropDownChoiceRenderer<ChartDateRange>());
		fromDatePicker = new DateTimePicker("fromDate", new UserToUTCDateModel(new PropertyModel<Date>(this, "from")));
		toDatePicker = new DateTimePicker("toDate", new UserToUTCDateModel(new EndOfDayDateModel(new PropertyModel<Date>(this, "to"))));
		
		dropDownChoice.setOutputMarkupId(true);
		dropDownChoice.setNullValid(false);
		
		add(dropDownChoice);    	
		add(fromDatePicker);
		add(toDatePicker);
		
		//setDefaultValuesFromModel();
	}		
	
	protected List<ChartDateRange> getDateRanges() {
		return Arrays.asList(ChartDateRange.chartDateRangesWithCustom());	
	}
	
	private void setDefaultValuesFromModel() {
		IModel<DateRange> model = new PropertyModel(getDefaultModel(), dateRangeProperty);
		DateRange chartDateRange = model.getObject();
		// TBD.  WEB-2612
	}

	@Override
	public void updateModel() {
		IModel<DateRange> model = new PropertyModel(getDefaultModel(), dateRangeProperty);
		model.setObject(createDateRange(from, to, dateRange));
	}
	
	private DateRange createDateRange(Date from, Date to, ChartDateRange dateRange) {
		if (dateRange==null) { 
			return new DateRange();
		}
		if (ChartDateRange.CUSTOM.equals(dateRange)) { 
			return new DateRange(new LocalDate(from),new LocalDate(to)); 
		} else { 
			return dateRange.asDateRange();
		}
	}
	
//	public void setRenderer(IChoiceRenderer<ChartDateRange> renderer) {
//		dropDownChoice.setChoiceRenderer(renderer);
//	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
        response.renderJavaScriptReference("javascript/dateRange.js");		
        String javascript = "dateRangePicker.init('%s');";
        response.renderOnDomReadyJavaScript(String.format(javascript, dropDownChoice.getMarkupId()));
        super.renderHead(response);
	}

	
	public void setFrom(Date from) {
		this.from = from;
	}

	public Date getFrom() {
		return from;
	}

	public void setTo(Date to) {
		this.to = to;
	}

	public Date getTo() {
		return to;
	}

	public void setDateRange(ChartDateRange dateRange) {
		this.dateRange = dateRange;
	}

	public ChartDateRange getDateRange() {
		return dateRange;
	}		
	
}

