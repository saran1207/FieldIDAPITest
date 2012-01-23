package com.n4systems.fieldid.wicket.pages.assetsearch.version2;

import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.odlabs.wiquery.core.events.Event;
import org.odlabs.wiquery.core.events.MouseEvent;
import org.odlabs.wiquery.core.events.WiQueryEventBehavior;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.ui.core.JsScopeUiEvent;

@SuppressWarnings("serial")
public class CollapsiblePanel extends Panel {

    private static final String CONTAINED_PANEL_MARKUP_ID = "containedPanel";

    private WebMarkupContainer collapseExpandLink;
    private WebMarkupContainer containedPanel;

    private ContextImage collapseImage;
    private ContextImage expandImage;

    public CollapsiblePanel(String id, final IModel<String> titleModel, String expandImageUrl, String collapseImageUrl) {
        super(id);
        setOutputMarkupId(true);
        add(collapseExpandLink = new WebMarkupContainer("collapseExpandLink"));
        collapseExpandLink.add(new Label("collapseExpandLinkLabel", titleModel));
        collapseExpandLink.add(expandImage = new ContextImage("expandImage", expandImageUrl));
        collapseExpandLink.add(collapseImage = new ContextImage("collapseImage", collapseImageUrl));
        expandImage.setOutputMarkupPlaceholderTag(true);
        collapseImage.setOutputMarkupPlaceholderTag(true);
    }

    public void addContainedPanel(WebMarkupContainer containedPanel) {
		addContainedPanel(containedPanel,true);
    }

	public void addContainedPanel(WebMarkupContainer containedPanel, boolean visible) {
        this.containedPanel = containedPanel;
        containedPanel.setOutputMarkupId(true);
        collapseExpandLink.add(createCollapseBehavior(containedPanel.getMarkupId()));
        //------
        add(containedPanel);
    }

    private Behavior createCollapseBehavior(final String id) {
		return new WiQueryEventBehavior(new Event(MouseEvent.CLICK) {
			@Override public JsScope callback() {
				return JsScopeUiEvent.quickScope("$('#"+id+"').slideToggle(140);" +
						"$('#"+expandImage.getMarkupId()+"').toggle();" + 
						"$('#"+collapseImage.getMarkupId()+"').toggle();");
			}
		});
	}

	
    public String getContainedPanelMarkupId() {
        return CONTAINED_PANEL_MARKUP_ID;
    }
    
	@Override	
    public void renderHead(IHeaderResponse response) {
    	response.renderCSSReference("style/component/collapsiblePanel.css");    	
    	super.renderHead(response);
    }
    

}
