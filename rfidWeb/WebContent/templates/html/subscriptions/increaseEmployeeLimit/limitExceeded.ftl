${action.setPageType('account_settings', 'increase_employee')!}

<head>
	<@n4.includeStyle type="page" href="signUp"/>
</head>

<div id="planLimitExceeded">
	<p>
		<@s.text name="label.plan_limit"><@s.param>${signUpDetails.users}</@s.param></@s.text>
	</p>
	<p>
		<a href="<@s.url action="upgradePlans"/>" id="upgradePlan"><@s.text name="label.upgrade_your_plan"/></a>
		<@s.text name="label.or"/>
		<a href="<@s.url action="systemSettingsEdit"/>"><@s.text name="label.back_to"/> <@s.text name="label.system_settings"/></a>
	</p>
</div>		