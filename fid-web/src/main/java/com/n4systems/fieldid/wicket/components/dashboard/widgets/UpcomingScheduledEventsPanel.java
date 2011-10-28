package com.n4systems.fieldid.wicket.components.dashboard.widgets;

import java.util.Calendar;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.n4systems.fieldid.wicket.components.org.OrgPicker;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.services.reporting.DashboardReportingService;
import com.n4systems.util.chart.ChartData;

//DELETE ME
public class UpcomingScheduledEventsPanel extends ChartablePanel<Calendar> {

	@SpringBean
	private DashboardReportingService reportingService;
	private Integer period = 30;
	private BaseOrg owner;

	public UpcomingScheduledEventsPanel(String id) {
		super(id);
		addPeriodButton("7days", 7);
		addPeriodButton("30days", 30);
		addPeriodButton("60days", 60);
		addPeriodButton("90days", 90);
		add(new OrgForm("ownerForm"));
		setOutputMarkupId(true);
	}

	private void addPeriodButton(String id, final int period) {
        add(new IndicatingAjaxLink(id) {
			@Override public void onClick(AjaxRequestTarget target) {
				setPeriod(period);
				target.addComponent(UpcomingScheduledEventsPanel.this);
			}        	
        });      		
	}

	@Override
	protected List<ChartData<Calendar>> getChartData() {
		return reportingService.getUpcomingScheduledEvents(period, owner);
	}
	
	public void setPeriod(Integer period) {
		this.period = period;
	}
	
	class OrgForm extends Form {

		public OrgForm(String id) {
			super(id);
			add(new OrgPicker("ownerPicker", new PropertyModel<BaseOrg>(UpcomingScheduledEventsPanel.this, "owner")) { 
				@Override 
				protected void closePicker(AjaxRequestTarget target) {
					super.closePicker(target);
					target.addComponent(UpcomingScheduledEventsPanel.this);
				}
				@Override 
				protected void cancelPicker(AjaxRequestTarget target) {
					super.cancelPicker(target);
					target.addComponent(UpcomingScheduledEventsPanel.this);
				}
			});
		}
		
	}

}
