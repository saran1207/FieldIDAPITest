<head>
	<#include "/templates/html/common/_calendar.ftl"/>
	<link rel="stylesheet" type="text/css" href="<@s.url value="/style/pageStyles/search.css"/>" />
	<script type="text/javascript" src="<@s.url value="/javascript/search.js"/>"></script>
	<script type="text/javascript">
		updatingColumnText = '<@s.text name="label.availablecolumnsupdating"/>'; 
		dynamicColumnUrl = '<@s.url action="scheduleDynamicColumns" namespace="/ajax" />';
	</script>
	<#include "/templates/html/common/_orgPicker.ftl"/>
	<#include "/templates/html/common/_columnView.ftl"/>
</head>

<div class="pageSection searchPageSection" id="criteriaForm">
	<div class="headerWithFootnote">
		<#if listPage?exists>
			<a href="javascript:void(0);" id="expandSection_reportForm" onclick="openSection('reportForm', 'expandSection_reportForm', 'collapseSection_reportForm');return false" ><img src="<@s.url value="/images/expandLarge.gif" />" /></a>
			<a href="javascript:void(0);" id="collapseSection_reportForm" onclick="closeSection('reportForm', 'collapseSection_reportForm', 'expandSection_reportForm');return false" style="display:none;"><img src="<@s.url value="/images/collapseLarge.gif" />" /></a>
		</#if>
		<span class="headerText">
			<@s.text name="label.schedulecriteria"/>
		</span>
		<br>
		<span class="footnoteText">
			<@s.text name="label.wildcard_explanation"/>
		</span>
	</div>
	
	<@s.form action="schedule!createSearch" id="reportForm" cssClass="crudForm twoColumns" theme="fieldid" cssStyle="${listPage?exists?string('display:none;','')}" >
		<#include "../common/_formErrors.ftl"/>
		<div class="sectionContent" >
			<div class="container">
				<div class="infoSet">
					<label for="criteria.rfidNumber"><@s.text name="label.schedulestatus"/></label> 
					<@s.select name="criteria.status" list="scheduleStatuses" listKey="name" listValue="%{getText(label)}"/>
				</div>
				
				<div class="infoSet">
					<label>&nbsp;</label>
				</div>
			</div>
				
			<div class="infoSet">
				<label for="criteria.rfidNumber"><@s.text name="label.rfidnumber"/></label> 
				<@s.textfield name="criteria.rfidNumber"/>
			</div>
			
			<div class="infoSet">
				<label for="criteria.serialNumber"><@s.text name="${sessionUser.serialNumberLabel}"/></label> 
				<@s.textfield name="criteria.serialNumber"/>
			</div>
			
				
			<div class="infoSet">
				<label for="criteria.orderNumber"><@s.text name="label.onumber"/></label>
				<@s.textfield name="criteria.orderNumber" />
			</div>
			<div class="infoSet">
				<label for="criteria.purchaseorder"><@s.text name="label.purchaseorder"/></label>
				<@s.textfield name="criteria.purchaseOrder" />
				
			</div>
			<#if securityGuard.assignedToEnabled>
				<div class="infoSet">
					<label for="criteria.assignedUser"><@s.text name="label.assignedto"/></label>
					<@s.select name="criteria.assignedUser" list="employees" listKey="id" listValue="displayName" emptyOption="true" />
				</div>	
			</#if>
			
			<div class="infoSet">
				<label for="criteria.location"><@s.text name="label.location"/></label>
				<@n4.location name="criteria.location" id="location" nodesList=helper.predefinedLocationTree fullName="${helper.getFullNameOfLocation(criteria.location)}"/>
			</div>	
		
			<div class="infoSet">
				<label for="criteria.inspectionType"><@s.text name="label.eventtypegroup"/></label>
				<@s.select name="criteria.inspectionType" list="inspectionTypes" listKey="id" listValue="name" emptyOption="true"/>
			</div>
			
			
			<div class="infoSet">
				<label for="criteria.referenceNumber"><@s.text name="label.referencenumber"/></label>
				<@s.textfield name="criteria.referenceNumber"/>
			</div>
			
			<#include "../customizableSearch/_productTypeSelect.ftl"/>
				
			<div class="infoSet">
				<label for="criteria.productStatus"><@s.text name="label.productstatus"/></label>
				<@s.select  name="criteria.assetStatus" list="assetStatuses" listKey="id" listValue="name" emptyOption="true" />
			</div>
			<#if securityGuard.projectsEnabled>
				<div class="infoSet">
					<label for="criteria.job"><@s.text name="label.job"/></label>
					<@s.select name="criteria.job" list="eventJobs" listKey="id" listValue="name" emptyOption="true" />
				</div>
			</#if>
			
			<#include "_ownershipFilters.ftl"/>
			
			<div class="infoSet">
				<label>&nbsp;</label>
			</div>
			
			<#include "_dateRange.ftl"/>
		</div>
		<#include "../customizableSearch/_selectColumns.ftl"/>
		
		<div class="formAction">
			<@s.reset key="label.clearform" onclick="clearForm(this.form); return false;"/>
			<@s.submit key="label.Run"/>
		</div>
	</@s.form >
</div>

