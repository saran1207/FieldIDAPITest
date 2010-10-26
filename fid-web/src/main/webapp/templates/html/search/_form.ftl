<head>
	<#include "/templates/html/common/_calendar.ftl"/>
	<#include "/templates/html/common/_columnView.ftl"/>
	<@n4.includeStyle type="page" href="search" />
	<script type="text/javascript" src="<@s.url value="/javascript/search.js"/>"></script>
	<script type="text/javascript">
		updatingColumnText = '<@s.text name="label.availablecolumnsupdating"/>'; 
		dynamicColumnUrl = '<@s.url action="searchDynamicColumns" namespace="/ajax" />';
	</script>
	
	<#include "/templates/html/common/_orgPicker.ftl"/>
</head>

<div class="pageSection searchPageSection" id="criteriaForm">
	<div class="headerWithFootnote">
		<#if listPage?exists>
			<a href="javascript:void(0);" id="expandSection_reportForm" onclick="openSection('reportForm', 'expandSection_reportForm', 'collapseSection_reportForm');return false" ><img src="<@s.url value="/images/expandLarge.gif" />" /></a>
			<a href="javascript:void(0);" id="collapseSection_reportForm" onclick="closeSection('reportForm', 'collapseSection_reportForm', 'expandSection_reportForm');return false" style="display:none;"><img src="<@s.url value="/images/collapseLarge.gif" />" /></a>
		</#if>
		<span class="headerText">
			<img src="<@s.url value="/images/tip-icon.png" />"/>&nbsp;<@s.text name="label.searchcriteria"/>
		</span>
		<br>
		<span class="footnoteText">
			<@s.text name="label.wildcard_explanation"/>
		</span>
	</div>
	
	<@s.form action="search!createSearch" id="reportForm" cssClass="crudForm searchForm" theme="fieldid" cssStyle="${listPage?exists?string('display:none;','')}" >
		<#include "../common/_formErrors.ftl"/>
		
		<div class="fieldGroup">
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
			<h2><@s.text name="label.asset_details"/></h2>
			<div class="infoSet">
				<label for="criteria.assetStatus"><@s.text name="label.assetstatus"/></label>
				<@s.select  name="criteria.assetStatus" list="assetStatuses" listKey="uniqueID" listValue="name" emptyOption="true" />
			</div>

			<#include "../customizableSearch/_assetTypeSelect.ftl"/>
		</div>		
		<div class="fieldGroup">
			<h2><@s.text name="label.ownership"/></h2>
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
				<label for="owner"><@s.text name="label.owner" /></label>
				<@n4.orgPicker name="owner"/>
			</div>
		</div>		
		<div class="fieldGroup">
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
		
		<div class="fieldGroup clearLeft">
			<h2><@s.text name="label.identifieddate"/></h2>
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
			<@s.submit key="label.Run"/>
		</div>
	</@s.form >
</div>
