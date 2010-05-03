
${action.setPageType('inspection', 'add')!}

<div id="inspectionType">${inspectionType.name}</div>

<button ><@s.text name="label.save_all"/></button>

<ul id="assets">
	<#list assets as asset>
		<li id="asset_${asset.id}"><span>${asset.serialNumber}</span> <span class="status"><@s.text name="label.not_sent"/></span></li> 
	</#list>
</ul>