package com.n4systems.fieldid.wicket.pages.widgets;

import java.util.Calendar;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.n4systems.fieldid.wicket.components.org.OrgPicker;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.services.reporting.DashboardReportingService;
import com.n4systems.util.chart.ChartData;
import com.n4systems.util.chart.ChartDataGranularity;

@SuppressWarnings("serial")
public class AssetsIdentifiedWidget extends ChartWidget<Calendar> {
	
	@SpringBean
	private DashboardReportingService reportingService;
	private ChartDataGranularity granularity = ChartDataGranularity.ALL;
	private BaseOrg owner;
	private ConfigForm configForm;
	
    public AssetsIdentifiedWidget(String id, WidgetDefinition widgetDefinition) {
		super(id, new Model<WidgetDefinition>(widgetDefinition));			
		setOutputMarkupId(true);		
        addGranularityButton("year", ChartDataGranularity.YEAR);
        addGranularityButton("quarter", ChartDataGranularity.QUARTER);
        addGranularityButton("month", ChartDataGranularity.MONTH);
        addGranularityButton("week", ChartDataGranularity.WEEK);
        add(new OrgForm("ownerForm"));
        
        addConfiguration();
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

    private void addConfiguration() {
    	add(configForm = new ConfigForm("configForm"));
    	configForm.setVisible(false);
    	configureButton.add(new SimpleAttributeModifier("onclick","$('#"+flotChart.getMarkupId()+"').toggle();"));
//    					new AjaxEventBehavior("onclick") {
//			@Override protected void onEvent(AjaxRequestTarget target) {
//				configForm.setVisible(true);
//				flotChart.setVisible(false);
//				target.addComponent(AssetsIdentifiedWidget.this);
//			}     		
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
	
	class OrgForm extends Form {

		public OrgForm(String id) {
			super(id);
			add(new OrgPicker("ownerPicker", new PropertyModel<BaseOrg>(AssetsIdentifiedWidget.this, "owner")) { 
				@Override protected void closePicker(AjaxRequestTarget target) {
					super.closePicker(target);
					target.addComponent(AssetsIdentifiedWidget.this);
				}
				@Override protected void cancelPicker(AjaxRequestTarget target) {
					super.cancelPicker(target);
					target.addComponent(AssetsIdentifiedWidget.this);
				}
			});
		}
		
	}
	
}



