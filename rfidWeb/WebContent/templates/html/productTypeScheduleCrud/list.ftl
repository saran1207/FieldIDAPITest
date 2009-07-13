<head>
	<script language="javascript" src="javascript/productTypeSchedule.js"> </script>
	<style>
		.actions {
			margin:0;
			padding-left:3px;
		}
	</style>
</head>

${action.setPageType('product_type', 'schedule_frequencies')!}

<#if !inspectionTypes.isEmpty() >
	
	<table class="list" >
		<tr>
			<th class="rowName"><@s.text name="label.inspectiontype"/></th>
			
			<th><@s.text name="label.schedulefrequency"/></th>
		</tr>
		
		<#list inspectionTypes as inspectionType>
		  
			<tr id="event_${inspectionType.id}">
				<td class="name">${inspectionType.name} 
					<#if inspectionType.discription?exists > - ${(inspectionType.description)!}</#if>
				</td>
				
				<td>
					<div id="eventFrequency_${inspectionType.id}" class="schedule">
						<#if schedules[inspectionType.name]?exists >
							<#assign schedule=schedules[inspectionType.name] />
						<#else>
							<#assign schedule=action.newSchedule() />
						</#if>
						<#include "_show.ftl" />
					</div>
					
					<div id="eventFrequencyOverrides_${inspectionType.id}_container" <#if !schedules[inspectionType.name]?exists >style="display:none"</#if> >
						<p style="padding-top:10px;">Customer Overrides 
							<a id="overrideExpand_${inspectionType.id}" href="javasript:void(0);" onclick="expandOverride( ${inspectionType.id} ); return false;"><img src="<@s.url value="/images/expand.gif" includeParams="none"/>" alt="[+]" title="show customer overrides"/></a>
							<a style="display:none" id="overrideCollapse_${inspectionType.id}" href="javasript:void(0);" onclick="collapseOverride( ${inspectionType.id} ); return false;"><img src="<@s.url value="/images/collapse.gif" includeParams="none"/>" alt="[+]" title="hide customer overrides"/></a>
						</p>
						
						<div id="overrides_${inspectionType.id}"  style="display:none">
							<div id="eventFrequencyOverrides_${inspectionType.id}" >
								<#if customerOverrideSchedules[inspectionType.name]?exists >
									<#list customerOverrideSchedules[inspectionType.name] as schedule >
										<div id="eventFrequencyOverride_${inspectionType.id}_${schedule.customer.id}" class="override customerOverride"> 
											<#include "_show.ftl"/>
										</div>
									</#list>
								</#if>
								
							</div>	 
							<div id="eventFrequencyOverrideForm_${inspectionType.id}" class="overrideForm">			
								<#assign schedule=action.newSchedule() />
								<#include "_add.ftl" />
							</div>
						</div>
						 
					</div>
				</td>
			</tr>
		</#list>
	</table>
<#else>
	<div class="emptyList" >
		<h2><@s.text name="label.noresults" /></h2>
		<p>
			<@s.text name="label.emptyinspectiontypesselected" />
			<a href="<@s.url action="selectInspectionTypes" includeParams="none" productTypeId="${productTypeId}" />"><@s.text name="label.clickheretoselectinspectiontypes"/></a>
		</p>
	</div>
</#if>


<script type="text/javascript" >
	editScheduleUrl =  '<@s.url action="productTypeScheduleEdit" namespace="/ajax" includeParams="none" />';
	cancelScheduleUrl = '<@s.url action="productTypeScheduleShow" namespace="/ajax" includeParams="none" />';
	removeScheduleUrl = '<@s.url action="productTypeScheduleDelete" namespace="/ajax" includeParams="none" />';
	
	removeWarning = '<@s.text name="warning.removedefaultschedule" />';
</script>

