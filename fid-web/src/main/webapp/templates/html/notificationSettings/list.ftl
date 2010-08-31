${action.setPageType('my_account', 'notification_settings')!}
<#assign secondaryNavAction="list"/>
<#include "_secondaryNav.ftl"/>

<#if !settingsList.isEmpty() >
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
	<div class="emptyList" >
		<h2><@s.text name="label.noresults" /></h2>
		<p><@s.text name="label.emptynotificationsettinglist" /></p>
		<a href="<@s.url value="notificationSettingAdd.action"/>" ><@s.text name="label.addnotificationsetting" /></a>
	</div>
</#if>


<script type="text/javascript">

</script>