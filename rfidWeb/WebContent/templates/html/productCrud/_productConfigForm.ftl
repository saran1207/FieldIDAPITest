<head>
	<#include "/templates/html/common/_orgPicker.ftl"/>
</head>
<#if securityGuard.jobSitesEnabled >
	<div class="formRowHolder">
		<#if !parentProduct?exists >
			<@s.select key="label.assignedto" name="assignedUser" list="employees" listKey="id" listValue="name" emptyOption="true" labelposition="left" />
		<#else>
			<div class="wwgrp" id="wwgrp_assigneduser">
				<span class="wwlbl" id="wwlbl_assigneduser">
					<label class="label" for="assigneduser"><@s.text name="label.assignedto"/>:</label>
				</span>
				<span class="wwctrl" id="assigneduser">
					${(product.assignedUser.userLabel)!}
				</span>
			</div>
		</#if>
	</div>
</#if>
<div class="formRowHolder">
	<label for="owner"><@s.text name="label.owner"/></label>
	<@n4.orgPicker name="owner"/>
</div>
<div class="formRowHolder">
	<#if !parentProduct?exists >
		<@s.textfield id="location" key="label.location" name="location" labelposition="left"/>
	<#else>
		<div class="wwgrp" id="wwgrp_location">
			<span class="wwlbl" id="wwlbl_location">
				<label class="label" for="location"><@s.text name="label.location"/>:</label>
			</span> 
			<span class="wwctrl" id="location">
				${(product.location)!}
			</span>
		</div>
	</#if>
	<@s.select key="label.productstatus" name="productStatus" list="productStatuses" listKey="uniqueID" listValue="name" emptyOption="true"  labelposition="left" />
</div>

<div class="formRowHolder">
		

	<@s.textfield key="label.purchaseorder" name="purchaseOrder" labelposition="left"/>
	<div id="wwgrp_identified" class="wwgrp">
		<#assign hasFieldErrors = fieldErrors?exists && fieldErrors['identified']?exists/> 

	
		<#if hasFieldErrors>
			<div class="wwerr">
				<#list fieldErrors['identified'] as error>
				    <div class="errorMessage">${error}</div><#t/>
				</#list>
			</div><#t/>
		</#if>
		<span id="wwlbl_identified" class="wwlbl">
			<label for="identified" class="label">
				<@s.text name="indicator.required" /> <@s.text cssClass="requiredField" name="label.identified" />:
			</label>
		</span> 
	
	

		<span id="wwctrl_identified" class="wwctrl">	
		<@s.textfield name="identified" size="15" id="identified" title="${sessionUser.displayDateFormat}" cssClass="dates" theme="simple" />
		</span>
		<img src="images/icons/FieldID_CALENDAR-CHECK-normal.png" border="0" id="identifiedTrigger">
		<script type="text/javascript">
		  Calendar.setup(
		    {
		      inputField  : "identified",         // ID of the input field
		      ifFormat    : "${Session.sessionUser.otherDateFormat}",    // the date format
		      button      : "identifiedTrigger"       // ID of the button
		    }
		  );
		</script>	
	</div>
</div>

<#if !securityGuard.integrationEnabled>
<div class="formRowHolder">
	<@s.textfield id="nonIntegrationOrderNumber" key="label.ordernumber" name="nonIntegrationOrderNumber" labelposition="left"/>
</div>
</#if>

<@s.iterator value="extentions" id="extention" status="stat" >
	<#if ( stat.index % 2) == 0 >
		<div class="formRowHolder">
	</#if>	
		<@s.hidden name="productExtentionValues[${stat.index}].extensionId" labelposition="left" />
		<@s.hidden name="productExtentionValues[${stat.index}].uniqueID" labelposition="left" />
		<@s.textfield key="${extentions[stat.index].extensionLabel}" name="productExtentionValues[${stat.index}].value" labelposition="left" />
	<#if (stat.index % 2) == 1 >
		</div>
	</#if>
</@s.iterator>
<#if (extentions.size() % 2) == 1 >
	</div>
</#if>


<div class="formRowHolder">
	<@s.select id="productType" key="label.producttype" labelposition="left" name="productTypeId" onchange="updateProductType(this)">
		<#include "/templates/html/common/_productTypeOptions.ftl"/>
	</@s.select>
	<div class="updating" id="productTypeIndicator">
		<img src="<@s.url value="/images/indicator_mozilla_blu.gif" />" alt="<@s.text name="updating"/>" />
	</div>
</div>	

<#include "_infoOptions.ftl">

<div class="formRowHolder">
	<@s.textarea id="comments" key="label.comments" name="comments" labelposition="left"/>
	<@s.select name="commentTemplate" list="commentTemplates" listKey="id" listValue="name" emptyOption="true" onchange="changeComments(this)"/>
</div>

<#include "_fileAttachment.ftl"/>
