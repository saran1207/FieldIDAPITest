${action.setPageType('inspection_type', 'list')!}

<#if !eventTypes.isEmpty() >
	<table class="list">
		<tr>
			<th><@s.text name="label.name" /></th>
			<th><@s.text name="label.type"/></th>
			<th><@s.text name="label.group"/></th>
			<th></th>
		<tr>
		
		<#list eventTypes as eventType>
			<tr>
				<td><a href="<@s.url action="eventType" uniqueID="${eventType.id}" />">${eventType.name}</a></td>
				<td>
					<#if eventType.master > ${action.getText('label.master')} <#else> ${action.getText('label.standard')} </#if>
				</td>
				<td>${eventType.group.name}</td>
				<td>
					<a href="<@s.url action="eventTypeEdit" uniqueID="${eventType.id}" />"><@s.text name="label.edit" /></a>&nbsp;
					<a href="<@s.url action="eventTypeCopy" uniqueID="${eventType.id}" />"><@s.text name="label.copy" /></a>
				</td>
			</tr>
		</#list>
	</table>
<#else >
	<div class="emptyList" >
		<h2><@s.text name="label.noresults"/></h2>
		<#if eventTypeGroups.empty>
			<p>
				<@s.text name="label.emptyeventtypelist" />
				<a href="<@s.url action="eventTypeGroupAdd"/>"><@s.text name="label.addthefirsteventtypegroup"/></a>
			</p>
		<#else>
			<p>
				<@s.text name="label.emptyeventtypelist" />
			</p>
		</#if>
		
	</div>
</#if>
