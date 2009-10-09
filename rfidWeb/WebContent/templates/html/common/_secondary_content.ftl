<div id="secondaryContent">
	<h2><@s.text name="label.new_to_fieldid"/></h2>
	<p class="titleSummary"><@s.text name="label.new_to_fieldid.full"/></p>
	<#if securityGuard.partnerCenterEnabled>
		<#include "../public/_requestAccount.ftl"/>
	<#else>
		<#include "../public/_plansAndPricing.ftl"/>
	</#if>
</div>