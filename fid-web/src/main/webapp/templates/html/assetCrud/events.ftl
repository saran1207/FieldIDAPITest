<div class="headerActions">
	<a id="startEvent" href="#" onclick="return redirect('<@s.url action="quickEvent" assetId="${uniqueID}" includeParams="none" />');"><@s.text name="label.start_event"/></a>
	<a id="manageEvent" href="#" onclick="return redirect('<@s.url action="eventGroups" uniqueID="${uniqueID}"/>');" ><@s.text name="label.view_events_by_date_group"/></a>
</div>

<#if !events.isEmpty() >
	<table class="list">
		<tr>
			<th><@s.text name="label.date_performed"/></th>
			<th><@s.text name="label.eventtype"/></th>
			<th><@s.text name="label.performed_by"/></th>
			<th><@s.text name="label.result"/></th>
			<th><@s.text name="label.assetstatus"/></th>
			<th>&nbsp;</th>
		</tr>
		<#list events as event >
			<tr>
				<td>${action.formatDateTime(event.date)}</td>
				<td>${event.type.name}</td>
				<td>
					<#assign user=event.performedBy />
					<#include "../eventCrud/_userName.ftl"/>
				</td>
				<td><@s.text name="${(event.status.label?html)!}"/></td>
				<td><@s.text name="${(event.assetStatus.name?html)!}"/></td>
				<td>
					<#if useContext>
						<#assign additionsToQueryString="&useContext=true"/>
					</#if>
					<#include "../eventCrud/_viewEventLink.ftl"/> 
					<#if sessionUser.hasAccess("editevent")>
						|
						<a href="#" onclick="return redirect('<@s.url action="selectEventEdit" uniqueID="${event.id}" includeParams="none" />');" ><@s.text name="label.edit"/></a>
					</#if>
				</td>
			</tr>
		</#list>
	</table>
<#else>
	<div class="emptyList">
		<h2><@s.text name="label.noresults"/></h2>
		<p>
			<@s.text name="label.emptyeventlist" />
		</p>
	</div>
</#if>

<#if !inVendorContext >
	<div class="formAction">
		<button onclick="window.location = '<@s.url action="eventGroups" uniqueID="${uniqueID}"/>'; return false;" ><@s.text name="label.manageevents"/> </button>
	</div>
</#if>