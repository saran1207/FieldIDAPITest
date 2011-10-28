package com.n4systems.fieldid.wicket.components.dashboard.widgets;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.n4systems.fieldid.wicket.components.org.OrgPicker;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.services.reporting.DashboardReportingService;
import com.n4systems.util.chart.ChartData;

//DELETE ME

@SuppressWarnings("serial")
public class AssetsStatusPanel extends ChartablePanel<String> {
	
	@SpringBean
	private DashboardReportingService reportingService;
	private BaseOrg owner;
	
    public AssetsStatusPanel(String id) {
        super(id);
        add(new OrgForm("ownerForm"));
        setOutputMarkupId(true);
    }

     @Override
    protected FlotOptions createOptions() {
    	return new HorizBarChartFlotOptions();
    }

	@Override
    protected List<ChartData<String>> getChartData() {
    	return reportingService.getAssetsStatus(owner);
    }

	class OrgForm extends Form {

		public OrgForm(String id) {
			super(id);
			add(new OrgPicker("ownerPicker", new PropertyModel<BaseOrg>(AssetsStatusPanel.this, "owner")) { 
				@Override protected void closePicker(AjaxRequestTarget target) {
					super.closePicker(target);
					target.addComponent(AssetsStatusPanel.this);
				}
				@Override protected void cancelPicker(AjaxRequestTarget target) {
					super.cancelPicker(target);
					target.addComponent(AssetsStatusPanel.this);
				}
			});
		}
		
	}
	
}



