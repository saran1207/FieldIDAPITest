<#if uniqueId?exists> 
	<#if uniqueId lt 0 >
		<#assign id=-1 * uniqueId />
	<#else>
		<#assign id=uniqueId />
	</#if>
</#if>
$$('#serialNumberRow_${id!} .serialNumberStatus').each( function(element) { element.update('')});
$('serialNumberRow_${id!}').removeClassName('warning');