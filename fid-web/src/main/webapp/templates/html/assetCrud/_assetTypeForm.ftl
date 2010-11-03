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