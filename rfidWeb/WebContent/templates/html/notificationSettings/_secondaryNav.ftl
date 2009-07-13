<ul class="secondaryNav" >
	<#if !secondaryNavAction?exists || secondaryNavAction != "list">
		<li><a href="<@s.url action="notificationSettings"/>">&#171; <@s.text name="label.view_all_notifications"/></a></li>
	<#else>
		<li class="add button"><a href="<@s.url value="notificationSettingAdd.action"/>" ><@s.text name="label.addnotificationsetting" /></a></li>
	</#if>
</ul>
