<head>
	<#include "/templates/html/common/_orgPicker.ftl"/>
	<#include "/templates/html/common/_columnView.ftl"/>
</head>
<div class="assetFormGroup">
	<h2><@s.text name="label.owner"/></h2>
	<#if securityGuard.assignedToEnabled >
		<div class="infoSet reducedPaddingInfoSet">
			<label class="label" for="assigneduser"><@s.text name="label.assignedto"/></label>
			<#if !parentAsset?exists>
				<@s.select id="assignedToSelectBox" name="assignedUser" headerKey="0" headerValue="${action.getText('label.unassigned')}" >
					<#include "/templates/html/common/_assignedToDropDown.ftl"/>
				</@s.select>
				<a href="#" class="assignToMeLink" onclick="setAssignedToAsCurrentUser(${sessionUser.id}); return false;" ><@s.text name="label.assign_to_me"/></a>
			<#else>
				<span class="fieldHolder" id="assignedUser">${(asset.assignedUser.userLabel)!}</span>
			</#if>
			
		</div>
	</#if>
	
	<div class="infoSet">
		<label class="label" for="owner"><@s.text name="label.owner"/></label>
        <#if bulkRegister?exists>
            <div style="display:block;">
                <input type="checkbox" name="useOwnerFromAssets" onchange="$('ownerId_orgName').disabled = this.checked;"/> <@s.text name="label.create_owners_from_assets"/>
            </div>
        </#if>
		<#if !parentAsset?exists >
			<@n4.orgPicker name="owner" theme="fieldid" required="true" id="ownerId"/>
		<#else>
			<span class="fieldHolder" id="owner">${(asset.owner.name?html)!}</span>
		</#if>
	</div>

    <#if !bulkRegister?exists>
        <div class="infoSet">
            <label class="label" for="asset.location"><@s.text name="label.location"/></label>
            <#if !parentAsset?exists >
                <span class="locationTree">
                    <@n4.location name="assetWebModel.location" id="location" nodesList=helper.predefinedLocationTree fullName="${helper.getFullNameOfLocation(assetWebModel.location)}"  theme="simple"/>
                </span>
            <#else>
                <span class="fieldHolder" id="advancedLocation">${(helper.getFullNameOfLocation(assetWebModel.location))?html}</span>
            </#if>
        </div>
    </#if>
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
	<div class="infoSet increasedWidthFieldHolder">
		<label for="" class="label"><#include "../common/_requiredMarker.ftl"/><@s.text name="label.identified"/></label>
        <#if bulkRegister?exists>
            <div style="display:block;">
                <input type="checkbox" name="useDatesFromAssets" onchange="$('identified').disabled = this.checked;"/> <@s.text name="label.use_dates_from_assets"/>
            </div>
        </#if>
		<@s.datetimepicker id="identified" name="identified" type="dateTime"/>
	</div>
</div>

<div class="assetFormGroup">
	<h2><@s.text name="label.asset_details"/></h2>
	<div class="infoSet reducedPaddingInfoSet">
		<label for="assetStatus" class="label"><@s.text name="label.assetstatus"/></label>
		<#if !parentAsset?exists >
			<@s.select name="assetStatus" list="assetStatuses" listKey="id" listValue="name" emptyOption="true"  />
		<#else>
			<span class="fieldHolder" id="assetStatus">${(asset.assetStatus.name?html)!}</span>
		</#if>		
	</div>

	<#include "_infoOptions.ftl">
	
	<div class="infoSet reducedPaddingInfoSet">
		<label for="comments" class="label"><@s.text name="label.comments"/></label>
		<span class="fieldHolder">
			<@s.select id="commentTemplateSelection" name="commentTemplate" list="commentTemplates" listKey="id" listValue="displayName" emptyOption="true" onchange="changeComments(this)" theme="fieldidSimple"/><br/>
			<@s.textarea id="comments"  name="comments" theme="fieldidSimple"/>
		</span>
	</div>
	
	<#if userSecurityGuard.allowedManageSafetyNetwork == true && publishedState?exists>
		<div class="infoSet reducedPaddingInfoSet">
			<label for="publishedState" class="label"><@s.text name="label.visibility"/></label>
			<@s.select name="publishedState" list="publishedStates" listKey="id" listValue="name" />
		</div>
	</#if>
</div>

<#if isAddForm>
	<#include "/templates/html/eventCrud/_schedules.ftl" />
</#if>

<#include "_fileAttachment.ftl"/>

