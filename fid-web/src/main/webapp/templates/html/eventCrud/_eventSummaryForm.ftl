<head>
	<#include "/templates/html/common/_orgPicker.ftl"/>
	<#include "/templates/html/common/_columnView.ftl"/>
</head>
<@s.hidden name="uniqueID" id="uniqueID"/>
<@s.hidden name="assetId"/>
<@s.hidden name="eventGroupId"/>
<@s.hidden id="eventTypeId" name="type"/>
<div id="assetSummary">
	<h2>${asset.type.name!?html} <@s.text name="label.summary"/></h2>
	
	<div class="infoSet">
		<label class="label"><@s.text name="${Session.sessionUser.serialNumberLabel}"/></label>
		<span class="fieldHolder">
			${asset.serialNumber?html}
		</span>
	</div>
	<div class="infoSet">
		<label class="label"><@s.text name="label.rfidnumber"/></label>
		<span class="fieldHolder">
			${asset.rfidNumber!?html}
		</span>
	</div>
	<div class="infoSet">
		<label class="label"><@s.text name="label.desc"/></label>
		<span class="fieldHolder">
			${asset.description?html}
		</span>
	</div>
</div>
<#if action.isParentAsset() >
	<h2><@s.text name="label.customerinformation"/></h2>
	
	<div class="infoSet">
		<label class="label" ><@s.text name="label.owner"/></label>
		<@n4.orgPicker name="modifiableEvent.owner" required="true" id="ownerId" />
	</div>	
	
	<div class="infoSet">
		<label class="label" for="asset.location"><@s.text name="label.location"/></label>
		<span class="fieldHolder locationFieldHolder"><@n4.location name="modifiableEvent.location" id="location" nodesList=helper.predefinedLocationTree fullName="${helper.getFullNameOfLocation(modifiableEvent.location)}" theme="simple"/></span>
	</div>



</#if>

<h2>${event.type.name?html} <@s.text name="label.details"/></h2>

<#if action.isParentAsset() >
	<div class="infoSet">
		<label class="label"><@s.text name="label.performed_by"/></label>
		<span class="fieldHolder">
			<@s.select name="performedBy" list="examiners" listKey="id" listValue="name"  />
		</span>
	</div>
	<div class="infoSet">
		<label class="label"><@s.text name="label.date_performed"/></label>
		<#if form_action="ADD">		
			<@s.datetimepicker theme="fieldid" id="datePerformed" onchange="updateAutoSuggest();" name="modifiableEvent.datePerformed"  type="dateTime"/>
		<#else>
			<@s.datetimepicker theme="fieldid" id="datePerformed" name="modifiableEvent.datePerformed"  type="dateTime"/>
		</#if>
		
	</div>
	
	<#if eventType.assignedToAvailable && form_action="ADD">
		<div  class="infoSet">
			<label class="label"><@s.text name="label.assign_asset_to"/></label>
			<@s.select name="assignedToId" list="employees" listKey="id" listValue="displayName" />
			<@s.hidden name="assignToSomeone" id="assignToSomeone" value="true"/>
		</div>
	<#elseif form_action="EDIT">
		<#include "_assigned_to.ftl"/>
	</#if>
	
	<#if eventScheduleOnEvent>
		<div class="infoSet"> 
			<label class="label"><@s.text name="label.scheduledon"/></label>
		
			<#if event.schedule?exists>
				<span class="fieldHolder">
					${action.formatDate(event.schedule.nextDate, false )}
				</span>
			<#else>
				<@s.text name="label.notscheduled"/>
			</#if>
			
		</div>
	<#else>
		<div class="infoSet <#if scheduleSuggested>suggested</#if>">
		
			<label class="label"><@s.text name="label.schedulefor"/></label>
			<span class="fieldHolder">
				<@s.select id="schedule" name="scheduleId" list="schedules" listKey="id" listValue="name" theme="fieldidSimple"/>
				<#if scheduleSuggested>
					<a href="javascript:void(0);" id="suggestedSchedule_button">?</a>
					<div id="suggestedSchedule" class="hidden quickView" ><@s.text name="label.wesuggestedascheduleforyou"/></div>
					<script type="text/javascript">
						$("suggestedSchedule_button").observe( 'click', function(event) { showQuickView('suggestedSchedule', event); } );
					</script>
				</#if>
			</span>
		</div>
	</#if>
		
	<div class="infoSet">
		<label class="label"><@s.text name="label.eventbook"/></label>
		<div class="fieldHolder">
			<span id="eventBookSelect" <#if newEventBookTitle?exists>style="display:none"</#if>>
				<@s.select name="book" id="eventBooks" list="eventBooks" listKey="id" listValue="name" theme="fieldidSimple">
					<#if !sessionUser.anEndUser >
						<@s.param name="headerKey"></@s.param>
						<@s.param name="headerValue"></@s.param>
					</#if>
					<#if newEventBookTitle?exists>
						<@s.param name="disabled" value="true"/>
					</#if> 
				</@s.select>
				<br/>
				<a href="javascript:void(0);" onclick="changeToNewEventBook();"><@s.text name="label.new_event_book"/></a>
			</span>
			<span id="eventBookTitle" <#if !newEventBookTitle?exists>style="display:none;"</#if>>
				<@s.textfield name="newEventBookTitle" id="newEventBook" theme="fieldidSimple">
					<#if !newEventBookTitle?exists>
						<@s.param name="disabled" value="true"/>
					</#if>
				</@s.textfield> 
				<br/>
				<a href="javascript:void(0);" onclick="changeToEventBookSelect()"><@s.text name="label.select_existing"/></a>
			</span>
		</div>
	</div>
</#if>
