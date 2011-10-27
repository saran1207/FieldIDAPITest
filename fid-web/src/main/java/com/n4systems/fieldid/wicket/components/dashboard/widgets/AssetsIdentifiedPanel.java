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
import com.n4systems.util.chart.ChartDataGranularity;

@SuppressWarnings("serial")
public class AssetsIdentifiedPanel extends ChartablePanel<Calendar> {
	
	@SpringBean
	private DashboardReportingService reportingService;
	private ChartDataGranularity granularity = ChartDataGranularity.ALL;
	private BaseOrg owner;
	
    public AssetsIdentifiedPanel(String id) {
        super(id);
        addGranularityButton("year", ChartDataGranularity.YEAR);
        addGranularityButton("quarter", ChartDataGranularity.QUARTER);
        addGranularityButton("month", ChartDataGranularity.MONTH);
        addGranularityButton("week", ChartDataGranularity.WEEK);
        add(new OrgForm("ownerForm"));
        setOutputMarkupId(true);
    }

    @SuppressWarnings("rawtypes")
	private void addGranularityButton(String id, final ChartDataGranularity period) {
        add(new IndicatingAjaxLink(id) {
			@Override public void onClick(AjaxRequestTarget target) {
				setGranularity(period);
				target.addComponent(AssetsIdentifiedPanel.this);
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

	class OrgForm extends Form {

		public OrgForm(String id) {
			super(id);
			add(new OrgPicker("ownerPicker", new PropertyModel<BaseOrg>(AssetsIdentifiedPanel.this, "owner")) { 
				@Override protected void closePicker(AjaxRequestTarget target) {
					super.closePicker(target);
					target.addComponent(AssetsIdentifiedPanel.this);
				}
				@Override protected void cancelPicker(AjaxRequestTarget target) {
					super.cancelPicker(target);
					target.addComponent(AssetsIdentifiedPanel.this);
				}
			});
		}
		
	}
	
}



