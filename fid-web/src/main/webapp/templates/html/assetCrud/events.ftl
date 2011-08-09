<head>
	<#include "/templates/html/common/_tooltip.ftl"/>
	<style>
		.textContainer {
			background-image: url(images/events-blank-slate.png);
		}
		.list .reducedWidth {
			min-width: 10px;
		}
	</style> 
</head>


<div class="headerActions">
	<#if sessionUser.hasAccess("createevent")>
		<a id="startEvent" href="#" onclick="return redirect('<@s.url action="quickEvent" assetId="${uniqueID}" includeParams="none" />');"><@s.text name="label.start_event"/></a>
	</#if>
	<a id="manageEvent" href="#" onclick="return redirect('<@s.url action="eventGroups" uniqueID="${uniqueID}"/>');" ><@s.text name="label.view_events_by_date_group"/></a>
</div>

<#if !pagedEvents.list.isEmpty() >
	
	<#assign page = pagedEvents/>
	
	<div id="tooltip" class="tooltip" style="display:none">
		<@s.text name="label.gps_location_recorded"/>
	</div>
	
	<#include '../common/_pagination.ftl' />
	<table id="eventsList" class="list">
		<tr>
			<@s.text id="dateLabel" name="label.date_performed"/>
			<@s.text id="typeLabel" name="label.eventtype"/>
			<@s.text id="performedByLabel" name="label.performed_by"/>
			<@s.text id="resultLabel" name="label.result"/>
			<@s.text id="assetStatusLabel" name="label.assetstatus"/>
		
			<#assign columns = [ "date", "type.name", "performedByFullName", "status", "assetStatus.name" ] >		
			<#assign labels = [ "${dateLabel}", "${typeLabel}", "${performedByLabel}", "${resultLabel}", "${assetStatusLabel}"]>
			<#assign x=0>
			
			<#list columns as column>
				<#if !sortColumn?exists && column == "date">
					<#assign selected = true>
				<#elseif sortColumn?exists && column == sortColumn>
					<#assign selected = true>
				<#else>
					<#assign selected = false>		
				</#if>				
				<#include "_eventColumnHeader.ftl">
				<#assign x=x+1>
			</#list>
			<th>&nbsp;</th>
			<#if tenant.settings.gpsCapture>
				<th class="reducedWidth">&nbsp;</th> 
			</#if>
		</tr>
		<#list page.list as event >
			<tr>
				<td>${action.formatDateTime(event.date)}</td>
				<td>${event.type.name}</td>
				<td>
					<#assign user=event.performedBy />
					<#include "../eventCrud/_userName.ftl"/>
				</td>
				
				
				<td <#if event.status.displayName== "Pass" >class="passColor"<#elseif event.status.displayName == "Fail">class="failColor"<#else>class="naColor"</#if>><p class="coloredBackground"><@s.text name="${(event.status.label?html)!}"/></p></td>
				<td><@s.text name="${(event.assetStatus.name?html)!}"/>&nbsp;</td>
				<td>
					<#if useContext>
						<#assign additionsToQueryString="&useContext=true"/>
					</#if>
					<#include "../eventCrud/_viewEventLink.ftl"/> 
					<#if sessionUser.hasAccess("editevent") && asset.owner.tenant = event.owner.tenant >
						|
						<a href="#" onclick="return redirect('<@s.url action="selectEventEdit" uniqueID="${event.id}" includeParams="none" />');" ><@s.text name="label.edit"/></a>
					</#if>
				</td>
				<#if (tenant.settings.gpsCapture && event.gpsLocation?exists) >
					<td class="reducedWidth">
						<img src="<@s.url value="/images/gps-icon-small.png"/>"/>
					</td>
				<#else>
					<td class="reducedWidth">
						&nbsp;
					</td>
				</#if>
			</tr>
		</#list>
	</table>
	<#include '../common/_pagination.ftl' />	
	
	<script type="text/javascript">
		document.observe("dom:loaded", function() {
			jQuery("#eventsList img").tooltip({
				tip: '#tooltip',
				position: 'center right',
				offset: [0, 15],
				delay: 0
			});		
		});
	</script>
<#else>
	<div class="initialMessage" >
		<div class="textContainer">
			<h1><@s.text name="label.emptyeventlist"/></h1>
			<p>
				<@s.text name="label.emptyeventlist_message">
					<@s.param>${asset.type.name}</@s.param>
				</@s.text>
			</p>
		</div>
	</div>
</#if>
