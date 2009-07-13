<div class="projectAsset " id="asset_${asset.id}">
	<span class="projectAssetSerial"><a href="<@s.url action="product" namespace="/" uniqueID="${asset.id}"/>">${asset.serialNumber?html}</a></span>
	<span class="projectAssetType">${(asset.type.name?html)!}</span>
	<#if sessionUser.hasAccess( "managejobs" ) >
		<span class="projectAssetRemove"><a href="<@s.url action="jobAssetDelete" namespace="/" projectId="${project.id}" uniqueID="${asset.id}"/>" assetId="${asset.id}" class="removeAssetLink"><img alt="x" src="<@s.url value="/images/x.gif"/>" assetId="${asset.id}"/></a></span>
	</#if>
</div>