<#if !events.isEmpty() >
	<table class="list">
		<tr>
			<th><@s.text name="label.date_performed"/></th>
			<th><@s.text name="label.eventtype"/></th>
			<th><@s.text name="label.result"/></th>
			<th><@s.text name="label.performed_by"/></th>
			<th><@s.text name="label.details"/></th>
		</tr>
		<#list events as event >
			<tr>
				<td>${action.formatDateTime(event.date)}</td>
				<td>${event.type.name}</td>
				<td><@s.text name="${(event.status.label?html)!}"/></td>
				<td>
					<#assign user=event.performedBy />
					<#include "../eventCrud/_userName.ftl"/>
				</td>
				<td>
					<#if useContext>
						<#assign additionsToQueryString="&useContext=true"/>
					</#if>
					<#include "../eventCrud/_viewEventLink.ftl"/>
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