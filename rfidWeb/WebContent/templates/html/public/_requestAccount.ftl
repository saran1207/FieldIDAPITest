<#if securityGuard.partnerCenterEnabled>
	<h2><@s.text name="label.new_to_fieldid"/></h2>
	<p class="titleSummary"><@s.text name="label.new_to_fieldid.full"/></p>
	
	<div class="actionButton">
		<div id="requestAccountButton" class="imageButton"><a href="<@s.url action="registerUser"/>"><span><@s.text name="label.requestanaccount"/></span></a></div>
	</div>
<#else>
	<#include "_plansAndPricing.ftl"/>
</#if>