<title><@s.text name="title.mass_event"/></title>
<#assign loaderDiv>
	<div class="loading"><img src="<@s.url value="/images/indicator_mozilla_blu.gif"/>"/></div>
</#assign>
<#assign row>
<tr class="%%EXTRA_CLASS%%"> 
	<td>%%IDENTIFIER%%</td>
	<td>%%RFID%%</td>
	<td>%%OWNER%%</td>
	<td>%%TYPE%%</td>
	<td>%%IDENTIFIED%%</td>
	<td>%%REFERENCE_NUMBER%%</td>
	<td>%%CREATION_STATUS%%</td>
</tr>
</#assign>

<head>
	<@n4.includeStyle href="steps"/>
	<@n4.includeScript src="steps"/>
	<@n4.includeScript src="event" />
    <@n4.includeScript src="signatures" />
	<@n4.includeScript src="multi_inspect" />
	<@n4.includeStyle href="newCss/event/event_base" />
    <@n4.includeStyle href="newCss/event/multi_event" />
    <@n4.includeStyle href="newCss/event/event_schedule" />

	<#include "/templates/html/common/_orgPicker.ftl"/>
	<#include "/templates/html/common/_columnView.ftl"/>
	<#include "/templates/html/common/_datetimepicker.ftl"/>
	
	<@n4.includeScript src="commentTemplates"/>
	<@n4.includeScript src="combobox"/>
	<@n4.includeScript src="schedulePickerDates"/>
		
	<@n4.includeScript>
		changeCommentUrl = '<@s.url action="commentTemplateShow" namespace="/ajax"/>';
		var loading_holder = '${loaderDiv?js_string}';
		var resultRow = '${row?js_string}';
		
		onDocumentLoad(function(){ toStep(1); });
		
		var asset = null;
		<#list assets as asset>
			<#include "/templates/html/assetCrud/_js_asset.ftl"/>
			assets.push(asset);
		</#list>
	</@n4.includeScript>

</head>

<div class="steps">
	<div class="step">
		<h2>1. <@s.text name="label.select_event"/></h2>
		<div class="stepContent" id="step1">
			<div class="multiEventInstructions">
				<p><@s.text name="label.multi_event_instructions"><@s.param>${assets.size()}</@s.param></@s.text></p>	
			</div>
			<#include "_selectEventType.ftl" />
			
		</div>
	</div>
	
	<div class="step">
		<h2>2. <@s.text name="label.scheduled_events"/></h2>
		
		<div class="stepContent" id="step2">
		
		</div>
		
		<div class="stepContent loader hide" id="step2Loading" >
			${loaderDiv}
			<p>
				<@s.text name="label.retrieving_event_schedules" />
			</p>
		</div>
		
	</div>
	
	<div class="step">
		<h2>3. <@s.text name="label.perform_the_event"/></h2>
		<div class="stepContent"  id="step3">
			
		</div>
		<div class="stepContent loader hide" id="step3Loading" >
			${loaderDiv}
			<p>
				<@s.text name="label.retrieving_event_info" />
			</p>
		</div>
	</div>
	<div class="step">
		<h2>4. <@s.text name="label.confirm"/></h2>
		<div class="stepContent"  id="step4">
			
			<p class="multiEventInstructions"><@s.text name="label.you_are_about_to_perform_mass_event_on"><@s.param><span id="eventTypeToReplace">&nbsp;</span></@s.param><@s.param><span class="boldedAssetNumber">${assets.size()}</span></@s.param></@s.text></p>
			<div class="stepAction">
				<input type="button" id="saveEvents" value="<@s.text name="label.save_all"/>" />
				<@s.text name="label.or"/> <a href="#" onclick="backToStep(3);$('continueButtonStep3').disabled=false;"><@s.text name="label.back_to_step"/> 3</a>
			</div>
			
			<div style="overflow:hidden text-align:center" class="progress hide stepAction">
				<div style="float:left">
					${loaderDiv}
					<p style="text-align:center">
						<@s.text name="label.sending"/>
					</p>
					<div style="width:300px; float:left;">
						<@n4.percentbar  progress="0" total="${assets.size()}"/>
					</div>
					<div style="float:left; margin:5px;"><span id="completedEvents">0</span> <@s.text name="label.of"/> ${assets.size()}</div>
				</div>
				<table id="creationError" class="list hide">
					<tr class="header">
						<th>${identifierLabel}</th>
						<th><@s.text name="label.rfidnumber"/></th>
						<th><@s.text name="label.owner"/></th>
						<th><@s.text name="label.assettype"/></th>
						<th><@s.text name="label.identified"/></th>
						<th><@s.text name="label.reference_number"/></th>
						<th><@s.text name="label.create_status"/></th>
					</tr>
				</table>
			</div>
		</div>	
	</div>
	<div class="step">
		<h2>5. <@s.text name="label.complete"/></h2>
		<div class="stepContent"  id="step5">
			<table id="listComplete" class="list">
				<tr class="header">
					<th>${identifierLabel}</th>
					<th><@s.text name="label.rfidnumber"/></th>
					<th><@s.text name="label.owner"/></th>
					<th><@s.text name="label.assettype"/></th>
					<th><@s.text name="label.identified"/></th>
					<th><@s.text name="label.reference_number"/></th>
					<th><@s.text name="label.create_status"/></th>
				</tr>
			</table>
			<div class="stepAction">
				<@s.form action="selectEventType" theme="simple">
					<#list assets as asset>
						<@s.hidden name="assetIds[${asset_index}]"/>
					</#list>
					<@s.submit id="preformAnotherEvent" key="label.perform_another_event_on_these_assets"/>
					<@s.text name="label.or"/>
					<a href="<@s.url action="assetSelection" namespace="/"/>"><@s.text name="label.select_a_new_set_of_assets"/></a>
				</@s.form>
			</div>
		</div>
		
	</div>
</div>
