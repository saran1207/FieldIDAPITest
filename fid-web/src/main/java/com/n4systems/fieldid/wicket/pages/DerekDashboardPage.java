package com.n4systems.fieldid.wicket.pages;

import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.JavascriptPackageResource;

import com.n4systems.fieldid.wicket.components.dashboard.widgets.AssetsIdentifiedPanel;

// FIXME DD : temporary page used to develop widget and avoid conflict with other dashboard development. 
//  this *must* be deleted by end of iteration and merged.
public class DerekDashboardPage extends FieldIDFrontEndPage {

    public DerekDashboardPage() {
        add(CSSPackageResource.getHeaderContribution("style/dashboard/dashboard.css"));
        add(JavascriptPackageResource.getHeaderContribution("javascript/flot/jquery.flot.js"));        
        add(JavascriptPackageResource.getHeaderContribution("javascript/flot/jquery.flot.navigate.js"));        
        add(JavascriptPackageResource.getHeaderContribution("javascript/dashboard.js"));
        add(new AssetsIdentifiedPanel("panel"));        
    }

}
