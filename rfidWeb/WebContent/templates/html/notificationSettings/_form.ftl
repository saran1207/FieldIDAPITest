<head>
	<script type="text/javascript" src="<@s.url value="javascript/customerUpdate.js" />"></script>
	<script type="text/javascript">
		customerChangeUrl = "<@s.url action="divisionList" namespace="/ajax" />";
	</script>
	<script type="text/javascript" src="javascript/notificationSettings.js"> </script>
	<link type="text/css" rel="stylesheet" href="<@s.url value="/style/pageStyles/notifications.css"/>"/>
	
</head>
${action.setPageType('my_account', 'notification_settings')!}
<h2><@s.text name="label.notification"/></h2>

<#include "/templates/html/common/_formErrors.ftl" />

<@s.hidden name="uniqueID"/>
<@s.hidden name="view.ID"/>
<@s.hidden name="view.ownerId"/>
<@s.hidden name="view.createdTimeStamp"/>
<div class="sectionContent">
	<div class="infoSet fullInfoSet">
		<label for="view.name"><@s.text name="label.name"/></label>
		<@s.textfield name="view.name">
			<#if (action.fieldErrors['view.name'])?exists> 
				<@s.param name="cssClass">inputError</@s.param>
				<@s.param name="title">${action.fieldErrors['view.name']}</@s.param>
			</#if>  
		</@s.textfield>
	</div>
	
	<div id="customerDivisionList">
		<#if securityGuard.jobSitesEnabled>
			<div class="infoSet">
				<label for="view.jobSiteId"><@s.text name="label.jobsite"/></label>
				<@s.select id="jobSite" name="view.jobSiteId" list="jobSites" listKey="id" listValue="name" headerKey="" headerValue="${action.getText('label.all_jobsites')}" />
			</div>
		<#else>
			<div class="infoSet">
				<label for="view.customerId"><@s.text name="label.customer"/></label>
				<@s.select id="customer" name="view.customerId" list="customers" listKey="id" listValue="displayName" headerKey="" headerValue="${action.getText('label.all_customers')}" onchange="customerChanged(this);" />
			</div>
			<div class="infoSet">
				<label for="view.divisionId"><@s.text name="label.division"/></label>
				<@s.select id="division" name="view.divisionId" list="divisions" listKey="id" listValue="displayName" headerKey="" headerValue="${action.getText('label.all_divisions')}" />
			</div>
		</#if>
	</div>
	
	<div class="infoSet fullInfoSet">
		<label for="view.productTypeId"><@s.text name="label.product_types"/></label>
		<@s.select name="view.productTypeId" list="productTypeList" listKey="id" listValue="displayName" headerKey="" headerValue="${action.getText('label.all_product_types')}" />
	</div>
	
	<div class="infoSet fullInfoSet">
		<label for="view.inspectionTypeId"><@s.text name="label.inspection_types"/></label>
		<@s.select name="view.inspectionTypeId" list="inspectionTypeList" listKey="id" listValue="displayName" headerKey="" headerValue="${action.getText('label.all_inspection_types')}" />
	</div>
	
	<div class="infoSet fullInfoSet">
		<label for="view.frequency"><@s.text name="label.frequency"/></label>
		<@s.select name="view.frequency" emptyOption="false">
			<@s.iterator id="group" value="frequencyGroups">
				<@s.optgroup label="${group.groupName}" list="%{frequencies}" listKey="id" listValue="displayName" />
			</@s.iterator>
		</@s.select>
	</div>
	
	<div class="infoSet">
		<label for="view.periodStart"><@s.text name="label.events_starting"/></label>
		<@s.select list="periodStartList" name="view.periodStart" listKey="id" listValue="displayName" />
	</div>
	
	<div class="infoSet">
		<label for="view.periodEnd"><@s.text name="label.for_the_next"/></label>
		<@s.select list="periodEndList" name="view.periodEnd" listKey="id" listValue="displayName" />
	</div>
	
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
</script>