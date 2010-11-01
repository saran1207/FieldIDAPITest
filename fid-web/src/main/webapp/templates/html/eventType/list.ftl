${action.setPageType('inspection_type', 'list')!}

<#if !inspectionTypes.isEmpty() >
	<table class="list">
		<tr>
			<th><@s.text name="label.name" /></th>
			<th><@s.text name="label.type"/></th>
			<th><@s.text name="label.group"/></th>
			<th></th>
		<tr>
		
		<#list inspectionTypes as inspectionType>
			<tr>
				<td><a href="<@s.url action="eventType" uniqueID="${inspectionType.id}" />">${inspectionType.name}</a></td>
				<td>
					<#if inspectionType.master > ${action.getText('label.master')} <#else> ${action.getText('label.standard')} </#if>
				</td>
				<td>${inspectionType.group.name}</td>
				<td>
					<a href="<@s.url action="eventTypeEdit" uniqueID="${inspectionType.id}" />"><@s.text name="label.edit" /></a>&nbsp;
					<a href="<@s.url action="eventTypeCopy" uniqueID="${inspectionType.id}" />"><@s.text name="label.copy" /></a>
				</td>
			</tr>
		</#list>
	</table>
<#else >
	<div class="emptyList" >
		<h2><@s.text name="label.noresults"/></h2>
		<#if inspectionTypeGroups.empty>
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
