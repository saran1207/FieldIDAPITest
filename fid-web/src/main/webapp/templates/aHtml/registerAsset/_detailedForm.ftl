<div id="extraDetails" class="detailedForm" style="display:none">
	<div class="formTitle">
		<h3><@s.text name="label.indentifiers" /></h3>
	</div>
	<div class="infoSet">
		<label for="identified" class="label"><@s.text name="label.identifieddate"/> <#include "/templates/html/common/_requiredMarker.ftl"/></label>
		<@s.textfield id="identified" name="identified" cssClass="datetimepicker"/>
	</div>
	
	<div class="infoSet">
		<label for="assetStatus" class="label"><@s.text name="label.assetstatus"/></label>
			<#if !parentAsset?exists >
				<@s.select name="assetStatus" list="assetStatuses" listKey="Id" listValue="name" emptyOption="true"  />
			<#else>
				<span class="fieldHolder" id="assetStatus">${(asset.assetStatus.name?html)!}</span>
			</#if>		
	</div>
	
	<div class="formTitle">
		<h3><@s.text name="label.ownership" /></h3>
	</div>
	<div class="infoSet">
		<label for="owner" class="label"><@s.text name="label.owner"/> <#include "/templates/html/common/_requiredMarker.ftl"/></label>
		<#if !parentAsset?exists >
			<@n4.orgPicker name="owner" theme="fieldid" id="ownerId"/>
		<#else>
			<span class="fieldHolder" id="owner">${(asset.owner.name?html)!}</span>
		</#if>
	</div>
	<#if securityGuard.assignedToEnabled >
		<div class="infoSet">
			<label for="assignedUser" class="label"><@s.text name="label.assignedto"/></label>
			<#if !parentAsset?exists >
				<@s.select name="assignedUser"  headerKey="0" headerValue="${action.getText('label.unassigned')}" >
						<#include "/templates/html/common/_assignedToDropDown.ftl"/>
				</@s.select>
			<#else>
				<span class="fieldHolder" id="assignedUser">${(asset.assignedUser.userLabel)!}</span>
			</#if>
		</div>
	</#if>
	<div class="formTitle">
		<h3><@s.text name="label.orderdetails" /></h3>
	</div>				
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
	<div class="formTitle">
		<h3><@s.text name="label.attributes"/></h3>
	</div>
	<#include "_infoOptions.ftl">

	<#if !linkedAsset.orderedInfoOptionList.isEmpty()>
		<#if autoAttributeCriteria?exists>
		<div id="suggestedAttributes">
		<#else>
		<div id="suggestedAttributes" style="display:none">
		</#if>
		   <h3> ${linkedAsset.tenant.name} <@s.text name="label.attributes"/></h3>
		   <p class="gray"><@s.text name="label.registerasset.linkedattributes"/></p>
			<#list linkedAsset.orderedInfoOptionList as infoOption >
				<div class="infoSet">
					<label class="label">${infoOption.infoField.name} <#if infoOption.infoField.retired >(<@s.text name="label.retired"/>)</#if> </label>
					<span class="fieldHolder" infoFieldName="${infoOption.infoField.name?j_string}">
						<p class="blue">${infoOption.name}</p>
					</span>
				</div>
			</#list>
		</div>
	</#if>
		
	<div class="infoSet">
		<label for="comments" class="label"><@s.text name="label.comments"/></label>
		<span class="fieldHolder">
			<@s.select id="commentTemplateSelection" name="commentTemplate" list="commentTemplates" listKey="id" listValue="displayName" emptyOption="true" onchange="changeComments(this)" theme="fieldidSimple"/><br/>
			<@s.textarea id="comments"  name="comments" theme="fieldidSimple"/>
		</span>
	</div>
	

		
</div>
