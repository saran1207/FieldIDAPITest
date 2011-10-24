package com.n4systems.fieldid.wicket.components.dashboard.widgets;

import java.util.Calendar;
import java.util.List;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.n4systems.services.reporting.ReportingService;
import com.n4systems.util.chart.ChartData;
import com.n4systems.util.chart.ChartDataGranularity;

@SuppressWarnings("serial")
public class AssetsIdentifiedPanel extends ChartablePanel<Calendar> {
	
	@SpringBean
	private ReportingService reportingService;
	private ChartDataGranularity period = ChartDataGranularity.ALL;
	
    public AssetsIdentifiedPanel(String id) {
        super(id);
        addPeriodButton("year", ChartDataGranularity.YEAR);
        addPeriodButton("quarter", ChartDataGranularity.QUARTER);
        addPeriodButton("month", ChartDataGranularity.MONTH);
        addPeriodButton("week", ChartDataGranularity.WEEK);
        setOutputMarkupId(true);
    }

    private void addPeriodButton(String id, final ChartDataGranularity period) {
        add(new Button(id).add(new AjaxEventBehavior("onclick") {
			@Override protected void onEvent(AjaxRequestTarget target) {
				setPeriod(period);
				target.addComponent(AssetsIdentifiedPanel.this);
			}
        }));
	}
    
    private void setPeriod(ChartDataGranularity period) {
    	this.period = period;
    }        	

	@Override
    protected List<ChartData<Calendar>> getChartData() {
    	return reportingService.getAssetsIdentified(period, null/*org filter not done yet*/);
    }


}



