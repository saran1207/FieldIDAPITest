<div id="secondaryContent">
	<h2><@s.text name="label.new_to_fieldid"/></h2>
	<p class="titleSummary"><@s.text name="label.new_to_fieldid.full"/></p>
	<#if userLimitService.readOnlyUsersEnabled>
		<#include "../public/_requestAccount.ftl"/>
	</#if>
	
	<#if securityGuard.plansAndPricingAvailable>
		<#include "../public/_plansAndPricing.ftl"/>
	</#if>
	
</div>