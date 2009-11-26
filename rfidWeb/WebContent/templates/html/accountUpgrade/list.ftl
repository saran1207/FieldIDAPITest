<#macro columnClass package>
	<#if package.current>class="currentPackage"<#elseif package.preferred>class="preferred"</#if>
</#macro>
<title>Upgrade Account</title>
<head>
	<@n4.includeStyle type="page" href="signUp"/>
</head>
${action.setPageType('account_settings', 'show')!}
<#assign currentPackageFilter=action.currentPackageFilter()/>

<#include "/templates/html/common/_formErrors.ftl"/>
<#assign purchaseAction="accountUpgrade"/>
<#assign purchaseLabel="label.upgrade"/>
<#include "../signUpPackages/_packagesListing.ftl"/>

<div>
	<@s.text name="label.back_to"/> <a href="<@s.url action="systemSettingsEdit"/>"><@s.text name="label.system_settings"/></a>
</div>