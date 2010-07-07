<head>
	<#include "/templates/html/common/_orgPicker.ftl"/>
	<#include "/templates/html/common/_columnView.ftl"/>
</head>



<#if securityGuard.assignedToEnabled >
	<div class="infoSet">
		<label class="label" for="assigneduser"><@s.text name="label.assignedto"/></label>
		<#if !parentProduct?exists >
			<@s.select  name="assignedUser" list="employees" listKey="id" listValue="displayName" headerKey="0" headerValue="${action.getText('label.unassigned')}" />
		<#else>
			<span class="fieldHolder" id="assignedUser">${(product.assignedUser.userLabel)!}</span>
		</#if>
	</div>
</#if>
<div class="infoSet">
	<label class="label" for="owner"><@s.text name="label.owner"/></label>
	<#if !parentProduct?exists >
		<@n4.orgPicker name="owner" theme="fieldid" required="true"/>
	<#else>
		<span class="fieldHolder" id="owner">${(product.owner.name?html)!}</span>
	</#if>
</div>




<div class="infoSet">
	<label class="label" for="location"><@s.text name="label.location"/></label>
	<#if !parentProduct?exists >
		<#if helper.hasPredefinedLocationTree()>
			
			<div class="fieldHolder">
				<@s.hidden name="advancedLocation" id="advancedLocation"/>
				<@s.hidden name="location" id="location"/>
				<@s.textfield id="locationName" name="locate" theme="simple" value="${helper.getFullNameOfLocation(product.advancedLocation)}" readonly="true"/>
				<a href="#" id="showLocationSelection"><@s.text name="label.choose"/></a>
				<div id="locationSelection">
					<label for="predefinedLocation"><@s.text name="label.predefined_location"/></label><br/>
					<@n4.dynamicLocation id="advancedLocationSelection" nodesList=helper.predefinedLocationTree name="advancedLocation" />
					<label for="freeformLocation"><@s.text name="label.freeform_location"/></label><br/>
					<@s.textfield id="freeform" name="freeformLocation" value="${product.advancedLocation.freeformLocation!}" theme="simple"/>
					<div>
					<input type="button" name="getactive" value="<@s.text name="label.select_location"/>" id="advancedLocationSelection_getactive"/>
					<@s.text name="label.or"/>
 					<a href="#" id="advancedLocationSelection_close"><@s.text name="label.cancel"/></a>
					</div>
				</div>
				<@n4.includeScript>
					onDocumentLoad(function() {
							$('advancedLocationSelection_getactive').observe('click', function(event) {
								event.stop();
								$$('#advancedLocationSelection a.active').each(function(element) {
										var predefinedLocationId = element.getAttribute("nodeId");
										var name = element.getAttribute("nodeName");
										$('advancedLocation').value = predefinedLocationId;
										$('location').value = $('freeform').getValue();
										$('locationName').value = name + " " + $('freeform').getValue();
										$('locationSelection').hide();
									});
								
							});
							$('advancedLocationSelection_close').observe('click', function(event) { event.stop(); $('locationSelection').hide();
								$('freeform').value = $('location').getValue();
								var predefinedLocationId = $('advancedLocation').getValue(); 
								jQuery("advancedLocationSelection").selectNode(predefinedLocationId, 'advancedLocationSelection');
							});
							$('showLocationSelection').observe('click', function(event) {
									event.stop();
									$('locationSelection').show();
									translate($('locationSelection'), $('showLocationSelection'), 0, 0);
								});
						});
				</@n4.includeScript>
			</div>
		<#else>
			<@s.textfield id="location" name="location" />
		</#if>
	<#else>
		<span class="fieldHolder" id="advancedLocation">${(helper.getFullNameOfLocation(product.advancedLocation))?html}</span>
	</#if>
</div>

<div class="infoSet">
	<label for="productStatus" class="label"><@s.text name="label.productstatus"/></label>
	<#if !parentProduct?exists >
		<@s.select name="productStatus" list="productStatuses" listKey="uniqueID" listValue="name" emptyOption="true"  />
	<#else>
		<span class="fieldHolder" id="productStatus">${(product.productStatus.name?html)!}</span>
	</#if>		
</div>

<div class="infoSet">
	<label for="purchaseOrder" class="label"><@s.text name="label.purchaseorder"/></label>
	<@s.textfield  name="purchaseOrder" />	
</div>

<div class="infoSet">
	<label for="" class="label"><@s.text name="label.identified"/> <#include "../common/_requiredMarker.ftl"/></label>
	<@s.datetimepicker id="identified" name="identified" type="dateTime"/>
</div>

<#if !securityGuard.integrationEnabled>
	<div class="infoSet">
		<label for="nonIntegrationOrderNumber" class="label"><@s.text name="label.ordernumber"/></label>
		<@s.textfield id="nonIntegrationOrderNumber" name="nonIntegrationOrderNumber" />	
	</div>

	
</#if>

<@s.iterator value="extentions" id="extention" status="stat" >
	<div class="infoSet">
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
		<@s.select id="commentTemplateSelection" name="commentTemplate" list="commentTemplates" listKey="id" listValue="displayName" emptyOption="true" onchange="changeComments(this)" theme="fieldidSimple"/><br/>
		<@s.textarea id="comments"  name="comments" theme="fieldidSimple"/>
	</span>
</div>

<#include "_fileAttachment.ftl"/>
