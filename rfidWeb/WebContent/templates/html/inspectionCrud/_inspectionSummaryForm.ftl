<head>
	<#include "/templates/html/common/_orgPicker.ftl"/>
	<#include "/templates/html/common/_columnView.ftl"/>
</head>
<@s.hidden name="uniqueID" id="uniqueID"/>
<@s.hidden name="productId"/>
<@s.hidden name="inspectionGroupId"/>
<@s.hidden id="inspectionTypeId" name="type"/>
<div id="productSummary">
	<h2>${product.type.name!?html} <@s.text name="label.summary"/></h2>
	
	<div class="infoSet">
		<label class="label"><@s.text name="${Session.sessionUser.serialNumberLabel}"/></label>
		<span class="fieldHolder">
			${product.serialNumber?html}
		</span>
	</div>
	<div class="infoSet">
		<label class="label"><@s.text name="label.rfidnumber"/></label>
		<span class="fieldHolder">
			${product.rfidNumber!?html}
		</span>
	</div>
	<div class="infoSet">
		<label class="label"><@s.text name="label.desc"/></label>
		<span class="fieldHolder">
			${product.description?html}
		</span>
	</div>
</div>
<#if action.isParentProduct() >
	<h2><@s.text name="label.customerinformation"/></h2>
	
	<div class="infoSet">
		<label class="label" ><@s.text name="label.owner"/></label>
		<@n4.orgPicker name="modifiableInspection.owner" required="true" id="ownerId" />
	</div>	
	
	<div class="infoSet">
		<label class="label" for="asset.location"><@s.text name="label.location"/></label>
		<div class="fieldHolder"><@n4.location name="modifiableInspection.location" id="location" nodesList=helper.predefinedLocationTree fullName="${helper.getFullNameOfLocation(modifiableInspection.location)}" theme="simple"/></div>
	</div>



</#if>

<h2>${inspection.type.name?html} <@s.text name="label.details"/></h2>

<#if action.isParentProduct() >
	<div class="infoSet">
		<label class="label"><@s.text name="label.performed_by"/></label>
		<@s.select name="performedBy" list="examiners" listKey="id" listValue="name"  />
	</div>
	<div class="infoSet">
		<label class="label"><@s.text name="label.date_performed"/></label>
		<#if form_action="ADD">		
			<@s.datetimepicker theme="fieldid" id="datePerformed" onchange="updateAutoSuggest();" name="modifiableInspection.datePerformed"  type="dateTime"/>
		<#else>
			<@s.datetimepicker theme="fieldid" id="datePerformed" name="modifiableInspection.datePerformed"  type="dateTime"/>
		</#if>
		
	</div>
	<#if inspectionScheduleOnInspection>
		<div class="infoSet"> 
			<label class="label"><@s.text name="label.scheduledon"/></label>
		
			<#if inspection.schedule?exists>
				<span  class="fieldHolder">
					${action.formatDate(inspection.schedule.nextDate, false )}
				</span>
			<#else>
				<@s.text name="label.notscheduled"/>
			</#if>
			
		</div>
	<#else>
		<div class="infoSet <#if scheduleSuggested>suggested</#if>">
		
			<label class="label"><@s.text name="label.schedulefor"/></label>
			<div class="fieldHolder">
				<@s.select id="schedule" name="scheduleId" list="schedules" listKey="id" listValue="name" theme="fieldidSimple"/>
				<#if scheduleSuggested>
					<a href="javascript:void(0);" id="suggestedSchedule_button">?</a>
					<div id="suggestedSchedule" class="hidden quickView" ><@s.text name="label.wesuggestedascheduleforyou"/></div>
					<script type="text/javascript">
						$("suggestedSchedule_button").observe( 'click', function(event) { showQuickView('suggestedSchedule', event); } );
					</script>
				</#if>
			</div>
		</div>
	</#if>
		
	<div class="infoSet">
		<label class="label"><@s.text name="label.inspectionbook"/></label>
		<div class="fieldHolder">
			<span id="inspectionBookSelect" <#if newInspectionBookTitle?exists>style="display:none"</#if>>
				<@s.select name="book" id="inspectionBooks" list="inspectionBooks" listKey="id" listValue="name" theme="fieldidSimple">
					<#if !sessionUser.anEndUser >
						<@s.param name="headerKey"></@s.param>
						<@s.param name="headerValue"></@s.param>
					</#if>
					<#if newInspectionBookTitle?exists>
						<@s.param name="disabled" value="true"/>
					</#if> 
				</@s.select>
				<a href="javascript:void(0);" onclick="changeToNewInspectionBook();"><@s.text name="label.new_inspection_book"/></a>
			</span>
			<span id="inspectionBookTitle" <#if !newInspectionBookTitle?exists>style="display:none;"</#if>>
				<@s.textfield name="newInspectionBookTitle" id="newInspectionBook" theme="fieldidSimple">
					<#if !newInspectionBookTitle?exists>
						<@s.param name="disabled" value="true"/>
					</#if>
				</@s.textfield> 
				<a href="javascript:void(0);" onclick="changeToInspectionBookSelect()"><@s.text name="label.select_existing"/></a>
			</span>
		</div>
	</div>
</#if>