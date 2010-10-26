<head>
	<#include "/templates/html/common/_calendar.ftl"/>
	<link rel="stylesheet" type="text/css" href="<@s.url value="/style/pageStyles/search.css"/>" />
	<script type="text/javascript" src="<@s.url value="/javascript/search.js"/>"></script>
	<script type="text/javascript">
		function assetTypeChanged(assetType) {
			return null;
		}
	</script>
</head>

<div class="pageSection" id="criteriaForm">
	<h2>
		<@s.text name="label.schedulecriteria"/>
	</h2>
	
	<@s.form action="startAssignSchedulesToJob!createSearch" id="reportForm" cssClass="crudForm twoColumns" theme="fieldid">
		<@s.hidden name="criteria.jobAndNullId"/>
		<@s.hidden name="criteria.searchId"/>
		<div class="sectionContent" >
			<div class="container">
				<div class="infoSet">
					<label for="criteria.status"><@s.text name="label.schedulestatus"/></label> 
					<@s.select name="criteria.status" list="scheduleStatuses" listKey="name" listValue="%{getText(label)}"/>
				</div>
			</div>
			
			<div class="infoSet">
				<label for="criteria.serialNumber"><@s.text name="${sessionUser.serialNumberLabel}"/></label> 
				<@s.textfield name="criteria.serialNumber"/>
			</div>
			
			<#include "../schedule/_ownershipFilters.ftl"/>
			
			<div class="infoSet">
				<label for="criteria.inspectionType"><@s.text name="label.eventtypegroup"/></label>
				<@s.select name="criteria.inspectionType" list="inspectionTypes" listKey="id" listValue="name" emptyOption="true"/>
			</div>
			
			
			<#include "../customizableSearch/_assetTypeSelect.ftl"/>
				
			<div class="infoSet">
				<label for="criteria.assetStatus"><@s.text name="label.assetstatus"/></label>
				<@s.select  name="criteria.assetStatus" list="assetStatuses" listKey="id" listValue="name" emptyOption="true" />
			</div>
			
			<#include "../schedule/_dateRange.ftl"/>
		</div>
				
		<div class="formAction">
			<@s.reset key="label.clearform" onclick="clearForm(this.form); return false;"/>
			<@s.submit key="label.Run"/>
		</div>
	</@s.form >
</div>

