${action.setPageType('product', 'inspections')!}

<#if !inspections.isEmpty() >
	<table class="list">
		<tr>
			<th><@s.text name="label.inspectiondate"/></th>
			<th><@s.text name="label.inspectiontype"/></th>
			<th><@s.text name="label.result"/></th>
			<th><@s.text name="label.inspectedby"/></th>
			<th><@s.text name="label.details"/></th>
		</tr>
		<#list inspections as inspection >
			<tr>
				<td>${action.formatDateTime(inspection.date)}</td>
				<td>${inspection.type.name}</td>
				<td>${inspection.status!}</td>
				<td>
					<#assign user=inspection.inspector />
					<#include "../inspectionCrud/_userName.ftl"/>
				</td>
				<td>
					<#include "../inspectionCrud/_viewInspectionLink.ftl"/>
				</td>
			</tr>
		</#list>
	</table>
<#else>
	<div class="emptyList">
		<h2><@s.text name="label.noresults"/></h2>
		<p>
			<@s.text name="label.emptyinspectionlist" />
		</p>
	</div>
</#if>

<div class="formAction">
	<button onclick="window.location = '<@s.url action="inspectionGroups" uniqueID="${uniqueID}"/>'; return false;" ><@s.text name="label.manageinspections"/> </button>
</div>