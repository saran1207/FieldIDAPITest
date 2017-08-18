package com.n4systems.fieldid;

/**
 * class used to hold constants shared across UI. 
 * so if a wicket page and a struts page need something you can share it here. 
 */
public interface UIConstants {
	public static final String DEFAULT_SUPPORT_URL = "http://help.fieldid.com/";

	/**
	 * Javascript to be inserted in each page's head section to allow walkme content to be automatically added.
	 * ${walkmeURL} is to be replaced by the environment specific version of the walkme url.
	 */
	public static final String BASE_WALKME_SCRIPT = "(function() {var walkme = document.createElement('script'); " +
			"walkme.type = 'text/javascript'; walkme.async = true; walkme.src = " +
			"'${walkmeURL}'; " +
			"var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(walkme, s); " +
			"window._walkmeConfig = {smartLoad:true}; })();";
}
