<#if uniqueId?exists >
	<#assign id=-1 * uniqueId />
</#if>
$$('#serialNumberRow_${id!} .serialNumberStatus').each( function(element) { element.update('')});
$('serialNumberRow_${id!}').removeClassName('warning');