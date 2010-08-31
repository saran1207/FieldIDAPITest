<#escape x as x?js_string >
	<#assign options>	
		<#include "/templates/html/productCrud/_infoOptions.ftl">
	</#assign>

	<#assign componentTypes>
		<ul>
			<#list productType.subTypes as type > 
				<li>
					${type.name}
				</li>
			</#list>
		</ul>
	</#assign>
	
	updatingProductComplete();
	replacedProductType( "${options}" )
	
</#escape>