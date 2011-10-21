package com.n4systems.fieldid.wicket.components.dashboard.widgets;

import java.util.Date;

import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.javascript.JsStatement;
import org.odlabs.wiquery.core.options.Options;

import com.n4systems.services.reporting.ReportingService;
import com.n4systems.util.chart.ChartData;

@SuppressWarnings("serial")
public class AssetsIdentifiedPanel extends Panel {
	
	@SpringBean
	private ReportingService reportingService;
	
    public AssetsIdentifiedPanel(String id) {
        super(id);
        add(new Flot("graph"));		
    }


    class Flot extends WebMarkupContainer {
    	
    	private JsStatement statement = new JsStatement();
    	private Options options;
    	
    	public Flot(final String id) {
    		super(id);
    		this.options = new Options();
            setOutputMarkupId(true).setMarkupId(getId());
			final ChartData data = reportingService.getAssetsIdentified(new Date(), null);
			add(new AbstractBehavior () {
				@Override
				public void renderHead(IHeaderResponse response) {
					StringBuffer javascriptBuffer = new StringBuffer();
					javascriptBuffer.append ("dashboardWidgetFactory.createWithData('"+id + "'," + data.toJavascriptString()+");");
					response.renderOnLoadJavascript(javascriptBuffer.toString());
				}
			});
    	}    	
    	    	
    }


}



