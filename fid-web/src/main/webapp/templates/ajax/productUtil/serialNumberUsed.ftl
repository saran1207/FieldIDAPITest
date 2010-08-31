<#if uniqueId?exists> 
	<#if uniqueId lt 0 >
		<#assign id=-1 * uniqueId />
	<#else>
		<#assign id=uniqueId />
	</#if>
</#if>
<#assign html>
	<span class="warning"><@s.text name="label.serial_number_in_use"/></span>
</#assign>
$$('#serialNumberRow_${id!} .serialNumberStatus').each( function(element) { element.update('${html?js_string}')});
$('serialNumberRow_${id!}').addClassName('warning');