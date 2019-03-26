package com.n4systems.fieldid;

/**
 * class used to hold constants shared across UI. 
 * so if a wicket page and a struts page need something you can share it here. 
 */
public interface UIConstants {
	public static final String DEFAULT_SUPPORT_URL = "https://support.ecompliance.com/hc/en-us/articles/115015728427";

	/**
	 * Javascript to be inserted in each page's head section to allow walkme content to be automatically added.
	 * ${walkmeURL} is to be replaced by the environment specific version of the walkme url.
	 */
	public static final String BASE_WALKME_SCRIPT = "(function() {var walkme = document.createElement('script'); " +
			"walkme.type = 'text/javascript'; walkme.async = true; walkme.src = " +
			"'${walkmeURL}'; " +
			"var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(walkme, s); " +
			"window._walkmeConfig = {smartLoad:true}; })();";

	public static final String SLAASK_JS_URL = "https://cdn.slaask.com/chat.js";
	public static final String SLAASK_JS_SCRIPT =
			"_slaask.identify('${UserName}', {\n" +
			"email: '${User_Email}',\n" +
			"plan: 'enterprise'\n" +
			"});\n" +
			"_slaask.init('368e32312430d2d2b55f5177bb368f2f');";


	/**
	 * Javascript to be inserted in each page's head section to allow walkme content to be automatically added.
	 * ${walkmeURL} is to be replaced by the environment specific version of the walkme url.
	 * <script type="text/javascript">
	 (function(){
	 var useriq=window._uiq=window._uiq||[];useriq.invoked&&window.console&&console.error&&console.error("Useriq snippet already exists."),useriq.invoked=!0,useriq.methods=["setSiteId","startTracker","setDoNotTrack","identify","track","group"],useriq.factory=function(e){return function(){var r=Array.prototype.slice.call(arguments);return r.unshift(e),useriq.push(r),useriq}};for(var i=0;i<useriq.methods.length;i++){var key=useriq.methods[i];useriq[key]=useriq.factory(key)}
	 // We have dynamically assigned your useriq_site_id
	 var useriq_site_id = "702097301"
	 //All green highlights indicate the areas in the UserIQ script that should contain your own variables
	 // user id is required
	 var user_id = "INSERT_YOUR_APP_USER_ID_HERE"
	 // account id is required for account analytics
	 var account_id = "INSERT_YOUR_APP_ACCOUNT_ID_HERE"
	 useriq.setSiteId(useriq_site_id)
	 useriq.identify(user_id, {
	 user_name: 'INSERT_USER_NAME_HERE',
	 account_id: account_id,
	 account_name: 'INSERT_ACCOUNT_NAME_HERE',
	 user_email: 'INSERT_USER_EMAIL_HERE',
	 signup_date: 'INSERT_USER_SIGNUP_DATE_HERE_YYYY-MM-DD',
	 })
	 useriq.startTracker()
	 var d=document, g=d.createElement("script"), s=d.getElementsByTagName("script")[0]; g.type="text/javascript";
	 g.defer=true; g.async=true; g.src="https://feed.useriq.com/useriq.js"; s.parentNode.insertBefore(g,s);
	 })();
	 </script>
	 *
	 *
	 */
	public static final String BASE_USEIQ_SCRIPT = "(function() {\n" +
			"var useriq=window._uiq=window._uiq||[];useriq.invoked&&window.console&&console.error&&console.error(\"Useriq snippet already exists.\"),useriq.invoked=!0,useriq.methods=[\"setSiteId\",\"startTracker\",\"setDoNotTrack\",\"identify\",\"track\",\"group\"],useriq.factory=function(e){return function(){var r=Array.prototype.slice.call(arguments);return r.unshift(e),useriq.push(r),useriq}};for(var i=0;i<useriq.methods.length;i++){var key=useriq.methods[i];useriq[key]=useriq.factory(key)} \n" +
			"var useriq_site_id = '%userIQSiteId%'; \n" +
			"var user_id = '%userId%'; \n" +
			"var account_id = '%salesforceId%'; \n" +
			"useriq.setSiteId(useriq_site_id); \n" +
			"useriq.identify(user_id, {\n" +
			"\t user_name: '%userName%',\n" +
			"\t account_id: account_id,\n" +
			"\t account_name: '%tenantName%',\n" +
			"\t user_email: '%userEmail%',\n" +
			"\t signup_date: '%userCreatedDate%',\n" +
			"\t }); " +
			"useriq.startTracker(); \n" +
			"var d=document, g=d.createElement(\"script\"), s=d.getElementsByTagName(\"script\")[0]; g.type=\"text/javascript\"; \n" +
			"g.defer=true; g.async=true; g.src=\"https://feed.useriq.com/useriq.js\"; s.parentNode.insertBefore(g,s); }) (); ";
}
