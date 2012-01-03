package com.n4systems.fieldid.wicket.util;

import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.protocol.http.request.WebClientInfo;

@SuppressWarnings("serial")
public class JavascriptPackageResourceIE {

	public static void renderJavaScriptReference(IHeaderResponse response, String url) {
       	WebClientInfo clientInfo = WebSession.get().getClientInfo();
       	if (clientInfo.getProperties().isBrowserInternetExplorer()) {
            response.renderJavaScriptReference(url);
       	}
	}

}
