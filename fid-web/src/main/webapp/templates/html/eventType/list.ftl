${action.setPageType('event_type', 'list')!}

<head>
	<@n4.includeStyle href="listFilter" type="page"/>
</head>

<div class="listFilter quickForm" >
	<div id="listFilterHeader">
		<h2><@s.text name="label.filter"/></h2>
		&nbsp;
		<span class="egColor"><@s.text name="message.filter_event_types"/></span>
	</div>
	<@s.form id="listFilterForm" method="get">
		<@s.textfield key="label.name" name="nameFilter" id="nameFilter" labelposition="left" />
		<@s.select key="label.eventtypegroup" name="groupFilter" id="groupFilter" list="eventTypeGroups" listKey="id" listValue="name" headerKey="" headerValue="All" labelposition="left"/>
		<div class="formAction">
			<@s.submit key="hbutton.filter" />
			<span><@s.text name="label.or" /></span>
			<a href="javascript:void(0);" onClick="$('nameFilter').value = '';$('groupFilter').selectedIndex = 0;$('listFilterForm').submit();"> <@s.text name="hbutton.clear"/></a>
		</div>
	</@s.form>
</div>

<#if eventTypes?exists && !eventTypes.isEmpty() >

	<table class="list">
		<tr>
			<th><@s.text name="label.name" /></th>
			<th><@s.text name="label.type"/></th>
			<th><@s.text name="label.eventtypegroup"/></th>
			<th><@s.text name="label.created" /></th>
			<th><@s.text name="label.last_modified" /></th>		
			<th></th>
		<tr>
		
		<#list eventTypes as eventType>
			<tr>
				<td><a href="<@s.url action="eventType" uniqueID="${eventType.id}" />">${eventType.name}</a></td>
				<td>
					<#if eventType.master > ${action.getText('label.master')} <#else> ${action.getText('label.standard')} </#if>
				</td>
				<td><#if eventType.group?exists><a href="<@s.url action="eventTypeGroup" uniqueID="${eventType.group.id}" />">${eventType.group.name!}</a></#if></td>
				<td><#if eventType.createdBy?exists>${eventType.createdBy.fullName!},&nbsp;</#if>${action.formatDateTime(eventType.created)}</td>
				<td><#if eventType.modifiedBy?exists>${eventType.modifiedBy.fullName!},&nbsp;</#if>${action.formatDateTime(eventType.modified)}</td>
				<td>
					<a href="<@s.url action="eventTypeEdit" uniqueID="${eventType.id}" />"><@s.text name="label.edit" /></a>&nbsp;
					<a href="<@s.url action="eventTypeCopy" uniqueID="${eventType.id}" />"><@s.text name="label.copy" /></a>
				</td>
			</tr>
		</#list>
	</table>
<#else>
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
