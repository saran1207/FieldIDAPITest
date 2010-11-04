<div class="customSelection">
	<h3><@s.text name="label.asset_types"/></h3>
	<#list publishedAssetTypes as assetType>
		<div  class="customSelectionType">
			<@s.checkbox name="importAssetTypeIds['${assetType.id}']" />
			<label>${assetType.name?html}</label>
		</div>
	</#list>
</div>

<div class="customSelection">
	<h3><@s.text name="label.event_types"/></h3>
	<#list publishedEventTypes as eventType>
		<div class="customSelectionType">
			<@s.checkbox name="importEventTypeIds['${eventType.id}']" />
			<label>${eventType.name?html}</label>
		</div>
	</#list>
</div>