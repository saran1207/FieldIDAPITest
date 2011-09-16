<div class="assetFormGroup assetTypeSelection">
	<div class="assetTypeLabel">
		<label for="assetTypeId"><@s.text name="label.assettype"/></label>
		<@s.select cssClass="chzn-select" id="assetType" name="assetTypeId" onchange="updateAssetType(this)">
			<#include "/templates/html/common/_assetTypeOptions.ftl"/>
		</@s.select>
		<span class="updating" id="assetTypeIndicator">
			<img src="<@s.url value="/images/indicator_mozilla_blu.gif" />" alt="<@s.text name="updating"/>" />
		</span>	
	</div>	
</div>