<#list publishedAssetTypeGroups as group >
	<label>${group.name}</label>
	<ul class="catalogPackage">
		<#list action.getPublishedAssetTypesForGroup(group.name) as assetType>
			<li>
				<@s.checkbox name="importAssetTypeIds['${assetType.id}']" /> <span class="catalogItem">${assetType.name?html}</span>
			</li>
		</#list>
	</ul>
</#list>
<label><@s.text name="label.ungrouped"/></label>
	<ul class="catalogPackage">
		<#list publishedAssetTypesUngrouped as assetType>
			<li>
				<@s.checkbox name="importAssetTypeIds['${assetType.id}']" /> <span class="catalogItem">${assetType.name?html}</span>
			</li>
		</#list>
	</ul>