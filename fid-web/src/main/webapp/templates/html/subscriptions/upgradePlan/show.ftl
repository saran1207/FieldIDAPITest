<head>
	<@n4.includeStyle type="page" href="signUp"/>
</head>
${action.setPageType('account_settings', 'upgrade')!}

<div id="upgradeAccount" class="fullForm transactionComplete">
	<h3 class="clean">
		<@s.text name="label.upgraded_from_x_to_y">
			<@s.param>${upgradePackage.name?html}</@s.param>
		</@s.text>
	</h3>
	
	<p>
		<@s.text name="label.upgrade_complete"/>
	</p>

	<#assign charge_label="label.you_have_been_charged"/>
	<#include "../common/_charges.ftl"/>
	
	<div class="actions">
		<a href="<@s.url action="systemSettingsEdit"/>"><@s.text name="label.back_to"/> <@s.text name="label.system_settings"/></a>
	</div>
</div>
