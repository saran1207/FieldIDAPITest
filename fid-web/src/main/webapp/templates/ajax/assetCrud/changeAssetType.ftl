<#escape x as x?js_string >
	<#assign options>	
		<#include "/templates/html/assetCrud/_infoOptions.ftl">
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
	
	updatingAssetComplete();
	replacedAssetType( "${options}" )
	
</#escape>