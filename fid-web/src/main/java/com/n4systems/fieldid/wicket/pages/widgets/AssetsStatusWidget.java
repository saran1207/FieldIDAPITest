package com.n4systems.fieldid.wicket.pages.widgets;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.n4systems.fieldid.wicket.components.dashboard.widgets.FlotOptions;
import com.n4systems.fieldid.wicket.components.dashboard.widgets.HorizBarChartFlotOptions;
import com.n4systems.fieldid.wicket.components.org.OrgPicker;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.services.reporting.DashboardReportingService;
import com.n4systems.util.chart.ChartData;

@SuppressWarnings("serial")
public class AssetsStatusWidget extends ChartWidget<String> {
	
	@SpringBean
	private DashboardReportingService reportingService;
	private BaseOrg owner;
	
    public AssetsStatusWidget(String id, WidgetDefinition widgetDefinition) {
		super(id, new Model<WidgetDefinition>(widgetDefinition));			
        add(new OrgForm("ownerForm"));
        setOutputMarkupId(true);
    }

     @Override
    protected FlotOptions<String> createOptions() {
    	return new HorizBarChartFlotOptions<String>();
    }

	@Override
    protected List<ChartData<String>> getChartData() {
    	return reportingService.getAssetsStatus(owner);
    }

	class OrgForm extends Form {

		public OrgForm(String id) {
			super(id);
			add(new OrgPicker("ownerPicker", new PropertyModel<BaseOrg>(AssetsStatusWidget.this, "owner")) { 
				@Override protected void closePicker(AjaxRequestTarget target) {
					super.closePicker(target);
					target.addComponent(AssetsStatusWidget.this);
				}
				@Override protected void cancelPicker(AjaxRequestTarget target) {
					super.cancelPicker(target);
					target.addComponent(AssetsStatusWidget.this);
				}
			});
		}
		
	}

}



