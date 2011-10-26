package com.n4systems.fieldid.wicket.util;

import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.behavior.IBehavior;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.protocol.http.WebRequestCycle;
import org.apache.wicket.protocol.http.request.WebClientInfo;

@SuppressWarnings("serial")
public class JavascriptPackageResourceIE {

	public static IBehavior getHeaderContribution(String url) {
       	WebClientInfo clientInfo = (WebClientInfo)WebRequestCycle.get().getClientInfo();
       	if (clientInfo.getProperties().isBrowserInternetExplorer()) {
       		return JavascriptPackageResource.getHeaderContribution(url);
       	} else {
       		return new AbstractBehavior() { /*do nothing for non-IE browsers */};
       	}
	}

}
