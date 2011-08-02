<#if uniqueId?exists> 
	<#if uniqueId lt 0 >
		<#assign id=-1 * uniqueId />
	<#else>
		<#assign id=uniqueId />
	</#if>
</#if>
$$('#identifierRow_${id!} .identifierStatus').each( function(element) { element.update('')});
$('identifierRow_${id!}').removeClassName('warning');