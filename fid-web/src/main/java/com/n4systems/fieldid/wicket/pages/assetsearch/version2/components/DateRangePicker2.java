package com.n4systems.fieldid.wicket.pages.assetsearch.version2.components;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import com.n4systems.fieldid.wicket.components.DateTimePicker;
import com.n4systems.fieldid.wicket.utils.EnumDropDownChoiceRenderer;
import com.n4systems.model.utils.DateRange;
import com.n4systems.util.chart.RangeType;


@SuppressWarnings("serial")
public class DateRangePicker2 extends Panel {

	private DropDownChoice<RangeType> dropDownChoice;
	private DateTimePicker fromDatePicker;
	private DateTimePicker toDatePicker;
	
	public DateRangePicker2(String id, IModel<DateRange> model) {
		super(id, model);		
		setOutputMarkupId(true);
		dropDownChoice = new DropDownChoice<RangeType>("dateRange",
				new PropertyModel<RangeType>(model, "rangeType"),
				getDateRanges(), 
				new EnumDropDownChoiceRenderer<RangeType>());
		
		final WebMarkupContainer container = new WebMarkupContainer("toFromContainer");
		container.setOutputMarkupId(true);
		
		dropDownChoice.setOutputMarkupId(true);
		dropDownChoice.setNullValid(false);
		dropDownChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") { 			
			@Override protected void onUpdate(AjaxRequestTarget target) {
				boolean isCustom = RangeType.CUSTOM.equals(dropDownChoice.getModel().getObject());
				container.setVisible(isCustom);
				target.add(DateRangePicker2.this);
			}
		});
		
		fromDatePicker = new DateTimePicker("fromDate", new PropertyModel<Date>(model, "fromDate"));
		toDatePicker = new DateTimePicker("toDate", new PropertyModel<Date>(model, "toDate"));
		container.add(fromDatePicker);
		container.add(toDatePicker);
		
		add(dropDownChoice);    	
		add(container);
	}		
	
	protected List<RangeType> getDateRanges() {
		return Arrays.asList(RangeType.rangeTypesWithCustom());
	}
	
}

