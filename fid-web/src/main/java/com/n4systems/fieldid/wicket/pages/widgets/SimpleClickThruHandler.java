package com.n4systems.fieldid.wicket.pages.widgets;

import org.apache.wicket.Component;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.n4systems.fieldid.wicket.components.search.results.SRSResultsPanel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.util.chart.FlotOptions;

@SuppressWarnings("serial")
public abstract class SimpleClickThruHandler implements ClickThruHandler {
		
	protected Long id;
	private Component component;

	public SimpleClickThruHandler(Long widgetDefinitionId) {
		this.id = widgetDefinitionId;
	}
		
	public SimpleClickThruHandler(Component component, Long widgetDefinitionId) {
		this(widgetDefinitionId);
		this.component = component;		    	
    }
    
	@Override
	public String getUrl() {		
	    return RequestCycle.get().getUrlRenderer().renderRelativeUrl(
	    		Url.parse(component.getPage().urlFor(getClickThruPage(),getParameters()).toString()));              	
	}
	
	protected Class<? extends FieldIDFrontEndPage> getClickThruPage() { 
		throw new IllegalStateException("You must override & implement getClickThruPage when using standard implementation of " + getClass().getSimpleName() );
	}

	public PageParameters getParameters() {
	    PageParameters parameters = new PageParameters();        
	    parameters.set(SRSResultsPanel.WIDGET_DEFINITION_PARAMETER, id);
	    // other parameters like X,Y, etc... will be set via javascript
	    return parameters;
	}
	
	@Override
	public <X> FlotOptions<X> updateOptions(FlotOptions<X> options) {
		options.fieldIdOptions.clickable = true;
		options.fieldIdOptions.url = getUrl();
		options.grid.clickable = true;				
		return options;		
	}


}
