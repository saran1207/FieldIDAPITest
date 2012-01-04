<head>
	<#include "/templates/html/common/_orgPicker.ftl"/>
	<#include "/templates/html/common/_columnView.ftl"/>
	<#include "/templates/html/common/_datetimepicker.ftl"/>
</head>
<@s.hidden name="uniqueID" id="uniqueID"/>
<@s.hidden name="assetId"/>
<@s.hidden name="eventGroupId"/>
<@s.hidden id="eventTypeId" name="type"/>
<div id="assetSummary" class="assetSummary">
	<h2>${asset.type.name!?html}  <@s.text name="label.summary"/></h2>

    <div class="infoSet">
		<label class="label"><@s.text name="label.desc"/></label>
		<span class="fieldHolder">
			${asset.description?html}
		</span>
	</div>
	<div class="infoSet">
		<label class="label">${identifierLabel}</label>
		<span class="fieldHolder">
            <a href="<@s.url action="asset" uniqueID="${asset.id}"/>">${asset.identifier?html}</a>
		</span>
	</div>
	<div class="infoSet">
		<label class="label"><@s.text name="label.rfidnumber"/></label>
		<span class="fieldHolder">
			${asset.rfidNumber!?html}
		</span>
	</div>
	<div class="infoSet">
		<label class="label"><@s.text name="label.referencenumber"/></label>
		<span class="fieldHolder">
			${asset.customerRefNumber!?html}
		</span>
	</div>
</div>
<#if action.isParentAsset() >
	<h2><@s.text name="label.owner"/></h2>
		
	<#if eventType.assignedToAvailable && form_action="ADD">
		<div  class="infoSet">
			<label class="label"><@s.text name="label.assigned_to"/></label>
			<span class="fieldHolder">
				<@s.select id="assignedToSelectBox" name="assignedToId"  headerKey="0" headerValue="${action.getText('label.unassigned')}" >
					<#include "/templates/html/common/_assignedToDropDown.ftl"/>
				</@s.select>
				<br/>
				<a href="#" onclick="setAssignedToAsCurrentUser(${sessionUser.id}); return false;" ><@s.text name="label.assign_to_me"/></a>
				<@s.hidden name="assignToSomeone" id="assignToSomeone" value="true"/>
			</span>
		</div>
	<#elseif form_action="EDIT">
		<#include "_assigned_to.ftl"/>
	</#if>
	
	
	<div class="infoSet">
		<label class="label" ><#include "/templates/html/common/_requiredMarker.ftl"/><@s.text name="label.owner"/></label>
		<span class="fieldHolder"><@n4.orgPicker name="modifiableEvent.owner" id="ownerId" /></span>
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
			<@s.textfield theme="fieldid" id="datePerformed" onchange="updateAutoSuggest();" name="modifiableEvent.datePerformed" cssClass="datetimepicker"/>
		<#else>
			<@s.textfield theme="fieldid" id="datePerformed" name="modifiableEvent.datePerformed" cssClass="datetimepicker"/>
		</#if>
		<script type="text/javascript">
			initDatePicker();
		</script>	
	</div>

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
		<div class="infoSet">		
			<label class="label"><@s.text name="label.schedulefor"/></label>
			<span class="fieldHolder">
				<@s.select id="schedule" name="scheduleId" list="schedules" listKey="id" listValue="name" theme="fieldidSimple"/>
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
