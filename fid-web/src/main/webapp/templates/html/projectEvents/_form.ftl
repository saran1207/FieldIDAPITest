<head>
	<#include "/templates/html/common/_calendar.ftl"/>
	<@n4.includeStyle type="page" href="search" />
	<script type="text/javascript" src="<@s.url value="/javascript/search.js"/>"></script>
	<script type="text/javascript">
		function assetTypeChanged(assetType) {
			return null;
		}
	</script>
</head>

<div class="pageSection searchPageSection" id="criteriaForm">
	<@s.form action="startAssignSchedulesToJob!createSearch" id="reportForm" cssClass="crudForm searchForm" theme="fieldid">
		<@s.hidden name="criteria.jobAndNullId"/>
		<@s.hidden name="criteria.searchId"/>
			
			<div class="fieldGroup">
				<h2><@s.text name="label.schedule_details"/></h2>
			
				<div class="infoSet">
					<label for="criteria.status"><@s.text name="label.schedulestatus"/></label> 
					<@s.select name="criteria.status" list="scheduleStatuses" listKey="name" listValue="%{getText(label)}"/>
				</div>
				<div class="infoSet">
					<label for="criteria.inspectionType"><@s.text name="label.eventtypegroup"/></label>
					<@s.select name="criteria.inspectionType" list="inspectionTypes" listKey="id" listValue="name" emptyOption="true"/>
				</div>
			</div>
			
			<div class="fieldGroup">
				<h2><@s.text name="label.scheduleddate"/></h2>
				<#include "../schedule/_dateRange.ftl"/>
			</div>
			
			<div class="fieldGroup ">
				<h2><@s.text name="label.identifiers"/></h2>
				<div class="infoSet">
					<label for="criteria.serialNumber"><@s.text name="${sessionUser.serialNumberLabel}"/></label> 
					<@s.textfield name="criteria.serialNumber"/>
				</div>
			</div>
			
			<div class="fieldGroup">
				<h2><@s.text name="label.ownership"/></h2>
				<#include "../schedule/_ownershipFilters.ftl"/>
			</div>
	
			<div class="fieldGroup ">	
				<h2><@s.text name="label.asset_details"/></h2>
				
				<#include "../customizableSearch/_assetTypeSelect.ftl"/>
					
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

