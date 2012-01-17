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

import com.n4systems.fieldid.wicket.utils.EnumDropDownChoiceRenderer;
import com.n4systems.model.utils.DateRange;
import com.n4systems.util.chart.ChartDateRange;


@SuppressWarnings("serial")
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
		
		setDefaultValuesFromModel();
		
//		fromDatePicker = new DateTimePicker("fromDate", new UserToUTCDateModel(new PropertyModel<Date>(this, "from")));
//		toDatePicker = new DateTimePicker("toDate", new UserToUTCDateModel(new PropertyModel<Date>(this, "to")));
		
		// TODO DD : may need to set time zone here...depends on how things are persisted?
		fromDatePicker = new DateTimePicker("fromDate", new PropertyModel<Date>(this, "from"));
		toDatePicker = new DateTimePicker("toDate", new PropertyModel<Date>(this, "to"));
		
		dropDownChoice.setOutputMarkupId(true);
		dropDownChoice.setNullValid(false);
		
		add(dropDownChoice);    	
		add(fromDatePicker);
		add(toDatePicker);		
	}		
	
	protected List<ChartDateRange> getDateRanges() {
		return Arrays.asList(ChartDateRange.chartDateRangesWithCustom());	
	}
	
	private void setDefaultValuesFromModel() {
		IModel<DateRange> model = new PropertyModel<DateRange>(getDefaultModel(), dateRangeProperty);
		DateRange modelDateRange = model.getObject();
		dateRange = (modelDateRange==null) ? ChartDateRange.FOREVER : ChartDateRange.fromDateRange(modelDateRange);  
		if (dateRange==null) {
			dateRange = ChartDateRange.CUSTOM;
			from = modelDateRange.getFromDate();
			to = modelDateRange.getToDate();
		}
	}

	@Override
	public void updateModel() {
		IModel<DateRange> model = new PropertyModel<DateRange>(getDefaultModel(), dateRangeProperty);
		// TODO DD : may need to set time zone here...depends on how things are persisted?
		model.setObject(createDateRange(from, to, dateRange));
	}
	
	private DateRange createDateRange(Date from, Date to, ChartDateRange dateRange) {
		if (dateRange==null) { 
			return DateRange.FOREVER;
		} else if (ChartDateRange.CUSTOM.equals(dateRange)) { 
			return new DateRange(from,to); 
		} else { 
			return dateRange.asDateRange();
		}
	}	
	
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

