<#if uniqueId?exists && uniqueId lt 0 >
	<#assign id=-1 * uniqueId />
</#if>
<#assign html>
	<span class="warning"><@s.text name="label.serial_number_in_use"/></span>
</#assign>
$$('#serialNumberRow_${id!} .serialNumberStatus').each( function(element) { element.update('${html?js_string}')});
$('serialNumberRow_${id!}').addClassName('warning');