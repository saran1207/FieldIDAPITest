<#if securityGuard.partnerCenterEnabled>
	
	<div class="columnSeperator">
		<@s.text name="label.or"/>
	</div>
	<p class="titleSummary"><@s.text name="label.get_a_user_account_with.full"><@s.param>${tenant.name}</@s.param></@s.text></p>
	
	<div class="actionButton">
		<div id="requestAccountButton" class="imageButton"><a href="<@s.url action="registerUser"/>"><span><@s.text name="label.requestanaccount"/></span></a></div>
	</div>
</#if>

