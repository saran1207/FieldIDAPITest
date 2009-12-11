<head>
	<@n4.includeStyle type="page" href="signUp"/>
</head>
${action.setPageType('account_settings', 'increase_employee')!}

<div id="upgradeAccount" class="fullForm transactionComplete">
	<h2 class="clean">
		<@s.text name="label.employee_limit_increased_to"><@s.param>${limits.employeeUsersMax}</@s.param></@s.text>
	</h2>
	
	<p>
		<@s.text name="label.employee_limit_increased"/>
	</p>

	<#assign charge_label="label.you_have_been_charged"/>
	<#include "../common/_charges.ftl"/>
	
	<div class="actions">
		<a href="<@s.url action="systemSettingsEdit"/>"><@s.text name="label.back_to"/> <@s.text name="label.system_settings"/></a>
	</div>
</div>
