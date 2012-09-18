<div id="basicDetails" class="basicForm">
	<div class="formTitle">
		<h3><@s.text name="nav.asset_information" /></h3>
	</div>
	<div class="infoSet">
		<label for="identifier" class="label"><@s.text name="label.id_number"/> <#include "/templates/html/common/_requiredMarker.ftl"/></label>
		<@s.textfield name="identifier"/>
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
		<label for="assetTypeId" class="label"><@s.text name="label.assettype"/> <#include "/templates/html/common/_requiredMarker.ftl"/></label>
		<@s.select id="assetType" name="assetTypeId" onchange="updateAssetType(this);showSuggestedAttributes();">
			<#include "/templates/html/common/_assetTypeOptions.ftl"/>
		</@s.select>
		<span class="fieldHolder updating" id="assetTypeIndicator">
			<img src="<@s.url value="/images/indicator_mozilla_blu.gif" />" alt="<@s.text name="updating"/>" />
		</span>	
	</div>
		
	<div class="infoSet">
		<label class="label" for="asset.location"><@s.text name="label.location"/></label>
        <div class="fieldHolder">
            <@n4.location name="assetWebModel.location" id="location" nodesList=predefinedLocationTree fullName="${helper.getFullNameOfLocation(assetWebModel.location)}"  theme="simple"/>
        </div>
	</div>
	<@s.hidden name="linkedAssetId" value="${linkedAsset.id}"/>
</div>
