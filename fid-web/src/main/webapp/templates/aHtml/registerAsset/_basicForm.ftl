<div id="basicDetails" class="basicForm">
	<div class="formTitle">
		<h3><@s.text name="nav.asset_information" /></h3>
	</div>
	<div class="infoSet">
		<label for="serialNumber" class="label"><@s.text name="label.serialnumber"/> <#include "/templates/html/common/_requiredMarker.ftl"/></label>
		<@s.textfield name="serialNumber"/>
	</div>
	<div class="infoSet">
		<label for="rfidNumber" class="label"><@s.text name="label.rfidnumber"/></label>
		<@s.textfield name="rfidNumber"/>
	</div>
	<div class="infoSet">
		<label for="customerRefNumber" class="label"><@s.text name="label.referencenumber"/></label>
		<@s.textfield name="customerRefNumber"/>
	</div>

	<div class="infoSet">
		<label for="productTypeId" class="label"><@s.text name="label.producttype"/> <#include "/templates/html/common/_requiredMarker.ftl"/></label>
		<@s.select id="productType" name="productTypeId" onchange="updateProductType(this);showSuggestedAttributes();">
			<#include "/templates/html/common/_productTypeOptions.ftl"/>
		</@s.select>
		<span class="fieldHolder updating" id="productTypeIndicator">
			<img src="<@s.url value="/images/indicator_mozilla_blu.gif" />" alt="<@s.text name="updating"/>" />
		</span>	
	</div>
		
	<div class="infoSet">
		<label class="label" for="asset.location"><@s.text name="label.location"/></label>
		<#if !parentProduct?exists >
			<div class="fieldHolder">
				<@n4.location name="asset.location" id="location" nodesList=helper.predefinedLocationTree fullName="${helper.getFullNameOfLocation(asset.location)}"  theme="simple"/>
			</div>
		<#else>
			<span class="fieldHolder" id="advancedLocation">${(helper.getFullNameOfLocation(asset.location))?html}</span>
		</#if>
	</div>
	<@s.hidden name="linkedProductId" value="${linkedProduct.id}"/>
</div>
