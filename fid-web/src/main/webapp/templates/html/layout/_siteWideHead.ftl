<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7"/>
<title><@s.text name="app.title" /> - <#include "_title.ftl"/></title>
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
<@n4.includeScript src="prototype"/>
<@n4.includeScript src="scriptaculous" />
<@n4.includeScript src="json2" />
<@n4.includeScript src="common" />
<!--[if IE 6]>
<@n4.includeScript src="common-ie6" />
<![endif]-->

<@n4.includeScript src="jquery-1.4.2.min.js"/>
<@n4.includeScript>jQuery.noConflict();</@n4.includeScript>

<@n4.includeStyle href="newCss/component/my_saved_items.css" />
<@n4.includeScript src="my_saved_items.js" />
