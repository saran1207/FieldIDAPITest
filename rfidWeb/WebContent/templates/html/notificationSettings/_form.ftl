<head>
	<script type="text/javascript" src="javascript/notificationSettings.js"> </script>
	<@n4.includeStyle type="page" href="notifications"/>
	
</head>
${action.setPageType('my_account', 'notification_settings')!}

<#include "/templates/html/common/_formErrors.ftl" />
<h2><@s.text name="label.notification"/></h2>


<#include "/templates/html/common/_orgPicker.ftl"/>

<@s.hidden name="uniqueID"/>
<@s.hidden name="view.ID"/>
<@s.hidden name="view.createdTimeStamp"/>
<div class="sectionContent">
	<div class="infoSet fullInfoSet">
		<label for="view.name"><@s.text name="label.name"/></label>
		<@s.textfield name="view.name"/>
	</div>
	<div class="infoSet fullInfoSet">
		<label for="view.frequency"><@s.text name="label.i_want_to_get_it"/></label>
		<@s.select name="view.frequency" emptyOption="false">
			<@s.iterator id="group" value="frequencyGroups">
				<@s.optgroup label="${group.groupName}" list="%{frequencies}" listKey="id" listValue="displayName" />
			</@s.iterator>
		</@s.select>
	</div>
</div>


<h2><@s.text name="label.notification_content"/> (<@s.text name="instruction.you_must_select_atleast_one_peice_of_content"/>)</h2>
<#if (action.fieldErrors['view.reportSelected'])?exists> 
	<div class="formErrors" >
		<@s.fielderror >
		     <@s.param>view.reportSelected</@s.param>
		</@s.fielderror>
	</div>
</#if>
<div class="sectionContent">
	<div class="infoSet fullInfoSet">
		<label for="view.includeUpcoming"><@s.text name="label.include_upcoming"/></label>
		<span class="infoField"><@s.checkbox name="view.includeUpcoming"  theme="fieldidSimple" id="includeUpcoming" /></span>
	</div>
	<div class="tiedToUpcoming notificationContentOptions">
		<div class="infoSet fullInfoSet">
			<label for="view.periodStart"><@s.text name="label.events_starting"/></label>
			<@s.select list="periodStartList" name="view.periodStart" listKey="id" listValue="displayName"  />
		</div>
		
		<div class="infoSet fullInfoSet">
			<label for="view.periodEnd"><@s.text name="label.for_the_next"/></label>
			<@s.select list="periodEndList" name="view.periodEnd" listKey="id" listValue="displayName" />
		</div>
	</div>	
	<div class="infoSet fullInfoSet">
		<label for="view.includeOverdue"><@s.text name="label.include_overdue"/></label>
		<span class="infoField"><@s.checkbox name="view.includeOverdue"  theme="fieldidSimple" /></span>
	</div>
</div>

<h2><@s.text name="label.filters"/></h2>
<div class="sectionContent">	
	<div class="infoSet fullInfoSet">
		<label for="owner"><@s.text name="label.owner"/></label>
		<@n4.orgPicker name="owner"/>
	</div>
	
	<div class="infoSet fullInfoSet">
		<label for="view.productTypeId"><@s.text name="label.product_types"/></label>
		<@s.select name="view.productTypeId" list="productTypeList" listKey="id" listValue="displayName" headerKey="" headerValue="${action.getText('label.all_product_types')}" />
	</div>
	
	<div class="infoSet fullInfoSet">
		<label for="view.inspectionTypeId"><@s.text name="label.inspection_types"/></label>
		<@s.select name="view.inspectionTypeId" list="inspectionTypeList" listKey="id" listValue="displayName" headerKey="" headerValue="${action.getText('label.all_inspection_types')}" />
	</div>
	
	
</div>
	
	

<h2><@s.text name="label.who_should_get_the_notification"/></h2>
<div class="sectionContent">	
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
	<div class="formAction" >
		<@s.url id="cancelUrl" action="notificationSettings"/>
		<@s.reset key="label.cancel" onclick="return redirect('${cancelUrl}');" />
		<@s.submit key="label.save"/>
	</div>
</div>
<script type="text/javascript">
	retireImageSrc = "<@s.url value="/images/retire.gif" />";
	addressCount = ${view.addresses.size()};
	
	function updateUpcomingOptions(element) {
		if ($('includeUpcoming').checked) {
			element.show();
		} else {
			element.hide();
		}
	}
	
	$$('.tiedToUpcoming').each(updateUpcomingOptions);
	$('includeUpcoming').observe('change', function(event) {
			$$('.tiedToUpcoming').each(updateUpcomingOptions);
		});
	
</script>