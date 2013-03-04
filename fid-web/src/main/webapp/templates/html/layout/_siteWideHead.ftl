<!-- CAVEAT : don't use "Edge" (as opposed to say "Emulate IE 9"), because it's just for testing. you probably want this to match the latest version of IE you support -->
<!-- this is put in basically to get the Lightview.js component to work on struts pages.  without it, it won't work in IE --> 
<!-- see WEB-2743 for details.  -->
<meta http-equiv="X-UA-Compatible" content="IE=${action.getIEHeader()}" />
<title><@s.text name="app.title" />: <#include "_title.ftl"/></title>
<@n4.includeStyle href="reset" /> 
<#include "layoutCss.ftl">
<!--[if IE 6]>
<@n4.includeStyle href="fieldid-ie6" />
<![endif]-->
<!--[if IE 7]>
<@n4.includeStyle href="fieldid-ie7"/>
<![endif]-->
<@n4.includeStyle href="site_wide"/>
<@n4.includeStyle href="branding/default"/>
<link rel="shortcut icon" href="<@s.url value="/images/favicon.ico" />" type="image/x-icon" />		
<@n4.includeScript src="${action.getProtoypeVersion()}"/>
<@n4.includeScript src="json2" />
<@n4.includeScript src="common" />
<!--[if IE 6]>
<@n4.includeScript src="common-ie6" />
<![endif]-->

<@n4.includeScript src="jquery-1.7.2.js"/>
<@n4.includeScript src="jquery-ui-1.8.13.custom.min.js"/>
<@n4.includeScript>jQuery.noConflict();</@n4.includeScript>
<@n4.includeStyle href="jquery-redmond/jquery-ui-1.8.13.custom"/>
<@n4.includeScript src='jquery.colorbox.js'/>
<@n4.includeStyle href='colorbox'/>

<@n4.includeStyle href="newCss/component/my_saved_items.css" />
<@n4.includeScript src="my_saved_items.js" />

<#include "apptegic.ftl">