<head>
	<#include "/templates/html/common/_orgPicker.ftl"/>
</head>

	
<#if userSecurityGuard.allowedManageSafetyNetwork == true >
	<div class="infoSet infoBlock">
		<label for="publishedState" class="label"><@s.text name="label.publishedstateselector"/></label>
		<@s.select name="publishedState" list="publishedStates" listKey="id" listValue="name" />		
	</div>
</#if>

<#if securityGuard.jobSitesEnabled >
	<div class="infoSet infoBlock">
		<label class="label" for="assigneduser"><@s.text name="label.assignedto"/></label>
		<#if !parentProduct?exists >
			<@s.select  name="assignedUser" list="employees" listKey="id" listValue="name" emptyOption="true" />
		<#else>
			<span class="fieldHolder" id="assigneduser">
					${(product.assignedUser.userLabel)!}
			</span>
		</#if>
	</div>
</#if>
<div class="infoSet infoBlock">
	<label class="label" for="owner"><@s.text name="label.owner"/></label>
	<#if !parentProduct?exists >
		<@n4.orgPicker name="owner" theme="fieldid" required="true"/>
	<#else>
		<span class="fieldHolder" id="owner">${(product.owner.name?html)!}</span>
	</#if>
</div>



<div class="infoSet infoBlock">
	<label class="label" for="location"><@s.text name="label.location"/></label>
	<#if !parentProduct?exists >
		<@s.textfield id="location" name="location" />
	<#else>
		<span class="fieldHolder" id="location">${(product.location?html)!}</span>
	</#if>
</div>
<div class="infoSet infoBlock">
	<label for="productStatus" class="label"><@s.text name="label.productstatus"/></label>
	<@s.select name="productStatus" list="productStatuses" listKey="uniqueID" listValue="name" emptyOption="true"  />		
</div>

<div class="infoSet infoBlock">
	<label for="purchaseOrder" class="label"><@s.text name="label.purchaseorder"/></label>
	<@s.textfield  name="purchaseOrder" />	
</div>

<div class="infoSet infoBlock">
	<label for="" class="label"><@s.text name="label.identified"/> <#include "../common/_requiredMarker.ftl"/></label>
	<@s.datetimepicker id="identified" name="identified" type="dateTime"/>
</div>

<#if !securityGuard.integrationEnabled>
	<div class="infoSet infoBlock">
		<label for="nonIntegrationOrderNumber" class="label"><@s.text name="label.ordernumber"/></label>
		<@s.textfield id="nonIntegrationOrderNumber" name="nonIntegrationOrderNumber" />	
	</div>

	
</#if>

<@s.iterator value="extentions" id="extention" status="stat" >
	<div class="infoSet infoBlock">
		<@s.hidden name="productExtentionValues[${stat.index}].extensionId" />
		<@s.hidden name="productExtentionValues[${stat.index}].uniqueID" />
		<label for="productExtentionValues[${stat.index}].value" class="label">${extentions[stat.index].extensionLabel?html}</label>
		<@s.textfield key="${extentions[stat.index].extensionLabel}" name="productExtentionValues[${stat.index}].value" />
	</div>
</@s.iterator>
<div class="infoSet">
	<label for="productTypeId" class="label"><@s.text name="label.producttype"/></label>
	<@s.select id="productType" name="productTypeId" onchange="updateProductType(this)">
		<#include "/templates/html/common/_productTypeOptions.ftl"/>
	</@s.select>
	<span class="fieldHolder updating" id="productTypeIndicator">
		<img src="<@s.url value="/images/indicator_mozilla_blu.gif" />" alt="<@s.text name="updating"/>" />
	</span>	
</div>
	

<#include "_infoOptions.ftl">

<div class="infoSet">
	<label for="comments" class="label"><@s.text name="label.comments"/></label>
	<span class="fieldHolder">
		<@s.select id="commentTemplateSelection" name="commentTemplate" list="commentTemplates" listKey="id" listValue="name" emptyOption="true" onchange="changeComments(this)" theme="fieldidSimple"/><br/>
		<@s.textarea id="comments"  name="comments" theme="fieldidSimple"/>
	</span>
</div>

<#include "_fileAttachment.ftl"/>
