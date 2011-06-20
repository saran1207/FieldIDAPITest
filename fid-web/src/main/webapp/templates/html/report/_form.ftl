<head>
	<#include "/templates/html/common/_columnView.ftl"/>
	<#include "/templates/html/common/_datetimepicker.ftl"/>
	<@n4.includeStyle type="page" href="search" />
	<@n4.includeStyle type="page" href="downloads" />
	
	<script type="text/javascript" src="<@s.url value="/javascript/search.js"/>"></script>
	<script type="text/javascript">
		updatingColumnText = '<@s.text name="label.availablecolumnsupdating"/>'; 
		dynamicColumnUrl = '<@s.url action="reportDynamicColumns" namespace="/ajax" />';
	</script>	
	<#include "/templates/html/common/_orgPicker.ftl"/>
</head>
${action.setPageType('report', 'show')!}
<div class="pageSection searchPageSection" id="criteriaForm">
	<div class="headerWithFootnote">
		<#if listPage?exists>
		<h2 class="decoratedHeaderAlternative">
			<a href="javascript:void(0);" id="expandSection_reportForm" onclick="openSection('reportForm', 'expandSection_reportForm', 'collapseSection_reportForm');return false" ><img src="<@s.url value="/images/expandLarge.gif" />" /></a>
			<a href="javascript:void(0);" id="collapseSection_reportForm" onclick="closeSection('reportForm', 'collapseSection_reportForm', 'expandSection_reportForm');return false" style="display:none;"><img src="<@s.url value="/images/collapseLarge.gif" />" /></a>
			<span class="headerText">
				<@s.text name="label.reportcriteria"/>
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
	<@s.form action="report!createSearch" id="reportForm" cssClass="crudForm searchForm" theme="fieldid" cssStyle="${listPage?exists?string('display:none;','')}" >
		
		<#include "../common/_formErrors.ftl"/>
		
		<div class="fieldGroup floatLeft fieldGroupGap">
			<h2><@s.text name="label.event_details"/></h2>
			<div class="infoSet">
				<label for="criteria.eventTypeGroup"><@s.text name="label.eventtypegroup"/></label>
				<@s.select id="eventTypeGroup" name="criteria.eventTypeGroup" list="eventTypeGroups" listKey="id" listValue="name" headerKey="" headerValue="${action.getText('label.all')}" onchange="updateEventTypes(this)"/>
			</div>
            <div class="infoSet">
                <label for="criteria.eventType"><@s.text name="label.eventtype"/></label>
                <@s.select cssClass="eventTypeSelect" id="eventType" name="criteria.eventType" list="eventTypes" listKey="id" listValue="name" emptyOption="true" onchange="eventTypeChanged(this)" />
            </div>
			<div class="infoSet">
				<label for="criteria.performedBy"><@s.text name="label.performed_by"/></label>
				<@s.select name="criteria.performedBy" list="examiners" listKey="id" listValue="name" emptyOption="true" />
			</div>
			<div class="infoSet">
				<label for="criteria.eventBook"><@s.text name="label.eventbook"/></label>
				<@s.select key="label.eventbook" name="criteria.eventBook" list="eventBooks" listKey="id" listValue="name" emptyOption="true" />
			</div>
			
			<#if securityGuard.projectsEnabled>
				<div class="infoSet">
					<label for="criteria.job"><@s.text name="label.job"/></label>
					<@s.select name="criteria.job" list="eventJobs" listKey="id" listValue="name" emptyOption="true" />
				</div>
			</#if>
			<div class="infoSet">
				<label for="criteria.status"><@s.text name="label.result"/></label>
				<@s.select name="criteria.status" emptyOption="true" list="statuses" listKey="id" listValue="%{getText(label)}"/>
			</div>
			<#if sessionUser.employeeUser || sessionUser.systemUser>
				<div class="infoSet">
					<label id="reducedLineHeightLabel" for="criteria.includeNetworkResults"><@s.text name="label.includesafetynetworkresults"/></label>
					<@s.checkbox name="criteria.includeNetworkResults" fieldValue="true"/>
				</div>
			<#else>
				<div class="infoSet">
					&nbsp;
				</div>
			</#if>
			<#if !securityGuard.projectsEnabled && securityGuard.assignedToEnabled>	
				<div class="infoSet">
					&nbsp;
				</div>
			</#if>
		</div>
		
		<div class="fieldGroup floatRight">
			<h2><@s.text name="label.asset_details"/></h2>
			<div class="infoSet">
				<label for="criteria.assetStatus"><@s.text name="label.assetstatus"/></label>
				<@s.select name="criteria.assetStatus" list="assetStatuses" listKey="id" listValue="name" emptyOption="true" />
			</div>
					
            <div class="infoSet">
                <label for="criteria.assetTypeGroup"><@s.text name="label.asset_type_group"/></label>
                <@s.select id="assetTypeGroup" name="criteria.assetTypeGroup" headerKey="" headerValue="${action.getText('label.all')}" onchange="updateAssetTypes(this)" list="assetTypeGroups" listKey="id" listValue="name"/>
            </div>

            <div class="infoSet">
                <label for="criteria.assetType"><@s.text name="label.assettype"/></label>
                <@s.select cssClass="assetTypeSelect" id="assetType" name="criteria.assetType" emptyOption="true" list="assetTypes" listKey="id" listValue="name" onchange="assetTypeChanged(this)"/>
            </div>
		</div>
			
		<div class="fieldGroup floatRight">
			<h2><@s.text name="label.ownership"/></h2>
			<#if securityGuard.assignedToEnabled >
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
			<div class="infoSet">
				<label for="owner"><@s.text name="label.owner"/></label>
				<@n4.orgPicker name="owner"/>
			</div>
			<#if !securityGuard.assignedToEnabled && securityGuard.projectsEnabled>
				<div class="infoSet">
					&nbsp;
				</div>
			</#if>
		</div>
	
		<div class="fieldGroup fieldGroupGap floatLeft">
			<h2><@s.text name="label.eventdate"/></h2>
			<div class="container">
				<div class="infoSet">
					<label for="fromDate"><@s.text name="label.fdate"/></label>
					<@s.textfield  name="fromDate" cssClass="datepicker"/>
				</div>
				<div class="infoSet">
					<label for="toDate"><@s.text name="label.tdate"/></label>
					<@s.textfield  name="toDate" cssClass="datepicker"/>
				</div>
			</div>
		</div>
	
		<div class="fieldGroup floatRight">
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
		
		<div class="fieldGroup fieldGroupGap floatLeft">
			<h2><@s.text name="label.orderdetails"/></h2>
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
					<label>&nbsp;</label>
				</div>
			</#if>
		</div>

		<#include "../customizableSearch/_selectColumns.ftl"/>
		
		<div class="formAction enlargedInput">
			<div class="centerContents">
				<@s.submit key="label.Run"/>
				<@s.text name="label.or"/>
				<#if listPage?exists>
					<@s.reset key="label.cancel" onclick="cancelReportChanges(this.form); return false;"/>
				<#else>
					<a href="javascript:void(0);" onclick="clearForm($('reportForm')); return false;"><@s.text name="label.clearform"/></a>
				</#if>
			</div>
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

<#include '../customizableSearch/_eventTypeScript.ftl'/>
<#include '../customizableSearch/_assetTypeScript.ftl'/>
