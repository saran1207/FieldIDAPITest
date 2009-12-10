<#assign foundPreferred=false>
<#assign preferredPackage=false>
<#macro price package>
	${currentPackageFilter.getUpgradeContractForPackage(package).pricePerUserPerMonth?string.currency}
</#macro>

<#macro columnClass package>
	
	<#if !package.current && (!foundPreferred || perferredPackage=package)>
		<#assign foundPreferred=true/>
		<#assign perferredPackage=package/>
		class="preferred"
	</#if>
</#macro>

<head>
	<@n4.includeStyle type="page" href="signUp"/>
</head>
${action.setPageType('account_settings', 'upgrade')!}


<#assign currentPackageFilter=action.currentPackageFilter()/>
<#include "/templates/html/common/_formErrors.ftl"/>
<#assign purchaseAction="upgradePlan"/>
<#assign purchaseLabel="label.upgrade"/>
<#include "../signUpPackages/_packagesListing.ftl"/>

<div>
	<@s.text name="label.back_to"/> <a href="<@s.url action="systemSettingsEdit"/>"><@s.text name="label.system_settings"/></a>
</div>