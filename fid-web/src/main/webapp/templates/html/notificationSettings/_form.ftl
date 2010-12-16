<head>
	<script type="text/javascript" src="javascript/notificationSettings.js"> </script>
	<@n4.includeStyle type="page" href="notifications"/>
	
</head>
${action.setPageType('my_account', 'notification_settings')!}

<#include "/templates/html/common/_formErrors.ftl" />



<#include "/templates/html/common/_orgPicker.ftl"/>

<@s.hidden name="uniqueID"/>
<@s.hidden name="view.ID"/>
<@s.hidden name="view.createdTimeStamp"/>

<div class="sectionContent name">
	<div class="infoSet fullInfoSet enlarged">
		<label class="bold" for="view.name"><@s.text name="label.name"/></label>
		<span class="fieldHolder"><@s.textfield name="view.name"/><p class="instructions"><@s.text name="instruction.name"/></p></span>
	</div>
</div>

<h2><@s.text name="label.delivery_options"/></h2>

<div class="sectionContent deliveryOptions">
	<div class="infoSet fullInfoSet">
		<label for="view.frequency"><@s.text name="label.i_want_to_get_it"/></label>
		<@s.select name="view.frequency" emptyOption="false">
			<@s.iterator id="group" value="frequencyGroups">
				<@s.optgroup label="${group.groupName}" list="%{frequencies}" listKey="id" listValue="displayName" />
			</@s.iterator>
		</@s.select>
	</div>
	<div class="infoSet fullInfoSet">
		<label>&nbsp;</label>
		<span class="infoField checkbox"><@s.checkbox name="view.sendBlankReport"  theme="fieldidSimple"/></span>
		<label class="large" for="view.sendBlankReport"><@s.text name="label.send_blank_report"/></label>
	</div>
</div>


<h2><@s.text name="label.notification_content"/></h2>
<#if (action.fieldErrors['view.reportSelected'])?exists> 
	<div class="formErrors" >
		<@s.fielderror >
		     <@s.param>view.reportSelected</@s.param>
		</@s.fielderror>
	</div>
</#if>
<div class="sectionContent content">
	<div id="upcomingEvents" class="infoSet fullInfoSet">
		<span class="infoField checkbox"><@s.checkbox name="view.includeUpcoming"  theme="fieldidSimple" id="includeUpcoming" /></span>
		<label for="view.includeUpcoming"><@s.text name="label.include_upcoming"/></label>
		<p class="upcomingDates">
			<@s.select list="periodStartList" name="view.periodStart" listKey="id" listValue="displayName" id="periodStart" />
			<span><@s.text name="label.for_the_next"/></span>
			<@s.select list="periodEndList" name="view.periodEnd"  listKey="id" listValue="displayName" id="periodEnd"/>
		</p>
	</div>
	<div class="infoSet fullInfoSet">
		<span class="infoField checkbox"><@s.checkbox name="view.includeOverdue"  theme="fieldidSimple" /></span>
		<label for="view.includeOverdue"><@s.text name="label.include_overdue"/></label>
	</div>
	<div class="infoSet fullInfoSet">
		<span class="infoField checkbox"><@s.checkbox name="view.includeFailed"  theme="fieldidSimple" /></span>
		<span class="wide">
			<label for="view.includeFailed"><@s.text name="label.include_failed"/></label>
			<p class="instructions"><@s.text name="instruction.content_example"/></p>
		</span>
	</div>
</div>

<h2><@s.text name="label.filters"/></h2>
<div class="sectionContent filters">	
	<div class="infoSet fullInfoSet">
		<label for="owner"><@s.text name="label.owner"/></label>
		<@n4.orgPicker name="owner"/>
	</div>
	
	<div class="infoSet fullInfoSet">
		<label for="view.assetTypeId"><@s.text name="label.asset_types"/></label>
		<@s.select name="view.assetTypeId" list="assetTypeList" listKey="id" listValue="displayName" headerKey="" headerValue="${action.getText('label.all_asset_types')}" />
	</div>
	
	<div class="infoSet fullInfoSet">
		<label for="view.eventTypeId"><@s.text name="label.event_types"/></label>
		<@s.select name="view.eventTypeId" list="eventTypeList" listKey="id" listValue="displayName" headerKey="" headerValue="${action.getText('label.all_event_types')}" />
	</div>
	
	
</div>
	
	

<h2><@s.text name="label.who_should_get_the_notification"/></h2>
<div class="sectionContent email">	
	<div id="emailAddresses" class="infoSet fullInfoSet">
		<label><@s.text name="label.emailaddresses"/></label>	
		<div id="addressList">
			<#if (action.fieldErrors['view.addresses'])?exists> 
				<@s.param name="cssClass">inputError</@s.param>
				<@s.param name="title">${action.fieldErrors['view.addresses']}</@s.param>
			</#if>
			<#list view.addresses as address >
				<#assign addressIdx=address_index/>
				<div id="addressSpan_${addressIdx}">
					<@s.textfield id="addressInput_${addressIdx}" name="view.addresses[${addressIdx}]"/>
					<a href="javascript:void(0);" onclick="removeAddress(${addressIdx}); return false;">
						<img src="<@s.url value="/images/retire.gif" includeParams="none"/>" />
					</a>
				</div>
			</#list>
			<div id="addAddressButton">
				<button onclick="addAddress(); return false;">
					<@s.text name="label.add"/>
				</button>
			</div>
		</div>
	</div>
	<div class="formAction borderLessFormAction" >
	<@s.submit key="label.save"/>
		<@s.url id="cancelUrl" action="notificationSettings"/>
		<@s.text name="label.or"/>
		<a href="#" onclick="return redirect('${cancelUrl}');"><@s.text name="label.cancel"/></a>
	</div>
</div>
<script type="text/javascript">
	retireImageSrc = "<@s.url value="/images/retire.gif" />";
	addressCount = ${view.addresses.size()};


	function updateUpcomingOptions() {
		if ($('includeUpcoming').checked) {
			$('periodStart').disabled = false;
			$('periodEnd').disabled = false;
		} else {
			$('periodStart').disabled = true;
			$('periodEnd').disabled = true;
		}
	}
	
	$('includeUpcoming').observe('change', function(event) {
			updateUpcomingOptions();
	});

	document.observe("dom:loaded", function() {
		updateUpcomingOptions();
	});
	
</script>
