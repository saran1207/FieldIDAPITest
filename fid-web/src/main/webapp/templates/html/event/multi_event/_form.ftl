<@s.form action="eventCreate" namespace="/multiEvent/ajax" id="eventCreate" cssClass="fullForm fluidSets" theme="fieldid">
	<#include "/templates/html/common/_formErrors.ftl"/>
	<@s.hidden name="type"/>
	<@s.hidden name="scheduleId" id="scheduleId" value="0"/>
	<@s.hidden name="assetId" id="assetId"/>

	<h2><@s.text name="label.owner"/></h2>
	
	<#if eventType.assignedToAvailable>
		<div class="infoSet">
			<label class="label"><@s.text name="label.assigned_to"/></label>
            <@s.select id="assignedToSelectBox" name="assignedToId" headerKey="-1" headerValue="${action.getText('label.keep_the_same')}" >
                <#include "/templates/html/common/_assignedToDropDown.ftl"/>
            </@s.select>

			<a href="#" class="assignToMeLink" onclick="setAssignedToAsCurrentUser(${sessionUser.id}); return false;" ><@s.text name="label.assign_to_me"/></a>
			<@s.hidden name="assignToSomeone" id="assignToSomeone" value="true"/>
		</div>
	</#if>
	
	<div class="infoSet">
		<label class="label"><@s.text name="label.owner"/></label>
		<@n4.orgPicker name="modifiableEvent.owner" id="ownerId" />
		<span class="fieldHolder setFromAssetCheckbox">
			<label class="checkBoxLabel"><@s.checkbox name="ownerSetFromAsset" onclick="toggleDisableOrgPicker();" theme="simple"/> <@s.text name="label.use_existing_values_from_assets"/></label>
		</span>
	</div>	
	
	
	<div class="infoSet">
		<label class="label"><@s.text name="label.location"/></label>
		<div class="fieldHolder">
			<span class="multiEventLocation"><@n4.location name="modifiableEvent.location" nodesList=helper.predefinedLocationTree fullName="${helper.getFullNameOfLocation(modifiableEvent.location)}"  theme="simple"/></span>
			<span class="fieldHolder">	
				<label class="checkBoxLabel"><@s.checkbox name="locationSetFromAsset" onclick="toggleDisableLocationPicker($$('.multiEventLocation input'),$$('.multiEventLocation a').first())" theme="simple"/> <@s.text name="label.use_existing_values_from_assets"/></label>
			</span>
		</div>
	</div>
		
	<h2>${event.type.name?html} <@s.text name="label.details"/></h2>
	<div class="infoSet">
		<label class="label"><@s.text name="label.performed_by"/></label>
		<@s.select name="performedBy" list="examiners" listKey="id" listValue="name"  />
	</div>
	<div class="infoSet">
		<label class="label"><@s.text name="label.date_performed"/></label>
		<@s.textfield id="datePerformed" name="modifiableEvent.datePerformed" cssClass="datetimepicker"/>
		<script type="text/javascript">
			initDatePicker();
		</script>
	</div>
	
	
	<#setting url_escaping_charset='UTF-8'>	
	<#list eventType.infoFieldNames as infoField >
		<div class="infoSet">
			<label class="label">${infoField?html}:</label>
			<@s.textfield name="encodedInfoOptionMap['${infoField?url}']"/>
		</div>
	</#list>
		
	<#assign formEvent=event>
	<#assign form_action="ADD">
	<#assign identifier="eventForm">
	<#include "/templates/html/eventCrud/_event.ftl" />
	
	<#include "/templates/html/eventCrud/_result.ftl" />
	
	<h2><@s.text name="label.posteventinformation"/></h2>
	<div class="infoSet">
		<label class="label"><@s.text name="label.comments"/></label>
		<div class="fieldHolder">
			<@s.select name="commentTemplate" list="commentTemplates" listKey="id" listValue="displayName" emptyOption="true" onchange="changeComments(this)" theme="fieldidSimple"/> 
			<@s.textarea name="comments" id="comments"  cols="50" rows="3" theme="fieldidSimple"/>
		</div>
	</div>
	
	<div class="infoSet">
		<label class="label"><@s.text name="label.printable"/></label>
		<span class="fieldHolder">	
			<label class="checkBoxLabel"><@s.checkbox name="printable" theme="simple"/> <@s.text name="label.printableexplination"/></label>
		</span>
	</div>
	
	<div class="infoSet">
		<label class="label"><@s.text name="label.assetstatus"/></label>
		<@s.select name="assetStatus" id="assetStatuses" list="assetStatuses" listKey="id" listValue="name" headerKey="" headerValue="" />
		<span class="fieldHolder setFromAssetCheckbox">	
			<label class="checkBoxLabel"><@s.checkbox name="statusSetFromAsset" onclick="toggleDisabled($(assetStatuses));" theme="simple"/> <@s.text name="label.use_existing_values_from_assets"/></label>
		</span>
	</div>

	<h2><@s.text name="label.autoschedules"/></h2>

	<div class="infoSet">
		<label class="label"><@s.text name="label.autoschedule"/></label>
		<span class="fieldHolder">
			<label class="checkBoxLabel"><@s.checkbox name="refreshAutoSchedules" theme="simple"/> <@s.text name="label.autoscheduleexplanation"/></label>
		</span>
	</div>
	
	<#include "/templates/html/eventCrud/_schedules.ftl" />
	
</@s.form>

<@n4.includeScript>
	function updateAssignToSomone() {
		var value = $('assignedToSelectBox').getValue();
		if (value == '-1') {
			$('assignToSomeone').value = 'false';
		} else {
			$('assignToSomeone').value = 'true';
		}
	}
	
	$$('#assignedToSelectBox').each(function(element) {
		element.observe('change', updateAssignToSomone);
		updateAssignToSomone();
	});
</@n4.includeScript>
