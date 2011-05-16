<head>
	<#include "/templates/html/common/_calendar.ftl"/>
	<@n4.includeStyle type="page" href="search" />
	<@n4.includeStyle type="page" href="downloads" />
	
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
			<h2 class="decoratedHeaderAlternative">
				<a href="javascript:void(0);" id="expandSection_reportForm" onclick="openSection('reportForm', 'expandSection_reportForm', 'collapseSection_reportForm');return false" ><img src="<@s.url value="/images/expandLarge.gif" />" /></a>
				<a href="javascript:void(0);" id="collapseSection_reportForm" onclick="closeSection('reportForm', 'collapseSection_reportForm', 'expandSection_reportForm');return false" style="display:none;"><img src="<@s.url value="/images/collapseLarge.gif" />" /></a>
				<span class="headerText">
					<@s.text name="label.schedulecriteria"/>
				</span>
			</h2>
		<#else>
			<div class="tipContainer">
				<img src="<@s.url value="/images/tip-icon.png" />"/>
				<p class="footnoteText">
					<@s.text name="label.wildcard_explanation"/>
				</p>
			</div>
		</#if>
	</div>
	
	<@s.form action="schedule!createSearch" id="reportForm" cssClass="crudForm searchForm" theme="fieldid" cssStyle="${listPage?exists?string('display:none;','')}" >
		<#include "../common/_formErrors.ftl"/>
		
			<div class="fieldGroup fieldGroupGap">
				<h2><@s.text name="label.schedule_details"/></h2>
				<div class="infoSet">
					<label for="criteria.rfidNumber"><@s.text name="label.schedulestatus"/></label> 
					<@s.select name="criteria.status" list="scheduleStatuses" listKey="name" listValue="%{getText(label)}"/>
				</div>

                <div class="infoSet">
                    <label for="criteria.eventTypeGroup"><@s.text name="label.eventtypegroup"/></label>
                    <@s.select name="criteria.eventTypeGroup" id="eventTypeGroup" list="eventTypeGroups" listKey="id" listValue="name" headerKey="" headerValue="${action.getText('label.all')}" onchange="updateEventTypes(this)"/>
                </div>
                <div class="infoSet">
                    <label for="criteria.eventType"><@s.text name="label.eventtype"/></label>
                   <@s.select cssClass="eventTypeSelect" id="eventType" name="criteria.eventType" list="eventTypes" listKey="id" listValue="name" emptyOption="true" onchange="eventTypeChanged(this)" />
                </div>

				<#if securityGuard.projectsEnabled>
					<div class="infoSet">
						<label for="criteria.job"><@s.text name="label.job"/></label>
						<@s.select name="criteria.job" list="eventJobs" listKey="id" listValue="name" emptyOption="true" />
					</div>
				</#if>
			</div>
			
			<div class="fieldGroup">
				<h2><@s.text name="label.scheduleddate"/></h2>
				<#include "_dateRange.ftl"/>
				<div class="infoSet">
					<label></label> 
				</div>
			</div>
		
			<div class="fieldGroup fieldGroupGap">
				<h2><@s.text name="label.identifiers"/></h2>
			
				<div class="infoSet">
					<label for="criteria.rfidNumber"><@s.text name="label.rfidnumber"/></label> 
					<@s.textfield name="criteria.rfidNumber"/>
				</div>
				<div class="infoSet">
					<label for="criteria.serialNumber"><@s.text name="${sessionUser.serialNumberLabel}"/></label> 
					<@s.textfield name="criteria.serialNumber"/>
				</div>
				<div class="infoSet">
					<label for="criteria.referenceNumber"><@s.text name="label.referencenumber"/></label>
					<@s.textfield name="criteria.referenceNumber"/>
				</div>
			</div>
					
			<div class="fieldGroup">	
				<h2><@s.text name="label.ownership"/></h2>
				<#if securityGuard.assignedToEnabled>
					<div class="infoSet">
						<label for="criteria.assignedUser"><@s.text name="label.assignedto"/></label>
						<@s.select name="criteria.assignedUser" emptyOption="true" >
							<#include "/templates/html/common/_assignedToDropDown.ftl"/>
						</@s.select>
					</div>	
				</#if>
				<div class="infoSet">
					<label for="criteria.location"><@s.text name="label.location"/></label>
					<@n4.location name="criteria.location" id="location" nodesList=helper.predefinedLocationTree fullName="${helper.getFullNameOfLocation(criteria.location)}"/>
				</div>	
				
				<#include "_ownershipFilters.ftl"/>
				
				<div class="infoSet">
					<label></label>
				</div>	
			</div>
			
			<div class="fieldGroup fieldGroupGap clearLeft">	
				<h2><@s.text name="label.orderdetails"/></h2>
				<div class="infoSet">
					<label for="criteria.orderNumber"><@s.text name="label.onumber"/></label>
					<@s.textfield name="criteria.orderNumber" />
				</div>
				<div class="infoSet">
					<label for="criteria.purchaseorder"><@s.text name="label.purchaseorder"/></label>
					<@s.textfield name="criteria.purchaseOrder" />
				</div>
			</div>
			
			<div class="fieldGroup">
				<h2><@s.text name="label.asset_details"/></h2>

                <div class="infoSet">
                    <label for="criteria.assetTypeGroup"><@s.text name="label.asset_type_group"/></label>
                    <@s.select id="assetTypeGroup" name="criteria.assetTypeGroup" headerKey="" headerValue="${action.getText('label.all')}" onchange="updateAssetTypes(this)" list="assetTypeGroups" listKey="id" listValue="name"/>
                </div>

                <div class="infoSet">
                    <label for="criteria.assetType"><@s.text name="label.assettype"/></label>
                    <@s.select cssClass="assetTypeSelect" id="assetType" name="criteria.assetType" emptyOption="true" list="assetTypes" listKey="id" listValue="name" onchange="assetTypeChanged(this)" />
                </div>
				
				<div class="infoSet">
					<label for="criteria.assetStatus"><@s.text name="label.assetstatus"/></label>
					<@s.select  name="criteria.assetStatus" list="assetStatuses" listKey="id" listValue="name" emptyOption="true" />
				</div>
			</div>
	
			
			<#include "../customizableSearch/_selectColumns.ftl"/>
		
		<div class="formAction enlargedInput">
			<div class="centerContents">
				<@s.submit key="label.Run"/>
				<@s.text name="label.or"/>
				<a href="javascript:void(0);" onclick="clearForm($('reportForm')); return false;"><@s.text name="label.clearform"/></a>
			</div>
		</div>
	</@s.form >
</div>

<#include '../customizableSearch/_eventTypeScript.ftl'/>
<#include '../customizableSearch/_assetTypeScript.ftl'/>