package com.n4systems.fieldid.wicket.pages.widgets;

import java.util.Calendar;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.services.reporting.DashboardReportingService;
import com.n4systems.util.chart.ChartData;
import com.n4systems.util.chart.ChartDataGranularity;

@SuppressWarnings("serial")
public class AssetsIdentifiedWidget extends ChartWidget<Calendar> {
	
	@SpringBean
	private DashboardReportingService reportingService;
	private ChartDataGranularity granularity = ChartDataGranularity.ALL;
	private ConfigForm configForm;
	
    public AssetsIdentifiedWidget(String id, WidgetDefinition widgetDefinition) {
		super(id, new Model<WidgetDefinition>(widgetDefinition));			
		setOutputMarkupId(true);		
        addGranularityButton("year", ChartDataGranularity.YEAR);
        addGranularityButton("quarter", ChartDataGranularity.QUARTER);
        addGranularityButton("month", ChartDataGranularity.MONTH);
        addGranularityButton("week", ChartDataGranularity.WEEK);
        add(new OrgForm("ownerForm"));
    }

	@SuppressWarnings("rawtypes")
	private void addGranularityButton(String id, final ChartDataGranularity period) {
        add(new IndicatingAjaxLink(id) {
			@Override public void onClick(AjaxRequestTarget target) {
				setGranularity(period);
				target.addComponent(AssetsIdentifiedWidget.this);
			}        	
        });         
	}
    
    private void setGranularity(ChartDataGranularity period) {
    	this.granularity = period;
    }        	
    
	@Override
    protected List<ChartData<Calendar>> getChartData() {
    	return reportingService.getAssetsIdentified(granularity, owner);
    }

	
	class ConfigForm extends Form {

		public ConfigForm(String id) {
			super(id);			
			add(new AjaxButton("configButton") {
				@Override protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
//					configForm.setVisible(false);
//					flotChart.setVisible(true);	
					target.addComponent(AssetsIdentifiedWidget.this);
				}				
			});
		}
	}
	
	
}



