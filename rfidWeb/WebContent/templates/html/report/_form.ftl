<head>
	<#include "/templates/html/common/_calendar.ftl"/>
	<link rel="stylesheet" type="text/css" href="<@s.url value="/style/pageStyles/search.css"/>" />
	<script type="text/javascript" src="<@s.url value="/javascript/search.js"/>"></script>
	<script type="text/javascript">
		updatingColumnText = '<@s.text name="label.availablecolumnsupdating"/>'; 
		dynamicColumnUrl = '<@s.url action="reportDynamicColumns" namespace="/ajax" />';
	</script>	
	<#include "/templates/html/common/_orgPicker.ftl"/>
	<style type="text/css">
		
		#criteriaForm {
			float:left;
			
		}
	</style>
</head>
<div class="pageSection" id="criteriaForm">
	<h2>
		<#if listPage?exists>
			<a href="javascript:void(0);" id="expandSection_reportForm" onclick="openSection('reportForm', 'expandSection_reportForm', 'collapseSection_reportForm');return false" ><img src="<@s.url value="/images/expandLarge.gif" />" /></a>
			<a href="javascript:void(0);" id="collapseSection_reportForm" onclick="closeSection('reportForm', 'collapseSection_reportForm', 'expandSection_reportForm');return false" style="display:none;"><img src="<@s.url value="/images/collapseLarge.gif" />" /></a>
		</#if>
		<@s.text name="label.reportcriteria"/>
	</h2>
	<@s.form action="report!createSearch" id="reportForm" cssClass="crudForm twoColumns" theme="fieldid" cssStyle="${listPage?exists?string('display:none;','')}" >
		<#include "../common/_formErrors.ftl"/>
		<div class="sectionContent" >
			<div class="infoSet">
				<label for="criteria.rfidNumber"><@s.text name="label.rfidnumber"/></label> 
				<@s.textfield name="criteria.rfidNumber"/>
			</div>
			<div class="infoSet">
				<label for="criteria.serialNumber"><@s.text name="${sessionUser.serialNumberLabel}"/></label> 
				<@s.textfield name="criteria.serialNumber"/>
			</div>
			<div class="infoSet">
				<label for="criteria.inspectionTypeGroup"><@s.text name="label.eventtypegroup"/></label>
				<@s.select name="criteria.inspectionTypeGroup" list="inspectionTypes" listKey="id" listValue="name" emptyOption="true" />
			</div>
			<div class="infoSet">
				<label for="criteria.performedBy"><@s.text name="label.performed_by"/></label>
				<@s.select name="criteria.performedBy" list="examiners" listKey="id" listValue="name" emptyOption="true" />
			</div>
			<div class="infoSet">
				<label for="criteria.orderNumber"><@s.text name="label.onumber"/></label>
				<@s.textfield name="criteria.orderNumber" />
			</div>
			<div class="infoSet">
				<label for="criteria.purchaseorder"><@s.text name="label.purchaseorder"/></label>
				<@s.textfield name="criteria.purchaseOrder" />
				
			</div>	
			<#if securityGuard.assignedToEnabled >
				<div class="infoSet">
					<label for="criteria.assignedUser"><@s.text name="label.assignedto"/></label>
					<@s.select name="criteria.assignedUser" list="employees" listKey="id" listValue="displayName" emptyOption="true" />
				</div>
			</#if>
			
			
			<div class="infoSet">
				<label for="criteria.inspectionBook"><@s.text name="label.inspectionbook"/></label>
				<@s.select key="label.inspectionbook" name="criteria.inspectionBook" list="inspectionBooks" listKey="id" listValue="name" emptyOption="true" />
			</div>
			<div class="infoSet">
				<label for="criteria.referenceNumber"><@s.text name="label.referencenumber"/></label>
				<@s.textfield name="criteria.referenceNumber"/>
			</div>
			
			<#include "../customizableSearch/_productTypeSelect.ftl"/>
			
			<div class="infoSet">
				<label for="criteria.productStatus"><@s.text name="label.productstatus"/></label>
				<@s.select  name="criteria.productStatus" list="productStatus" listKey="uniqueID" listValue="name" emptyOption="true" />
			</div>
		
			<#if securityGuard.projectsEnabled>
				<div class="infoSet">
					<label for="criteria.job"><@s.text name="label.job"/></label>
					<@s.select name="criteria.job" list="eventJobs" listKey="id" listValue="name" emptyOption="true" />
				</div>
			</#if>
			<div class="infoSet">
				<label for="criteria.location"><@s.text name="label.location"/></label>
				<@s.textfield name="criteria.location"/>
			</div>	
			<div class="infoSet">
				<label for="owner"><@s.text name="label.owner"/></label>
				<@n4.orgPicker name="owner"/>
			</div>
			
			<#if sessionUser.employeeUser>
			<div class="infoSet">
				<label for="criteria.includeNetworkResults"><@s.text name="label.includesafetynetworkresults"/></label>
				<@s.checkbox name="criteria.includeNetworkResults" fieldValue="true"/>
			</div>
			</#if>
			<div class="infoSet">
				<label for="criteria.status"><@s.text name="label.result"/></label>
				<@s.select name="criteria.status" emptyOption="true" list="statuses" listKey="id" listValue="%{getText(label)}"/>
			</div>
			
			<div class="container">
				<div class="infoSet">
					<label for="fromDate"><@s.text name="label.fdate"/></label>
					<@s.datetimepicker  name="fromDate" />
				</div>
				<div class="infoSet">
					<label for="toDate"><@s.text name="label.tdate"/></label>
					<@s.datetimepicker  name="toDate" />
				</div>
			</div>
		</div>
		<#include "../customizableSearch/_selectColumns.ftl"/>
		
		<div class="formAction">
			<@s.reset key="label.clearform" onclick="clearForm(this.form); return false;"/>
			<#if listPage?exists>
				<@s.reset key="label.cancel" onclick="cancelReportChanges(this.form); return false;"/>
			</#if>
			<@s.submit key="label.Run"/>
		</div>
	</@s.form >
</div>
<#if listPage?exists>
	<script type="text/javascript">
		var elements = $('reportForm').getElements();
		for (var i = 0; i < elements.size(); i++) {
			Element.extend(elements[i]).observe('change', function(event) { changedForm(); });
		}
	</script>
</#if>