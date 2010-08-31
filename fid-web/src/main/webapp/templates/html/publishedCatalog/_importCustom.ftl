<div class="customSelection">
	<h3><@s.text name="label.asset_types"/></h3>
	<#list publishedProductTypes as productType>
		<div  class="customSelectionType">
			<@s.checkbox name="importProductTypeIds['${productType.id}']" />
			<label>${productType.name?html}</label>
		</div>
	</#list>
</div>

<div class="customSelection">
	<h3><@s.text name="label.event_types"/></h3>
	<#list publishedInspectionTypes as inspectionType>
		<div class="customSelectionType">
			<@s.checkbox name="importInspectionTypeIds['${inspectionType.id}']" />
			<label>${inspectionType.name?html}</label>
		</div>
	</#list>
</div>