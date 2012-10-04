<head>
	<#include "/templates/html/common/_datetimepicker.ftl"/>
	<#include "/templates/html/common/_columnView.ftl"/>
	<@n4.includeStyle type="page" href="search" />
    <@n4.includeScript src="search.js"/>
	<script type="text/javascript">
		function assetTypeChanged(assetType) {
			return null;
		}
        getLocationPickerUrl = '<@s.url namespace="ajax" action="updateJobEventLocation"/>';
	</script>
</head>

<div class="pageSection searchPageSection" id="criteriaForm">
	<@s.form action="startAssignSchedulesToJob!createSearch" id="reportForm" cssClass="crudForm searchForm" theme="fieldid">
		<@s.hidden name="criteria.jobAndNullId"/>
		<@s.hidden name="criteria.searchId"/>
			
			<div class="fieldGroup fieldGroupGap">
				<h2><@s.text name="label.schedule_details"/></h2>
			
				<div class="infoSet">
					<label for="criteria.status"><@s.text name="label.schedulestatus"/></label> 
					<@s.select name="criteria.status" list="scheduleStatuses" listKey="name" listValue="%{getText(label)}"/>
				</div>
				<div class="infoSet">
					<label for="criteria.eventType"><@s.text name="label.eventtypegroup"/></label>
					<@s.select name="criteria.eventType" list="eventTypes" listKey="id" listValue="name" emptyOption="true"/>
				</div>
			</div>
			
			<div class="fieldGroup">
				<h2><@s.text name="label.scheduleddate"/></h2>
				<#include "../schedule/_dateRange.ftl"/>
			</div>
			
			<div class="fieldGroup fieldGroupGap">
				<h2><@s.text name="label.identifiers"/></h2>
				<div class="infoSet">
					<label for="criteria.identifier">${identifierLabel}</label>
					<@s.textfield name="criteria.identifier"/>
				</div>
			</div>
			
			<div class="fieldGroup owners">
				<h2><@s.text name="label.ownership"/></h2>
				<div class="infoSet">
					<label for="criteria.location"><@s.text name="label.location"/></label>
                    <span class="locationTree">
					    <@n4.location name="criteria.location" id="location" nodesList=predefinedLocationTree fullName="${helper.getFullNameOfLocation(criteria.location)}" theme="fieldid"/>
                    </span>
				</div>

				<#include "../schedule/_ownershipFilters.ftl"/>
			</div>
		
	
			<div class="fieldGroup ">	
				<h2><@s.text name="label.asset_details"/></h2>
				
                <div class="infoSet">
                    <label for="criteria.assetTypeGroup"><@s.text name="label.asset_type_group"/></label>
                    <@s.select id="assetTypeGroup" name="criteria.assetTypeGroup" headerKey="" headerValue="${action.getText('label.all')}" onchange="updateAssetTypes(this)" list="assetTypeGroups" listKey="id" listValue="name"/>
                </div>

                <div class="infoSet">
                    <label for="criteria.assetType"><@s.text name="label.assettype"/></label>
                    <@s.select cssClass="assetTypeSelect" id="assetType" name="criteria.assetType" emptyOption="true" list="assetTypes" listKey="id" listValue="name"/>
                </div>
					
				<div class="infoSet">
					<label for="criteria.assetStatus"><@s.text name="label.assetstatus"/></label>
					<@s.select  name="criteria.assetStatus" list="assetStatuses" listKey="id" listValue="name" emptyOption="true" />
				</div>
			</div>
				
		<div class="formAction">
			<div class="centerContents">
				<@s.submit key="label.Run"/>
				<@s.text name="label.or"/>
				<a href="javascript:void(0);" onclick="clearForm($('reportForm')); return false;"><@s.text name="label.clearform"/></a>
			</div>
		</div>
	</@s.form >
</div>

<#include '../customizableSearch/_assetTypeScript.ftl'/>