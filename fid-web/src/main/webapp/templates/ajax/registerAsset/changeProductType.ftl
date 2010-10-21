<#escape x as x?js_string >
	<#assign options>	
		<#include "/templates/aHtml/registerAsset/_infoOptions.ftl">
	</#assign>

	<#assign componentTypes>
		<ul>
			<#list assetType.subTypes as type >
				<li>
					${type.name}
				</li>
			</#list>
		</ul>
	</#assign>
	
	updatingProductComplete();
	replacedProductType( "${options}" )
	
</#escape>