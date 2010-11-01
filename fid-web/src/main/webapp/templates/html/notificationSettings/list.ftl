${action.setPageType('my_account', 'notification_settings')!}

<head>
	<@n4.includeStyle href="notifications" type="page"/>
</head>

<#if !settingsList.isEmpty() >
	<#assign secondaryNavAction="list"/>
	<#include "_secondaryNav.ftl"/>
	<table class="list">
		<tr>
			<th><@s.text name="label.name" /></th>
			<th><@s.text name="label.sent"/></th>
			<th><@s.text name="label.events_starting"/></th>
			<th><@s.text name="label.for_the_next"/></th>
			<th></th>
		<tr>
		
		<#list settingsList as notificationSetting>
			<tr>
				<td>${notificationSetting.name?html}</td>
				<td><@s.text name="${notificationSetting.frequency.label}"/></td>
				<td><@s.text name="${notificationSetting.periodStart.label}"/></td>
				<td><@s.text name="${notificationSetting.periodEnd.label}"/></td>
				<td>
					<a href="<@s.url action="notificationSettingEdit" namespace="/" uniqueID="${notificationSetting.id}" />"><@s.text name="label.edit" /></a>
					&nbsp;
					<a href="<@s.url action="notificationSettingDelete" namespace="/" uniqueID="${notificationSetting.id}" />"><@s.text name="label.delete" /></a>
				</td>
			</tr>
		</#list>
	</table>
<#else>
	<@s.url id="addNotificationSettingUrl" action="notificationSettingAdd"/>
	
	<div class="initialMessage">
		<div class="textContainer" >
			<h1><@s.text name="label.create_email_notification"/></h1>
			<p><@s.text name="label.create_email_notification_message" /></p>
		</div>
			<input type="submit" value="<@s.text name="label.create_email_notification_now"/>"onclick="return redirect('${addNotificationSettingUrl}');"/>
	</div>
</#if>


<script type="text/javascript">

</script>