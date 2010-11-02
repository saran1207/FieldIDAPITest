<head>
	<#include "/templates/html/common/_orgPicker.ftl"/>
	<#include "/templates/html/common/_columnView.ftl"/>
</head>

<div class="assetFormGroup">
	<div class="infoSet enlarged">
		<label for="assetTypeId" class="label"><@s.text name="label.assettype"/></label>
		<@s.select id="assetType" name="assetTypeId" onchange="updateAssetType(this)">
			<#include "/templates/html/common/_assetTypeOptions.ftl"/>
		</@s.select>
		<span class="fieldHolder updating" id="assetTypeIndicator">
			<img src="<@s.url value="/images/indicator_mozilla_blu.gif" />" alt="<@s.text name="updating"/>" />
		</span>	
	</div>
</div>

<#include "_assetSerialAndRfidForm.ftl"/>

<div class="assetFormGroup">
	<h2><@s.text name="label.owner"/></h2>
	<#if securityGuard.assignedToEnabled >
		<div class="infoSet">
			<label class="label" for="assigneduser"><@s.text name="label.assignedto"/></label>
			<#if !parentAsset?exists >
				<@s.select  name="assignedUser" list="employees" listKey="id" listValue="displayName" headerKey="0" headerValue="${action.getText('label.unassigned')}" />
			<#else>
				<span class="fieldHolder" id="assignedUser">${(asset.assignedUser.userLabel)!}</span>
			</#if>
		</div>
	</#if>
	
	<div class="infoSet">
		<label class="label" for="owner"><@s.text name="label.owner"/></label>
		<#if !parentAsset?exists >
			<@n4.orgPicker name="owner" theme="fieldid" required="true"/>
		<#else>
			<span class="fieldHolder" id="owner">${(asset.owner.name?html)!}</span>
		</#if>
	</div>

	<div class="infoSet">
		<label class="label" for="asset.location"><@s.text name="label.location"/></label>
		<#if !parentAsset?exists >
			<div class="fieldHolder">
				<@n4.location name="assetWebModel.location" id="location" nodesList=helper.predefinedLocationTree fullName="${helper.getFullNameOfLocation(assetWebModel.location)}"  theme="simple"/>
			</div>
		<#else>
			<span class="fieldHolder" id="advancedLocation">${(helper.getFullNameOfLocation(assetWebModel.location))?html}</span>
		</#if>
	</div>
</div>

<div class="assetFormGroup">
	<h2><@s.text name="label.orderdetails"/></h2>
	
	<div class="infoSet">
		<label for="purchaseOrder" class="label"><@s.text name="label.purchaseorder"/></label>
		<@s.textfield  name="purchaseOrder" />	
	</div>
	
	<#if !securityGuard.integrationEnabled>
		<div class="infoSet">
			<label for="nonIntegrationOrderNumber" class="label"><@s.text name="label.ordernumber"/></label>
			<@s.textfield id="nonIntegrationOrderNumber" name="nonIntegrationOrderNumber" />	
		</div>
	</#if>
</div>

<div class="assetFormGroup">
	<h2><@s.text name="label.identifieddate"/></h2>
	<div class="infoSet">
		<label for="" class="label"><@s.text name="label.identified"/> <#include "../common/_requiredMarker.ftl"/></label>
		<@s.datetimepicker id="identified" name="identified" type="dateTime"/>
	</div>
</div>

<#include "_infoOptions.ftl">

<#include "_fileAttachment.ftl"/>

<#if userSecurityGuard.allowedManageSafetyNetwork == true && publishedState?exists>
	<div class="infoSet">
		<label for="publishedState" class="label"><@s.text name="label.publishedstateselector"/></label>
		<@s.select name="publishedState" list="publishedStates" listKey="id" listValue="name" />
	</div>
</#if>