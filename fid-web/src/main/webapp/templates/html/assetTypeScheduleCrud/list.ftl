<head>
    <@n4.includeScript src="assetTypeSchedule.js"/>
    <style>
        .actions {
            margin:0;
            padding-left:3px;
        }
    </style>
	<script type="text/javascript">
		function setId(newId, newName){
			$('eventTypeIdToUpdate').value=newId;
			$('eventTypeName').innerHTML=newName;
			$('orgPickerForm').show();
		}
	</script>
	<#include "/templates/html/common/_orgPicker.ftl"/>
</head>
${action.setPageType('asset_type', 'schedule_frequencies')!}
<#include "/templates/html/common/_formErrors.ftl"/>
<#if !eventTypes.isEmpty() >
	<table id="eventListTable" class="list" >
		<tr>
			<th class="rowName"><@s.text name="label.eventtype"/></th>
			
			<th><@s.text name="label.schedulefrequency"/></th>
		</tr>
		
		<#list eventTypes as eventType>
		  
			<tr id="event_${eventType.id}">
				<td class="name">${eventType.name}
					<#if eventType.discription?exists > - ${(eventType.description)!}</#if>
				</td>
				<td>
					<div id="eventFrequency_${eventType.id}" class="schedule">
						<#if schedules[eventType.name]?exists >
							<#assign schedule=schedules[eventType.name] />
						<#else>
							<#assign schedule=action.newSchedule() />
						</#if>
						<#include "_show.ftl" />
					</div>
					
					<div id="eventFrequencyOverrides_${eventType.id}_container" <#if !schedules[eventType.name]?exists >style="display:none"</#if> >
						<p style="padding-top:10px; font-size:12px;"><@s.text name="label.overrides"/> 
							<a id="overrideExpand_${eventType.id}" href="javasript:void(0);" onclick="expandOverride( ${eventType.id} ); $('orgPickerForm').hide();; return false;"><img src="<@s.url value="/images/expand.gif" includeParams="none"/>" alt="[+]" title="show customer overrides"/></a>
							<a style="display:none" id="overrideCollapse_${eventType.id}" href="javasript:void(0);" onclick="collapseOverride( ${eventType.id} ); $('orgPickerForm').hide();; return false;"><img src="<@s.url value="/images/collapse.gif" includeParams="none"/>" alt="[+]" title="hide customer overrides"/></a>
						</p>
						
						<div id="overrides_${eventType.id}"  style="display:none">
							<div id="eventFrequencyOverrides_${eventType.id}" >
								<#if overrideSchedules[eventType.name]?exists >
									<#list overrideSchedules[eventType.name] as schedule >
										<div id="eventFrequencyOverride_${eventType.id}_${schedule.owner.id}" class="override customerOverride">
											<#include "_showOverrides.ftl"/>
										</div>
									</#list>
								</#if>
							</div>	 
							<div id="eventFrequencyOverrideForm_${eventType.id}" class="overrideForm">
								<#assign schedule=action.newSchedule() />
								<a href="javascript:void(0);" onclick="setId('${eventType.id}','${eventType.name}'); translatePosition($('orgFormContainer'), this, 0, 0); return false;" ><@s.text name="label.add_override" /></a>
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
			<@s.text name="label.emptyeventtypesselected" />
			<a href="<@s.url action="selectEventTypes" includeParams="none" assetTypeId="${assetTypeId}" />"><@s.text name="label.clickheretoselecteventtypes"/></a>
		</p>
	</div>
</#if>

<div id="orgPickerForm" style="display:none">	
	<div id="orgFormContainer">
	<@s.form id="orgForm" name="orgPickerForm" action="eventFrequencyOverrideCreate" theme="fieldidSimple" >
		<@s.hidden name="assetTypeId" />
		<@s.hidden name="uniqueID" />
		<@s.hidden id="eventTypeIdToUpdate" name="eventTypeId"/>
		<h3><@s.text name="label.overrides_title"/></h3>
		<br/>
		<p><@s.text name="label.overrides_description"/></p>
		<br/>
	
		<@s.text name="label.capital_for"/>
		
		<span class="customer">
			<#if !uniqueID?exists >
				<@n4.orgPicker name="owner" required="true" orgType="non_primary"/>
			<#else>
				<@s.hidden name="ownerId" />
			</#if>
		</span>
		
		, <@s.text name="label.schedule_a"/> 
		
		<b><span id="eventTypeName" > </span> </b>&nbsp;<@s.text name="label.every"/> 
		<span class="frequency">
			<@s.textfield name="frequency"/>
		</span>
		<@s.text name="label.days"/>.
		<span class="orgPickerFormActions">
			<a href="javascript:void(0);" onclick="orgPickerForm.submit(); return false;"> <@s.text name="label.save" /></a>
			<@s.text name="label.or"/>
			<a href="javascript:void(0);" onclick="$('orgPickerForm').hide(); return false;" ><@s.text name="label.close" /></a>
		</span>
	</@s.form>
		</div>
</div>

<script type="text/javascript" >
	editScheduleUrl =  '<@s.url action="assetTypeScheduleEdit" namespace="/ajax"/>';
	cancelScheduleUrl = '<@s.url action="assetTypeScheduleShow" namespace="/ajax"/>';
	removeScheduleUrl = '<@s.url action="assetTypeScheduleDelete" namespace="/ajax"/>';
	removeWarning = '<@s.text name="warning.removedefaultschedule" />';
</script>

